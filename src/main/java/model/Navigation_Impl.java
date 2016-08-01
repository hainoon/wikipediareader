package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.transform.TransformerFactoryConfigurationError;

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
public class Navigation_Impl implements Navigation {

	private Read_Model daten;

	/**
	 * Konstruktor
	 * 
	 * @param pfad
	 *            Pfad zu Dateiverzeichnis mit den benötigten Daten
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 * 
	 */
	public Navigation_Impl(String pfad, ModelLadefortschritt fortschritt, boolean erstellungerlaubt, String sprache2) throws TransformerFactoryConfigurationError, Exception {
		Write_Model daten2 = new Write_Model(pfad, fortschritt, erstellungerlaubt, sprache2);
		daten2.start();
		daten = new Read_Model_Impl(daten2.getDocument(), fortschritt);
//		System.out.println("Datenstruktur erstellt");
	}

	// ==================== KAPITEL STEUERUNG ================================
	@Override
	public Duration getStartWertVorherigesKapitel(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		int aktuellesKapitel = daten.getKapitelNummerFuerWortIndex(index);
		return daten.getKapitelStartzeit(aktuellesKapitel - 1);
	}

	@Override
	public Duration getStartWertNaechstesKapitel(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		int aktuellesKapitel = daten.getKapitelNummerFuerWortIndex(index);
		return daten.getKapitelStartzeit(aktuellesKapitel + 1);
	}

	@Override
	public Duration getKapitelanfangVonWort(int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		int aktuellesKapitel = daten.getKapitelNummerFuerWortIndex(index);
		return daten.getKapitelStartzeit(aktuellesKapitel);
	}

	@Override
	public Duration getKapitelX(int x)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		return daten.getKapitelStartzeit(x);
	}

	@Override
	public int getKapitel(int x)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		return daten.getKapitelNummerFuerWortIndex(x);
	}

	@Override
	public int getKapitelanzahlInArtikel() throws WortExistiertNichtException {
		return daten.getKapitelElemente().size();
	}

	// ==================== ABSATZ STEUERUNG ===================================

	@Override
	public Duration getStartwertNaechsterAbsatz(int index)
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException {
		int aktuellerAbsatz = daten.getAbsatzNummerFuerWortIndex(index);
		System.out.println("aktueller Index: " + index + " aktueller Absatz" + aktuellerAbsatz);
		return daten.getAbsatzStartzeit(aktuellerAbsatz + 1);
	}

	@Override
	public Duration getStartwertVorherigerAbsatz(int index)
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException {
		int aktuellerAbsatz = daten.getAbsatzNummerFuerWortIndex(index);
		return daten.getAbsatzStartzeit(aktuellerAbsatz - 1);
	}

	@Override
	public Duration getAbsatzzanfangVonWort(int index)
			throws AbsatzExistiertNichtException, WortExistiertNichtException, FehlerhaftesWortException {
		int aktuelleAbsatznummer = daten.getAbsatzNummerFuerWortIndex(index);
		return daten.getAbsatzStartzeit(aktuelleAbsatznummer);
	}

	@Override
	public Duration getAbsatzX(int x)
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException {
		return daten.getAbsatzStartzeit(x);
	}

	@Override
	public int getAbsatz(int x) throws AbsatzExistiertNichtException, WortExistiertNichtException {
		return daten.getAbsatzNummerFuerWortIndex(x);
	}

	@Override
	public int getAbsatzanzahlInArtikel() {
		return daten.getAbsaetzeElemente().size();
	}

	// ====================== SATZ STEUERUNG ===================================
	@Override
	public Duration getStartwertNaechsterSatz(int index) throws FehlerhaftesWortException, FehlerhafterSatzException,
			SatzExistiertNichtException, WortExistiertNichtException {
		int aktuelleSatznummer = daten.getSatzNummerFuerWortIndex(index);
		return daten.getSatzStartzeit(aktuelleSatznummer + 1);
	}

	@Override
	public Duration getStartwertVorherigerSatz(int index) throws FehlerhaftesWortException, SatzExistiertNichtException,
			WortExistiertNichtException, FehlerhafterSatzException {
		int aktuelleSatznummer = daten.getSatzNummerFuerWortIndex(index);
		return daten.getSatzStartzeit(aktuelleSatznummer - 1);
	}

	@Override
	public Duration getSatzanfangVonWort(int index) throws FehlerhaftesWortException, SatzExistiertNichtException,
			WortExistiertNichtException, FehlerhafterSatzException {
		int aktuelleSatznummer = daten.getSatzNummerFuerWortIndex(index);
		return daten.getSatzStartzeit(aktuelleSatznummer);
	}

	@Override
	public Duration getSatzX(int x) throws FehlerhaftesWortException, FehlerhafterSatzException,
			SatzExistiertNichtException, WortExistiertNichtException {
		return daten.getSatzStartzeit(x);
	}

	@Override
	public int getSatz(int x)
			throws FehlerhaftesWortException, SatzExistiertNichtException, WortExistiertNichtException {
		return daten.getSatzNummerFuerWortIndex(x);
	}

	@Override
	public int getSatzanzahlInArtikel() {
		return daten.getSaetzeElemente().size();
	}

	// ====================== WORT STEUERUNG ===================================

	@Override
	public Duration getStartwertWort(int i) throws FehlerhaftesWortException, WortExistiertNichtException {
		return daten.getWortStartzeit(i);
	}

	@Override
	public Duration getStartwertNaechstesWort(int i) throws FehlerhaftesWortException, WortExistiertNichtException {
		return daten.getWortStartzeit(i + 1);
	}

	@Override
	public Duration getStartwertVorherigesWort(int i) throws FehlerhaftesWortException, WortExistiertNichtException {
		return daten.getWortStartzeit(i - 1);
	}

	@Override
	public String getWort(int index) throws WortExistiertNichtException, FehlerhaftesWortException {
		return daten.getWort(index);
	}

	@Override
	public int getWortanzahlInArtikel() {
		return daten.getWoerterElements().size();
	}

	// ================== SONSTIGES STEUERUNG ==================================

	@Override
	public int findeWort_vorwaerts(String wort, int index)
			throws FehlerhaftesWortException, WortExistiertNichtException {
		return daten.findeWort_vorwaerts(wort, index);
	}

	@Override
	public int findeWort_rueckwaerts(String wort, int index)
			throws FehlerhaftesWortException, WortExistiertNichtException {
		return daten.findeWort_rueckwaerts(wort, index);
	}

	@Override
	public int getWortIndexAusZeit(Duration zeit) throws WortExistiertNichtException, FehlerhaftesWortException {
		return daten.getWortIndexAusZeit(zeit);
	}

	@Override
	public Duration getZeitdauerArtikel() {
		return new Duration(Double.parseDouble(daten.getWoerterElements().last().attr("stop")));
	}

	@Override
	public TreeMap<Integer, String> getLinks() throws IOException {
		return daten.getLinks();
	}

	@Override
	public ArrayList<String> getInhaltsverzeichnis() {
		return daten.getInhaltsverzeichnis();
	}
}
