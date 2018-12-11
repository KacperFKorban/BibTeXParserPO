package bibtexparser.exceptions;

/**
 * Exceptin thrown when a given crossref key cannot be found or has a wrong value
 */
public class BibTeXWrongQuotationKeyCrossRefException extends BibTeXException {
  private static final String msg = "Wrong quotation key";
  public BibTeXWrongQuotationKeyCrossRefException() {
  }

  public BibTeXWrongQuotationKeyCrossRefException(String s) {
    super(msg, s);
  }

  public BibTeXWrongQuotationKeyCrossRefException(String s, int lineNumber) {
    super(msg, s, lineNumber);
  }
}
