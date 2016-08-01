//package navigationTest;
//
//import static org.junit.Assert.*;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import javafx.util.Duration;
//import model.ModelLadefortschritt;
//import model.Navigation;
//import model.Navigation_Impl;
//
//public class NavigationTest {
//
//	@Before
//	public void init() throws Exception {
//		new Navigation_Impl("test/navigationTest/Testdaten/Minimalbeispiel1/", new ModelLadefortschritt(0, ""), false);
//	}
//
//	@Test
//	public void testNavigation_Impl2() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testFindeWort_vorwaerts() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testFindeWort_rueckwaerts() {
//		fail("Not yet implemented"); // TODO
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
//	@Test
//	public void testGetZeitdauerArtikel() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetLinks() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public void testGetInhaltsverzeichnis() {
//		fail("Not yet implemented"); // TODO
//	}
//
//}
