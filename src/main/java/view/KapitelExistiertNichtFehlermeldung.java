package view;

import javafx.stage.Stage;

/**
 * Fehlermeldung für nicht existierende Kapitel
 * 
 * @author marcel
 *
 */
public class KapitelExistiertNichtFehlermeldung extends Fehlermeldung {

	/**
	 * Konstruktor
	 * 
	 * @param primaryStage
	 *            primaryStage
	 * @param e 
	 */
	public KapitelExistiertNichtFehlermeldung(Stage primaryStage, Exception e) {
		super(primaryStage,e, "Kapitel exisiert nicht" );
	}

}
