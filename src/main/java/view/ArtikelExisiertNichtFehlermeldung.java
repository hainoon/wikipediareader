package view;

import javafx.stage.Stage;

/**
 * Fehlermeldung für nicht existierende Artikel
 * 
 * @author marcel
 *
 */
public class ArtikelExisiertNichtFehlermeldung extends Fehlermeldung {
	/**
	 * Konstruktor
	 * 
	 * @param primaryStage
	 *            primaryStage
	 * @param e 
	 */
	public ArtikelExisiertNichtFehlermeldung(Stage primaryStage, Exception e) {
		super(primaryStage, e, "Artikel exisiert nicht");
	}

}
