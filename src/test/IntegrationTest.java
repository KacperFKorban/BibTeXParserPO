package test;

import bibtexparser.bibliography.Bibliography;
import bibtexparser.parser.Parser;
import bibtexparser.prettifier.PrettifierVisitor;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Class containing integration tests
 */
public class IntegrationTest {

  @Test
  public void testIntegrationOfParserAndPrettifier() throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    Bibliography bibliography = Parser.parse("data/testSample.bib");

    Bibliography bibliography1 = new Bibliography();
    bibliography.getRecords().entrySet().forEach(entry -> bibliography1.addEntry(entry.getKey(), entry.getValue()));

    PrettifierVisitor prettifierVisitor = new PrettifierVisitor(entry -> true, '-', '|', '*');

    assertNotNull(bibliography.prettify(prettifierVisitor));

    assertEquals(bibliography.getRecords(), bibliography1.getRecords());
  }
}
