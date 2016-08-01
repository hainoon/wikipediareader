package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.TreeMap;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import exceptions.AbsatzExistiertNichtException;
import exceptions.FehlerhafterSatzException;
import exceptions.FehlerhaftesWortException;
import exceptions.KapitelExistiertNichtException;
import exceptions.SatzExistiertNichtException;
import exceptions.WortExistiertNichtException;
import javafx.util.Duration;

/**
 * @author marcel
 *
 */
public class Read_Model_Impl extends Observable implements Read_Model {

	private Document _d;

	/**
	 * Konstruktor
	 * 
	 * @param d
	 *            Dokument aus dem gelesen werden soll
	 * @param html
	 *            Parser für die ursprüngliche HTML-Datei
	 */
	public Read_Model_Impl(Document d, ModelLadefortschritt fortschritt) {
		this.addObserver(fortschritt);
		_d = d;
		setChanged();
		notifyObservers(Statusmeldungen.Reader);
	}

	// ============================ KAPITEL ===================================

	@Override
	public Elements getKapitelElemente() throws WortExistiertNichtException {
		// return d.getElementsByTag("h[1-3]"); // TODO
		// Elements ungefiltertesInhaltsverzeichnis = _d.select("h1,h2,h3");
		// Elements inhaltsverzeichnis = new Elements();
		// for(Element element : ungefiltertesInhaltsverzeichnis){
		// if(element.getElementsByTag("word").size() > 0){
		// inhaltsverzeichnis.add(element);
		// }
		// }
		// return inhaltsverzeichnis;
		Elements inhaltsverzeichnis = _d.select("inhaltsverzeichnis");
		Elements kapitelelemente = new Elements();
		for (Element element : inhaltsverzeichnis.select("li")) {
			if (element.hasAttr("index")) {
				try {
					int index = Integer.parseInt(element.attr("index"));
					kapitelelemente.add(getWortElement(index));
				} catch (Exception e) { // TODO wieder entfernen
					int index = Integer.parseInt(element.attr("index"));
					System.err.println("Wort nicht gefunden: " + index);
				}
			}
		}
		return kapitelelemente;
	}

	@Override
	public Element getKapitelElement(int index) throws KapitelExistiertNichtException, WortExistiertNichtException {
		Elements kapitel = getKapitelElemente();
		if (gueltigerKapitelIndex(index, kapitel)) {
			// if (index == 0) {
			// try {
			// return getWortElement(0);
			// } catch (WortExistiertNichtException e) {
			// e.printStackTrace();
			// } // TODO wegen Inhaltsverzeichnis bzw. Inhalt vor erster
			// // Überschrift
			// }
			return kapitel.get(index);
		} else {
			throw new KapitelExistiertNichtException();
		}
	}

	/**
	 * Prüft ob es sich um einen gültigen Kapitelindex handelt
	 * 
	 * @param index
	 *            Kapitelnummer
	 * @param kapitel
	 *            Kapitelmenge
	 * @return Kapitel existiert
	 */
	private boolean gueltigerKapitelIndex(int index, Elements kapitel) {
		return index >= 0 && index < kapitel.size();
	}

	@Override
	public Duration getKapitelStartzeit(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		Element kapitel = getKapitelElement(index);
		Elements ele = kapitel.select("word");
		if (ele.size() == 0) {
			throw new WortExistiertNichtException("leerer Kapitelbezeichner");
		}
		if (ele.first().hasAttr("start")) {
			return new Duration(Double.parseDouble(ele.first().attr("start")));
		} else {
			throw new FehlerhaftesWortException("start fehlt");
		}
	}

	@Override
	public int getKapitelNummerFuerWortIndex(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		Elements kapitel = getKapitelElemente();
//		Element kapitel1 = kapitel.get(0);
//		int startindexKapitel1 = getIndex(kapitel1);
//		if (index < startindexKapitel1) {
//			return -1; // TODO Inhaltsverzeichnis / Präambel besser als Kapitel
//						// in der Datenstruktur einbauen
//		}
		for (int i = 0; i < kapitel.size() - 1; ++i) {
			// TODO i = 0 ->
			// Inhaltsverzeichnis
			int startKapitel1 = getIndex(kapitel.get(i));
			int startKapitel2 = getIndex(kapitel.get(i + 1));

			if (index >= startKapitel1 && index < startKapitel2) {
				return i;
			}
		}
		throw new KapitelExistiertNichtException("Kein Kapitel gefunden. Wortindex = " + index);
	}

