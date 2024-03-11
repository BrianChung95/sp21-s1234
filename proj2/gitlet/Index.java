package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 *  @author Brian Zhong
 */
public class Index implements Serializable {
    private HashMap<String, String> additionStagingArea;
    private HashSet<String> removalStagingArea;

    public Index() {
        additionStagingArea = new HashMap<>();
        removalStagingArea = new HashSet<>();
    }
    public static Index loadIndex() {
        if (Repository.INDEX_FILE.exists()) {
            return Utils.readObject(Repository.INDEX_FILE, Index.class);
        }
        return new Index();
    }

    public boolean isEmpty() {
        return additionStagingArea.isEmpty() && removalStagingArea.isEmpty();
    }

    public void saveIndex() {
        Utils.writeObject(Repository.INDEX_FILE, this);
    }

    public HashMap<String, String> getAdditionStagingArea() {
        return additionStagingArea;
    }

    public void setAdditionStagingArea(HashMap<String, String>additionStagingArea) {
        this.additionStagingArea = additionStagingArea;
    }

    public String setAdditionStagingAreaForFile(String fileName, String hash) {
        String oldHash = additionStagingArea.getOrDefault(fileName, null);
        this.additionStagingArea.put(fileName, hash);
        return oldHash;
    }

    public HashSet<String> getRemovalStagingArea() {
        return removalStagingArea;
    }

    public void setRemovalStagingArea(HashSet<String> removalStagingArea) {
        this.removalStagingArea = removalStagingArea;
    }

    public void addToAdditional(String fileName, String hash) {
        additionStagingArea.put(fileName, hash);
    }

    public void addToRemoval(String fileName) {
        removalStagingArea.add(fileName);
    }

    public boolean isInAdditional(String fileName) {
        return additionStagingArea.containsKey(fileName);
    }

    public boolean isInRemoval(String fileName) {
        return removalStagingArea.contains(fileName);
    }

    public String removeFromAdditionalIfExist(String fileName) {
        return additionStagingArea.remove(fileName);
    }

    public void removeFromRemovalIfExist(String fileName) {
        removalStagingArea.remove(fileName);
    }

    public void cleanStagingArea() {
        this.additionStagingArea.clear();
        this.removalStagingArea.clear();
    }

    public static void printIndex() {
        Index index = loadIndex();
        System.out.println(index.additionStagingArea);
        System.out.println(index.removalStagingArea);
    }

    public static void printStagedFiles() {
        Index index = loadIndex();
        for (String s : index.additionStagingArea.keySet()) {
            System.out.println(s);
        }
    }

    public static void printRemovedFiles() {
        Index index = loadIndex();
        for (String s : index.removalStagingArea) {
            System.out.println(s);
        }
    }


}
