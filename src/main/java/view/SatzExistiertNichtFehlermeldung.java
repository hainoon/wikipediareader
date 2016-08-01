package view;

import javafx.stage.Stage;

/**
 * Fehlermeldung für nicht existierende Sätze
 * 
 * @author marcel
 *
 */
public class SatzExistiertNichtFehlermeldung extends Fehlermeldung {

	/**
	 * Konstruktor
	 * 
	 * @param primaryStage
	 *            primaryStage
	 * @param e 
	 */
	public SatzExistiertNichtFehlermeldung(Stage primaryStage, Exception e) {
		super(primaryStage,e , "Satz exisiert nicht");
	}

}
