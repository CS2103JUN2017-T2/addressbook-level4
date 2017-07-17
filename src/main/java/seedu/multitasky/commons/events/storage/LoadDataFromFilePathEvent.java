package seedu.multitasky.commons.events.storage;

import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.model.EntryBook;

//@@author A0132788U
/**
 * Loads data from file on the given filepath.
 */
public class LoadDataFromFilePathEvent extends BaseEvent {
    private EntryBook data;
    private String filepath;

    public LoadDataFromFilePathEvent(EntryBook data, String filepath) {
        this.data = data;
        this.filepath = filepath;
    }

    public EntryBook getData() {
        return data;
    }

    public void setData(EntryBook data) {
        this.data = data;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public String toString() {
        return "redo";
    }
}
