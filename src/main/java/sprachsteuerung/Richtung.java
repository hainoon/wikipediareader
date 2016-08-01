/**
 * 
 */
package sprachsteuerung;

/**
 * Elemente eines Sprachbefehls die etwas über die Richtung aussagen
 * 
 * @author marcel
 *
 */
public enum Richtung {
	ZURÜCK, ZURUECK,  RÜCKWÄRTS, RUECKWAERTS, BACK, BACKWARDS, BEHIND, PREVIOUS, NÄCHSTES, NAECHSTES, VORHERIGES, WEITER, FURTHER, VOR, VORWÄRTS, VORWAERTS, NEXT,  FORWARDS, DEFAULT;

	public static Richtung normalisierteRichtung(Richtung r) {
		switch (r) {
		case VOR:
		case NÄCHSTES:
		case NAECHSTES:
		case VORWÄRTS:
		case VORWAERTS:
		case WEITER:
		case FURTHER:
		case NEXT:
		case FORWARDS:
			return WEITER;
		case ZURÜCK:
		case ZURUECK:
		case VORHERIGES:
		case RÜCKWÄRTS:
		case RUECKWAERTS:
		case BACK:
		case BACKWARDS:
		case PREVIOUS:
		case BEHIND:
			return ZURÜCK;
		default:
			return DEFAULT;
		}
	}
}
