package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import exceptions.InhaltsverzeichnisExistiertNichtException;

/**
 * Parsen von HTML-Dateien
 * 
 * @author marcel
 *
 */
public class Html_Parser {

	private File input;
	private Document doc;

	/**
	 * Konstruktor
	 * 
	 * @param pfad
	 *            Pfad wo sich benötigte Dateien befinden
	 */
	public Html_Parser(File file) {
		input = file;
		try {
			doc = Jsoup.parse(input, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Filtert das Inhaltsverzeichnis aus der Datei
	 * 
	 * @return
	 */
	public ArrayList<String> getInhaltsverzeichnis() {
		Element content = doc.getElementById("toc");
		Elements links = content.child(1).getElementsByTag("a");
		ArrayList<String> output = new ArrayList<String>();
		output.add("Präambel"); // TODO
		output.add("Start"); // TODO
		for (Element link : links) {
			// String linkHref = link.attr("href");
			String linkText = link.text();
			output.add(linkText);
		}
		return output;
	}

	public Elements getInhaltsverzeichnisTag() throws InhaltsverzeichnisExistiertNichtException {
		try {
			return doc.getElementById("toc").children();
		} catch (Exception e) {
			throw new InhaltsverzeichnisExistiertNichtException();
		}
	}

	/**
	 * Gibt alle Links der Datei zurück
	 * 
	 * @return
	 */
	public ArrayList<String> getLinkListe() {
		Elements links = doc.select("a[href]");
		ArrayList<String> result = new ArrayList<>();
		for (Element e : links) {
			result.add(e.text());
		}
		return result;
	}

	/**
	 * Gibt die Links aus dem relevanten Teil der Datei zurück (aus Absätzen
	 * usw)
	 * 
	 * @return
	 */
	public ArrayList<String> getLinkListeRelevanterTeil() {
		Elements relevant = doc.select("p");
		Document doc2 = (Document) new Document("").insertChildren(0, relevant);
		Elements links = doc2.select("a[href]");
		ArrayList<String> result = new ArrayList<>();
		for (Element e : links) {
			result.add(e.text());
		}
		return result;
	}

	/**
	 * Filtert die Absätze aus der Datei
	 * 
	 * @return
	 */
	public ArrayList<String> getAbsaetze() {
		Elements inhalt = doc.getElementsByTag("p");
		ArrayList<String> absaetze = new ArrayList<String>();
		for (int i = 0; i < inhalt.size(); ++i) {
			absaetze.add(inhalt.get(i).text());
		}
		return absaetze;
	}

	/**
	 * Gibt den Inhalt von p-Tags zurück, die ein gesuchtes Wort enthalten
	 * 
	 * @param wort
	 * @return
	 */
	public String sucheAbsatz(String wort) {
		return doc.select("p:contains(" + wort + ")").text();
	}

	/**
	 * Gibt eine Liste mit Elementen zurück die in der HTML-Datei in
	 * <p>
	 * bzw <h...> liegen
	 * 
	 * @return
	 */
	public ArrayList<String> getWortliste() {
		Elements elemente = getRelevantenHtmlTeil();
		ArrayList<String> list = new ArrayList<String>();
		for (Element e : elemente) {
			String s = e.text();
			String[] st = s.split(" ");
			for (String st1 : st) {
				// list.add(st1.replaceAll("\\p{P}", "")); // TODO
				list.add(st1);
			}
		}
		return list;
	}

	/**
	 * Ermittelt durch Tiefensuche die einzelnen Wörter
	 * 
	 * @param list
	 * @return
	 */
	public ArrayList<String> getWortListeRekursiv(List<Node> list) {
		ArrayList<String> eNeu = new ArrayList<String>();
		for (Node e : list) {
			if (e.childNodeSize() > 0) {
				ArrayList<String> eNeu2 = getWortListeRekursiv(e.childNodes());
				eNeu.addAll(eNeu2);
			} else {
				try {
					String t = ((TextNode) e).text();
					String[] text = t.split(" ");
					for (String s : text) {
						eNeu.add(s);
					}
				} catch (Exception ex) {
					System.out.println("Textknoten konnte nicht gecastet werden");
					// TODO sauberer lösen
				}

			}
		}
		return eNeu;
	}

	/**
	 * Filtert Tabellen usw aus der HTML-Datei
	 * 
	 * @return
	 */
	public Elements getRelevantenHtmlTeil() { // TODO !!! Fehlerquelle !!!
		Elements elemente_alt = doc.body().children();
		Elements elemente_neu = new Elements();
		for (Element e : elemente_alt) {
			if (e.tagName().equals("p") || e.tagName().equals("ul") || e.tagName().contains("h")) {
				if (!e.text().equals("")) { // TODO
					elemente_neu.add(e);
				}
			}
		}
//		Elements elemente_neu = elemente_alt.select("p,h1,h2,h3,ul");
//		for(Element e: elemente_neu){
//			if(!e.hasText() && e.isBlock()){
//				elemente_neu.remove(e);
//			}
//		}
		return elemente_neu;
	}

	public boolean htmlTags() {
		return doc.child(0).tagName().equals("html");
	}

}
