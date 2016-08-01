package exceptions;

/**
 * Exception für nicht existierende Kapitel
 * 
 * @author marcel
 *
 */
public class KapitelExistiertNichtException extends Exception {

	/**
	 * Konstruktor
	 * 
	 * @param string
	 *            Fehlermeldung
	 */
	public KapitelExistiertNichtException(String string) {
		super(string);
	}

	/**
	 * Konstruktor
	 */
	public KapitelExistiertNichtException() {
		super();
	}

}
