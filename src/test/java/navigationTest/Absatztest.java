///**
// * 
// */
//package navigationTest;
//
//import static org.junit.Assert.*;
//
//import javax.xml.transform.TransformerFactoryConfigurationError;
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
//public class Absatztest {
//
//	@Test
//	public void testGetStartwertNaechsterAbsatz() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("Konkretes_Beispiel3", new ModelLadefortschritt(0, ""), false);
//		Duration zeit = n.getStartwertNaechsterAbsatz(0);
//		assertEquals(new Duration(32630), zeit);
//		zeit = n.getStartwertNaechsterAbsatz(1);
//		assertEquals(new Duration(32630), zeit);
//		zeit = n.getStartwertNaechsterAbsatz(60);
//		assertEquals(new Duration(79140), zeit);
//		zeit = n.getStartwertNaechsterAbsatz(61);
//		assertEquals(new Duration(79140), zeit);
//		zeit = n.getStartwertNaechsterAbsatz(181); // Loisach -> Etymologie
//		assertEquals(new Duration(93690), zeit);
//		zeit = n.getStartwertNaechsterAbsatz(183); // Etymologie -> Von
//		assertEquals(new Duration(115240), zeit);
//		try {
//			zeit = n.getStartwertNaechsterAbsatz(5049);
//			fail("Ungültigen Index akzeptiert");
//		} catch (Exception e) {
//
//		}
//	}
//
//	@Test
//	public void testGetStartwertVorherigerAbsatz() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("Konkretes_Beispiel3", new ModelLadefortschritt(0, ""), false);
//		Duration zeit;
//		try {
//			zeit = n.getStartwertVorherigerAbsatz(0);
//			fail("Ungültigen Index akzeptiert");
//		} catch (Exception e) {
//
//		}
//		zeit = n.getStartwertVorherigerAbsatz(60);
//		assertEquals(new Duration(600), zeit);
//		zeit = n.getStartwertVorherigerAbsatz(61);
//		assertEquals(new Duration(600), zeit);
//		zeit = n.getStartwertVorherigerAbsatz(183);
//		assertEquals(new Duration(79140), zeit);
//		zeit = n.getStartwertVorherigerAbsatz(184);
//		assertEquals(new Duration(79140), zeit);
//	}
//	
//	@Test
//	public void testGetAbsatzzanfangVonWort() {
//		fail("Not yet implemented"); // TODO
//	}
//	
//	@Test
//	public void testGetAbsatzX() throws TransformerFactoryConfigurationError, Exception {
//		Navigation_Impl n = new Navigation_Impl("Isar", new ModelLadefortschritt(0, ""), false);
//		assertEquals(248330, n.getAbsatzX(10).toMillis(),0);
//	}
//	
//	@Test
//	public void testGetAbsatz() {
//		fail("Not yet implemented"); // TODO
//	}
//	
//	@Test
//	public void testGetAbsatzanzahlInArtikel() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("Minimalbeispiel4", new ModelLadefortschritt(0, ""), false);
//		int anzahl = n.getAbsatzanzahlInArtikel();
//		assertEquals(6, anzahl);
//	}
//	
//	@Test
//	public void testSonstiges() throws TransformerFactoryConfigurationError, Exception{
//		Navigation_Impl n = new Navigation_Impl("Isar", new ModelLadefortschritt(0, ""), false);
//		assertEquals(9, n.getAbsatz(426));
//		assertEquals(10, n.getAbsatz(511));
//		assertEquals(10, n.getAbsatz(513));
//	}
//
//}
