/**
 * 
 */
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import exceptions.ArtikelExistiertNichtException;
import exceptions.EinstellungsException;
import exceptions.FehlerhafterSatzException;
import exceptions.FehlerhaftesWortException;
import exceptions.InhaltsverzeichnisExistiertNichtException;
import exceptions.MatchingException;
import exceptions.SatzExistiertNichtException;
import exceptions.TextstelleNichtGefundenException;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import player.InhaltsverzeichnisSprachsynthese;

/**
 * Neue Datenstruktur --> well, what exactly does this class do?
 * 
 * @author marcel
 *
 */
public class Write_Model extends Observable {

	// TODO Parallelisieren
	private Document _datenstrukur;

	private Html_Parser _html;
	private String _artikel;

	private JsonArray words;
	private JsonArray json_word;
	private JsonArray json_sentence;
	private int satzNummer;

	private ArrayList<String> html_word;

	private int counterWortanzahl;
	private int counterGesamt;
	private Element zwischenspeicherUeberschrift; // TODO sauberer

	private final File _datenOrdner;
	private File _wikiDatei;
	private File _audioOgg;
	private File _timing;

	private String sprache;

	private ModelLadefortschritt _fortschritt;

	private enum Aenderung {
		ERGAENZEN, LOESCHEN, ERSETZEN, BEIBEHALTEN;
	}

	/**
	 * Konstruktor
	 * 
	 * @param artikel
	 * @param erstellungerlaubt
	 *            Generierung eigener Korpusdateien erlaubt
	 * @param sprache2
	 * @throws TransformerFactoryConfigurationError
	 * @throws Exception
	 */
	public Write_Model(String artikel, ModelLadefortschritt fortschritt, boolean erstellungerlaubt, String sprache2)
			throws TransformerFactoryConfigurationError, Exception {
		sprache = sprache2;
		_artikel = artikel;
		_fortschritt = fortschritt;
		counterWortanzahl = 0;
		counterGesamt = 0;
		this.addObserver(_fortschritt);
		_datenOrdner = erstelleDatenordner(sprache);
		if (datenstrukturBereitsImSpeicher()) {
			System.out.println("Daten liegen bereits vor");
			// TODO
			_fortschritt.setAnzahlHauptschritte(1);
		} else if (benötigterInputBereitsImKorpus()) {
			// TODO Fall das auf mehrere ogg geteilt berücksichtigen -> merge
			_fortschritt.setAnzahlHauptschritte(1);// TODO sauberer
			File wav;
			if (sprache.equals("Englisch")) {
				wav = new File("english/" + _artikel + "/audio.wav");
			} else {
				wav = new File("german-2/" + _artikel + "/audio.wav");
			}
			if (!wav.exists()) {
				konvertiereAudioDatei();
			} else {
				FileUtils.copyFile(wav, new File(_datenOrdner.toPath() + "/audio.wav"));
			}
		} else {
			if (erstellungerlaubt) {
				erstelleBenoetigteInputDateien(fortschritt);
			} else {
				throw new EinstellungsException("Download / Sprachsynthese ausgeschlossen!");
			}
		}
	}

	private File erstelleDatenordner(String sprache2) {
		File datenOrdner = new File("Artikel/" + sprache2 + "/" + _artikel + "/");
		if (!datenOrdner.exists()) {
			datenOrdner.mkdirs();
		}
		return datenOrdner;
	}

	private void erstelleInhaltsverzeichnisWav() throws MaryConfigurationException, IOException, SynthesisException {
		InhaltsverzeichnisSprachsynthese.inhaltsverzeichnisSprachsynthese(getInhaltsverzeichnisEintraege(), _datenOrdner.getPath(), sprache);
	}

