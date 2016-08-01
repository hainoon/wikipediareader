/**
 * 
 */
package exceptions;

/**
 * Fehler beim Starten der Audioaufnahme des Sprachbefehls
 * 
 * @author marcel
 *
 */
public class SprachbefehlAufnahmeException extends Exception {

	public SprachbefehlAufnahmeException(String text) {
		super(text);
	}
}
