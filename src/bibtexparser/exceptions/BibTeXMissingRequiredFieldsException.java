package bibtexparser.exceptions;

/**
 * Exception thrown when an entry lacks fields of required types
 */
public class BibTeXMissingRequiredFieldsException extends BibTeXException {
  private static final String msg = "Missing required field";
  public BibTeXMissingRequiredFieldsException() {
  }

  public BibTeXMissingRequiredFieldsException(String s) {
    super(msg, s);
  }

  public BibTeXMissingRequiredFieldsException(String s, int lineNumber) {
    super(msg, s, lineNumber);
  }

  public BibTeXMissingRequiredFieldsException(String r, String s, int lineNumber) {
    super(msg + " " + r, s, lineNumber);
  }
}
