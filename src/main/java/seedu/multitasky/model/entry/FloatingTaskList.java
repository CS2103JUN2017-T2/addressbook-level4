package seedu.multitasky.model.entry;

import static java.util.Objects.requireNonNull;

import java.util.List;

public class FloatingTaskList extends EntryList {

    /**
     * Adds a floating task to the list.
     * Pre-conditions: toAdd is not null and is of type FloatingTask.
     */
    @Override
    public void add(ReadOnlyEntry toAdd) {
        requireNonNull(toAdd);
        assert (toAdd instanceof FloatingTask);

        internalList.add(new FloatingTask(toAdd));
    }

    // @@author A0126623L
    /**
     * Clears the current list of floating tasks and add all elements from the list of entries given.
     *
     * @param entries must be a list of floating tasks.
     */
    public void setEntries(List<? extends ReadOnlyEntry> entries) {
        final FloatingTaskList replacement = new FloatingTaskList();
        for (final ReadOnlyEntry entry : entries) {
            replacement.add(new Event(entry));
        }
        super.setEntries(replacement);
    }
}
