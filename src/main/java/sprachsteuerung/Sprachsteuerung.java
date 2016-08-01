/**
 * 
 */
package sprachsteuerung;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.xml.transform.TransformerFactoryConfigurationError;

import exceptions.AbsatzExistiertNichtException;
import exceptions.ArtikelExistiertNichtException;
import exceptions.AudiodateiExistiertNichtException;
import exceptions.FehlerhafterSatzException;
import exceptions.FehlerhaftesWortException;
import exceptions.KapitelExistiertNichtException;
import exceptions.SatzExistiertNichtException;
import exceptions.SprachbefehlAufnahmeException;
import exceptions.WortExistiertNichtException;
import marytts.exceptions.MaryConfigurationException;
import model.Log;
import model.ModelPositionsparameter;
import player.Player;
import player.Player_javafx;

/**
 * Sprachsteuerung des Players
 * 
 * @author marcel
 *
 */
public class Sprachsteuerung {
	private GoogleSpeech g;
	private Player p;
	private ModelPositionsparameter position;
	private Log logger;
	private boolean wizzard;

	/**
	 * Konstruktor
	 * 
	 * @param p1
	 *            Player
	 */
	public Sprachsteuerung(Player p1, Log l) {
		p = p1;
		position = p.getAktuellePositionsparameter();
		logger = l;
	}

	/**
	 * Starte Aufnahme des Sprachbefehls
	 * 
	 * @throws SprachbefehlAufnahmeException
	 *             Aufnahme des Sprachbefehls fehlgeschlagen
	 */
	public void sprachbefehlStart() throws SprachbefehlAufnahmeException {
		g = new GoogleSpeech();
		System.out.println("Mouse Pressed");
		p.pauseAudio();
		g.sprachbefehlStart();
	}

	/**
	 * Beende Aufnahme des Sprachbefehls und starte Auswertung
	 * 
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 */
	public String sprachbefehlEnde() throws TransformerFactoryConfigurationError, Exception {
		System.out.println("Mouse Released");
		p.startAudio();
		Befehl befehl = g.sprachbefehlEnde();

		// loggt die erkannten Spracheingaben
		Object[] params = new Object[1 + befehl.getAlternativen().size()];
		params[0] = befehl.getBefehlAlsString();
		for (int i = 0; i < befehl.getAlternativen().size(); ++i) {
			params[i + 1] = befehl.getAlternativen().get(i);
		}
		logger.methode("Sprachsteuerung", "erkannte Spracheingabe", params);
		if (!wizzard) {
			System.out.println("Befehl: " + befehl.getBefehlAlsString());
			// try {
			try {
				werteAus(befehl);

			} catch (Exception e) {
				Stack<String> alternativen = new Stack<>();
				List<String> a = befehl.getAlternativen();
				Collections.reverse(a);
				alternativen.addAll(a);
				while (!alternativen.isEmpty()) {
					try {
						String alternativBefehl = alternativen.pop();
						befehl = new Befehl(alternativBefehl, alternativen);
						werteAus(befehl);
						return befehl.getBefehlAlsString();
					} catch (Exception e1) {
						// TODO
					}
				}
				throw e;
			}
		}

		return befehl.getBefehlAlsString();
		// }
		// catch (SprachbefehlException e) {
		// return null;
		// }
	}

	/**
	 * Werte Sprachbefehl aus und führe ihn aus
	 * 
	 * @param befehl
	 *            Befehl
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 */
	private void werteAus(Befehl befehl) throws TransformerFactoryConfigurationError, Exception {
		System.out.println("Werte aus: " + befehl.getBefehlAlsString() + ": " + befehl.getAktion());
		// Springe zu Position Start / Ende
		if (befehl.istNurAktionUndPosition()) {
			System.out.println("Fall 1: Springe zu Position Start/ Ende");
			if (befehl.getPosition() == Position.ANFANG) {
				springeZuAnfang(befehl);
			} else {
				springeZuEnde(befehl);
			}
		}
		// Springe 1 Element vor / zurück
		else if (befehl.istNurElementUndRichtung()) {
			System.out.println("Fall 2: Springe 1 Element vor / zurück");
			springeEinElement(befehl);

		}
		// Springe vor/ zurück (Kapitel)
		else if (befehl.istNurRichtung()) {
			System.out.println("Fall 2b: Springe vor/ zurück (Kapitel)");
			springeEinElement(befehl);

		} else if (befehl.istNurWert()) {
			p.springeZuKapitel(befehl.getWert());
		} else if (befehl.istNurElement()) {
			springeEinElement(befehl);
		}
		// Keine Befehlselemente -> Springe zu spezifischen Namen
		else if (befehl.istBezeichner()) {
			System.out.println("Fall 3: Keine Befehlselemente -> Springe zu spezifischen Namen");
			bezeichnerBefehl(befehl);
			// Normalfälle
		} else {
			System.out.println("Fall 4: Normalfälle ");
			switch (befehl.getAktion()) {
			case PAUSE:
				logger.aktionSprachbefehl("Pause", befehl);
				p.pauseAudio();
				break;
			case START:
				logger.aktionSprachbefehl("Start", befehl);
				p.startAudio();
				break;
			case STOP:
				logger.aktionSprachbefehl("Stop", befehl);
				p.stopAudio(true);
				break;
			case INHALTSVERZEICHNIS:
				logger.aktionSprachbefehl("Inhaltsverzeichnis", befehl);
				p.leseInhaltsverzeichnis();
				break;
			case SPRINGE:
				springeZuBefehl(befehl);
				break;
			default:
				logger.warnung("Kein Sprachbefehl erkannt, suche nach Artikel",
						"Sprachbefehl Default Case -> Suche nach Artikel", befehl.getBefehlAlsString());
				System.out.println("Kein Sprachbefehl erkannt, suche nach Artikel");
				// TODO
				p.starteArtikel(befehl.getBefehlAlsString(), true);
			}
		}

	}

