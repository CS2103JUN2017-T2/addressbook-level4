package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.EntryBook;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.exceptions.DuplicateEntryException;
import seedu.address.model.tag.Tag;

//@@author A0126623L
/**
 * A utility class to help with building Entrybook objects. Example usage: <br>
 * {@code EntryBook ab = new EntryBookBuilder().withEntry("John", "Doe").withTag("Friend").build();}
 */
public class EntryBookBuilder {

	private EntryBook entryBook;

	public EntryBookBuilder() {
		entryBook = new EntryBook();
	}

	public EntryBookBuilder(EntryBook entryBook) {
		this.entryBook = entryBook;
	}

	public EntryBookBuilder withEntry(Entry entry) {
		entryBook.addEntry(entry);
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
