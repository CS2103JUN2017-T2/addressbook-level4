package seedu.multitasky.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.model.entry.exceptions.EntryOverdueException;
import seedu.multitasky.model.entry.exceptions.OverlappingAndOverdueEventException;
import seedu.multitasky.model.entry.exceptions.OverlappingEventException;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.model.tag.UniqueTagList;
import seedu.multitasky.model.util.EntryBuilder;

/**
 * Wraps all data at the entry-book level
 */
public class EntryBook implements ReadOnlyEntryBook {

    // TODO: Decide later if it's useful to keep an internal list of all entries
    private final MiscEntryList _allEntriesList;
    private final EventList _eventList;
    private final DeadlineList _deadlineList;
    private final FloatingTaskList _floatingTaskList;
    private final UniqueTagList _tags;

    public EntryBook() {
        _allEntriesList = new MiscEntryList();
        _eventList = new EventList();
        _deadlineList = new DeadlineList();
        _floatingTaskList = new FloatingTaskList();
        _tags = new UniqueTagList();
    }

    /**
     * Creates an EntryBook using the Entries and Tags in the {@code toBeCopied}
     */
    public EntryBook(ReadOnlyEntryBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    // @@author A0126623L
    private void setAllEntriesList() throws DuplicateEntryException {
        /*
         * The reset of list of all entries is different from the others because here we want to add the
         * references to existing entries, not making new copies.
         */
        this._allEntriesList.setEntries(new MiscEntryList());
        for (ReadOnlyEntry entry : _eventList) {
            this._allEntriesList.add(entry);
        }
        for (ReadOnlyEntry entry : _deadlineList) {
            this._allEntriesList.add(entry);
        }
        for (ReadOnlyEntry entry : _floatingTaskList) {
            this._allEntriesList.add(entry);
        }
    }

    public void setEventList(List<? extends ReadOnlyEntry> entries) throws DuplicateEntryException {
        this._eventList.setEntries(entries);
    }

    public void setDeadlineList(List<? extends ReadOnlyEntry> entries) throws DuplicateEntryException {
        this._deadlineList.setEntries(entries);
    }

    public void setFloatingTaskList(List<? extends ReadOnlyEntry> entries) throws DuplicateEntryException {
        this._floatingTaskList.setEntries(entries);
    }

    public void setTags(Collection<Tag> tags) throws UniqueTagList.DuplicateTagException {
        this._tags.setTags(tags);
    }

    // @@author A0126623L
    public void resetData(ReadOnlyEntryBook newData) {
        requireNonNull(newData);

        try {
            setEventList(newData.getEventList());
            setDeadlineList(newData.getDeadlineList());
            setFloatingTaskList(newData.getFloatingTaskList());

            setAllEntriesList();
        } catch (DuplicateEntryException e) {
            assert false : "EntryBooks should not have duplicate entries";
        }

        try {
            setTags(newData.getTagList());
        } catch (UniqueTagList.DuplicateTagException e) {
            assert false : "EntryBooks should not have duplicate tags";
        }
        syncMasterTagListWith(_allEntriesList);
    }
    // @@author

    // @@author A0125586X
    public void setComparators(Comparator<ReadOnlyEntry> eventComparator,
                               Comparator<ReadOnlyEntry> deadlineComparator,
                               Comparator<ReadOnlyEntry> floatingTaskComparator) {
        _eventList.setComparator(eventComparator);
        _deadlineList.setComparator(deadlineComparator);
        _floatingTaskList.setComparator(floatingTaskComparator);
    }
    // @@author

    //// entry-level operations

    // @@author A0126623L
    /**
     * Adds an entry to the entry book.
     * Creates the appropriate sub-type of the new entry and adds its reference to the active entry list, as
     * well as one of event/deadline/floating task list.
     * Also checks the new entry's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the entry to point to those in {@link #tags}.
     * @throws EntryOverdueException
     * @throws OverlappingAndOverdueEventException
     */
    public void addEntry(ReadOnlyEntry e)
            throws DuplicateEntryException, OverlappingEventException,
            OverlappingAndOverdueEventException, EntryOverdueException {
        try {
            addToEntrySubtypeList(e);
        } finally {
            Entry newEntry = convertToEntry(e);
            syncMasterTagListWith(newEntry);
            _allEntriesList.add(newEntry);
        }
    }
    // @@author

    // @@author A0126623L
    /**
     * Add a given ReadOnlyEntry to one of either active, deadline or floating task list.
     * @throws OverlappingAndOverdueEventException
     * @throws EntryOverdueException
     */
    private void addToEntrySubtypeList(ReadOnlyEntry newEntry)
            throws DuplicateEntryException, OverlappingEventException, OverlappingAndOverdueEventException,
            EntryOverdueException {
        if (newEntry instanceof Event) {
            boolean overlappingEventPresent = _eventList.hasOverlappingEvent(newEntry);
            boolean eventIsDue = ((Event) newEntry).isOverdue();

            _eventList.add(newEntry);

            if (overlappingEventPresent && eventIsDue) {
                throw new OverlappingAndOverdueEventException();
            } else if (overlappingEventPresent) {
                throw new OverlappingEventException();
            } else if (eventIsDue) {
                throw new EntryOverdueException();
            }
        } else if (newEntry instanceof Deadline) {
            boolean deadlineIsDue = ((Deadline) newEntry).isOverdue();

            _deadlineList.add(newEntry);

            if (deadlineIsDue) {
                throw new EntryOverdueException();
            }
        } else {
            assert (newEntry instanceof FloatingTask);
            _floatingTaskList.add(newEntry);
        }
    }
    // @@author

    // @@author A0126623L
    /**
     * Replaces the given entry {@code target} in the list with {@code editedReadOnlyEntry}.
     * {@code EntryBook}'s tag list will be updated with the tags of {@code editedReadOnlyEntry}.
     *
     * {@code editedReadOnlyEntry} must be of the same entry sub-type as {@code target}.
     *
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     * @throws OverlappingEventException if {@code target} is an event and would overlap with existing active events
     * after being updated.
     * @throws EntryOverdueException if {@code editedReadOnlyEntry} is overdue.
     * @throws OverlappingAndOverdueEventException is an event which overlaps with
     *              existing event(s) and is overdue.
     * @see #syncMasterTagListWith(Entry)
     */
    public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedReadOnlyEntry)
            throws DuplicateEntryException, EntryNotFoundException, OverlappingEventException,
            OverlappingAndOverdueEventException, EntryOverdueException {
        requireNonNull(target);
        requireNonNull(editedReadOnlyEntry);
        try {
            updateEntryInSubtypeList(target, editedReadOnlyEntry);
            /*
             * this._allEntriesList does not need updating because it's pointing to the same entries contained
             * in the appropriate sub-type lists.
             */
        } finally {
            Entry editedEntry = convertToEntry(editedReadOnlyEntry);
            syncMasterTagListWith(editedEntry);
        }
    }
    // @@author

