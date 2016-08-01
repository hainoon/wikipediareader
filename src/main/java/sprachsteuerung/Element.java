/**
 * 
 */
package sprachsteuerung;

/**
 * Artikelelemente (eines Sprachbefehls)
 * 
 * @author marcel
 *
 */
public enum Element {
	SATZ, SÄTZE, SENTENCE, SENTENCES, WORT, WÖRTER, WORTS, WORD, WORDS, ARTIKEL, ARTIKELS, ARTICLE, ARTICLES, KAPITEL, KAPITELS, ABSCHNITT, SECTION, SECTIONS, CHAPTER, CHAPTERS, ABSCHNITTE, DEFAULT, ABSATZ, ABSÄTZE, ABSAETZE, PARAGRAPH, PARAGRAPHS;

	public static Element normalisiertesElement(Element e) {
		switch (e) {
		case SATZ:
		case SÄTZE:
		case SENTENCE:
		case SENTENCES:
			return SATZ;
		case WORT:
		case WÖRTER:
		case WORTS:
		case WORD:
		case WORDS:
			return WORT;
		case ABSATZ:
		case ABSAETZE:
		case ABSÄTZE:
		case PARAGRAPH:
		case PARAGRAPHS:
			return ABSATZ;
		case ARTIKEL:
		case ARTIKELS:
		case ARTICLE:
		case ARTICLES:
			return ARTIKEL;
		case KAPITEL:
		case KAPITELS:
		case SECTION:
		case SECTIONS:
		case CHAPTER:
		case CHAPTERS:
		case ABSCHNITT:
		case ABSCHNITTE:
			return KAPITEL;
		default:
			return DEFAULT;
		}
	}
}