	private void bezeichnerBefehl(Befehl befehl) throws KapitelExistiertNichtException, FehlerhaftesWortException,
			WortExistiertNichtException, TransformerFactoryConfigurationError, Exception, MaryConfigurationException,
			InterruptedException, ArtikelExistiertNichtException, IOException, FehlerhafterSatzException,
			SatzExistiertNichtException, AbsatzExistiertNichtException, AudiodateiExistiertNichtException {
		if (!springeZuKapitelName(befehl)) {
			boolean linkname = springeZuLinkName(befehl);
			if (!linkname) {
				System.out.println("Sprachbefehl \"" + befehl.getBefehlAlsString()
						+ "\" konnte nicht zugeordnet werden. Suche Artikel");
				logger.aktionSprachbefehl("Artikelbezeichner", befehl);
				p.starteArtikel(befehl.getBefehlAlsString(), true); // TODO

			}
		}
	}

	private void springeZuBefehl(Befehl befehl) throws FehlerhaftesWortException, WortExistiertNichtException,
			FehlerhafterSatzException, SatzExistiertNichtException, KapitelExistiertNichtException {
		int wert = befehl.getWert();
		if (befehl.getRichtung().equals(Richtung.ZURÜCK) || befehl.getRichtung().equals(Richtung.VORHERIGES)) {
			wert = wert * (-1);
		}
		if (befehl.getRichtung() != Richtung.DEFAULT) {
			wert = position.get_aktuellesKapitel() + wert;
		}
		System.out.println("Skip " + wert + " " + befehl.getArtikelElement());
		// TODO BEGINNING + END OF ...
		switch (befehl.getArtikelElement()) {
		case WORT:
			logger.aktionSprachbefehl("Springe Index", befehl);
			p.springeZuIndex(position.get_aktuellerIndex() + wert);
			break;
		case SATZ:
			logger.aktionSprachbefehl("Springe Satz", befehl);
			p.springeZuSatz(position.get_aktuellerSatz() + wert);
			break;
		case ARTIKEL:
			logger.aktionSprachbefehl("Springe Artikel", befehl);
			System.out.println("TODO ARTICLE");// TODO
			break;
		case KAPITEL:
		default:
			logger.aktionSprachbefehl("Springe Kapitel", befehl);
			p.springeZuKapitel(wert);

		}
	}

	/**
	 * Springt um ein Element vor / zurück
	 * 
	 * @param befehl
	 *            Befehl
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 * @throws AbsatzExistiertNichtException
	 */
	private void springeEinElement(Befehl befehl)
			throws FehlerhaftesWortException, FehlerhafterSatzException, SatzExistiertNichtException,
			WortExistiertNichtException, KapitelExistiertNichtException, AbsatzExistiertNichtException {
		switch (befehl.getRichtung()) {
		case ZURÜCK:
			zurueck1Befehl(befehl);
			break;
		case WEITER:
		default:
			weiter1Befehl(befehl);
			break;
		}
	}

	private void zurueck1Befehl(Befehl befehl) throws FehlerhaftesWortException, SatzExistiertNichtException,
			WortExistiertNichtException, FehlerhafterSatzException, KapitelExistiertNichtException {
		switch (befehl.getArtikelElement()) {
		case ARTIKEL:
			logger.aktionSprachbefehl("1 Artikel zurück", befehl);
			break;
		// TODO
		case SATZ:
			logger.aktionSprachbefehl("1 Satz zurück", befehl);
			p.vorherigerSatz();
			break;
		case WORT:
			logger.aktionSprachbefehl("1 Wort zurück", befehl);
			p.vorherigesWort();
			break;
		case KAPITEL:
		default:
			logger.aktionSprachbefehl("1 Kapitel zurück", befehl);
			p.vorherigesKapitel();
			break;
		}
	}

