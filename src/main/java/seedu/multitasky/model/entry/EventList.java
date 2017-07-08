package seedu.multitasky.model.entry;

import static java.util.Objects.requireNonNull;

import java.util.List;

/**
 * A list of Event objects that does not allow nulls.
 * Supports a minimal set of list operations.
 */
public class EventList extends EntryList {

    /**
     * Adds an event to the list.
     * Pre-conditions: toAdd is not null and is of type Event.
     */
    @Override
    public void add(ReadOnlyEntry toAdd) {
        requireNonNull(toAdd);
        assert (toAdd instanceof Event);

        internalList.add(new Event(toAdd));
    }

    /**
     * Sets EventList to contain the given List of Events.
     */
    public void setEntries(List<? extends ReadOnlyEntry> entries) {
        final EventList replacement = new EventList();
        for (final ReadOnlyEntry entry : entries) {
            replacement.add(new Event(entry));
        }
        super.setEntries(replacement);
    }

}
