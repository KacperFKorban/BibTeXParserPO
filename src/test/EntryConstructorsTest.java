package test;

import bibtexparser.category.Book;
import bibtexparser.category.Conference;
import bibtexparser.category.Manual;
import bibtexparser.exceptions.BibTeXCollidingFieldsException;
import bibtexparser.exceptions.BibTeXMissingRequiredFieldsException;
import bibtexparser.field.FieldType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class containing entry constructors tests
 */
public class EntryConstructorsTest {

  private static Manual manual;
  private static Map<FieldType, String> manualFields;
  private static String manualQuotationKey;
  private static Manual manualMissingRequired;
  private static Map<FieldType, String> manualFieldsMissingRequired;
  private static String manualQuotationKeyMissingRequired;
  private static Conference conferenceColliding;
  private static String conferenceQuotationKeyColliding;
  private static Map<FieldType, String> conferenceFieldsCollidingFields;
  private static Map<FieldType, String> correctFields = new LinkedHashMap<>();
  private static Map<FieldType, String> bookWithCollidingFields = new LinkedHashMap<>();
  private static Map<FieldType, String> bookWithCollidingFieldsRemovedIgnored = new LinkedHashMap<>();
  private static Map<FieldType, String> bookWithoutRequiredFields = new LinkedHashMap<>();

  @BeforeClass
  public static void setUp() {
    manualFields = new LinkedHashMap<>();
    manualFields.put(FieldType.TITLE, "Tytul");
    manualFields.put(FieldType.AUTHOR, "Autor");
    manualFields.put(FieldType.EDITOR, "Edytor");

    manualQuotationKey = "quotationKey";

    manualFieldsMissingRequired = new LinkedHashMap<>();
    manualFieldsMissingRequired.put(FieldType.AUTHOR, "Autor");
    manualFieldsMissingRequired.put(FieldType.EDITOR, "Edytor");

    manualQuotationKeyMissingRequired = "quotationKeyMissingRequired";

    conferenceQuotationKeyColliding = "quotationKeyCollidingFields";

    conferenceFieldsCollidingFields = new LinkedHashMap<>();
    conferenceFieldsCollidingFields.put(FieldType.AUTHOR, "Autor");
    conferenceFieldsCollidingFields.put(FieldType.TITLE, "Tytul");
    conferenceFieldsCollidingFields.put(FieldType.BOOKTITLE, "BookTitle");
    conferenceFieldsCollidingFields.put(FieldType.YEAR, "1939");
    conferenceFieldsCollidingFields.put(FieldType.EDITOR, "Edytor");
    conferenceFieldsCollidingFields.put(FieldType.VOLUME, "I");
    conferenceFieldsCollidingFields.put(FieldType.NUMBER, "1");

    correctFields.put(FieldType.AUTHOR, "autor");
    correctFields.put(FieldType.TITLE, "tytul");
    correctFields.put(FieldType.PUBLISHER, "dziennik");
    correctFields.put(FieldType.YEAR, "2018");
    correctFields.put(FieldType.VOLUME, "2");
    correctFields.put(FieldType.KEY, "12");
    correctFields.put(FieldType.INSTITUTION, "firma");

    bookWithoutRequiredFields.put(FieldType.AUTHOR, "autor");
    bookWithoutRequiredFields.put(FieldType.TITLE, "tytul");
    bookWithoutRequiredFields.put(FieldType.JOURNAL, "dziennik");
    bookWithoutRequiredFields.put(FieldType.YEAR, "2018");
    bookWithoutRequiredFields.put(FieldType.VOLUME, "2");
    bookWithoutRequiredFields.put(FieldType.KEY, "12");

    bookWithCollidingFields.put(FieldType.AUTHOR, "autor");
    bookWithCollidingFields.put(FieldType.EDITOR, "edytor");
    bookWithCollidingFields.put(FieldType.TITLE, "tytul");
    bookWithCollidingFields.put(FieldType.PUBLISHER, "dziennik");
    bookWithCollidingFields.put(FieldType.YEAR, "2018");
    bookWithCollidingFields.put(FieldType.NOTE, "notka");
    bookWithCollidingFields.put(FieldType.MONTH, "january");
    bookWithCollidingFields.put(FieldType.EDITION, "5");
  }

  @Test
  public void bibTeXManualConstructorShouldThrowExceptionWhenCalledWithRequiredFieldsTest() {
    manual = new Manual(manualQuotationKey, manualFields);
  }

  @Test(expected = BibTeXMissingRequiredFieldsException.class)
  public void bibTeXManualConstructorShouldThrowExceptionWhenCalledWithoutRequiredFieldsTest() {
    manualMissingRequired = new Manual(manualQuotationKeyMissingRequired, manualFieldsMissingRequired);
  }

  @Test(expected = BibTeXCollidingFieldsException.class)
  public void bibTeXConferenceConstructorShouldThrowExceptionWhenCalledWithCollidingFieldsTest() {
    conferenceColliding = new Conference(conferenceQuotationKeyColliding, conferenceFieldsCollidingFields);
  }

  @Test
  public void constructingACorrectEntryShouldNotThrowAnyExceptions() {
    new Book("quotationKey", correctFields);
  }

  @Test(expected = BibTeXMissingRequiredFieldsException.class)
  public void contructingAnEntryWithoutRequiredFieldsShouldThrowAnException() {
    new Book("quotationKey", bookWithoutRequiredFields);
  }

  @Test(expected = BibTeXCollidingFieldsException.class)
  public void constructingAnEntryWithCollidingFieldsShouldThrowAnException() {
    new Book("quotationKey", bookWithCollidingFields);
  }
}
