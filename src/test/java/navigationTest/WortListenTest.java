//package navigationTest;
//
//import static org.junit.Assert.*;
//
//import java.io.File;
//import java.util.ArrayList;
//
//import org.jsoup.nodes.Node;
//import org.junit.Test;
//
//import model.Html_Parser;
//import model.Write_Model;
//
//public class WortListenTest {
//
//	@Test
//	public void test() {
//		Html_Parser p = new Html_Parser(new File("test/navigationTest/Testdaten/Minimalbeispiel1/"));
//		ArrayList<String> normal = p.getWortliste();
//		ArrayList<Node> daten = new ArrayList<Node>();
//		for(Node n: p.getRelevantenHtmlTeil()){
//			daten.add(n);
//		}
//		ArrayList<String> rekursiv = p.getWortListeRekursiv(daten);
//		assertTrue(normal.equals(rekursiv));
//	}
//
//}
