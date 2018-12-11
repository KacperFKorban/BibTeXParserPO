package bibtexparser.category;

import bibtexparser.field.FieldType;
import bibtexparser.field.FieldTypeAlternative;
import bibtexparser.field.FieldTypeContainer;
import bibtexparser.field.FieldTypeSingleton;
import bibtexparser.prettifier.PrettifierVisitor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class containing information of a single inbook
 */
public class Inbook extends Entry {
  /**
   * Class Fields
   */
  protected static EntryType categoryType = EntryType.INBOOK;
  protected static Map<FieldType, FieldTypeContainer> required = new LinkedHashMap<>();
  protected static Map<FieldType, FieldTypeContainer> optional = new LinkedHashMap<>();
  static {
    required.put(FieldType.AUTHOR, new FieldTypeAlternative(FieldType.AUTHOR, FieldType.EDITOR));
    required.put(FieldType.EDITOR, new FieldTypeAlternative(FieldType.EDITOR, FieldType.AUTHOR));
    required.put(FieldType.TITLE, new FieldTypeSingleton(FieldType.TITLE));
    required.put(FieldType.CHAPTER, new FieldTypeAlternative(FieldType.CHAPTER, FieldType.PAGES));
    required.put(FieldType.PAGES, new FieldTypeAlternative(FieldType.PAGES, FieldType.CHAPTER));
    required.put(FieldType.PUBLISHER, new FieldTypeSingleton(FieldType.PUBLISHER));
    required.put(FieldType.YEAR, new FieldTypeSingleton(FieldType.YEAR));

    optional.put(FieldType.VOLUME, new FieldTypeAlternative(FieldType.VOLUME, FieldType.NUMBER));
    optional.put(FieldType.NUMBER, new FieldTypeAlternative(FieldType.NUMBER, FieldType.VOLUME));
    optional.put(FieldType.SERIES, new FieldTypeSingleton(FieldType.SERIES));
    optional.put(FieldType.TYPE, new FieldTypeSingleton(FieldType.TYPE));
    optional.put(FieldType.ADDRESS, new FieldTypeSingleton(FieldType.ADDRESS));
    optional.put(FieldType.EDITION, new FieldTypeSingleton(FieldType.EDITION));
    optional.put(FieldType.MONTH, new FieldTypeSingleton(FieldType.MONTH));
    optional.put(FieldType.YEAR, new FieldTypeSingleton(FieldType.YEAR));
    optional.put(FieldType.NOTE, new FieldTypeSingleton(FieldType.NOTE));
    optional.put(FieldType.KEY, new FieldTypeSingleton(FieldType.KEY));
  }

  /**
   * Constructs an inbook with given key and fields
   *
   * @param quotationKey entry key
   * @param fields entry fields
   */
  public Inbook(String quotationKey, Map<FieldType, String> fields) {
    super(quotationKey, fields);
    correctlyInitialized(required, optional, fields);
  }

  /**
   * Returns entry type
   *
   * @return entry type
   */
  @Override
  public EntryType getType() {
    return categoryType;
  }

  /**
   * Returns prettified string representation of entry
   *
   * @param prettifier visitor class creating prettified representation of entry
   * @return prettified string representation of entry
   */
  @Override
  public String accept(PrettifierVisitor prettifier) {
    return prettifier.visit(this);
  }
}
