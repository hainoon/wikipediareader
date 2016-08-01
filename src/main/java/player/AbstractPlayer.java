package player;

import java.io.IOException;
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
import javafx.util.Duration;
import marytts.exceptions.MaryConfigurationException;
import model.Log;
import model.ModelPositionsparameter_Impl;
import model.Navigation;

/**
 * Logik des Players. Unabhängig von den verschiedenen darunter liegenden
 * Datenstrukturen -> navigiert über das springen zu bestimmten Zeitpunkten
 * 
 * @author marcel
 *
 */
public abstract class AbstractPlayer implements Player {

	protected Navigation _navi;
	protected Log _logger;

	protected boolean artikelwechsel = true;
	protected boolean erstellungerlaubt = true;

	// Positionsparameter
	ModelPositionsparameter_Impl aktuellePositionsparameter;

	protected ArrayList<String> links;
	protected ArrayList<String> inhaltsverzeichnis;

	// Parameter für das Skippen um einen festgelegten Wert
	protected Duration leichtZ = new Duration(10000);
	protected int leichtI = 10;

	@Override
	public abstract void startAudio()
			throws FehlerhaftesWortException, FehlerhafterSatzException, MaryConfigurationException,
			InterruptedException, ArtikelExistiertNichtException, IOException, WortExistiertNichtException,
			SatzExistiertNichtException, AbsatzExistiertNichtException, KapitelExistiertNichtException,
			AudiodateiExistiertNichtException, TransformerFactoryConfigurationError, Exception;

	@Override
	public abstract void pauseAudio();

	@Override
	public abstract void stopAudio(boolean automatisch);

	@Override
	public void naechsterSatz() throws FehlerhaftesWortException, FehlerhafterSatzException,
			SatzExistiertNichtException, WortExistiertNichtException {
		_logger.methode("Player", "naechsterSatz");
		springeZuZeitpunkt(_navi.getStartwertNaechsterSatz(aktuellePositionsparameter.get_aktuellerIndex()));
	}

	@Override
	public void vorherigerSatz() throws FehlerhaftesWortException, SatzExistiertNichtException,
			WortExistiertNichtException, FehlerhafterSatzException {
		_logger.methode("Player", "vorherigerSatz");
		springeZuZeitpunkt(_navi.getStartwertVorherigerSatz(aktuellePositionsparameter.get_aktuellerIndex()));
	}

	@Override
	public void naechsterAbsatz()
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException {
		_logger.methode("Player", "naechsterAbsatz");
		springeZuZeitpunkt(_navi.getStartwertNaechsterAbsatz(aktuellePositionsparameter.get_aktuellerIndex()));
	}

	@Override
	public void vorherigerAbsatz()
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException {
		_logger.methode("Player", "vorherigerAbsatz");
		springeZuZeitpunkt(_navi.getStartwertVorherigerAbsatz(aktuellePositionsparameter.get_aktuellerIndex()));
	}

	@Override
	public void satzanfang() throws FehlerhaftesWortException, SatzExistiertNichtException, WortExistiertNichtException,
			FehlerhafterSatzException {
		_logger.methode("Player", "satzanfang");
		springeZuZeitpunkt(_navi.getSatzanfangVonWort(aktuellePositionsparameter.get_aktuellerIndex()));
	}

	@Override
	public void absatzanfang()
			throws AbsatzExistiertNichtException, WortExistiertNichtException, FehlerhaftesWortException {
		_logger.methode("Player", "absatzanfang");
		springeZuZeitpunkt(_navi.getAbsatzzanfangVonWort(aktuellePositionsparameter.get_aktuellerIndex()));
	}

