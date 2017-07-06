package seedu.address.testutil;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.EntryBook;
import seedu.address.model.entry.Entry;

//@@author A0125586X
/**
 * Provides typical entries for use in testing.
 */
public class TypicalEntries {

    public static final Index INDEX_FIRST_ENTRY = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_ENTRY = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_ENTRY = Index.fromOneBased(3);

    public final Entry lunch, dog, cat, novel, groceries, code, tasks, test, travis;

    // @@author A0125586X
    public TypicalEntries() {
        try {
            lunch = new EntryBuilder().withName("Take lunch to work").withTags("food").build();
            dog = new EntryBuilder().withName("Take dog for walk").withTags("pets", "exercise").build();
            cat = new EntryBuilder().withName("Fill up cat food bowl").withTags("pets").build();
            novel = new EntryBuilder().withName("Write novel").build();
            groceries = new EntryBuilder().withName("Buy groceries").build();
            code = new EntryBuilder().withName("Refactor code").build();
            tasks = new EntryBuilder().withName("Write two more tasks").build();
            test = new EntryBuilder().withName("Import test cases").build();
            travis = new EntryBuilder().withName("Scold Travis").build();

        } catch (IllegalValueException e) {
            throw new AssertionError("Sample data cannot be invalid", e);
        }
    }

    // @@author A0126623L
    public static void loadEntryBookWithSampleData(EntryBook entryBook) {
        for (Entry entry : new TypicalEntries().getTypicalEntries()) {
            entryBook.addEntry(new Entry(entry));
        }
    }

    public Entry[] getTypicalEntries() {
        return new Entry[] { lunch, dog, cat, novel, groceries, code, tasks, test, travis };
    }

    // @@author A0126623L
    public EntryBook getTypicalEntryBook() {
        EntryBook entryBook = new EntryBook();
        loadEntryBookWithSampleData(entryBook);
        return entryBook;
    }
}
