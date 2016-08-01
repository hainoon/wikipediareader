/**
 * 
 */
package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import exceptions.AbsatzExistiertNichtException;
import exceptions.FehlerhafterSatzException;
import exceptions.FehlerhaftesWortException;
import exceptions.KapitelExistiertNichtException;
import exceptions.SatzExistiertNichtException;
import exceptions.WortExistiertNichtException;
import javafx.util.Duration;

/**
 * Ermittel aus dem Modell relevante (Zeit-)Werte 
 * 
 * @author marcel
 *
 */
public interface Navigation {

	// TODO double zeit -> Duration zeit
	// ==================== KAPITEL STEUERUNG ================================

	/**
	 * Gibt den Startzeitwert des vorherigen Kapitels zurück
	 * 
	 * @param index
	 *            Wortindex
	 * @return Zeitwert in Sekunden für den Startwert des vorherigen Kapitels
	 * 
	 * @throws FehlerhaftesWortException
	 * @throws WortExistiertNichtException
	 * 
	 */
	public Duration getStartWertVorherigesKapitel(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Gibt den Startzeitwert des naechsten Kapitels zurück
	 * 
	 * @param index
	 *            Wortindex
	 * @return Zeitwert in Sekunden für den Startwert des nächsten Kapitels
	 * 
	 * @throws FehlerhaftesWortException
	 * @throws WortExistiertNichtException
	 * 
	 */
	public Duration getStartWertNaechstesKapitel(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Gibt den Zeitwert des Kapitelanfangs zurück
	 * 
	 * @param index
	 *            Wortindex
	 * @return Zeitwert in Sekunden für den Startwert des aktuellen Kapitels
	 * @throws KapitelExistiertNichtException
	 * @throws FehlerhaftesWortException
	 * @throws WortExistiertNichtException
	 */
	public Duration getKapitelanfangVonWort(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Liefert den Zeitwert des ersten Worts des x-ten Kapitels
	 * 
	 * @param index
	 *            Wortindex
	 * @return Zeitwert in Sekunden für den Startwert des x-ten Kapitels
	 * @throws WortExistiertNichtException
	 * @throws FehlerhaftesWortException
	 */
	public Duration getKapitelX(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Liefert für einen Wortindex x zurück im wievielten Kapitel sich das Wort
	 * befindet.
	 * 
	 * @param x
	 *            Wortindex
	 * @return Kapitelnummer für Wort x
	 * @throws KapitelExistiertNichtException
	 * @throws FehlerhaftesWortException
	 * @throws WortExistiertNichtException
	 */
	public int getKapitel(int x)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Gibt die Kapitelanzahl des Artikels zurück
	 * 
	 * @return Kapitelanzahl im Artikel
	 * @throws WortExistiertNichtException 
	 */
	public int getKapitelanzahlInArtikel() throws WortExistiertNichtException;

	// ==================== ABSATZ STEUERUNG ===================================

	/**
	 * Gibt den Startzeitwert des nächsten Absatzes zurück
	 * 
	 * @param index
	 *            Wortindex
	 * @return Zeitwert in Sekunden für den Anfang des nächsten Absatzes
	 * @throws WortExistiertNichtException
	 * @throws AbsatzExistiertNichtException
	 * @throws FehlerhaftesWortException
	 * 
	 * 
	 */
	public Duration getStartwertNaechsterAbsatz(int index)
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Gibt den Startzeitwert des vorherigen Absatzes zurück
	 * 
	 * @param index
	 *            Wortindex
	 * @return Zeitwert in Sekunden für den Anfang des vorherigen Absatzes
	 * 
	 * @throws WortExistiertNichtException
	 * @throws AbsatzExistiertNichtException
	 * @throws FehlerhaftesWortException
	 */
	public Duration getStartwertVorherigerAbsatz(int index)
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Gibt den Zeitwert des Absatzanfangs zurück
	 * 
	 * @param index
	 *            Wortindex
	 * @return Zeitwert in Sekunden für den Anfang des aktuellen Absatzes
	 * @throws WortExistiertNichtException
	 * @throws AbsatzExistiertNichtException
	 * @throws FehlerhaftesWortException
	 */
	public Duration getAbsatzzanfangVonWort(int index)
			throws AbsatzExistiertNichtException, WortExistiertNichtException, FehlerhaftesWortException;

	/**
	 * Liefert den x-ten Absatz
	 * 
	 * @param index
	 *            Wortindex
	 * @return Zeitwert des Anfangs des x-ten Absatz
	 * @throws WortExistiertNichtException
	 * @throws AbsatzExistiertNichtException
	 * @throws FehlerhaftesWortException
	 */
	public Duration getAbsatzX(int index)
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Liefert für einen Wortindex x zurück im wievielten Absatz sich das Wort
	 * befindet.
	 * 
	 * @param index
	 *            Wortindex
	 * @return Absatznummer für Wortindex
	 * @throws WortExistiertNichtException
	 * @throws AbsatzExistiertNichtException
	 */
	public int getAbsatz(int index) throws AbsatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Gibt die Absatzanzahl des Artikels zurück
	 * 
	 * @return Absatzanzahl im Artikel
	 */
	public int getAbsatzanzahlInArtikel();

	// ====================== SATZ STEUERUNG ===================================

	/**
	 * Gibt den Startzeitwert des nächsten Satzes zurück
	 * 
	 * @param index
	 *            Wortindex
	 * @return Startzeit des nächsten Satz
	 * @throws WortExistiertNichtException
	 * @throws SatzExistiertNichtException
	 * @throws FehlerhafterSatzException
	 * @throws FehlerhaftesWortException
	 * 
	 * 
	 */
	public Duration getStartwertNaechsterSatz(int index) throws FehlerhaftesWortException, FehlerhafterSatzException,
			SatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Gibt den Startzeitwert des vorherigen Satzes zurück
	 * 
	 * @param index
	 *            Wortindex
	 * @return Startzeit des vorherigen Satz
	 * @throws FehlerhafterSatzException
	 * @throws WortExistiertNichtException
	 * @throws SatzExistiertNichtException
	 * @throws FehlerhaftesWortException
	 * 
	 * 
	 */
	public Duration getStartwertVorherigerSatz(int index) throws FehlerhaftesWortException, SatzExistiertNichtException,
			WortExistiertNichtException, FehlerhafterSatzException;

	/**
	 * Gibt den Zeitwert des Satzanfangs zurück
	 * 
	 * @param index
	 *            Wortindex
	 * @return Startzeit des aktuellen Satz
	 * @throws WortExistiertNichtException
	 * @throws SatzExistiertNichtException
	 * @throws FehlerhaftesWortException
	 * @throws FehlerhafterSatzException
	 * 
	 */
	public Duration getSatzanfangVonWort(int index) throws FehlerhaftesWortException, SatzExistiertNichtException,
			WortExistiertNichtException, FehlerhafterSatzException;

	/**
	 * Liefert den x-ten Satz
	 * 
	 * @param index
	 *            Wortindex
	 * @return Startzeit des x-ten Satz
	 * @throws WortExistiertNichtException
	 * @throws SatzExistiertNichtException
	 * @throws FehlerhafterSatzException
	 * @throws FehlerhaftesWortException
	 */
	public Duration getSatzX(int index) throws FehlerhaftesWortException, FehlerhafterSatzException,
			SatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Liefert für einen Wortindex x zurück im wievielten Satz sich das Wort
	 * befindet.
	 * 
	 * @param index
	 *            Wortindex
	 * @return Satznummer für Wort
	 * @throws WortExistiertNichtException
	 * @throws SatzExistiertNichtException
	 * @throws FehlerhaftesWortException
	 * 
	 */
	public int getSatz(int index)
			throws FehlerhaftesWortException, SatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Gibt die Satzanzahl des Artikels zurück
	 * 
	 * @return Satzanzahl
	 */
	public int getSatzanzahlInArtikel();

	// ====================== WORT STEUERUNG ===================================

	/**
	 * Gibt den Startzeitwert des Wortes mit Index i zurück
	 * 
	 * @param index
	 *            Wortindex
	 * @return Startzeit des Wort
	 * @throws WortExistiertNichtException
	 * @throws FehlerhaftesWortException
	 */
	public Duration getStartwertWort(int index) throws FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Gibt den Startwert für ein Wort hinter Wort i
	 * 
	 * @param index
	 *            Wortindex
	 * @return Startzeit des nächsten Wort
	 * @throws WortExistiertNichtException
	 * @throws FehlerhaftesWortException
	 */
	public Duration getStartwertNaechstesWort(int index) throws FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Gibt den Startwert für ein Wort vor Wort i
	 * 
	 * @param index
	 *            Wortindex
	 * @return Startzeit des vorherigen Wort
	 * @throws WortExistiertNichtException
	 * @throws FehlerhaftesWortException
	 */
	public Duration getStartwertVorherigesWort(int index) throws FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Liefert das Wort an Index index
	 * 
	 * @param index
	 *            Wortindex
	 * @return Inhalt des Wort
	 * @throws FehlerhaftesWortException
	 * @throws WortExistiertNichtException
	 */
	public String getWort(int index) throws WortExistiertNichtException, FehlerhaftesWortException;

	/**
	 * Gibt die Wortanzahl im Artikel zurück
	 * 
	 * @return Wortanzahl im Artikel
	 */
	public int getWortanzahlInArtikel();

	// ================== SONSTIGES STEUERUNG ==================================

	/**
	 * Liefert den Index wo das Wort als nächstes auftritt.
	 * 
	 * @param wort
	 *            Suchbegriff
	 * @param index
	 *            Wortindex
	 * @return Index des Auftretens des Suchbegriffs
	 * @throws WortExistiertNichtException
	 * @throws FehlerhaftesWortException
	 */
	public int findeWort_vorwaerts(String wort, int index)
			throws FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Liefert den Index wo das Wort zuvor auftrat.
	 * 
	 * @param wort
	 *            Suchbegriff
	 * @param index
	 *            Wortindex
	 * @return Index des Auftretens des Suchbegriffs
	 * @throws WortExistiertNichtException
	 * @throws FehlerhaftesWortException
	 */
	public int findeWort_rueckwaerts(String wort, int index)
			throws FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Ermittelt aus einer gegebenen Zeit die Position/ Index des Wortes
	 * 
	 * @param zeit
	 *            Zeitwert in Sekunden
	 * @return Zu Zeitwert zugehöriger Wortindex
	 * 
	 * @throws FehlerhaftesWortException
	 * @throws WortExistiertNichtException
	 */
	public int getWortIndexAusZeit(Duration zeit) throws WortExistiertNichtException, FehlerhaftesWortException;

	/**
	 * Gibt die Zeitdauer des Artikels zurück
	 * 
	 * @return Zeitdauer des Artikels in Sekunden
	 */
	public Duration getZeitdauerArtikel();

	/**
	 * Links des Artikels
	 * 
	 * @return Wortindex + Wortinhalt der Links
	 * @throws IOException
	 */
	public TreeMap<Integer, String> getLinks() throws IOException;

	/**
	 * Inhaltsverzeichnis
	 * 
	 * @return Liste von Kapitelbezeichner des Inhaltsverzeichnisses
	 */
	public ArrayList<String> getInhaltsverzeichnis();

}
