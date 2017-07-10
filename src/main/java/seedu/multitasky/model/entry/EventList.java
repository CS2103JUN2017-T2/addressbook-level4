package seedu.multitasky.model.entry;

import java.util.List;

import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

//@@author A0126623L
/**
 * A list of Event objects that does not allow nulls.
 */
public class EventList extends EntryList {

    /**
     * Adds an event to the list.
     *
     * @param toAdd is of type Event and must not be null.
     * @throws DuplicateEntryException if {@code toAdd} already exists in the list.
     */
    @Override
    public void add(ReadOnlyEntry toAdd) throws DuplicateEntryException {
        super.add(toAdd);
        assert (toAdd instanceof Event);

        internalList.add(new Event(toAdd));
        sortInternalList();
    }

    //@@author A0125586X
    /**
     * Overrides updateEntry in EntryList to sort after updating in case start date was changed.
     *
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     * @throws DuplicateEntryException if {@code editedEntry} already exists in the list.
     */
    @Override
    public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry)
            throws DuplicateEntryException, EntryNotFoundException {
        super.updateEntry(target, editedEntry);
        sortInternalList();
    }
    //@@author



    // @@author A0126623L
    /**
     * Clears the current list of events and add all elements from the list of entries given.
     *
     * @param entries must be a list of events.
     */
    public void setEntries(List<? extends ReadOnlyEntry> entries) throws DuplicateEntryException {
        final EventList replacement = new EventList();
        for (final ReadOnlyEntry entry : entries) {
            replacement.add(new Event(entry));
        }
        super.setEntries(replacement);
    }

}
