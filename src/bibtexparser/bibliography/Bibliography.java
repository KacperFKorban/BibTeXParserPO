package bibtexparser.bibliography;

import bibtexparser.category.Entry;
import bibtexparser.prettifier.PrettifierVisitor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Data container for BibTeX bibliography, contains BibTeX entries
 */
public class Bibliography {
  private Map<String, Entry> records = new LinkedHashMap<>();

  /**
   * Returns map of all entries in this bibliography
   *
   * @return map of all entries in this bibliography
   */
  public Map<String, Entry> getRecords() {
    return records;
  }

  /**
   * Adds entry to this bibliography
   *
   * @param key entry quotation key
   * @param entry BibTeX entry to be added
   */
  public void addEntry(String key, Entry entry) {
    records.put(key, entry);
  }

  /**
   * Returns prettified bibliography
   *
   * @param prettifier prettifier visitor creating prettified String form of entry
   * @return prettified bibliography
   */
  public String prettify(PrettifierVisitor prettifier) {
    StringBuilder res = new StringBuilder();
    for(Entry entry : records.values()) {
      res.append(entry.accept(prettifier));
    }
    return res.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Bibliography)) return false;
    Bibliography that = (Bibliography) o;
    return records.equals(that.records);
  }

  @Override
  public int hashCode() {
    return Objects.hash(records);
  }
}
