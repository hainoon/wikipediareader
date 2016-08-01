///**
// * 
// */
//package sprachbefehleTest;
//
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//
//import exceptions.SprachbefehlException;
//import sprachsteuerung.Aktion;
//import sprachsteuerung.Befehl;
//import sprachsteuerung.Element;
//import sprachsteuerung.Position;
//import sprachsteuerung.Richtung;
//import sprachsteuerung.Wert;
//
///**
// * @author marcel
// *
// */
//public class BefehlTest {
//
//	@Test
//	public void testeLeerenBefehl() {
//		try {
//			new Befehl("", null);
//			fail("Hätte SprachbefehlException werfen sollen");
//		} catch (SprachbefehlException e) {
//		}
//	}
//
//	@Test
//	public void testeSetzeWertEins() throws SprachbefehlException {
//		Befehl b = new Befehl("Springe", null);
//		assertTrue(Wert.getDefault() == b.getWert());
//		b = new Befehl("Springe 1 weiter", null);
//		assertTrue(1 == b.getWert());
//		b = new Befehl("Springe einen weiter", null);
//		assertTrue(1 == b.getWert());
//		b = new Befehl("Einen weiter springen", null);
//		assertTrue(1 == b.getWert());
//		assertFalse(2 == b.getWert());
//		// b = new Befehl("Ein Haus");
//		// assertEquals(Wert.DEFAULT, b.getWert()); //TODO
//	}
//
//	@Test
//	public void testeSetzeWert() throws SprachbefehlException {
//		Befehl b = new Befehl("Springe 2 weiter", null);
//		assertTrue(2 == b.getWert());
//		b = new Befehl("Springe", null);
//		assertTrue(Wert.getDefault() == b.getWert());
//	}
//
//	@Test
//	public void testeSetzeAktion() throws SprachbefehlException {
//		Befehl b = new Befehl("Springe 2 weiter", null);
//		assertEquals(Aktion.SPRINGE, b.getAktion());
//		b = new Befehl("2 weiter", null);
//		assertEquals(Aktion.DEFAULT, b.getAktion());
//	}
//
//	@Test
//	public void testeSetzeArtikelElement() throws SprachbefehlException {
//		Befehl b = new Befehl("Springe 2 Sätze weiter", null);
//		assertEquals(Aktion.SPRINGE, b.getAktion());
//		b = new Befehl("Springe 1 Satz weiter", null);
//		assertEquals(Aktion.SPRINGE, b.getAktion());
//		b = new Befehl("2 weiter", null);
//		assertEquals(Aktion.DEFAULT, b.getAktion());
//	}
//
//	@Test
//	public void testeSetzeRichtung() throws SprachbefehlException {
//		Befehl b = new Befehl("Springe 2 weiter", null);
//		assertEquals(Richtung.WEITER, b.getRichtung());
//		b = new Befehl("Springe", null);
//		assertEquals(Richtung.DEFAULT, b.getRichtung());
//	}
//
//	@Test
//	public void mehrereWerte() {
//		try {
//			new Befehl("Springe einen zwei weiter", null);
//			fail("Hätte SprachbefehlException werfen sollen");
//		} catch (SprachbefehlException e) {
//		}
//		try {
//			new Befehl("Springe einen weiter", null);
//		} catch (SprachbefehlException e) {
//			fail("Hätte keine SprachbefehlException werfen sollen");
//		}
//	}
//
//	@Test
//	public void mehrereAktionen() {
//		try {
//			new Befehl("Springe Springe", null);
//			fail("Hätte SprachbefehlException werfen sollen");
//		} catch (SprachbefehlException e) {
//		}
//		try {
//			new Befehl("Springe pause", null);
//			fail("Hätte SprachbefehlException werfen sollen");
//		} catch (SprachbefehlException e) {
//		}
//		try {
//			new Befehl("Springe", null);
//		} catch (SprachbefehlException e) {
//			fail("Hätte keine SprachbefehlException werfen sollen");
//		}
//	}
//
//	/**
//	 * Testet ob bei einem Befehl ohne Befehlselemente der Bezeichner übrig
//	 * bleibt (z.B. Link)
//	 * 
//	 * @throws SprachbefehlException
//	 */
//	@Test
//	public void testeBezeichner() throws SprachbefehlException {
//		Befehl b = new Befehl("Geografie", null);
//		assertEquals(b.getAktion(), Aktion.DEFAULT);
//		assertEquals(b.getArtikelElement(), Element.DEFAULT);
//		assertEquals(b.getPosition(), Position.DEFAULT);
//		assertEquals(b.getRichtung(), Richtung.DEFAULT);
//		assertTrue(b.getWert() == Wert.getDefault());
//		assertEquals("Geografie", b.getBefehlAlsString());
//	}
//
//}
