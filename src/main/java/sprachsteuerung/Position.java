/**
 * 
 */
package sprachsteuerung;

/**
 * Positionen die ein Element einnehmen kann
 * 
 * @author marcel
 *
 */
public enum Position {
	ANFANG, BEGINNING, ENDE, END, DEFAULT;

	public static Position normalisiertePosition(Position p) {
		switch (p) {
		case ANFANG:
		case BEGINNING:
			return ANFANG;
		case ENDE:
		case END:
			return ENDE;
		default:
			return DEFAULT;
		}
	}
}
