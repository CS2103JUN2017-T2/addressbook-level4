package seedu.multitasky.commons.events.ui;

import java.io.File;

import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.storage.Storage;
import seedu.multitasky.storage.StorageManager;

/**
 * Indicates a request for App termination
 */
public class ExitAppRequestEvent extends BaseEvent {

    // @@author A0132788U
    /**
     * Deletes all the snapshot files when the program exits as they are no longer needed.
     */
    public void deleteAllSnapshotFiles(Storage storage) {
        String filePath;
        File toDelete;
        while (StorageManager.getNumSnapshots() != 0) {
            filePath = storage.getFilePathForDeletion();
            toDelete = new File(filePath);
            toDelete.delete();
            StorageManager.decrementNumSnapshots();
        }
    }
    // @@author

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
