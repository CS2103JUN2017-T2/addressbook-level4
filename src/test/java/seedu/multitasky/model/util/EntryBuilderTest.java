package seedu.multitasky.model.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;

public class EntryBuilderTest {

    // @@author A0125586X
    @Test
    public void wrongName_illegalValueException() {
        boolean thrown = false;
        try {
            EntryBuilder.build("$event",
                               new GregorianCalendar(2000, Calendar.JANUARY, 1, 00, 00),
                               new GregorianCalendar(2000, Calendar.JANUARY, 1, 02, 00),
                               "party", "fun");
        } catch (IllegalValueException e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;

        try {
            EntryBuilder.build("$deadline",
                               null,
                               new GregorianCalendar(2005, Calendar.MAY, 5, 17, 00),
                               "tag1", "tag2");
            EntryBuilder.build("$deadline",
                               new GregorianCalendar(2005, Calendar.MAY, 5, 17, 00),
                               "tag1", "tag2");
        } catch (IllegalValueException e) {
            thrown = true;
        }

        try {
            EntryBuilder.build("$float",
                               "tag1", "tag2");
        } catch (IllegalValueException e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;
    }

    // @@author A0125586X
    @Test
    public void buildEvent() {
        Entry entry = null;
        try {
            entry = EntryBuilder.build("Event1",
                                       new GregorianCalendar(2000, Calendar.JANUARY, 1, 00, 00),
                                       new GregorianCalendar(2000, Calendar.JANUARY, 1, 02, 00),
                                       "party", "fun");
        } catch (IllegalValueException e) {
            fail("Error in EntryBuilderTest.buildEvent() sample data");
        }
        assertTrue(entry instanceof Event);
        Entry entry2 = EntryBuilder.build(entry);
        assertTrue(entry2 instanceof Event);
        assertTrue(entry.equals(entry2));
    }

    // @@author A0125586X
    @Test
    public void buildDeadline() {
        Entry[] entries = new Entry[2];
        try {
            entries[0] = EntryBuilder.build("Deadline1",
                                            null,
                                            new GregorianCalendar(2005, Calendar.MAY, 5, 17, 00),
                                            "tag1", "tag2");
            entries[1] = EntryBuilder.build("Deadline2",
                                            new GregorianCalendar(2005, Calendar.MAY, 5, 17, 00),
                                            "tag1", "tag2");
        } catch (IllegalValueException e) {
            fail("Error in EntryBuilderTest.buildDeadline() sample data");
        }
        assertTrue(entries[0] instanceof Deadline);
        assertTrue(entries[1] instanceof Deadline);
        Entry entry = EntryBuilder.build(entries[0]);
        assertTrue(entry instanceof Deadline);
        assertTrue(entry.equals(entries[0]));
    }

    // @@author A0125586X
    @Test
    public void buildFloatingTask() {
        Entry entry = null;
        try {
            entry = EntryBuilder.build("Float1",
                                       "tag1", "tag2");
        } catch (IllegalValueException e) {
            fail("Error in EntryBuilderTest.buildFloatingTask() sample data");
        }
        assertTrue(entry instanceof FloatingTask);
        Entry entry2 = EntryBuilder.build(entry);
        assertTrue(entry instanceof FloatingTask);
        assertTrue(entry.equals(entry2));
    }
}