	private void weiter1Befehl(Befehl befehl)
			throws FehlerhaftesWortException, FehlerhafterSatzException, SatzExistiertNichtException,
			WortExistiertNichtException, KapitelExistiertNichtException, AbsatzExistiertNichtException {
		switch (befehl.getArtikelElement()) {
		case ARTIKEL:
			logger.aktionSprachbefehl("1 Artikel weiter", befehl);
			System.out.println("TODO");
			break;
		// TODO
		case SATZ:
			logger.aktionSprachbefehl("1 Satz weiter", befehl);
			p.naechsterSatz();
			break;
		case ABSATZ:
			logger.aktionSprachbefehl("1 Absatz weiter", befehl);
			p.naechsterAbsatz();
			break;
		case WORT:
			logger.aktionSprachbefehl("1 Wort weiter", befehl);
			p.naechstesWort();
			break;
		case KAPITEL:
		default:
			logger.aktionSprachbefehl("1 Kapitel weiter", befehl);
			p.naechstesKapitel();
			break;
		}
	}

	/**
	 * Springt zu einem Artikel
	 * 
	 * @param befehl
	 *            Befehl
	 * @return Artikel-/Kapitelbezeichner im Befehl erkannt
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 */
	private boolean springeZuLinkName(Befehl befehl) throws TransformerFactoryConfigurationError, Exception {
		for (int i = ((Player_javafx) p).getAlleLinks().firstKey(); i < ((Player_javafx) p).getAlleLinks()
				.lastKey(); i = ((Player_javafx) p).getAlleLinks().higherKey(i)) { // TODO
			String s = ((Player_javafx) p).getAlleLinks().get(i);
			String s2 = ((Player_javafx) p).getAlleLinks().get(i).toUpperCase(); // TODO
			String s1 = befehl.getBefehlAlsString().toUpperCase() + " ";
			if (s1.contains(s2) && !s2.equals("")) {
				System.out.println("|" + s2 + "<" + s1 + "|");// TODO
				logger.aktionSprachbefehl("Link folgen", befehl);
				System.out.println("Folge Link: " + s);
				p.starteArtikel(s, true);

				return true;
			}
		}
		System.out.println("Kein Linkname erkannt");
		return false;
	}

	/**
	 * Springt zu einem Kapitel
	 * 
	 * @param befehl
	 *            Befehl
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 */
	private boolean springeZuKapitelName(Befehl befehl)
			throws KapitelExistiertNichtException, FehlerhaftesWortException, WortExistiertNichtException {
		for (int i = 0; i < p.getInhaltsverzeichnis().size(); ++i) {
			String s = p.getInhaltsverzeichnis().get(i);
			s = s.substring(s.indexOf(" ") + 1);
			if (befehl.getBefehlAlsString().toUpperCase().contains(s.toUpperCase())) {
				logger.aktionSprachbefehl("Kapitelname", befehl);
				p.springeZuKapitel(i);
				return true;
			}
			for (String alternative : befehl.getAlternativen()) {
				if (alternative.toUpperCase().contains(s.toUpperCase())) {
					logger.aktionSprachbefehl("alternativer Kapitelname", befehl);
					p.springeZuKapitel(i);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Springt zu Elementende -> springt um 1 Weiter
	 * 
	 * @param befehl
	 *            Befehl
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 */
	private void springeZuEnde(Befehl befehl) throws FehlerhaftesWortException, FehlerhafterSatzException,
			SatzExistiertNichtException, WortExistiertNichtException, KapitelExistiertNichtException {
		switch (befehl.getArtikelElement()) {
		case SATZ:
			p.naechsterSatz();
			break;
		case ARTIKEL:
			// TODO
			System.out.println("nächster Artikel / Artikelende");
			break;
		case KAPITEL:
		default:
			p.naechstesKapitel();
			// TODO Logger
			// TODO weitere Cases
		}
	}

	/**
	 * Springt zu Elementanfang
	 * 
	 * @param befehl
	 *            Befehl
	 * @throws FehlerhafterSatzException
	 *             Satzinformationen fehlerhaft
	 * @throws WortExistiertNichtException
	 *             Wort existiert nicht
	 * @throws SatzExistiertNichtException
	 *             Satz existiert nicht
	 * @throws FehlerhaftesWortException
	 *             Wortinformationen fehlerhaft
	 * @throws KapitelExistiertNichtException
	 *             Kapitel existiert nicht
	 */
	private void springeZuAnfang(Befehl befehl) throws FehlerhaftesWortException, SatzExistiertNichtException,
			WortExistiertNichtException, FehlerhafterSatzException, KapitelExistiertNichtException {
		switch (befehl.getArtikelElement()) {
		case SATZ:
			p.satzanfang();
			break;
		case ARTIKEL:
			p.artikelanfang();
			break;
		case KAPITEL:
		default:
			p.kapitelanfang();
		}
		// TODO Logger
		// TODO weitere Cases
	}
	
	public void setWizzard(){
		wizzard = true;
	}

}
