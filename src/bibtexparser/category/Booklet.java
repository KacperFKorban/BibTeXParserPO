package bibtexparser.category;

import bibtexparser.field.FieldType;
import bibtexparser.field.FieldTypeContainer;
import bibtexparser.field.FieldTypeSingleton;
import bibtexparser.prettifier.PrettifierVisitor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class containing information of a single booklet
 */
public class Booklet extends Entry {
  /**
   * Class Fields
   */
  protected static EntryType categoryType = EntryType.BOOKLET;
  protected static Map<FieldType, FieldTypeContainer> required = new LinkedHashMap<>();
  protected static Map<FieldType, FieldTypeContainer> optional = new LinkedHashMap<>();
  static {
    required.put(FieldType.TITLE, new FieldTypeSingleton(FieldType.TITLE));

    optional.put(FieldType.AUTHOR, new FieldTypeSingleton(FieldType.AUTHOR));
    optional.put(FieldType.HOWPUBLISHED, new FieldTypeSingleton(FieldType.HOWPUBLISHED));
    optional.put(FieldType.ADDRESS, new FieldTypeSingleton(FieldType.ADDRESS));
    optional.put(FieldType.MONTH, new FieldTypeSingleton(FieldType.MONTH));
    optional.put(FieldType.YEAR, new FieldTypeSingleton(FieldType.YEAR));
    optional.put(FieldType.NOTE, new FieldTypeSingleton(FieldType.NOTE));
    optional.put(FieldType.KEY, new FieldTypeSingleton(FieldType.KEY));
  }

  /**
   * Constructs a booklet with given key and fields
   *
   * @param quotationKey entry key
   * @param fields entry fields
   */
  public Booklet(String quotationKey, Map<FieldType, String> fields) {
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
