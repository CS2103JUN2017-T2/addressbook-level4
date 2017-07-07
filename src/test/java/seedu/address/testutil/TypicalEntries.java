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

    public final Entry cook, programming, hire, spectacles, clean, sell;

    // @@author A0125586X
    public TypicalEntries() {
        try {
            //CHECKSTYLE.OFF: LineLength
            cook = new EntryBuilder().withName("Learn to cook").withTags("goals").build();
            programming = new EntryBuilder().withName("Learn programming").withTags("lessons", "computer").build();
            hire = new EntryBuilder().withName("Hire an assistant").withTags("help").build();
            spectacles = new EntryBuilder().withName("Make new spectacles").withTags("health", "eyesight").build();
            clean = new EntryBuilder().withName("Clean up room").withTags("never").build();
            sell = new EntryBuilder().withName("Sell old things").withTags("sale", "clutter").build();
            //CHECKSTYLE.ON: LineLength

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
        return new Entry[] { cook, programming, hire};
    }

    public Entry[] getTypicalFloatingTasks() {
        return new Entry[] { cook, programming, hire };
    }

    // @@author A0126623L
    public EntryBook getTypicalEntryBook() {
        EntryBook entryBook = new EntryBook();
        loadEntryBookWithSampleData(entryBook);
        return entryBook;
    }
}
