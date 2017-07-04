package seedu.address.model;

import java.util.Set;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyEntryBook newData);

    /** Returns the EntryBook */
    ReadOnlyEntryBook getEntryBook();

    /** Deletes the given entry. */
    void deleteEntry(ReadOnlyEntry target) throws EntryNotFoundException;

    /** Adds the given entry */
    void addEntry(ReadOnlyEntry entry);

    /**
     * Replaces the given entry {@code target} with {@code editedEntry}.
     *
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     */
    void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry)
            throws EntryNotFoundException;

    /** Returns the filtered entry list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getFilteredFloatingTaskList();

    /** Updates the filter of the filtered entry list to show all entries */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered entry list to filter by the given keywords*/
    void updateFilteredFloatingTaskList(Set<String> keywords);

}
