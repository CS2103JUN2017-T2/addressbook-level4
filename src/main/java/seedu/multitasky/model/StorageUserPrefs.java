package seedu.multitasky.model;

//@@author A0132788U
/**
 * Represents the default values in UserPrefs object that storage has access to.
 * Extends LogicUserPrefs to maintain DIP for Logic.
 */
public interface StorageUserPrefs extends LogicUserPrefs {

    public String getEntryBookFilePath();

    public void setEntryBookFilePath(String entryBookFilePath);

    /**
     * The getter and setter for the snapshot file path
     */
    public static String getEntryBookSnapshotPath() {
        return UserPrefs.getEntryBookSnapshotPath();
    }

    public static void setEntryBookSnapshotPath(String entryBookSnapshotPath) {
        UserPrefs.setEntryBookSnapshotPath(entryBookSnapshotPath);
    }

    /**
     * Methods to update the indices when files are created during mutation/deleted during exit
     */
    public static void incrementIndexByOne() {
        UserPrefs.incrementIndexByOne();
        ;
    }

    public static void decrementIndexByOne() {
        UserPrefs.decrementIndexByOne();
        ;
    }

    public static int getIndex() {
        return UserPrefs.getIndex();
    }

    public static void setIndex(int index) {
        UserPrefs.setIndex(index);
    }
}
