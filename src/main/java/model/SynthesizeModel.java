package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Observable;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.datatypes.MaryDataType;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AppendableSequenceAudioInputStream;

/** 
 * if aligned audio (wav+json) is not available, query MaryTTS for timings and audio  
 * and write two matching files; based on XmlToJson+ArtikelSprachsynthese
 * 
 * This class is called by Write_Model.
 * 
 * @author timo
 *
 */
public class SynthesizeModel extends Observable {

	final String targetDirectory;
	final WikiDocument wikiDocument;
	final MaryInterface marytts;
	
	JsonObject json;
	AppendableSequenceAudioInputStream audio;
	// the below are used during processing to correctly set timings of later syntheses and to keep track of word/sentence IDs
	int counterSentences = 0;
	int counterWords = 0;
	
	public SynthesizeModel(String targetDirectory, File wikiDatei, String language) throws MaryConfigurationException {
		this.targetDirectory = targetDirectory;
		wikiDocument = new WikiDocument(wikiDatei);
		marytts = setupMary(language);
		json = new JsonObject();	
		json.add("sentences", new JsonArray());
		json.add("words", new JsonArray());

	}

	/** get the resulting timing file; must only be called after run() 
	 * @throws IOException */
	public File getTimingFile() throws IOException {
		File file = new File(targetDirectory + "align.data.json");
		FileWriter writer = new FileWriter(file);
		Gson gson = new Gson();
		writer.write(gson.toJson(json));
		writer.close();
		return file;
	}
	
	public File getAudioFile() throws IOException {
		File file = new File(targetDirectory + "audio.wav");
		AudioSystem.write(audio, AudioFileFormat.Type.WAVE, file);
		return file;
	}

	/** run synthesis of the full article once; the resulting documents
	 * can afterwards be retrieved with getTimingFile() and getAudioFile()  */
	public void run() {
		double sentenceOffset = 0;
		while (wikiDocument.hasMoreSentences()) {
			String sentence = null;
			org.w3c.dom.Document sentDoc = null;
			try {
				sentence = wikiDocument.nextSentence();
				sentDoc = maryttsGenerateXML(sentence);
				AudioInputStream sentAudio = maryttsGenerateAudio(sentDoc);
				appendJSON(sentDoc, sentenceOffset);
				appendAudio(sentAudio);
				sentenceOffset += 1000 * sentAudio.getFrameLength() / sentAudio.getFormat().getFrameRate();
				notifyObservers(Statusmeldungen.WavTeil);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("sentence is \"" + sentence + "\"");
//				System.err.println("sentDoc is " + sentDoc);
			}
		}
		audio.doneAppending();
		notifyObservers(Statusmeldungen.Wav);
	}
	
	private Document maryttsGenerateXML(String sentence) throws SynthesisException {
		marytts.setInputType(MaryDataType.TEXT.name());
		marytts.setOutputType(MaryDataType.REALISED_ACOUSTPARAMS.name());
		return marytts.generateXML(sentence);
	}

	private AudioInputStream maryttsGenerateAudio(Document sentDoc) throws SynthesisException {
		marytts.setInputType(MaryDataType.ACOUSTPARAMS.name());
		marytts.setOutputType(MaryDataType.AUDIO.name());
		return marytts.generateAudio(sentDoc);
	}

	private void appendJSON(org.w3c.dom.Document sentDoc, double satzuebertrag) throws TransformerException {
		org.jsoup.nodes.Document d = fromW3C(sentDoc);
		Elements saetze = d.select("s");
		double previousTime = satzuebertrag;
		JsonArray sentences = (JsonArray) json.get("sentences");
		JsonArray words = (JsonArray) json.get("words");
		for (Element satz : saetze) {
			Elements woerter = satz.select("t");
			int wortanzahlImSatz = woerter.size();
			sentences.add(neuerSatz(counterSentences, counterWords, wortanzahlImSatz));
			++counterSentences;
			for (Element wort : woerter) {
				String original = wort.ownText();
				String normalized = normalize(wort);
				int index = counterWords;
				++counterWords;
				double start;
				double stop;
				if (wort.select("ph").last() != null) {
					stop = Double.parseDouble(wort.select("ph").last().attr("end")) * 1000 + satzuebertrag;
					start = Double.parseDouble(wort.select("ph").first().attr("end")) * 1000 - Double.parseDouble(wort.select("ph").first().attr("d")) + satzuebertrag;
				} else {
					stop = start = previousTime;
				}
				previousTime = stop;
				words.add(neuesWort(index, original, start, stop, normalized));
			}
		}
	}
	
