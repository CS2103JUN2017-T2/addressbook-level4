package seedu.address.model;

import java.util.Set;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

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
    void addEntry(ReadOnlyEntry entry) throws DuplicateEntryException;

    /**
     * Replaces the given entry {@code target} with {@code editedEntry}.
     *
     * @throws DuplicateEntryException if updating the entry's details causes the entry to be equivalent to
     *      another existing person in the list.
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     */
    void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry)
            throws DuplicateEntryException, EntryNotFoundException;

    /** Returns the filtered entry list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getFilteredEntryList();

    /** Updates the filter of the filtered entry list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered entry list to filter by the given keywords*/
    void updateFilteredEntryList(Set<String> keywords);

}
