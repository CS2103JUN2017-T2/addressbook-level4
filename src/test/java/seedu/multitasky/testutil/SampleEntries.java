package seedu.multitasky.testutil;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryOverdueException;
import seedu.multitasky.model.entry.exceptions.OverlappingAndOverdueEventException;
import seedu.multitasky.model.entry.exceptions.OverlappingEventException;
import seedu.multitasky.model.util.EntryBuilder;

/**
 * Provides sample entries for use in testing.
 */
public class SampleEntries {

    public static final Index INDEX_FIRST_ENTRY = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_ENTRY = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_ENTRY = Index.fromOneBased(3);

    // @@author A0125586X
    // Events (active)
    public static final Entry DINNER, CAT, MOVIE, OPENING;
    // Events (archive)
    public static final Entry ASSIGNMENT, SHOPPING;
    // Events (bin)
    public static final Entry GYM, PAINT;
    // Deadlines (active)
    public static final Entry TAX, PAPER, SUBMISSION, UPGRADE;
    // Deadlines (archive)
    public static final Entry QUIZ, REGISTRATION;
    // Deadlines (bin)
    public static final Entry APPOINTMENT, EMAIL;
    // Floating tasks (active)
    public static final Entry COOK, PROGRAMMING, HIRE, SPECTACLES, CLEAN, SELL;
    // Floating tasks (archive)
    public static final Entry EXERCISE, BOOK;
    // Floating tasks (bin)
    public static final Entry BAKE, SING;

