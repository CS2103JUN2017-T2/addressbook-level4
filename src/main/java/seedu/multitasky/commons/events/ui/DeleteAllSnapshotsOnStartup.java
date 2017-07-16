package seedu.multitasky.commons.events.ui;

import java.io.File;

// @@author A0132788U
/**
 * Event that deletes all the snapshot files created from previous program run on startup.
 */
public class DeleteAllSnapshotsOnStartup {

    private static final String FILE_PATH = "data/snapshots/entrybook";
    private String newFilePath;
    private boolean ifExists;
    private int index = 1;
    private File toDelete;

    public void deleteAllSnapshotFiles() {
        newFilePath = FILE_PATH + index + ".xml";
        toDelete = new File(newFilePath);
        ifExists = toDelete.exists();
        while (ifExists == true) {
            newFilePath = FILE_PATH + index + ".xml";
            toDelete = new File(newFilePath);
            ifExists = toDelete.exists();
            toDelete.delete();
            index++;
        }
    }
    // @@author

}