	/**
	 * Ermittelt den Index des ersten Worts im Kapitel
	 * 
	 * @param kapitel
	 *            Kapitelmenge
	 * @return Index des ersten Worts im Kapitel
	 * @throws FehlerhaftesWortException
	 *             benötigte Attribute des Worts fehlen
	 * @throws KapitelExistiertNichtException
	 *             fehlerhaftes Kapitel
	 */
	private int getIndex(Element kapitel) throws FehlerhaftesWortException, KapitelExistiertNichtException {
		Elements kW = kapitel.select("word");
		if (kW.size() == 0) {
			throw new KapitelExistiertNichtException("leeres Kapitel");
		} else {
			Element k = kW.first();
			if (k.hasAttr("index")) {
				return Integer.parseInt(k.attr("index"));
			} else {
				throw new FehlerhaftesWortException("index fehlt");
			}
		}
	}
	// ============================ ABSATZ ====================================

	@Override
	public Elements getAbsaetzeElemente() {
		Elements absaetze = _d.children().select("p");
		return absaetze;
	}

	@Override
	public Element getAbsatzElement(int index) throws AbsatzExistiertNichtException {
		if (gueltigerAbsatzindex(index)) {
			return getAbsaetzeElemente().get(index);
		} else {
			throw new AbsatzExistiertNichtException();
		}
	}

	/**
	 * Prüft ob es sich um eine gültige Absatznummer handelt
	 * 
	 * @param index
	 *            Absatznummer
	 * @return gültige Absatznummer
	 */
	private boolean gueltigerAbsatzindex(int index) {
		return index >= 0 && index < getAbsaetzeElemente().size();
	}

	@Override
	public Duration getAbsatzStartzeit(int index)
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException {
		Elements wortM = getAbsatzElement(index).select("word");
		if (wortM.size() == 0 && getAbsatzElement(index).select("sentence").size() > 0) {
			// Workaround: Wenn beim Absatz die Struktur mit
			// <p><sentence><h123> beginnt
			// enthält er komischerweise keine <word> trotzdem dies in der
			// Datenstruktur der Fall ist
			Element satz= getAbsatzElement(index).select("sentence").first();
			return getWortStartzeit(Integer.parseInt(satz.attr("start_word")));

		}
		Element erstesWortImAbsatz = wortM.first();
		if (erstesWortImAbsatz.hasAttr("start")) {
			return new Duration(Double.parseDouble(erstesWortImAbsatz.attr("start")));
		} else {
			throw new FehlerhaftesWortException("start fehlt");
		}
//		throw new WortExistiertNichtException("leerer Absatz, Wortindex: " + index);
		// TODO

	}

	@Override
	public int getAbsatzNummerFuerWortIndex(int index)
			throws AbsatzExistiertNichtException, WortExistiertNichtException {
		Elements wortM = _d.select("word[index=" + index + "]");
		if (wortM.size() != 1) {
			throw new WortExistiertNichtException("Ungültige Wortanzahl");
		}
		Element wort = wortM.first();
		Elements absaetze = getAbsaetzeElemente();
		for (int i = 0; i < absaetze.size(); ++i) {
			Elements woerter = absaetze.get(i).select("word");
			if (woerter.contains(wort)) {
				return i;
			}
		}
		for (int i = absaetze.size() - 1; i >= 0; --i) {
			Element absatz = absaetze.get(i);
			if (absatz.select("sentence").size() > 0) {
				// Workaround: Wenn beim Absatz die Struktur mit
				// <p><sentence><h123> beginnt
				// enthält er komischerweise keine <word> trotzdem dies in der
				// Datenstruktur der Fall ist
				int startwort = Integer.parseInt(absatz.select("sentence").first().attr("start_word"));
				if (index >= startwort) {
					// erster Absatz mit einem Startindex kleiner dem Wortindex
					return i;
				}
			}
		}
		throw new AbsatzExistiertNichtException("Kein Absatz gefunden. Wortindex = " + index);
	}
	// ============================ SATZ ======================================

