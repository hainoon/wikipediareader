package view;

import javafx.stage.Stage;

/**
 * Fehlermeldung für nicht existierende Absätze
 * 
 * @author marcel
 *
 */
public class AbsatzExistiertNichtFehlermeldung extends Fehlermeldung {

	/**
	 * Konstruktor
	 * 
	 * @param primaryStage
	 *            primaryStage
	 * @param e 
	 */
	public AbsatzExistiertNichtFehlermeldung(Stage primaryStage, Exception e) {
		super(primaryStage,e, "Absatz exisiert nicht" );
	}

}
