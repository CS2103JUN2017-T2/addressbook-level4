package seedu.multitasky.model;

//@@author A0132788U
/**
 * Represents the default values in UserPrefs object that Storage has access to.
 */
public interface StorageUserPrefs {

    /**
     * The getter and setter for the file path (for save and open)
     */
    public String getEntryBookFilePath();

    public void setEntryBookFilePath(String entryBookFilePath);

    /**
     * The getter and setter for the snapshot file path (for undo and redo)
     */
    public String getEntryBookSnapshotPath();

    public void setEntryBookSnapshotPath(String entryBookSnapshotPath);

}