	@Override
	public Elements getSaetzeElemente() {
		return _d.getElementsByTag("sentence");
	}

	@Override
	public Elements getSatzElement(int index) throws SatzExistiertNichtException {
		if (gueltigerSatzindex(index)) {
			Elements satz = getSaetzeElemente().select("sentence[index=" + index + "]");
			if (satz.size() != 1) {
				throw new SatzExistiertNichtException("Ungültige Satzanzahl");
			}
			return satz;
		} else {
			throw new SatzExistiertNichtException("Ungültiger Satzindex");
		}
	}

	/**
	 * Prüft ob es sich um einer gültige Satznummer handelt
	 * 
	 * @param index
	 *            Satznummer
	 * @return gültige Satznummer
	 */
	private boolean gueltigerSatzindex(int index) {
		return index >= 0 && index < getSaetzeElemente().size();
	}

	@Override
	public String getSatz(int index) throws SatzExistiertNichtException {
		// return getSatzElement(index).text();

		Elements es = getSatzElement(index).first().children();
		return es.text();
		// for(Element e : es){
		// ergebnis += e.text()+" ";
		// }
		// return ergebnis;
	}

	@Override
	public Duration getSatzStartzeit(int index) throws FehlerhaftesWortException, FehlerhafterSatzException,
			SatzExistiertNichtException, WortExistiertNichtException {
		Elements satz = getSatzElement(index);
		if (satz.hasAttr("start_word")) {
			return getWortStartzeit(Integer.parseInt(satz.attr("start_word")));
		} else {
			throw new FehlerhafterSatzException("start_word fehlt");
		}
	}

	@Override
	public int getSatzNummerFuerWortIndex(int wortindex)
			throws FehlerhaftesWortException, SatzExistiertNichtException, WortExistiertNichtException {
		Element wort = getWortElement(wortindex);
		Elements saetze = getSaetzeElemente();
		for (Element satz : saetze) {
			if (satz.select("word").contains(wort) || Integer.parseInt(satz.attr("start_word")) == wortindex) {
				if (satz.hasAttr("index")) {
					return Integer.parseInt(satz.attr("index"));
				} else {
					throw new FehlerhaftesWortException("index fehlt");
				}
			}
		}
		throw new SatzExistiertNichtException("Kein Satz gefunden. Wortindex = " + wortindex);
	}

	// ============================ WORT ======================================

	@Override
	public Elements getWoerterElements() {
		return _d.getElementsByTag("word");
	}

	@Override
	public Element getWortElement(int index) throws WortExistiertNichtException {
		if (gueltigerWortIndex(index)) {
//			Elements wort = _d.getElementsByTag("word").select("[index=" + index + "]");
			Elements wort = _d.select("word[index=\""+index+"\"");
			if (wort.size() == 1) {
				return wort.first();
			} else {
				throw new WortExistiertNichtException("Ungültige Wortanzahl");
			}
		} else {
			throw new WortExistiertNichtException("Ungültiger Index");
		}
	}

	/**
	 * Gültiger Wortindex
	 * 
	 * @param index
	 *            Wortnummer
	 * @return gültiger Wortindex
	 */
	private boolean gueltigerWortIndex(int index) {
		return index >= 0 && index < _d.getElementsByTag("word").size();
	}

	@Override
	public String getWort(int index) throws WortExistiertNichtException, FehlerhaftesWortException {
		if (gueltigerWortIndex(index)) {
			Element wort = getWortElement(index);
			if (wort.hasAttr("normalized")) {
				String w = wort.attr("normalized").replaceAll("\"", "");
				// Da bei Aufruf von normalized die Anführungszeichen
				// mitgenommen
				// werden
				return w;
			} else {
				throw new FehlerhaftesWortException("normalized fehlt");
			}
		} else {
			throw new WortExistiertNichtException("Ungültiger Wortindex");
		}
	}

	@Override
	public Duration getWortStartzeit(int index) throws FehlerhaftesWortException, WortExistiertNichtException {
		Element wort = getWortElement(index);
		if (wort.hasAttr("start")) {
			return new Duration(Double.parseDouble(wort.attr("start")));
		} else {
			throw new FehlerhaftesWortException("start fehlt");
		}
	}

