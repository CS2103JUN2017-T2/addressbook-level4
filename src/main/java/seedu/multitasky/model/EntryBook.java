package seedu.multitasky.model;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.DeadlineList;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.EntryList;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.EventList;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.FloatingTaskList;
import seedu.multitasky.model.entry.MiscEntryList;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.model.tag.UniqueTagList;

/**
 * Wraps all data at the entry-book level
 */
public class EntryBook implements ReadOnlyEntryBook {

    private final MiscEntryList _activeList;
    private final MiscEntryList _archive;
    private final MiscEntryList _bin;
    private final EventList _eventList;
    private final DeadlineList _deadlineList;
    private final FloatingTaskList _floatingTaskList;
    private final UniqueTagList _tags;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid
     * duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     * TODO by ChuaPingChan: Improve this section.
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     * among constructors.
     */
    {
        _activeList = new MiscEntryList();
        _archive = new MiscEntryList();
        _bin = new MiscEntryList();
        _eventList = new EventList();
        _deadlineList = new DeadlineList();
        _floatingTaskList = new FloatingTaskList();
        _tags = new UniqueTagList();
    }

    public EntryBook() {
    }

    /**
     * Creates an EntryBook using the Entries and Tags in the {@code toBeCopied}
     */
    public EntryBook(ReadOnlyEntryBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setActiveList(List<? extends ReadOnlyEntry> entries) {
        this._activeList.setEntries(entries);
    }

    public void setArchive(List<? extends ReadOnlyEntry> entries) {
        this._archive.setEntries(entries);
    }

    public void setBin(List<? extends ReadOnlyEntry> entries) {
        this._bin.setEntries(entries);
    }

    public void setEventList(List<? extends ReadOnlyEntry> entries) {
        this._eventList.setEntries(entries);
    }

    public void setDeadlineList(List<? extends ReadOnlyEntry> entries) {
        this._deadlineList.setEntries(entries);
    }

    public void setFloatingTaskList(List<? extends ReadOnlyEntry> entries) {
        this._floatingTaskList.setEntries(entries);
    }

    public void setTags(Collection<Tag> tags) throws UniqueTagList.DuplicateTagException {
        this._tags.setTags(tags);
    }

    public void resetData(ReadOnlyEntryBook newData) {
        requireNonNull(newData);

        setActiveList(newData.getActiveList());
        setArchive(newData.getArchive());
        setBin(newData.getBin());
        setEventList(newData.getEventList());
        setDeadlineList(newData.getDeadlineList());
        setFloatingTaskList(newData.getFloatingTaskList());

        try {
            setTags(newData.getTagList());
        } catch (UniqueTagList.DuplicateTagException e) {
            assert false : "EntryBooks should not have duplicate tags";
        }
        syncMasterTagListWith(_activeList);
    }

    //// entry-level operations

    /**
     * Adds an entry to the entry book.
     * Creates the appropriate sub-type of the new entry and adds its reference to the active entry list, as
     * well as one of event/deadline/floating task list.
     * Also checks the new entry's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the entry to point to those in {@link #tags}.
     */
    public void addEntry(ReadOnlyEntry e) {
        addToEntrySubTypeList(e);

        Entry newEntry = convertToEntrySubType(e);
        syncMasterTagListWith(newEntry);
        _activeList.add(newEntry); // Adds reference of newEntry to activeList, not creating a copy.
    }

    /**
     * Add a given ReadOnlyEntry to one of either active, deadline or floating task list.
     */
    private void addToEntrySubTypeList(ReadOnlyEntry newEntry) {
        if (newEntry instanceof Event) {
            _eventList.add(newEntry);
        } else if (newEntry instanceof Deadline) {
            _deadlineList.add(newEntry);
        } else {
            assert (newEntry instanceof FloatingTask);
            _floatingTaskList.add(newEntry);
        }
    }

    /**
     * Replaces the given entry {@code target} in the list with {@code editedReadOnlyEntry}.
     * {@code EntryBook}'s tag list will be updated with the tags of {@code editedReadOnlyEntry}.
     *
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     * @see #syncMasterTagListWith(Entry)
     */
    public void updateEntry(ReadOnlyEntry target,
                            ReadOnlyEntry editedReadOnlyEntry)
            throws EntryNotFoundException {
        requireNonNull(editedReadOnlyEntry);

        _activeList.updateEntry(target, editedReadOnlyEntry);

        Entry editedEntry = convertToEntrySubType(editedReadOnlyEntry);
        syncMasterTagListWith(editedEntry);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any entry
        // in the entry list.
    }

    /**
     * Converts a given ReadOnlyEntryObject to an appropriate Event object (i.e. event, deadline or floating
     * task).
     *
     * @return Entry
     */
    public Entry convertToEntrySubType(ReadOnlyEntry editedReadOnlyEntry) {
        Entry newEntry;
        if (editedReadOnlyEntry instanceof Event) {
            newEntry = (Event) editedReadOnlyEntry;
        } else if (editedReadOnlyEntry instanceof Deadline) {
            newEntry = (Deadline) editedReadOnlyEntry;
        } else {
            assert (editedReadOnlyEntry instanceof FloatingTask);
            newEntry = (FloatingTask) editedReadOnlyEntry;
        }
        return newEntry;
    }

    /**
     * Ensures that every tag in this entry:
     * - exists in the master list {@link #tags}
     * - points to a Tag object in the master list
     */
    private void syncMasterTagListWith(Entry entry) {
        final UniqueTagList entryTags = new UniqueTagList(entry.getTags());
        _tags.mergeFrom(entryTags);

        // Create map with values = tag object references in the master list
        // used for checking entry tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        _tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of entry tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        entryTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        entry.setTags(correctTagReferences);
    }

    /**
     * Ensures that every tag in these entries:
     * - exists in the master list {@link #tags}
     * - points to a Tag object in the master list
     *
     * @see #syncMasterTagListWith(Entry)
     */
    private void syncMasterTagListWith(EntryList entries) {
        entries.forEach(this::syncMasterTagListWith);
    }

    /**
     * Removes entry from the appropriate lists (i.e. active, event, deadline, floating task lists) and add it
     * to the bin.
     *
     * @param entryToRemove
     * @return boolean
     * @throws EntryNotFoundException
     */
    public boolean removeEntry(ReadOnlyEntry entryToRemove) throws EntryNotFoundException {
        if (_activeList.remove(entryToRemove) && removeFromEntrySubTypeList(entryToRemove)) {
            return true;
        } else {
            throw new EntryNotFoundException();
        }
    }

    /**
     * Removes an entry from the appropriate list (i.e. event list, deadline list, floating task list).
     *
     * @param entryToRemove is of type Event, Deadline or FloatingTask
     * @return boolean
     */
    private boolean removeFromEntrySubTypeList(ReadOnlyEntry entryToRemove) throws EntryNotFoundException {
        if (entryToRemove instanceof Event) {
            return _eventList.remove(entryToRemove);
        } else if (entryToRemove instanceof Deadline) {
            return _deadlineList.remove(entryToRemove);
        } else {
            assert (entryToRemove instanceof FloatingTask);
            return _floatingTaskList.remove(entryToRemove);
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        _tags.add(t);
    }

    //// util methods

    /**
     * @return String Information of the number of active entries and tags present.
     */
    @Override
    public String toString() {
        return _activeList.asObservableList().size() + " active entries, " + _tags.asObservableList().size()
               + " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<ReadOnlyEntry> getActiveList() {
        return new UnmodifiableObservableList<>(_activeList.asObservableList());
    }

    @Override
    public ObservableList<ReadOnlyEntry> getArchive() {
        return new UnmodifiableObservableList<>(_archive.asObservableList());
    }

    @Override
    public ObservableList<ReadOnlyEntry> getBin() {
        return new UnmodifiableObservableList<>(_bin.asObservableList());
    }

    @Override
    public ObservableList<ReadOnlyEntry> getEventList() {
        return new UnmodifiableObservableList<>(_eventList.asObservableList());
    }

    @Override
    public ObservableList<ReadOnlyEntry> getDeadlineList() {
        return new UnmodifiableObservableList<>(_deadlineList.asObservableList());
    }

    @Override
    public ObservableList<ReadOnlyEntry> getFloatingTaskList() {
        return new UnmodifiableObservableList<>(_floatingTaskList.asObservableList());
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return new UnmodifiableObservableList<>(_tags.asObservableList());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
               || (other instanceof EntryBook // instanceof handles nulls
                   && this.getActiveList().equals(((EntryBook) other).getActiveList())
                   && this.getArchive().equals(((EntryBook) other).getArchive())
                   && this.getBin().equals(((EntryBook) other).getBin())
                   && this.getEventList().equals(((EntryBook) other).getEventList())
                   && this.getDeadlineList().equals(((EntryBook) other).getDeadlineList())
                   && this.getFloatingTaskList().equals(((EntryBook) other).getFloatingTaskList())
                   && this._tags.equalsOrderInsensitive(((EntryBook) other)._tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(getActiveList(), getArchive(), getBin(),
                            getEventList(), getDeadlineList(), getFloatingTaskList(),
                            getTagList());
    }
}
