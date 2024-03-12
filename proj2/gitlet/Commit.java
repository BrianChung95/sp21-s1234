package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Brian Zhong
 */
public class Commit implements Dumpable, Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /** The timestamp of the Commit */
    private String timestamp;
    private String parent;

    private final int length;

    private HashMap<String, String> trackedFiles;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public HashMap<String, String> getTrackedFiles() {
        return trackedFiles;
    }

    public void setTrackedFiles(HashMap<String, String> trackedFiles) {
        this.trackedFiles = trackedFiles;
    }

    /**
     * Commit Constructor for init command.
     */
    public Commit() {
        this.message = "initial commit";
        this.parent = null;
        this.timestamp = Utils.formatDate(new Date(0L));
        this.trackedFiles = new HashMap<>();
        this.length = 1;
    }

    public Commit(Index index, String message) {
        Commit headCommit = RepositoryUtils.getHeadCommit();
        this.message = message;
        this.parent = RepositoryUtils.getHeadHash();
        this.timestamp = Utils.formatDate(new Date());
        this.trackedFiles = headCommit.getTrackedFiles();
        this.length = headCommit.getLength() + 1;
        HashMap<String, String> addStagingArea = index.getAdditionStagingArea();
        HashSet<String> removeStagingArea = index.getRemovalStagingArea();
        for (Map.Entry<String, String> entry : addStagingArea.entrySet()) {
            String fileName = entry.getKey();
            String fileHash = entry.getValue();
            trackedFiles.put(fileName, fileHash);
        }

        for (String fileName : removeStagingArea) {
            trackedFiles.remove(fileName);
        }
    }

    public int getLength() {
        return length;
    }

    public String getCommitId() {
        byte[] serializedCommit = serialize(this);
        return sha1(serializedCommit);
    }

    public String  persistCommit() {
        String commitHash = getCommitId();
        File commit = join(Repository.OBJ_DIR, commitHash);
        if (!commit.exists()) {
            try {
                commit.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writeObject(commit, this);
        }
        return commitHash;
    }

    public static Commit loadCommit(String hash) {
        File commitFile = join(Repository.OBJ_DIR, hash);
        if (commitFile.exists()) {
            return readObject(commitFile, Commit.class);
        }
        return null;
    }

    public String getHashForFile(String fileName) {
        return trackedFiles.getOrDefault(fileName, null);
    }

    public boolean isTracked(String fileName) {
        return trackedFiles.containsKey(fileName);
    }

    public boolean isUpToDate(String fileName) {
        if (!isTracked(fileName)) {
            return false;
        }
        File curVersion = Utils.join(Repository.CWD, fileName);
        String newHash = Utils.sha1(Utils.readContentsAsString(curVersion));
        return newHash.equals(trackedFiles.get(fileName));
    }

    @Override
    public String toString() {
        return "Date: " + this.timestamp +
                "\n" +
                this.message +
                "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Commit)) {
            return false;
        }

        Commit commit = (Commit) o;
        return this.getCommitId().equals(commit.getCommitId());
    }


    /**
     * TODO: finish up dump method
     */
    @Override
    public void dump() {

    }
}
