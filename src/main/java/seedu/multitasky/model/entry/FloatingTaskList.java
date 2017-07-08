package seedu.multitasky.model.entry;

import static java.util.Objects.requireNonNull;

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
}
