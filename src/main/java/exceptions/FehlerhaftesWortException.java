package exceptions;

/**
 * Exception für fehlerhafte Wörter
 * 
 * @author marcel
 *
 */
public class FehlerhaftesWortException extends Exception {
	/**
	 * Konstruktor
	 */
	public FehlerhaftesWortException() {
		super();
	}

	/**
	 * Konstruktor
	 * 
	 * @param text
	 *            Fehlermeldung
	 */
	public FehlerhaftesWortException(String text) {
		super(text);
	}
}
