package view;

import javafx.stage.Stage;

/**
 * Fehlermeldung für nicht existierende Wörter
 * 
 * @author marcel
 *
 */
public class WortExistiertNichtFehlermeldung extends Fehlermeldung {

	/**
	 * Konstruktor
	 * 
	 * @param primaryStage
	 *            primaryStage
	 * @param e 
	 */
	public WortExistiertNichtFehlermeldung(Stage primaryStage, Exception e) {
		super(primaryStage,e ,"Wort exisiert nicht");
	}
}
