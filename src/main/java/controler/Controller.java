package controler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observer;
import java.util.Stack;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.io.FileUtils;
import org.hsqldb.lib.FileUtil;

import exceptions.AbsatzExistiertNichtException;
import exceptions.ArtikelExistiertNichtException;
import exceptions.AudiodateiExistiertNichtException;
import exceptions.EinstellungsException;
import exceptions.FehlerhafterSatzException;
import exceptions.FehlerhaftesWortException;
import exceptions.KapitelExistiertNichtException;
import exceptions.ModusException;
import exceptions.SatzExistiertNichtException;
import exceptions.SprachbefehlAufnahmeException;
import exceptions.SprachbefehlAuswertungException;
import exceptions.SprachbefehlException;
import exceptions.WortExistiertNichtException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import marytts.exceptions.MaryConfigurationException;
import model.DownloadVonWiki;
import model.Elemente;
import model.Log;
import model.ModelLadefortschritt;
import model.ModelPositionsparameter_Impl;
import model.Read_Model_Impl;
import model.Write_Model;
import player.Player;
import player.Player_javafx;
import sprachsteuerung.Richtung;
import sprachsteuerung.Sprachsteuerung;
import view.AbsatzExistiertNichtFehlermeldung;
import view.ArtikelExisiertNichtFehlermeldung;
import view.Fehlermeldung;
import view.Gui_javafx;
import view.Gui_Einstellungen;
import view.KapitelExistiertNichtFehlermeldung;
import view.KeinVorherigerArtikelFehlermeldung;
import view.Ladeanzeige;
import view.SatzExistiertNichtFehlermeldung;
import view.Sprachbefehlfehlermeldung;
import view.WortExistiertNichtFehlermeldung;

/**
 * Controller Klasse. Setzt Funktionalität von Player, Sprachsteuerung usw.
 * zusammen und bindet es an die Gui an.
 * 
 * 
 * @author marcel
 *
 */
public class Controller extends Application implements Observer {
	private Player p;
	private Gui_javafx _gui;
	private Gui_Einstellungen _guiEinstellungen;
	private Ladeanzeige _ladeanzeige;
	private Sprachsteuerung _s;
	private ModelPositionsparameter_Impl _aktuellePosition;
	private Log _logger;
	private ModelLadefortschritt _fortschritt;
	private Stage _primaryStage;
	private boolean _reset;
	private boolean _wizzard;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this._primaryStage = primaryStage;
		_ladeanzeige = new Ladeanzeige();
		_fortschritt = new ModelLadefortschritt(1, "Erstelle Datenstruktur");
		_fortschritt.addObserver(_ladeanzeige);
		p = new Player_javafx(primaryStage, _fortschritt);
		_aktuellePosition = p.getAktuellePositionsparameter();
		_gui = new Gui_javafx(p, _aktuellePosition);
		_guiEinstellungen = new Gui_Einstellungen(primaryStage);
		einstellungenGui();
		_aktuellePosition.addObserver(_gui); // TODO
		_aktuellePosition.addObserver(this);
		// TODO notwendig für Guiaktualisierung bei Artikelwechsel

