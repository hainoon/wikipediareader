package model;

/**
 * Statusmeldungen für Ladefortschritt
 * 
 * @author marcel
 *
 */
public enum Statusmeldungen {
	ArtikelGeladen, ArtikelVorhanden, JsonSynthese, Xml, XmlTeil, Wav, WavTeil, Json, Html, Datenstruktur, Reader, Hauptschritt, Teilschritt, Ladevorgang0, Ladevorgang1;

	public static String nachricht(Statusmeldungen s) {
		switch (s) {
		case ArtikelGeladen:
			return "Artikel geladen";
		case ArtikelVorhanden:
			return "Artikel vorhanden";
		case JsonSynthese:
			return "Json für aus Sprachsynthese erstellt";
		case Xml:
			return "gesamte Xml-Datei erstellt";
		case XmlTeil:
			return "Teil-Xml-Datei erstellt";
		case Wav:
			return "Wav-Datei erstellt";
		case WavTeil:
			return "Teil-Wav-Datei erstellt";
		case Json:
			return "Json ausgelesen";
		case Html:
			return "Html ausgelesen";
		case Datenstruktur:
			return "Datenstruktur erstellt";
		case Reader:
			return "Leser Datenstruktur erstellt";
		default:
			// TODO
			return s.name();
		}
	}
}
