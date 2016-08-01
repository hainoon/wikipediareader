package main;

import java.io.IOException;

import exceptions.FehlerhafterSatzException;
import exceptions.FehlerhaftesWortException;
import controler.Controller;


/**
 * Klasse zum Starten des Projekts. Enthält main-Methode.
 * 
 * Vorm starten muss sox fürs mergen von wav-Dateien installiert werden und ggf.
 * einige Dateien für Marytts ergänzt werden TODO
 * 
 * @author marcel
 *
 */
public class StartUp {

	/**
	 * Main-Methode
	 * 
	 * @param args
	 * @throws IOException
	 * @throws FehlerhafterSatzException
	 * @throws FehlerhaftesWortException
	 */
	public static void main(String[] args) throws IOException, FehlerhaftesWortException, FehlerhafterSatzException {
		Controller.launch(Controller.class, args);
//		StartUpTest.launch(StartUpTest.class, args);
	}

	//TODO eigentlich sollte Klasse überflüssig sein, da auch mittels Controller.start() gestartet werden sollte
}
