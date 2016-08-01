/**
 * 
 */
package sprachsteuerung;

/**
 * Zahlenwerte innerhalb eines Sprachbefehls
 * 
 * @author marcel
 *
 */
public enum Wert {
	// TODO automatisierte Zahlenerkennung
	ELF, ELEVEN, ZWÖLF, TWELVE, THIRDTEEN, FIFTEEN, ZEHN, TEN, TEEN, ZWANZIG, TWENTY, DREIßIG, THIRDTY, VIERZIG, FOURTY, EIN, EINEN, ONE, ZWEI, TWO, DREI, THREE, VIER, FOUR, FÜNF, FIVE, SECHS, SECH, SIX, SIEBEN, SIEB, SEVEN, ACHT, EIGHT, NEUN, NINE, DEFAULT;

	private static int defaultWert = 0;

	public static int normalisierterWert(Wert w) {
		switch (w) {
		case EIN:
		case EINEN:
		case ONE:
			return 1;
		case ZWEI:
		case TWO:
			return 2;
		case DREI:
		case THREE:
			return 3;
		case VIER:
		case FOUR:
			return 4;
		case FÜNF:
		case FIVE:
			return 5;
		case SECHS:
		case SECH:
		case SIX:
			return 6;
		case SIEBEN:
		case SIEB:
		case SEVEN:
			return 7;
		case ACHT:
		case EIGHT:
			return 8;
		case NEUN:
		case NINE:
			return 9;
		case ZEHN:
		case TEN:
		case TEEN:
			return 10;
		case ELF:
		case ELEVEN:
			return 11;
		case ZWÖLF:
		case TWELVE:
			return 12;
		case THIRDTEEN:
			return 13;
		case FIFTEEN:
			return 15;
		case ZWANZIG:
		case TWENTY:
			return 20;
		case DREIßIG:
		case THIRDTY:
			return 30;
		case VIERZIG:
		case FOURTY:
			return 40;
		default:
			return defaultWert;
		}
	}

	public static int getDefault() {
		return defaultWert;
	}

	public static int getWertAusString(String s) throws Exception {
		int ergebnis = 0;
		s = s.toUpperCase();
		String[] woerter = s.split(" ");
		for (String wort : woerter) {
			for (Wert w : Wert.values()) {
				if (wort.contains(w.name())) {
					ergebnis += Wert.normalisierterWert(w);
					wort = wort.replaceAll(w.name(), "");
				}
			}
		}
		if (ergebnis == 0) {
			throw new Exception();
		}
		return ergebnis;
	}
}
