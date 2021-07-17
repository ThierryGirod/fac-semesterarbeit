import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import context.Context;
import interpreter.Interpreter;
import parser.Parser;
import parser.parsetree.ParsetreeItem;
import runtime.Program;
import scanner.Scanner;

/**
 * Startpunkt für die Applikation
 *
 */
public class App {

    public static void main(String... args) throws Exception {
        
        final String filepath;
        boolean verbose = true; // Visualize scanner and parsetree
        InputStream input;

        if(args.length == 0) {
            filepath  = "sample.txt";
            input = App.class.getClassLoader().getResourceAsStream(filepath);
        } else {
            if(args.length == 2){
                filepath  = args[0];
                verbose = Boolean.parseBoolean(args[1]);
            } else {
                filepath  = args[0];
            }
            File file = new File(filepath);
            input = new FileInputStream(file);
        }

        final var reader = new InputStreamReader(input);

        final var scanner = new Scanner(reader, verbose);

        final var parser = new Parser(scanner);
        
        final var root = (Program) parser.parse().value;

        if(verbose) {
            // Visualise the parsetree
            ParsetreeItem.visualizeParsetree(root.getParsetree(0));
        }

        final var interpreter = new Interpreter();
        final var context = new Context();
        root.accept(interpreter, context);
        
        
    }
}
