/**
 * 
 */
package sprachsteuerung;

import java.util.ArrayList;
import java.util.List;

import exceptions.SprachbefehlException;

/**
 * Sammelt die relevanten Elemente eines Sprachbefehls
 * 
 * @author marcel
 *
 */
public class Befehl {
	private Aktion _aktion;
	private Element _artikelElement;
	private int _wert;
	private Position _position;
	private Richtung _richtung;
	private String _befehlAlsString;
	private List<String> alternative;

	/**
	 * Konstruktor
	 * 
	 * @param input
	 *            Der gesamte Befehl
	 * @param alternative
	 * @throws SprachbefehlException
	 */
	public Befehl(String input, List<String> alternative) throws SprachbefehlException {
		assert input != null : "Fehlerhafter Input";
		input.replaceAll("\n", "");
		this.alternative = alternative;
		_befehlAlsString = input;
		ArrayList<String> befehlelemente = new ArrayList<String>();
		for (String s1 : input.split(" ")) {
			if (s1.matches("[1-9]")) {
				s1 = intZahlZuStringZahl(s1);
			}
			befehlelemente.add(s1.toUpperCase());
		}

		setzeAktion(befehlelemente);
		setzeArtikelElement(befehlelemente);
		setzeWert(befehlelemente);
		setzeRichtung(befehlelemente);
		setzePosition(befehlelemente);

	}

	public List<String> getAlternativen() {
		return alternative;
	}

	/**
	 * Setzt das Positionselement des Befehls
	 * 
	 * @param befehlelemente
	 *            Menge aller Befehlselemente
	 * @throws SprachbefehlException
	 */
	private void setzePosition(ArrayList<String> befehlelemente) throws SprachbefehlException {
		for (Position p : Position.values()) {
			for (String s : befehlelemente) {
				if (s.equals(p.name())) {
					if (_position != null) {
						throw new SprachbefehlException("Mehrere Positionen erkannt");
					}
					_position = Position.normalisiertePosition(p);
				}
			}
		}
		if (_position == null) {
			_position = Position.normalisiertePosition(Position.DEFAULT);
		}
	}

	/**
	 * Setzt das Richtungselement des Befehls
	 * 
	 * @param befehlelemente
	 *            Menge aller Befehlselemente
	 * @throws SprachbefehlException
	 */
	private void setzeRichtung(ArrayList<String> befehlelemente) throws SprachbefehlException {
		for (Richtung r : Richtung.values()) {
			for (String s : befehlelemente) {
				if (s.equals(r.name())) {
					if (_richtung != null) {
						throw new SprachbefehlException("Mehrere Richtungen erkannt");
					}
					_richtung = Richtung.normalisierteRichtung(r);
				}
			}
		}
		if (_richtung == null) {
			_richtung = Richtung.normalisierteRichtung(Richtung.DEFAULT);
		}
	}

	/**
	 * Setzt den Zahlenwert der im Befehl (evtl.) vorhanden ist
	 * 
	 * @param befehlelemente
	 *            Menge aller Befehlselemente
	 * @throws SprachbefehlException
	 */
	private void setzeWert(ArrayList<String> befehlelemente) throws SprachbefehlException {
		try {
			for (String s : befehlelemente) {
				_wert = Wert.getWertAusString(s);
				if (_wert != Wert.normalisierterWert(Wert.DEFAULT)) {
					return;
				}
			}
		} catch (Exception e) {
			// kein Wert erkannt
			_wert = Wert.normalisierterWert(Wert.DEFAULT);
		}

	}

	/**
	 * Setzt das Artikelelement (z.B. SATZ) des Befehls
	 * 
	 * @param befehlelemente
	 *            Menge aller Befehlselemente
	 * @throws SprachbefehlException
	 */
	private void setzeArtikelElement(ArrayList<String> befehlelemente) throws SprachbefehlException {
		for (Element e : Element.values()) {
			for (String s : befehlelemente) {
				if (s.equals(e.name())) {
					if (_artikelElement != null) {
						throw new SprachbefehlException("Mehrere Artikelelemente erkannt");
					}
					_artikelElement = Element.normalisiertesElement(e);
				}
			}
		}
		if (_artikelElement == null) {
			_artikelElement = Element.normalisiertesElement(Element.DEFAULT);
		}
	}

