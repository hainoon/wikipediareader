package exceptions;

/**
 * Exception für fehlerhafte Sätze
 * 
 * @author marcel
 *
 */
public class FehlerhafterSatzException extends Exception {
	/**
	 * Konstruktor
	 */
	public FehlerhafterSatzException() {
		super();
	}

	/**
	 * Konstruktor
	 * 
	 * @param text
	 *            Fehlermeldung
	 */
	public FehlerhafterSatzException(String text) {
		super(text);
	}
}
