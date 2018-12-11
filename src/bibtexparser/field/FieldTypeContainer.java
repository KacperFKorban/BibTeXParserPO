package bibtexparser.field;

import bibtexparser.exceptions.BibTeXCollidingFieldsException;

import java.util.Set;

/**
 * Class meant to deal with colliding field types in certain entry types
 */
public abstract class FieldTypeContainer {

  /**
   * Returns whether a type is contained in this container
   *
   * @param type field type
   * @return whether a type is contained in this container
   */
  public abstract boolean contains(FieldType type);

  /**
   * Returns whether first type from this container is equal to given type
   *
   * @param type field type
   * @return whether first type from this container is equal to given type
   */
  public abstract boolean keyEquals(FieldType type);

  /**
   * Returns false if given set contains maximum one of types from this container
   *
   * @param types set of field types
   * @return false if given set contains maximum one of types contained in this container
   * @throws BibTeXCollidingFieldsException if a given set contains two different types from this container
   */
  public abstract boolean collides(Set<FieldType> types);

  /**
   * Returns whether given set contains at least one of types from this container
   *
   * @param types set of field types
   * @return whether given set contains at least one of types from this container
   */
  public abstract boolean containedIn(Set<FieldType> types);
}
