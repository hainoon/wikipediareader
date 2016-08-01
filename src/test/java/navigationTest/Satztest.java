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
//public class Satztest {
//
//	@Test
//	/**
//	 * Testet den Zeit-Startwert des nächsten Satzes
//	 * 
//	 * @throws Exception
//	 */
//	public void testGetStartwertNaechsterSatz() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("Minimalbeispiel1", new ModelLadefortschritt(0, ""), false);
//		assertEquals(new Duration(6430), n.getStartwertNaechsterSatz(0));
//		assertEquals(new Duration(6430), n.getStartwertNaechsterSatz(1));
//		assertEquals(new Duration(6430), n.getStartwertNaechsterSatz(12));
//		assertEquals(new Duration(23880), n.getStartwertNaechsterSatz(13));
//		try {
//			n.getStartwertNaechsterSatz(59);
//			fail("Ungültigen Index akzeptiert");
//		} catch (Exception e) {
//
//		}
//
//		try {
//			n.getStartwertNaechsterSatz(60);
//			fail("Ungültigen Index akzeptiert");
//		} catch (Exception e) {
//
//		}
//	}
//
//	@Test
//	/**
//	 * Testet den Zeit-Startwert des vorherigen Satzes
//	 * 
//	 * @throws Exception
//	 */
//	public void testGetStartwertVorherigerSatz() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("Minimalbeispiel1", new ModelLadefortschritt(0, ""), false);
//		assertEquals(new Duration(600), n.getStartwertVorherigerSatz(13));
//		assertEquals(new Duration(600), n.getStartwertVorherigerSatz(14));
//		assertEquals(new Duration(6430), n.getStartwertVorherigerSatz(59));
//
//		try {
//			n.getStartwertVorherigerSatz(0);
//			fail("Ungültigen Index akzeptiert");
//		} catch (Exception e) {
//
//		}
//
//		try {
//			n.getStartwertVorherigerSatz(12);
//			fail("Ungültigen Index akzeptiert");
//		} catch (Exception e) {
//
//		}
//
//		try {
//			n.getStartwertVorherigerSatz(60);
//			fail("Ungültigen Index akzeptiert");
//		} catch (Exception e) {
//
//		}
//	}
//
//	@Test
//	/**
//	 * Testet ob der Zeitwert des Satzanfangs richtig ermittelt wird
//	 * 
//	 * @throws Exception
//	 */
//	public void testGetSatzanfangVonWort() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("Minimalbeispiel1", new ModelLadefortschritt(0, ""), false);
//		assertEquals(n.getSatzanfangVonWort(0), new Duration(600));
//		assertEquals(n.getSatzanfangVonWort(1), new Duration(600));
//		assertEquals(n.getSatzanfangVonWort(12), new Duration(600));
//		assertEquals(n.getSatzanfangVonWort(13), new Duration(6430));
//	}
//
//	@Test
//	/**
//	 * Testet ob der Zeit-Startwert des x-ten Satzes richtig ermittelt wird
//	 * 
//	 * @throws Exception
//	 */
//	public void testGetSatzX() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("Konkretes_Beispiel3", new ModelLadefortschritt(0, ""), false);
//		assertEquals(new Duration(600), n.getSatzX(0));
//		assertEquals(new Duration(6430), n.getSatzX(1));
//		assertEquals(new Duration(93690), n.getSatzX(7));
//		assertEquals(new Duration(115240), n.getSatzX(8));
//
//	}
//
//	@Test
//	/**
//	 * Testet ob für einen Wortindex der Zugehörige Satzindex richtig ermittelt
//	 * wird
//	 * 
//	 * @throws Exception
//	 */
//	public void testGetSatz() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("Konkretes_Beispiel3", new ModelLadefortschritt(0, ""), false);
//		assertEquals(0, n.getSatz(0));
//		assertEquals(0, n.getSatz(1));
//		assertEquals(0, n.getSatz(12));
//		assertEquals(1, n.getSatz(13));
//		assertEquals(7, n.getSatz(183));
//		assertEquals(7, n.getSatz(184));
//	}
//
//	@Test
//	/**
//	 * Testet ob die Satzanzahl im Artikel richtig ermittelt wird
//	 * 
//	 * @throws Exception
//	 */
//	public void testGetSatzanzahlInArtikel() throws Exception {
//		Navigation_Impl n = new Navigation_Impl("Minimalbeispiel1", new ModelLadefortschritt(0, ""), false);
//		assertEquals(3, n.getSatzanzahlInArtikel());
//	}
//	
//	@Test
//	/**
//	 * Sonstiges
//	 */
//	public void testSonstiges() throws TransformerFactoryConfigurationError, Exception{
//		Navigation_Impl n = new Navigation_Impl("Isar", new ModelLadefortschritt(0, ""), false);
//		assertEquals(7, n.getSatz(183));
//	}
//}
