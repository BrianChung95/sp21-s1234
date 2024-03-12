package gitlet;

public class MergeCommit extends Commit {
    private String mergedParent;

    public MergeCommit(Index index, String mergedParent, String curBranch, String mergedBranch) {
        super(index, null);
        String message = "Merged " + mergedBranch + " into " + curBranch + ".";
        this.setMessage(message);
        this.mergedParent = mergedParent.substring(0, 7);
    }

    @Override
    public String toString() {
        return  "Merge: " + this.getParent().substring(0, 7) + " " + mergedParent.substring(0, 7) +
                "Date: " + this.getTimestamp() +
                "\n" +
                this.getMessage() +
                "\n";
    }
}
