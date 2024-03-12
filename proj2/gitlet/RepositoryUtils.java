package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static gitlet.Utils.*;

public class RepositoryUtils {

    public static void modifyHEADFileContentForBranch(File branchFile) {
        String headFileContent = Repository.HEAD_FILE_REF_PREFIX +
                computeRelativePath(Repository.GITLET_DIR, branchFile);
        writeContents(Repository.HEAD_FILE, headFileContent);
    }

    public static File createOrModifyBranchFile(String branchName, String commitHash) {
        File branchFile = join(Repository.HEADS_DIR, branchName);
        if (!branchFile.exists()) {
            try {
                branchFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        writeContents(branchFile, commitHash);
        return branchFile;
    }

    public static String getHeadHash() {
        String headFileContent = readContentsAsString(Repository.HEAD_FILE);
        String headHash;

        String pathToHead = headFileContent.substring(Repository.HEAD_FILE_REF_PREFIX.length());
        // Reads the file in heads folder that contains hash of the HEAD commit
        File headHashFile = join(Repository.GITLET_DIR, pathToHead);
        headHash = readContentsAsString(headHashFile);

        return headHash;
    }

    public static String getBranchHash(String branchName) {
        File branchFile = join(Repository.HEADS_DIR, branchName);
        return readContentsAsString(branchFile);
    }

    /**
     * Look for the Commit that the HEAD pointer is pointing to and return it.
     * @return A Commit object which the HEAD pointer is pointing to.
     */
    public static Commit getHeadCommit() {
        String headHash = getHeadHash();
        return getCommit(headHash);
    }

    public static Commit getCommit(String hash) {
        File commitFile = join(Repository.OBJ_DIR, hash);
        return readObject(commitFile, Commit.class);
    }

    public static Commit getBranchHead(String branchName) {
        String branchHash = getBranchHash(branchName);
        return getCommit(branchHash);
    }

    public static String getCurHead() {
        String headFileContent = readContentsAsString(Repository.HEAD_FILE);
        String curHead = headFileContent.substring(headFileContent.lastIndexOf('\\') + 1);
        return curHead;
    }

    public static List<String> getAllBranchesWithCurMarked() {
        List<String> filenames = plainFilenamesIn(Repository.HEADS_DIR);
        if (filenames == null) return null;

        // Get the name of the current head by extracting word after the last \
        String curHead = getCurHead();
        for (int i = 0; i < filenames.size(); ++i) {
            if (filenames.get(i).equals(curHead)) {
                filenames.set(i, "*" + curHead);
            }
        }
        return filenames;
    }

    public static Commit findSplitPoint(Commit commit1, Commit commit2) {
        int len1 = commit1.getLength();
        int len2 = commit2.getLength();
        if (len2 > len1) {
            return findSplitPoint(commit2, commit1);
        }
        int delta = len1 - len2;

        Commit cur1 = commit1;
        Commit cur2 = commit2;

        while (delta > 0) {
            cur1 = getCommit(cur1.getParent());
            --delta;
        }

        while (!cur1.equals(cur2)) {
            cur1 = getCommit(cur1.getParent());
            cur2 = getCommit(cur2.getParent());
            System.out.println(cur1);
            System.out.println(cur2);
        }
        return cur1;
    }

    public static void generateConflictFile(File fileInHead, File fileInGivenBranch) {
        String contentInHead = readContentsAsString(fileInHead);
        String contentInGivenBranch = readContentsAsString(fileInGivenBranch);
        String content = "<<<<<<< HEAD\n" + contentInHead + "=======\n" + contentInGivenBranch + ">>>>>>>\n";
        writeContents(fileInHead, content);
    }
}
