package bibtexparser.exceptions;

/**
 * Exception thrown when parser encounters unsupported pattern (i.e. field type)
 */
public class BibTeXUnsupportedPatternException extends BibTeXException {
  private static final String msg = "Wrong pattern";
  public BibTeXUnsupportedPatternException() {
  }

  public BibTeXUnsupportedPatternException(String s) {
    super(msg, s);
  }

  public BibTeXUnsupportedPatternException(String s, int lineNumber) {
    super(msg, s, lineNumber);
  }
}