	public ArrayList<String> getInhaltsverzeichnisEintraege() { // TODO
		Elements linksInhaltsverzeichnis = _datenstrukur.select("inhaltsverzeichnis").select("a");
		ArrayList<String> inhaltsverzeichnisEintraege = new ArrayList<String>();
		for (Element link : linksInhaltsverzeichnis) {
			inhaltsverzeichnisEintraege.add(link.text());
		}
		return inhaltsverzeichnisEintraege;
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws FehlerhaftesWortException
	 * @throws FehlerhafterSatzException
	 * @throws MaryConfigurationException
	 * @throws SynthesisException
	 * @throws MatchingException
	 * @throws InhaltsverzeichnisExistiertNichtException
	 */
	public void start() throws FileNotFoundException, IOException, FehlerhaftesWortException, FehlerhafterSatzException,
			MaryConfigurationException, SynthesisException, MatchingException,
			InhaltsverzeichnisExistiertNichtException {
		if (!datenstrukturBereitsImSpeicher()) {
			// TODO Parallellisieren
			bereiteHtmlAuf();
			setChanged();
			notifyObservers("HTML vorbereitet");
			bereiteJsonAuf();
			setChanged();
			notifyObservers("Json vorbereitet");
			erstelle();
			setChanged();
			notifyObservers("Datenstruktur erstellt");
		} else {
			File input = new File(_datenOrdner.getPath() + "/datenstruktur.html");
			_datenstrukur = Jsoup.parse(input, "UTF-8", "");
		}
	}

	public Document getDocument() {
		return _datenstrukur;
	}

	/**
	 * Erstellt die neue Datenstruktur
	 * 
	 * @throws IOException
	 * @throws FehlerhaftesWortException
	 * @throws FehlerhafterSatzException
	 * @throws MaryConfigurationException
	 * @throws SynthesisException
	 * @throws MatchingException
	 * @throws InhaltsverzeichnisExistiertNichtException
	 * 
	 * @throws Exception
	 */
	private void erstelle()
			throws IOException, FehlerhaftesWortException, FehlerhafterSatzException, MaryConfigurationException,
			SynthesisException, MatchingException, InhaltsverzeichnisExistiertNichtException {

		// initialisiere
		Elements htmlInput = _html.getRelevantenHtmlTeil();
		Element datenstrukturErgebnis = new Element(Tag.valueOf("html"), "");
		_datenstrukur = new Document("");
		Document datenStrukurZwischenergebnis = new Document("");
		satzNummer = 0;

		// lese Input
		ArrayList<Node> htmlInput2TagListe = htmlInput2TagListe(htmlInput);
		String[] htmlString = getHtmlStrings();
		String[] jsonString = getJsonStrings();
		setChanged();
		notifyObservers("Input ausgelesen");

		// ermittel Abweichungen zwischen den Inputs ->
		// Änderungs-/Anpassungsbedarf
		ArrayList<Aenderung> aenderungen = backtrace(levenshtein(jsonString, htmlString));
		setChanged();
		notifyObservers("Levenstein");

		// erstelle <word>-Tags für die einzelenen Wörter
		List<Node> wordTags = erstelleWordTags(htmlInput2TagListe, aenderungen, 0);
		datenStrukurZwischenergebnis.insertChildren(0, wordTags);
		final File f2 = new File(_datenOrdner.toPath() + "/datenstrukturOhneSätze.html");
		FileUtils.writeStringToFile(f2, datenStrukurZwischenergebnis.outerHtml(), "UTF-8");
		setChanged();
		notifyObservers("Wörter erstellt");

		// erstelle <inhaltsverzeichnis>-Tag
		Element inhaltsverzeichnisTag = erstelleInhaltsverzeichnisTag(datenStrukurZwischenergebnis);
		datenstrukturErgebnis.appendChild(inhaltsverzeichnisTag);
		setChanged();
		notifyObservers("Inhaltsverzeichnis erstellt");

		// versehe die <word>-Tags mit zugehörigen <sentence>-Tags
		for (Node aktuellerTag : wordTags) {
			Element satzTags;
			if (!leererTag(aktuellerTag)) {
				satzTags = erstelleSatzTags(aktuellerTag.clone());
				if (satzTags != null) { // TODO
					datenstrukturErgebnis.appendChild(satzTags);
				}
			}
		}
		_datenstrukur.insertChildren(0, datenstrukturErgebnis.childNodes());
		final File f = new File(_datenOrdner.toPath() + "/datenstruktur.html");
		FileUtils.writeStringToFile(f, _datenstrukur.outerHtml(), "UTF-8");
		setChanged();
		notifyObservers("Sätze erstellt");

		// synthetisere das Inhaltsverzeichnis
		erstelleInhaltsverzeichnisWav();
		setChanged();
		notifyObservers("Audio Inhaltsverzeichnis");
	}

	private ArrayList<Node> htmlInput2TagListe(Elements htmlInput) {
		ArrayList<Node> htmlInput2TagListe = new ArrayList<>();
		for (Node aktuellerTag : htmlInput) {
			htmlInput2TagListe.add(aktuellerTag);
		}
		return htmlInput2TagListe;
	}

	private String[] getJsonStrings() {
		String[] jsonString = new String[json_word.size()];
		for (int i = 0; i < json_word.size(); ++i) {
			JsonElement je = json_word.get(i);
			jsonString[i] = je.getAsJsonObject().get("normalized").getAsString();
		}
		return jsonString;
	}

	private String[] getHtmlStrings() {
		String[] htmlString = new String[html_word.size()];
		for (int i = 0; i < html_word.size(); ++i) {
			htmlString[i] = html_word.get(i);
		}
		return htmlString;
	}

	private Element erstelleInhaltsverzeichnisTag(Document d2) throws InhaltsverzeichnisExistiertNichtException {
		Element inhaltsverzeichnis = new Element(Tag.valueOf("inhaltsverzeichnis"), "");

		Elements ueberschriften = ueberschriftenDatenstruktur(d2);

		// Vergleich des Inhaltsverzeichnis aus der html mit den Überschriften
		// aus der vorläufigen Datenstruktur. Wenn Überschrift in beiden
		// vorhanden, dann wird diese ins neue Inhaltsverzeichnis übernommen.
		// Etwas umständlich, allerdings sicherer Weg nicht mehr benötigte
		// Inhaltsverzeichniseinträge zu löschen und trotzdem zugleich für den
		// Inhaltsverezichniseintrag den zugehörigen Wortindex zu bekommen
		if (hatInhaltsverzeichnisHTML()) {
			Elements inhaltsverzeichnisHtml = _html.getInhaltsverzeichnisTag().select("li");
			ArrayList<Element> zuLoeschen = new ArrayList<>();
			// während der for-Schleife Löschen geht leider nicht
			for (Element eintragInhaltsverzeichnis : inhaltsverzeichnisHtml) {
				if (eintragInhaltsverzeichnis.select("a").hasAttr("href")) {
					String id = eintragInhaltsverzeichnis.select("a").attr("href");
					try {
						Element textstelle = getTextstelle(d2, id);
						eintragInhaltsverzeichnis.attr("index", textstelle.select("word").first().attr("index"));

					} catch (Exception e) {
						try {
							String indexTextstelle = ermittelInhaltsverzeichnisEintragAusUeberschrift(ueberschriften,
									eintragInhaltsverzeichnis);
							eintragInhaltsverzeichnis.attr("index", indexTextstelle);
						} catch (Exception e1) {
							System.err.println("Es konnte keine Textstelle für den Inhaltsverzeichniseintrag "
									+ eintragInhaltsverzeichnis.text() + " gefunden werden | "
									+ inhaltsverzeichnisHtml.indexOf(eintragInhaltsverzeichnis));
							zuLoeschen.add(eintragInhaltsverzeichnis);

						}
					}
				}
			}
			//zuLoeschen.forEach(e -> inhalthaltsverzeichnisHtml.remove(e));
			for (Element e : zuLoeschen) {
				inhaltsverzeichnisHtml.remove(e);
			}
			inhaltsverzeichnis.append(
					"<li class=\"toclevel-1 tocsection-1\" index=\"0\"><a href=\"#\"><span class=\"tocnumber\">0</span> <span class=\"toctext\">Präambel</span></a></li>");
			for (Element e : inhaltsverzeichnisHtml) {
				inhaltsverzeichnis.appendChild(e);
			}
			//inhaltsverzeichnisHtml.forEach(e -> inhaltsverzeichnis.appendChild(e));
		}

		return inhaltsverzeichnis;
	}

	private String ermittelInhaltsverzeichnisEintragAusUeberschrift(Elements ueberschriften,
			Element eintragInhaltsverzeichnis) throws TextstelleNichtGefundenException {
		String indexTextstelle = "";
		for (Element alternativ : ueberschriften) {
			String strin1 = alternativ.text();
			String string2 = eintragInhaltsverzeichnis.select("span[class=\"toctext\"]").text().replaceAll(" ", "");
			if (strin1.equals(string2)) {
				indexTextstelle = alternativ.select("word").first().attr("index");
			}
		}
		if (!indexTextstelle.equals("")) {
			return indexTextstelle;
		}
		throw new TextstelleNichtGefundenException();
	}

	private Element getTextstelle(Document d2, String id) throws TextstelleNichtGefundenException {
		Element textstelle = d2.select("span" + id).first();
		if (textstelle != null && textstelle.select("word").size() > 0) {
			return textstelle;
		}

		throw new TextstelleNichtGefundenException();
	}

	private Elements ueberschriftenDatenstruktur(Document d2) {
		Elements ungefiltertesInhaltsverzeichnis = d2.select("h1,h2,h3");
		Elements in2 = new Elements();
		for (Element element : ungefiltertesInhaltsverzeichnis) {
			if (element.getElementsByTag("word").size() > 0) {
				in2.add(element);
			}
		}
		return in2;
	}

	private boolean hatInhaltsverzeichnisHTML() {
		try {
			return _html.getInhaltsverzeichnisTag() != null;
		} catch (InhaltsverzeichnisExistiertNichtException e) {
			return false;
		}
	}

	private boolean hatInhaltsverzeichnisDatenstruktur() {
		return _datenstrukur.select("inhaltsverzeichnis").size() == 1;
	}

	private static int minimum2(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	/**
	 * Berechnet die Levenshtein-Matrix und gibt die Levenshtein-Distanz aus.
	 * 
	 * https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/
	 * Levenshtein_distance#Java
	 * 
	 * @param wortliste1
	 * @param wortliste2
	 * @return
	 */
	private int[][] levenshtein(String[] wortliste1, String[] wortliste2) {
		int[][] distance = new int[wortliste1.length + 1][wortliste2.length + 1];

		for (int positionWortliste1 = 0; positionWortliste1 <= wortliste1.length; positionWortliste1++) {
			distance[positionWortliste1][0] = positionWortliste1;
		}
		for (int positionWortliste2 = 1; positionWortliste2 <= wortliste2.length; positionWortliste2++) {
			distance[0][positionWortliste2] = positionWortliste2;
		}

		for (int positionWortliste1 = 1; positionWortliste1 <= wortliste1.length; positionWortliste1++) {
			for (int positionWortliste2 = 1; positionWortliste2 <= wortliste2.length; positionWortliste2++) {
				distance[positionWortliste1][positionWortliste2] = minimum2(
						distance[positionWortliste1 - 1][positionWortliste2] + 1,
						distance[positionWortliste1][positionWortliste2 - 1] + 1,
						distance[positionWortliste1 - 1][positionWortliste2 - 1]
								+ ((wortliste1[positionWortliste1 - 1].equals(wortliste2[positionWortliste2 - 1])) ? 0
										: 1));
			}
		}
		return distance;

	}

	/**
	 * Erstellt aus der "Levenshtein-Matrix" eine Liste an "Aktionen" die für
	 * ein Matching notwendig sind.
	 * 
	 * @param levenshtein
	 *            Levenshtein-matrix
	 * @return Liste an Änderungen
	 */
	private ArrayList<Aenderung> backtrace(int[][] levenshtein) {

		// levenshtein[y-Koordinate][x-Koordinate]
		ArrayList<Aenderung> result = new ArrayList<Aenderung>();
		int aktuellX = levenshtein[levenshtein.length - 1].length - 1;

		// beginne unten rechts in Matrix und mache so lange weiter bis in
		// oberster Zeile angekommen
		for (int aktuellY = levenshtein.length - 1; aktuellY > 0; --aktuellY) {
			int aktuellePosition = levenshtein[aktuellY][aktuellX];
			int oberhalb = levenshtein[aktuellY - 1][aktuellX];
			int obenlinks = levenshtein[aktuellY - 1][Math.max(0, aktuellX - 1)];
			int links = levenshtein[aktuellY][Math.max(0, aktuellX - 1)];
			while (links < aktuellePosition && aktuellX - 1 != -1) {
				result.add(Aenderung.LOESCHEN);
				--aktuellX;
				aktuellePosition = levenshtein[aktuellY][aktuellX];
				oberhalb = levenshtein[aktuellY - 1][aktuellX];
				obenlinks = levenshtein[aktuellY - 1][Math.max(0, aktuellX - 1)];
				links = levenshtein[aktuellY][Math.max(0, aktuellX - 1)];
			}
			if (oberhalb < aktuellePosition) {
				result.add(Aenderung.ERGAENZEN);
			} else if (obenlinks < aktuellePosition) {
				result.add(Aenderung.ERSETZEN);
				--aktuellX;
			} else {
				result.add(Aenderung.BEIBEHALTEN);
				--aktuellX;
			}
		}
		Collections.reverse(result);
		return result;
	}

	/**
	 * Erstellt die Tags in der neuen Datenstruktur (bis auf Satztags) //TODO
	 * Satztags integrieren
	 * 
	 * @param aktuellerKnoten
	 * @param aenderungen
	 * @param aktuelleBaumtiefe
	 * @return
	 * @throws FehlerhaftesWortException
	 * @throws MatchingException
	 */
	private List<Node> erstelleWordTags(List<Node> aktuellerKnoten, ArrayList<Aenderung> aenderungen,
			int aktuelleBaumtiefe) throws FehlerhaftesWortException, MatchingException {
		List<Node> inhaltAktuellerKindknoten = new ArrayList<Node>();
		for (Node aktuellerKindknoten : aktuellerKnoten) {
			if (hatKindknoten(aktuellerKindknoten)) {
				// wenn der aktuelle Knoten Kindknoten hat, dann führe tag() mit
				// Kindknoten aus -> Führt insgesamt zu DFS-Verhalten
				++aktuelleBaumtiefe;
				List<Node> eNeu2 = erstelleWordTags(aktuellerKindknoten.childNodes(), aenderungen, aktuelleBaumtiefe);
				--aktuelleBaumtiefe;
				tagKorrekur(aktuellerKnoten, aenderungen, aktuellerKindknoten, eNeu2); // TODO
				Element el = new Element(((Element) aktuellerKindknoten).tag(), aktuellerKindknoten.baseUri(),
						aktuellerKindknoten.attributes());
				for (Node n : eNeu2) {
					if (n != null) { // TODO verhindern dass es überhaupt null
										// wird
						el.appendChild(n);
					} else {
						System.out.println("tag = null, Index: " + eNeu2.indexOf(n));
						// TODO
					}
				}
				inhaltAktuellerKindknoten.add(el);
			} else {
				ArrayList<String> text = getWoerterAusTextKnoten(aktuellerKindknoten);

				for (String s : text) {
					if (counterGesamt >= aenderungen.size()) {
						break; // TODO sauberer lösen
					}
					Aenderung aenderung = aenderungen.get(counterGesamt);
					boolean hinzufuegen = true;

					while (!(aenderung.equals(Aenderung.BEIBEHALTEN) || aenderung.equals(Aenderung.ERSETZEN))
							&& counterGesamt < aenderungen.size()) {
						try {
							Element input = matchingEintrag(aenderung, counterWortanzahl, s);
							// if (input != null) {
							inhaltAktuellerKindknoten.add(input);
							++counterWortanzahl;
							++counterGesamt;
							// } else {
						} catch (MatchingException me) {
							++counterGesamt;
							hinzufuegen = false;
							break; // TODO sauberer lösen
						}

						if (counterGesamt >= aenderungen.size()) {
							return inhaltAktuellerKindknoten;
						}
						aenderung = aenderungen.get(counterGesamt);
					}
					if (hinzufuegen) {
						inhaltAktuellerKindknoten.add(matchingEintrag(aenderung, counterWortanzahl, s));
						++counterWortanzahl;
						++counterGesamt;
					}
				}
				// Punkt am Ende des Satzes
				if (counterGesamt < aenderungen.size()) {
					Aenderung aenderung = aenderungen.get(counterGesamt);

					while (aenderung.equals(Aenderung.ERGAENZEN) && aktuellerKindknoten.parent() != null
							&& counterGesamt < json_word.size()) {
						if (((Element) aktuellerKindknoten.parent()).tagName().equals("p")) {
							inhaltAktuellerKindknoten.add(matchingEintrag(Aenderung.ERGAENZEN, counterWortanzahl, ""));
							++counterWortanzahl;
							++counterGesamt;
							aenderung = aenderungen.get(counterGesamt);
						} else {
							break;
						}
					}
				}
			}
		}
		postfixKorrektur(aenderungen, inhaltAktuellerKindknoten, aktuelleBaumtiefe);
		return inhaltAktuellerKindknoten;
	}

	/**
	 * Endet ein Absatz mit einer Reihe von Ergänzungen, so werden diese gemäß
	 * des Algorithmus eigentlich erst im nächsten Schritt ergänzt. Ist diese
	 * jedoch für die Erstellung einer Überschrift zuständig, wird diese durch
	 * diese zusätzlichen Informationen verfälscht. Dies soll durch diese
	 * Methode korrigiert werden
	 * 
	 * @param e1
	 * @param aenderungen
	 * @param e
	 * @param eNeu2
	 * @throws MatchingException
	 * @throws FehlerhaftesWortException
	 */
	private void tagKorrekur(List<Node> e1, ArrayList<Aenderung> aenderungen, Node e, List<Node> eNeu2)
			throws FehlerhaftesWortException, MatchingException {
		int nachfolger = e1.indexOf(e) + 1;
		if (nachfolger < e1.size() && counterGesamt < aenderungen.size()) {
			if (e1.get(nachfolger) != null) {
				// Korrektur des Bereichs vor Überschriften
				if (((Element) e1.get(nachfolger).parent()).tagName().contains("h")) {
					Aenderung aenderung = aenderungen.get(counterGesamt);
					while (aenderung.equals(Aenderung.ERGAENZEN) || aenderungen.equals(Aenderung.ERSETZEN)) {
						Element input = matchingEintrag(Aenderung.ERGAENZEN, counterWortanzahl, "");
						if (isUeberschrift(input.text())) {
							break;
							// Abbruch damit Überschriftinhalt nicht noch in
							// p-Tag geschrieben wird
							// TODO
						}
						eNeu2.add(input);
						++counterWortanzahl;
						++counterGesamt;
						aenderung = aenderungen.get(counterGesamt);
					}
				}
				// TODO Fehler die durch Korrektur noch nicht erfasst werden
				// beheben
			}
		}
	}

	private boolean isUeberschrift(String text) {
		ArrayList<String> inhaltsverzeichnis = _html.getInhaltsverzeichnis();
		for (String s : inhaltsverzeichnis) {
			String[] teilS = s.split(" ");
			for (int i = 0; i < teilS.length; ++i) {
				String s1 = teilS[i];
				if (s1.trim().matches(text.trim())) { // TODO
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * Wenn die Folge der Änderung mit Ergänzen endet, wird dies nicht über die
	 * Menge der Strings aus der HTML-Datei abedeckt. Diese Methode korrigiert
	 * dies
	 * 
	 * @param aenderungen
	 * @param eNeu
	 * @throws FehlerhaftesWortException
	 */
	private void postfixKorrektur(ArrayList<Aenderung> aenderungen, List<Node> eNeu, int tiefe)
			throws FehlerhaftesWortException {
		boolean notwendig = postfixKorrekturNotwendig(aenderungen);
		boolean richtigeTiefe = (tiefe == 0 && !_html.htmlTags()) || (tiefe == 1 && _html.htmlTags());
		while (notwendig && richtigeTiefe && counterGesamt < aenderungen.size()) {
			Aenderung aenderung = aenderungen.get(counterGesamt);
			try {
				Element input = matchingEintrag(aenderung, counterWortanzahl, "");
				eNeu.add(input);
				++counterWortanzahl;
				++counterGesamt;
				if (counterGesamt >= aenderungen.size()) {
					return;
				}
				aenderung = aenderungen.get(counterGesamt);
			} catch (MatchingException me) {
				++counterGesamt;
			}
		}
	}

	private boolean postfixKorrekturNotwendig(ArrayList<Aenderung> aenderungen) {
		List<Aenderung> rest = aenderungen.subList(counterGesamt, aenderungen.size());
		if (!rest.contains(Aenderung.BEIBEHALTEN)) {
			return true;
		}
		return false;
	}

	/**
	 * Erstellt ein neues Wortelement für die neue Datenstruktur
	 * 
	 * @param element
	 * @param counterWortanzahl
	 * @param s
	 * @return
	 * @throws FehlerhaftesWortException
	 * @throws MatchingException
	 */
	private Element matchingEintrag(Aenderung element, int counterWortanzahl, String s)
			throws FehlerhaftesWortException, MatchingException {
		switch (element) {
		case LOESCHEN:
			throw new MatchingException("Element aus HTML-Datei das nicht in Datenstruktur benötigt wird");
		case ERGAENZEN:
			return erstelleWordTag(counterWortanzahl, s); // YYY Fall
		case ERSETZEN:
			return erstelleWordTag(counterWortanzahl, s); // OOO Fall

		default:
			return erstelleWordTag(counterWortanzahl, s);
		}
	}

	/**
	 * Holt aus dem aktuellen Textknoten die einzelnen Worte
	 * 
	 * @param e
	 * @return
	 */
	private ArrayList<String> getWoerterAusTextKnoten(Node e) {
		ArrayList<String> ergebnis = new ArrayList<String>();
		try {
			if (e instanceof TextNode) { // TODO catch entfernen
				String t = ((TextNode) e).text();
				String[] text = t.split(" ");
				for (String s : text) {
					ergebnis.add(s);
				}
			} else {
				// TODO beheben
				System.out.println("Textknoten konnte nicht gecastet werden");
			}
		} catch (Exception ex) {
			System.out.println("Textknoten konnte nicht gecastet werden");
			// TODO sauberer lösen
		}
		return ergebnis;
	}

	/**
	 * Ergänzt in der Datei die Satz-Tags
	 * 
	 * @param e
	 * @param eNeu2
	 * @param el
	 * @throws FehlerhaftesWortException
	 * @throws FehlerhafterSatzException
	 * @throws Exception
	 */
	private Element erstelleSatzTags(Node e) throws FehlerhaftesWortException, FehlerhafterSatzException {
		Element el = new Element(((Element) e).tag(), e.baseUri(), e.attributes());
		List<Node> eNeu2 = e.childNodes();
		if (!((Element) e).tagName().contains("h") && tagEnthaeltWoerter((Element) e)
				&& satzNummer < json_sentence.size()) {
			// satzNummer < json_sentence.size() wegen leeren Tags nach
			// eigentlichen Artikel //TODO
			int satzobergrenze = json_sentence.get(satzNummer).getAsJsonObject().get("end_word").getAsInt();
			Element satzEl = new Element(Tag.valueOf("sentence"), "");
			satzEl.attr("start_word", getStart_Word());
			satzEl.attr("end_word", getEnd_Word());
			satzEl.attr("index", satzNummer + "");
			// Füge Überschriften als erstes Element des neuen Satzes ein.
			// Würden Sätze direkt beim Durchlaufen des Überschriften-Tags
			// erstellt werden, würde es zu fehlern kommen, da der Satz am Ende
			// des Überschriftentags (und Tags allgemein) beendet wird
			if (zwischenspeicherUeberschrift != null) {
				satzEl.appendChild(zwischenspeicherUeberschrift);
				zwischenspeicherUeberschrift = null;
				// TODO Ansatz die Überschrift als erstes Element des neuen
				// Satzes (der in einem p-Tag liegt) einzufüggen verfälscht
				// geringfügig die Struktur im Vergleich zur ursprünglichen
				// html: Statt <h>x</h><p>y</p> ist es nun <p><h>x</h>y</p>.
				// Dies ist Notwendig da sonst die Satztags nicht richtig
				// erstellt werden können bzw. die Überschriften nicht ihren
				// zuggehörigen Sätzen zugeordnet werden können. (Wäre dann
				// <h><word index=1>x</word></h><p><sentence start= 1 end =
				// 2><word index=2>y</word></sentence></p>) Sprich
				// Definitionslücke bezüglich der Sätze an der Stelle der
				// Überschriften
			}
			for (Node el1 : eNeu2) {
				boolean inhalt = false;
				boolean leererTag = false;
				// try { // TODO eleganter
				int x = getLetztenWortIndex(el1);
				inhalt = x < satzobergrenze;
				leererTag = x == -1;
				// } catch (Exception ex) {
				// if (ex.getMessage() == null) {
				// ex.printStackTrace();
				// throw ex;
				// } else if (ex.getMessage().equals("kein Inhalt")) {
				//
				// } else {
				// ex.printStackTrace();
				// throw ex; // TODO
				// }
				// }
				if (leererTag) {
					System.out.println("leerer Tag entfernt");
					// TODO funktioniert das bei Inhaltsverzeichnis?
				} else if (inhalt) {
					satzEl.appendChild(el1.clone());
				} else {
					el.appendChild(satzEl);
					++satzNummer;
					satzobergrenze = json_sentence.get(satzNummer).getAsJsonObject().get("end_word").getAsInt();
					satzEl = new Element(Tag.valueOf("sentence"), "");
					satzEl.appendChild(el1.clone());
					satzEl.attr("start_word", getStart_Word());
					satzEl.attr("end_word", getEnd_Word());
					satzEl.attr("index", satzNummer + "");
				}
			}
			++satzNummer;
			el.appendChild(satzEl);
		} else if (((Element) e).tagName().contains("h") && tagEnthaeltWoerter((Element) e)
				&& satzNummer < json_sentence.size()) {
			zwischenspeicherUeberschrift = (Element) e;
			return null; // TODO isÜberschriftException
		} else {
			for (Node el1 : eNeu2) {
				el.appendChild(el1.clone()); // TODO entfernen oder dafür sorgen
												// dass kein else-Fall
			}
		}

		return el;
	}

	/**
	 * Liefert das letzte Wort eines Satzes
	 * 
	 * @return Wortinhalt
	 * @throws FehlerhafterSatzException
	 */
	private String getEnd_Word() throws FehlerhafterSatzException {
		JsonObject sentence = json_sentence.get(satzNummer).getAsJsonObject();
		if (sentence.has("end_word")) {
			return sentence.get("end_word").getAsString();
		} else {
			throw new FehlerhafterSatzException("end_word fehlt");
		}
	}

	/**
	 * Liefert das erste Wort eines Satzes
	 * 
	 * @return Wortinhalt
	 * @throws FehlerhafterSatzException
	 */
	private String getStart_Word() throws FehlerhafterSatzException {
		JsonObject sentence = json_sentence.get(satzNummer).getAsJsonObject();
		if (sentence.has("start_word")) {
			return sentence.get("start_word").getAsString();
		} else {
			throw new FehlerhafterSatzException("start_word fehlt");
		}
	}

	private boolean tagEnthaeltWoerter(Element e) {
		return e.select("word").size() > 0;
	}

	/**
	 * Gibt den Index des letzen Wortes im aktuellen Tag zurück
	 * 
	 * @param el1
	 * @return
	 * @throws FehlerhaftesWortException
	 * @throws Exception
	 */
	private int getLetztenWortIndex(Node el1) throws FehlerhaftesWortException {
		if (hatKindknoten(el1)) {
			int letztesKind = -1;
			int wert = getLetztenWortIndex(el1.childNode(el1.childNodeSize() + letztesKind));
			while (wert == -1 && (el1.childNodeSize() + letztesKind > 0)) {
				// Springe solange zum vorherigen Knoten bis
				// Knoten mit word-Tag
				--letztesKind;
				wert = getLetztenWortIndex(el1.childNode(el1.childNodeSize() + letztesKind));
			}
			return wert;
		} else if (leererTag(el1)) {
			return -1; // für leere Knoten, damit diese
						// übersprungen werden
		} else {
			if (el1.parent().hasAttr("index")) {
				String index = el1.parent().attr("index");
				// if (index.equals("")) {
				// throw new FehlerhaftesWortException("kein Inhalt");
				// }
				return Integer.parseInt(index);
			} else {
				throw new FehlerhaftesWortException("index fehlt");
			}
		}
	}

	private boolean leererTag(Node el1) {
		return !el1.toString().contains("<word") && !((Element) el1.parent()).tagName().equals("word");
	}

	/**
	 * Sagt aus ob der aktuelle Knoten weitere Kindknoten besitzt
	 * 
	 * @param e
	 * @return
	 */
	private boolean hatKindknoten(Node e) {
		return e.childNodeSize() > 0;
	}

	// ======================== Word-Tag erstellen =======================
	/**
	 * Erstellt einen Wort-Tag
	 * 
	 * @param e2
	 * @throws FehlerhaftesWortException
	 */
	private Element erstelleWordTag(int index, String s) throws FehlerhaftesWortException {
		Element e2 = new Element(Tag.valueOf("word"), "");
		e2.attr("index", getIndex(index));
		e2.attr("start", getStart(index));
		e2.attr("stop", getStop(index));
		e2.attr("original", getOriginal(index));
		e2.attr("normalized", getNormalized(index));
		e2.attr("html", s);
		e2.text(getNormalized(index).replaceAll("\"", ""));

		return e2;
	}

	/**
	 * @param index
	 * @return
	 * @throws FehlerhaftesWortException
	 */
	private String getStop(int index) throws FehlerhaftesWortException {
		JsonObject wort = json_word.get(index).getAsJsonObject();
		if (wort.has("stop")) {
			return wort.get("stop").toString();
		} else {
			throw new FehlerhaftesWortException("stop fehlt");
		}
	}

	/**
	 * @param index
	 * @return
	 * @throws FehlerhaftesWortException
	 */
	private String getStart(int index) throws FehlerhaftesWortException {
		JsonObject wort = json_word.get(index).getAsJsonObject();
		if (wort.has("start")) {
			return wort.get("start").toString();
		} else {
			throw new FehlerhaftesWortException("start fehlt");
		}
	}

	/**
	 * @param index
	 * @return
	 * @throws FehlerhaftesWortException
	 */
	private String getIndex(int index) throws FehlerhaftesWortException {
		JsonObject wort = json_word.get(index).getAsJsonObject();
		if (wort.has("index")) {
			return wort.get("index").toString();
		} else {
			throw new FehlerhaftesWortException("index fehlt");
		}
	}

	/**
	 * @param index
	 * @return
	 * @throws FehlerhaftesWortException
	 */
	private String getNormalized(int index) throws FehlerhaftesWortException {
		JsonObject wort = json_word.get(index).getAsJsonObject();
		if (wort.has("normalized")) {
			return wort.get("normalized").toString();
		} else {
			throw new FehlerhaftesWortException("index fehlt");
		}
	}

	/**
	 * @param index
	 * @return
	 * @throws FehlerhaftesWortException
	 */
	private String getOriginal(int index) throws FehlerhaftesWortException {
		JsonObject wort = json_word.get(index).getAsJsonObject();
		if (wort.has("original")) {
			return wort.get("original").toString();
		} else {
			throw new FehlerhaftesWortException("index fehlt");
		}
	}

	// ================== Json-Timing aufbereiten =============================

	// TODO diesen Abschnitt in eigenständige Klasse auslagern

	/**
	 * Bereitet die Json-Struktur auf (falsche Labels werden versucht zu
	 * korrigieren usw.)
	 * 
	 * @throws FileNotFoundException
	 * @throws JsonSyntaxException
	 * @throws JsonIOException
	 */
	private void bereiteJsonAuf() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		// TODO
		JsonParser parser = new JsonParser();
		JsonObject jsonobj = (JsonObject) parser.parse(new FileReader(_timing));
		words = jsonobj.get("words").getAsJsonArray();
		JsonArray sentences = jsonobj.get("sentences").getAsJsonArray();

		json_word = new JsonArray();
		json_sentence = new JsonArray();
		int labelverschiebung = 0;
		for (JsonElement satz : sentences) {
			JsonObject aktuellerSatz = satz.getAsJsonObject();
			int startwert = aktuellerSatz.get("start_word").getAsInt();
			int stopwert = aktuellerSatz.get("end_word").getAsInt();

			JsonObject satzObjekt = new JsonObject();
			satzObjekt.addProperty("start_word", startwert + labelverschiebung);

			for (int wortindex = startwert; wortindex < stopwert; ++wortindex) {
				if (stopwert == sentences.get(sentences.size() - 1).getAsJsonObject().get("end_word").getAsInt()) {
					--stopwert;
					// Korrektur, da in align.data.json das end_word einen index
					// hat der nicht mehr exisitert (letztes wort +1)
				}
				JsonObject aktuellesWort = words.get(wortindex).getAsJsonObject();

				int index = aktuellesWort.get("index").getAsInt(); // TODO
																	// robuster
				String original = aktuellesWort.get("original").getAsString(); // TODO
																				// robuster
				int start = setzeStartwertJsonWord(aktuellesWort);
				int stop = setzeStopwertJsonWord(wortindex, aktuellesWort, start);
				String normalized = aktuellesWort.get("normalized").getAsString(); // TODO
																					// robuster

				if (!(StringUtils.countMatches(normalized, " ") > 2)) {
					JsonObject objW = erstelleJsonWort(labelverschiebung, index, original, start, stop, normalized);
					json_word.add(objW);
				} else {
					labelverschiebung = teileLabel(labelverschiebung, index, start, stop, normalized);
				}
			}
			satzObjekt.addProperty("end_word", stopwert + labelverschiebung);
			json_sentence.add(satzObjekt);
		}
	}

	/**
	 * Erstellt für ein Wort das entsprechende JSON-Objekt
	 * 
	 * @param labelverschiebung
	 * @param index
	 * @param original
	 * @param start
	 * @param stop
	 * @param normalized
	 * @return
	 */
	private JsonObject erstelleJsonWort(int labelverschiebung, int index, String original, int start, int stop,
			String normalized) {
		JsonObject obj = new JsonObject();
		obj.addProperty("index", index + labelverschiebung);
		obj.addProperty("original", original);
		obj.addProperty("start", start);
		obj.addProperty("stop", stop);
		obj.addProperty("normalized", normalized);
		return obj;
	}

	private int setzeStartwertJsonWord(JsonObject aktuellesWort) {
		int start;
		if (aktuellesWort.has("start")) {
			start = aktuellesWort.get("start").getAsInt();
		} else {
			start = json_word.get(json_word.size() - 1).getAsJsonObject().get("stop").getAsInt();
		}
		return start;
	}

	private int setzeStopwertJsonWord(int indexWort, JsonObject aktuellesWort, int start) {
		int stop;
		if (aktuellesWort.has("stop")) {
			stop = aktuellesWort.get("stop").getAsInt();
		} else {
			try {
				int c = 1;
				JsonObject nachfolger = words.get(indexWort + c).getAsJsonObject();
				boolean nachfolgerEnthaeltZeit = nachfolger.has("start");
				while (!nachfolgerEnthaeltZeit) {
					++c;
					nachfolger = words.get(indexWort + c).getAsJsonObject();
					nachfolgerEnthaeltZeit = words.get(indexWort + c).getAsJsonObject().has("start");
				}
				int differenz = nachfolger.get("start").getAsInt() - start;
				stop = start + (differenz / c);
			} catch (IndexOutOfBoundsException ex) {
				stop = -1; // TODO
			}

		}
		return stop;
	}

	/**
	 * Bereinigt falsche Labels in der JSON-Struktur. Wenn in einem Label
	 * fälschlicherweise mehrere Wörter liegen werden diese auf mehrere Labels
	 * verteilt und die Struktur wird entsprechend angepasst
	 * 
	 * @param labelverschiebung
	 * @param index
	 * @param start
	 * @param stop
	 * @param normalized
	 * @return
	 */
	private int teileLabel(int labelverschiebung, int index, int start, int stop, String normalized) {
		String[] s = normalized.split("\\s+");
		int zeitdauer = (int) ((stop - start) / (s.length + 0.0));
		for (int j = 0; j < s.length; ++j) {
			int stopwertWort = (start + ((j + 1) * zeitdauer));
			if (j == s.length - 1) {
				stopwertWort = stop;
			}
			JsonObject obj = erstelleJsonWort(labelverschiebung, (index + j), s[j], (start + (j * zeitdauer)),
					stopwertWort, s[j]);

			json_word.add(obj);
		}
		labelverschiebung = labelverschiebung + s.length - 1;
		return labelverschiebung;
	}

	// ========================== Prüfe Input ==================================

	private boolean datenstrukturBereitsImSpeicher() {
		File artikelAudio = new File(_datenOrdner.getPath() + "/audio.wav");
		File datenstrukur = new File(_datenOrdner.getPath() + "/datenstruktur.html");
		File inhaltsverzeichnisAudio = new File(_datenOrdner.getPath() + "/inhaltsverzeichnis.wav");
		return artikelAudio.exists() && datenstrukur.exists() && inhaltsverzeichnisAudio.exists();
	}

	private boolean benötigterInputBereitsImKorpus() {
		// TODO Fall das auf mehrere ogg geteilt berücksichtigen -> merge
		String input1;
		String input2;
		if (sprache.equals("Deutsch")) {
			input1 = "german-2/";
			input2 = "align/gen/";
		} else {
			input1 = "english/";
			input2 = "align_english/";
		}
		_wikiDatei = new File(input1 + _artikel + "/wiki.html");
		_audioOgg = new File(input1 + _artikel + "/audio.ogg");
		_timing = new File(input2 + _artikel + "/steps/align.data.json");
		File audiowav = new File(input1 + _artikel + "/audio.wav");
		System.out.println(
				_wikiDatei.exists() + " " + _audioOgg.exists() + " " + audiowav.exists() + " " + _timing.exists());
		return _wikiDatei.exists() && (_audioOgg.exists() || audiowav.exists()) && _timing.exists();
	}

	// ========================== Erstelle Input ===============================

	private void erstelleBenoetigteInputDateien(ModelLadefortschritt fortschritt) throws IOException,
			ArtikelExistiertNichtException, TransformerFactoryConfigurationError, Exception, MaryConfigurationException {
		_fortschritt.setAnzahlHauptschritte(5);// TODO sauberer
		downloadVonWiki(sprache);
		_wikiDatei = new File(_datenOrdner.getPath() + "/wiki.html");
		SynthesizeModel sm = new SynthesizeModel(_datenOrdner.toPath().toString() + "/", _wikiDatei, sprache);
		sm.addObserver(_fortschritt);
		sm.run();
		//erstelleTimingInformationen();
		//_timing = new File(_datenOrdner.getPath() + "/align.data.json");
		_timing = sm.getTimingFile();
		sm.getAudioFile();
		
		//ArtikelSprachsynthese sprachsynthese = new ArtikelSprachsynthese("Artikel/"+sprache+"/" + _artikel + "/", sprache);
		// sauberer
		// lösen
		//sprachsynthese.addObserver(_fortschritt); // TODO
		//sprachsynthese.createWavFile();
		fortschritt.setLadevorgangAktiv(false);// TODO
	}

	/**
	 * Lädt, wenn Artikel nicht vorhanden, Artikel von Wikipedia herunter (wenn
	 * dieser durch den eingegebenne Suchbegriff gefunden werdne kann)
	 * 
	 * @param sprache2
	 * 
	 * @param string
	 *            Suchbegriff
	 * @throws IOException
	 * @throws ArtikelExistiertNichtException
	 */// TODO in Modell auslagern
	private void downloadVonWiki(String sprache2) throws IOException, ArtikelExistiertNichtException {
		DownloadVonWiki wikiDownloader = new DownloadVonWiki(sprache2);
		wikiDownloader.addObserver(_fortschritt); // TODO observer
		wikiDownloader.download(_artikel); // TODO einkomentieren
	}

	/* *
	 * Generiert aus xml-Timing-Dateien das Json-Timing
	 * 
	 * @throws TransformerFactoryConfigurationError
	 * @throws Exception
	 * /// TODO in Modell auslagern
	private void erstelleTimingInformationen() throws TransformerFactoryConfigurationError, Exception {
		XmlToJson xml2Json = new XmlToJson(_datenOrdner.toPath().toString() + "/", _wikiDatei, sprache);
		xml2Json.addObserver(_fortschritt); // TODO Observer
		xml2Json.start();
	} */

	/** 
	 * this class is used to combine downloaded OGG files into one WAV file.
	 * it is not used during synthesis
	 * @throws IOException
	 */
	private void konvertiereAudioDatei() throws IOException {
		// TODO observer
		File audioWav = new File(_datenOrdner.toPath() + "/audio.wav");
		ArrayList<String> command = new ArrayList<>();
		command.add("ffmpeg"); // TODO in Readme
		command.add("-i");
		command.add(_audioOgg.toURI().toString());
		command.add(audioWav.getPath());
		new ProcessBuilder(command).start();
		System.out.println("ogg -> wav");
	}

	// ============== Input auf benötigte Javastruktur anpassen ==============

	/**
	 * Gibt eine bereinigte Form (nicht benötigte Wörter werden entfernt) der
	 * HTML-Struktur zurück
	 * 
	 * @return
	 */
	private ArrayList<String> bereiteHtmlAuf() {
		_html = new Html_Parser(_wikiDatei);
		// html_word = _html.getWortliste();
		ArrayList<Node> daten = new ArrayList<Node>();
		for (Node n : _html.getRelevantenHtmlTeil()) {
			daten.add(n);
		}
		html_word = _html.getWortListeRekursiv(daten);
		return _html.getWortliste();
	}

}