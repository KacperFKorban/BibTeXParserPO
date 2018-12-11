package bibtexparser.exceptions;

/**
 * Exception thrown when program arguments syntax is incorrect
 */
public class BibTeXProgramArgumentsException extends BibTeXException {
  public BibTeXProgramArgumentsException() {
  }

  public BibTeXProgramArgumentsException(String c, String s) {
    super(c, s);
  }
}
