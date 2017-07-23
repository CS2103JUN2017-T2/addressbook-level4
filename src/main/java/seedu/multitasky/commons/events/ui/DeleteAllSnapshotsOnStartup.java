package seedu.multitasky.commons.events.ui;

import java.io.File;

// @@author A0132788U
/**
 * Event that deletes all the snapshot files created from previous program run during startup.
 */
public class DeleteAllSnapshotsOnStartup {

    private static final String FILE_PATH = "data/snapshots/entrybook";
    private String newFilePath;
    private boolean shouldExist;
    private int index = 1;

    public void deleteAllSnapshotFiles() {
        newFilePath = FILE_PATH + index + ".xml";
        File toDelete = new File(newFilePath);
        shouldExist = toDelete.exists();
        while (shouldExist) {
            newFilePath = FILE_PATH + index + ".xml";
            toDelete = new File(newFilePath);
            shouldExist = toDelete.exists();
            toDelete.delete();
            index++;
        }
    }
    // @@author

}
