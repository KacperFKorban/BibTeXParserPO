package bibtexparser.parser;

import bibtexparser.bibliography.Bibliography;
import bibtexparser.category.*;
import bibtexparser.exceptions.BibTeXWrongFieldValueException;
import bibtexparser.exceptions.BibTeXWrongQuotationKeyCrossRefException;
import bibtexparser.exceptions.BibTeXWrongStringSyntaxException;
import bibtexparser.field.FieldType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class releasing api to parse BibTeX files
 */
public class Parser {

  /**
   * Map mapping entry type enums to types of their class representations
   */
  private static Map<EntryType, Class<? extends Entry>> categoryTypeMap = new HashMap<>();
  static {
    categoryTypeMap.put(EntryType.ARTICLE, Article.class);
    categoryTypeMap.put(EntryType.BOOK, Book.class);
    categoryTypeMap.put(EntryType.INPROCEEDINGS, Inproceedings.class);
    categoryTypeMap.put(EntryType.CONFERENCE, Conference.class);
    categoryTypeMap.put(EntryType.BOOKLET, Booklet.class);
    categoryTypeMap.put(EntryType.INBOOK, Inbook.class);
    categoryTypeMap.put(EntryType.INCOLLECTION, Incollection.class);
    categoryTypeMap.put(EntryType.MANUAL, Manual.class);
    categoryTypeMap.put(EntryType.MASTERSTHESIS, Mastersthesis.class);
    categoryTypeMap.put(EntryType.PHDTHESIS, Phdthesis.class);
    categoryTypeMap.put(EntryType.TECHREPORT, Techreport.class);
    categoryTypeMap.put(EntryType.MISC, Misc.class);
    categoryTypeMap.put(EntryType.UNPUBLISHED, Unpublished.class);
  }

  /**
   * Regex patterns used to extract desired data from raw input
   */
  private static final Pattern FIELD_PATTERN = Pattern.compile("\\s*([a-zA-Z]\\w*)\\s*=\\s*(\\S.*)\\s*");
  private static final Pattern NUMBER_IN_QUOTES_PATTERN = Pattern.compile("\\s*\"(\\S*)\"\\s*");
  private static final Pattern NUMBER_IN_BRACES_PATTERN = Pattern.compile("\\s*\\{(\\S*)\\}\\s*");
  private static final Pattern NUMBER_PATTERN = Pattern.compile("\\s*(\\S*)\\s*");
  private static final Pattern NAME_PATTERN_1 = Pattern.compile("\\s*(.*)\\s*\\|\\s*(.*)\\s*");
  private static final Pattern NAME_PATTERN_2 = Pattern.compile("\\s*(.*)\\s*\\|\\s*(.*)\\s*\\|\\s*(.*)\\s*");
  private static final Pattern ENTRY_NAME_PATTERN = Pattern.compile("@(\\w+)\\s*\\{");
  private static final Pattern WHITE_SPACES_PATTERN = Pattern.compile("\\s+");

  public static int lineNumber = 0;

  /**
   * Returns bibliography obtained by parsing .bib file
   *
   * @param fileName path to .bib file
   * @return bibliography obtained by parsing .bib file
   * @throws IOException if there is an error with accessing given file
   * @throws NoSuchMethodException if an entry connot be constructed
   * @throws InstantiationException if an entry connot be constructed
   * @throws IllegalAccessException if an entry connot be constructed
   * @throws InvocationTargetException if an entry connot be constructed
   */
  public static Bibliography parse(String fileName)
          throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    Bibliography bibliography = new Bibliography();

    Map<String, String> variables = StringVariable.defaultString();

    File file = new File(fileName);

    Scanner scanner = new Scanner(file);

