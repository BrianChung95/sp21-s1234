package gitlet;

import java.io.File;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Brian Zhong
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        // We can check if a repo is initialized or not by checking if there's a .gitlet folder
        File gitlet = Utils.join(Repository.CWD, ".gitlet");
        boolean isInitialized = gitlet.exists();
        String firstArg = args[0];

        // We cannot perform commands other than init without initializing the repo
        if (!isInitialized && !firstArg.equals("init")) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length == 1 || args[1].isEmpty()) {
                    System.out.println("Please enter a commit message.");
                    return;
                }

                Repository.commit(args[1]);
                break;
            case "rm":
                Repository.remove(args[1]);
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.globalLog();
                break;
            case "find":
                Repository.find(args[1]);
                break;
            case "status":
                Repository.status();
                break;
            case "checkout":
                if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                } else if (args.length == 3 && args[1].equals("--")) {
                    Repository.checkoutFile(null, args[2]);
                } else if (args.length == 4 && args[2].equals("--")) {
                    Repository.checkoutFile(args[1], args[3]);
                }
                break;
            case "reset":
                Repository.reset(args[1]);
                break;
            case "branch":
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                Repository.removeBranch(args[1]);
                break;
            case "merge":
                Repository.merge(args[1]);
                break;
            case "prCommon":
                Commit commit1 = RepositoryUtils.getHeadCommit();
                Commit commit2 = RepositoryUtils.getBranchHead(args[1]);
                Commit commit3 = RepositoryUtils.findSplitPoint(commit1, commit2);
                System.out.println("Parent: " + commit3.getParent());
                System.out.println("Commit time: " + commit3.getTimestamp());
                System.out.println("Tracked files: " + commit3.getTrackedFiles());
                System.out.println("Message: " + commit3.getMessage());
                break;
            case "prIndex":
                Index.printIndex();
                break;
            case "prCommit":
                Commit commit = RepositoryUtils.getHeadCommit();
                System.out.println("Parent: " + commit.getParent());
                System.out.println("Commit time: " + commit.getTimestamp());
                System.out.println("Tracked files: " + commit.getTrackedFiles());
                System.out.println("Message: " + commit.getMessage());
                break;
            default:
                System.out.println("No command with that name exists.");
        }
    }
}
