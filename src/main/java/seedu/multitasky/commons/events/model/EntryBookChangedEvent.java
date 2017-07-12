package seedu.multitasky.commons.events.model;

import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.model.ReadOnlyEntryBook;

/** Indicates the EntryBook in the model has changed */
public class EntryBookChangedEvent extends BaseEvent {

    public final ReadOnlyEntryBook data;

    public EntryBookChangedEvent(ReadOnlyEntryBook data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of events " + data.getEventList().size()
                + ", number of deadlines " + data.getDeadlineList().size()
                + ", number of floating tasks " + data.getFloatingTaskList().size()
                + ", number of tags " + data.getTagList().size();
    }
}
