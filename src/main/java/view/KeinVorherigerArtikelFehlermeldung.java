package view;

import javafx.stage.Stage;

/**
 * Fehlermeldung, dass zuvor kein Artikel abgespielt wurde
 * 
 * @author marcel
 *
 */
public class KeinVorherigerArtikelFehlermeldung extends Fehlermeldung {

	/**
	 * Konstruktor
	 * 
	 * @param primaryStage
	 *            primaryStage
	 */
	public KeinVorherigerArtikelFehlermeldung(Stage primaryStage, Exception e) {
		super(primaryStage, e, "Kein vorheriger Artikel vorhanden");
	}

}
