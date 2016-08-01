package view;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Modality;

/**
 * Fehlermeldung
 * 
 * @author marcel
 *
 */
public class Fehlermeldung {
	protected VBox dialogVbox;
	protected String text;

	/**
	 * Konstruktor
	 * 
	 * @param primaryStage
	 *            primaryStage
	 */

	public Fehlermeldung(final Stage primaryStage, Exception e, String text2) {
		this.text = text2;
		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(primaryStage);
		dialogVbox = new VBox(20);
		dialogVbox.getChildren().add(new Text(text));
		Scene dialogScene = new Scene(dialogVbox, 500, 200);
		dialog.setScene(dialogScene);
		dialog.show();
		// f.getDialogBox().getChildren().add(new Text(e.getMessage()));
		System.err.println(e.getClass().getName()); // TODO
		e.printStackTrace();
	}

	protected void setText(String text) {
		this.text = text;
		dialogVbox.getChildren().add(new Text(text));
	}
}
