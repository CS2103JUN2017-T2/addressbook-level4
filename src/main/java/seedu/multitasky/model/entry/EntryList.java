package seedu.multitasky.model.entry;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.util.CollectionUtil;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.model.entry.util.Comparators;
import seedu.multitasky.model.util.EntryBuilder;

/**
 * A list of entries that does not allow nulls.
 * Supports a minimal set of list operations.
 *
 * @see Entry#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public abstract class EntryList implements Iterable<Entry> {

    protected final ObservableList<Entry> internalList = FXCollections.observableArrayList();

    protected Comparator<ReadOnlyEntry> comparator = Comparators.ENTRY_DEFAULT;

    // @@author A0126623L
    /**
     * Adds an entry to the list.
     *
     * @throws DuplicateEntryException if {@code toAdd} already exists in the list.
     */
    public void add(ReadOnlyEntry toAdd) throws DuplicateEntryException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            if (!isArchivedOrDeletedFloatingTask(toAdd)) {
                throw new DuplicateEntryException();
            }
        }
    };
    // @@author

    /**
     * Returns an unmodifiable copy of the ObservableList.
     */
    public UnmodifiableObservableList<Entry> asObservableList() {
        return new UnmodifiableObservableList<>(internalList);
    }

    /**
     * Returns true if the list contains an equivalent entry as the given
     * argument.
     */
    public boolean contains(ReadOnlyEntry toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
               || (other instanceof EntryList // instanceof handles nulls
                   && this.internalList.equals(((EntryList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public Iterator<Entry> iterator() {
        return internalList.iterator();
    }

    /**
     * Removes the equivalent entry from the list.
     *
     * @throws EntryNotFoundException if no such entry could be found in the list.
     */
    public boolean remove(ReadOnlyEntry toRemove) throws EntryNotFoundException {
        requireNonNull(toRemove);
        final boolean entryFoundAndDeleted = internalList.remove(toRemove);
        if (!entryFoundAndDeleted) {
            throw new EntryNotFoundException();
        }
        return entryFoundAndDeleted;
    }

    // @@author A0126623L
    /**
     * Changes the state (i.e. ACTIVE, ARCHIVED, DELETED) of an existing entry to {@code newState}.
     * @param entryToChange
     * @param newState
     * @throws DuplicateEntryException, EntryNotFoundException
     */
    public void changeEntryState(ReadOnlyEntry entryToChange, Entry.State newState)
            throws DuplicateEntryException, EntryNotFoundException {
        Entry editedEntry = EntryBuilder.build(entryToChange);
        editedEntry.setState(newState);
        this.updateEntry(entryToChange, editedEntry);
    }

    /**
     * Clears the current list of entries and add all elements from replacement.
     * The updated list of entries will contain the references to the elements in {@code replacement}.
     * @param replacement
     */
    public void setEntries(EntryList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    /**
     * Resets the data of the entry {@code target} in the list with that of the {@code editedEntry}.
     *
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     * @throws DuplicateEntryException if {@code editedEntry} already exists in the list.
     */
    public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry)
            throws DuplicateEntryException, EntryNotFoundException {
        requireNonNull(editedEntry);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new EntryNotFoundException();
        }

        Entry entryToUpdate = internalList.get(index);

        if (duplicatesPresentAfterEditing(target, editedEntry)) {
            if (!isArchivedOrDeletedFloatingTask(editedEntry)) {
                throw new DuplicateEntryException();
            }
        }

        entryToUpdate.resetData(editedEntry);
        // TODO: The code below is just a workaround to notify observers of the
        // updated entry.
        // The right way is to implement observable properties in the Entry class.
        // Then, EntryCard should then bind its text labels to those observable properties.
        internalList.set(index, entryToUpdate);
    }

    // @@author A0126623L
    /**
     * Checks if duplicate entries are present after editing an entry. The entry to be edited
     * is excluded from the duplicates-check.
     * @param target        entry to edit
     * @param editedEntry
     * @return boolean
     */
    private boolean duplicatesPresentAfterEditing(ReadOnlyEntry target, ReadOnlyEntry editedEntry) {
        ArrayList<Entry> prospectiveInternalList = createInternalListCopyWithoutTarget(target);
        return prospectiveInternalList.contains(editedEntry);
    }

    // @@author A0126623L
    private ArrayList<Entry> createInternalListCopyWithoutTarget(ReadOnlyEntry target) {
        ArrayList<Entry> copiedInternalList = new ArrayList<Entry>();
        for (Entry e : internalList) {
            if (!(e == target)) {
                copiedInternalList.add(e);
            }
        }
        return copiedInternalList;
    }

    // @@author A0126623L
    /**
     * Checks if the given entry is a floating task with the "archived" or "deleted" status, which is allowed in model.
     *
     * @param entry must not be null.
     * @return boolean
     */
    public static boolean isArchivedOrDeletedFloatingTask(ReadOnlyEntry entry) {
        Objects.requireNonNull(entry);
        return (entry instanceof FloatingTask
                && (entry.getState().equals(Entry.State.ARCHIVED)
                    || entry.getState().equals(Entry.State.DELETED)));
    }
    // @@author A0126623L

    // @@author A0125586X
    /**
     * Sorts the internal list using the comparator stored inside the class
     */
    protected void sortInternalList() {
        Collections.sort(internalList, comparator);
    }

    /**
     * Sets the comparator for this list, and sorts it using the comparator.
     */
    public void setComparator(Comparator<ReadOnlyEntry> comparator) {
        this.comparator = comparator;
        sortInternalList();
    }
    // @@author
}
