package seedu.multitasky.model;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Set;

import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.model.entry.exceptions.EntryOverdueException;
import seedu.multitasky.model.entry.exceptions.OverlappingAndOverdueEventException;
import seedu.multitasky.model.entry.exceptions.OverlappingEventException;
import seedu.multitasky.storage.exception.NothingToRedoException;
import seedu.multitasky.storage.exception.NothingToUndoException;

// @@author A0126623L
/**
 * The API of the Model component.
 */
public interface Model {

    public enum Search {
        AND, OR, POWER_AND, POWER_OR
    };

    public static final Search[] STRICT_SEARCHES = { Search.AND, Search.POWER_AND };
    public static final Search[] LENIENT_SEARCHES = { Search.AND, Search.OR, Search.POWER_AND,
        Search.POWER_OR };

    // =========== EntryBook-level Operations ===========

    /** Returns the EntryBook */
    ReadOnlyEntryBook getEntryBook();

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyEntryBook newData);

    // =========== Entry Level Operations ===========

    /** Adds the given entry
     * @throws EntryOverdueException
     * @throws OverlappingAndOverdueEventException
     */
    void addEntry(ReadOnlyEntry entry) throws DuplicateEntryException, OverlappingEventException,
            OverlappingAndOverdueEventException, EntryOverdueException;

    /** Deletes the given entry. */
    void deleteEntry(ReadOnlyEntry target) throws DuplicateEntryException, EntryNotFoundException;

    /**
     * Replaces the given entry {@code target} with {@code editedEntry}.
     * Entry can be changed from one type to another, e.g. from an event to a deadline.
     *
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     * @throws EntryOverdueException if {@code editedReadOnlyEntry} is overdue.
     * @throws OverlappingAndOverdueEventException is an event which overlaps with
     *              existing event(s) and is overdue.
     */
    void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry)
            throws DuplicateEntryException, EntryNotFoundException, OverlappingEventException,
            OverlappingAndOverdueEventException, EntryOverdueException;

    /** Updates the state of a given entry.
     * @throws EntryOverdueException
     * @throws OverlappingAndOverdueEventException
     */
    void changeEntryState(ReadOnlyEntry entryToChange, Entry.State newState)
            throws DuplicateEntryException, EntryNotFoundException, OverlappingEventException,
            OverlappingAndOverdueEventException, EntryOverdueException;

    /** Clears all entries of a specific state. */
    public void clearStateSpecificEntries(Entry.State state);

    // =========== Filtered Entry List Accessors ===========

    /** Returns the filtered event list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getFilteredEventList();

    /** Returns the filtered deadline list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getFilteredDeadlineList();

    /** Returns the filtered floating task list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getFilteredFloatingTaskList();

    /** Returns the active entry list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getActiveList();

    /** Updates the filter of all filtered lists to show all active entries */
    public void updateAllFilteredListToShowAllActiveEntries();

    /**
     * Updates the filter of all entry lists to filter by the given keywords,
     * date range and state. Attempts all the different searches in order until it has at least 1 result.
     */
    void updateAllFilteredLists(Set<String> keywords, Calendar startDate, Calendar endDate,
                                Entry.State state, Search... searches);

    /**
     * Updates the filter of all entry lists to filter by the given keywords,
     * date range and state. Attempts all the different searches in order until it has at least 1 result.
     * Allows for two states to be shown at the same time.
     */
    void updateAllFilteredLists(Set<String> keywords, Calendar startDate, Calendar endDate,
                                Entry.State state, Entry.State state2, Search... searches);

    /**
     * Updates the filter of the filtered event list to filter by the given keywords,
     * date range and state using the specified search type.
     */
    void updateFilteredEventList(Set<String> keywords, Calendar startDate, Calendar endDate,
                                 Entry.State state, Search search, int level);

    /**
     * Updates the filter of the filtered deadline list to filter by the given keywords,
     * date range and state using the specified search type.
     */
    void updateFilteredDeadlineList(Set<String> keywords, Calendar startDate, Calendar endDate,
                                    Entry.State state, Search search, int level);

    /**
     * Updates the filter of the filtered floating task list to filter by the given keywords,
     * date range and state using the specified search type.
     */
    void updateFilteredFloatingTaskList(Set<String> keywords, Calendar startDate, Calendar endDate,
                                        Entry.State state, Search search, int level);

    /** Updates the sorting comparators used. */
    void updateSortingComparators(Comparator<ReadOnlyEntry> eventComparator,
                                  Comparator<ReadOnlyEntry> deadlineComparator,
                                  Comparator<ReadOnlyEntry> floatingTaskComparator);

    // =========== Storage-Related Operations ===========

    /** Undo the previous data-changing action */
    void undoPreviousAction() throws NothingToUndoException;

    /** Redo the previous undo action */
    void redoPreviousAction() throws NothingToRedoException;

    /** Change the file path for storage */
    void changeFilePath(String newFilePath);

    /**
     * Load data at the given file path
     *
     * @throws IllegalValueException
     */
    void openFilePath(String newFilePath) throws IllegalValueException;

}