		_logger = new Log(_aktuellePosition);
		_s = new Sprachsteuerung(p, _logger);
		p.setLogger(_logger);
		interaktionGui();
		// ((Player_javafx) p).autoplay(); // TODO

	}

	private void einstellungenGui() {
		_guiEinstellungen.getOk().setOnAction(e -> {
			p.setSprache(_guiEinstellungen.getSprache());
			try {
				setAlternativenStartartikel();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			((Player_javafx) p).autoplay(); // TODO
			modusEinstellungen();
			inhaltsverzeichnisEinstellungen();
			linkEinstellungen();
			artikelEinstellungen();
			_guiEinstellungen.ausblenden();
			setReset();
		});
	}

	private void setReset() {
		switch (_guiEinstellungen.getReset()) {
		case RESET0:
			_reset = false;
			break;
		case RESET1:
			_reset = true;
			break;
		}

	}

	private void setAlternativenStartartikel()
			throws MaryConfigurationException, InterruptedException, ArtikelExistiertNichtException, IOException,
			FehlerhaftesWortException, FehlerhafterSatzException, WortExistiertNichtException,
			SatzExistiertNichtException, AbsatzExistiertNichtException, KapitelExistiertNichtException,
			AudiodateiExistiertNichtException, TransformerFactoryConfigurationError, Exception {
		String alternativ = _guiEinstellungen.getAlternativenStartartikel();
		if (!alternativ.equals("")) {
			((Player_javafx) p).setGuiInit(true);
			p.starteArtikel(alternativ, true);
		}
	}

	private void artikelEinstellungen() {
		switch (_guiEinstellungen.getArtikel()) {
		case All:
			p.setErstellungErlaubt(true);
			break;
		case OnlyCorpus:
			p.setErstellungErlaubt(false);
			break;
		}
	}

	private void linkEinstellungen() {
		switch (_guiEinstellungen.getLinks()) {
		case DisplayLinks1:
			_gui.setLinksanzeigen(true);
			break;
		case DisplayLinks0:
			_gui.setLinksanzeigen(false);
			_gui.blendeArtikelSucheAus();
			break;
		}

		switch (_guiEinstellungen.getLinksfolgen()) {
		case FollowLinks1:
			p.setArtikelwechsel(true);
			break;
		case FollowLinks0:
			p.setArtikelwechsel(false);
			break;
		}
	}

	private void inhaltsverzeichnisEinstellungen() {
		switch (_guiEinstellungen.getInhaltsverzeichnis()) {
		case Identifier1:
			_gui.inhaltsverzeichnisAnzeigen(true);
			break;
		case Identifier0:
			_gui.annonymisiereInhaltsverzeichnis();
			break;
		case NoTableOfContent:
			_gui.inhaltsverzeichnisAnzeigen(false);
			break;
		}
	}

	private void modusEinstellungen() {
		switch (_guiEinstellungen.getModusAuswahl()) {
		case GUI:
			_gui.setOhneSprachsteuerungModus();
			break;
		case VUI:
			_gui.setSprachsteuerungsModus();
			break;
		case WOZ:
			_wizzard = true;
			_gui.setSprachsteuerungsModus();
			disableSprachfunktion();
			p.stopAudio(true);
			break;
		case Complete:
			_gui.setNormalModus();
			break;
		}
	}

	private void disableSprachfunktion() {
		_s.setWizzard();
		_gui.blendeAllesAus();

	}

	/**
	 * Schnittstelle zur Gui. Regelt die Interaktion
	 * 
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 */
	private void interaktionGui() throws FehlerhaftesWortException, FehlerhafterSatzException {

		funktionalitaetButtons();
		funktionalitaetZeitslider();
		funktionalitaetIndexSlider();
		funktionalitaetSatzslider();
		funktionalitaetAbsatzslider();
		funktionalitaetKapitelslider();
		funktionalitaetInhaltsverzeichnis();

		// sorgt dafür, dass bei Veränderung der Zeit des Mediaplayers die
		// entsprechenden Daten geändert werden
		p.listenerAktuelleZeit(new InvalidationListener() {
			@Override
			public void invalidated(Observable arg0) {
				updateDaten();
			}
		});

		// init wenn Mediaplayer bereit
		p.listenerStart(new Runnable() {
			public void run() {
				p.initZeit();
				updateDaten(); // TODO hier notwendig?
			}
		});

		p.listenerEnde();

		_primaryStage.setOnCloseRequest(event -> {
			// TODO logger benachrichtigen
			// TODO bugfix
			p.stopAudio(true);
			if (_reset) {
				File artikel = new File("Artikel/");
				deleteFile(artikel);
			}
			System.exit(0);
			;
		});
	}

	private void deleteFile(File f) {
		if (f.isDirectory()) {
			for (File f1 : f.listFiles()) {
				deleteFile(f1);
			}
		}
		f.delete();
	}

	/**
	 * Setzt die Funktionalität des Inhaltsverzeichnisses
	 */
	private void funktionalitaetInhaltsverzeichnis() {
		_aktuellePosition.set_kapitelanzahlArtikel(_gui.getInhaltsverzeichnisButton().size()); // TODO
																								// sauberer
																								// lösen
		for (int i = 0; i < _aktuellePosition.get_kapitelanzahlArtikel(); ++i) {
			int counter = i;
			ArrayList<Button> list = _gui.getInhaltsverzeichnisButton();
			_gui.getInhaltsverzeichnisButton().get(counter).setOnAction(e -> {
				try {
					_logger.aktionButton("Inhaltsverzeichnis Button",
							_gui.getInhaltsverzeichnisButton().get(counter).getText());
					p.springeZuKapitel(counter);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
		}
	}

	/**
	 * Setzt die Funktionalität des Kapitelindexsliders
	 */
	private void funktionalitaetKapitelslider() {
		_gui.getKapitelSlider().valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				if (_gui.getKapitelSlider().isValueChanging()) {
					_logger.aktionSlider("Kapitelslider");
					int index = (int) (_aktuellePosition.get_kapitelanzahlArtikel()
							* (_gui.getKapitelSlider().getValue() / 100.0));
					try {
						p.springeZuKapitel(index);
					} catch (KapitelExistiertNichtException | FehlerhaftesWortException
							| WortExistiertNichtException e) {
						fehlermeldung(e);
					}
				}
			}
		});
	}

	/**
	 * Setzt die Funktionalität des Absatzindexsliders
	 */
	private void funktionalitaetAbsatzslider() {
		_gui.getAbsatzSlider().valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				if (_gui.getAbsatzSlider().isValueChanging()) {
					_logger.aktionSlider("	");
					int index = (int) (_aktuellePosition.get_absatzanzahlArtikel()
							* (_gui.getAbsatzSlider().getValue() / 100.0));
					try {
						p.springeZuAbsatz(index);
					} catch (FehlerhaftesWortException | AbsatzExistiertNichtException
							| WortExistiertNichtException e) {
						fehlermeldung(e);
					}

				}
			}
		});
	}

	/**
	 * Setzt die Funktionalität des Satzindexsliders
	 */
	private void funktionalitaetSatzslider() {
		_gui.getSatzSlider().valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				if (_gui.getSatzSlider().isValueChanging()) {
					_logger.aktionSlider("Satzslider");
					int index = (int) (_aktuellePosition.get_satzanzahlArtikel()
							* (_gui.getSatzSlider().getValue() / 100.0));
					try {
						p.springeZuSatz(index);
					} catch (FehlerhaftesWortException | FehlerhafterSatzException | SatzExistiertNichtException
							| WortExistiertNichtException e) {
						fehlermeldung(e);
					}
				}
			}
		});
	}

	/**
	 * Setzt die Funktionalität des Wortindexsliders
	 */
	private void funktionalitaetIndexSlider() {
		_gui.getIndexSlider().valueProperty().addListener(new InvalidationListener() {

			@Override
			public void invalidated(Observable arg0) {
				if (_gui.getIndexSlider().isValueChanging()) {
					_logger.aktionSlider("Indexslider");
					int index = (int) (_aktuellePosition.get_wortanzahlArtikel()
							* (_gui.getIndexSlider().getValue() / 100.0));
					try {
						p.springeZuIndex(index);
					} catch (FehlerhaftesWortException | WortExistiertNichtException e) {
						fehlermeldung(e);
					}
				}
			}
		});
	}

	/**
	 * Setzt die Funktionalität des Zeitsliders
	 */
	private void funktionalitaetZeitslider() {
		_gui.getZeitSlider().valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				// TODO Robustheit
				if (_gui.getZeitSlider().isValueChanging()) {
					_logger.aktionSlider("Zeitslider");
					Duration zeit = _aktuellePosition.get_zeitdauerArtikel()
							.multiply(_gui.getZeitSlider().getValue() / 100.0);
					p.springeZuZeitpunkt(zeit);
				}
			}
		});
	}

	/**
	 * Setzt die Funktionalität der Buttons
	 * 
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 */
	private void funktionalitaetButtons() throws FehlerhaftesWortException, FehlerhafterSatzException {
		_gui.getSkipZurueckZeitButton().setOnAction(e -> {
			p.springeLeichtZurueckZeit();
			_logger.aktionButton("leicht zurück Zeit Button");
		});
		_gui.getSkipZurueckKapitelButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Kapitel zurück Button");
				p.vorherigesKapitel();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSkipZurueckAbsatzButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Vorheriger Absatz Button");
				p.vorherigerAbsatz();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSkipZurueckSatzButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Vorheriger Satz Button");
				p.vorherigerSatz();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSkipZurueckSatzAnfangButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Satzanfang Button");
				p.satzanfang();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSkipZurueckAbsatzAnfangButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Absatzanfang Button");
				p.absatzanfang();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSkipZurueckKapitelAnfangButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Kapitelanfang Button");
				p.kapitelanfang();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSkipZueruckArtikelAnfang().setOnAction(e -> {
			_logger.aktionButton("Artikelanfang Button");
			p.artikelanfang();
		});
		_gui.getSkipZurueckIndexButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Leicht Zurück Index Button");
				p.springeLeichtZurueckIndex();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getPlayPauseButton().setOnAction(e -> {
			if (p.isPlaying()) { // TODO Logik aus Gui auslagern
				_logger.aktionButton("Pause Button");
				p.pauseAudio();
				_gui.wechselPlayPause(p.isPlaying());
			} else {
				try {
					_logger.aktionButton("Play Button");
					p.startAudio();
				} catch (EinstellungsException e2) {
					fehlermeldung(e2);
					// TODO
				} catch (Exception e1) {
					fehlermeldung(e1);
				}
				_gui.wechselPlayPause(p.isPlaying());
			}
		});
		_gui.getStopButton().setOnAction(e -> {
			_logger.aktionButton("Stop Button");
			p.stopAudio(false);
			_gui.wechselPlayPause(p.isPlaying());
		});
		_gui.getSkipVorZeitButton().setOnAction(e -> {
			_logger.aktionButton("Leicht Vor Zeit Button");
			p.springeLeichtVorZeit();
		});
		_gui.getSkipVorSatzButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Satz Vor Button");
				p.naechsterSatz();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSkipVorAbsatzButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Absatz Vor Button");
				p.naechsterAbsatz();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSkipVorKapitelButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Kapitel Vor Button");
				p.naechstesKapitel();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSkipVorIndexButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Index Vor Button");
				p.springeLeichtVorIndex();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSucheRueckwaertsButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Suche Rückwärts Button");
				String wort = _gui.getSuchFeld().getText();
				p.sucheWort_einzeln(Richtung.RÜCKWÄRTS, wort);
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSucheVorwaertsButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Suche Vorwärts Button");
				String wort = _gui.getSuchFeld().getText();
				p.sucheWort_einzeln(Richtung.VORWÄRTS, wort);
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getInhaltsverzeichnisVorlesenButton().setOnMousePressed(e -> {
			try {
				_logger.aktionButton("Inhaltsverzeichnis vorlesen Button gedrückt");
				p.leseInhaltsverzeichnis();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getInhaltsverzeichnisVorlesenButton().setOnMouseReleased(e -> {
			try {
				_logger.aktionButton("Inhaltsverzeichnis vorlesen Button losgelassen");
				p.brecheVorlesenInhaltsverzeichnisAb();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getZurueckButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Zurück Button");
				p.zurueck();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getVorButton().setOnAction(e -> {
			_logger.aktionButton("Vor Button");
			try {
				p.vor();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getArtikelSuchenButton().setOnAction(e -> {
			try {
				_logger.aktionButton("Artikel Suchen Button");
				p.starteArtikel(_gui.getSuchFeldArtikel().getText(), true);
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		// _gui.getSprachbefehlButton().setOnAction(
		// e -> sprachbefehlStart());
		_gui.getSprachbefehlButton().setOnMousePressed(e -> {
			try {
				_logger.aktionButton("Sprachbefehl Button gedrückt");
				p.brecheVorlesenInhaltsverzeichnisAb();
				_s.sprachbefehlStart();
			} catch (Exception e1) {
				fehlermeldung(e1);
			}
		});
		_gui.getSprachbefehlButton().setOnMouseReleased(e -> {
			try {
				_logger.aktionButton("Sprachbefehl Button gelöst");
				String sprachbefehl = _s.sprachbefehlEnde();
				_gui.setSprachbefehlText(sprachbefehl);
			} catch (Exception e1) {
				if (!_wizzard) {
					fehlermeldung(e1);
				}
			}
		});

		_gui.getReset().setOnAction(e -> {
			try {
				_logger.warnung("RESET", "RESET", "RESET");
				((Player_javafx) p).setGuiInit(true);
				p.starteArtikel(_aktuellePosition.get_aktuellerArtikel(), false);
			} catch (Exception e2) {
				fehlermeldung(e2);
			}
		});
	}

	/**
	 * Aktualisiert die wichtigen Parameter hier und in der Gui
	 */
	private void updateDaten() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				try {
					p.updatePosition();
				} catch (MalformedURLException | WortExistiertNichtException | FehlerhaftesWortException
						| SatzExistiertNichtException | FehlerhafterSatzException | AbsatzExistiertNichtException
						| KapitelExistiertNichtException e) {
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void update(java.util.Observable o, Object arg) {
		// TODO Auto-generated method stub
		if (arg == Elemente.ARTIKEL) {
			try {
				_logger.ereignis("Artikelwechsel");
				_gui.artikelwechsel();
				interaktionGui();
			} catch (FehlerhaftesWortException | FehlerhafterSatzException e) {
				e.printStackTrace();
			}
		} else if (arg instanceof String) {
			System.out.println(arg);
		}
	}

	private void fehlermeldung(Exception e) {
		if (e instanceof ArtikelExistiertNichtException) {
			new ArtikelExisiertNichtFehlermeldung(_primaryStage, e);
		} else if (e instanceof KapitelExistiertNichtException) {
			new KapitelExistiertNichtFehlermeldung(_primaryStage, e);
		} else if (e instanceof AbsatzExistiertNichtException) {
			new AbsatzExistiertNichtFehlermeldung(_primaryStage, e);
		} else if (e instanceof SatzExistiertNichtException) {
			new SatzExistiertNichtFehlermeldung(_primaryStage, e);
		} else if (e instanceof WortExistiertNichtException) {
			new WortExistiertNichtFehlermeldung(_primaryStage, e);
		} else if (e instanceof SprachbefehlException || e instanceof SprachbefehlAufnahmeException
				|| e instanceof SprachbefehlAuswertungException) {
			new Sprachbefehlfehlermeldung(_primaryStage, e);
		} else {
			new Fehlermeldung(_primaryStage, e, e.getMessage());
		}
		_logger.warnung(e);
	}
}
