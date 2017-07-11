package seedu.multitasky.model.entry;

import java.util.List;

import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

//@@author A0126623L
/**
 * A list of Deadline objects that does not allow nulls.
 */
public class DeadlineList extends EntryList {

    // @@author A0126623L
    /**
     * Adds a deadline to the list.
     * Pre-condition: toAdd is not null and is of type Deadline.
     *
     * @throws DuplicateEntryException if {@code toAdd} already exists in the list.
     */
    @Override
    public void add(ReadOnlyEntry toAdd) throws DuplicateEntryException {
        super.add(toAdd);
        assert (toAdd instanceof Deadline);

        internalList.add(new Deadline(toAdd));
        sortInternalList();
    }

    //@@author A0125586X
    /**
     * Overrides updateEntry in DeadlineList to sort after updating in case start date was changed.
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

    // @@author A0126623L
    /**
     * Clears the current list of deadlines and add all elements from the list of entries given.
     *
     * @param entries must be a list of deadlines.
     */
    public void setEntries(List<? extends ReadOnlyEntry> entries) throws DuplicateEntryException {
        final DeadlineList replacement = new DeadlineList();
        for (final ReadOnlyEntry entry : entries) {
            replacement.add(new Deadline(entry));
        }
        super.setEntries(replacement);
    }
    // @@author

}
