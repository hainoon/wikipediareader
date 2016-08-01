package view;

import exceptions.SprachbefehlAufnahmeException;
import exceptions.SprachbefehlAuswertungException;
import exceptions.SprachbefehlException;
import javafx.stage.Stage;

public class Sprachbefehlfehlermeldung extends Fehlermeldung {

	/**
	 * Konstruktor
	 * 
	 * @param primaryStage
	 *            primaryStage
	 * @param e
	 */
	public Sprachbefehlfehlermeldung(Stage primaryStage, Exception e) {
		super(primaryStage, e,"");
		if (e instanceof SprachbefehlAuswertungException) {
			setText("Fehler bei der Auswertung des Sprachbefehls (keine Antwort durch Google)");
		} else if (e instanceof SprachbefehlAufnahmeException) {
			setText("Fehler bei der Aufnahme des Sprachbefehls (Mikrophon)");
		} else if (e instanceof SprachbefehlException) {
			setText("Fehler bei der Auswertung des Sprachbefehls (interner Auswertungsfehler)");
		}
	}

}
