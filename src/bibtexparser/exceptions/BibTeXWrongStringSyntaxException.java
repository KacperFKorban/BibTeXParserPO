package bibtexparser.exceptions;

/**
 * Exception thrown when a string variable declaration has wrong syntax
 */
public class BibTeXWrongStringSyntaxException extends BibTeXException {
  private static final String msg = "Wrong string syntax";
  public BibTeXWrongStringSyntaxException() {
  }

  public BibTeXWrongStringSyntaxException(String s) {
    super(msg, s);
  }

  public BibTeXWrongStringSyntaxException(String s, int lineNumber) {
    super(msg, s, lineNumber);
  }
}
