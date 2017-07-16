package seedu.multitasky.model;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Set;

import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.model.entry.Entry;
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

    /** Updates the state of a given entry. */
    void changeEntryState(ReadOnlyEntry entryToChange, Entry.State newState)
            throws DuplicateEntryException, EntryNotFoundException;

    /** Undo the previous data-changing action */
    void undoPreviousAction() throws NothingToUndoException;

    /** Redo the previous undo action */
    void redoPreviousAction() throws NothingToRedoException;

    /** Change the file path for storage */
    void changeFilePath(String newFilePath);

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

    /** Updates the filter of the filtered event list to show all entries */
    void updateFilteredEventListToShowAll();

    /** Updates the filter of the filtered deadline list to show all entries */
    void updateFilteredDeadlineListToShowAll();

    /** Updates the filter of the filtered floating task list to show all entries */
    void updateFilteredFloatingTaskListToShowAll();

    /** Updates the filter of all filtered lists to show all entries */
    public void updateAllFilteredListToShowAll();

    /** Updates the filter of all filtered lists to show all active entries */
    public void updateAllFilteredListToShowAllActiveEntries();

    /** Updates the filter of all filtered lists to show all archived entries */
    public void updateAllFilteredListToShowAllArchivedEntries();

    /** Updates the filter of all filtered lists to show all deleted entries */
    public void updateAllFilteredListToShowAllDeletedEntries();

    /** Updates the filter of the filtered event list to filter by the given keywords and state */
    void updateFilteredEventList(Set<String> keywords, Entry.State state);

    /** Updates the filter of the filtered event list to filter by the given date range and state */
    void updateFilteredEventList(Calendar startDate, Calendar endDate, Entry.State state);

    /** Updates the filter of the filtered deadline list to filter by the given keywords and state */
    void updateFilteredDeadlineList(Set<String> keywords, Entry.State state);

    /** Updates the filter of the filtered deadline list to filter by the given date range and state */
    void updateFilteredDeadlineList(Calendar startDate, Calendar endDate, Entry.State state);

    /** Updates the filter of the filtered floating task list to filter by the given keywords and state */
    void updateFilteredFloatingTaskList(Set<String> keywords, Entry.State state);

    /** Updates the filter of the filtered floating task list to filter by the given date range and state */
    void updateFilteredFloatingTaskList(Calendar startDate, Calendar endDate, Entry.State state);

    /** Updates the sorting comparators used. */
    void updateSortingComparators(Comparator<ReadOnlyEntry> eventComparator,
                                  Comparator<ReadOnlyEntry> deadlineComparator,
                                  Comparator<ReadOnlyEntry> floatingTaskComparator);

}
