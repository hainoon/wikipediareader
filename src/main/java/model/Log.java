package model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import sprachsteuerung.Befehl;

/**
 * Loggt Nutzerverhalten
 * 
 * @author marcel
 *
 */
public class Log {
	private ModelPositionsparameter aktuellePosition;
	private Logger logger;
	private Handler handler;

	public Log(ModelPositionsparameter aktuellePosition1) throws SecurityException, IOException {
		aktuellePosition = aktuellePosition1;
		logger = Logger.getLogger(this.getClass().getName());
		File ordner = new File("Logger/");
		if (!ordner.exists()) {
			ordner.mkdirs();
		}
		int anzahl = ordner.listFiles().length;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
		Date dt = new Date();
		String zeit = sdf.format(dt);
		handler = new FileHandler("Logger/" + "Test" + anzahl + "|Datum:" + zeit + ".wikipediabrowser_log.xml");
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		logger.info("Initialisiert");
	}

	public void aktionButton(String string) {
		ArrayList<Object> a = getPositionsInformationen();
		logger.log(Level.INFO, string, a);
	}

	public void aktionButton(String string, String details) {
		ArrayList<Object> b = new ArrayList<>();
		b.add(details);
		ArrayList<Object> a = getPositionsInformationen();
		b.add(a);
		logger.log(Level.INFO, string, b);

	}

	public void aktionSlider(String string) {
		ArrayList<Object> a = getPositionsInformationen();
		logger.log(Level.INFO, string, a);
	}

	public void ereignis(String string) {
		ArrayList<Object> a = getPositionsInformationen();
		logger.log(Level.INFO, string, a);
	}

	public void aktionSprachbefehl(String string, Befehl befehl) {
		ArrayList<Object> a = getPositionsInformationen();
		ArrayList<Object> b = new ArrayList<>();
		ArrayList<Object> c = new ArrayList<>();
		b.add(a);
		b.add(befehl.getBefehlAlsString());
		c.add(befehl.getWert());
		c.add(befehl.getAktion());
		c.add(befehl.getArtikelElement());
		c.add(befehl.getPosition());
		c.add(befehl.getRichtung());
		b.add(c);
		logger.log(Level.INFO, string, b);
	}

	private ArrayList<Object> getPositionsInformationen() {
		ArrayList<Object> a = new ArrayList<Object>();
		a.add(aktuellePosition.get_aktuellerArtikel());
		a.add(aktuellePosition.get_aktuelleZeit().toMillis());
		a.add(aktuellePosition.get_aktuellerIndex());
		a.add(aktuellePosition.get_aktuellesWort());
		a.add(aktuellePosition.get_aktuellerSatz());
		a.add(aktuellePosition.get_aktuellerAbsatz());
		a.add(aktuellePosition.get_aktuellesKapitel());
		return a;
	}

	public void warnung(Exception e) {
		logger.warning(e.getMessage());
	}

	@SuppressWarnings("unused")
	public void warnung(String e, String element, String fehler) {
		logger.warning(e);
	}

	public void methode(String klasse, String methode) {
		logger.log(Level.INFO, klasse + "." + methode);
	}

	public void methode(String klasse, String methode, Object[] parameter) {
		logger.log(Level.INFO, klasse + "." + methode, parameter);
	}

	public void updatePosition() {
		logger.log(Level.INFO, "aktualisierte Position", getPositionsInformationen());
	}

}
