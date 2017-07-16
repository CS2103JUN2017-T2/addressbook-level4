package seedu.multitasky.commons.events.storage;

import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.model.EntryBook;

//@@author A0132788U
/**
 * Indicates the EntryBook in the model has changed due to Redo action.
 */
public class EntryBookToRedoEvent extends BaseEvent {

    private EntryBook data;
    private String message;

    public EntryBookToRedoEvent(EntryBook data, String message) {
        this.data = data;
        this.message = message;
    }

    public EntryBook getData() {
        return data;
    }

    public void setData(EntryBook data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "redo";
    }
}
