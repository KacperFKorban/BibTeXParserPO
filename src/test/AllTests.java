package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Class running all tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({UnitTestsSuite.class, IntegrationTest.class})
public class AllTests {
}