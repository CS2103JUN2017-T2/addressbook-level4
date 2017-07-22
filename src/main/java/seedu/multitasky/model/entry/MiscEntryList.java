package seedu.multitasky.model.entry;

import java.util.List;

import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;

//@@author A0126623L
/**
 * A list of active entries (not marked as 'done') that does not allow nulls.
 */
public class MiscEntryList extends EntryList {

    // @@author A0126623L
    /**
     * Adds the reference to an entry to the list. It does not create new entry objects.
     *
     * @param toAdd is a subclass of Entry and must not be null.
     */
    @Override
    public void add(ReadOnlyEntry toAdd) throws DuplicateEntryException {
        super.add(toAdd);
        assert (toAdd instanceof Entry);
        internalList.add((Entry) toAdd);
    }

    // @@author A0126623L
    /**
     * Sets MiscEntryList to contain the reference to the entries in the given list.
     */
    public void setEntries(List<? extends ReadOnlyEntry> entries) throws DuplicateEntryException {
        final MiscEntryList replacement = new MiscEntryList();
        for (final ReadOnlyEntry entry : entries) {
            replacement.add(entry);
        }
        super.setEntries(replacement);
    }

}