    while(scanner.hasNextLine()) {
      lineNumber++;
      if(scanner.findInLine(ENTRY_NAME_PATTERN) != null) {
        String categoryName = scanner.match().group(1).toLowerCase();
        if (categoryName.equals("string")) {
          parseString(scanner, variables);
        } else {
          String body = parseBody(scanner);

          Entry record = parseRecord(categoryName, body, variables, bibliography);

          if(record != null && !bibliography.getRecords().containsKey(record.getQuotationKey()))
            bibliography.addEntry(record.getQuotationKey(), record);
        }
      }
      else {
        scanner.nextLine();
      }
    }
    return bibliography;
  }

  /**
   * Returns parsed entry
   *
   * @param categoryName entry type name
   * @param body raw entry body
   * @param variables accumulated string variables up to this moment
   * @param bibliography accumulated entries up to this moment
   * @return parsed entry
   * @throws NoSuchMethodException if an entry connot be constructed
   * @throws InstantiationException if an entry connot be constructed
   * @throws IllegalAccessException if an entry connot be constructed
   * @throws InvocationTargetException if an entry connot be constructed
   */
  public static Entry parseRecord(String categoryName, String body, Map<String, String> variables, Bibliography bibliography)
          throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    EntryType category;
    List<String> tokenizedBody = tokenizeBody(body);
    try {
      category = EntryType.fromString(categoryName);
    }
    catch(Exception e) {
      if(categoryName.equals("preamble") || categoryName.equals("comment")) {
        for(String s : tokenizedBody) {
          lineNumber+=countOccurrences('\n', s);
        }
        lineNumber--;
        return null;
      }
      throw e;
    }

    String quotationKey = tokenizedBody.get(0).toLowerCase();
    tokenizedBody.remove(0);

    LinkedHashMap<FieldType, String> fields = parseFields(tokenizedBody, variables, bibliography);

    Class<? extends Entry> classObject = categoryTypeMap.get(category);

    Entry record = classObject.getConstructor(String.class, Map.class).newInstance(quotationKey, fields);

    lineNumber--;
    return record;
  }

  /**
   * Returns parsed map of fields
   *
   * @param tokenizedBody tokenized body split at every comma
   * @param variables accumulated string variables up to this moment
   * @param bibliography accumulated entries up to this moment
   * @return parsed map of fields
   */
  public static LinkedHashMap<FieldType, String> parseFields(List<String> tokenizedBody, Map<String, String> variables, Bibliography bibliography) {
    LinkedHashMap<FieldType, String> res = new LinkedHashMap<>();
    int i = 0;
    int n = tokenizedBody.size();
    boolean subst = false;
    for(String s : tokenizedBody) {
      i++;
      if(i == n && s.charAt(s.length()-1) == '\n') {
        lineNumber--;
        subst = true;
      }
      lineNumber+=countOccurrences('\n', s);
      Matcher whiteSpaces = WHITE_SPACES_PATTERN.matcher(s);
      if(whiteSpaces.matches()) continue;

      s = s.replace('\n', ' ');

      Matcher matcher = FIELD_PATTERN.matcher(s);

      if(!matcher.matches())
        throw new BibTeXWrongFieldValueException(s, lineNumber);

      String fieldName = matcher.group(1);

      if(fieldName.equals("crossref")) {
        String crossrefKey = parseNormalValue(matcher.group(2), variables).toLowerCase();

        Entry entry = bibliography.getRecords().get(crossrefKey);

        if(entry == null) {
          throw new BibTeXWrongQuotationKeyCrossRefException(crossrefKey, lineNumber);
        }
        else {
          for(Map.Entry field : entry.getFields().entrySet()) {
            if(!res.containsKey(field.getKey())) {
              res.put((FieldType)field.getKey(), (String)field.getValue());
            }
          }
        }
      }
      else {
        FieldType fieldType = FieldType.fromString(fieldName);

        String fieldValueString = matcher.group(2);

        String fieldValue = parseValue(fieldType, fieldValueString, variables);

        res.put(fieldType, fieldValue);
      }
    }
    if(subst)
      lineNumber++;
    return res;
  }

  /**
   * Parses a string variable and adds it to variables
   *
   * @param scanner scanner on parsed file
   * @param variables accumulated string variables up to this moment
   */
  private static void parseString(Scanner scanner, Map<String, String> variables) {
    String body = parseBody(scanner);
    boolean alreadyAdded = false;
    String[] lines = body.split(",");
    for(String s : lines) {
      lineNumber+=countOccurrences('\n', s);
      Matcher whiteSpaces = WHITE_SPACES_PATTERN.matcher(s);
      if(whiteSpaces.matches()) continue;

      Matcher matcher = FIELD_PATTERN.matcher(s);
      if (!matcher.matches())
        throw new BibTeXWrongFieldValueException(s, lineNumber);

      String stringName = matcher.group(1);

      String valueString = parseNormalValue(matcher.group(2), variables);
      if(alreadyAdded)
        throw new BibTeXWrongStringSyntaxException(s, lineNumber);
      variables.put(stringName, valueString);
      alreadyAdded = true;
    }
    lineNumber--;
  }

  /**
   * Returns body tokenized at every comma
   *
   * @param body raw body
   * @return body tokenized at every comma
   */
  private static List<String> tokenizeBody(String body) {
    String[] tokenizedBody = body.split(",");
    return new ArrayList<>(Arrays.asList(tokenizedBody));
  }

  /**
   * Returns body of an entry as text
   *
   * @param scanner scanner on parsed file
   * @return body of an entry as text
   */
  private static String parseBody(Scanner scanner) {
    StringBuilder stringBuilder = new StringBuilder();

    int bracketsCount = 1;

    while(bracketsCount > 0 && scanner.hasNext()) {
      char nextCharacter = ' ';
      try {
        nextCharacter = scanner.findInLine(".").charAt(0);
      }
      catch (NullPointerException e) {
        stringBuilder.append("\n");
        scanner.nextLine();
        continue;
      }
      if(nextCharacter == '{') bracketsCount++;
      if(nextCharacter == '}') bracketsCount--;
      if(bracketsCount > 0) {
        stringBuilder.append(nextCharacter);
      }
    }
    return stringBuilder.toString();
  }

  /**
   * Returns parsed value
   *
   * @param type field type
   * @param rawValue raw value
   * @param variables accumulated string variables up to this moment
   * @return parsed value
   */
  public static String parseValue(FieldType type, String rawValue, Map<String, String> variables) {
    String value = "";
    if(type.isNumber()) {
      value = parseNumber(rawValue, variables);
    }
    else if(type.isName()) {
      value = parseNames(rawValue, variables);
    }
    if(value.equals("")) {
      value = parseNormalValue(rawValue, variables);
    }
    return value;
  }

  /**
   * Returns parsed value that isn't a number or a name
   *
   * @param rawValue raw value
   * @param variables accumulated string variables up to this moment
   * @return parsed value that isn't a number or a name
   */
  private static String parseNormalValue(String rawValue, Map<String, String> variables) {
    StringBuilder res = new StringBuilder();
    StringBuilder tmp = new StringBuilder();
    boolean inQuotes = false;
    boolean concat = false;
    int braces = 0;
    for(char c : rawValue.toCharArray()) {
      if(c == '"') {
        if(inQuotes && braces == 0) {
          if(res.length() == 0 || concat) {
            res.append(tmp);
            tmp.setLength(0);
            concat = false;
          }
          else
            throw new BibTeXWrongFieldValueException(rawValue, lineNumber);
        }
        else if(braces != 0) {
          tmp.append('"');
        }
        inQuotes = !inQuotes;
      }
      else if(c == '{') {
        braces++;
      }
      else if(c == '}') {
        braces--;
        if(braces < 0) {
          throw new BibTeXWrongFieldValueException(rawValue, lineNumber);
        }
        else if(braces == 0) {
          if(res.length() == 0 || concat) {
            res.append(tmp);
            tmp.setLength(0);
            concat = false;
          }
          else
            throw new BibTeXWrongFieldValueException(rawValue, lineNumber);
        }
      }
      else if(inQuotes || braces != 0) {
        tmp.append(c);
      }
      else if(c == '#') {
        concat = true;
      }
      else if(!Character.isWhitespace(c)) {
        tmp.append(c);
      }
      else if(Character.isWhitespace(c) && (res.length() == 0 || concat) && tmp.length() != 0) {
        String stringName = tmp.toString();
        tmp.setLength(0);
        String str = variables.get(stringName);
        concat = false;
        if(str != null)
          res.append(str);
        else
          throw new BibTeXWrongFieldValueException(rawValue, lineNumber);
      }
    }

    if((res.length() == 0 || concat) && tmp.length() != 0) {
      String stringName = tmp.toString();
      tmp.setLength(0);
      String str = variables.get(stringName);
      concat = false;
      if(str != null)
        res.append(str);
      else
        throw new BibTeXWrongFieldValueException(rawValue, lineNumber);
    }

    if(concat || inQuotes || tmp.length() != 0)
      throw new BibTeXWrongFieldValueException(rawValue, lineNumber);

    return res.toString();
  }

  /**
   * Returns parsed names
   *
   * @param rawValue raw value
   * @param variables accumulated string variables up to this moment
   * @return parsed names
   */
  private static String parseNames(String rawValue, Map<String, String> variables) {
    String rawValueParsed = parseNormalValue(rawValue, variables);
    String[] names = rawValueParsed.split("\\s*and\\s*");
    List<String> namesList = Arrays
            .stream(names)
            .map(name -> parseName(name, variables))
            .collect(Collectors.toList());
    return String.join(" and ", namesList);
  }

  /**
   * Returns number of occurances of a given character in a given string
   *
   * @param c character
   * @param str string
   * @return number of occurances of a given character in a given string
   */
  private static long countOccurrences(char c, String str) {
    return str
            .chars()
            .filter(x -> x == c)
            .count();
  }

  /**
   * Returns parsed name
   *
   * @param str raw value
   * @param variables accumulated string variables up to this moment
   * @return parsed name
   */
  private static String parseName(String str, Map<String, String> variables) {
    long pipes = countOccurrences('|', str);
    String res = "";

    if(pipes == 0) {
      res = str.trim();
    }
    else if(pipes == 1) {
      Matcher matcher = NAME_PATTERN_1.matcher(str);
      if(matcher.matches())
        res = matcher.group(2).trim() + ' ' + matcher.group(1).trim();
    }
    else if(pipes == 2) {
      Matcher matcher = NAME_PATTERN_2.matcher(str);
      if(matcher.matches())
        res = matcher.group(3).trim() + ' ' + matcher.group(1).trim() + ' ' + matcher.group(2).trim();
    }
    if(res.equals(""))
      throw new BibTeXWrongFieldValueException(str, lineNumber);

    return res;
  }

  /**
   * Returns parsed number
   *
   * @param rawValue raw value
   * @param variables accumulated string variables up to this moment
   * @return parsed number
   */
  private static String parseNumber(String rawValue, Map<String, String> variables) {
    Matcher matcherWithQuotes = NUMBER_IN_QUOTES_PATTERN.matcher(rawValue);
    Matcher matcherWithBraces = NUMBER_IN_BRACES_PATTERN.matcher(rawValue);
    Matcher matcher = NUMBER_PATTERN.matcher(rawValue);

    if(matcherWithBraces.matches())
      return matcherWithBraces.group(1);
    else if(matcherWithQuotes.matches())
      return matcherWithQuotes.group(1);
    else if(matcher.matches())
      return matcher.group(1);
    else if(variables.containsKey(rawValue.trim()))
      return variables.get(rawValue.trim());
    else
      throw new BibTeXWrongFieldValueException(rawValue, lineNumber);
  }

}
