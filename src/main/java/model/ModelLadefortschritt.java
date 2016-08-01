package model;

import java.util.Observable;
import java.util.Observer;

/**
 * Modell für die Anzeige die beim Artikelwechsel den Fortschritt der
 * Generierung von Datenstruktur- und Audiodateien anzeiggt
 * 
 * @author marcel
 *
 */
public class ModelLadefortschritt extends Observable implements Observer {

	private boolean ladevorgangAktiv;
	private double prozessfortschritt;
	private double teilprozessfortschritt;
	private double anzahlHauptschritte;
	private double aktuellerHauptschritt;
	private double anzahlTeilschritte;
	private double aktuellerTeilschritt;
	private String aktuelleHauptoperation; // TODO in Gui auslagern
	private String aktuelleTeiloperation; // TODO in Gui auslagern

	public ModelLadefortschritt(int anzahlHauptschritte, String aktuelleHauptoperation) {
		super();
		this.anzahlHauptschritte = anzahlHauptschritte;
		this.ladevorgangAktiv = false;
		this.prozessfortschritt = 0;
		this.teilprozessfortschritt = 0;
		this.aktuellerHauptschritt = 0;
		this.aktuellerTeilschritt = 0;
		this.aktuelleHauptoperation = aktuelleHauptoperation;
	}

	public boolean isLadevorgangAktiv() {
		return ladevorgangAktiv;
	}

	public void setLadevorgangAktiv(boolean ladevorgangAktiv) {
		this.ladevorgangAktiv = ladevorgangAktiv;
		setChanged();
		if (ladevorgangAktiv == true) {
			reset();
			notifyObservers(Statusmeldungen.Ladevorgang1);
		} else {
			notifyObservers(Statusmeldungen.Ladevorgang0);
		}
	}

	public double getProzessfortschritt() {
		return prozessfortschritt;
	}

	public double getTeilprozessfortschritt() {
		return teilprozessfortschritt;
	}

	public void setProzessfortschritt(double prozessfortschritt) {
		this.prozessfortschritt = prozessfortschritt;
	}

	public double getAnzahlHauptschritte() {
		return anzahlHauptschritte;
	}

	public void setAnzahlHauptschritte(int anzahlHauptschritte) {
		this.anzahlHauptschritte = anzahlHauptschritte;
	}

	public double getAktuellerHauptschritt() {
		return aktuellerHauptschritt;
	}

	public void setAktuellerHauptschritt(int aktuellerHauptschritt) {
		this.aktuellerHauptschritt = aktuellerHauptschritt;
	}

	public double getAnzahlTeilschritte() {
		return anzahlTeilschritte;
	}

	public void setAnzahlTeilschritte(int anzahlTeilschritte) {
		this.anzahlTeilschritte = anzahlTeilschritte;
	}

	public double getAktuellerTeilschritt() {
		return aktuellerTeilschritt;
	}

	public void setAktuellerTeilschritt(int aktuellerTeilschritt) {
		this.aktuellerTeilschritt = aktuellerTeilschritt;
	}

	public String getAktuelleHauptoperation() {
		return aktuelleHauptoperation;
	}

	public void setAktuelleHauptoperation(String aktuelleHauptoperation) {
		this.aktuelleHauptoperation = aktuelleHauptoperation;
	}

	public String getAktuelleTeiloperation() {
		return aktuelleTeiloperation;
	}

	public void setAktuelleTeiloperation(String aktuelleTeiloperation) {
		this.aktuelleTeiloperation = aktuelleTeiloperation;
	}

	public void updateHauptschritte(String hauptschritt) {
		aktuelleHauptoperation = aktuelleHauptoperation + ": abgeschlossen!";
		setChanged();
		notifyObservers(Statusmeldungen.Hauptschritt);
		++aktuellerHauptschritt;
		aktuelleHauptoperation = hauptschritt;
		aktuellerTeilschritt = 0;
		teilprozessfortschritt = 0;
		// System.out.println("H: " + prozessfortschritt);
		setChanged();
		notifyObservers(Statusmeldungen.Hauptschritt);
	}

	public void updateTeilschritt(String teiloperation) {
		++aktuellerTeilschritt;
		setTeilschritt(teiloperation);
	}

	public void updateTeilschritt(double wert, String teiloperation) {
		aktuellerTeilschritt = wert;
		setTeilschritt(teiloperation);
	}

	private void setTeilschritt(String teiloperation) {
		teilprozessfortschritt = aktuellerTeilschritt / anzahlTeilschritte;
		prozessfortschritt = (aktuellerHauptschritt / anzahlHauptschritte)
				+ ((1.0 / anzahlHauptschritte) * teilprozessfortschritt);
		aktuelleTeiloperation = teiloperation;
		setChanged();
		notifyObservers(Statusmeldungen.Teilschritt);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO idealerweise Logik Programmablauf auslagern
		if (o instanceof Write_Model) {
			anzahlTeilschritte = 9; // TODO Logik auslagern
			if (arg instanceof String) {
				updateTeilschritt((String) arg); // TODO auf STatusmeldung
													// umstellen
			}
		} else if (o instanceof DownloadVonWiki) {
			// TODO Logik auslagern
			anzahlTeilschritte = 2;
			if (arg == Statusmeldungen.ArtikelVorhanden) {
				updateTeilschritt("Download Wiki: Test ob Artikel vorhanden");
			} else if (arg == Statusmeldungen.ArtikelGeladen) {
				updateTeilschritt("Download Wiki: Artikel speichern");
				updateHauptschritte("Timinginformationen berechnen");
			} else {
				System.err.println();// TODO
			}
		} else if (o instanceof SynthesizeModel) { //TODO: seems to not do the right thing (too few steps are shown)
			anzahlTeilschritte = ((SynthesizeModel) o).processingStepsTotal();
			if (arg == Statusmeldungen.WavTeil) {
				int aktuell = ((SynthesizeModel) o).processingStepsDone();
				updateTeilschritt(aktuell, "audio " + aktuell + " von " + anzahlTeilschritte + " erstellt.");
			} else if (arg == Statusmeldungen.Wav) {
				updateHauptschritte("Artikel synthetisiert");
			}
		} else if (o instanceof Write_Model) {
			// TODO Schritte feingliedriger (mehr Observermeldungen)
			// TODO Logik auslagern
			anzahlTeilschritte = 3;
			if (arg == Statusmeldungen.Html) {
				System.out.println("html");
				updateTeilschritt("Html ausgelesen");
			} else if (arg == Statusmeldungen.Json) {
				System.out.println("json");
				updateTeilschritt("Json ausgelesen");
			} else if (arg == Statusmeldungen.Datenstruktur) {
				updateHauptschritte("Sprachsynthese (wav erstellen)");
			}
		} else if (o instanceof Read_Model) {
			anzahlTeilschritte = 1; // TODO
			if (arg == Statusmeldungen.Reader) {
				updateHauptschritte("Reader erstellt");
			}
		}
	}

	private void reset() {
		ladevorgangAktiv = false;
		prozessfortschritt = 0;
		teilprozessfortschritt = 0;
		anzahlHauptschritte = 0;
		aktuellerHauptschritt = 0;
		anzahlTeilschritte = 0;
		aktuellerTeilschritt = 0;
		aktuelleHauptoperation = ""; // TODO in Gui auslagern
		aktuelleTeiloperation = "";

	}

}