	@Override
	public void kapitelanfang()
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		_logger.methode("Player", "kapitelanfang");
		springeZuZeitpunkt(_navi.getKapitelanfangVonWort(aktuellePositionsparameter.get_aktuellerIndex()));
	}

	@Override
	public void artikelanfang() {
		_logger.methode("Player", "artikelanfang");
		springeZuZeitpunkt(Duration.ZERO);
	}

	@Override
	public void naechstesWort() throws FehlerhaftesWortException, WortExistiertNichtException {
		_logger.methode("Player", "naechstesWort");
		springeZuZeitpunkt(_navi.getStartwertNaechstesWort(aktuellePositionsparameter.get_aktuellerIndex()));
	}

	@Override
	public void vorherigesWort() throws FehlerhaftesWortException, WortExistiertNichtException {
		_logger.methode("Player", "vorherigesWort");
		springeZuZeitpunkt(_navi.getStartwertVorherigesWort(aktuellePositionsparameter.get_aktuellerIndex()));
	}

	@Override
	public void naechstesKapitel()
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		_logger.methode("Player", "naechstesKapitel");
		springeZuZeitpunkt(_navi.getStartWertNaechstesKapitel(aktuellePositionsparameter.get_aktuellerIndex()));
	}

	@Override
	public void vorherigesKapitel()
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		_logger.methode("Player", "vorherigesKapitel");
		springeZuZeitpunkt(_navi.getStartWertVorherigesKapitel(aktuellePositionsparameter.get_aktuellerIndex()));
	}

	@Override
	public void springeZuIndex(int index) throws FehlerhaftesWortException, WortExistiertNichtException {
		Object[] params = { index };
		_logger.methode("Player", "springeZuIndex", params);
		springeZuZeitpunkt(_navi.getStartwertWort(index));
	}

	@Override
	public void springeZuSatz(int x) throws FehlerhaftesWortException, FehlerhafterSatzException,
			SatzExistiertNichtException, WortExistiertNichtException {
		Object[] params = { x };
		_logger.methode("Player", "springeZuSatz", params);
		springeZuZeitpunkt(_navi.getSatzX(x));
	}

	@Override
	public void springeZuAbsatz(int index)
			throws FehlerhaftesWortException, AbsatzExistiertNichtException, WortExistiertNichtException {
		Object[] params = { index };
		_logger.methode("Player", "springeZuAbsatz", params);
		springeZuZeitpunkt(_navi.getAbsatzX(index));
	}

	@Override
	public void springeZuKapitel(int x)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		Object[] params = { x };
		_logger.methode("Player", "springeZuKapitel", params);
		springeZuZeitpunkt(_navi.getKapitelX(x));
	}

	@Override
	public abstract void springeZuZeitpunkt(Duration zeit);

	@Override
	public void springeLeichtVorZeit() {
		_logger.methode("Player", "springeLeichtVorZeit");
		springeZuZeitpunkt(aktuellePositionsparameter.get_aktuelleZeit().add(leichtZ));

	}

	@Override
	public void springeLeichtZurueckZeit() {
		_logger.methode("Player", "springeLeichtZurueckZeit");
		springeZuZeitpunkt(aktuellePositionsparameter.get_aktuelleZeit().subtract(leichtZ));

	}

	@Override
	public void springeLeichtVorIndex() throws FehlerhaftesWortException, WortExistiertNichtException {
		_logger.methode("Player", "springeLeichtVorIndex");
		springeZuIndex(aktuellePositionsparameter.get_aktuellerIndex() + leichtI);
	}

	@Override
	public void springeLeichtZurueckIndex() throws FehlerhaftesWortException, WortExistiertNichtException {
		_logger.methode("Player", "springeLeichtZurueckIndex");
		springeZuIndex(aktuellePositionsparameter.get_aktuellerIndex() - leichtI);
	}

	@Override
	public void setzeLesezeichen() {
		// TODO Auto-generated method stub

	}

	@Override
	public ModelPositionsparameter_Impl getAktuellePositionsparameter() {
		return aktuellePositionsparameter;
	}

	@Override
	public ArrayList<String> getLinks() {
		return links;
	}

	@Override
	public ArrayList<String> getInhaltsverzeichnis() {
		return inhaltsverzeichnis;
	}

	@Override
	public void setLogger(Log log) {
		_logger = log;
	}

	@Override
	public void setArtikelwechsel(boolean artikelwechsel) {
		this.artikelwechsel = artikelwechsel;

	}

	@Override
	public void setErstellungErlaubt(boolean erstellungErlaubt) {
		this.erstellungerlaubt = erstellungErlaubt;
	}

}
