package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.entry.EntryList;
import seedu.address.model.entry.exceptions.EntryNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Wraps all data at the entry-book level
 */
public class EntryBook implements ReadOnlyEntryBook {

    private final EntryList entries;
    private final UniqueTagList tags;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        entries = new EntryList();
        tags = new UniqueTagList();
    }

    public EntryBook() {}

    /**
     * Creates an EntryBook using the Entries and Tags in the {@code toBeCopied}
     */
    public EntryBook(ReadOnlyEntryBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setEntries(List<? extends ReadOnlyEntry> entries) {
        this.entries.setEntries(entries);
    }

    public void setTags(Collection<Tag> tags) throws UniqueTagList.DuplicateTagException {
        this.tags.setTags(tags);
    }

    public void resetData(ReadOnlyEntryBook newData) {
        requireNonNull(newData);
        setEntries(newData.getEntryList());
        try {
            setTags(newData.getTagList());
        } catch (UniqueTagList.DuplicateTagException e) {
            assert false : "EntryBooks should not have duplicate tags";
        }
        syncMasterTagListWith(entries);
    }

    //// entry-level operations

    /**
     * Adds an entry to the entry book.
     * Also checks the new entry's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the entry to point to those in {@link #tags}.
     */
    public void addEntry(ReadOnlyEntry e) {
        Entry newEntry = new Entry(e);
        syncMasterTagListWith(newEntry);
        entries.add(newEntry);
    }

    /**
     * Replaces the given entry {@code target} in the list with {@code editedReadOnlyEntry}.
     * {@code EntryBook}'s tag list will be updated with the tags of {@code editedReadOnlyEntry}.
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncMasterTagListWith(Entry)
     */
    public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedReadOnlyEntry)
            throws EntryNotFoundException {
        requireNonNull(editedReadOnlyEntry);

        Entry editedEntry = new Entry(editedReadOnlyEntry);
        syncMasterTagListWith(editedEntry);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any entry
        // in the entry list.
        entries.updateEntry(target, editedEntry);
    }

    /**
     * Ensures that every tag in this entry:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     */
    private void syncMasterTagListWith(Entry entry) {
        final UniqueTagList entryTags = new UniqueTagList(entry.getTags());
        tags.mergeFrom(entryTags);

        // Create map with values = tag object references in the master list
        // used for checking entry tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of entry tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        entryTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        entry.setTags(correctTagReferences);
    }

    /**
     * Ensures that every tag in these entries:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     *  @see #syncMasterTagListWith(Entry)
     */
    private void syncMasterTagListWith(EntryList entries) {
        entries.forEach(this::syncMasterTagListWith);
    }

    public boolean removeEntry(ReadOnlyEntry key) throws EntryNotFoundException {
        if (entries.remove(key)) {
            return true;
        } else {
            throw new EntryNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    //// util methods

    @Override
    public String toString() {
        return entries.asObservableList().size() + " entries, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<ReadOnlyEntry> getEntryList() {
        return new UnmodifiableObservableList<>(entries.asObservableList());
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return new UnmodifiableObservableList<>(tags.asObservableList());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EntryBook // instanceof handles nulls
                && this.entries.equals(((EntryBook) other).entries)
                && this.tags.equalsOrderInsensitive(((EntryBook) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(entries, tags);
    }
}
