package seedu.multitasky.model;

import java.util.Set;

import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.storage.exception.NothingToRedoException;
import seedu.multitasky.storage.exception.NothingToUndoException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyEntryBook newData);

    /** Returns the EntryBook */
    ReadOnlyEntryBook getEntryBook();

    /** Deletes the given entry. */
    void deleteEntry(ReadOnlyEntry target) throws DuplicateEntryException, EntryNotFoundException;

    /** Adds the given entry */
    void addEntry(ReadOnlyEntry entry) throws DuplicateEntryException;

    /** Undo the previous data-changing action */
    void undoPreviousAction() throws NothingToUndoException;

    /** Redo the previous undo action */
    void redoPreviousAction() throws NothingToRedoException;

    /**
     * Replaces the given entry {@code target} with {@code editedEntry}.
     *
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     */
    void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry) throws DuplicateEntryException,
            EntryNotFoundException;

    /** Returns the filtered event list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getFilteredEventList();

    /** Returns the filtered deadline list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getFilteredDeadlineList();

    /** Returns the filtered floating task list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getFilteredFloatingTaskList();

    /** Returns the active entry list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getActiveList();

    /** Returns the entry archive as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getArchive();

    /** Returns the entry bin as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getBin();

    /** Updates the filter of the filtered event list to show all entries */
    void updateFilteredEventListToShowAll();

    /** Updates the filter of the filtered deadline list to show all entries */
    void updateFilteredDeadlineListToShowAll();

    /** Updates the filter of the filtered floating task list to show all entries */
    void updateFilteredFloatingTaskListToShowAll();

    /** Updates the filter of all filtered lists to show all entries */
    public void updateAllFilteredListToShowAll();

    /** Updates the filter of the filtered event list to filter by the given keywords */
    void updateFilteredEventList(Set<String> keywords);

    /** Updates the filter of the filtered deadline list to filter by the given keywords */
    void updateFilteredDeadlineList(Set<String> keywords);

    /** Updates the filter of the filtered floating task list to filter by the given keywords */
    void updateFilteredFloatingTaskList(Set<String> keywords);

}
