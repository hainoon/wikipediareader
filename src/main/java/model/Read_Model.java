package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

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
 * Liest Informationen aus der Datenstruktur
 * 
 * @author marcel
 *
 */
public interface Read_Model {
	// ============================ KAPITEL ===================================
	/**
	 * Gibt die Kapitel der neuen Datenstruktur zurück
	 * 
	 * @return Kapitelelemente (h-Tags)
	 * @throws WortExistiertNichtException 
	 */
	public Elements getKapitelElemente() throws WortExistiertNichtException;

	/**
	 * Gibt einen spezielles Kapitel der neuen Datenstruktur zurück
	 * 
	 * @param index
	 *            Kapitelnummer
	 * @return Kapitelelement (h-Tag)
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * @throws WortExistiertNichtException 
	 */
	public Element getKapitelElement(int index) throws KapitelExistiertNichtException, WortExistiertNichtException;

	/**
	 * Gibt die Startzeit des Kapitels zurück
	 * 
	 * @param index
	 *            Kapitelnummer
	 * @return Startzeitwert
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * @throws FehlerhaftesWortException
	 *             Wort enthält nicht alle benötigten Attribute
	 * @throws WortExistiertNichtException
	 *             leerer Kapitelbezeichner
	 */
	public Duration getKapitelStartzeit(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Ermittelt aus dem Wortindex die Kapitelnummer
	 * 
	 * @param index
	 *            Index des Wort
	 * @return Kapitelnummer
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * @throws FehlerhaftesWortException
	 *             Ein zur Berechnung benötigtes Wort enthält nicht alle
	 *             benötigten Attribute
	 * @throws WortExistiertNichtException
	 *             Ungültige Wortanzahl
	 */
	public int getKapitelNummerFuerWortIndex(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException;

	// ============================ ABSATZ ====================================

	/**
	 * Gibt die Absätze der neuen Datenstruktur zurück
	 * 
	 * @return Menge der Absätze (p-Tags)
	 */
	public Elements getAbsaetzeElemente();

	/**
	 * Gibt einen speziellen Absatz der neuen Datenstruktur zurück
	 * 
	 * @param index
	 *            Absatznummer
	 * @return Absatz (p-Tag)
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 */
	public Element getAbsatzElement(int index) throws AbsatzExistiertNichtException;

	/**
	 * Startzeit eines Absatz
	 * 
	 * @param index
	 *            Absatznummer
	 * @return
	 * @throws FehlerhaftesWortException
	 *             Ein zur Berechnung benötigtes Wort enthält nicht alle
	 *             benötigten Attribute
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 */
	public Duration getAbsatzStartzeit(int index)
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Ermittelt aus einem Wortindex die zugehörige Absatznummer
	 * 
	 * @param index
	 *            Wortindex
	 * @return Absatznummer
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 * @throws WortExistiertNichtException
	 *             Ungültiger Wortindex
	 */
	public int getAbsatzNummerFuerWortIndex(int index)
			throws AbsatzExistiertNichtException, WortExistiertNichtException;

	// ============================ SATZ ======================================

	/**
	 * Gibt die Sätze der neuen Datenstruktur zurück
	 * 
	 * @return
	 */
	public Elements getSaetzeElemente();

	/**
	 * Gibt einen speziellen Satz der neuen Datenstruktur zurück
	 * 
	 * @param index
	 *            Satznummer
	 * @return Satz
	 * @throws SatzExistiertNichtException
	 *             AStz existiert nicht
	 */
	public Elements getSatzElement(int index) throws SatzExistiertNichtException;

	/**
	 * Gibt den Textinhalt eines Satzes zurück
	 * 
	 * @param index
	 *            Satznummer
	 * @return Textinhalt eines Satzes
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 */
	public String getSatz(int index) throws SatzExistiertNichtException;

	/**
	 * Gibt die Startzeit eines Satzes zurück
	 * 
	 * @param index
	 *            Satznummer
	 * @return Startzeit eines Satzes
	 * @throws FehlerhaftesWortException
	 *             Wort enthält nicht alle benötigten Attribute
	 * @throws FehlerhafterSatzException
	 *             Satz enthält nicht alle benötigten Attribute
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 */
	public Duration getSatzStartzeit(int index) throws FehlerhaftesWortException, FehlerhafterSatzException,
			SatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Ermittelt für einen Wortindex die zugehörige Satznummer
	 * 
	 * @param wortindex
	 *            Wortindex
	 * @return Satznummer
	 * @throws FehlerhaftesWortException
	 *             Wort enthält nicht alle benötigten Attribute
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 */
	public int getSatzNummerFuerWortIndex(int wortindex)
			throws FehlerhaftesWortException, SatzExistiertNichtException, WortExistiertNichtException;

	// ============================ WORT ======================================

	/**
	 * Gibt die Wörter der neuen Datenstruktur zurück
	 * 
	 * @return Wortmenge
	 */
	public Elements getWoerterElements();

	/**
	 * Gibt einen spezielles Wort der neuen Datenstruktur zurück
	 * 
	 * @param index
	 *            Wortnummer
	 * @return Wort
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 */
	public Element getWortElement(int index) throws WortExistiertNichtException;

	/**
	 * Textinhalt eines Wort
	 * 
	 * @param index
	 *            Wortnummer
	 * @return Textinhalt
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             Wort enthält nicht alle benötigten Attribute
	 */
	public String getWort(int index) throws WortExistiertNichtException, FehlerhaftesWortException;

	/**
	 * Gibt die Startzeit des Worts in Millisekunden zurück
	 * 
	 * @param index
	 *            Wortnummer
	 * @return Startzeit
	 * @throws FehlerhaftesWortException
	 *             Wort enthält nicht alle benötigten Attribute
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 */
	public Duration getWortStartzeit(int index) throws FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Ermittelt aus einem Zeitwert den zugehörigen Wortindex
	 * 
	 * @param zeit
	 *            Zeitwert
	 * @return Wortnummer
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             Wort enthält nicht alle benötigten Attribute
	 */
	public int getWortIndexAusZeit(Duration zeit) throws WortExistiertNichtException, FehlerhaftesWortException;

	// ============================ SONSTIGES ================================

	/**
	 * Gibt Links die in Absätzen sind zurück
	 * 
	 * @return
	 * @throws IOException
	 */
	public TreeMap<Integer, String> getLinks() throws IOException;


	public ArrayList<String> getInhaltsverzeichnis();

	/**
	 * Gibt den reinen Text der neuen Datenstruktur zurück
	 * 
	 * @return
	 */
	public String getText();

	public Elements getLinkElement();

//	public int getGroesse(Elemente element) throws Exception;

	public int findeWort_vorwaerts(String wort, int index)
			throws FehlerhaftesWortException, WortExistiertNichtException;

	public int findeWort_rueckwaerts(String wort, int index)
			throws FehlerhaftesWortException, WortExistiertNichtException;
}
