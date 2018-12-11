package test;

import bibtexparser.bibliography.Bibliography;
import bibtexparser.category.Article;
import bibtexparser.category.Book;
import bibtexparser.category.Booklet;
import bibtexparser.category.Manual;
import bibtexparser.field.FieldType;
import bibtexparser.parser.StringVariable;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static bibtexparser.parser.Parser.*;
import static org.junit.Assert.*;

/**
 * Class containing parser tests
 */
public class ParserTest {

  private static List<String> tokenizedFields = new ArrayList<>();
  private static LinkedHashMap<FieldType, String> fields = new LinkedHashMap<>();
  private static Map<String, String> variables = StringVariable.defaultString();
  private static Manual manual;
  private static Map<FieldType, String> manualFields = new LinkedHashMap<>();
  private static String manualBody;
  private static Bibliography bibliography = new Bibliography();

  @BeforeClass
  public static void setUp() {
    tokenizedFields.add("author = \"Autor\"");
    tokenizedFields.add("title=\"Tytul\"");

    fields.put(FieldType.AUTHOR, "Autor");
    fields.put(FieldType.TITLE, "Tytul");

    manualFields.put(FieldType.TITLE, "Tytul");
    manualFields.put(FieldType.AUTHOR, "Autor");
    manualFields.put(FieldType.EDITOR, "Edytor");

    manual = new Manual("quotationKey", manualFields);

    manualBody = "quotationKey,\ntitle=\"Tytul\",\nauthor = \"Autor\",\neditor=\"Edytor\"";

    variables.put("auth", "Marcin Kowalski");

    Map<FieldType, String> fields1 = new LinkedHashMap<>();
    fields1.put(FieldType.AUTHOR, "I.P. Freely");
    fields1.put(FieldType.TITLE, "A small paper");
    fields1.put(FieldType.JOURNAL, "The journal of small papers");
    fields1.put(FieldType.YEAR, "1997");
    fields1.put(FieldType.VOLUME, "-1");
    fields1.put(FieldType.NOTE, "to appear");
    Map<FieldType, String> fields2 = new LinkedHashMap<>();
    fields2.put(FieldType.TITLE, "Tytul bookletu");
    fields2.put(FieldType.AUTHOR, "John Terry and Marek Grechuta");
    Map<FieldType, String> fields3 = new LinkedHashMap<>();
    fields3.put(FieldType.AUTHOR, "Garry Cahill and John Terry");
    fields3.put(FieldType.TITLE, "Chelsea");
    fields3.put(FieldType.PUBLISHER, "Chelsea inc");
    fields3.put(FieldType.YEAR, "2016");
    fields3.put(FieldType.NOTE, "Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident sunt in culpa qui officia deserunt mollit anim id est laborum.");
    Map<FieldType, String> fields4 = new LinkedHashMap<>();
    fields4.put(FieldType.AUTHOR, "Garry Cahill and John Terry");
    fields4.put(FieldType.TITLE, "Aston Villa");
    fields4.put(FieldType.PUBLISHER, "Chelsea inc");
    fields4.put(FieldType.YEAR, "2017");
    fields4.put(FieldType.NOTE, "notka");

    bibliography.addEntry("small", new Article("small", fields1));
    bibliography.addEntry("boklet", new Booklet("boklet", fields2));
    bibliography.addEntry("ksiazka", new Book("ksiazka", fields3));
    bibliography.addEntry("ksiega", new Book("ksiega", fields4));
  }

  @Test
  public void parseTest() throws Exception {
    assertEquals(bibliography, parse("data/testSample.bib"));
  }

  @Test
  public void parseFieldsTest() {
    assertEquals(fields, parseFields(tokenizedFields, variables, bibliography));
  }

  @Test
  public void parseRecordTest() throws Exception {
    assertEquals(manual.getFields(), parseRecord("manual", manualBody, variables, bibliography).getFields());
  }

  @Test
  public void parseValueShouldWorkForNumbersTest() {
    assertEquals("123", parseValue(FieldType.VOLUME, "123", variables));
  }

  @Test
  public void parseValueShouldWorkForNumbersWithQuotesTest() {
    assertEquals("123", parseValue(FieldType.VOLUME, "\"123\"", variables));
  }

  @Test
  public void parseValueShouldWorkForNameInDirectFormTest() {
    assertEquals("Mariusz Kowalski", parseValue(FieldType.AUTHOR, "\"Mariusz Kowalski\"", variables));
  }

  @Test
  public void parseValueShouldWorkForNameWithPipeTest() {
    assertEquals("Mariusz Kowalski", parseValue(FieldType.AUTHOR, "\"Kowalski | Mariusz\"", variables));
  }

  @Test
  public void parseValueShouldWorkForNameWithPipeWithVonTest() {
    assertEquals("Mariusz von Kowalski", parseValue(FieldType.AUTHOR, "\"von Kowalski | Mariusz\"", variables));
  }

  @Test
  public void parseValueShouldWorkForNameWithPipesTest() {
    assertEquals("Mariusz von Kowalski Jr", parseValue(FieldType.AUTHOR, "\"von Kowalski | Jr | Mariusz\"", variables));
  }

  @Test
  public void parseValueShouldWorkForNamesTest() {
    assertEquals("Mariusz Kowalski and Stanisław Polak", parseValue(FieldType.AUTHOR, "\"Kowalski | Mariusz and Stanisław Polak\"", variables));
  }

  @Test
  public void parseValueShouldWorkForTitlesTest() {
    assertEquals("Ksiazka Marcin Kowalski", parseValue(FieldType.TITLE, "\"Ksiazka \" # auth", variables));
    assertEquals("Marcin Kowalski", parseValue(FieldType.TITLE, "auth", variables));
    assertEquals("Ksiazka # auth Marcin Kowalski", parseValue(FieldType.TITLE, "\"Ksiazka # auth \" # auth", variables));
    assertEquals("slowo1 slowo2", parseValue(FieldType.TITLE, "\"slowo1 \" # \"slowo2\" ", variables));
    assertEquals("Ksiazka Marcin Kowalski ale fajna", parseValue(FieldType.TITLE, "\"Ksiazka \" # auth # \" ale fajna\"", variables));
    assertEquals("Ksiazka Marcin Kowalski ale \"fajna\"", parseValue(FieldType.TITLE, "\"Ksiazka \" # auth # { ale \"fajna\"}", variables));
    assertEquals("Ksiazka Marcin Kowalski ale fajna", parseValue(FieldType.TITLE, "\"Ksiazka \" # auth # { ale fajna}", variables));
    assertEquals("Ksiazka Marcin Kowalski ale fajna", parseValue(FieldType.TITLE, "{Ksiazka } # auth # { ale fajna}", variables));
  }
}