	@Override
	public int getWortIndexAusZeit(Duration zeit) throws WortExistiertNichtException, FehlerhaftesWortException {
		Duration startzeitErstesWort = startZeitErstesWort();
		if (zeit.toMillis() >= 0.0 && zeit.toMillis() < startzeitErstesWort.toMillis()) {
			// Sonderfall, dass das erste Wort nicht bei 0.0 sec anfängt
			return 0;
		}
		for (Element wort : getWoerterElements()) {
			if (wort.hasAttr("start") && wort.hasAttr("stop")) {
				double startzeit = Integer.parseInt(wort.attr("start"));
				double stopzeit = Integer.parseInt(wort.attr("stop"));
				if (zeit.toMillis() >= startzeit && zeit.toMillis() < stopzeit) {
					return Integer.parseInt(wort.attr("index"));
				} else if (zeit.toMillis() < startzeit) {
					// Sonderfall dass zwischen der Stopzeit von Wort 1 und der
					// Startzeit von Wort 2 ein undefiniertes Intervall besteht
					return Integer.parseInt(wort.attr("index")) - 1;
				}
			} else {
				throw new FehlerhaftesWortException("start oder stop fehlt");
			}
		}
		throw new WortExistiertNichtException();
	}

	/**
	 * Startzeit des ersten Worts
	 * 
	 * @return Startzeit des ersten Worts
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             Wort enthält nicht alle benötigten Attribute
	 */
	private Duration startZeitErstesWort() throws WortExistiertNichtException, FehlerhaftesWortException {
		Element erstesWort = getWortElement(0);
		Duration startzeitErstesWort;
		if (erstesWort.hasAttr("start")) {
			startzeitErstesWort = new Duration(Double.parseDouble(erstesWort.attr("start")));
		} else {
			throw new FehlerhaftesWortException("start fehlt");
		}
		return startzeitErstesWort;
	}

	// ============================ SONSTIGES ================================

	@Override
	public TreeMap<Integer, String> getLinks() throws IOException { // TODO
		assert _d != null && _d.childNodeSize() > 0 : "nicht initialisiert";
		Elements links = _d.getElementsByTag("a");
		TreeMap<Integer, String> linkmenge = new TreeMap<Integer, String>();
		for (Element e : links) {
			if (e.childNodeSize() > 0) {
				if (e.child(0).tagName().equals("word")) {
					int index = Integer.parseInt(e.child(0).attr("index"));
					String text = e.text();
					linkmenge.put(index, text);
				}
			}
		}
		return linkmenge;
	}

	@Override
	public ArrayList<String> getInhaltsverzeichnis() { // TODO
		Elements inhalt = _d.select("inhaltsverzeichnis").select("a");
		ArrayList<String> ergebnis = new ArrayList<String>();
		for (Element e : inhalt) {
			ergebnis.add(e.text());
		}
		return ergebnis;
	}

	@Override
	public String getText() {
		return _d.text();
	}

	@Override
	public Elements getLinkElement() { // TODO
		return _d.getElementsByTag("a");
	}

	@Override
	public int findeWort_vorwaerts(String wort, int index)
			throws FehlerhaftesWortException, WortExistiertNichtException {
		Elements woerter = getWoerterElements();
		Elements match = woerter.select(":contains(" + wort + ")");
		for (Element e : match) {
			if (e.hasAttr("index")) {
				int indexMatch = Integer.parseInt(e.attr("index"));
				if (indexMatch > index) {
					return indexMatch;
				}
			} else {
				throw new FehlerhaftesWortException("index fehlt");
			}
		}
		throw new WortExistiertNichtException();
	}

	@Override
	public int findeWort_rueckwaerts(String wort, int index)
			throws FehlerhaftesWortException, WortExistiertNichtException {
		Elements woerter = getWoerterElements();
		Elements match = woerter.select(":contains(" + wort + ")");
		for (int i = match.size() - 1; i > 0; --i) {
			Element e = match.get(i);
			if (e.hasAttr("index")) {
				int indexMatch = Integer.parseInt(e.attr("index"));
				if (indexMatch > index) {
					return indexMatch;
				}
			} else {
				throw new FehlerhaftesWortException("index fehlt");
			}
		}
		throw new WortExistiertNichtException();
	}

}