package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static gitlet.Utils.*;
import static gitlet.RepositoryUtils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    public static final String HEAD_FILE_REF_PREFIX = "ref: ";

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** The objects directory. */
    public static final File OBJ_DIR = join(GITLET_DIR, "objects");

    /** HEAD file storing the commit that HEAD pointer is pointing at */
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD");

    /** The refs directory */
    public static final File REFS_DIR = join(GITLET_DIR, "refs");

    /** The heads directory */
    public static final File HEADS_DIR = join(REFS_DIR, "heads");

    /** The index file representing the staging area */
    public static final File INDEX_FILE = join(GITLET_DIR, "index");

    /**
     * Set up directories for persistence.
     */
    public static void setuPersistence() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        OBJ_DIR.mkdirs();
        HEADS_DIR.mkdirs();

    }

    /**
     * Set up directories for persistence, create the initial commit and a master
     * branch that points to initial commit. Also have the HEAD pointer pointing
     * to initial commit.
     */
    public static void init() {
        // Create .gitlet dir and other dirs inside
        setuPersistence();

        // Create the initial commit
        Commit initialCommit = new Commit();
        // Store the initial commit to the objects folder
        String initCommitSha1 = initialCommit.persistCommit( );
        // Create master branch that points to initial commit
        File branchFile = createOrModifyBranchFile("master", initCommitSha1);
        // Set HEAD pointer to initial commit
        modifyHEADFileContentForBranch(branchFile);
    }

    /**
     * Perform add command. Add a file to staging area.
     * @param fileName The name of the file to be added.
     */
    public static void add(String fileName) {
        File fileToAdd = join(CWD, fileName);
        if (!fileToAdd.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        String hash = sha1(readContents(fileToAdd));
        Index stagingArea = Index.loadIndex();
        Commit headCommit = getHeadCommit();
        String hashInCommit = headCommit.getHashForFile(fileName);
        if (hash.equals(hashInCommit)) {
            // If the current working version of the file is identical to the version in the current commit,
            // do not stage it to be added, and remove it from the staging area if it is already there
            stagingArea.removeFromAdditionalIfExist(fileName);
        } else {
            // Adds a copy of the files to staging area, overwrites the previous entry with new content
            stagingArea.addToAdditional(fileName, hash);
            File newfile = join(OBJ_DIR, hash);
            Utils.writeContents(newfile, readContents(fileToAdd));
        }
        // The file will no longer be staged for removal, if it was at the time of the command.
        stagingArea.removeFromRemovalIfExist(fileName);
        // Persist index
        stagingArea.saveIndex();
    }

    /**
     * Create a new snapshot (commit) based on the parent commit the staging area (both Addition and Removal)
     * @param message commit message describing the changes of the files
     */
    public static void commit(String message) {
        Index index = Index.loadIndex();
        if (index.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        // Create a new commit with staging area and message
        Commit newCommit = new Commit(index, message);
        String newSha = newCommit.persistCommit();
        createOrModifyBranchFile("master", newSha);
        // The staging area is cleared after a commit.
        index.cleanStagingArea();
        index.saveIndex();
    }

    /**
     * Perform git remove.
     * 1: Remove the file if it's currently staged for addition.
     * 2: Stage it for removal, remove it from working dir if it's tracked by the current commit and remove the file.
     * @param fileName The name of the file to be removed
     */
    public static void remove(String fileName) {
        Index index = Index.loadIndex();
        Commit curCommit = RepositoryUtils.getHeadCommit();

        // If the file is neither staged nor tracked by the head commit
        if (!index.isInAdditional(fileName) && !curCommit.isTracked(fileName)) {
            System.out.println("No reason to remove the file");
        }

        // Unstage the file if it is currently staged for addition.
        index.removeFromAdditionalIfExist(fileName);

        if (curCommit.isTracked(fileName)) {
            // If the file is tracked in the current commit, stage it for removal
            index.addToRemoval(fileName);
            // Remove the file from the working directory
            File fileToDelete = Utils.join(CWD, fileName);
            Utils.restrictedDelete(fileToDelete);
        }

        index.saveIndex();
    }

    /**
     * Log the commit history.
     */
    public static void log() {
        Commit cur = RepositoryUtils.getHeadCommit();
        String curHash = RepositoryUtils.getHeadHash();
        String parentHash = cur.getParent();
        while (curHash != null) {
            System.out.println("===");
            System.out.println("commit " + curHash);
            System.out.println(cur);
            curHash = parentHash;
            if (parentHash != null) {
                cur = RepositoryUtils.getCommit(parentHash);
                parentHash = cur.getParent();
            }
        }
    }

    /**
     * Prints information about all commits ever made. The order of the commits does not matter here.
     */
    public static void globalLog() {
        List<String> filenames = Utils.plainFilenamesIn(OBJ_DIR);
        if (filenames == null) return;

        for (String fn : filenames) {
            try {
                File commitFile = Utils.join(OBJ_DIR, fn);
                Commit commit = Utils.readObject(commitFile, Commit.class);
                System.out.println("===");
                System.out.println("commit " + fn);
                System.out.println(commit);
            } catch (IllegalArgumentException e) {
            }
        }
    }

    /**
     *  Prints out the ids of all commits that have the given commit message, one per line.
     * @param message The commit message we are searching for.
     */
    public static void find(String message) {
        if (message.isEmpty()) return;

        List<String> filenames = Utils.plainFilenamesIn(OBJ_DIR);
        if (filenames == null) return;
        for (String fn : filenames) {
            try {
                File commitFile = Utils.join(OBJ_DIR, fn);
                Commit commit = Utils.readObject(commitFile, Commit.class);
                if (message.equals(commit.getMessage())){
                    System.out.println(fn);
                }
            } catch (IllegalArgumentException e) {
            }
        }
    }

    /**
     * Displays current status of the repository.
     * TODO: Entries should be listed in lexicographic order
     * TODO: Modifications Not Staged For Commit
     * TODO: Untracked Files
     */
    public static void status() {
        List<String> branches = RepositoryUtils.getAllBranchesWithCurMarked();
        System.out.println("=== Branches ===");
        for (String branch : branches) {
            System.out.println(branch);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        Index.printStagedFiles();
        System.out.println();

        System.out.println("=== Removed Files ===");
        Index.printRemovedFiles();
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println("=== Untracked Files ===");
    }
}
