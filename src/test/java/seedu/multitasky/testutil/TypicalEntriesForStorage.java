package seedu.multitasky.testutil;

import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryOverdueException;
import seedu.multitasky.model.entry.exceptions.OverlappingAndOverdueEventException;
import seedu.multitasky.model.entry.exceptions.OverlappingEventException;
import seedu.multitasky.model.util.EntryBuilder;

//@@author A0132788U
/**
 * Provides entries for use in testing storage.
 */

public class TypicalEntriesForStorage {

    public final Entry eat, journal, decorate, project;

    public TypicalEntriesForStorage() {
        try {
            eat = EntryBuilder.build("Eat flaxseed",
                                     "health");
            journal = EntryBuilder.build("Write experiences in diary",
                                         "writing");
            decorate = EntryBuilder.build("Decorate new room",
                                          "organize");
            project = EntryBuilder.build("Finish 2103 project",
                                         "important");
        } catch (Exception e) {
            throw new AssertionError("Sample data cannot be invalid", e);
        }
    }

    public static void loadEntryBookWithSampleData(EntryBook entryBook) throws DuplicateEntryException {
        for (Entry entry : SampleEntries.getSampleActiveFloatingTasks()) {
            try {
                entryBook.addEntry(new FloatingTask(entry));
            } catch (OverlappingEventException oee) {
                // Ignore overlapping events when loading entry book for testing.
            } catch (OverlappingAndOverdueEventException e) {
                // Do nothing. Overlapping and overdue entries are fine.
            } catch (EntryOverdueException e) {
                // Do nothing. Overdue entries are fine.
            }
        }
    }

    public Entry[] getTypicalEntries() {
        return new Entry[] { project, journal, eat };
    }

    public EntryBook getTypicalEntryBook() throws DuplicateEntryException {
        EntryBook entryBook = new EntryBook();
        loadEntryBookWithSampleData(entryBook);
        return entryBook;
    }
}
