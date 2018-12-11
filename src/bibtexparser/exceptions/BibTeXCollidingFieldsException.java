package bibtexparser.exceptions;

import bibtexparser.field.FieldType;

/**
 * Exception thrown when an entry contains colliding fields
 */
public class BibTeXCollidingFieldsException extends BibTeXException {
  private static final String msg = "Colliding fields";
  public BibTeXCollidingFieldsException() {
  }

  public BibTeXCollidingFieldsException(String s) {
    super(msg, s);
  }

  public BibTeXCollidingFieldsException(String s, int lineNumber) {
    super(msg, s, lineNumber);
  }

  public BibTeXCollidingFieldsException(FieldType type1, FieldType type2, String s, int lineNumber) {
    super(msg + " " + type1 + " " + type2, s, lineNumber);
  }
}
