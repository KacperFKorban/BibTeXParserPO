package bibtexparser;

import bibtexparser.bibliography.Bibliography;
import bibtexparser.category.Entry;
import bibtexparser.commandline.ProgramArgumentsHandler;
import bibtexparser.parser.Parser;
import bibtexparser.prettifier.PrettifierVisitor;

/**
 * Class containing utility functions
 */
public class Utils {

  /**
   * Function meant to handle program arguments
   *
   * @param args program arguments
   */
  public static void handleInput(String[] args) {
    if(args.length == 0) {
      ProgramArgumentsHandler.printHelp();
    }
    else {
      String fileName = args[0];
      Bibliography bibliography;
      try {
        bibliography = Parser.parse(fileName);

        if(args.length == 1) {
          PrettifierVisitor prettifier = new PrettifierVisitor((Entry entry) -> true, '-', '|', '*');

          System.out.println(bibliography.prettify(prettifier));
        }
        else {
          PrettifierVisitor prettifier = new PrettifierVisitor(ProgramArgumentsHandler.parseCommandLine(args, 1), '-', '|', '*');

          System.out.println(bibliography.prettify(prettifier));
        }
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
  }
}
