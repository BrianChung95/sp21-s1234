package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static gitlet.Utils.*;

public class RepositoryUtils {

    public static void modifyHEADFileContentForBranch(File branchFile) {
        String headFileContent = Repository.HEAD_FILE_REF_PREFIX +
                Utils.computeRelativePath(Repository.GITLET_DIR, branchFile);
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
        File branchFile = Utils.join(Repository.HEADS_DIR, branchName);
        return Utils.readContentsAsString(branchFile);
    }

    /**
     * Look for the Commit that the HEAD pointer is pointing to and return it.
     * @return A Commit object which the HEAD pointer is pointing to.
     */
    public static Commit getHeadCommit() {
        String headHash = getHeadHash();
        File headFile = join(Repository.OBJ_DIR, headHash);
        return readObject(headFile, Commit.class);
    }

    public static Commit getCommit(String hash) {
        File commitFile = join(Repository.OBJ_DIR, hash);
        return readObject(commitFile, Commit.class);
    }

    public static String getCurHead() {
        String headFileContent = readContentsAsString(Repository.HEAD_FILE);
        String curHead = headFileContent.substring(headFileContent.lastIndexOf('\\') + 1);
        return curHead;
    }

    public static List<String> getAllBranchesWithCurMarked() {
        List<String> filenames = Utils.plainFilenamesIn(Repository.HEADS_DIR);
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
}
