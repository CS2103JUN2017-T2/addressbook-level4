package seedu.multitasky.testutil;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.model.util.SampleDataUtil;

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
            cook = new FloatingTask(new Name("Learn to cook"), SampleDataUtil.getTagSet("goals"));

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
        // TODO add events and deadlines
        for (Entry entry: new TypicalEntries().getTypicalFloatingTasks()) {
            entryBook.addEntry(new FloatingTask(entry));
        }
    }

    public Entry[] getTypicalEntries() {
        return new Entry[] { cook, programming, hire };
    }

    //@@author A0125586X
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
