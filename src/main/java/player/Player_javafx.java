package player;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Stack;
import java.util.TreeMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.io.FileUtils;

import exceptions.AbsatzExistiertNichtException;
import exceptions.ArtikelExistiertNichtException;
import exceptions.FehlerhafterSatzException;
import exceptions.FehlerhaftesWortException;
import exceptions.KapitelExistiertNichtException;
import exceptions.SatzExistiertNichtException;
import exceptions.WortExistiertNichtException;
import view.KeinVorherigerArtikelFehlermeldung;
import view.VariablenGuiEinstellungen;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import javafx.util.Duration;

import model.ModelLadefortschritt;
import model.ModelPositionsparameter_Impl;
import model.Navigation_Impl;
import sprachsteuerung.Richtung;

/**
 * Diese Klasse ist für die Audioausgabe zuständig. Zudem stellt sie den
 * Controller zwischen Model und View dar.
 * 
 * Player der auf dem Media-Framework von JavaFx aufbaut
 * 
 * @author marcel
 *
 */
public class Player_javafx extends AbstractPlayer {

	private String _artikel = "Isar";

	private File _audioDatei;
	private File _linkTon;
	private File _datenOrdner;
	private String sprache = "Deutsch";

	// private Stack<String> _verlauf;

	private Media _media;
	private MediaPlayer _mediaPlayer;
	private MediaPlayer playerinhaltsverzeichnis;
	private Stage _stage; // TODO wird das hier wirklich benötigt?!
	private Duration zuletztgestoppt;

	private ModelLadefortschritt fortschritt;

	private int linksichtbarkeit = 30;

	private TreeMap<Integer, String> _links; // TODO

	private Stack<String> _verlauf; // für zurück
	private Stack<String> _verlauf2; // für vor

	private boolean guiInit; // TODO nach Nutzerstudien wieder entfernen

	/**
	 * Konstruktor
	 * 
	 * @param primaryStage
	 *            primaryStage
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 */
	public Player_javafx(Stage primaryStage, ModelLadefortschritt fortschritt)
			throws TransformerFactoryConfigurationError, Exception {
		assert primaryStage != null : "Ungültige Stage";
		this.fortschritt = fortschritt;
		_verlauf = new Stack<String>();
		_verlauf2 = new Stack<String>();
		_stage = primaryStage;
		init(primaryStage,sprache);
		// autoplay();
	}

	// ==================== GRUND STEUERUNG ===================================
	@Override
	public void starteArtikel(String artikel, boolean verlauf) throws TransformerFactoryConfigurationError, Exception {
		if (artikelwechsel || guiInit) {
			guiInit = false; // TODO nach Nutzerstudien wieder entfernen
			// TODO zurueck entfernen
			if (artikel.contains(" ")) { // TODO
				String[] s = artikel.split("\\s+");
				artikel = s[0];
			}
			stopAudio(true);
			if (verlauf && !_artikel.equals(artikel)) {
				_verlauf.push(_artikel);
			}
			_artikel = artikel;
			init(_stage, sprache);
			Object[] params = { artikel };
			_logger.methode("Player", "starteArtikel", params);
			autoplay();
		} else {
			throw new Exception("Artikelwechsel momentan deaktiviert");
		}
	}

	@Override
	public void startAudio() throws TransformerFactoryConfigurationError, Exception {
		Object[] params = { _artikel };
		_logger.methode("Player", "startAudio", params);
		Status status = _mediaPlayer.getStatus();
		bereiteStartVor(status);
		System.out.println("play");
		_mediaPlayer.play();

	}

	@Override
	public void pauseAudio() {
		Status status = _mediaPlayer.getStatus();
		_logger.methode("Player", "pauseAudio");
		if (status == Status.PLAYING) {
			System.out.println("pause");
			_mediaPlayer.pause();
		}

	}

	@Override
	public void stopAudio(boolean automatisch) {
		Status status = _mediaPlayer.getStatus();
		Object[] params = { _artikel };
		_logger.methode("Player", "stopAudio", params);
		if (status == Status.PLAYING || status == Status.PAUSED) {
			System.out.println("stop");
			if (automatisch) {
				zuletztgestoppt = aktuellePositionsparameter.get_aktuelleZeit();
			} else {
				zuletztgestoppt = new Duration(0);
			}
			_mediaPlayer.stop();
			_media = null; // TODO

		}

	}

