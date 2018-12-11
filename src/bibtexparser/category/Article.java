package bibtexparser.category;

import bibtexparser.field.FieldType;
import bibtexparser.field.FieldTypeContainer;
import bibtexparser.field.FieldTypeSingleton;
import bibtexparser.prettifier.PrettifierVisitor;

import java.util.*;

/**
 * Class containing information of a single article
 */
public class Article extends Entry {
  /**
   * Class Fields
   */
  protected static EntryType categoryType = EntryType.ARTICLE;
  protected static Map<FieldType, FieldTypeContainer> required = new LinkedHashMap<>();
  protected static Map<FieldType, FieldTypeContainer> optional = new LinkedHashMap<>();
  static {
    required.put(FieldType.AUTHOR, new FieldTypeSingleton(FieldType.AUTHOR));
    required.put(FieldType.TITLE, new FieldTypeSingleton(FieldType.TITLE));
    required.put(FieldType.JOURNAL, new FieldTypeSingleton(FieldType.JOURNAL));
    required.put(FieldType.YEAR, new FieldTypeSingleton(FieldType.YEAR));

    optional.put(FieldType.VOLUME, new FieldTypeSingleton(FieldType.VOLUME));
    optional.put(FieldType.NUMBER, new FieldTypeSingleton(FieldType.NUMBER));
    optional.put(FieldType.PAGES, new FieldTypeSingleton(FieldType.PAGES));
    optional.put(FieldType.MONTH, new FieldTypeSingleton(FieldType.MONTH));
    optional.put(FieldType.NOTE, new FieldTypeSingleton(FieldType.NOTE));
    optional.put(FieldType.KEY, new FieldTypeSingleton(FieldType.KEY));
  }

  /**
   * Constructs an article with given key and fields
   *
   * @param quotationKey entry key
   * @param fields entry fields
   */
  public Article(String quotationKey, Map<FieldType, String> fields) {
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
