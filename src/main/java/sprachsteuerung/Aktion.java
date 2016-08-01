/**
 * 
 */
package sprachsteuerung;

/**
 * Aktionswörter eines Befehls
 * 
 * @author marcel
 *
 */
public enum Aktion {
	SPRINGE, GO, LEAP, JUMP, START, PLAY, STOPP, PAUSE, SPIELE, CONTINUE, DEFAULT, STOP, INHALTSVERZEICHNIS, CONTENT, CONTENTS;

	public static Aktion normalisierteAktion(Aktion a) {
		switch (a) {
		case SPRINGE:
		case GO:
		case LEAP:
		case JUMP:
			return SPRINGE;
		case START:
		case CONTINUE:
		case SPIELE:
		case PLAY:
			return START;
		case STOP:
		case STOPP:
			return STOP;
		case PAUSE:
			return PAUSE;
		case INHALTSVERZEICHNIS:
		case CONTENT:
		case CONTENTS:
			return INHALTSVERZEICHNIS;
		default:
			return DEFAULT;
		}
	}
}
