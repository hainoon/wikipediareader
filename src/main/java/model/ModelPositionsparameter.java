package model;

import javafx.util.Duration;

public interface ModelPositionsparameter {

	/**
	 * 
	 * @return
	 */
	public String get_aktuellerArtikel();

	/**
	 * 
	 * @param _aktuellerArtikel
	 */
	public void set_aktuellerArtikel(String _aktuellerArtikel);

	/**
	 * von Titel abgespielte Zeit
	 * 
	 * @return abgespielte Zeit
	 */
	public Duration get_aktuelleZeit();

	/**
	 * Setzt abgespielte Zeit
	 * 
	 * @param _aktuelleZeit
	 *            abgespielte Zeit
	 */
	public void set_aktuelleZeit(Duration _aktuelleZeit);

	/**
	 * Index des Worts das gerade abgespielt wird
	 * 
	 * @return aktueller Wortindex
	 */
	public int get_aktuellerIndex();

	/**
	 * Setzt den Index des Worts das gerade abgespielt wird
	 * 
	 * @param _aktuellerIndex
	 *            Wortindex
	 */
	public void set_aktuellerIndex(int _aktuellerIndex);

	/**
	 * Wortanzahl des Artikels
	 * 
	 * @return Wortanzahl des Artikels
	 */
	public int get_wortanzahlArtikel();

	public void set_wortanzahlArtikel(int _wortanzahlArtikel);

	/**
	 * Index des satz der gerade abgespielt wird
	 * 
	 * @return Satzindex
	 */
	public int get_aktuellerSatz();

	/**
	 * Setzt den Index des Worts der gerade abgespielt wird
	 * 
	 * @param _aktuellerSatz
	 *            Satzindex
	 */
	public void set_aktuellerSatz(int _aktuellerSatz);

	/**
	 * Satzanzahl des Artikels
	 * 
	 * @return Satzanzahl des Artikels
	 */
	public int get_satzanzahlArtikel();

	public void set_satzanzahlArtikel(int _satzanzahlArtikel);

	/**
	 * Index des Absatz der gerade abgespielt wird
	 * 
	 * @return Absatzindex
	 */
	public int get_aktuellerAbsatz();

	/**
	 * Setzt den Index des Absatz der gerade abgespielt wird
	 * 
	 * @param _aktuellerAbsatz
	 *            Absatzindex
	 */
	public void set_aktuellerAbsatz(int _aktuellerAbsatz);

	/**
	 * Absatzanzahl des Artikels
	 * 
	 * @return Absatzanzahl des Artikels
	 */
	public int get_absatzanzahlArtikel();

	public void set_absatzanzahlArtikel(int _absatzanzahlArtikel);

	/**
	 * Index des Kapitels das gerade abgespielt wird
	 * 
	 * @return Kapitelindex
	 */
	public int get_aktuellesKapitel();

	/**
	 * Setzt den Index des Kapitels das gerade abgespielt wird
	 * 
	 * @param _aktuellesKapitel
	 *            Kapitelindex
	 */
	public void set_aktuellesKapitel(int _aktuellesKapitel);

	/**
	 * Kapitelanzahl des Artikels
	 * 
	 * @return Kapitelanzahl des Artikels
	 */
	public int get_kapitelanzahlArtikel();

	public void set_kapitelanzahlArtikel(int _kapitelanzahlArtikel);

	/**
	 * Textinhalt des Worts das gerade abgespielt wird
	 * 
	 * @return Textinhalt des aktuellen Worts
	 */
	public String get_aktuellesWort();

	/**
	 * Setzt den Textinhalt des Worts das gerade abgespielt wird
	 * 
	 * @param _aktuellesWort
	 *            Textinhalt des Worts
	 */
	public void set_aktuellesWort(String _aktuellesWort);

	/**
	 * Zeitobergrenze des aktuellen Index
	 * 
	 * @return Zeitobergrenze des aktuellen Index
	 */
	public Duration get_genzwertObenIndex();

	/**
	 * Setzt die Zeitobergrenze des aktuellen Index
	 * 
	 * @param _genzwertObenIndex
	 *            Zeitobergrenze des aktuellen Index
	 */
	public void set_genzwertObenIndex(Duration _genzwertObenIndex);

	/**
	 * Zeitobergrenze des aktuellen Satz
	 * 
	 * @return Zeitobergrenze des aktuellen Satz
	 */
	public Duration get_grenzwertObenSatz();

