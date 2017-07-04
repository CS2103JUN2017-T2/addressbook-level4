package seedu.address.model;


import javafx.collections.ObservableList;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.tag.Tag;

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
