package db61b;

import java.util.Scanner;
import static db61b.FileOperation.*;
import java.io.File;

/** The main program for db61b.
 *  @author P. N. Hilfinger
 */
public class Main {

    /** Version designation for this program. */
    private static final String VERSION = "2.0";

    /** Starting with an empty database, read and execute commands from
     *  System.in until receiving a 'quit' ('exit') command or until
     *  reaching the end of input. */
    public static void main(String[] unused) {
        // Version statement
        System.out.printf("DB61B System.  Version %s.%n", VERSION);

        File snapshots = new File("./snapshots");
        judge_dir_exists(snapshots);

        File logs = new File("./logs");
        judge_dir_exists(logs);
        // String s="****";
        // String version_name = Trie.encrypt_sha_1(s);
        // File version = new File("./snapshots/"+version_name);
        // judge_file_exists(version);
        // Trie version_tree = new Trie();
        // version_tree.Insert(version_name);
        // System.out.println(version_tree.Find("f442327a"));

        Scanner input = new Scanner(System.in);
        // Pass input into "CommandInterpreter"
        CommandInterpreter interpreter =
            new CommandInterpreter(input, System.out);

        // Keep receiving commands till the end of the input file.
        while (true) {
            try {
                if (!interpreter.statement()) {
                    break;
                }
            } catch (DBException e) {// When the dbms throws an error, use e.getMessage() to get error message
                System.out.printf("Error: %s%n", e.getMessage());
            //    System.out.println("Please enter a ';' to continue correctly");
                interpreter.skipCommand();// Skip the command rather than shutting down the whole program
            }
        }
    }

}

