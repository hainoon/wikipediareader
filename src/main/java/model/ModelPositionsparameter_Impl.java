/**
 * 
 */
package model;

import java.util.Observable;

import javafx.util.Duration;

/**
 * Hilfsklasse zur Speicherung der Positionsparameter ( da Player nicht direkt
 * Oberservable )
 * 
 * @author marcel
 *
 */
public class ModelPositionsparameter_Impl extends Observable implements ModelPositionsparameter {
	private String _aktuellerArtikel;
	private Duration _aktuelleZeit;
	private int _aktuellerIndex;
	private int _aktuellerSatz;
	private int _aktuellerAbsatz;
	private int _aktuellesKapitel;
	private String _aktuellesWort;

	private Duration _genzwertObenIndex;
	private Duration _grenzwertObenSatz;
	private Duration _grenzwertObenAbsatz;
	private Duration _grenzwertObenKapitel;

	private Duration _genzwertUntenIndex;
	private Duration _grenzwertUntenSatz;
	private Duration _grenzwertUntenAbsatz;
	private Duration _grenzwertUntenKapitel;
	private Duration _zeitdauerArtikel;

	private int _wortanzahlArtikel;
	private int _satzanzahlArtikel;
	private int _absatzanzahlArtikel;
	private int _kapitelanzahlArtikel;

	public ModelPositionsparameter_Impl(String artikel, int wortanzahlInArtikel, int satzanzahlInArtikel,
			int absatzanzahlInArtikel, int kapitelanzahlInArtikel) {
		_aktuellerArtikel = artikel;
		_wortanzahlArtikel = wortanzahlInArtikel;
		_satzanzahlArtikel = satzanzahlInArtikel;
		_absatzanzahlArtikel = absatzanzahlInArtikel;
		_kapitelanzahlArtikel = kapitelanzahlInArtikel;
	}

	@Override
	public String get_aktuellerArtikel() {
		return _aktuellerArtikel;
	}

	@Override
	public void set_aktuellerArtikel(String _aktuellerArtikel) {
		this._aktuellerArtikel = _aktuellerArtikel;
		setChanged();
		notifyObservers(Elemente.ARTIKEL);
	}

	@Override
	public Duration get_aktuelleZeit() {
		return _aktuelleZeit;
	}

	@Override
	public void set_aktuelleZeit(Duration _aktuelleZeit) {
		this._aktuelleZeit = _aktuelleZeit;
		setChanged();
		notifyObservers(Elemente.ZEIT);
	}

	@Override
	public int get_aktuellerIndex() {
		return _aktuellerIndex;
	}

	@Override
	public void set_aktuellerIndex(int _aktuellerIndex) {
		this._aktuellerIndex = _aktuellerIndex;
		setChanged();
		notifyObservers(Elemente.INDEX);
	}

	@Override
	public int get_wortanzahlArtikel() {
		return _wortanzahlArtikel;
	}

	@Override
	public void set_wortanzahlArtikel(int _wortanzahlArtikel) {
		setChanged();
		notifyObservers(Elemente.INDEX);
		this._wortanzahlArtikel = _wortanzahlArtikel;
	}

	@Override
	public int get_aktuellerSatz() {
		return _aktuellerSatz;
	}

	@Override
	public void set_aktuellerSatz(int _aktuellerSatz) {
		this._aktuellerSatz = _aktuellerSatz;
		setChanged();
		notifyObservers(Elemente.SATZ);
	}

	@Override
	public int get_satzanzahlArtikel() {
		return _satzanzahlArtikel;
	}

	@Override
	public void set_satzanzahlArtikel(int _satzanzahlArtikel) {
		this._satzanzahlArtikel = _satzanzahlArtikel;
		setChanged();
		notifyObservers(Elemente.SATZ);
	}

	@Override
	public int get_aktuellerAbsatz() {
		return _aktuellerAbsatz;
	}

	@Override
	public void set_aktuellerAbsatz(int _aktuellerAbsatz) {
		this._aktuellerAbsatz = _aktuellerAbsatz;
		setChanged();
		notifyObservers(Elemente.ABSATZ);
	}

	@Override
	public int get_absatzanzahlArtikel() {
		return _absatzanzahlArtikel;
	}

	@Override
	public void set_absatzanzahlArtikel(int _absatzanzahlArtikel) {
		this._absatzanzahlArtikel = _absatzanzahlArtikel;
		setChanged();
		notifyObservers(Elemente.ABSATZ);
	}

	@Override
	public int get_aktuellesKapitel() {
		return _aktuellesKapitel;
	}

	@Override
	public void set_aktuellesKapitel(int _aktuellesKapitel) {
		this._aktuellesKapitel = _aktuellesKapitel;
		setChanged();
		notifyObservers(Elemente.KAPITEL);
	}

	@Override
	public int get_kapitelanzahlArtikel() {
		return _kapitelanzahlArtikel;
	}

	@Override
	public void set_kapitelanzahlArtikel(int _kapitelanzahlArtikel) {
		this._kapitelanzahlArtikel = _kapitelanzahlArtikel;
		setChanged();
		notifyObservers(Elemente.KAPITEL);
	}

