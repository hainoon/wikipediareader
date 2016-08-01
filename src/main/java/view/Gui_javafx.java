package view;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.io.FileUtils;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Elemente;
import model.ModelPositionsparameter_Impl;
import player.Player;
import player.Player_javafx;

/**
 * GUI des Players. JavaFX-Version
 * 
 * @author marcel
 *
 */
@SuppressWarnings("restriction")
public class Gui_javafx implements Observer {

	private Player _p;
	private ModelPositionsparameter_Impl _mp;

	private boolean linksSichtbar = true;
	private boolean inhaltsverzeichnisSichtbar = true;

	private BorderPane pane;
	private MediaView mediaView;

	// wichtige Leisten + Bereiche
	private HBox mitte;
	private VBox linkliste;
	private VBox unten;
	private HBox oben;
	private VBox kontrollleiste;
	private VBox slider;
	private HBox anzeige;

	private HBox indexSliderBox;
	private HBox zeitSliderBox;
	private HBox satzSliderBox;
	private HBox absatzSliderBox;
	private HBox kapitelSliderBox;

	private HBox kapitelBox;
	private HBox absatzBox;
	private HBox satzBox;
	private HBox zeitBox;
	private HBox neben1;
	private HBox neben2;

	VBox inhalt;

	// Slider
	private Slider zeitSlider;
	private Slider indexSlider;
	private Slider satzSlider;
	private Slider absatzSlider;
	private Slider kapitelSlider;

	// Buttons
	// Button startButton;
	// Button pauseButton;
	private Button playPauseButton;
	private Button stopButton;
	private Button skipVorKapitelButton;
	private Button skipVorZeitButton;
	private Button skipVorAbsatzButton;
	private Button skipVorSatzButton;
	private Button skipVorIndexButton;
	private Button skipZurueckZeitButton;
	private Button skipZurueckSatzAnfangButton;
	private Button skipZurueckAbsatzAnfangButton;
	private Button skipZurueckKapitelAnfangButton;
	private Button skipZurueckSatzButton;
	private Button skipZurueckKapitelButton;
	private Button skipZurueckAbsatzButton;
	private Button skipZurueckIndexButton;
	private Button skipZueruckArtikelAnfang;
	private Button sucheRueckwaertsButton;
	private Button sucheVorwaertsButton;
	private Button vorButton;
	private Button zurueckButton;
	private Button artikelSuchenButton;
	private Button inhaltsverzeichnisVorlesenButton;
	private Button sprachbefehlButton;
	private Button zusaetzlicheSlider;
	private Button zusaetzlicheSteuerungButton;
	private Button reset;

	// Buttons die während der Laufzeit dynamisch erstellt und wieder gelöscht
	// werden
	private ArrayList<Button> inhaltsverzeichnisButton;
	private ArrayList<Button> linklisteButtons;

	// Suchfelder
	private TextField suchFeldWort;
	private TextField suchFeldArtikel;

	// Anzeigeelemente
	private Label zeitanzeige;
	private Label indexanzeige;
	private Label satzanzeige;
	private Label absatzanzeige;
	private Label kapitelanzeige;
	private Label wortanzeige;
	private Label sprachbefehl;
	private Label artikelsuchelabel;
	private Label wortsucheLabel;

	private String indexText = "Index: \t";
	private String zeitText = "Zeit: \t";
	private String satzText = "Satz: \t";
	private String absatzText = "Absatz: \t";
	private String kapitelText = "Kapitel: \t";
	private String wortText = "Wort: \t";

	// Parameter des Fensters allgemein
	private int breite = 1100;
	private int hoehe = 900;
	private boolean slidereingeblendet; // TODO Logik aus Gui auslagern
	private boolean zusaetzlicheSteuerungEingeblendet; // TODO Logik aus Gui
	// auslagern

	/**
	 * Konstruktor
	 * 
	 * @param primaryStage
	 * @param mediaView
	 * @param inhaltsverzeichnis
	 *            Liste mit Kapitelbezeichnern fürs Inhaltsverzeichnis
	 * @param links
	 *            Liste mit Linkbezeichnern
	 */

