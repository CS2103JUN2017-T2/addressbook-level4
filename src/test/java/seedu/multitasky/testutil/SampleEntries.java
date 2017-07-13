package seedu.multitasky.testutil;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.util.EntryBuilder;

/**
 * Provides sample entries for use in testing.
 */
public class SampleEntries {

    public static final Index INDEX_FIRST_ENTRY = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_ENTRY = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_ENTRY = Index.fromOneBased(3);

    // @@author A0125586X
    // Events
    public static final Entry DINNER, CAT, MOVIE, OPENING;
    // Deadlines
    public static final Entry TAX, PAPER, SUBMISSION, UPGRADE;
    // Floating tasks
    public static final Entry COOK, PROGRAMMING, HIRE, SPECTACLES, CLEAN, SELL;

    static {
        try {
            // Events
            DINNER      = EntryBuilder.build("Dinner with family",
                          new GregorianCalendar(2017, Calendar.DECEMBER, 25, 19, 00),
                          new GregorianCalendar(2017, Calendar.DECEMBER, 25, 21, 00),
                          "cook");
            CAT         = EntryBuilder.build("Feed the cat",
                          new GregorianCalendar(2017, Calendar.JULY, 12, 19, 00),
                          new GregorianCalendar(2017, Calendar.JULY, 12, 21, 00),
                          "pet");
            MOVIE       = EntryBuilder.build("Watch Spiderman",
                          new GregorianCalendar(2017, Calendar.JULY, 15, 15, 00),
                          new GregorianCalendar(2017, Calendar.JULY, 15, 17, 30),
                          "marvel", "comics");
            OPENING     = EntryBuilder.build("Attend exhibition opening",
                          new GregorianCalendar(2017, Calendar.OCTOBER, 7, 18, 00),
                          new GregorianCalendar(2017, Calendar.OCTOBER, 7, 21, 00),
                          "tuxedo", "suit");

            // Deadlines
            TAX         = EntryBuilder.build("Submit tax forms",
                          new GregorianCalendar(2017, Calendar.JULY, 1, 00, 00),
                          "money", "pay");
            PAPER       = EntryBuilder.build("CS2103 finals",
                          new GregorianCalendar(2017, Calendar.JULY, 28, 10, 0),
                          "school", "exam", "study");
            SUBMISSION  = EntryBuilder.build("Submit assignment",
                          new GregorianCalendar(2017, Calendar.JULY, 12, 16, 00),
                          "school");
            UPGRADE     = EntryBuilder.build("Upgrade computer",
                          new GregorianCalendar(2017, Calendar.DECEMBER, 1, 00, 00),
                          "personal", "project");

            // Floating tasks
            COOK        = EntryBuilder.build("Learn to cook",
                          "goals");
            PROGRAMMING = EntryBuilder.build("Learn programming",
                          "lessons", "computer");
            HIRE        = EntryBuilder.build("Hire an assistant",
                          "help");
            SPECTACLES  = EntryBuilder.build("Make new spectacles",
                          "health", "eyesight");
            CLEAN       = EntryBuilder.build("Clean up room",
                          "never", "hopefully");
            SELL        = EntryBuilder.build("Sell old things",
                          "sale", "clutter");

        } catch (Exception e) {
            throw new AssertionError("Sample data cannot be invalid", e);
        }
    }

    public static Entry[] getSampleEntries() {
        return new Entry[] {
            DINNER, TAX, PAPER, COOK, PROGRAMMING
        };
    }

    public static Entry[] getSampleEvents() {
        return new Entry[] { DINNER };
    }

    public static Entry[] getSampleDeadlines() {
        return new Entry[] { TAX, PAPER };
    }

    public static Entry[] getSampleFloatingTasks() {
        return new Entry[] { COOK, PROGRAMMING };
    }
    // @@author

    // @@author A0126623L
    /**
     * Adds sample entries to the provided EntryBook
     */
    public static void loadEntryBookWithSampleData(EntryBook entryBook) {
        Objects.requireNonNull(entryBook);
        try {
            for (Entry entry: getSampleEntries()) {
                entryBook.addEntry(EntryBuilder.build(entry));
            }
        } catch (Exception e) {
            assert false : "Sample entries cannot have errors";
        }
    }
    //@@author

    // @@author A0126623L
    public static EntryBook getSampleEntryBook() {
        EntryBook entryBook = new EntryBook();
        loadEntryBookWithSampleData(entryBook);
        return entryBook;
    }
    //@@author
}
