package sprachsteuerung;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;

import net.sourceforge.javaflacencoder.FLACFileWriter;

import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.darkprograms.speech.recognizer.Recognizer;

import exceptions.SprachbefehlAufnahmeException;
import exceptions.SprachbefehlAuswertungException;
import exceptions.SprachbefehlException;

/**
 * Anbindung an die Google-Speech-API
 * 
 * Jarvis Speech API Tutorial
 * 
 * Quelle: - https://github.com/lkuza2/java-speech-api
 * 
 * - https://github.com/lkuza2/java-speech-api/wiki/Hello-World
 * 
 * @author marcel
 *
 */
public class GoogleSpeech {
	private File _aufnahme;
	private Microphone _mikrophon;
	private String _api = "AIzaSyBEyYVZVabgqMRprQz7GdNDtekMgTZ3foY"; // Key für
																		// Googleaccount 

	// "Benutzer-Key" / "Projekt-Key"

	/**
	 * Startet die Aufzeichnung eines Sprachbefehls
	 */
	public void sprachbefehlStart() throws SprachbefehlAufnahmeException {
		_mikrophon = new Microphone(FLACFileWriter.FLAC);
		_aufnahme = new File("sprachbefehl.flac");
		try {
			_mikrophon.captureAudioToFile(_aufnahme);
		} catch (LineUnavailableException e) {
			throw new SprachbefehlAufnahmeException(e.getMessage());
		}
		// TODO Aufnahme Logo
		System.out.println("Nehme auf ...");

	}

	/**
	 * Beendet die Aufzeichnung eines Sprachbefehls und gibt diesen als Befehl
	 * zurück
	 * 
	 * @return Befehlsobjekt
	 * @throws SprachbefehlAuswertungException
	 *             Keine Verbindung zu Google
	 * @throws SprachbefehlException
	 *             Ungültiger Befehl
	 */
	public Befehl sprachbefehlEnde() throws SprachbefehlAuswertungException, SprachbefehlException {
		_mikrophon.close();
		System.out.println("Aufnahme beendet");
		// TODO Aufnahme Logo stoppen

		Recognizer recognizer = new Recognizer(Recognizer.Languages.GERMAN, _api);
		String result = "";
		List<String> alternative;
		try {
			int anzahlDerAlternativen = 4;
			GoogleResponse antwort = recognizer.getRecognizedDataForFlac(_aufnahme, anzahlDerAlternativen,
					(int) _mikrophon.getAudioFormat().getSampleRate());
			System.out.println("Google Response: " + antwort.getResponse());
			System.out.println(
					"Google is " + Double.parseDouble(antwort.getConfidence()) * 100 + "% confident in" + " the reply"); // TODO
																															// entfernen
			result = antwort.getResponse();
			System.out.println("Other Possible responses are: ");
			for (String s : antwort.getOtherPossibleResponses()) {
				System.out.println("\t" + s);
			} // TODO entfernen
				// TODO Alternativen verarbeiten
			alternative = antwort.getOtherPossibleResponses();
		} catch (IOException | NullPointerException e) {
			System.out.println("ERROR: Google cannot be contacted");// TODO
																	// entfernen
			throw new SprachbefehlAuswertungException(e.getMessage());
		}
		_aufnahme.deleteOnExit();
		return new Befehl(result, alternative);
	}

}