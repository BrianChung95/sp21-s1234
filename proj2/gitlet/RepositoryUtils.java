package gitlet;

import java.io.File;
import java.io.IOException;

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
        // If HEAD file starts with 'ref: ', then it's a reference to file in heads folder
        if (headFileContent.startsWith(Repository.HEAD_FILE_REF_PREFIX)) {
            String pathToHead = headFileContent.substring(Repository.HEAD_FILE_REF_PREFIX.length());
            // Reads the file in heads folder that contains hash of the HEAD commit
            File headHashFile = join(Repository.GITLET_DIR, pathToHead);
            headHash = readContentsAsString(headHashFile);

        } else {
            // Else it's the hash of the HEAD commit
            headHash = headFileContent;
        }
        return headHash;
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
}
