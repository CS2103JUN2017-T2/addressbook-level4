package seedu.multitasky.model;

import javafx.collections.ObservableList;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;

/**
 * Unmodifiable view of an entry book
 */
public interface ReadOnlyEntryBook {

    /**
     * Returns an unmodifiable view of the entry list.
     * This list will not contain any duplicate entry.
     */
    ObservableList<ReadOnlyEntry> getEntryList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

}
