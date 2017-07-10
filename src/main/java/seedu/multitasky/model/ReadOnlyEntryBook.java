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
     * TODO: Update this Javadoc if duplicate is not allowed in the future.
     */
    ObservableList<ReadOnlyEntry> getActiveList();

    /**
     * Returns an unmodifiable view of the archived entry list.
     * TODO: Update this Javadoc if duplicate is not allowed in the future.
     */
    ObservableList<ReadOnlyEntry> getArchive();

    /**
     * Returns an unmodifiable view of the deleted entry list.
     * TODO: Update this Javadoc if duplicate is not allowed in the future.
     */
    ObservableList<ReadOnlyEntry> getBin();

    /**
     * Returns an unmodifiable view of the (active) event list.
     * TODO: Update this Javadoc if duplicate is not allowed in the future.
     */
    ObservableList<ReadOnlyEntry> getEventList();

    /**
     * Returns an unmodifiable view of the (active) deadline list.
     * TODO: Update this Javadoc if duplicate is not allowed in the future.
     */
    ObservableList<ReadOnlyEntry> getDeadlineList();

    /**
     * Returns an unmodifiable view of the (active) floating task list.
     * TODO: Update this Javadoc if duplicate is not allowed in the future.
     */
    ObservableList<ReadOnlyEntry> getFloatingTaskList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

}
