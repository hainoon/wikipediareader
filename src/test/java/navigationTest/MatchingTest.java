//package navigationTest;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import org.apache.commons.io.FileUtils;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.junit.Test;
//
//import model.Html_Parser;
//import model.ModelLadefortschritt;
//import model.Read_Model_Impl;
//import model.Write_Model;
//
//public class MatchingTest {
//
//	@Test
//	/**
//	 * Testet einzelnen Satz
//	 * 
//	 * @throws IOException
//	 */
//	public void datenStrukturErstellbarMinimalbeispiel1() throws Exception {
//		String pfad = "Minimalbeispiel1";
//		Write_Model w2 = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		Read_Model_Impl w = new Read_Model_Impl(w2.getDocument(), new ModelLadefortschritt(0,""));
//		assertTrue(vergleicheLinks(w2));
//		assertTrue(w.getAbsaetzeElemente().size() == 1);
//		// assertTrue(w.getWoerter().size() == 60);//TODO
//		assertTrue(w.getSaetzeElemente().size() == 3);
//		assertTrue(w.getWortElement(41).text().equals("Die"));
//		Elements satz = w.getSatzElement(2);
//		System.out.println(w.getText());
//		System.out.println(FileUtils.readFileToString(new File(pfad + "vergleich.txt")));
////		assertTrue(vergleicheTexte(w, pfad)); //TODO
//	}
//
//	@Test
//	/**
//	 * Testet mehrere Sätze
//	 * 
//	 * @throws IOException
//	 */
//	public void datenStrukturErstellbarMinimalbeispiel2() throws Exception {
//		String pfad = "Minimalbeispiel2";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		assertTrue(vergleicheLinks(w));
////		assertTrue(vergleicheTexte(w, pfad)); //TODO
//	}
//
//	@Test
//	/**
//	 * Testet Einbindung in HTML-Datei mit nicht benötigten voranstehenden
//	 * Inhalt
//	 * 
//	 * @throws IOException
//	 */
//	public void datenStrukturErstellbarMinimalbeispiel3() throws Exception {
//		String pfad = "Minimalbeispiel3";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		assertTrue(vergleicheLinks(w));
////		assertTrue(vergleicheTexte(w, pfad)); //TODO
//	}
//
//	@Test
//	/**
//	 * Testet Einbindung in HTML-Datei mit nicht benötigten voranstehenden sowie
//	 * dazwischen stehenden Inhalten (insbesondere leere p-Tags und neue
//	 * Kapitel)
//	 * 
//	 * @throws IOException
//	 */
//	public void datenStrukturErstellbarMinimalbeispiel4() throws Exception {
//		String pfad = "Minimalbeispiel4";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		assertTrue(vergleicheLinks(w));
////		assertTrue(vergleicheTexte(w, pfad)); //TODO
//	}
//
//	@Test
//	/**
//	 * Erstes viertel Isar
//	 * 
//	 * @throws IOException
//	 */
//	public void datenStrukturErstellbarMinimalbeispiel5() throws Exception {
//		Write_Model w = new Write_Model("Minimalbeispiel5",new ModelLadefortschritt(0,""), false);
//		assertTrue(vergleicheLinks(w));
////		assertTrue(vergleicheTexte(w, pfad)); //TODO
//	}
//
//	@Test
//	/**
//	 * span-tag
//	 * 
//	 * @throws IOException
//	 */
//	public void datenStrukturErstellbarMinimalbeispiel6() throws Exception {
//		String pfad = "Minimalbeispiel6";
//		Write_Model w2 = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		Read_Model_Impl w = new Read_Model_Impl(w2.getDocument(),new ModelLadefortschritt(0,""));
//		// assertTrue(vergleicheLinks(w));
//		ArrayList<String> links = w.getLinkListeRelevanterTeil();
//		String[] erwarteteLinksInput = { "Quellen", "Hinterautal", "Karwendelketten", "Gleirsch-Halltal-Kette",
//				"Hinterautal-Vomper-Kette", "Meter über Adria" };
//		ArrayList<String> erwarteteLinks = new ArrayList<>();
//		for (int i = 0; i < erwarteteLinksInput.length; ++i) {
//			erwarteteLinks.add(erwarteteLinksInput[i]);
//		}
//		assertTrue(links.equals(erwarteteLinks));
//		assertTrue(vergleicheTexte(w2, pfad)); //TODO
//	}
//	
//	@Test
//	/**
//	 * Überschriftkorrektur + Postfixkorrekur
//	 * 
//	 * @throws IOException
//	 */
//	public void datenStrukturErstellbarMinimalbeispiel7() throws Exception {
//		String pfad = "Minimalbeispiel7";
//		Write_Model w2 = new Write_Model(pfad, new ModelLadefortschritt(0,""));
//		Read_Model_Impl w = new Read_Model_Impl(w2.getDocument(),new ModelLadefortschritt(0,""));
//		ArrayList<String> links = w.getLinkListeRelevanterTeil();
////		String[] erwarteteLinksInput = { "Marcel Rohde" };
//		String[] erwarteteLinksInput = { "Marcel" };	//TODO eigentlich Marcel Rohde
//		ArrayList<String> erwarteteLinks = new ArrayList<>();
//		for (int i = 0; i < erwarteteLinksInput.length; ++i) {
//			erwarteteLinks.add(erwarteteLinksInput[i]);
//		}
//		assertTrue(links.equals(erwarteteLinks));
//		assertTrue(vergleicheTexte(w2, pfad)); //TODO
//	}
//	
//	@Test
//	/**
//	 * EÜberschriften Korrektur
//	 * 
//	 * @throws IOException
//	 */
//	public void datenStrukturErstellbarMinimalbeispiel8() throws Exception {
//		Write_Model w = new Write_Model("Minimalbeispiel8",new ModelLadefortschritt(0,""));
//		assertTrue(vergleicheLinks(w));
////		assertTrue(vergleicheTexte(w, pfad)); //TODO
//	}
//
//	@Test
//	/**
//	 * Isar komplett
//	 * 
//	 * @throws IOException
//	 */
//	public void datenStrukturErstellbarKonkretesBeispiel1() throws Exception {
//		String pfad = "Konkretes_Beispiel1";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""));
//		assertTrue(vergleicheLinks(w));
//	}
//
//	@Test
//	/**
//	 * 42 komplett
//	 * 
//	 * @throws IOException
//	 */
//	public void datenStrukturErstellbarKonkretesBeispiel2() throws Exception {
//		String pfad = "Konkretes_Beispiel2";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""));
//		assertTrue(vergleicheLinks(w));
//	}
//	
//	@Test
//	/**
//	 * Isar komplett -> manuell bearbeitetes Allignement
//	 * @throws Exception
//	 */
//	public void datenStrukturErstellbarKonkretesBeispiel3() throws Exception {
//		String pfad = "Konkretes_Beispiel3";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""));
//		assertTrue(vergleicheLinks(w));
//	}
//
//	private boolean vergleicheLinks(Write_Model w) throws IOException {
//		Read_Model_Impl w2 = new Read_Model_Impl(w.getDocument(),new ModelLadefortschritt(0,""));
//		ArrayList<String> links = w2.getLinkListeRelevanterTeil();
//		ArrayList<String> links2 = new ArrayList<String>();
//		for (Element e : w2.getLinkElement()) {
//			links2.add(e.text());
//		}
//		return links2.equals(links);
//	}
//
//	private boolean vergleicheTexte(Write_Model w, String pfad) throws IOException {
//		Read_Model_Impl w2 = new Read_Model_Impl(w.getDocument(),new ModelLadefortschritt(0,""));
//		String str = FileUtils.readFileToString(new File(pfad + "vergleich.txt"));
//		return w2.getText().equals(str);
//	}
//
//}
