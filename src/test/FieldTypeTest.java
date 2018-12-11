package test;

import bibtexparser.exceptions.BibTeXUnsupportedPatternException;
import bibtexparser.field.FieldType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class containing field type tests
 */
public class FieldTypeTest {

  @Test
  public void fromStringShouldWorkForAuthorTest() {
    assertEquals(FieldType.AUTHOR, FieldType.fromString("author"));
  }

  @Test
  public void fromStringShouldWorkForTitleTest() {
    assertEquals(FieldType.TITLE, FieldType.fromString("title"));
  }

  @Test
  public void fromStringShouldWorkForNoteTest() {
    assertEquals(FieldType.NOTE, FieldType.fromString("note"));
  }

  @Test(expected = BibTeXUnsupportedPatternException.class)
  public void fromStringShouldThrowExceptionWhenTryingToParseUnsupportedPatternTest() {
    FieldType.fromString("nie przejdzie");
  }
}