package seedu.address.model.entry;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.entry.exceptions.DuplicatePersonException;
import seedu.address.model.entry.exceptions.PersonNotFoundException;

/**
 * A list of entries that does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Entry#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class EntryList implements Iterable<Entry> {

    private final ObservableList<Entry> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent entry as the given argument.
     */
    public boolean contains(ReadOnlyEntry toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds an entry to the list.
     */
    public void add(ReadOnlyEntry toAdd) {
        requireNonNull(toAdd);

        internalList.add(new Entry(toAdd));
    }

    /**
     * Replaces the entry {@code target} in the list with {@code editedEntry}.
     *
     * @throws EntryNotFoundException if {@code target} could not be found in the list.
     */
    public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry)
            throws EntryNotFoundException {
        requireNonNull(editedEntry);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new EntryNotFoundException();
        }
        
        // TODO by Ping Chan: This line can probably be removed later.
        Entry entryToUpdate = internalList.get(index);

        entryToUpdate.resetData(editedEntry);
        // TODO: The code below is just a workaround to notify observers of the updated entry.
        // The right way is to implement observable properties in the Entry class.
        // Then, EntryCard should then bind its text labels to those observable properties.
        internalList.set(index, entryToUpdate);
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
        return EntryFoundAndDeleted;
    }

    public void setEntries(EntryList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setEntries(List<? extends ReadOnlyEntry> entries) {
        final EntryList replacement = new EntryList();
        for (final ReadOnlyEntry entry : entries) {
            replacement.add(new Entry(entry));
        }
        setEntries(replacement);
    }

    public UnmodifiableObservableList<Entry> asObservableList() {
        return new UnmodifiableObservableList<>(internalList);
    }

    @Override
    public Iterator<Entry> iterator() {
        return internalList.iterator();
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
}
