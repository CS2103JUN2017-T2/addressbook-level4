package seedu.multitasky.model;

import java.util.Set;

import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

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
    void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry) throws EntryNotFoundException;

    // @@author A0126623L
    /** Returns the filtered event list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getFilteredEventList();

    // @@author A0126623L
    /** Returns the filtered deadline list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getFilteredDeadlineList();

    // @@author A0126623L
    /** Returns the filtered floating task list as an {@code UnmodifiableObservableList<ReadOnlyEntry>} */
    UnmodifiableObservableList<ReadOnlyEntry> getFilteredFloatingTaskList();

    // @@author A0126623L
    /** Updates the filter of the filtered event list to show all entries */
    void updateFilteredEventListToShowAll();

    // @@author A0126623L
    /** Updates the filter of the filtered deadline list to show all entries */
    void updateFilteredDeadlineListToShowAll();

    // @@author A0126623L
    /** Updates the filter of the filtered floating task list to show all entries */
    void updateFilteredFloatingTaskListToShowAll();

    // @@author A0126623L
    /** Updates the filter of the filtered event list to filter by the given keywords */
    void updateFilteredEventList(Set<String> keywords);

    // @@author A0126623L
    /** Updates the filter of the filtered deadline list to filter by the given keywords */
    void updateFilteredDeadlineList(Set<String> keywords);

    // @@author A0126623L
    /** Updates the filter of the filtered floating task list to filter by the given keywords */
    void updateFilteredFloatingTaskList(Set<String> keywords);

    // @@author
}
