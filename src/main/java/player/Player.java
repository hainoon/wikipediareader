/**
 * 
 */
package player;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.transform.TransformerFactoryConfigurationError;

import exceptions.AbsatzExistiertNichtException;
import exceptions.ArtikelExistiertNichtException;
import exceptions.AudiodateiExistiertNichtException;
import exceptions.FehlerhafterSatzException;
import exceptions.FehlerhaftesWortException;
import exceptions.KapitelExistiertNichtException;
import exceptions.SatzExistiertNichtException;
import exceptions.WortExistiertNichtException;
import javafx.beans.InvalidationListener;
import javafx.util.Duration;
import marytts.exceptions.MaryConfigurationException;
import model.Log;
import model.ModelPositionsparameter_Impl;
import sprachsteuerung.Richtung;

/**
 * Definiert die Schnittstelle aller Player-Klassen
 * 
 * @author marcel
 * 
 */
public interface Player {

	// ======================= Grundlegende Steuerung ==========================
	/**
	 * Startet die Audioausgabe
	 * 
	 * @throws FehlerhafterSatzException
	 *             fehlerhafte Satzinformationen
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * @throws InterruptedException
	 * @throws MaryConfigurationException
	 * @throws IOException
	 * @throws ArtikelExistiertNichtException
	 *             Artikel existiert nicht
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws AudiodateiExistiertNichtException
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 */
	public void startAudio() throws FehlerhaftesWortException, FehlerhafterSatzException, MaryConfigurationException,
			InterruptedException, ArtikelExistiertNichtException, IOException, WortExistiertNichtException,
			SatzExistiertNichtException, AbsatzExistiertNichtException, KapitelExistiertNichtException,
			AudiodateiExistiertNichtException, TransformerFactoryConfigurationError, Exception;

	/**
	 * Pausiert die Audioausgabe
	 */
	public void pauseAudio();

	/**
	 * Stopt die Audioausgabe
	 * 
	 * @param automatisch
	 *            wurde die Audioausgabe automatisch gestoppt ja / nein
	 *            
	 */
	public void stopAudio(boolean automatisch);

	// ======================= Artikel-Steuerung ===============================

	/**
	 * Startet einen festgelegten Artikel, Datei liegt lokal vor
	 * 
	 * @param text
	 * @param verlauf Artikel in Verlauf aufnehmen
	 *            Artikelname
	 * @throws InterruptedException
	 * @throws MaryConfigurationException
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * @throws IOException
	 * @throws ArtikelExistiertNichtException
	 *             Artikel existiert nicht
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws AudiodateiExistiertNichtException
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 */
	public void starteArtikel(String text, boolean verlauf)
			throws MaryConfigurationException, InterruptedException, ArtikelExistiertNichtException, IOException,
			FehlerhaftesWortException, FehlerhafterSatzException, WortExistiertNichtException,
			SatzExistiertNichtException, AbsatzExistiertNichtException, KapitelExistiertNichtException,
			AudiodateiExistiertNichtException, TransformerFactoryConfigurationError, Exception;

	/**
	 * Springt zum Artikelanfang
	 */
	public void artikelanfang();

	// ======================= Kapitel-Steuerung ===============================

