package sprachbefehleTest;

import sprachsteuerung.Wert;
import static org.junit.Assert.*;

import org.junit.Test;


public class TestWert {

	@Test
	public void normalfall() throws Exception {
		assertEquals(Wert.getWertAusString("zwölf"), 12);
		assertEquals(Wert.getWertAusString("drei"), 3);
		assertEquals(Wert.getWertAusString("twelve"), 12);
		assertEquals(Wert.getWertAusString("three"), 3);
	}
	
	@Test 
	public void dreizehn() throws Exception{
		assertEquals(Wert.getWertAusString("dreizehn"), 13);
		assertEquals(Wert.getWertAusString("thirdteen"), 13);
	}
	
	@Test 
	public void fuenfzehn() throws Exception{
		assertEquals(Wert.getWertAusString("fünfzehn"), 15);
		assertEquals(Wert.getWertAusString("fifteen"), 15);
	}
	
	@Test 
	public void fuenfundzwanzig() throws Exception{
		assertEquals(Wert.getWertAusString("fünfundzwanzig"), 25);
		assertEquals(Wert.getWertAusString("twentyfive"), 25);
	}

}
