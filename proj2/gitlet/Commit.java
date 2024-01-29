package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
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
    private Date timestamp;
    private String parent;

    private HashMap<String, String> trackedFiles;

    public enum CommitFlag {
        ADDITION,
        REMOVAL
    }

    /**
     * Commit Constructor for init command.
     */
    public Commit() {
        this.message = "initial commit";
        this.parent = null;
        this.timestamp = new Date(0L); // TODO: verify this time
        this.trackedFiles = new HashMap<>();
    }

    public Commit(String message, String parent, HashMap<String, String> stagedFiles, CommitFlag flag) {
        this.message = message;
        this.parent = parent;
        this.timestamp = new Date();
        // TODO: Finish up this constructor. Or think of a better design
        switch (flag) {
            case ADDITION:
                break;
            case REMOVAL:
                break;
            default:
                break;
        }
    }

    public String  persistCommit() {
        byte[] serializedCommit = serialize(this);
        String commitHash = sha1(serializedCommit);
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

    /**
     * TODO: finish up dump method
     */
    @Override
    public void dump() {

    }
}
