package bibtexparser.field;

import bibtexparser.exceptions.BibTeXCollidingFieldsException;
import bibtexparser.parser.Parser;

import java.util.Objects;
import java.util.Set;

/**
 * Class describing a possible collision in field types
 */
public class FieldTypeAlternative extends FieldTypeContainer {
  private FieldType type1;
  private FieldType type2;

  /**
   * Constructs a new container with a possible collision of two field types
   *
   * @param type1 field type
   * @param type2 field type
   */
  public FieldTypeAlternative(FieldType type1, FieldType type2) {
    this.type1 = type1;
    this.type2 = type2;
  }

  /**
   * Returns whether given type equals to any of contained types
   *
   * @param type field type
   * @return whether given type equals to any of contained types
   */
  @Override
  public boolean contains(FieldType type) {
    return type == this.type1 || type == this.type2;
  }

  /**
   * Returns whether first type from this container is equal to given type
   *
   * @param type field type
   * @return whether first type from this container is equal to given type
   */
  @Override
  public boolean keyEquals(FieldType type) {
    return type == this.type1;
  }

  /**
   * Returns false if given set contains maximum one of types from this container
   *
   * @param types set of field types
   * @return false if given set contains maximum one of types from this container
   * @throws BibTeXCollidingFieldsException if a given set contains both types from this container
   */
  @Override
  public boolean collides(Set<FieldType> types) {
    if(types.contains(type1) && types.contains(type2))
      throw new BibTeXCollidingFieldsException(type1, type2, types.toString(), Parser.lineNumber);
    return false;
  }

  /**
   * Returns whether given set contains at least one of types from this container
   *
   * @param types set of field types
   * @return whether given set contains at least one of types from this container
   */
  @Override
  public boolean containedIn(Set<FieldType> types) {
    return types.contains(type1) || types.contains(type2);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if(o.getClass() != FieldTypeSingleton.class && o.getClass() != FieldTypeAlternative.class) return false;
    FieldTypeContainer that = (FieldTypeContainer) o;
    return that.keyEquals(this.type1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type1);
  }
}

