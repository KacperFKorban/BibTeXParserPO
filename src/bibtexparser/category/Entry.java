package bibtexparser.category;

import bibtexparser.exceptions.BibTeXCollidingFieldsException;
import bibtexparser.exceptions.BibTeXMissingRequiredFieldsException;
import bibtexparser.field.FieldType;
import bibtexparser.field.FieldTypeContainer;
import bibtexparser.parser.Parser;
import bibtexparser.prettifier.PrettifierVisitor;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class containing information of a single entry
 */
public abstract class Entry {

  /**
   * Class Fields
   */
  protected Map<FieldType, String> fields;
  protected String quotationKey;

  /**
   * Constructs an entry with given key and fields
   *
   * @param quotationKey entry key
   * @param fields entry fields
   */
  public Entry(String quotationKey, Map<FieldType, String> fields) {
    this.quotationKey = quotationKey;
    this.fields = fields;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Entry)) return false;
    Entry that = (Entry) o;
    return fields.equals(that.fields) &&
            quotationKey.equals(that.quotationKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fields, quotationKey);
  }

  /**
   * Returns entry key
   *
   * @return entry key
   */
  public String getQuotationKey() {
    return quotationKey;
  }

  /**
   * Returns entry fields
   *
   * @return entry fields
   */
  public Map<FieldType, String> getFields() {
    return fields;
  }

  /**
   * Returns entry type
   *
   * @return entry type
   */
  public abstract EntryType getType();

  /**
   * Returns prettified string representation of entry
   *
   * @param prettifier visitor class creating prettified representation of entry
   * @return prettified string representation of entry
   */
  public abstract String accept(PrettifierVisitor prettifier);

  /**
   * Returns whether an entry was correctly initialized
   *
   * @param required required field types
   * @param optional optional field types
   * @param fields entry fields
   * @return whether an entry was correctly initialized
   */
  protected static boolean correctlyInitialized(Map<FieldType, FieldTypeContainer> required, Map<FieldType, FieldTypeContainer> optional, Map<FieldType, String> fields) {
    Map<FieldType, String> res = removeIgnored(required, optional, fields);
    if(!hasRequired(required, res)) throw new BibTeXMissingRequiredFieldsException(required.keySet().stream().filter(x -> !fields.keySet().contains(x)).collect(Collectors.toSet()).toString(), fields.keySet().toString(), Parser.lineNumber);
    return !collides(required, res) && !collides(optional, res);
  }

  /**
   * Returns whether any of entry fields collide in given fields collection
   *
   * @param containers field types collection
   * @param fields entry fields
   * @return whether any of entry fields collide in given fields collection
   */
  private static boolean collides(Map<FieldType, FieldTypeContainer> containers, Map<FieldType, String> fields) {
    return containers
            .values()
            .stream()
            .anyMatch(container -> container
                    .collides(fields
                            .keySet()));
  }

  /**
   * Returns whether entry fields contain all required field types
   *
   * @param required required field types
   * @param fields entry fields
   * @return whether entry fields contain all required field types
   */
  private static boolean hasRequired(Map<FieldType, FieldTypeContainer> required, Map<FieldType, String> fields) {
    return required
            .values()
            .stream()
            .allMatch(container -> container
                    .containedIn(fields
                            .keySet()));
  }

  /**
   * Function removing all ignored fields from entry fields
   *
   * @param required required field types
   * @param optional optional field types
   * @param fields entry fields
   * @return removing all ignored fields from entry fields
   */
  private static Map<FieldType, String> removeIgnored(Map<FieldType, FieldTypeContainer> required, Map<FieldType, FieldTypeContainer> optional, Map<FieldType, String> fields) {
    fields.entrySet().removeIf(entry -> !required.keySet().contains(entry.getKey()) && !optional.keySet().contains(entry.getKey()));
    return fields;
  }
}