	@Override
	public void springeZuZeitpunkt(Duration zeit) {
		// assert gueltigerZeitwert(zeit) : "Ungültiger Zeitwert";
		_logger.ereignis("Beginn Zeitpunktwechsel");
		_mediaPlayer.seek(zeit);
	}

	@Override
	public void sucheWort_einzeln(Richtung richtung, String wort) throws FehlerhaftesWortException,
			WortExistiertNichtException, SatzExistiertNichtException, FehlerhafterSatzException {
		assert richtung != Richtung.DEFAULT : "Ungültige Richtung";
		Richtung r = Richtung.normalisierteRichtung(richtung);
		Object[] params = { r, wort };
		_logger.methode("Player", "sucheWort_einzeln", params);
		int index = -1;
		switch (r) {
		case WEITER:
			index = _navi.findeWort_vorwaerts(wort, aktuellePositionsparameter.get_aktuellerIndex());
			springeZuZeitpunkt(_navi.getSatzanfangVonWort(index));
			break;
		case ZURÜCK:
			index = _navi.findeWort_rueckwaerts(wort, aktuellePositionsparameter.get_aktuellerIndex());
			springeZuZeitpunkt(_navi.getSatzanfangVonWort(index));
			break;
		default: // TODO
		}
	}

	@Override
	public void vor() throws TransformerFactoryConfigurationError, Exception {
		_logger.methode("Player", "vor");
		if (_verlauf2.size() == 0) {
			new KeinVorherigerArtikelFehlermeldung(_stage, new ArtikelExistiertNichtException());
		} else {
			String vorherigerArtikel = _verlauf2.pop();
			starteArtikel(vorherigerArtikel, false);
		}
	}

	@Override
	public void zurueck() throws TransformerFactoryConfigurationError, Exception {
		_logger.methode("Player", "zurueck");
		if (_verlauf.size() == 0) {
			new KeinVorherigerArtikelFehlermeldung(_stage, new ArtikelExistiertNichtException());
		} else {
			String vorherigerArtikel = _verlauf.pop();
			_verlauf2.push(vorherigerArtikel);
			starteArtikel(vorherigerArtikel, false);
		}
	}
	// ==================== LINK ==============================================

	/**
	 * Ermittelt die Links die innerhalb einer bestimmten Entfernung bezüglich
	 * des Wortindex liegen
	 * 
	 * @param gebiet
	 *            Anzahl der Indizes die als Umfeld der aktuellen Position
	 *            gelten. 50 % vor aktueller Position, 50 % nach aktueller
	 *            Position
	 * @return Linkmenge
	 */
	private ArrayList<String> aktuelleLinks(int gebiet) {
		// assert gebiet > 0 && gebiet < _wortanzahlArtikel / 10 : "Ungültiges
		// Umfeld -> zu groß oder zu klein";
		ArrayList<String> ergebnis = new ArrayList<String>();

		int einzugsgroesse = gebiet;

		for (int i = _links.firstKey(); i < _links.lastKey(); i = _links.higherKey(i)) {
			if (i <= aktuellePositionsparameter.get_aktuellerIndex() + (einzugsgroesse / 2)
					&& i >= aktuellePositionsparameter.get_aktuellerIndex() - (einzugsgroesse / 2)) {
				ergebnis.add(_links.get(i));
			}
		}
		return ergebnis;
	}

	@Override
	public void playLinkTon() throws MalformedURLException {
		if (artikelwechsel) {
			String linkpfad = _linkTon.toURI().toString();
			MediaPlayer mediaplayer2 = new MediaPlayer(new Media(linkpfad));
			mediaplayer2.volumeProperty().set(0);
			Duration endzeit = aktuellePositionsparameter.get_genzwertObenIndex()
					.subtract(aktuellePositionsparameter.get_genzwertUntenIndex());
			//TODO endzeit vom letzten Wort des Links ermitteln statt vom ersten
			System.out
					.print("\n Differenz = " + aktuellePositionsparameter.get_genzwertObenIndex().toSeconds() + " - ");
			System.out.print(aktuellePositionsparameter.get_genzwertUntenIndex().toSeconds() + " = ");
			System.out.println(endzeit);
			mediaplayer2.getMedia().getMarkers().put("abbruch", endzeit);
			mediaplayer2.setOnMarker(e -> mediaplayer2.stop());
			mediaplayer2.setOnEndOfMedia(new Runnable() {

				@Override
				public void run() {
					System.out.println("Link abgespielt");// TODO entfernen
				}
			});
			mediaplayer2.play();
		}
	}