    // @@author A0126623L
    /**
     * Replaces the given entry {@code target} in the appropriate sub-type list with {@code editedReadOnlyEntry}.
     *
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     * @throws OverlappingEventException  if {@code target} is an event and would overlap with existing active events
     *              after being updated.
     * @throws OverlappingAndOverdueEventException if {@code editedReadOnlyEntry} is an event which overlaps with
     *              existing event(s) and is overdue.
     * @throws EntryOverdueException if {@code editedReadOnlyEntry} is overdue.
     * @see #syncMasterTagListWith(Entry)
     */
    private void updateEntryInSubtypeList(ReadOnlyEntry target, ReadOnlyEntry editedReadOnlyEntry)
            throws DuplicateEntryException, EntryNotFoundException, OverlappingEventException,
            OverlappingAndOverdueEventException, EntryOverdueException {
        if (target instanceof Event) {
            boolean overlappingEventPresent = editedReadOnlyEntry.isActive()
                                              && _eventList.hasOverlappingEventAfterUpdate(target,
                                                                                           editedReadOnlyEntry);
            Event editedEvent = (Event) editedReadOnlyEntry;
            boolean editedEventIsDue = editedEvent.isOverdue();

            _eventList.updateEntry(target, editedReadOnlyEntry);

            if (overlappingEventPresent && editedEventIsDue) {
                throw new OverlappingAndOverdueEventException();
            } else if (editedEventIsDue) {
                throw new EntryOverdueException();
            } else if (overlappingEventPresent) {
                throw new OverlappingEventException();
            }
        } else if (target instanceof Deadline) {
            Deadline editedDeadline = (Deadline) editedReadOnlyEntry;
            boolean editedDeadlineIsDue = editedDeadline.isOverdue();

            _deadlineList.updateEntry(target, editedReadOnlyEntry);

            if (editedDeadlineIsDue) {
                throw new EntryOverdueException();
            }
        } else {
            assert (target instanceof FloatingTask);
            _floatingTaskList.updateEntry(target, editedReadOnlyEntry);
        }
    }
    // @@author