	/**
	 * Setzt das Aktionselement des Befehls
	 * 
	 * @param befehlelemente
	 *            Menge aller Befehlselemente
	 * @throws SprachbefehlException
	 */
	private void setzeAktion(ArrayList<String> befehlelemente) throws SprachbefehlException {
		for (Aktion a : Aktion.values()) {
			for (String s : befehlelemente) {
				if (s.equals(a.name())) {
					if (_aktion != null) {
						throw new SprachbefehlException("Mehrere Aktionen erkannt");
					}
					_aktion = Aktion.normalisierteAktion(a);
				}
			}
		}
		if (_aktion == null) {
			_aktion = Aktion.normalisierteAktion(Aktion.DEFAULT);
		}
	}

	/**
	 * Schreibt Zahlen aus
	 * 
	 * @param zahl
	 *            Eine Zahl
	 * @return eine ausgeschriebene Zahl
	 */
	private String intZahlZuStringZahl(String zahl) {
		assert (zahl.length() == 1 || Wert.values().toString().contains(zahl)) : "Keine Zahl";
		switch (zahl) {
		case "1":
		case "EINEN":
		case "EIN":
			return "EINEN";
		case "2":
		case "ZWEI":
			return "ZWEI";
		case "3":
		case "DREI":
			return "DREI";
		case "4":
		case "VIER":
			return "VIER";
		case "5":
		case "FÜNF":
			return "FÜNF";
		case "6":
		case "SECHS":
			return "SECHS";
		case "7":
		case "SIEBEN":
			return "SIEBEN";
		case "8":
		case "ACHT":
			return "ACHT";
		case "9":
		case "NEUN":
			return "NEUN";
		}
		return null;
	}

	/**
	 * Gibt das Aktionselement des Befehls zurück
	 * 
	 * @return _action Aktionselement des Befehls
	 */
	public Aktion getAktion() {
		return _aktion;
	}

	/**
	 * Gibt das Artikelelement des Befehls zurück
	 * 
	 * @return _element Artikelelement des Befehls
	 */
	public Element getArtikelElement() {
		return _artikelElement;
	}

	/**
	 * Gibt den im Befehl eventuell enthaltenen Zahlenwert zurück
	 * 
	 * @return _wert Zahlenwert des Befehls
	 */
	public int getWert() {
		return _wert;
	}

	/**
	 * Gibt den Befehl zurück
	 * 
	 * @return _befehlAlsString Befehl
	 */
	public String getBefehlAlsString() {
		// TODO zweiter String mit Interpretation
		return _befehlAlsString;
	}

	/**
	 * Gibt das Positionselement des Befehls zurück
	 * 
	 * @return _position Positionselement des Befehls
	 */
	public Position getPosition() {
		return _position;
	}

	/**
	 * Gibt das Richtungselement des Befehls zurück
	 * 
	 * @return _richtung Richtungselement des Befehls
	 */
	public Richtung getRichtung() {
		return _richtung;
	}

	public boolean istBezeichner() {
		return getRichtung() == Richtung.DEFAULT && getArtikelElement() == Element.DEFAULT
				&& getPosition() == Position.DEFAULT && getWert() == Wert.getDefault() && getAktion() == Aktion.DEFAULT;
	}

	public boolean istNurRichtung() {
		return getRichtung() != Richtung.DEFAULT && getPosition() == Position.DEFAULT
				&& getArtikelElement() == Element.DEFAULT && getWert() == Wert.getDefault()
				&& getAktion() == Aktion.DEFAULT;
	}

	public boolean istNurWert() {
		return getRichtung() == Richtung.DEFAULT && getPosition() == Position.DEFAULT
				&& getArtikelElement() == Element.DEFAULT && getWert() != Wert.getDefault()
				&& getAktion() == Aktion.DEFAULT;
	}

	public boolean istNurAktionUndPosition() {
		return getRichtung() == Richtung.DEFAULT && getPosition() != Position.DEFAULT && getWert() == Wert.getDefault()
				&& (getAktion() == Aktion.SPRINGE || getAktion() == Aktion.DEFAULT);
	}

	public boolean istNurElementUndRichtung() {
		return getRichtung() != Richtung.DEFAULT && getPosition() == Position.DEFAULT
				&& getArtikelElement() != Element.DEFAULT && getWert() == Wert.getDefault()
				&& getAktion() == Aktion.DEFAULT;
	}

	public boolean istNurElement() {
		return getRichtung() == Richtung.DEFAULT && getPosition() == Position.DEFAULT
				&& getArtikelElement() != Element.DEFAULT && getWert() == Wert.getDefault()
				&& getAktion() == Aktion.DEFAULT;
	}

	public boolean istNurAktion() {
		// TODO Auto-generated method stub
		return false;
	}

}
