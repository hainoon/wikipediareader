//package navigationTest;
//
//import static org.junit.Assert.*;
//
//import org.jsoup.nodes.Element;
//import org.junit.Test;
//
//import javafx.util.Duration;
//import model.ModelLadefortschritt;
//import model.Read_Model_Impl;
//import model.Write_Model;
//
//public class DatenstrukturTest {
//
//	@Test
//	public void testGetWortElement() throws Exception {
//		String pfad = "Minimalbeispiel1";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		w.start();
//		Read_Model_Impl w2 = new Read_Model_Impl(w.getDocument(),new ModelLadefortschritt(0,""));
//		String wort = w2.getWortElement(0).toString().replace("\n", "");
//		String vergleich = "<word index=\"0\" start=\"600\" stop=\"850\" original=\"&quot;Sie &quot;\" normalized=\"&quot;Sie&quot;\" html=\"Die\"> Sie </word>";
//		assertEquals(vergleich, wort);
//
//	}
//
//	@Test
//	public void testGetWort() throws Exception {
//		String pfad = "Minimalbeispiel1";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		w.start();
//		Read_Model_Impl w2 = new Read_Model_Impl(w.getDocument(),new ModelLadefortschritt(0,""));
//		String wort = w2.getWort(41);
//		assertEquals(wort, "Die");
//	}
//
//	@Test
//	public void getWortStartzeit() throws Exception {
//		String pfad = "Minimalbeispiel1";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		w.start();
//		Read_Model_Impl w2 = new Read_Model_Impl(w.getDocument(),new ModelLadefortschritt(0,""));
//		Duration wort = w2.getWortStartzeit(41);
//		assertEquals(wort, new Duration(23880));
//	}
//
//	@Test
//	public void testGetSatz() throws Exception {
//		String pfad = "Minimalbeispiel1";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		w.start();
//		Read_Model_Impl w2 = new Read_Model_Impl(w.getDocument(),new ModelLadefortschritt(0,""));
//		String satz = w2.getSatz(2);
//		String vergleich = "Die 295 km lange Isar ist ein Fluss in Tirol (Österreich) und Bayern (Deutschland).";
//		assertEquals(satz, vergleich);
//	}
//
//	@Test
//	public void testGetSatzNummerFuerWortIndex() throws Exception {
//		String pfad = "Minimalbeispiel1";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		w.start();
//		Read_Model_Impl w2 = new Read_Model_Impl(w.getDocument(),new ModelLadefortschritt(0,""));
//		int index = w2.getSatzNummerFuerWortIndex(0);
//		assertEquals(index, 0);
//		index = w2.getSatzNummerFuerWortIndex(1);
//		assertEquals(index, 0);
//		index = w2.getSatzNummerFuerWortIndex(12);
//		assertEquals(index, 0);
//		index = w2.getSatzNummerFuerWortIndex(13);
//		assertEquals(index, 1);
//		index = w2.getSatzNummerFuerWortIndex(41);
//		assertEquals(index, 2);
//		index = w2.getSatzNummerFuerWortIndex(42);
//		assertEquals(index, 2);
//		index = w2.getSatzNummerFuerWortIndex(59);
//		assertEquals(index, 2);
//		try {
//			w2.getSatzNummerFuerWortIndex(60);
//			fail("Ungültiger Index akzeptiert");
//		} catch (Exception e) {
//
//		}
//	}
//
//	@Test
//	public void testGetSatznummerFuerWortIndexGeschachtelteStruktur() throws Exception {
//		String pfad = "Minimalbeispiel2";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		w.start();
//		Read_Model_Impl w2 = new Read_Model_Impl(w.getDocument(),new ModelLadefortschritt(0,""));
//		// Teste Satzindex von Wort das in einem Link enthalten ist
//		int index = w2.getSatzNummerFuerWortIndex(48);
//		assertEquals(index, 2);
//	}
//
//	@Test
//	public void testGetSatzStartzeit() throws Exception {
//		String pfad = "Minimalbeispiel1";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		w.start();
//		Read_Model_Impl w2 = new Read_Model_Impl(w.getDocument(),new ModelLadefortschritt(0,""));
//		Duration satz = w2.getSatzStartzeit(0);
//		assertEquals(satz, new Duration(600));
//		satz = w2.getSatzStartzeit(2);
//		assertEquals(satz, new Duration(23880));
//	}
//
//	@Test
//	public void testGetKapitelStartzeit() throws Exception {
//		String pfad = "Konkretes_Beispiel3";
//		Write_Model w = new Write_Model(pfad, new ModelLadefortschritt(0,""), false);
//		w.start();
//		Read_Model_Impl w2 = new Read_Model_Impl(w.getDocument(), new ModelLadefortschritt(0, ""));
//		Duration kapitel = w2.getKapitelStartzeit(0);
//		assertEquals(kapitel, new Duration(0));
//		//TODO Präambel als eigenes Kapitel in Datenstruktur einbauen
//		kapitel = w2.getKapitelStartzeit(1);
//		assertEquals(kapitel, new Duration(93690));
//	}
//
//	@Test
//	public void testGetAbsatzStartzeit() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetInhaltsverzeichnis() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetGroesse() {
//		fail("Not yet implemented"); // TODO
//	}
//
//}
