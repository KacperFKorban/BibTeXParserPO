package bibtexparser.prettifier;

import bibtexparser.category.Entry;
import bibtexparser.field.FieldType;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

/**
 * Visitor class meant to generate a prettified string representation of entries
 */
public class PrettifierVisitor {

  /**
   * Class variables
   */
  private Function<Entry, Boolean> predicate;
  private char frameCharH;
  private char frameCharV;
  private char dashCharacter;
  private int nameWidth = 16;
  private int widthOfFrame = 100;
  private int distanceFromFrames = 1;
  private int textWidth = widthOfFrame - nameWidth - 3;

  /**
   * Constructs a new prettifier
   *
   * @param predicate function from Entry to Boolean
   * @param frameCharH horizontal frame character
   * @param frameCharV vertical frame character
   * @param dashCharacter character used when displaying names
   */
  public PrettifierVisitor(Function<Entry, Boolean> predicate, char frameCharH, char frameCharV, char dashCharacter) {
    this.predicate = predicate;
    this.frameCharH = frameCharH;
    this.frameCharV = frameCharV;
    this.dashCharacter = dashCharacter;
  }

  /**
   * Modifies predicate using or operator with another function
   *
   * @param f function from Entry to Boolean
   */
  public void or(Function<Entry, Boolean> f) {
    Function<Entry, Boolean> p = predicate;
    this.predicate = (Entry entry) -> p.apply(entry) || f.apply(entry);
  }

  /**
   * Modifies predicate using and operator with another function
   *
   * @param f function from Entry to Boolean
   */
  public void and(Function<Entry, Boolean> f) {
    Function<Entry, Boolean> p = predicate;
    this.predicate = (Entry entry) -> p.apply(entry) && f.apply(entry);
  }

  /**
   * Returns row of horizontal frame characters
   *
   * @return row of horizontal frame characters
   */
  private String frameRow() {
    StringBuilder res = new StringBuilder();
    for(int i = 0; i < widthOfFrame; i++) {
      res.append(frameCharH);
    }
    return res.toString();
  }

  /**
   * Returns a list created by splitting a string at every n characters
   *
   * @param str string to be split
   * @param n number of characters to split
   * @return a list created by splitting a string at every n characters
   */
  private ArrayList<String> splitAtEvery(String str, int n) {
    ArrayList<String> res = new ArrayList<>();
    StringBuilder tmp = new StringBuilder();
    int i = 0;
    for(char c : str.toCharArray()) {
      tmp.append(c);
      i++;
      if(i >= n) {
        res.add(tmp.toString());
        tmp.setLength(0);
        i = 0;
      }
    }
    res.add(tmp.toString());
    return res;
  }

  /**
   * Returns a value with fixed width, completed with spaces
   *
   * @param str value
   * @param n width
   * @return a value with fixed width, completed with spaces
   */
  private String formatUpToChars(String str, int n) {
    StringBuilder res = new StringBuilder();
    for(int i = 0; i < distanceFromFrames; i++) {
      res.append(' ');
    }
    res.append(str);
    for(int i = 0; i < distanceFromFrames; i++) {
      res.append(' ');
    }
    while(res.length() < n) {
      res.append(' ');
    }
    return res.toString();
  }

  /**
   * Returns a prettified field
   *
   * @param type field type
   * @param value field value
   * @return a prettified field
   */
  private String prettifyField(FieldType type, String value) {
    StringBuilder res = new StringBuilder();
    if(type.isName()) {
      String[] arr = value.split(" and ");
      if(arr.length == 1) {
        res.append(nameAndValueInLine(type.toString(), arr[0]));
      }
      else {
        res.append(nameAndValueInLine(type.toString(), dashCharacter + " " + arr[0]));
        for(int i = 1; i < arr.length; i++) {
          res.append(nameAndValueInLine("", dashCharacter + " " + arr[i]));
        }
      }
    }
    else {
      String arr = value.replace('\n', ' ');
      ArrayList<String> tokenized = splitAtEvery(arr, textWidth - 2);
      res.append(nameAndValueInLine(type.toString(), tokenized.get(0).trim()));
      for(int i = 1; i < tokenized.size(); i++) {
        res.append(nameAndValueInLine("", tokenized.get(i).trim()));
      }
    }
    return res.toString();
  }

  /**
   * Returns a formatted row with name and a value
   *
   * @param name name
   * @param value value
   * @return a formatted row with name and a value
   */
  private String nameAndValueInLine(String name, String value) {
    StringBuilder res = new StringBuilder();
    res.append(frameCharV);
    res.append(formatUpToChars(name, nameWidth));
    res.append(frameCharV);
    res.append(formatUpToChars(value, textWidth));
    res.append(frameCharV);
    res.append('\n');
    return res.toString();
  }

  /**
   * Returns a formatted row with a value
   *
   * @param str value
   * @return a formatted row with a value
   */
  private String stringInLine(String str) {
    StringBuilder res = new StringBuilder();
    res.append(frameCharV);
    res.append(formatUpToChars(str, widthOfFrame - 2));
    res.append(frameCharV);
    return res.toString();
  }

  /**
   * Returns prettified entry
   *
   * @param record entry
   * @return prettified entry
   */
  public String visit(Entry record) {
    StringBuilder res = new StringBuilder();
    if(predicate.apply(record)) {
      res.append(frameRow());
      res.append('\n');
      res.append(stringInLine(record.getType().toString() + " ( " + record.getQuotationKey() + " )"));
      res.append('\n');
      for(Map.Entry field : record.getFields().entrySet()) {
        res.append(frameRow());
        res.append('\n');
        res.append(prettifyField((FieldType)field.getKey(), (String)field.getValue()));
      }
      res.append(frameRow());
      res.append('\n');
    }
    return res.toString();
  }

  /**
   * Returns predicate
   *
   * @return predicate
   */
  public Function<Entry, Boolean> getPredicate() {
    return predicate;
  }

  /**
   * Sets predicate
   *
   * @param predicate function from Entry to Boolean
   */
  public void setPredicate(Function<Entry, Boolean> predicate) {
    this.predicate = predicate;
  }

  /**
   * Returns horizontal frame character
   *
   * @return horizontal frame character
   */
  public char getFrameCharH() {
    return frameCharH;
  }

  /**
   * Sets horizontal frame character
   *
   * @param frameCharH horizontal frame character
   */
  public void setFrameCharH(char frameCharH) {
    this.frameCharH = frameCharH;
  }

  /**
   * Returns vertical frame character
   *
   * @return vertical frame character
   */
  public char getFrameCharV() {
    return frameCharV;
  }

  /**
   * Sets vertical frame character
   *
   * @param frameCharV vertical frame character
   */
  public void setFrameCharV(char frameCharV) {
    this.frameCharV = frameCharV;
  }

  /**
   * Returns dash character used in displaying names
   *
   * @return dash character used in displaying names
   */
  public char getDashCharacter() {
    return dashCharacter;
  }

  /**
   * Sets dash character used in displaying names
   *
   * @param dashCharacter dash character used in displaying names
   */
  public void setDashCharacter(char dashCharacter) {
    this.dashCharacter = dashCharacter;
  }
}
