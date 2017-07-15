package seedu.multitasky.commons.events.ui;

import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.model.entry.Entry;

// @@author A0125586X
/**
 * Indicates that the entries listed in the UI are now a different type (active/archived/deleted)
 */
public class ListTypeUpdateEvent extends BaseEvent {

    public final Entry.State state;

    public ListTypeUpdateEvent(Entry.State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
