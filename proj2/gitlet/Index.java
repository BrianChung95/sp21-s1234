package gitlet;

import java.util.TreeSet;

public class Index {
    private TreeSet<String> additionStagingArea;
    private TreeSet<String> removalStagingArea;

    public TreeSet<String> getAdditionStagingArea() {
        return additionStagingArea;
    }

    public void setAdditionStagingArea(TreeSet<String> additionStagingArea) {
        this.additionStagingArea = additionStagingArea;
    }

    public TreeSet<String> getRemovalStagingArea() {
        return removalStagingArea;
    }

    public void setRemovalStagingArea(TreeSet<String> removalStagingArea) {
        this.removalStagingArea = removalStagingArea;
    }

    public void addToAdditional(String fileName) {
        additionStagingArea.add(fileName);
    }

    public void addToRemoval(String fileName) {
        removalStagingArea.add(fileName);
    }

    public boolean isInAdditional(String fileName) {
        return additionStagingArea.contains(fileName);
    }

    public boolean isInRemoval(String fileName) {
        return removalStagingArea.contains(fileName);
    }
}
