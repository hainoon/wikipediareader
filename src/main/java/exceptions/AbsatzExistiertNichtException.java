package exceptions;

/**
 * Exception für nicht existierende Absätze
 * 
 * @author marcel
 *
 */
public class AbsatzExistiertNichtException extends Exception {
	/**
	 * Konstruktor
	 * 
	 * @param string
	 *            Fehlermeldung
	 */
	public AbsatzExistiertNichtException(String string) {
		super(string);
	}

	/**
	 * Konstruktor
	 */
	public AbsatzExistiertNichtException() {
		super();
	}
}
