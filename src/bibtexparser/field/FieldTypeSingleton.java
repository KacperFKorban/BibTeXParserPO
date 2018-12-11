package bibtexparser.field;

import java.util.Objects;
import java.util.Set;

/**
 * Class describing a field type without the possibility of collision
 */
public class FieldTypeSingleton extends FieldTypeContainer {
  private FieldType type;

  /**
   * Constructs a new container without any collisions
   *
   * @param type field type
   */
  public FieldTypeSingleton(FieldType type) {
    this.type = type;
  }

  /**
   * Returns whether given type is equal to the type contained in this container
   *
   * @param type field type
   * @return whether given type is equal to the type contained in this container
   */
  @Override
  public boolean contains(FieldType type) {
    return this.type == type;
  }

  /**
   * Returns whether type from this container is equal to given type
   *
   * @param type field type
   * @return whether type from this container is equal to given type
   */
  @Override
  public boolean keyEquals(FieldType type) {
    return type == this.type;
  }

  /**
   * Returns that there is no collisions
   *
   * @param types set of field types
   * @return false
   */
  @Override
  public boolean collides(Set<FieldType> types) {
    return false;
  }

  /**
   * Returns whether given set contains the type from this container
   *
   * @param types set of field types
   * @return whether given set contains the type from this container
   */
  @Override
  public boolean containedIn(Set<FieldType> types) {
    return types.contains(type);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if(o.getClass() != FieldTypeSingleton.class && o.getClass() != FieldTypeAlternative.class) return false;
    FieldTypeContainer that = (FieldTypeContainer) o;
    return that.keyEquals(this.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type);
  }
}
