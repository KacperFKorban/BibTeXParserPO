package bibtexparser.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Class containing implementation details on string variables
 */
public class StringVariable {

  /**
   * Returns default map of string variables
   *
   * @return default map of string variables
   */
  public static Map<String, String> defaultString() {
    Map<String, String> variables = new HashMap<>();

    variables.put("jan", "January");
    variables.put("feb", "February");
    variables.put("mar", "March");
    variables.put("apr", "April");
    variables.put("may", "May");
    variables.put("jun", "June");
    variables.put("jul", "July");
    variables.put("aug", "August");
    variables.put("sep", "September");
    variables.put("oct", "October");
    variables.put("nov", "November");
    variables.put("dec", "December");

    return variables;
  }
}
