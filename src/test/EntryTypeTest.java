package test;

import bibtexparser.category.EntryType;
import bibtexparser.exceptions.BibTeXUnsupportedPatternException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class containing entry type tests
 */
public class EntryTypeTest {

  @Test
  public void fromStringShouldWorkForArticleTest() {
    assertEquals(EntryType.ARTICLE, EntryType.fromString("article"));
  }

  @Test
  public void fromStringShouldWorkForBookTest() {
    assertEquals(EntryType.BOOK, EntryType.fromString("book"));
  }

  @Test
  public void fromStringShouldWorkForBookletTest() {
    assertEquals(EntryType.BOOKLET, EntryType.fromString("booklet"));
  }

  @Test(expected = BibTeXUnsupportedPatternException.class)
  public void fromStringShouldThrowExceptionWhenTryingToParseUnsupportedPatternTest() {
    EntryType.fromString("nie przejdzie");
  }
}