	@Override
	public int naechstKleinererLink() {
		return _links.ceilingKey(aktuellePositionsparameter.get_aktuellerIndex());
	}

	@Override
	public int naechstGroessererLink() {
		return _links.floorKey(aktuellePositionsparameter.get_aktuellerIndex());
	}

	@Override
	public String getLink(int aktuellerLink) {
		return _links.get(aktuellerLink);
	}

	public TreeMap<Integer, String> getAlleLinks() {
		return _links; // TODO
	}

	@Override
	public double firstLink() {
		// TODO besser Lösen
		return _links.firstKey();
	}

	/**
	 * Spielt wenn notwendig einen Ton für Links ab
	 * 
	 * @throws MalformedURLException
	 */
	private void pruefeObLinkton() throws MalformedURLException {
		if (_links.containsKey(aktuellePositionsparameter.get_aktuellerIndex())) {
			playLinkTon();
		}
	}
	// ==================== POSITION ==========================================

	@Override
	public void updatePosition() throws WortExistiertNichtException, FehlerhaftesWortException,
			SatzExistiertNichtException, FehlerhafterSatzException, AbsatzExistiertNichtException,
			KapitelExistiertNichtException, MalformedURLException {
		aktuellePositionsparameter.set_aktuelleZeit(_mediaPlayer.getCurrentTime());
		Duration aktuell = aktuellePositionsparameter.get_aktuelleZeit();
		if (aktuell.toMillis() > aktuellePositionsparameter.get_genzwertObenIndex().toMillis()
				|| aktuell.toMillis() < aktuellePositionsparameter.get_genzwertUntenIndex().toMillis()) {
			int index_neu = _navi.getWortIndexAusZeit(aktuell);
			updateWort(index_neu);
			pruefeObLinkton();
			links = aktuelleLinks(linksichtbarkeit);
			updateSatz(aktuell, index_neu);
			updateAbsatz(aktuell, index_neu);
			updateKapitel(aktuell, index_neu);
			_logger.updatePosition();
		}

	}