	private org.jsoup.nodes.Document fromW3C(org.w3c.dom.Document from) throws TransformerException {
		// thanks to: http://stackoverflow.com/questions/36799471/converting-w3c-dom-element-to-jsoup-element
		StringWriter writer = new StringWriter();
	    TransformerFactory.newInstance().newTransformer().transform(new DOMSource(from), new StreamResult(writer));
	    String html = writer.toString();
	    // Parse the html string with Jsoup
	    return Jsoup.parse(html); // This is now your Jsoup document object!
	}

	private JsonObject neuesWort(int index, String original, double start, double stop, String normalized) {
		JsonObject ergebnis = new JsonObject();
		ergebnis.addProperty("index", index);
		ergebnis.addProperty("original", original);
		ergebnis.addProperty("start", start);
		ergebnis.addProperty("stop", stop);
		ergebnis.addProperty("normalized", normalized);
		return ergebnis;
	}
	
	private String normalize(Element wort) {
		// TODO richtig normalisieren
		return wort.ownText();
	}
	
	private JsonObject neuerSatz(int counterSentences, int counterWords, int wortanzahlImSatz) {
		JsonObject ergebnis = new JsonObject();
		ergebnis.addProperty("start_word", counterWords);
		ergebnis.addProperty("end_word", counterWords + wortanzahlImSatz);
		ergebnis.addProperty("index", counterSentences);
		return ergebnis;
	}
	

	/** append to our audio stream (create it if it's not yet existing) */
	private void appendAudio(AudioInputStream sentAudio) {
		if (audio != null) {
			audio.append(sentAudio);
		} else {
			audio = new AppendableSequenceAudioInputStream(sentAudio.getFormat(), Collections.singletonList(sentAudio));
		}
	}

	private MaryInterface setupMary(String language) throws MaryConfigurationException {
		Locale l;
		String voice;
		if (language.equals("Deutsch")) {
			System.out.println("Deutsch");
			l = Locale.GERMAN;
			voice = "bits3-hsmm";
		} else {
			System.out.println("Englisch");
			l = Locale.US;
			voice = "cmu-slt-hsmm";
		}
		MaryInterface marytts = new LocalMaryInterface();
		marytts.setLocale(l);
		marytts.setVoice(voice);
		marytts.setInputType(MaryDataType.TEXT.name());
		marytts.setOutputType(MaryDataType.REALISED_ACOUSTPARAMS.name());
		return marytts;
	}
	
	public int processingStepsDone() {
		return wikiDocument.sentIndex;
	}
	
	public int processingStepsTotal() {
		return wikiDocument.sentenceCandidates.size();
	}
	
	/** segment the wiki document into a list of (some sort of) sentences */
	private class WikiDocument {
		
		final Html_Parser parser;
		int sentIndex = 0;
		final ArrayList<String> sentenceCandidates;
		
		public WikiDocument(File wikiDatei) {
			parser = new Html_Parser(wikiDatei);
			ArrayList<String> words = parser.getWortliste();
			sentenceCandidates = new ArrayList<String>();
			String aktuell = "";
			for (int i = 0; i < words.size(); ++i) {
				String wort = words.get(i);
				aktuell += wort + " ";
				boolean satzzeichen = wort.matches(".*[.?!]$") || i == words.size() - 1; // TODO
																							// ggf
																							// besser
				// lösen
				if (satzzeichen) {
					sentenceCandidates.add(aktuell);
					aktuell = "";
				}
				// die eigentlichen Sätze werden später über Marytts ermittelt.
				// Trennung ist hier nur notwendig um mehrere Einzeldateien zu
				// erhalten, da diese nebenläufig weiterverwendet werden können ->
				// performanter als mit einer Datei weiterzuarbeiten
			}
		}

		public String nextSentence() {
			return sentenceCandidates.get(sentIndex++);
		}

		public boolean hasMoreSentences() {
			return sentIndex < sentenceCandidates.size();
		}

	}
	
}
