package seedu.multitasky.model.entry;

import java.util.List;

import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.util.Comparators;

public class FloatingTaskList extends EntryList {

    // @@author A0125586X
    public FloatingTaskList() {
        super();
        comparator = Comparators.FLOATING_TASK_DEFAULT;
    }

    // @@author A0126623L
    /**
     * Adds a floating task reference to the list.
     * Pre-conditions: toAdd is not null and is of type FloatingTask.
     *
     * @throws DuplicateEntryException if {@code toAdd} already exists in the list.
     */
    @Override
    public void add(ReadOnlyEntry toAdd) throws DuplicateEntryException {
        super.add(toAdd);
        if (!(toAdd instanceof FloatingTask)) {
            throw new AssertionError("Non-FloatingTask type cannot be added to an FloatingTaskList.");
        }

        internalList.add((FloatingTask) toAdd);
    }

    // @@author A0126623L
    /**
     * Clears the current list of floating tasks and add all elements from the list of entries given.
     *
     * @param entries must be a list of floating tasks.
     */
    public void setEntries(List<? extends ReadOnlyEntry> entries) throws DuplicateEntryException {
        final FloatingTaskList replacement = new FloatingTaskList();
        for (final ReadOnlyEntry entry : entries) {
            replacement.add(entry);   // Type check is done within add().
        }
        super.setEntries(replacement);
    }
}
