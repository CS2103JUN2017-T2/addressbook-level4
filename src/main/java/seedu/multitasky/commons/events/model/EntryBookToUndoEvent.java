package seedu.multitasky.commons.events.model;

import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.model.ReadOnlyEntryBook;

/** Indicates the EntryBook in the model has changed */
public class EntryBookToUndoEvent extends BaseEvent {

    public final ReadOnlyEntryBook data;

    public EntryBookToUndoEvent(ReadOnlyEntryBook data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "undo";
    }
}
