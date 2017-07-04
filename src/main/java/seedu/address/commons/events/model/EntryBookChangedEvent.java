package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyEntryBook;

/** Indicates the EntryBook in the model has changed*/
public class EntryBookChangedEvent extends BaseEvent {

    public final ReadOnlyEntryBook data;

    public EntryBookChangedEvent(ReadOnlyEntryBook data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getEntryList().size() + ", number of tags " + data.getTagList().size();
    }
}
