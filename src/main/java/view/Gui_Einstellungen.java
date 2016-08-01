package view;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;

/**
 * Oberfläche für Einstellungen der GUI
 * 
 * @author marcel
 *
 */
public class Gui_Einstellungen {

	private BorderPane pane;

	private int breite = 500;
	private int hoehe = 400;

	private final ComboBox<VariablenGuiEinstellungen> modusAuswahl;
	private final ComboBox<VariablenGuiEinstellungen> inhaltsverzeichnis;
	private final ComboBox<VariablenGuiEinstellungen> links;
	private final ComboBox<VariablenGuiEinstellungen> linksfolgen;
	private final ComboBox<VariablenGuiEinstellungen> artikel;
	private final ComboBox<VariablenGuiEinstellungen> reset;
	private Button ok;
	private Stage stage;

	private ComboBox<String> startartikel;

	private ComboBox<VariablenGuiEinstellungen> sprache;

	public Gui_Einstellungen(Stage primaryStage) {

		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(primaryStage);

		pane = new BorderPane();

		primaryStage.setTitle("Wikipedia Browser");
		Group root = new Group();
		Scene scene = new Scene(root, breite, hoehe);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		grid.add(new Label("modus"), 0, 1);
		modusAuswahl = new ComboBox<>();
		modusAuswahl.getItems().addAll(VariablenGuiEinstellungen.Complete, VariablenGuiEinstellungen.GUI,
				VariablenGuiEinstellungen.VUI, VariablenGuiEinstellungen.WOZ);
		modusAuswahl.setMinWidth(250);
		modusAuswahl.getSelectionModel().select(0);
		grid.add(modusAuswahl, 1, 1);

		grid.add(new Label("table of contents"), 0, 2);
		inhaltsverzeichnis = new ComboBox<>();
		inhaltsverzeichnis.getItems().addAll(VariablenGuiEinstellungen.Identifier1,
				VariablenGuiEinstellungen.Identifier0,
				VariablenGuiEinstellungen.NoTableOfContent);
		inhaltsverzeichnis.setMinWidth(250);
		inhaltsverzeichnis.getSelectionModel().select(0);
		grid.add(inhaltsverzeichnis, 1, 2);

		grid.add(new Label("show links"), 0, 3);
		links = new ComboBox<>();
		links.getItems().addAll(VariablenGuiEinstellungen.DisplayLinks1, VariablenGuiEinstellungen.DisplayLinks0);
		links.setMinWidth(250);
		links.getSelectionModel().select(0);
		grid.add(links, 1, 3);

		grid.add(new Label("follow links"), 0, 4);
		linksfolgen = new ComboBox<>();
		linksfolgen.getItems().addAll(VariablenGuiEinstellungen.FollowLinks1,
				VariablenGuiEinstellungen.FollowLinks0);
		linksfolgen.setMinWidth(250);
		linksfolgen.getSelectionModel().select(0);
		grid.add(linksfolgen, 1, 4);

		grid.add(new Label("article corpus"), 0, 5);
		artikel = new ComboBox<>();
		artikel.getItems().addAll(VariablenGuiEinstellungen.All, VariablenGuiEinstellungen.OnlyCorpus);
		artikel.setMinWidth(250);
		artikel.getSelectionModel().select(0);
		grid.add(artikel, 1, 5);

		grid.add(new Label("delete article data"), 0, 6);
		reset = new ComboBox<>();
		reset.getItems().addAll(VariablenGuiEinstellungen.RESET0, VariablenGuiEinstellungen.RESET1);
		reset.setMinWidth(250);
		reset.getSelectionModel().select(0);
		grid.add(reset, 1, 6);

		grid.add(new Label("article"), 0, 7);
		startartikel = new ComboBox<>();
		startartikel.getItems().addAll("Isar","Asphalt", "Berlin", "Bundestagswahlrecht", "Hanseat", "Zugspitze", "Accessibility");
		startartikel.setMinWidth(250);
		startartikel.getSelectionModel().select(0);
		grid.add(startartikel, 1, 7);
		
		grid.add(new Label("article language"), 0, 8);
		sprache = new ComboBox<>();
		sprache.getItems().addAll(VariablenGuiEinstellungen.Deutsch, VariablenGuiEinstellungen.English);
		sprache.setMinWidth(250);
		sprache.getSelectionModel().select(0);
		grid.add(sprache, 1, 8);

		pane.setCenter(grid);
		// pane.setBottom(kontrollleiste);

		ok = new Button("ok");
		ok.setMinSize(100, 50);
		pane.setBottom(ok);

		scene.setRoot(pane);

		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}

	public VariablenGuiEinstellungen getModusAuswahl() {
		return modusAuswahl.getSelectionModel().getSelectedItem();
	}

	public VariablenGuiEinstellungen getInhaltsverzeichnis() {
		return inhaltsverzeichnis.getSelectionModel().getSelectedItem();
	}

	public VariablenGuiEinstellungen getLinks() {
		return links.getSelectionModel().getSelectedItem();
	}

	public VariablenGuiEinstellungen getLinksfolgen() {
		return linksfolgen.getSelectionModel().getSelectedItem();
	}

	public VariablenGuiEinstellungen getArtikel() {
		return artikel.getSelectionModel().getSelectedItem();
	}

	public VariablenGuiEinstellungen getReset() {
		return reset.getSelectionModel().getSelectedItem();
	}

	public String getAlternativenStartartikel() {
		return startartikel.getSelectionModel().getSelectedItem();
	}
	
	public String getSprache(){
		return sprache.getSelectionModel().getSelectedItem().toString();
	}

	public Button getOk() {
		return ok;
	}

	public void ausblenden() {
		stage.hide();
	}
}
