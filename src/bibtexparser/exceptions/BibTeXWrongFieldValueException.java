package bibtexparser.exceptions;

/**
 * Exception thrown when parser encounters a field value which cannot be converted into an appropriate value
 */
public class BibTeXWrongFieldValueException extends BibTeXException {
  private static final String msg = "Wrong field value";
  public BibTeXWrongFieldValueException() {
  }

  public BibTeXWrongFieldValueException(String s) {
    super(msg, s);
  }

  public BibTeXWrongFieldValueException(String s, int lineNumber) {
    super(msg, s, lineNumber);
  }
}
