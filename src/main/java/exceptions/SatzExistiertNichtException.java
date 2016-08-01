package exceptions;

/**
 * Exception für nicht existierende Sätze
 * 
 * @author marcel
 *
 */
public class SatzExistiertNichtException extends Exception {
	/**
	 * Konstruktor
	 * 
	 * @param string
	 *            Fehlermeldung
	 */
	public SatzExistiertNichtException(String string) {
		super(string);
	}

	/**
	 * Konstruktor
	 */
	public SatzExistiertNichtException() {
		super();
	}
}
