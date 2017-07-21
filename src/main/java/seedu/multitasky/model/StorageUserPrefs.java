package seedu.multitasky.model;

//@@author A0132788U
/**
 * Represents the default values in UserPrefs object that storage has access to.
 */
public interface StorageUserPrefs {

    public String getEntryBookFilePath();

    public void setEntryBookFilePath(String entryBookFilePath);

    /**
     * The getter and setter for the snapshot file path
     */
    public static void getEntryBookSnapshotPath() {
    }

    public static void setEntryBookSnapshotPath(String entryBookSnapshotPath) {
    }

    /**
     * Methods to update the indices when files are created during mutation/deleted during exit
     */
    public static void incrementIndexByOne() {
    }

    public static void decrementIndexByOne() {
    }

    public static void getIndex() {
    }

    public static void setIndex(int index) {
    }
}
