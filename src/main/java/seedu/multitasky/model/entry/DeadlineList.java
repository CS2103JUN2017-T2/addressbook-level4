package seedu.multitasky.model.entry;

import java.util.List;

import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;

//@@author A0126623L
/**
 * A list of Deadline objects that does not allow nulls.
 */
public class DeadlineList extends EntryList {

    // @@author A0126623L
    /**
     * Adds a deadline to the list.
     * Pre-condition: toAdd is not null and is of type Deadline.
     */
    @Override
    public void add(ReadOnlyEntry toAdd) throws DuplicateEntryException {
        super.add(toAdd);
        assert (toAdd instanceof Deadline);

        internalList.add(new Deadline(toAdd));
    }
    // @@author

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
