package seedu.multitasky.commons.events.storage;

import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.model.ReadOnlyEntryBook;

//@@author A0132788U

/**
 * Event indicating the file path for storage has changed.
 */
public class FilePathChangedEvent extends BaseEvent {

    public final ReadOnlyEntryBook data;
    private String newFilePath;

    public FilePathChangedEvent(ReadOnlyEntryBook data, String newFilePath) {
        this.data = data;
        this.newFilePath = newFilePath;
    }

    public String getNewFilePath() {
        return newFilePath;
    }

    public void setNewFilePath(String newFilePath) {
        this.newFilePath = newFilePath;
    }

    @Override
    public String toString() {
        return "File path is: " + newFilePath;
    }
}
