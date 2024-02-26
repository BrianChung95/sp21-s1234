package gitlet;

import java.io.File;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            return;
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                    return;
                }

                Repository.commit(args[1]);
                break;
            // TODO: FILL THE REST IN
        }
    }
}