	public Gui_javafx(Player p, ModelPositionsparameter_Impl mp) {
		_mp = mp;
		_p = p;

		Stage primaryStage = ((Player_javafx) p).get_stage();
		MediaView mediaView = new MediaView(((Player_javafx) p).get_mediaPlayer()); // TODO
		// inhaltsverzeichnis = _p.getInhaltsverzeichnis(); // TODO
		// links = _p.getLinks();

		pane = new BorderPane();

		primaryStage.setTitle("Wikipedia Browser");
		Group root = new Group();
		Scene scene = new Scene(root, breite, hoehe);

		pane.setStyle("-fx-background-color: #bfc2c7;");
		Pane mvPane = new Pane() {
		};
		mvPane.getChildren().add(mediaView);
		mvPane.setStyle("-fx-background-color: black;");
		pane.setCenter(mvPane);

		erstelleKontrollleiste();
		erstelleSlider();
		erstelleAnzeige();

		unten = new VBox();
		unten.getChildren().add(anzeige);
		unten.getChildren().add((sprachbefehl = new Label("Letzter Sprachbefehl:")));
		unten.getChildren().add(slider);
		unten.getChildren().add(kontrollleiste);

		unten.setPadding(new Insets(5, 10, 5, 10));
		BorderPane.setAlignment(unten, Pos.CENTER);
		pane.setBottom(unten);

		mitte = new HBox();
		mitte.setPadding(new Insets(5, 10, 5, 10));
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(mitte);
		pane.setCenter(scroll);

		erstelleObereLeiste();
		addInhaltsverzeichnis(p.getInhaltsverzeichnis());
		mitte.getChildren().add((linkliste = new VBox()));
		linklisteButtons = new ArrayList<Button>();
		updateLinksliste();

		zusaetzlicheSteuerung();

		scene.setRoot(pane);

		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	private void zusaetzlicheSteuerung() {
		getZusaetzlicheSlider().setOnAction(e -> {
			if (isSlidereingeblendet()) {
				blendeZusaetzlicheSliderAus();
			} else {
				blendeZusaetzlicheSliderEin();
			}
		});
		getZusaetzlicheSteuerungButton().setOnAction(e -> {
			if (isZuesaetlicheSteuerungEingeblendet()) {
				blendeZusaetzlicheSteuerungAus();
			} else {
				blendeZusaetzicheSteuerungEin();
			}
		});
	}

	/**
	 * Erstellt den Bereich in dem aktuelle Informationen zum Vorlesestand
	 * angezeigt werden
	 */
	private void erstelleAnzeige() {
		anzeige = new HBox();
		anzeige.setAlignment(Pos.BOTTOM_LEFT);
		zeitanzeige = new Label();
		indexanzeige = new Label();
		satzanzeige = new Label();
		absatzanzeige = new Label();
		kapitelanzeige = new Label();
		wortanzeige = new Label();

		anzeige.getChildren().add(new Label(zeitText));
		anzeige.getChildren().add(zeitanzeige);
		anzeige.getChildren().add(new Label(indexText));
		anzeige.getChildren().add(indexanzeige);
		anzeige.getChildren().add(new Label(satzText));
		anzeige.getChildren().add(satzanzeige);
		anzeige.getChildren().add(new Label(absatzText));
		anzeige.getChildren().add(absatzanzeige);
		anzeige.getChildren().add(new Label(kapitelText));
		anzeige.getChildren().add(kapitelanzeige);
		anzeige.getChildren().add(new Label(wortText));
		anzeige.getChildren().add(wortanzeige);
	}

	/**
	 * Erstellt den Bereich in dem sich die Slider befinden
	 */
	private void erstelleSlider() {
		slider = new VBox();
		slider.setAlignment(Pos.CENTER);

		zusaetzlicheSlider = new Button("",
				new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/mehr.png"))));

		indexSliderBox = new HBox();
		indexSlider = new Slider();
		HBox.setHgrow(indexSlider, Priority.ALWAYS);
		indexSlider.setMinWidth(50);
		indexSlider.setMaxWidth(Double.MAX_VALUE);
		indexSliderBox.getChildren().add(new Label(indexText));
		indexSliderBox.getChildren().add(indexSlider);
		indexSliderBox.getChildren().add(zusaetzlicheSlider);
		slider.getChildren().add(indexSliderBox);

		zeitSliderBox = new HBox();
		zeitSlider = new Slider();
		HBox.setHgrow(zeitSlider, Priority.ALWAYS);
		zeitSlider.setMinWidth(50);
		zeitSlider.setMaxWidth(Double.MAX_VALUE);
		zeitSliderBox.getChildren().add(new Label("Zeit: \t"));
		zeitSliderBox.getChildren().add(zeitSlider);
		// slider.getChildren().add(zeitSliderBox);

		satzSliderBox = new HBox();
		satzSlider = new Slider();
		HBox.setHgrow(satzSlider, Priority.ALWAYS);
		satzSlider.setMinWidth(50);
		satzSlider.setMaxWidth(Double.MAX_VALUE);
		satzSliderBox.getChildren().add(new Label("Satz: \t"));
		satzSliderBox.getChildren().add(satzSlider);
		// slider.getChildren().add(satzSliderBox);

		absatzSliderBox = new HBox();
		absatzSlider = new Slider();
		HBox.setHgrow(absatzSlider, Priority.ALWAYS);
		absatzSlider.setMinWidth(50);
		absatzSlider.setMaxWidth(Double.MAX_VALUE);
		absatzSliderBox.getChildren().add(new Label("Absatz: \t"));
		absatzSliderBox.getChildren().add(absatzSlider);
		// slider.getChildren().add(absatzSliderBox);

		kapitelSliderBox = new HBox();
		kapitelSlider = new Slider();
		HBox.setHgrow(kapitelSlider, Priority.ALWAYS);
		kapitelSlider.setMinWidth(50);
		kapitelSlider.setMaxWidth(Double.MAX_VALUE);
		kapitelSliderBox.getChildren().add(new Label("Kapitel: \t"));
		kapitelSliderBox.getChildren().add(kapitelSlider);
		// slider.getChildren().add(kapitelSliderBox);
	}

	public void blendeZusaetzlicheSliderEin() {
		slider.getChildren().add(zeitSliderBox);
		slider.getChildren().add(satzSliderBox);
		slider.getChildren().add(absatzSliderBox);
		slider.getChildren().add(kapitelSliderBox);
		setZusaetzlicheSliderText();
		slidereingeblendet = true;
	}

	public void blendeZusaetzlicheSliderAus() {
		slider.getChildren().remove(zeitSliderBox);
		slider.getChildren().remove(satzSliderBox);
		slider.getChildren().remove(absatzSliderBox);
		slider.getChildren().remove(kapitelSliderBox);
		setZusaetzlicheSliderText();
		slidereingeblendet = false;
	}

	/**
	 * Erstellt den Bereich in dem die Buttons zur Navigation innerhalb des
	 * Artikels liegen
	 */
	private void erstelleKontrollleiste() {
		kontrollleiste = new VBox();
		kontrollleiste.setAlignment(Pos.CENTER);

		double buttongroesse = buttons();

		HBox hauptkontrolle = new HBox();
		ArrayList<Button> hauptButtons = new ArrayList<Button>();
		hauptkontrolle.setAlignment(Pos.CENTER);
		hauptButtons.add(skipZurueckIndexButton);
		// hauptButtons.add(startButton);
		hauptButtons.add(playPauseButton);
		hauptButtons.add(stopButton);
		hauptButtons.add(skipVorIndexButton);
		hauptButtons.add(inhaltsverzeichnisVorlesenButton);
		hauptButtons.add(sprachbefehlButton);
		hauptButtons.add(zusaetzlicheSteuerungButton);
		for (Button b : hauptButtons) {
			hauptkontrolle.getChildren().add(b);
			b.setMinWidth(buttongroesse / hauptButtons.size());
			b.setMaxWidth(buttongroesse / hauptButtons.size());
			b.setMinHeight(buttongroesse / 15);
			b.setMaxHeight(buttongroesse / 15);
		}

		kapitelButtons(buttongroesse, hauptButtons);
		absatzButtons(buttongroesse, hauptButtons);
		satzButtons(buttongroesse, hauptButtons);
		zeitButtons(buttongroesse, hauptButtons);

		kontrollleiste.getChildren().add(hauptkontrolle); // TODO in
															// entsprechende
															// Methode
															// abschieben
	}

	/**
	 * @param buttongroesse
	 * @param hauptButtons
	 */
	private void zeitButtons(double buttongroesse, ArrayList<Button> hauptButtons) {
		zeitBox = new HBox();
		ArrayList<Button> zeitButtons = new ArrayList<Button>();
		zeitBox.getChildren().add(new Label(zeitText));
		zeitButtons.add(skipZurueckZeitButton);
		zeitButtons.add(skipZueruckArtikelAnfang);
		zeitButtons.add(skipVorZeitButton);
		for (Button b : zeitButtons) {
			zeitBox.getChildren().add(b);
			b.setMinWidth(buttongroesse / hauptButtons.size());
			b.setMaxWidth(buttongroesse / hauptButtons.size());
		}
	}

	/**
	 * @param buttongroesse
	 * @param hauptButtons
	 */
	private void satzButtons(double buttongroesse, ArrayList<Button> hauptButtons) {
		satzBox = new HBox();
		ArrayList<Button> satzButtons = new ArrayList<Button>();
		satzBox.getChildren().add(new Label(satzText));
		satzButtons.add(skipZurueckSatzButton);
		satzButtons.add(skipZurueckSatzAnfangButton);
		satzButtons.add(skipVorSatzButton);
		for (Button b : satzButtons) {
			satzBox.getChildren().add(b);
			b.setMinWidth(buttongroesse / hauptButtons.size());
			b.setMaxWidth(buttongroesse / hauptButtons.size());
		}
	}

	/**
	 * @param buttongroesse
	 * @param hauptButtons
	 */
	private void absatzButtons(double buttongroesse, ArrayList<Button> hauptButtons) {
		absatzBox = new HBox();
		ArrayList<Button> absatzButtons = new ArrayList<Button>();
		absatzBox.getChildren().add(new Label(absatzText));
		absatzButtons.add(skipZurueckAbsatzButton);
		absatzButtons.add(skipZurueckAbsatzAnfangButton);
		absatzButtons.add(skipVorAbsatzButton);
		for (Button b : absatzButtons) {
			absatzBox.getChildren().add(b);
			b.setMinWidth(buttongroesse / hauptButtons.size());
			b.setMaxWidth(buttongroesse / hauptButtons.size());
		}
	}

	/**
	 * @param buttongroesse
	 * @param hauptButtons
	 */
	private void kapitelButtons(double buttongroesse, ArrayList<Button> hauptButtons) {
		kapitelBox = new HBox();
		ArrayList<Button> kapitelButtons = new ArrayList<Button>();
		kapitelBox.getChildren().add(new Label(kapitelText));
		kapitelButtons.add(skipZurueckKapitelButton);
		kapitelButtons.add(skipZurueckKapitelAnfangButton);
		kapitelButtons.add(skipVorKapitelButton);
		for (Button b : kapitelButtons) {
			kapitelBox.getChildren().add(b);
			b.setMinWidth(buttongroesse / hauptButtons.size());
			b.setMaxWidth(buttongroesse / hauptButtons.size());
		}
	}

	/**
	 * @return
	 */
	private double buttons() {
		double buttongroesse = breite * 0.9;

		// Image playImage = new Image(getClass().getResourceAsStream("Icons/PNG/play.png"));
		Image pauseImage = new Image(getClass().getResourceAsStream("Icons/PNG/pause.png"));
		Image stopImage = new Image(getClass().getResourceAsStream("Icons/PNG/stop.png"));
		Image anfangImage = new Image(getClass().getResourceAsStream("Icons/PNG/prev 1.png"));
		Image zurueckImage = new Image(getClass().getResourceAsStream("Icons/PNG/prev 2.png"));
		Image vorImage = new Image(getClass().getResourceAsStream("Icons/PNG/next 2.png"));
		Image spracheImage = new Image(getClass().getResourceAsStream("Icons/PNG/mic.png"));

		skipZurueckZeitButton = new Button("", new ImageView(anfangImage));
		skipZurueckKapitelButton = new Button("", new ImageView(anfangImage));
		skipZurueckAbsatzButton = new Button("", new ImageView(anfangImage));
		skipZurueckSatzButton = new Button("", new ImageView(anfangImage));
		skipZurueckIndexButton = new Button("", new ImageView(zurueckImage));
		// startButton = new Button("", new ImageView(playImage));
		playPauseButton = new Button("", new ImageView(pauseImage));
		stopButton = new Button("", new ImageView(stopImage));
		skipVorKapitelButton = new Button("", new ImageView(vorImage));
		skipVorZeitButton = new Button("", new ImageView(vorImage));
		skipVorSatzButton = new Button("", new ImageView(vorImage));
		skipVorAbsatzButton = new Button("", new ImageView(vorImage));
		skipVorIndexButton = new Button("", new ImageView(vorImage));
		skipZurueckSatzAnfangButton = new Button("Anfang");
		skipZurueckAbsatzAnfangButton = new Button("Anfang");
		skipZurueckKapitelAnfangButton = new Button("Anfang");
		skipZueruckArtikelAnfang = new Button("Anfang");
		zusaetzlicheSteuerungButton = new Button("",
				new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/mehr.png"))));
		sprachbefehlButton = new Button("", new ImageView(spracheImage));
		inhaltsverzeichnisVorlesenButton = new Button("Inhaltsverzeichnis");
		return buttongroesse;
	}

	public void blendeZusaetzicheSteuerungEin() {
		neben1 = new HBox();
		neben2 = new HBox();
		neben1.getChildren().add(kapitelBox);
		neben1.getChildren().add(absatzBox);
		neben2.getChildren().add(satzBox);
		neben2.getChildren().add(zeitBox);

		kontrollleiste.getChildren().add(neben1);
		kontrollleiste.getChildren().add(neben2);
		setZusaetzlicheSteuerungButtonText();
		zusaetzlicheSteuerungEingeblendet = true;
	}

	public void blendeZusaetzlicheSteuerungAus() {
		kontrollleiste.getChildren().remove(neben1);
		kontrollleiste.getChildren().remove(neben2);
		setZusaetzlicheSteuerungButtonText();
		zusaetzlicheSteuerungEingeblendet = false;
	}

	/**
	 * Fügt der Gui ein Inhaltsverzeichnis hinzu
	 * 
	 * @param inhaltsverzeichnis
	 *            Liste mit Kapitelbezeichnern
	 */
	private void addInhaltsverzeichnis(ArrayList<String> inhaltsverzeichnis) {
		if (inhaltsverzeichnisSichtbar) {
			inhalt = new VBox();

			inhaltsverzeichnisButton = new ArrayList<Button>();

			inhalt.getChildren().add(new Label("Inhaltsverzeichnis:"));
			for (String s : inhaltsverzeichnis) {
				Button b = new Button(s);
				inhaltsverzeichnisButton.add(b);
				inhalt.getChildren().add(b);
			}
			for (Button b : inhaltsverzeichnisButton) {
				b.setMinWidth(200);
				b.setMaxWidth(200);
			}
			mitte.getChildren().add(inhalt);
		}
	}

	public void updateInhaltsverzeichnis() {
		mitte.getChildren().remove(inhalt);

		addInhaltsverzeichnis(_p.getInhaltsverzeichnis());
	}

	public void inhaltsverzeichnisAnzeigen(boolean anzeigen) {
		inhaltsverzeichnisSichtbar = anzeigen;
		inhalt.setVisible(inhaltsverzeichnisSichtbar);
	}

	public void annonymisiereInhaltsverzeichnis() {
		for (int i = 0; i < inhaltsverzeichnisButton.size(); ++i) {
			Button b = inhaltsverzeichnisButton.get(i);
			String artikelnummer = b.getText().split(" ")[0];
			b.setText(i + " (= Section " + artikelnummer + ")");
		}
	}

	/**
	 * Erstellt eine Anzeige der Links
	 * 
	 * @param links
	 */
	public void updateLinksliste() {
		mitte.getChildren().remove(linkliste);
		if (linksSichtbar) {
			linkliste = new VBox();
			linklisteButtons = new ArrayList<Button>();

			linkliste.getChildren().add(new Label("Links im Umfeld:"));

			for (String s : _p.getLinks()) {
				Button b = new Button(s);
				linklisteButtons.add(b);
				linkliste.getChildren().add(b);
			}
			for (Button b : linklisteButtons) {
				b.setMinWidth(200);
				b.setMaxWidth(200);
			}
			mitte.getChildren().add(linkliste);
		}
	}

	/**
	 * Fügt der GUI die Suchunktion hinzu
	 */
	private void erstelleObereLeiste() {
		oben = new HBox();
		// oben.setAlignment(Pos.CENTER);

		wortsucheLabel = new Label("Suche nach Satz mit Wort: ");
		sucheRueckwaertsButton = new Button("",
				new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/arrow 2.png"))));
		suchFeldWort = new TextField();
		sucheVorwaertsButton = new Button("",
				new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/arrow 1.png"))));

		vorButton = new Button("", new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/arrow 1.png"))));
		zurueckButton = new Button("",
				new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/arrow 2.png"))));

		suchFeldArtikel = new TextField();
		artikelSuchenButton = new Button("",
				new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/search.png"))));

		oben.getChildren().add(zurueckButton);
		oben.getChildren().add(vorButton);
		Label leer = new Label();
		leer.setMinWidth(50);
		oben.getChildren().add(leer);

		artikelsuchelabel = new Label("Suche nach Artikel: ");
		oben.getChildren().add(artikelsuchelabel);
		oben.getChildren().add(suchFeldArtikel);
		oben.getChildren().add(artikelSuchenButton);
		Label leer2 = new Label();
		leer2.setMinWidth(50);
		oben.getChildren().add(leer2);

		oben.getChildren().add(wortsucheLabel);
		oben.getChildren().add(sucheRueckwaertsButton);
		oben.getChildren().add(suchFeldWort);
		oben.getChildren().add(sucheVorwaertsButton);
		
		reset = new Button("reset");
		oben.getChildren().add(reset);

		pane.setTop(oben);
	}

	// TODO Getter und Setter die durch Strukturänderung nicht mehr benötigt
	// werden ggf entfernen (innerhalb dieser Klasse werden teilweise besagt
	// getter und setter verwendet)

	// /**
	// * Button der den Artikel startet
	// *
	// * @return the startButton
	// */
	// public Button getStartButton() {
	// return startButton;
	// }

	/**
	 * Button der den Artikel pausiert
	 * 
	 * @return the pauseButton
	 */
	public Button getPlayPauseButton() {
		return playPauseButton;
	}

	/**
	 * Button der den Artikel stoppt
	 * 
	 * @return the stopButton
	 */
	public Button getStopButton() {
		return stopButton;
	}

	/**
	 * Button bei dem um einen festgelegten Zeitwert vor gesprungen wird
	 * 
	 * @return the skipVorZeitButton
	 */
	public Button getSkipVorZeitButton() {
		return skipVorZeitButton;
	}

	/**
	 * Button bei dem um einen festgelegten Zeitwert zurück gesprungen wird
	 * 
	 * @return the skipZurueckZeitButton
	 */
	public Button getSkipZurueckZeitButton() {
		return skipZurueckZeitButton;
	}

	/**
	 * Button bei dem um einen Satz vor gesprungen wird
	 * 
	 * @return the skipVorSatzButton
	 */
	public Button getSkipVorSatzButton() {
		return skipVorSatzButton;
	}

	/**
	 * Button bei dem zum Satzanfang zurück gesprungen wird
	 * 
	 * @return the skipZurueckSatzAnfangButton
	 */
	public Button getSkipZurueckSatzAnfangButton() {
		return skipZurueckSatzAnfangButton;
	}

	/**
	 * Button bei dem zum vorherigen Satz zurück gesprungen wird
	 * 
	 * @return the skipZurueckSatzButton
	 */
	public Button getSkipZurueckSatzButton() {
		return skipZurueckSatzButton;
	}

	/**
	 * Button bei dem um eine festgelegte Wortanzahl vor gesprungen wird
	 * 
	 * @return the skipVorIndexButton
	 */
	public Button getSkipVorIndexButton() {
		return skipVorIndexButton;
	}

	/**
	 * Button bei dem um eine festgelegte Wortanzahl zurück gesprungen wird
	 * 
	 * @return the skipZurueckIndexButton
	 */
	public Button getSkipZurueckIndexButton() {
		return skipZurueckIndexButton;
	}

	/**
	 * Button bei dem um ein Kapitel vor gesprungen wird
	 * 
	 * @return the skipVorKapitelButton
	 */
	public Button getSkipVorKapitelButton() {
		return skipVorKapitelButton;
	}

	/**
	 * Button bei dem zum Anfang des Artikels gesprungen wird
	 * 
	 * @return the skipZurueckKapitelAnfangButton
	 */
	public Button getSkipZurueckKapitelAnfangButton() {
		return skipZurueckKapitelAnfangButton;
	}

	/**
	 * Button bei dem zum vorherigen Kapitel gesprungen wird
	 * 
	 * @return the skipZurueckKapitelButton
	 */
	public Button getSkipZurueckKapitelButton() {
		return skipZurueckKapitelButton;
	}

	/**
	 * Button bei dem um einen Absatz weiter gesprungen wird
	 * 
	 * @return the skipVorAbsatzButton
	 */
	public Button getSkipVorAbsatzButton() {
		return skipVorAbsatzButton;
	}

	/**
	 * Button bei dem zum Anfang des Absatzes gesprungen wird
	 * 
	 * @return the skipZurueckAbsatzAnfangButton
	 */
	public Button getSkipZurueckAbsatzAnfangButton() {
		return skipZurueckAbsatzAnfangButton;
	}

	/**
	 * Button bei dem zum vorherigen Absatz gesprungen wird
	 * 
	 * @return the skipZurueckAbsatzButton
	 */
	public Button getSkipZurueckAbsatzButton() {
		return skipZurueckAbsatzButton;
	}

	/**
	 * Button bei dem zum Artikelanfang gesprungen wird
	 * 
	 * @return the skipZueruckArtikelAnfang
	 */
	public Button getSkipZueruckArtikelAnfang() {
		return skipZueruckArtikelAnfang;
	}

	/**
	 * Button bei dem im Artikel rückwärts nach einem Begriff gesucht wird
	 * 
	 * @return the sucheRueckwaertsButton
	 */
	public Button getSucheRueckwaertsButton() {
		return sucheRueckwaertsButton;
	}

	/**
	 * Button bei dem im Artikel vorwärts nach einem Begriff gesucht wird
	 * 
	 * @return the sucheVorwaertsButton
	 */
	public Button getSucheVorwaertsButton() {
		return sucheVorwaertsButton;
	}

	/**
	 * @return the zusaetzlicheSteuerungButton
	 */
	public Button getZusaetzlicheSteuerungButton() {
		return zusaetzlicheSteuerungButton;
	}

	public void setZusaetzlicheSteuerungButtonText() {
		if (zusaetzlicheSteuerungEingeblendet) {
			zusaetzlicheSteuerungButton
					.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/mehr.png"))));
		} else {
			zusaetzlicheSteuerungButton
					.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/weniger.png"))));
		}
	}

	/**
	 * Button für Testfunktion
	 * 
	 * @return the test
	 */
	public Button getInhaltsverzeichnisVorlesenButton() {
		return inhaltsverzeichnisVorlesenButton;
	}

	public Button getSprachbefehlButton() {
		return sprachbefehlButton;
	}

	/**
	 * Button für Artikel-Vor-Funktion
	 * 
	 * @return the vorButton
	 */
	public Button getVorButton() {
		return vorButton;
	}

	/**
	 * Button für Artikel-Zurück-Funktion
	 * 
	 * @return the zurueckButton
	 */
	public Button getZurueckButton() {
		return zurueckButton;
	}

	/**
	 * @return the artikelSuchenButton
	 */
	public Button getArtikelSuchenButton() {
		return artikelSuchenButton;
	}

	/**
	 * Liste mit den Buttons fürs Inhaltsverzeichnis
	 * 
	 * @return the inhaltsverzeichnisButton
	 */
	public ArrayList<Button> getInhaltsverzeichnisButton() {
		return inhaltsverzeichnisButton;
	}

	/**
	 * Liste mit Buttons für die Linkliste
	 * 
	 * @return the linklisteButtons
	 */
	public ArrayList<Button> getLinklisteButtons() {
		return linklisteButtons;
	}

	/**
	 * @return the zusaetzlicheSlider
	 */
	public Button getZusaetzlicheSlider() {
		return zusaetzlicheSlider;
	}

	/**
	 * @param zusaetzlicheSlider
	 *            the zusaetzlicheSlider to set
	 */
	public void setZusaetzlicheSliderText() {
		if (slidereingeblendet) {
			zusaetzlicheSlider
					.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/mehr.png"))));
		} else {
			zusaetzlicheSlider
					.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/weniger.png"))));
		}
	}

	/**
	 * @return the slidereingeblendet
	 */
	public boolean isSlidereingeblendet() {
		return slidereingeblendet;
	}

	/**
	 * Setzen der Zeitanzeige
	 * 
	 * @param zeitanzeigeWert
	 *            aktueller Zeitwert
	 * @param zeitgrenzeWert
	 *            Zeitdauer des gesamten Artikels
	 */
	public void setZeitanzeige(Duration zeitanzeigeWert, Duration zeitgrenzeWert) {
		zeitanzeige.setText(formatTime(zeitanzeigeWert, zeitgrenzeWert) + "\t");
	}

	/**
	 * Setzen der Wortindex-Anzeige
	 * 
	 * @param indexanzeigeWert
	 *            aktueller Wortindex
	 * @param indexGrenzwert
	 *            Wortanzahl im gesamten Artikel
	 */
	public void setIndexanzeige(int indexanzeigeWert, int indexGrenzwert) {
		indexanzeige.setText(indexanzeigeWert + "/" + indexGrenzwert + "\t");
	}

	/**
	 * Setzen der Satzindex-Anzeige
	 * 
	 * @param satzanzeigeWert
	 *            aktueller Satzindex
	 * @param satzGrenzwert
	 *            Anzahl der Sätze im Artikel
	 */
	public void setSatzanzeige(int satzanzeigeWert, int satzGrenzwert) {
		satzanzeige.setText(satzanzeigeWert + "/" + satzGrenzwert + "\t");
	}

	/**
	 * Setzen der Absatzindex-Anzeige
	 * 
	 * @param absatzanzeigeWert
	 *            aktueller Absatzindex
	 * @param absatzGrenzwert
	 *            Anzahl der Absätze im Artikel
	 */
	public void setAbsatzanzeige(int absatzanzeigeWert, int absatzGrenzwert) {
		absatzanzeige.setText(absatzanzeigeWert + "/" + absatzGrenzwert + "\t");
	}

	/**
	 * Setzen der Kapitelindex-Anzeige
	 * 
	 * @param kapitelanzeigeWert
	 *            aktueller Kapitelindex
	 * @param kapitelGrenzwert
	 *            Anzahl der Kapitel im Artikel
	 */
	public void setKapitelanzeige(int kapitelanzeigeWert, int kapitelGrenzwert) {
		kapitelanzeige.setText(kapitelanzeigeWert + "/" + kapitelGrenzwert + "\t");
	}

	/**
	 * Setzen der Wortanzeige
	 * 
	 * @param wortanzeigeWert
	 *            aktuelles Wort
	 */
	public void setWortanzeige(String wortanzeigeWert) {
		wortanzeige.setText(wortanzeigeWert + "\t");
	}

	/**
	 * Zeitslider
	 * 
	 * @return the zeitSlider
	 */
	public Slider getZeitSlider() {
		return zeitSlider;
	}

	/**
	 * Wortindex-Slider
	 * 
	 * @return the indexSlider
	 */
	public Slider getIndexSlider() {
		return indexSlider;
	}

	/**
	 * Satzindex-Slider
	 * 
	 * @return the satzSlider
	 */
	public Slider getSatzSlider() {
		return satzSlider;
	}

	/**
	 * Kapitelindex-Slider
	 * 
	 * @return the kapitelSlider
	 */
	public Slider getKapitelSlider() {
		return kapitelSlider;
	}

	/**
	 * Absatzindex-Slider
	 * 
	 * @return the absatzSlider
	 */
	public Slider getAbsatzSlider() {
		return absatzSlider;
	}

	/**
	 * Suchfeld Wortsuche
	 * 
	 * @return the suchFeld
	 */
	public TextField getSuchFeld() {
		return suchFeldWort;
	}

	/**
	 * @return the suchFeldArtikel
	 */
	public TextField getSuchFeldArtikel() {
		return suchFeldArtikel;
	}

	/**
	 * Formatieren von zwei Zeitwerten auf das Format hh:mm:ss/hh:mm:ss Quelle:
	 * https://blog.idrsolutions.com/2015/04/javafx-mp3-music-player-embedding
	 * -sound-in-your-application/ TODO
	 * 
	 * @param aktuell
	 *            aktuelle Zeit
	 * @param grenze
	 *            Zeitobergrenze
	 * @return
	 */
	private String formatTime(Duration aktuell, Duration grenze) {
		int intElapsed = (int) Math.floor(aktuell.toSeconds());
		int elapsedHours = intElapsed / (60 * 60);
		if (elapsedHours > 0) {
			intElapsed -= elapsedHours * 60 * 60;
		}
		int elapsedMinutes = intElapsed / 60;
		int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

		if (grenze.greaterThan(Duration.ZERO)) {
			int intDuration = (int) Math.floor(grenze.toSeconds());
			int durationHours = intDuration / (60 * 60);
			if (durationHours > 0) {
				intDuration -= durationHours * 60 * 60;
			}
			int durationMinutes = intDuration / 60;
			int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
			if (durationHours > 0) {
				return String.format("%d:%02d:%02d/%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds,
						durationHours, durationMinutes, durationSeconds);
			} else {
				return String.format("%02d:%02d/%02d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes,
						durationSeconds);
			}
		} else {
			if (elapsedHours > 0) {
				return String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
			} else {
				return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
			}
		}
	}

	public boolean isZuesaetlicheSteuerungEingeblendet() {
		return zusaetzlicheSteuerungEingeblendet;
	}

	public void setSprachbefehlText(String text) {
		if (text.equals("")) {
			sprachbefehl.setText("Letzter Sprachbefehl: \t Kein Befehl erkannt");
		} else {
			sprachbefehl.setText("Letzter Sprachbefehl: \t" + text);
		}
	}

	public void wechselPlayPause(boolean isPlaying) {
		if (!isPlaying) {
			playPauseButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/pause.png"))));
		} else {
			playPauseButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("Icons/PNG/play.png"))));
		}
	}

	@Override
	public void update(java.util.Observable o, Object arg) {
		Elemente e = (Elemente) arg;
		switch (e) {
		case ZEIT:
			aktualisiereZeitanzeige();
			aktualisiereZeitslider();
			break;
		case INDEX:
			aktualisiereIndexAnzeige();
			aktualisiereIndexSlider();
			aktualisiereGui_Links();
			aktualisiereGui_Kapitelpointer();
			aktualisiereLinkpointer();
			break;
		case SATZ:
			aktualisiereSatzAnzeige();
			aktualisiereSatzSlider();
			break;
		case ABSATZ:
			aktualisiereAbsatzanzeige();
			aktualisiereAbsatzSlider();
			break;
		case KAPITEL:
			aktualisiereKapitelAnzeige();
			aktualisiereKapitelSlider();
			break;
		case WORT:
			aktualisiereWortAnzeige();
			break;
		// Artikel wird jetzt in Controller geregelt, weil sich die Interaktion
		// auch ändert
		// case ARTIKEL:
		// // TODO unnötige Anweisungen entfernen
		// updateInhaltsverzeichnis();
		// aktualisiereGui_Links();
		// aktualisiereZeitanzeige();
		// aktualisiereZeitslider();
		// aktualisiereSatzAnzeige();
		// aktualisiereSatzSlider();
		// aktualisiereAbsatzanzeige();
		// aktualisiereAbsatzSlider();
		// aktualisiereKapitelAnzeige();
		// aktualisiereKapitelSlider();
		// aktualisiereGui_Kapitelpointer();
		// links = _p.getLinks();
		// aktualisiereLinkpointer();
		// break;
		default:
			System.err.println("Fehlerhaftes Update Positionsparameter");// TODO
		}
	}

	public void artikelwechsel() {
		// TODO unnötige Anweisungen entfernen
		updateInhaltsverzeichnis();
		aktualisiereGui_Links();
		aktualisiereZeitanzeige();
		aktualisiereZeitslider();
		aktualisiereSatzAnzeige();
		aktualisiereSatzSlider();
		aktualisiereAbsatzanzeige();
		aktualisiereAbsatzSlider();
		aktualisiereKapitelAnzeige();
		aktualisiereKapitelSlider();
		aktualisiereGui_Kapitelpointer();
		aktualisiereLinkpointer();
	}

	/**
	 * Aktualisiert die Anzeige von Links
	 */
	private void aktualisiereGui_Links() {
		ArrayList<String> links = _p.getLinks();
		if (links.size() != getLinklisteButtons().size()) {
			updateLinksliste();
		} else if (getLinklisteButtons().size() > 0 && links.size() > 0) {
			if (!getLinklisteButtons().get(getLinklisteButtons().size() - 1).getText()
					.equals(links.get(links.size() - 1))) {
				updateLinksliste();
			}
		}

		for (int i = 0; i < getLinklisteButtons().size(); ++i) {
			String s = getLinklisteButtons().get(i).getText();
			getLinklisteButtons().get(i).setOnAction(e -> {
				try {
					_p.starteArtikel(s, true); // TODO in Controler einfügen
				} catch (Exception e1) {
					// TODO
					e1.printStackTrace();
				}
			});
		}

	}

	public void setLinksanzeigen(boolean anzeigen) {
		linksSichtbar = anzeigen;
		linkliste.setVisible(linksSichtbar);
	}

	/**
	 * Aktualisiert den Zeitindexslider
	 */
	private void aktualisiereZeitslider() {
		Duration d = _mp.get_aktuelleZeit();
		getZeitSlider().setDisable(d.isUnknown());
		if (!getZeitSlider().isDisabled() && d.greaterThan(Duration.ZERO) && !getZeitSlider().isValueChanging()) {
			getZeitSlider().setValue(d.divide(_mp.get_zeitdauerArtikel()).toMillis() * 100);
		}
	}

	/**
	 * Aktualisiert den Wortindexslider
	 */
	private void aktualisiereIndexSlider() {
		int aktuelleIndex = _mp.get_aktuellerIndex();
		int anzahlIndex = _mp.get_wortanzahlArtikel();
		getIndexSlider().setDisable(ungueltigesIntervall(aktuelleIndex, anzahlIndex));
		if (!getIndexSlider().isDisabled() && !getIndexSlider().isValueChanging()) {
			getIndexSlider().setValue((aktuelleIndex * 100.0) / anzahlIndex);
		}
	}

	/**
	 * Aktualisiert den Satzindexslider
	 */
	private void aktualisiereSatzSlider() {
		int aktuellerSatz = _mp.get_aktuellerSatz();
		int anzahlSaetze = _mp.get_satzanzahlArtikel();
		getSatzSlider().setDisable(ungueltigesIntervall(aktuellerSatz, anzahlSaetze));
		if (!getSatzSlider().isDisabled() && !getSatzSlider().isValueChanging()) {
			getSatzSlider().setValue((aktuellerSatz * 100.0) / anzahlSaetze);
		}
	}

	/**
	 * Altualisiert den Absatzindexslider
	 */
	private void aktualisiereAbsatzSlider() {
		int aktuellerAbsatz = _mp.get_aktuellerAbsatz();
		int anzahlAbsaetze = _mp.get_absatzanzahlArtikel();
		getAbsatzSlider().setDisable(ungueltigesIntervall(aktuellerAbsatz, anzahlAbsaetze));
		if (!getAbsatzSlider().isDisabled() && !getAbsatzSlider().isValueChanging()) {
			getAbsatzSlider().setValue((aktuellerAbsatz * 100.0) / anzahlAbsaetze);
		}
	}

	/**
	 * Aktualisiert den Kapitelindexslider
	 */
	private void aktualisiereKapitelSlider() {
		int aktuellesKapitel = _mp.get_aktuellesKapitel();
		int anzahlKapitel = _mp.get_kapitelanzahlArtikel();
		getKapitelSlider().setDisable(ungueltigesIntervall(aktuellesKapitel, anzahlKapitel));
		if (!getKapitelSlider().isDisabled() && !getKapitelSlider().isValueChanging()) {
			getKapitelSlider().setValue((aktuellesKapitel * 100.0) / anzahlKapitel);
		}
	}

	/**
	 * Aktualisiert die Wortanzeige
	 */
	private void aktualisiereWortAnzeige() {
		setWortanzeige(_mp.get_aktuellesWort());
	}

	/**
	 * Aktualisiert die Kapitelanzeige
	 */
	private void aktualisiereKapitelAnzeige() {
		setKapitelanzeige(_mp.get_aktuellesKapitel(), _mp.get_kapitelanzahlArtikel());
	}

	/**
	 * Aktualisiert die Absatzanzeige
	 */
	private void aktualisiereAbsatzanzeige() {
		setAbsatzanzeige(_mp.get_aktuellerAbsatz(), _mp.get_absatzanzahlArtikel());
	}

	/**
	 * Aktualisiert die Satzanzeige
	 */
	private void aktualisiereSatzAnzeige() {
		setSatzanzeige(_mp.get_aktuellerSatz(), _mp.get_satzanzahlArtikel());
	}

	/**
	 * Aktualisiert die Indexanzeige
	 */
	private void aktualisiereIndexAnzeige() {
		setIndexanzeige(_mp.get_aktuellerIndex(), _mp.get_wortanzahlArtikel());
	}

	/**
	 * Aktualisiert die Zeitanzeige
	 */
	private void aktualisiereZeitanzeige() {
		setZeitanzeige(_mp.get_aktuelleZeit(), _mp.get_zeitdauerArtikel());
	}

	/**
	 * Aktualisiert den Pointer der das aktuelle Kapitel anzeigt
	 */
	private void aktualisiereGui_Kapitelpointer() {
		for (int i = 0; i < getInhaltsverzeichnisButton().size(); ++i) {
			if (i == _mp.get_aktuellesKapitel()) { // TODO
				getInhaltsverzeichnisButton().get(i).setStyle("-fx-base: #b6e7c9");
			} else {
				getInhaltsverzeichnisButton().get(i).setStyle("");
			}
		}
	}

	/**
	 * Setzt den Pointer auf den nächstgelegenen Link TODO so abändern dass
	 * immer nur ein Link aktuell ist
	 */
	private void aktualisiereLinkpointer() {
		int aktuellerIndex = _mp.get_aktuellerIndex();
		if (getLinklisteButtons().size() > 0 && aktuellerIndex >= _p.firstLink()) { // TODO
			int aktuellerLink;
			int naechstkleinererLink = _p.naechstKleinererLink(); // TODO
			int naechstgroessererLink = _p.naechstGroessererLink(); // TODO
			if (Math.abs(naechstkleinererLink - aktuellerIndex) < Math.abs(naechstgroessererLink - aktuellerIndex)) {
				aktuellerLink = naechstkleinererLink;
			} else {
				aktuellerLink = naechstgroessererLink;
			}

			for (int i = 0; i < getLinklisteButtons().size(); ++i) {
				if (getLinklisteButtons().get(i).getText().equals(_p.getLink(aktuellerLink))) {
					getLinklisteButtons().get(i).setStyle("-fx-base: #b6e7c9");
				} else {
					getInhaltsverzeichnisButton().get(i).setStyle("");
				}
			}
		}
		aktualisiereGui_Kapitelpointer(); // TODO Gui so ändern dass //
											// unabhängig
	}

	private boolean ungueltigesIntervall(int i1, int i2) {
		return i1 < 0 || i1 > i2;
	}

	public void setNormalModus() {
		// TODO Auto-generated method stub
		slider.setVisible(true);
		kapitelBox.setVisible(true);
		absatzBox.setVisible(true);
		satzBox.setVisible(true);
		zeitBox.setVisible(true);
		oben.setVisible(true);
		skipVorIndexButton.setVisible(true);
		skipZurueckIndexButton.setVisible(true);
		inhaltsverzeichnisVorlesenButton.setVisible(true);
		zusaetzlicheSteuerungButton.setVisible(true);
		playPauseButton.setVisible(true);
		stopButton.setVisible(true);
		sprachbefehlButton.setVisible(true);
		sprachbefehlButton.setVisible(true);
	}

	public void blendeArtikelSucheAus() {
		vorButton.setVisible(false);
		zurueckButton.setVisible(false);
		suchFeldArtikel.setVisible(false);
		artikelSuchenButton.setVisible(false);
		artikelsuchelabel.setVisible(false);
	}

	public void blendeArtikelSucheEin() {
		vorButton.setVisible(true);
		zurueckButton.setVisible(true);
		suchFeldArtikel.setVisible(true);
		artikelSuchenButton.setVisible(true);
		artikelsuchelabel.setVisible(true);
	}
	
	public void setSprachsteuerungsModus() {
		// TODO Auto-generated method stub
		slider.setVisible(false);
		kapitelBox.setVisible(false);
		absatzBox.setVisible(false);
		satzBox.setVisible(false);
		zeitBox.setVisible(false);
		blendeArtikelSucheAus();
		blendeWortSucheAus();
		skipVorIndexButton.setVisible(false);
		skipZurueckIndexButton.setVisible(false);
		inhaltsverzeichnisVorlesenButton.setVisible(false);
		zusaetzlicheSteuerungButton.setVisible(false);
		playPauseButton.setVisible(false);
		stopButton.setVisible(false);
		sprachbefehlButton.setVisible(true);
		for(Button b: inhaltsverzeichnisButton){
			b.setDisable(true);
		}
	}

	public void setOhneSprachsteuerungModus() {
		// TODO Auto-generated method stub
		slider.setVisible(true);
		kapitelBox.setVisible(true);
		absatzBox.setVisible(true);
		satzBox.setVisible(true);
		zeitBox.setVisible(true);
		oben.setVisible(true);
		skipVorIndexButton.setVisible(true);
		skipZurueckIndexButton.setVisible(true);
		inhaltsverzeichnisVorlesenButton.setVisible(true);
		zusaetzlicheSteuerungButton.setVisible(true);
		playPauseButton.setVisible(true);
		stopButton.setVisible(true);
		sprachbefehlButton.setVisible(false);
		blendeArtikelSucheAus();
		blendeWortSucheAus();
	}

	public void blendeWortSucheAus() {
		sucheRueckwaertsButton.setVisible(false);
		sucheVorwaertsButton.setVisible(false);
		suchFeldWort.setVisible(false);
		wortsucheLabel.setVisible(false);
	}
	
	/**
	 * Fur Nutzerstudien eingeführter Resetbutton, damit bei kompletten Zusammenbruch des Systems zurückgesetzt werden kann.
	 * @return
	 */
	public Button getReset(){
		return reset;
	}

	public void blendeAllesAus() {
		_p.stopAudio(true);
		setSprachsteuerungsModus();
		mitte.setVisible(false);
		anzeige.setVisible(false);
		
	}



}
