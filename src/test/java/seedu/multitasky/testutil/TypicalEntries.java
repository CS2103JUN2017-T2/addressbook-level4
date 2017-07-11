package seedu.multitasky.testutil;

import java.util.Calendar;
import java.util.GregorianCalendar;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.util.EntryBuilder;

/**
 * Provides typical entries for use in testing.
 */
public class TypicalEntries {

    public static final Index INDEX_FIRST_ENTRY = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_ENTRY = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_ENTRY = Index.fromOneBased(3);

    // Events
    public final Entry dinner;
    // Deadlines
    public final Entry paper;
    // Floating tasks
    public final Entry cook, programming, hire, spectacles, clean, sell;

    // @@author A0125586X
    public TypicalEntries() {
        try {
            //CHECKSTYLE.OFF: LineLength
            // Events
            dinner = new EntryBuilder().withName("Dinner with family").withTags("cook")
                    .withStartDateAndTime(new GregorianCalendar(2017, Calendar.DECEMBER, 25, 19, 00))
                    .withEndDateAndTime(new GregorianCalendar(2017, Calendar.DECEMBER, 25, 21, 00)).build();
            // Deadlines
            paper = new EntryBuilder().withName("CS2103 finals").withTags("school", "study")
                    .withEndDateAndTime(new GregorianCalendar(2017, Calendar.JULY, 28, 10, 0)).build();
            // Floating tasks
            cook = new EntryBuilder().withName("Learn to cook").withTags("goals").build();
            programming = new EntryBuilder().withName("Learn programming").withTags("lessons", "computer").build();
            hire = new EntryBuilder().withName("Hire an assistant").withTags("help").build();
            spectacles = new EntryBuilder().withName("Make new spectacles").withTags("health", "eyesight").build();
            clean = new EntryBuilder().withName("Clean up room").withTags("never").build();
            sell = new EntryBuilder().withName("Sell old things").withTags("sale", "clutter").build();
            //CHECKSTYLE.ON: LineLength

        } catch (Exception e) {
            throw new AssertionError("Sample data cannot be invalid", e);
        }
    }
    // @@author

    // @@author A0126623L
    public static void loadEntryBookWithSampleData(EntryBook entryBook) {
        try {
            for (Entry entry: new TypicalEntries().getTypicalEvents()) {
                entryBook.addEntry(new Event(entry));
            }
            for (Entry entry: new TypicalEntries().getTypicalFloatingTasks()) {
                entryBook.addEntry(new FloatingTask(entry));
            }
        } catch (Exception e) {
            assert false : "Sample entries cannot have errors";
        }

    }

    public Entry[] getTypicalEntries() {
        return new Entry[] { cook, programming, hire };
    }

    //@@author A0125586X
    public Entry[] getTypicalEvents() {
        return new Entry[] { dinner };
    }
    public Entry[] getTypicalFloatingTasks() {
        return new Entry[] { cook, programming, hire };
    }
    //@@author

    // @@author A0126623L
    public EntryBook getTypicalEntryBook() {
        EntryBook entryBook = new EntryBook();
        loadEntryBookWithSampleData(entryBook);
        return entryBook;
    }
}