	/**
	 * Springt bei der Audioausgabe zum nächsten Kapitel
	 * 
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * 
	 */
	public void naechstesKapitel()
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Springt bei der Audioausgabe zum vorherigen Kapitel bzw. zum Anfang des
	 * aktuellen
	 * 
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * 
	 */
	public void vorherigesKapitel()
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Springt zum Kapitelanfang
	 * 
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 */
	public void kapitelanfang()
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Springt zu x-ten Kapitel
	 * 
	 * @param index
	 *            Kapitelindex
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 */
	public void springeZuKapitel(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException;

	// ======================= Absatz-Steuerung ================================

	/**
	 * Springt bei der Audioausgabe zum vorherigen Absatz
	 * 
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * 
	 */
	public void vorherigerAbsatz()
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Setzt die Audioausgabe beim nächsten Absatz fort
	 * 
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * 
	 */
	public void naechsterAbsatz()
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Springt zum Absatzanfang
	 * 
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 */
	public void absatzanfang()
			throws AbsatzExistiertNichtException, WortExistiertNichtException, FehlerhaftesWortException;

	/**
	 * Springt in der Wiedergabe zu einem bestimmten Absatz
	 * 
	 * @param index
	 *            Absatzindex
	 * @throws WortExistiertNichtException
	 * @throws AbsatzExistiertNichtException
	 * @throws FehlerhaftesWortException
	 */
	public void springeZuAbsatz(int index)
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException;

	// ======================= Satz-Steuerung ==================================

	/**
	 * Setzt die Audioausgabe beim nächsten Satz fort
	 * 
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht Wort existiert nicht
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht Satz existiert nicht
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 * 
	 */
	public void naechsterSatz() throws FehlerhaftesWortException, FehlerhafterSatzException,
			SatzExistiertNichtException, WortExistiertNichtException;

	/**
	 * Springt bei der Audioausgabe zum vorherigen Satz
	 * 
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * 
	 */
	public void vorherigerSatz() throws FehlerhaftesWortException, SatzExistiertNichtException,
			WortExistiertNichtException, FehlerhafterSatzException;

	/**
	 * Springt zum Satzanfang
	 * 
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 */
	public void satzanfang() throws FehlerhaftesWortException, SatzExistiertNichtException, WortExistiertNichtException,
			FehlerhafterSatzException;

	/**
	 * Springt zu x-ten Satz
	 * 
	 * @param index
	 *            Satzindex
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 */
	public void springeZuSatz(int index) throws FehlerhaftesWortException, FehlerhafterSatzException,
			SatzExistiertNichtException, WortExistiertNichtException;

	// ======================= Wort-Steuerung ==================================

	/**
	 * Springt bei der Audioausgabe zum nächsten Wort
	 * 
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 */
	public void naechstesWort() throws FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Springt bei der Audioausgabe zum vorherigen Wort bzw. zum Wortanfang des
	 * aktuellen Wortes
	 * 
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 */
	public void vorherigesWort() throws FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Springt zu x-ten Wort
	 * 
	 * @param index
	 *            Wortindex
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 */
	public void springeZuIndex(int index) throws FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Springt ein paar Wörter vor
	 * 
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 */
	public void springeLeichtVorIndex() throws FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Springt ein paar Wörter zurück
	 * 
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 */
	public void springeLeichtZurueckIndex() throws FehlerhaftesWortException, WortExistiertNichtException;

	/**
	 * Sucht nach einem Wort und springt an die Stelle wo dieses das nächste mal
	 * erscheint
	 */
	/**
	 * @param richtung
	 *            vorwärts oder rückwärts
	 * @param wort
	 *            Suchbegriff
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * 
	 */
	public void sucheWort_einzeln(Richtung richtung, String wort) throws FehlerhaftesWortException,
			WortExistiertNichtException, SatzExistiertNichtException, FehlerhafterSatzException;

	// ======================= Zeit-Steuerung ==================================

	/**
	 * Springt zu einen bestimmten Zeitpunkt
	 * 
	 * @param zeit
	 *            Zeitpunkt in Sekunden
	 */
	public void springeZuZeitpunkt(Duration zeit);

	/**
	 * Springt ein paar Sekunden vor
	 */
	public void springeLeichtVorZeit();

	/**
	 * Springt ein paar Sekunden zurück
	 */
	public void springeLeichtZurueckZeit();

	// ======================= Sonstige Steuerung ==============================

	/**
	 * Setzt am aktuellen Zeitpunkt bzw. Satzanfang ein Lesezeichen TODO
	 */
	public void setzeLesezeichen();

	/**
	 * Springt zum vorherigen Artikel
	 * 
	 * @throws InterruptedException
	 * @throws MaryConfigurationException
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 * @throws FehlerhaftesWortException
	 *             fehlerhafte Wortinformationen
	 * @throws IOException
	 * 
	 * @throws ArtikelExistiertNichtException
	 *             Artikel existiert nicht
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws AudiodateiExistiertNichtException
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 */
	public void zurueck() throws MaryConfigurationException, InterruptedException, ArtikelExistiertNichtException,
			IOException, FehlerhaftesWortException, FehlerhafterSatzException, WortExistiertNichtException,
			SatzExistiertNichtException, AbsatzExistiertNichtException, KapitelExistiertNichtException,
			AudiodateiExistiertNichtException, TransformerFactoryConfigurationError, Exception;

	/**
	 * Springt einen Artikel vor
	 * 
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 */
	public void vor() throws TransformerFactoryConfigurationError, Exception;

	/**
	 * Startet eine zweite Audioausgabe mit einem Signalton
	 * 
	 * @throws MalformedURLException
	 */
	public void playLinkTon() throws MalformedURLException;

	/**
	 * An die Zeitvariabel des Mediaplayers einen Listener anhängen
	 * 
	 * @param invalidationListener
	 *            Listener
	 */
	public void listenerAktuelleZeit(InvalidationListener invalidationListener);

	/**
	 * Definiert eine Aktion die beim Start der Medienwiedergabe ausgeführt
	 * werden soll
	 * 
	 * @param r
	 *            Aktion
	 */
	public void listenerStart(Runnable r);

	/**
	 * Initialisiert die Zeitdauer des Artikels
	 */
	public void initZeit();

	/**
	 * Sorgt dafür, dafür dass beim Erreichen des Artikels eine bestimmte Aktion
	 * ausgeführt werden soll. Definiert besagte Funktion.
	 */
	public void listenerEnde();

	// ======================= Laufzeit-Inforamtionen ==========================

	/**
	 * Gibt zurück ob der Player momentan einen Artikel abspielt (pausiert =
	 * false)
	 * 
	 * @return Wiedergabe findet statt
	 */
	public boolean isPlaying();

	/**
	 * Aktuelle Positionsparameter
	 * 
	 * @return Aktuelle Positionsparameter
	 */
	public ModelPositionsparameter_Impl getAktuellePositionsparameter();

	/**
	 * Vorheriger Link
	 * 
	 * @return Vorheriger Link
	 */
	public int naechstKleinererLink();

	/**
	 * Nächster Link
	 * 
	 * @return Nächster Link
	 */
	public int naechstGroessererLink();

	/**
	 * Spezifischer Link
	 * 
	 * @param aktuellerLink
	 *            Linknummer
	 * @return spezifischer Link
	 */
	public String getLink(int aktuellerLink);

	/**
	 * Erster Link
	 * 
	 * @return Erster Link
	 */
	public double firstLink();

	/**
	 * Alle Links im Artikel
	 * 
	 * @return Alle Links im Artikel
	 */
	public ArrayList<String> getLinks();

	// /**
	// * Aktuelle abgespieltes Wort
	// *
	// * @return Aktuelle abgespieltes Wort
	// */
	// public String getAktuellesWort();

	/**
	 * Update der Positionsparameter
	 * 
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             Wortinforamtionen fehlerhaft
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws FehlerhafterSatzException
	 *             Satzinforamtionen fehlerhaft
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * @throws MalformedURLException
	 */
	public void updatePosition() throws WortExistiertNichtException, FehlerhaftesWortException,
			SatzExistiertNichtException, FehlerhafterSatzException, AbsatzExistiertNichtException,
			KapitelExistiertNichtException, MalformedURLException;

	/**
	 * Alle Kapitelbezeichner des Inhaltsverzeichnisses
	 * 
	 * @return Alle Kapitelbezeichner des Inhaltsverzeichnisses
	 */
	public ArrayList<String> getInhaltsverzeichnis();

	public void leseInhaltsverzeichnis() throws MalformedURLException;
	
	public void brecheVorlesenInhaltsverzeichnisAb();
	
	public void setLogger(Log log);
	
	public void setArtikelwechsel(boolean artikelwechsels);
	
	public void setErstellungErlaubt(boolean erstellungErlaubt);

	public void setSprache(String string);
}
