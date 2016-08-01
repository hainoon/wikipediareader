package exceptions;

/**
 * Exception für nicht existierende Wörter
 * 
 * @author marcel
 *
 */
public class WortExistiertNichtException extends Exception {
	/**
	 * Konstruktor
	 * 
	 * @param string
	 *            Fehlermeldung
	 */
	public WortExistiertNichtException(String string) {
		super(string);
	}

	/**
	 * Konstruktor
	 */
	public WortExistiertNichtException() {
		super();
	}
}
