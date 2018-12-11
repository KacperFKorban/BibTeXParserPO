package bibtexparser.commandline;

import bibtexparser.category.EntryType;
import bibtexparser.category.Entry;
import bibtexparser.exceptions.BibTeXProgramArgumentsException;
import bibtexparser.field.FieldType;

import java.util.function.Function;

/**
 * Class meant to parse and handle program arguments
 */
public class ProgramArgumentsHandler {

  /**
   * Prints help message
   */
  public static void printHelp() {
    StringBuilder res = new StringBuilder();
    res.append("BibTeXParser\n");
    res.append("Parameters:\n");
    res.append("\tfilePath <- path to the file meant to be parsed\n");
    res.append("Flags:\n");
    res.append("\t-c [CategoryName] <- filter entries displayed based on entryType\n");
    res.append("\t                     (if more than one name is given the condition\n");
    res.append("\t                     is interpreted as alternative)\n");
    res.append("\t-f fieldType [value] <- filter entries displayed based on entryType\n");
    res.append("\t                        (if more than one name is given the condition\n");
    res.append("\t                        is interpreted as alternative)\n");
    res.append("And logical operator is applied between conditions created by different flags\n");
    System.out.println(res.toString());
  }

  /**
   * Returns function limiting displayed entries based on program argument flags
   *
   * @param args program arguments
   * @param beginIndex index in args from which flags start
   * @return function limiting displayed entries based on program argument flags
   */
  public static Function<Entry, Boolean> parseCommandLine(String[] args, int beginIndex) {
    Function<Entry, Boolean> res = (Entry entry) -> true;
    Function<Entry, Boolean> tmp = (Entry entry) -> false;
    int noArgs = 0;
    String lastFlag = null;
    boolean byEntryType = false;
    boolean byFieldType = false;
    FieldType fieldType = null;
    for(int i = beginIndex; i < args.length; i++) {
      if(args[i].charAt(0) == '-') {
        if((byEntryType && noArgs < 1) || (byFieldType && noArgs < 2))
          throw new BibTeXProgramArgumentsException("Wrong number of arguments", lastFlag);
        if(byEntryType || byFieldType)
          res = and(res, tmp);
        tmp = (Entry entry) -> false;
        byEntryType = false;
        byFieldType = false;
        fieldType = null;
        noArgs = 0;
        lastFlag = args[i];
        if(args[i].equals("-c")) {
          byEntryType = true;
        }
        else if(args[i].equals("-f")) {
          byFieldType = true;
        }
        else {
          throw new BibTeXProgramArgumentsException("Unknown flag", lastFlag);
        }
      }
      else {
        if(byEntryType) {
          tmp = or(tmp, findByEntryType(EntryType.fromString(args[i])));
          noArgs++;
        }
        else if(fieldType != null) {
          tmp = or(tmp, findByFieldValueContains(fieldType, args[i]));
          noArgs++;
        }
        else if(byFieldType){
          fieldType = FieldType.fromString(args[i]);
          noArgs++;
        }
      }
    }
    if(byEntryType || byFieldType)
      res = and(res, tmp);
    return res;
  }

  /**
   * Returns function which for x returns f(x) or g(x)
   *
   * @param f function from Entry to Boolean
   * @param g function from Entry to Boolean
   * @return function which for x returns f(x) or g(x)
   */
  private static Function<Entry, Boolean> or(Function<Entry, Boolean> f, Function<Entry, Boolean> g) {
    return (Entry entry) -> f.apply(entry) || g.apply(entry);
  }

  /**
   * Returns function which for x returns f(x) and g(x)
   *
   * @param f function from Entry to Boolean
   * @param g function from Entry to Boolean
   * @return function which for x returns f(x) and g(x)
   */
  private static Function<Entry, Boolean> and(Function<Entry, Boolean> f, Function<Entry, Boolean> g) {
    return (Entry entry) -> f.apply(entry) && g.apply(entry);
  }

  /**
   * Returns function that checks if entry is of certain type
   *
   * @param type entry type (i.e. BOOK)
   * @return function that checks if an entry is of certain type
   */
  private static Function<Entry, Boolean> findByEntryType(EntryType type) {
    return (Entry entry) -> entry.getType() == type;
  }

  /**
   * Returns function that checks if an entry contains given field and its value contains given value
   *
   * @param fieldType field type (i.e. AUTHOR)
   * @param value value of given field
   * @return function that checks if an entry contains given field and its value contains given value
   */
  private static Function<Entry, Boolean> findByFieldValueContains(FieldType fieldType, String value) {
    return (Entry entry) -> entry.getFields().containsKey(fieldType) && entry.getFields().get(fieldType).contains(value);
  }

  /**
   * Returns function that checks if an entry contains given field and its value is equal to given value
   *
   * @param fieldType field type (i.e. AUTHOR)
   * @param value value of given field
   * @return function that checks if an entry contains given field and its value is equal to given value
   */
  private static Function<Entry, Boolean> findByFieldValueEquals(FieldType fieldType, String value) {
    return (Entry entry) -> entry.getFields().containsKey(fieldType) && entry.getFields().get(fieldType).equals(value);
  }
}
