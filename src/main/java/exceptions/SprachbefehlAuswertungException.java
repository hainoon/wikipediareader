/**
 * 
 */
package exceptions;

/**
 * Fehler bei der Auswertung des Sprachbefehls durch Google
 * 
 * @author marcel
 *
 */
public class SprachbefehlAuswertungException extends Exception {

	public SprachbefehlAuswertungException(String text) {
		super(text);
	}
}
