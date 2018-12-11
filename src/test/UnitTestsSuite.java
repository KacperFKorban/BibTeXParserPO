package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Class running all unit tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({EntryConstructorsTest.class, EntryTypeTest.class, FieldTypeTest.class, ParserTest.class})
public class UnitTestsSuite {
}
