package seedu.multitasky.model.entry;

import java.util.List;

import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;

public class FloatingTaskList extends EntryList {

    // @@author A0126623L
    /**
     * Adds a floating task to the list.
     * Pre-conditions: toAdd is not null and is of type FloatingTask.
     */
    @Override
    public void add(ReadOnlyEntry toAdd) throws DuplicateEntryException {
        super.add(toAdd);
        assert (toAdd instanceof FloatingTask);

        internalList.add(new FloatingTask(toAdd));
    }
    // @@author

    // @@author A0126623L
    /**
     * Clears the current list of floating tasks and add all elements from the list of entries given.
     *
     * @param entries must be a list of floating tasks.
     */
    public void setEntries(List<? extends ReadOnlyEntry> entries) throws DuplicateEntryException {
        final FloatingTaskList replacement = new FloatingTaskList();
        for (final ReadOnlyEntry entry : entries) {
            replacement.add(new FloatingTask(entry));
        }
        super.setEntries(replacement);
    }
    // @@author
}
