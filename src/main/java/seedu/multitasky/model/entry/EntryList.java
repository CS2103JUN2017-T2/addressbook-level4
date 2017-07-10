package seedu.multitasky.model.entry;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.util.CollectionUtil;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

/**
 * A list of entries that does not allow nulls.
 * Supports a minimal set of list operations.
 *
 * @see Entry#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public abstract class EntryList implements Iterable<Entry> {

    protected final ObservableList<Entry> internalList = FXCollections.observableArrayList();

    /**
     * Adds an entry to the list.
     */
    public void add(ReadOnlyEntry toAdd) throws DuplicateEntryException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateEntryException();
        }
    };

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

    /**
     * Clears the current list of entries and add all elements from replacement.
     *
     * @param replacement
     */
    public void setEntries(EntryList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    /**
     * Resets the data of the entry {@code target} in the list with that of the {@code editedEntry}.
     *
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     */
    public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry) throws DuplicateEntryException,
                                                                             EntryNotFoundException {
        requireNonNull(editedEntry);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new EntryNotFoundException();
        }

        Entry entryToUpdate = internalList.get(index);

        if (!entryToUpdate.equals(editedEntry) && internalList.contains(editedEntry)) {
            throw new DuplicateEntryException();
        }

        entryToUpdate.resetData(editedEntry);
        // TODO: The code below is just a workaround to notify observers of the
        // updated entry.
        // The right way is to implement observable properties in the Entry class.
        // Then, EntryCard should then bind its text labels to those observable properties.
        internalList.set(index, entryToUpdate);
    }
}
