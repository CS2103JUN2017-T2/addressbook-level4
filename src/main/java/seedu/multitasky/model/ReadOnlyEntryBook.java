package seedu.multitasky.model;

import javafx.collections.ObservableList;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;

/**
 * Unmodifiable view of an entry book
 */
public interface ReadOnlyEntryBook {

    /**
     * Returns an unmodifiable view of the active entry list.
     */
    ObservableList<ReadOnlyEntry> getAllEntries();

    /**
     * Returns an unmodifiable view of the (active) event list.
     */
    ObservableList<ReadOnlyEntry> getEventList();

    /**
     * Returns an unmodifiable view of the (active) deadline list.
     */
    ObservableList<ReadOnlyEntry> getDeadlineList();

    /**
     * Returns an unmodifiable view of the (active) floating task list.
     */
    ObservableList<ReadOnlyEntry> getFloatingTaskList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

}
