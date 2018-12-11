package bibtexparser.exceptions;

/**
 * Main BibTeX exception
 */
public class BibTeXException extends RuntimeException {
  public BibTeXException() {
  }

  public BibTeXException(String c, String s) {
    super(c + " in: " + s);
  }

  public BibTeXException(String c, String s, int lineNumber) {
    super(c + " in line number " + lineNumber + ", namely: " + s);
  }
}