	@Override
	public String get_aktuellesWort() {
		return _aktuellesWort;
	}

	@Override
	public void set_aktuellesWort(String _aktuellesWort) {
		this._aktuellesWort = _aktuellesWort;
		setChanged();
		notifyObservers(Elemente.WORT);
	}

	@Override
	public Duration get_genzwertObenIndex() {
		return _genzwertObenIndex;
	}

	@Override
	public void set_genzwertObenIndex(Duration _genzwertObenIndex) {
		this._genzwertObenIndex = _genzwertObenIndex;
	}

	@Override
	public Duration get_grenzwertObenSatz() {
		return _grenzwertObenSatz;
	}

	@Override
	public void set_grenzwertObenSatz(Duration _grenzwertObenSatz) {
		this._grenzwertObenSatz = _grenzwertObenSatz;
	}

	@Override
	public Duration get_grenzwertObenAbsatz() {
		return _grenzwertObenAbsatz;
	}

	@Override
	public void set_grenzwertObenAbsatz(Duration _grenzwertObenAbsatz) {
		this._grenzwertObenAbsatz = _grenzwertObenAbsatz;
	}

	@Override
	public Duration get_grenzwertObenKapitel() {
		return _grenzwertObenKapitel;
	}

	@Override
	public void set_grenzwertObenKapitel(Duration _grenzwertObenKapitel) {
		this._grenzwertObenKapitel = _grenzwertObenKapitel;
	}

	@Override
	public Duration get_genzwertUntenIndex() {
		return _genzwertUntenIndex;
	}

	@Override
	public void set_genzwertUntenIndex(Duration _genzwertUntenIndex) {
		this._genzwertUntenIndex = _genzwertUntenIndex;
	}

	@Override
	public Duration get_grenzwertUntenSatz() {
		return _grenzwertUntenSatz;
	}

	@Override
	public void set_grenzwertUntenSatz(Duration _grenzwertUntenSatz) {
		this._grenzwertUntenSatz = _grenzwertUntenSatz;
	}

	@Override
	public Duration get_grenzwertUntenAbsatz() {
		return _grenzwertUntenAbsatz;
	}

	@Override
	public void set_grenzwertUntenAbsatz(Duration _grenzwertUntenAbsatz) {
		this._grenzwertUntenAbsatz = _grenzwertUntenAbsatz;
	}

	@Override
	public Duration get_grenzwertUntenKapitel() {
		return _grenzwertUntenKapitel;
	}

	@Override
	public void set_grenzwertUntenKapitel(Duration _grenzwertUntenKapitel) {
		this._grenzwertUntenKapitel = _grenzwertUntenKapitel;
	}

	@Override
	public Duration get_zeitdauerArtikel() {
		return _zeitdauerArtikel;
	}

	@Override
	public void set_zeitdauerArtikel(Duration _zeitdauerArtikel) {
		this._zeitdauerArtikel = _zeitdauerArtikel;
	}

	public void setNeuerArtikel() {
		setChanged();
		notifyObservers(Elemente.ARTIKEL); // TODO sauber lösen
	}

	public String positionsInformationen() {
		return "\n<Zeit>" + _aktuelleZeit + "</Zeit>\n<Index>" + _aktuellerIndex + "</Index>\n<Wort>" + _aktuellesWort
				+ "</Wort>\n <Satz>" + _aktuellerSatz + "</Satz>\n <Absatz>" + _aktuellerAbsatz + "</Absatz>\n<Kapitel>"
				+ _aktuellesKapitel + "</Kapitel>\n";
	}

	// TODO gucken ob nachfolgendes noch irgendwo benötigt werden könnte
	// @Override
	// private boolean gueltigerZeitwert(Duration zeit) { // TODO entfernen
	// return zeit.toMillis() >= 0 && zeit.toMillis() <=
	// _navi.getZeitdauerArtikel().toMillis();
	// }
	//
	// @Override
	// public boolean gueltigeKapitelanzahl() {
	// return _kapitelanzahlArtikel > 0 && _kapitelanzahlArtikel <
	// _wortanzahlArtikel;
	// }
	//
	// @Override
	// public boolean gueltigeAbsatzanzahl() {
	// return _absatzanzahlArtikel > 0 && _absatzanzahlArtikel <=
	// _satzanzahlArtikel
	// && _absatzanzahlArtikel < _wortanzahlArtikel;
	// // TODO && _absatzanzahlArtikel > _kapitelanzahlArtikel
	// }
	//
	// @Override
	// public boolean gueltigeSatzanzahl() {
	// return _satzanzahlArtikel > 0 && _satzanzahlArtikel < _wortanzahlArtikel
	// && _satzanzahlArtikel > _kapitelanzahlArtikel;
	// }
	//
	// @Override
	// public boolean gueltigeWortanzahl() {
	// return _wortanzahlArtikel > 0 && _wortanzahlArtikel > _satzanzahlArtikel
	// && _wortanzahlArtikel > _absatzanzahlArtikel && _wortanzahlArtikel >
	// _kapitelanzahlArtikel;
	// }

}
