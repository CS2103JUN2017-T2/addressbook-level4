package seedu.multitasky.testutil;

import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.util.EntryBuilder;

//@@author A0132788U
/**
 * Provides entries for use in testing storage.
 */

public class TypicalEntriesForStorage {

    public final Entry eat, journal, decorate, project;

    public TypicalEntriesForStorage() {
        try {
            eat = new EntryBuilder().withName("Eat flaxseed").withTags("health").build();
            journal = new EntryBuilder().withName("Write experiences in diary").withTags("writing").build();
            decorate = new EntryBuilder().withName("Decorate new room").withTags("organize").build();
            // Manually added
            project = new EntryBuilder().withName("Finish 2103 project").withTags("important").build();
        } catch (Exception e) {
            throw new AssertionError("Sample data cannot be invalid", e);
        }
    }

    public static void loadEntryBookWithSampleData(EntryBook entryBook) {
        for (Entry entry : new TypicalEntries().getTypicalFloatingTasks()) {
            entryBook.addEntry(new FloatingTask(entry));
        }
    }

    public Entry[] getTypicalEntries() {
        return new Entry[] { project, journal, eat };
    }

    public EntryBook getTypicalEntryBook() {
        EntryBook entryBook = new EntryBook();
        loadEntryBookWithSampleData(entryBook);
        return entryBook;
    }
}