    static {
        try {
            // Events (active)
            DINNER = EntryBuilder.build("Dinner with family",
                                        new GregorianCalendar(2017, Calendar.DECEMBER, 25, 19, 00),
                                        new GregorianCalendar(2017, Calendar.DECEMBER, 25, 21, 00),
                                        "cook");
            CAT = EntryBuilder.build("Feed the cat",
                                     new GregorianCalendar(2017, Calendar.JULY, 12, 19, 00),
                                     new GregorianCalendar(2017, Calendar.JULY, 12, 21, 00),
                                     "pet");
            MOVIE = EntryBuilder.build("Watch Spiderman",
                                       new GregorianCalendar(2017, Calendar.JULY, 15, 15, 00),
                                       new GregorianCalendar(2017, Calendar.JULY, 15, 17, 30),
                                       "marvel", "comics");
            OPENING = EntryBuilder.build("Attend exhibition opening",
                                         new GregorianCalendar(2017, Calendar.OCTOBER, 7, 18, 00),
                                         new GregorianCalendar(2017, Calendar.OCTOBER, 7, 21, 00),
                                         "tuxedo", "suit");
            // Events (archive)
            ASSIGNMENT = EntryBuilder.build("Finish cs2103 assignment",
                                            new GregorianCalendar(2017, Calendar.SEPTEMBER, 9, 19, 00),
                                            new GregorianCalendar(2017, Calendar.SEPTEMBER, 9, 21, 00),
                                            "school");
            ASSIGNMENT.setState(Entry.State.ARCHIVED);
            SHOPPING = EntryBuilder.build("Buy grocery",
                                          new GregorianCalendar(2017, Calendar.NOVEMBER, 13, 19, 00),
                                          new GregorianCalendar(2017, Calendar.NOVEMBER, 13, 21, 00),
                                          "weekends");
            SHOPPING.setState(Entry.State.ARCHIVED);
            // Events (bin)
            GYM = EntryBuilder.build("Go to gym",
                                     new GregorianCalendar(2017, Calendar.SEPTEMBER, 11, 13, 00),
                                     new GregorianCalendar(2017, Calendar.SEPTEMBER, 11, 15, 00),
                                     "health");
            GYM.setState(Entry.State.DELETED);
            PAINT = EntryBuilder.build("Attend painting workshop",
                                       new GregorianCalendar(2017, Calendar.NOVEMBER, 21, 14, 00),
                                       new GregorianCalendar(2017, Calendar.NOVEMBER, 21, 16, 00),
                                       "hobby");
            PAINT.setState(Entry.State.DELETED);

            // Deadlines (active)
            TAX = EntryBuilder.build("Submit tax forms",
                                     new GregorianCalendar(2017, Calendar.JULY, 1, 00, 00),
                                     "money", "pay");
            PAPER = EntryBuilder.build("CS2103 finals",
                                       new GregorianCalendar(2017, Calendar.JULY, 28, 10, 0),
                                       "school", "exam", "study");
            SUBMISSION = EntryBuilder.build("Submit assignment",
                                            new GregorianCalendar(2017, Calendar.JULY, 12, 16, 00),
                                            "school");
            UPGRADE = EntryBuilder.build("Upgrade computer",
                                         new GregorianCalendar(2017, Calendar.DECEMBER, 1, 00, 00),
                                         "personal", "project");
            // Deadlines (archive)
            QUIZ = EntryBuilder.build("Complete post-lecture quiz",
                                      new GregorianCalendar(2017, Calendar.JULY, 20, 00, 00),
                                      "school");
            QUIZ.setState(Entry.State.ARCHIVED);
            REGISTRATION = EntryBuilder.build("Register for marathon",
                                              new GregorianCalendar(2017, Calendar.NOVEMBER, 19, 10, 0),
                                              "health");
            REGISTRATION.setState(Entry.State.ARCHIVED);
            // Deadlines (bin)
            APPOINTMENT = EntryBuilder.build("Make health check appointment",
                                             new GregorianCalendar(2017, Calendar.APRIL, 16, 00, 00),
                                             "health", "weekends");
            APPOINTMENT.setState(Entry.State.DELETED);
            EMAIL = EntryBuilder.build("Email penpal",
                                       new GregorianCalendar(2017, Calendar.NOVEMBER, 9, 14, 0),
                                       "hobby");
            EMAIL.setState(Entry.State.DELETED);

            // Floating tasks (active)
            COOK = EntryBuilder.build("Learn to cook",
                                      "goals");
            PROGRAMMING = EntryBuilder.build("Learn programming",
                                             "lessons", "computer");
            HIRE = EntryBuilder.build("Hire an assistant",
                                      "help");
            SPECTACLES = EntryBuilder.build("Make new spectacles",
                                            "health", "eyesight");
            CLEAN = EntryBuilder.build("Clean up room",
                                       "never", "hopefully");
            SELL = EntryBuilder.build("Sell old things",
                                      "sale", "clutter");
            // Floating Tasks (archive)
            EXERCISE = EntryBuilder.build("Go jogging",
                                          "health");
            EXERCISE.setState(Entry.State.ARCHIVED);
            BOOK = EntryBuilder.build("Read books",
                                      "hobby");
            BOOK.setState(Entry.State.ARCHIVED);
            // Floating Tasks (bin)
            BAKE = EntryBuilder.build("Learn baking",
                                      "hobby");
            BAKE.setState(Entry.State.DELETED);
            SING = EntryBuilder.build("Learn singing",
                                      "hobby");
            SING.setState(Entry.State.DELETED);

        } catch (Exception e) {
            throw new AssertionError("Sample data cannot be invalid", e);
        }
    }

    public static Entry[] getSampleActiveEntries() {
        return new Entry[] {
            DINNER, TAX, PAPER, COOK, PROGRAMMING
        };
    }

    public static Entry[] getSampleActiveEvents() {
        return new Entry[] { DINNER };
    }

    public static Entry[] getSampleActiveDeadlines() {
        return new Entry[] { TAX, PAPER };
    }

    public static Entry[] getSampleActiveFloatingTasks() {
        return new Entry[] { COOK, PROGRAMMING };
    }
    // @@author

    // @@author A0126623L
    public static Entry[] getSampleArchivedEntries() {
        return new Entry[] {
            ASSIGNMENT, QUIZ, EXERCISE
        };
    }
    // @@author

    // @@author A0126623L
    public static Entry[] getSampleArchivedEvents() {
        return new Entry[] {
            ASSIGNMENT
        };
    }
    // @@author

    // @@author A0126623L
    public static Entry[] getSampleArchivedDeadlines() {
        return new Entry[] {
            QUIZ
        };
    }
    // @@author

