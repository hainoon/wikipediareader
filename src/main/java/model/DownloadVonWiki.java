package model;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import exceptions.ArtikelExistiertNichtException;

/**
 * Lädt Html-Dateien von Wikipedia herunter
 * 
 * @author marcel
 *
 */
public class DownloadVonWiki extends Observable {
	
	private String sprache;
	private String abkuerzung;

	public DownloadVonWiki(String sprache2) {
		sprache = sprache2;
		if(sprache.equals("Englisch")){
			abkuerzung = "en";
		}else{
			abkuerzung = "de";
		}
	}

	public void download(String name) throws IOException, ArtikelExistiertNichtException {
		String artikel = istTitel(name);
		if (artikel != null) {
			File f = new File("Artikel/"+sprache+"/" + name + "/wiki.html");
			FileUtils.writeStringToFile(f, Jsoup
					.connect("http://"+abkuerzung+".wikipedia.org/w/index.php?action=render&title=" + artikel).get().outerHtml(),
					"UTF-8");
			setChanged();
			notifyObservers(Statusmeldungen.ArtikelGeladen);
		} else {
			throw new ArtikelExistiertNichtException();
		}
	}

	/**
	 * Prüft ob ein passender Artikel bei Wikipedia existiert
	 * 
	 * @param titel
	 * @return
	 */
	private String istTitel(String titel) { // TODO sauber machen
		try {
			String anfrage = "https://"+abkuerzung+".wikipedia.org/w/api.php?action=query&list=search&srsearch=" + titel
					+ "&srwhat=nearmatch&format=json&continue=";

			URL url = new URL(anfrage);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			String st1 = IOUtils.toString(conn.getInputStream(), "UTF-8");
			JsonParser p = new JsonParser();
			JsonObject obj = p.parse(st1).getAsJsonObject().getAsJsonObject("query").getAsJsonArray("search").get(0)
					.getAsJsonObject();
			String t = obj.get("title").getAsString();
			// erst durch das aufrufen von title wird geprüft ob der Artikel
			// existiert
			setChanged();
			notifyObservers(Statusmeldungen.ArtikelVorhanden);
			return t;
		} catch (IOException | IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}

}
