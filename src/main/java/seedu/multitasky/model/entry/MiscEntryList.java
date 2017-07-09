package seedu.multitasky.model.entry;

import static java.util.Objects.requireNonNull;

import java.util.List;

//@@author A0126623L
/**
 * A list of active entries (not marked as 'done') that does not allow nulls.
 */
public class MiscEntryList extends EntryList {

    /**
     * Adds the reference to an entry to the list. It does not create new entry objects.
     *
     * @param toAdd is a subclass of Entry and must not be null.
     */
    @Override
    public void add(ReadOnlyEntry toAdd) {
        requireNonNull(toAdd);
        assert (toAdd instanceof Entry);
        add((Entry) toAdd);
    }

    /**
     * Private helper method to add entry references to ActiveList.
     */
    private void add(Entry toAdd) {
        this.internalList.add(toAdd);
    }

    /**
     * Sets EventList to contain the reference to the entries in the given list.
     */
    public void setEntries(List<? extends ReadOnlyEntry> entries) {
        final EventList replacement = new EventList();
        for (final ReadOnlyEntry entry : entries) {
            replacement.add(entry);
        }
        super.setEntries(replacement);
    }

}
