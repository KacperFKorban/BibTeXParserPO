package bibtexparser.field;

import bibtexparser.exceptions.BibTeXUnsupportedPatternException;
import bibtexparser.parser.Parser;

/**
 * Enumeration determining type of a field
 */
public enum FieldType {
  AUTHOR,
  TITLE,
  JOURNAL,
  YEAR,
  VOLUME,
  NUMBER,
  PAGES,
  MONTH,
  NOTE,
  KEY,
  PUBLISHER,
  SERIES,
  ADDRESS,
  EDITION,
  BOOKTITLE,
  EDITOR,
  ORGANIZATION,
  HOWPUBLISHED,
  CHAPTER,
  TYPE,
  SCHOOL,
  INSTITUTION;

  /**
   * Returns whether a field is a name
   *
   * @return whether a field is a name
   */
  public boolean isName() {
    switch(this) {
      case AUTHOR:
        return true;
      case EDITOR:
        return true;
      default:
        return false;
    }
  }

  /**
   * Returns whether a field is a number
   *
   * @return whether a field is a number
   */
  public boolean isNumber() {
    switch(this) {
      case YEAR:
        return true;
      case VOLUME:
        return true;
      case NUMBER:
        return true;
      case CHAPTER:
        return true;
      default:
        return false;
    }
  }

  /**
   * Returns enum constructor of given field type
   *
   * @param name name of field type
   * @return enum constructor of given field type
   * @throws BibTeXUnsupportedPatternException if a field type name cannot be converted
   */
  public static FieldType fromString(String name) {
    String lowerName = name.toLowerCase();
    switch(lowerName) {
      case("author"):
        return AUTHOR;
      case("title"):
        return TITLE;
      case("journal"):
        return JOURNAL;
      case("year"):
        return YEAR;
      case("volume"):
        return VOLUME;
      case("number"):
        return NUMBER;
      case("pages"):
        return PAGES;
      case("month"):
        return MONTH;
      case("note"):
        return NOTE;
      case("key"):
        return KEY;
      case("publisher"):
        return PUBLISHER;
      case("series"):
        return SERIES;
      case("address"):
        return ADDRESS;
      case("edition"):
        return EDITION;
      case("booktitle"):
        return BOOKTITLE;
      case("editor"):
        return EDITOR;
      case("organization"):
        return ORGANIZATION;
      case("howpublished"):
        return HOWPUBLISHED;
      case("chapter"):
        return CHAPTER;
      case("type"):
        return TYPE;
      case("school"):
        return SCHOOL;
      case("institution"):
        return INSTITUTION;
    }
    if(Parser.lineNumber != 0)
      throw new BibTeXUnsupportedPatternException(name, Parser.lineNumber);
    throw new BibTeXUnsupportedPatternException(name);

  }
}
