package exceptions;

public class AudiodateiExistiertNichtException extends Exception {
	/**
	 * Konstruktor
	 */
	public AudiodateiExistiertNichtException() {
		super();
	}

	/**
	 * Konstruktor
	 * 
	 * @param text
	 *            Fehlermeldung
	 */
	public AudiodateiExistiertNichtException(String text) {
		super(text);
	}
}