	/**
	 * Setzt die Zeitobergrenze des aktuellen Satz
	 * 
	 * @param _grenzwertObenSatz
	 *            Zeitobergrenze des aktuellen Satz
	 */
	public void set_grenzwertObenSatz(Duration _grenzwertObenSatz);

	/**
	 * Zeitobergrenze des aktuellen Absatz
	 * 
	 * @return Zeitobergrenze des aktuellen Absatz
	 */
	public Duration get_grenzwertObenAbsatz();

	/**
	 * Setzt die Zeitobergrenze des aktuellen Absatz
	 * 
	 * @param _grenzwertObenAbsatz
	 *            Zeitobergrenze des aktuellen Absatz
	 */
	public void set_grenzwertObenAbsatz(Duration _grenzwertObenAbsatz);

	/**
	 * Zeitobergrenze des aktuellen Kapitels
	 * 
	 * @return Zeitobergrenze des aktuellen Kapitels
	 */
	public Duration get_grenzwertObenKapitel();

	/**
	 * Setzt die Zeitobergrenze des aktuellen Kapitels
	 * 
	 * @param _grenzwertObenKapitel
	 *            Zeitobergrenze des aktuellen Kapitels
	 */
	public void set_grenzwertObenKapitel(Duration _grenzwertObenKapitel);

	/**
	 * Zeituntergrenze des aktuellen Index
	 * 
	 * @return Zeituntergrenze des aktuellen Index
	 */
	public Duration get_genzwertUntenIndex();

	/**
	 * Setzt die Zeituntergrenze des aktuellen Index
	 * 
	 * @param _genzwertUntenIndex
	 *            Zeituntergrenze des aktuellen Index
	 */
	public void set_genzwertUntenIndex(Duration _genzwertUntenIndex);

	/**
	 * Zeituntergrenze des aktuellen Satz
	 * 
	 * @return Zeituntergrenze des aktuellen Satz
	 */
	public Duration get_grenzwertUntenSatz();

	/**
	 * Setzt die Zeituntergrenze des aktuellen Satz
	 * 
	 * @param _grenzwertUntenSatz
	 *            Zeituntergrenze des aktuellen Satz
	 */
	public void set_grenzwertUntenSatz(Duration _grenzwertUntenSatz);

	/**
	 * Zeituntergrenze des aktuellen Absatz
	 * 
	 * @return Zeituntergrenze des aktuellen Absatz
	 */
	public Duration get_grenzwertUntenAbsatz();

	/**
	 * Setzt die eituntergrenze des aktuellen Absatz
	 * 
	 * @param _grenzwertUntenAbsatz
	 *            Zeituntergrenze des aktuellen Absatz
	 */
	public void set_grenzwertUntenAbsatz(Duration _grenzwertUntenAbsatz);

	/**
	 * Zeituntergrenze des aktuellen Kapitels
	 * 
	 * @return Zeituntergrenze des aktuellen Kapitels
	 */
	public Duration get_grenzwertUntenKapitel();

	/**
	 * Setzt die Zeituntergrenze des aktuellen Kapitels
	 * 
	 * @param _grenzwertUntenKapitel
	 *            Zeituntergrenze des aktuellen Kapitels
	 */
	public void set_grenzwertUntenKapitel(Duration _grenzwertUntenKapitel);

	/**
	 * Zeitdauer des Artikels
	 * 
	 * @return Zeitdauer des Artikels
	 */
	public Duration get_zeitdauerArtikel();

	public void set_zeitdauerArtikel(Duration _zeitdauerArtikel);

	// TODO gucken ob nachfolgendes noch irgendwo benötigt werden könnte
	// /**
	// * Prüft ob es sich um einen gültigen Zeitwert handelt
	// *
	// * @param zeit
	// * Zeit
	// * @return gültiger Zeitwert
	// */
	// public boolean gueltigerZeitwert(Duration zeit);
	//
	// /**
	// * Überprüft ob die Anzahl der Kapitel ein gültiger Wert ist
	// *
	// * @return gültige Kapitelanzahl
	// */
	// public boolean gueltigeKapitelanzahl();
	//
	// /**
	// * Überprüft ob die Anzahl der Absätze ein gültiger Wert ist
	// *
	// * @return gültige Absatzanzahl
	// */
	// public boolean gueltigeAbsatzanzahl();
	//
	// /**
	// * Überprüft ob die Anzahl der Sätze ein gültiger Wert ist
	// *
	// * @return gültige Satzanzahl
	// */
	// public boolean gueltigeSatzanzahl();
	//
	// public boolean gueltigeWortanzahl();

}
