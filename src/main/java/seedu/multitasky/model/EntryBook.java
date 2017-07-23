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
    private final MiscEntryList allEntriesList;
    private final EventList eventList;
    private final DeadlineList deadlineList;
    private final FloatingTaskList floatingTaskList;
    private final UniqueTagList tags;

    // @@author A0126623L
    public EntryBook() {
        allEntriesList = new MiscEntryList();
        eventList = new EventList();
        deadlineList = new DeadlineList();
        floatingTaskList = new FloatingTaskList();
        tags = new UniqueTagList();
    }

    /**
     * Creates an EntryBook using the Entries and Tags in the {@code toBeCopied}
     */
    public EntryBook(ReadOnlyEntryBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

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
        syncMasterTagListWith(allEntriesList);
    }

    /*****************************
     * List overwrite operations *
     *****************************/

    /**
     * Fills up {@code allEntriesList} with existing events, deadlines
     * and floating tasks in the {@code EntryBook}'s.
     * @throws DuplicateEntryException
     */
    private void setAllEntriesList() throws DuplicateEntryException {
        this.allEntriesList.setEntries(new MiscEntryList());
        for (ReadOnlyEntry entry : eventList) {
            this.allEntriesList.add(entry);
        }
        for (ReadOnlyEntry entry : deadlineList) {
            this.allEntriesList.add(entry);
        }
        for (ReadOnlyEntry entry : floatingTaskList) {
            this.allEntriesList.add(entry);
        }
    }

    private void setEventList(List<? extends ReadOnlyEntry> entries) throws DuplicateEntryException {
        this.eventList.setEntries(entries);
    }

    private void setDeadlineList(List<? extends ReadOnlyEntry> entries) throws DuplicateEntryException {
        this.deadlineList.setEntries(entries);
    }

    private void setFloatingTaskList(List<? extends ReadOnlyEntry> entries) throws DuplicateEntryException {
        this.floatingTaskList.setEntries(entries);
    }

    private void setTags(Collection<Tag> tags) throws UniqueTagList.DuplicateTagException {
        this.tags.setTags(tags);
    }

    // @@author A0126623L
    /**************************
     * Entry-level operations *
     **************************/

    /**
     * Adds an entry to the entry book.
     * Creates the appropriate sub-type of the new entry and adds its reference to the active entry list, as
     * well as one of event/deadline/floating task list.
     * Also checks the new entry's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the entry to point to those in {@link #tags}.
     * @throws EntryOverdueException
     * @throws OverlappingAndOverdueEventException
     */
    public void addEntry(ReadOnlyEntry entryToAdd)
            throws DuplicateEntryException, OverlappingEventException,
            OverlappingAndOverdueEventException, EntryOverdueException {
        try {
            addToEntrySubtypeList(entryToAdd);
        } catch (OverlappingEventException | OverlappingAndOverdueEventException | EntryOverdueException e) {
            // TODO: Figure out how to prevent repeating lines within this catch block and outside.
            Entry newEntry = convertToEntry(entryToAdd);
            syncMasterTagListWith(newEntry);
            allEntriesList.add(newEntry);
            throw e;
        }
        Entry newEntry = convertToEntry(entryToAdd);
        syncMasterTagListWith(newEntry);
        allEntriesList.add(newEntry);
    }

    /**
     * Add a given ReadOnlyEntry to one of either active, deadline or floating task list.
     * @param newEntry to add
     * @throws DuplicateEntryException
     * @throws OverlappingEventException
     * @throws OverlappingAndOverdueEventException
     * @throws EntryOverdueException
     */
    private void addToEntrySubtypeList(ReadOnlyEntry newEntry)
            throws DuplicateEntryException, OverlappingEventException, OverlappingAndOverdueEventException,
            EntryOverdueException {
        if (newEntry instanceof Event) {
            addToEventListWithOverlapAndOverdueCheck(newEntry);
        } else if (newEntry instanceof Deadline) {
            addToDeadlineListWithOverdueCheck(newEntry);
        } else {
            assert (newEntry instanceof FloatingTask);
            floatingTaskList.add(newEntry);
        }
    }

    private void addToEventListWithOverlapAndOverdueCheck(ReadOnlyEntry newEntry)
            throws DuplicateEntryException, OverlappingAndOverdueEventException, OverlappingEventException,
            EntryOverdueException {
        boolean overlappingEventPresent = eventList.hasOverlappingEvent(newEntry);
        boolean eventIsDue = ((Event) newEntry).isOverdue();

        eventList.add(newEntry);

        overlappingOrOverdueEventAlarm(overlappingEventPresent, eventIsDue);
    }

    private void addToDeadlineListWithOverdueCheck(ReadOnlyEntry newEntry)
            throws DuplicateEntryException, EntryOverdueException {
        boolean deadlineIsDue = ((Deadline) newEntry).isOverdue();

        deadlineList.add(newEntry);

        if (deadlineIsDue) {
            throw new EntryOverdueException();
        }
    }

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
            /**
             * this.allEntriesList does not need updating because it's pointing to the same entries contained
             * in the appropriate sub-type lists.
             */
        } catch (EntryNotFoundException | OverlappingEventException
                 | OverlappingAndOverdueEventException | EntryOverdueException e) {
            // TODO: Figure out how to prevent repeating lines within this catch block and outside.
            Entry editedEntry = convertToEntry(editedReadOnlyEntry);
            syncMasterTagListWith(editedEntry);
            throw e;
        }
        Entry editedEntry = convertToEntry(editedReadOnlyEntry);
        syncMasterTagListWith(editedEntry);
    }

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
            editEventWithOverlappingAndOverdueCheck(target, editedReadOnlyEntry);
        } else if (target instanceof Deadline) {
            editDeadlineWithOverdueCheck(target, editedReadOnlyEntry);
        } else {
            assert (target instanceof FloatingTask);
            floatingTaskList.updateEntry(target, editedReadOnlyEntry);
        }
    }

    private void editEventWithOverlappingAndOverdueCheck(ReadOnlyEntry target,
                                                         ReadOnlyEntry editedReadOnlyEntry)
            throws DuplicateEntryException, EntryNotFoundException, OverlappingAndOverdueEventException,
            EntryOverdueException, OverlappingEventException {
        boolean overlappingEventPresent = editedReadOnlyEntry.isActive()
                                          && eventList.hasOverlappingEventAfterUpdate(target,
                                                                                      editedReadOnlyEntry);
        Event editedEvent = (Event) editedReadOnlyEntry;
        boolean editedEventIsDue = editedEvent.isOverdue();

        eventList.updateEntry(target, editedReadOnlyEntry);

        overlappingOrOverdueEventAlarm(overlappingEventPresent, editedEventIsDue);
    }

    private void editDeadlineWithOverdueCheck(ReadOnlyEntry target, ReadOnlyEntry editedReadOnlyEntry)
            throws DuplicateEntryException, EntryNotFoundException, EntryOverdueException {
        Deadline editedDeadline = (Deadline) editedReadOnlyEntry;
        boolean editedDeadlineIsDue = editedDeadline.isOverdue();

        deadlineList.updateEntry(target, editedReadOnlyEntry);

        if (editedDeadlineIsDue) {
            throw new EntryOverdueException();
        }
    }

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

    /**
     * Removes entry from the appropriate lists (i.e. active, event, deadline, floating task lists).
     *
     * @param entryToRemove
     * @return boolean
     * @throws EntryNotFoundException
     */
    public boolean removeEntry(ReadOnlyEntry entryToRemove) throws EntryNotFoundException {
        return (allEntriesList.remove(entryToRemove) && removeFromEntrySubtypeList(entryToRemove));
    }

    /**
     * Removes an entry from the appropriate list (i.e. event list, deadline list, floating task list).
     *
     * @param entryToRemove is of type Event, Deadline or FloatingTask
     * @return boolean
     */
    private boolean removeFromEntrySubtypeList(ReadOnlyEntry entryToRemove) throws EntryNotFoundException {
        if (entryToRemove instanceof Event) {
            return eventList.remove(entryToRemove);
        } else if (entryToRemove instanceof Deadline) {
            return deadlineList.remove(entryToRemove);
        } else {
            assert (entryToRemove instanceof FloatingTask);
            return floatingTaskList.remove(entryToRemove);
        }
    }

    /**
     * Clears all entries of a specific state within this {@code EntryBook}.
     * @param state of entries to be cleared.
     */
    public void clearStateSpecificEntries(Entry.State state) {

        ArrayList<ReadOnlyEntry> entriesToRemove = new ArrayList<>();

        collectStateSpecificEntriesToRemove(state, entriesToRemove);

        removeEntriesInList(entriesToRemove);
    }

    private void collectStateSpecificEntriesToRemove(Entry.State state,
                                                     ArrayList<ReadOnlyEntry> entriesToRemove) {
        for (ReadOnlyEntry readOnlyEntry : allEntriesList) {
            if (readOnlyEntry.getState() == state) {
                entriesToRemove.add(readOnlyEntry);
            }
        }
    }

    private void removeEntriesInList(List<ReadOnlyEntry> entriesToRemove) throws AssertionError {
        try {
            for (ReadOnlyEntry entryToRemove : entriesToRemove) {
                this.removeEntry(entryToRemove);
            }
        } catch (EntryNotFoundException enfe) {
            throw new AssertionError("If things are going as expected, "
                                     + " EntryNotFoundException shouldn't be arising here.");
        }
    }

    /**
     * Marks an entry from the appropriate lists (i.e. active, event, deadline, floating task lists)
     * as {@code deleted} or {@code archived}.
     * Pre-condition: After the entry state is updated, it cannot result in a duplicate with an existing
     * active entry.
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
            changeEventStateWithOverlapAndOverdueCheck(entryToChange, newState);
        } else if (entryToChange instanceof Deadline) {
            changeDeadlineStateWithOverdueCheck(entryToChange, newState);
        } else {
            assert (entryToChange instanceof FloatingTask);
            floatingTaskList.changeEntryState(entryToChange, newState);
        }
    }

    /**
     * Checks if a change of the {@code State} of a given {@code Event} will result in an overdue
     * event or an event that overlaps with existing active events. If these do not happen, the
     * state change will be made.
     * Pre-condition: {@code entryToChange} must be an Event.
     * @param entryToChange
     * @param newState
     * @throws DuplicateEntryException
     * @throws EntryNotFoundException
     * @throws OverlappingAndOverdueEventException
     * @throws OverlappingEventException
     * @throws EntryOverdueException
     */
    private void changeEventStateWithOverlapAndOverdueCheck(ReadOnlyEntry entryToChange, Entry.State newState)
            throws DuplicateEntryException, EntryNotFoundException, OverlappingAndOverdueEventException,
            OverlappingEventException, EntryOverdueException {
        if (!(entryToChange instanceof Event)) {
            throw new AssertionError("entryToChange must be of type Event.");
        }
        Entry prospectiveEntry = EntryBuilder.build(entryToChange);
        prospectiveEntry.setState(newState);
        boolean overlappingEventPresent = newState.equals(Entry.State.ACTIVE)
                                          && eventList.hasOverlappingEventAfterUpdate(entryToChange,
                                                                                      prospectiveEntry);
        boolean eventIsDue = newState.equals(Entry.State.ACTIVE)
                             && ((Event) prospectiveEntry).isOverdue();

        eventList.changeEntryState(entryToChange, newState);

        overlappingOrOverdueEventAlarm(overlappingEventPresent, eventIsDue);
    }

    /**
     * Checks if a change of the {@code State} of a given {@code Deadline} will result in an overdue
     * deadline. If this do not happen, the state change will be made.
     * Pre-condition: {@code entryToChange} must be a Deadline.
     * @param entryToChange
     * @param newState
     * @throws DuplicateEntryException
     * @throws EntryNotFoundException
     * @throws EntryOverdueException
     */
    private void changeDeadlineStateWithOverdueCheck(ReadOnlyEntry entryToChange, Entry.State newState)
            throws DuplicateEntryException, EntryNotFoundException, EntryOverdueException {
        if (!(entryToChange instanceof Deadline)) {
            throw new AssertionError("entryToChange must be of type Deadline.");
        }
        Entry prospectiveEntry = EntryBuilder.build(entryToChange);
        prospectiveEntry.setState(newState);
        boolean deadlineIsDue = newState.equals(Entry.State.ACTIVE)
                                && ((Deadline) prospectiveEntry).isOverdue();

        deadlineList.changeEntryState(entryToChange, newState);

        if (deadlineIsDue) {
            throw new EntryOverdueException();
        }
    }

    /**
     * Throws the appropriate exceptions if overlapping and overdue event exists.
     * @param overlappingEventPresent
     * @param eventIsDue
     * @throws OverlappingAndOverdueEventException
     * @throws OverlappingEventException
     * @throws EntryOverdueException
     */
    private void overlappingOrOverdueEventAlarm(boolean overlappingEventPresent, boolean eventIsDue)
            throws OverlappingAndOverdueEventException, OverlappingEventException, EntryOverdueException {
        if (overlappingEventPresent && eventIsDue) {
            throw new OverlappingAndOverdueEventException();
        } else if (overlappingEventPresent) {
            throw new OverlappingEventException();
        } else if (eventIsDue) {
            throw new EntryOverdueException();
        }
    }

    /************************
     * Tag-level operations *
     ************************/

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    // @@author A0126623L-reused
    /**
     * Ensures that every tag in this entry:
     * - exists in the master list {@link #tags}
     * - points to a Tag object in the master list
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
     * - exists in the master list {@link #tags}
     * - points to a Tag object in the master list
     *
     * @see #syncMasterTagListWith(Entry)
     */
    private void syncMasterTagListWith(EntryList entries) {
        entries.forEach(this::syncMasterTagListWith);
    }

    /*****************
     *  util methods *
     *****************/

    // @@author A0125586X
    public void setComparators(Comparator<ReadOnlyEntry> eventComparator,
                               Comparator<ReadOnlyEntry> deadlineComparator,
                               Comparator<ReadOnlyEntry> floatingTaskComparator) {
        eventList.setComparator(eventComparator);
        deadlineList.setComparator(deadlineComparator);
        floatingTaskList.setComparator(floatingTaskComparator);
    }

    // @@author A0126623L
    /**
     * @return String Information of the number of active entries and tags present.
     */
    @Override
    public String toString() {
        return allEntriesList.asObservableList().size() + " entries, " + tags.asObservableList().size()
               + " tags";
    }

    @Override
    public ObservableList<ReadOnlyEntry> getAllEntries() {
        return new UnmodifiableObservableList<>(allEntriesList.asObservableList());
    }

    @Override
    public ObservableList<ReadOnlyEntry> getEventList() {
        return new UnmodifiableObservableList<>(eventList.asObservableList());
    }

    @Override
    public ObservableList<ReadOnlyEntry> getDeadlineList() {
        return new UnmodifiableObservableList<>(deadlineList.asObservableList());
    }

    @Override
    public ObservableList<ReadOnlyEntry> getFloatingTaskList() {
        return new UnmodifiableObservableList<>(floatingTaskList.asObservableList());
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return new UnmodifiableObservableList<>(tags.asObservableList());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
               || (other instanceof EntryBook // instanceof handles nulls
                   && this.getAllEntries().equals(((EntryBook) other).getAllEntries())
                   && this.getEventList().equals(((EntryBook) other).getEventList())
                   && this.getDeadlineList().equals(((EntryBook) other).getDeadlineList())
                   && this.getFloatingTaskList().equals(((EntryBook) other).getFloatingTaskList())
                   && this.tags.equalsOrderInsensitive(((EntryBook) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(getAllEntries(), getEventList(), getDeadlineList(),
                            getFloatingTaskList(), getTagList());
    }
}
