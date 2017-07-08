package seedu.multitasky.model.entry;

import static java.util.Objects.requireNonNull;

import java.util.List;

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
    public void add(ReadOnlyEntry toAdd) {
        requireNonNull(toAdd);
        assert (toAdd instanceof Deadline);

        internalList.add(new Deadline(toAdd));
    }

    // @@author A0126623L
    /**
     * Clears the current list of Deadlines and add all elements from the list of entries given.
     *
     * @param entries must be a list of Deadlines.
     */
    public void setEntries(List<? extends ReadOnlyEntry> entries) {
        final DeadlineList replacement = new DeadlineList();
        for (final ReadOnlyEntry entry : entries) {
            replacement.add(new Deadline(entry));
        }
        super.setEntries(replacement);
    }

}
