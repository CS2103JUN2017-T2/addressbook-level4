package seedu.multitasky.commons.events.model;

import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.model.EntryBook;

//@@author A0132788U
/** Indicates the EntryBook in the model has changed */
public class EntryBookToUndoEvent extends BaseEvent {

    private EntryBook data;

    public EntryBookToUndoEvent(EntryBook data) {
        this.data = data;
    }

    public EntryBook getData() {
        return data;
    }

    public void setData(EntryBook data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "undo";
    }
}
