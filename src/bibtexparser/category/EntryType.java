package bibtexparser.category;

import bibtexparser.exceptions.BibTeXUnsupportedPatternException;
import bibtexparser.parser.Parser;

/**
 * Enumeration determining type of a entry
 */
public enum EntryType {
  ARTICLE,
  BOOK,
  INPROCEEDINGS,
  CONFERENCE,
  BOOKLET,
  INBOOK,
  INCOLLECTION,
  MANUAL,
  MASTERSTHESIS,
  PHDTHESIS,
  TECHREPORT,
  MISC,
  UNPUBLISHED;

  /**
   * Returns enum constructor of given entry type
   *
   * @param name name of entry type
   * @return enum constructor of given entry type
   * @throws BibTeXUnsupportedPatternException if a entry type name cannot be converted
   */
  public static EntryType fromString(String name) {
    String lowerName = name.toLowerCase();
    switch(lowerName) {
      case "article":
        return ARTICLE;
      case "book":
        return BOOK;
      case "inproceedings":
        return INPROCEEDINGS;
      case "conference":
        return CONFERENCE;
      case "booklet":
        return BOOKLET;
      case "inbook":
        return INBOOK;
      case "incollection":
        return INCOLLECTION;
      case "manual":
        return MANUAL;
      case "mastersthesis":
        return MASTERSTHESIS;
      case "phdthesis":
        return PHDTHESIS;
      case "techreport":
        return TECHREPORT;
      case "misc":
        return MISC;
      case "unpublished":
        return UNPUBLISHED;
    }
    if(Parser.lineNumber != 0)
      throw new BibTeXUnsupportedPatternException(name, Parser.lineNumber);
    else
      throw new BibTeXUnsupportedPatternException(name);
  }
}
