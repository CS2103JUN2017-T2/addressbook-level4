package seedu.multitasky.testutil;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.OverlappingEventException;
import seedu.multitasky.model.tag.Tag;

/**
 * A utility class to help with building EntryBook objects.
 */
public class EntryBookBuilder {

    private EntryBook entryBook;

    public EntryBookBuilder() {
        entryBook = new EntryBook();
    }

    public EntryBookBuilder(EntryBook entryBook) {
        this.entryBook = entryBook;
    }

    public EntryBookBuilder withEntry(Entry entry) throws DuplicateEntryException {
        try {
            entryBook.addEntry(entry);
        } catch (OverlappingEventException oee) {
            // Ignore overlapping events in building entrybook for testing.
        }
        return this;
    }

    public EntryBookBuilder withTag(String tagName) throws IllegalValueException {
        entryBook.addTag(new Tag(tagName));
        return this;
    }

    public EntryBook build() {
        return entryBook;
    }
}
