package seedu.multitasky.model.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.tag.Tag;

public class SampleDataUtil {
    // @@author A0125586X
    public static Entry[] getSampleEntries() {
        try {
            // CHECKSTYLE.OFF: LineLength
            return new Entry[] {
                new EntryBuilder().withName("Dinner with family").withTags("cook")
                                  .withStartDateAndTime(new GregorianCalendar(2017, Calendar.DECEMBER, 25, 19,
                                                                              00))
                                  .withEndDateAndTime(new GregorianCalendar(2017, Calendar.DECEMBER, 25, 21,
                                                                            00))
                                  .build(),

                new EntryBuilder().withName("Submit taxes").withTags("money")
                                  .withEndDateAndTime(new GregorianCalendar(2017, Calendar.JULY, 1, 00, 00))
                                  .build(),
                new EntryBuilder().withName("CS2103 finals").withTags("school", "study")
                                  .withEndDateAndTime(new GregorianCalendar(2017, Calendar.JULY, 28, 10, 0))
                                  .build(),

                new EntryBuilder().withName("Learn to cook").withTags("goals").build(),
                new EntryBuilder().withName("Learn programming").withTags("lessons", "computer").build(),
            };
            // CHECKSTYLE.ON: LineLength
        } catch (Exception e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }

    public static Entry[] getSampleEvents() {
        try {
            // CHECKSTYLE.OFF: LineLength
            return new Entry[] {
                new EntryBuilder().withName("Dinner with family").withTags("cook")
                                  .withStartDateAndTime(new GregorianCalendar(2017, Calendar.DECEMBER, 25, 19,
                                                                              00))
                                  .withEndDateAndTime(new GregorianCalendar(2017, Calendar.DECEMBER, 25, 21,
                                                                            00))
                                  .build(),
            };
            // CHECKSTYLE.ON: LineLength
        } catch (Exception e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }

    public static Entry[] getSampleDeadlines() {
        try {
            // CHECKSTYLE.OFF: LineLength
            return new Entry[] {
                new EntryBuilder().withName("Submit taxes").withTags("money")
                                  .withEndDateAndTime(new GregorianCalendar(2017, Calendar.JULY, 1, 00, 00))
                                  .build(),
                new EntryBuilder().withName("CS2103 finals").withTags("school", "study")
                                  .withEndDateAndTime(new GregorianCalendar(2017, Calendar.JULY, 28, 10, 0))
                                  .build(),
            };
            // CHECKSTYLE.ON: LineLength
        } catch (Exception e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }

    public static Entry[] getSampleFloatingTasks() {
        try {
            // CHECKSTYLE.OFF: LineLength
            return new Entry[] {
                new EntryBuilder().withName("Learn to cook").withTags("goals").build(),
                new EntryBuilder().withName("Learn programming").withTags("lessons", "computer").build(),
            };
            // CHECKSTYLE.ON: LineLength
        } catch (Exception e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }
    // @@author

    public static ReadOnlyEntryBook getSampleEntryBook() {
        // TODO write catch block for DuplicateEntryException
        EntryBook sampleEntryBook = new EntryBook();
        try {
            for (Entry sampleEntry : getSampleEntries()) {
                sampleEntryBook.addEntry(sampleEntry);
            }
        } catch (DuplicateEntryException e) {
            assert false : "Sample Entry Book generation failed. EntryBooks should not have duplicate entries.";
        }
        return sampleEntryBook;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) throws IllegalValueException {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
