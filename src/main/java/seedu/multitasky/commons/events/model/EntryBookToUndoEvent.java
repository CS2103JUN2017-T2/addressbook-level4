package seedu.multitasky.commons.events.model;

import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.model.EntryBook;

/** Indicates the EntryBook in the model has changed */
public class EntryBookToUndoEvent extends BaseEvent {

    public EntryBook data;

    public EntryBook getData() {
        return data;
    }

    public void setData(EntryBook data) {
        this.data = data;
    }

    public EntryBookToUndoEvent(EntryBook data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "undo";
    }
}