    // @@author A0126623L
    public static Entry[] getSampleArchivedFloatingTasks() {
        return new Entry[] {
            EXERCISE
        };
    }
    // @@author

    // @@author A0126623L
    public static Entry[] getSampleDeletedEntries() {
        return new Entry[] {
            GYM, APPOINTMENT, BAKE
        };
    }
    // @@author

    // @@author A0126623L
    public static Entry[] getSampleDeletedEvents() {
        return new Entry[] {
            GYM
        };
    }
    // @@author

    // @@author A0126623L
    public static Entry[] getSampleDeletedDeadlines() {
        return new Entry[] {
            APPOINTMENT
        };
    }
    // @@author

    // @@author A0126623L
    public static Entry[] getSampleDeletedFloatingTasks() {
        return new Entry[] {
            BAKE
        };
    }
    // @@author

    // @@author A0126623L
    /**
     * Adds sample entries to the provided EntryBook
     */
    public static void loadEntryBookWithSampleActiveEntries(EntryBook entryBook) {
        Objects.requireNonNull(entryBook);
        for (Entry entry : getSampleActiveEntries()) {
            try {
                entryBook.addEntry(EntryBuilder.build(entry));
            } catch (OverlappingEventException oee) {
                // Do nothing. OverlappingEventException is to be expected here.
            } catch (OverlappingAndOverdueEventException e) {
                // Do nothing. Overlapping and overdue entries are fine.
            } catch (EntryOverdueException e) {
                // Do nothing. Overdue entries are fine.
            } catch (DuplicateEntryException e) {
                assert false : "Sample entries cannot have duplicate entries.";
            }
        }
    }
    // @@author

    // @@author A0126623L
    private static void loadEntryBookWithSampleArchivedEntries(EntryBook entryBook) {
        Objects.requireNonNull(entryBook);
        for (Entry entry : getSampleArchivedEntries()) {
            try {
                entryBook.addEntry(entry);
            } catch (OverlappingEventException oee) {
                // Do nothing. OverlappingEventException is to be expected here.
            } catch (OverlappingAndOverdueEventException e) {
                // Do nothing. Overlapping and overdue entries are fine.
            } catch (EntryOverdueException e) {
                // Do nothing. Overdue entries are fine.
            } catch (DuplicateEntryException e) {
                assert false : "Sample entries cannot have duplicate entries.";
            }
        }
    }
    // @@author

    // @@author A0126623L
    private static void loadEntryBookWithSampleDeletedEntries(EntryBook entryBook) {
        Objects.requireNonNull(entryBook);
        for (Entry entry : getSampleDeletedEntries()) {
            try {
                entryBook.addEntry(entry);
            } catch (OverlappingEventException oee) {
                // Do nothing. OverlappingEventException is to be expected here.
            } catch (OverlappingAndOverdueEventException e) {
                // Do nothing. Overlapping and overdue entries are fine.
            } catch (EntryOverdueException e) {
                // Do nothing. Overdue entries are fine.
            } catch (DuplicateEntryException e) {
                assert false : "Sample entries cannot have duplicate entries.";
            }
        }
    }
    // @@author

    // @@author A0126623L
    /**
     * @return an {@code EntryBook} with active sample events, deadlines and floating tasks.
     */
    public static EntryBook getSampleEntryBookWithActiveEntries() {
        EntryBook entryBook = new EntryBook();
        loadEntryBookWithSampleActiveEntries(entryBook);
        return entryBook;
    }
    // @@author

    // @@author A0126623L
    /**
     * @return  an {@code EntryBook} with sample events, deadlines and floating tasks
     *          of various states (i.e. active, archived, deleted).
     */
    public static EntryBook getSampleEntryBook() {
        EntryBook entryBook = new EntryBook();
        loadEntryBookWithSampleActiveEntries(entryBook);
        loadEntryBookWithSampleArchivedEntries(entryBook);
        loadEntryBookWithSampleDeletedEntries(entryBook);
        return entryBook;
    }
    // @@author
}
