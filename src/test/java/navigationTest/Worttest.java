///**
// * 
// */
//package navigationTest;
//
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//
//import javafx.util.Duration;
//import model.ModelLadefortschritt;
//import model.Navigation_Impl;
//
///**
// * @author marcel
// *
// */
//public class Worttest {
//
//	@Test
//	public void testGetStartwertWort() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("test/navigationTest/Testdaten/Minimalbeispiel1/", new ModelLadefortschritt(0, ""), false);
//		Duration zeit = n.getStartwertWort(0);
//		assertEquals(zeit, new Duration(600));
//		zeit = n.getStartwertWort(1);
//		assertEquals(zeit, new Duration(850));
//		zeit = n.getStartwertWort(41);
//		assertEquals(zeit, new Duration(23880));
//	}
//
//	@Test
//	public void testGetStartwertNaechstesWort() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("test/navigationTest/Testdaten/Minimalbeispiel1/", new ModelLadefortschritt(0, ""), false);
//		Duration zeit = n.getStartwertNaechstesWort(0);
//		assertEquals(zeit, new Duration(850));
//		zeit = n.getStartwertNaechstesWort(1);
//		assertEquals(zeit, new Duration(1280));
//		zeit = n.getStartwertNaechstesWort(41);
//		assertEquals(zeit, new Duration(24030));
//		try {
//			zeit = n.getStartwertNaechstesWort(59);
//			fail("Ungültigen Index zugelassen");
//		} catch (Exception e) {
//
//		}
//	}
//
//	@Test
//	public void testGetStartwertVorherigesWort() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("test/navigationTest/Testdaten/Minimalbeispiel1/", new ModelLadefortschritt(0, ""), false);
//		Duration zeit = n.getStartwertVorherigesWort(1);
//		assertEquals(zeit, new Duration(600));
//		try {
//			zeit = n.getStartwertVorherigesWort(0);
//			fail("Ungültigen Index zugelassen");
//		} catch (Exception e) {
//
//		}
//	}
//
//	@Test
//	public void testGetWort() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetWortanzahlInArtikel() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("test/navigationTest/Testdaten/Minimalbeispiel1/", new ModelLadefortschritt(0, ""), false);
//		int anzahl = n.getWortanzahlInArtikel();
//		assertEquals(60, anzahl);
//	}
//
//	@Test
//	public void testGetWortIndexAusZeit() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("test/navigationTest/Testdaten/Minimalbeispiel1/", new ModelLadefortschritt(0, ""), false);
//		int index = n.getWortIndexAusZeit(new Duration(0));
//		assertEquals(index, 0);
//		index = n.getWortIndexAusZeit(new Duration(600));
//		assertEquals(0, index);
//		index = n.getWortIndexAusZeit(new Duration(849));
//		assertEquals(0, index);
//		index = n.getWortIndexAusZeit(new Duration(850));
//		assertEquals(1, index);
//	}
//
//}