    // @@author A0126623L
    /**
     * Type-cast a given ReadOnlyEntry object to an editable Entry object (i.e. event, deadline or floating
     * task).
     */
    private Entry convertToEntry(ReadOnlyEntry editedReadOnlyEntry) {
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
    // @@author

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
     * Removes entry from the appropriate lists (i.e. active, event, deadline, floating task lists).
     *
     * @param entryToRemove
     * @return boolean
     * @throws EntryNotFoundException
     */
    public boolean removeEntry(ReadOnlyEntry entryToRemove) throws EntryNotFoundException {
        return (_allEntriesList.remove(entryToRemove) && removeFromEntrySubtypeList(entryToRemove));
    }

    /**
     * Removes an entry from the appropriate list (i.e. event list, deadline list, floating task list).
     *
     * @param entryToRemove is of type Event, Deadline or FloatingTask
     * @return boolean
     */
    private boolean removeFromEntrySubtypeList(ReadOnlyEntry entryToRemove) throws EntryNotFoundException {
        if (entryToRemove instanceof Event) {
            return _eventList.remove(entryToRemove);
        } else if (entryToRemove instanceof Deadline) {
            return _deadlineList.remove(entryToRemove);
        } else {
            assert (entryToRemove instanceof FloatingTask);
            return _floatingTaskList.remove(entryToRemove);
        }
    }

    // @@author A0126623L
    /**
     * Clears all entries of a specific state within this {@code EntryBook}.
     * @param state of entries to be cleared.
     */
    public void clearStateSpecificEntries(Entry.State state) {

        ArrayList<ReadOnlyEntry> entriesToRemove = new ArrayList<>();

        // TODO by ChuaPingChan: Refactoring required - SLAP.
        for (ReadOnlyEntry readOnlyEntry : _allEntriesList) {
            if (readOnlyEntry.getState() == state) {
                entriesToRemove.add(readOnlyEntry);
            }
        }

        try {
            for (ReadOnlyEntry entryToRemove : entriesToRemove) {
                this.removeEntry(entryToRemove);
            }
        } catch (EntryNotFoundException enfe) {
            throw new AssertionError("If things are going as expected, "
                                     + " EntryNotFoundException shouldn't be arising here.");
        }
    }
    // @@author

    // @@author A0126623L
    /**
     * Marks an entry from the appropriate lists (i.e. active, event, deadline, floating task lists) as deleted.
     * Pre-condition: After the entry state is updated, it cannot be an exact match to an existing entry.
     *
     * @param entryToMark
     * @param newState      cannot be null
     * @return boolean
     * @throws DuplicateEntryException
     * @throws EntryNotFoundException
     * @throws OverlappingEventException if entryToChange overlaps with existing active events after being restored.
     * @throws OverlappingAndOverdueEventException
     * @throws EntryOverdueException
     */
    public void changeEntryState(ReadOnlyEntry entryToChange, Entry.State newState)
            throws DuplicateEntryException, EntryNotFoundException, OverlappingEventException,
            OverlappingAndOverdueEventException, EntryOverdueException {
        if (entryToChange instanceof Event) {
            // Checks if there will be overlapping entry after entryToChange is set to active.
            Entry prospectiveEntry = EntryBuilder.build(entryToChange);
            prospectiveEntry.setState(newState);
            boolean overlappingEventPresent = newState.equals(Entry.State.ACTIVE)
                                              && _eventList.hasOverlappingEventAfterUpdate(entryToChange,
                                                                                           prospectiveEntry);
            boolean eventIsDue = newState.equals(Entry.State.ACTIVE)
                                 && ((Event) prospectiveEntry).isOverdue();

            _eventList.changeEntryState(entryToChange, newState);

            if (overlappingEventPresent && eventIsDue) {
                throw new OverlappingAndOverdueEventException();
            } else if (overlappingEventPresent) {
                throw new OverlappingEventException();
            } else if (eventIsDue) {
                throw new EntryOverdueException();
            }

        } else if (entryToChange instanceof Deadline) {
            // Checks if there will be overlapping deadline after entryToChange is set to active.
            Entry prospectiveEntry = EntryBuilder.build(entryToChange);
            prospectiveEntry.setState(newState);
            boolean deadlineIsDue = newState.equals(Entry.State.ACTIVE)
                                    && ((Deadline) prospectiveEntry).isOverdue();

            _deadlineList.changeEntryState(entryToChange, newState);

            if (deadlineIsDue) {
                throw new EntryOverdueException();
            }
        } else {
            assert (entryToChange instanceof FloatingTask);
            _floatingTaskList.changeEntryState(entryToChange, newState);
        }
    }
    // @@author

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
        return _allEntriesList.asObservableList().size() + " entries, " + _tags.asObservableList().size()
               + " tags";
    }

    @Override
    public ObservableList<ReadOnlyEntry> getAllEntries() {
        return new UnmodifiableObservableList<>(_allEntriesList.asObservableList());
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
                   && this.getAllEntries().equals(((EntryBook) other).getAllEntries())
                   && this.getEventList().equals(((EntryBook) other).getEventList())
                   && this.getDeadlineList().equals(((EntryBook) other).getDeadlineList())
                   && this.getFloatingTaskList().equals(((EntryBook) other).getFloatingTaskList())
                   && this._tags.equalsOrderInsensitive(((EntryBook) other)._tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(getAllEntries(), getEventList(), getDeadlineList(),
                            getFloatingTaskList(), getTagList());
    }
}