	/**
	 * Setzt aktuelles Kapitel
	 * 
	 * @param aktuell
	 *            aktueller Zeitwert
	 * @param index
	 *            aktueller Index
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 */
	private void updateKapitel(Duration aktuell, int index)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		if (aktuell.toMillis() > aktuellePositionsparameter.get_grenzwertObenKapitel().toMillis()
				|| aktuell.toMillis() < aktuellePositionsparameter.get_grenzwertUntenKapitel().toMillis()) {
			int kapitel = _navi.getKapitel(index);
			aktuellePositionsparameter.set_aktuellesKapitel(kapitel);
			aktuellePositionsparameter.set_grenzwertObenKapitel(_navi.getKapitelX(kapitel + 1));
			aktuellePositionsparameter.set_grenzwertUntenKapitel(_navi.getKapitelX(kapitel));
		}
	}

	/**
	 * Setzt aktuellen Absatz
	 * 
	 * @param aktuell
	 *            aktueller Zeitwert
	 * @param index
	 *            aktueller Index
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 */
	private void updateAbsatz(Duration aktuell, int index)
			throws AbsatzExistiertNichtException, WortExistiertNichtException, FehlerhaftesWortException {
		if (aktuell.toMillis() > aktuellePositionsparameter.get_grenzwertObenAbsatz().toMillis()
				|| aktuell.toMillis() < aktuellePositionsparameter.get_grenzwertUntenAbsatz().toMillis()) {
			int absatz = _navi.getAbsatz(index);
			aktuellePositionsparameter.set_aktuellerAbsatz(absatz);
			aktuellePositionsparameter.set_grenzwertObenAbsatz(_navi.getAbsatzX(absatz + 1));
			aktuellePositionsparameter.set_grenzwertUntenAbsatz(_navi.getAbsatzX(absatz));
		}
	}

	/**
	 * Setzt aktuellen Satz
	 * 
	 * @param aktuell
	 *            aktueller Zeitwert
	 * @param index
	 *            aktueller Index
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 */
	private void updateSatz(Duration aktuell, int index) throws FehlerhaftesWortException, SatzExistiertNichtException,
			WortExistiertNichtException, FehlerhafterSatzException {
		if (aktuell.toMillis() > aktuellePositionsparameter.get_grenzwertObenSatz().toMillis()
				|| aktuell.toMillis() < aktuellePositionsparameter.get_grenzwertUntenSatz().toMillis()) {
			int satz = _navi.getSatz(index);
			aktuellePositionsparameter.set_aktuellerSatz(satz);
			aktuellePositionsparameter.set_grenzwertObenSatz(_navi.getSatzX(satz + 1));
			aktuellePositionsparameter.set_grenzwertUntenSatz(_navi.getSatzX(satz));
		}
	}

	/**
	 * Setzt aktuelles Wort
	 * 
	 * @param index
	 *            aktueller Index
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws MalformedURLException
	 */
	private void updateWort(int index)
			throws FehlerhaftesWortException, WortExistiertNichtException, MalformedURLException {
		aktuellePositionsparameter.set_aktuellerIndex(index);
		aktuellePositionsparameter.set_genzwertObenIndex(_navi.getStartwertNaechstesWort(index));
		aktuellePositionsparameter.set_genzwertUntenIndex(_navi.getStartwertWort(index));
		String wort = _navi.getWort(index);
		aktuellePositionsparameter.set_aktuellesWort(wort);
	}

	/**
	 * Initialisiert die aktuellen Positionsparameter des Artikels
	 * 
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 * @throws AbsatzExistiertNichtException
	 *             Absatz existiert nicht
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * @throws MalformedURLException
	 * 
	 */
	private void initPosition() throws WortExistiertNichtException, FehlerhaftesWortException,
			SatzExistiertNichtException, FehlerhafterSatzException, AbsatzExistiertNichtException,
			KapitelExistiertNichtException, MalformedURLException {
		if (aktuellePositionsparameter == null) {
			aktuellePositionsparameter = new ModelPositionsparameter_Impl(_artikel, _navi.getWortanzahlInArtikel(),
					_navi.getSatzanzahlInArtikel(), _navi.getAbsatzanzahlInArtikel(),
					_navi.getKapitelanzahlInArtikel());
		} else {
			aktuellePositionsparameter.set_aktuellerArtikel(_artikel); // TODO
																		// weg
																		// ->
																		// wenn
																		// hier
																		// gesetzt,
																		// dann
																		// Problem
																		// bei
																		// Artikelwechsel
			aktuellePositionsparameter.set_wortanzahlArtikel(_navi.getWortanzahlInArtikel());
			aktuellePositionsparameter.set_satzanzahlArtikel(_navi.getSatzanzahlInArtikel());
			aktuellePositionsparameter.set_absatzanzahlArtikel(_navi.getAbsatzanzahlInArtikel());
			aktuellePositionsparameter.set_kapitelanzahlArtikel(_navi.getKapitelanzahlInArtikel());
		}
		aktuellePositionsparameter.set_aktuelleZeit(_mediaPlayer.getCurrentTime());
		Duration aktuelleZeit = aktuellePositionsparameter.get_aktuelleZeit();
		int index = _navi.getWortIndexAusZeit(aktuelleZeit);
		updateWort(index);
		int satz = _navi.getSatz(index);
		aktuellePositionsparameter.set_aktuellerSatz(satz);
		aktuellePositionsparameter.set_grenzwertObenSatz(_navi.getSatzX(satz + 1));
		aktuellePositionsparameter.set_grenzwertUntenSatz(_navi.getSatzX(satz));
		int absatz = _navi.getAbsatz(index);
		aktuellePositionsparameter.set_aktuellerAbsatz(absatz);
		aktuellePositionsparameter.set_grenzwertObenAbsatz(_navi.getAbsatzX(absatz + 1));
		aktuellePositionsparameter.set_grenzwertUntenAbsatz(_navi.getAbsatzX(absatz));
		int kapitel = _navi.getKapitel(index);
		aktuellePositionsparameter.set_aktuellesKapitel(kapitel);
		aktuellePositionsparameter.set_grenzwertObenKapitel(_navi.getKapitelX(kapitel + 1));
		aktuellePositionsparameter.set_grenzwertUntenKapitel(_navi.getKapitelX(kapitel));

	}
	// ==================== LISTENER ==========================================

	@Override
	public void listenerAktuelleZeit(InvalidationListener listener) {
		// TODO
		_mediaPlayer.currentTimeProperty().addListener(listener);

	}

	@Override
	public void listenerStart(Runnable runnable) {
		// TODO
		_mediaPlayer.setOnReady(runnable);
	}

	@Override
	public void listenerEnde() {
		// TODO Auto-generated method stub
		_mediaPlayer.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				System.out.println("Ende");
				_mediaPlayer.stop(); // TODO
			}
		});
	}

	// ==================== SONSTIGES =========================================

	/**
	 * Artikel automatisch abspielen
	 */
	public void autoplay() {
		_mediaPlayer.setAutoPlay(true);
	}

	/**
	 * Initialisiert den Player
	 * 
	 * @param primaryStage
	 *            primaryStage
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 */
	private void init(Stage primaryStage, String sprache2) throws TransformerFactoryConfigurationError, Exception {
		fortschritt.setLadevorgangAktiv(true);
		_datenOrdner = new File("Artikel/"+sprache2+"/" + _artikel + "/");
		_linkTon = new File("link.wav");
		_navi = new Navigation_Impl(_artikel, fortschritt, erstellungerlaubt, sprache2);
		_audioDatei = new File(_datenOrdner.getPath()+ "/audio.wav");
		_media = new Media(_audioDatei.toURI().toString());

		fortschritt.setAnzahlTeilschritte(4);
		_mediaPlayer = new MediaPlayer(_media);
		inhaltsverzeichnis = _navi.getInhaltsverzeichnis();
		Media quelle = new Media(new File(_datenOrdner.getPath() + "/inhaltsverzeichnis" + ".wav").toURI().toURL().toString());
		playerinhaltsverzeichnis = new MediaPlayer(quelle);
		initPosition();
		fortschritt.updateTeilschritt("Aktuelle Position ermittelt");
		_links = _navi.getLinks();
		links = aktuelleLinks(linksichtbarkeit);
		fortschritt.updateTeilschritt("Links ermittelt");
		fortschritt.updateTeilschritt("Inhaltsverzeichnis gesetzt");
		fortschritt.updateTeilschritt("Aktuelle Position gesetzt");
		fortschritt.setLadevorgangAktiv(false);
	}

	@Override
	public void initZeit() {
		// TODO aus Schnittstelle entfernen
		aktuellePositionsparameter.set_zeitdauerArtikel(_mediaPlayer.getMedia().getDuration());
	}

	/**
	 * Bereitet den Start von Audiodateien vor -> Player initialisieren wenn
	 * dies bisher nicht geschehen ist
	 * 
	 * @param status
	 *            Wiedergabestatus des Mediaplayers
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 */
	private void bereiteStartVor(Status status) throws TransformerFactoryConfigurationError, Exception {
		if (status == Status.STOPPED || status == Status.UNKNOWN) {
			init(_stage, sprache);
			_mediaPlayer.setStartTime(zuletztgestoppt);
		}
	}

	@Override
	public boolean isPlaying() {
		return _mediaPlayer.getStatus() == Status.PLAYING;
	}

	/**
	 * Gibt den Player zurück
	 * 
	 * @return Player
	 */
	public MediaPlayer get_mediaPlayer() {
		return _mediaPlayer;
	}

	/**
	 * Gibt primaryStage zurück
	 * 
	 * @return primaryStage
	 */
	public Stage get_stage() {
		return _stage;
	}

	@Override
	public void leseInhaltsverzeichnis() throws MalformedURLException {
		// TODO ggf auslagern
		_logger.methode("Player", "readInhaltsverzeichnis");
		_mediaPlayer.pause();
		Media quelle = new Media(new File(_datenOrdner.getPath() + "/inhaltsverzeichnis" + ".wav").toURI().toURL().toString());
		playerinhaltsverzeichnis = new MediaPlayer(quelle);
		playerinhaltsverzeichnis.play();
		// try {
		// wait((long) quelle.getDuration().toMillis());
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		playerinhaltsverzeichnis.setOnEndOfMedia(() -> {
			_mediaPlayer.play();
		});
	}

	@Override
	public void brecheVorlesenInhaltsverzeichnisAb() {
		Status status = playerinhaltsverzeichnis.getStatus();
		if (status == Status.PLAYING) {
			playerinhaltsverzeichnis.stop();
		}
		_mediaPlayer.play(); // TODO an Satzanfang neu beginnen

	}

	/**
	 * Workaround um für Nutzerstudien den Startartikel ändern zu können
	 * 
	 * @param b
	 */
	public void setGuiInit(boolean b) {
		guiInit = b; // TODO nach Nutzerstudien wieder entfernen
	}

	@Override
	public void setSprache(String sprache) {
		this.sprache = sprache;
		
	}

}
