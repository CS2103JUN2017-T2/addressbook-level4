package seedu.multitasky.model.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.tag.Tag;


public class EntryBuilderTest {

    // @@author A0125586X
    @Test
    public void entryBuilder_build_success() {
        Entry entry = EntryBuilder.build();
        assertTrue(entry.getName().toString().equals(EntryBuilder.DEFAULT_NAME));
        try {
            assertTrue(entry.getTags().contains(new Tag(EntryBuilder.DEFAULT_TAGS)));
        } catch (IllegalValueException e) {
            fail("EntryBuilder default build should not fail");
        }
    }

    // @@author A0125586X
    @Test
    public void entryBuilder_wrongName_illegalValueException() {
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
    }

    // @@author A0125586X
    @Test
    public void entryBuilder_wrongDateCombination_illegalValueException() {
        boolean thrown = false;
        Calendar startDate = new GregorianCalendar(2000, Calendar.JANUARY, 1, 00, 00);
        Calendar endDate = null;
        try {
            EntryBuilder.build("event", startDate, endDate, "tag1", "tag2");
        } catch (IllegalValueException e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;

        try {
            EntryBuilder.build("event", startDate, endDate, TagSetBuilder.getTagSet("tag1", "tag2"));
        } catch (IllegalValueException e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;

        try {
            EntryBuilder.build(new Name("event"), startDate, endDate, "tag1", "tag2");
        } catch (IllegalValueException e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;

        try {
            EntryBuilder.build(new Name("event"), startDate, endDate, TagSetBuilder.getTagSet("tag1", "tag2"));
        } catch (IllegalValueException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    // @@author A0125586X
    @Test
    public void entryBuilder_buildEvent_success() {
        List<Entry> entries = new ArrayList<>();
        Calendar startDate = new GregorianCalendar(2000, Calendar.JANUARY, 1, 00, 00);
        Calendar endDate = new GregorianCalendar(2000, Calendar.JANUARY, 1, 02, 00);
        try {
            entries.add(EntryBuilder.build("Event1", startDate, endDate, "tag1", "tag2"));
            entries.add(EntryBuilder.build("Event1", startDate, endDate, TagSetBuilder.getTagSet("tag1", "tag2")));
            entries.add(EntryBuilder.build(new Name("Event1"), startDate, endDate, "tag1", "tag2"));
            entries.add(EntryBuilder.build(new Name("Event1"), startDate, endDate,
                                           TagSetBuilder.getTagSet("tag1", "tag2")));
        } catch (IllegalValueException e) {
            fail("Building sample data cannot fail");
        }
        for (Entry entry : entries) {
            assertTrue(entry instanceof Event);
            Entry entry2 = EntryBuilder.build(entry);
            assertTrue(entry2 instanceof Event);
            assertTrue(entry.equals(entry2));
        }
    }

    // @@author A0125586X
    @Test
    public void entryBuilder_buildDeadline_success() {
        List<Entry> entries = new ArrayList<>();
        Calendar startDate = null;
        Calendar endDate = new GregorianCalendar(2000, Calendar.JANUARY, 1, 02, 00);
        try {
            entries.add(EntryBuilder.build("Deadline1", startDate, endDate, "tag1", "tag2"));
            entries.add(EntryBuilder.build("Deadline1", startDate, endDate, TagSetBuilder.getTagSet("tag1", "tag2")));
            entries.add(EntryBuilder.build(new Name("Deadline1"), startDate, endDate, "tag1", "tag2"));
            entries.add(EntryBuilder.build(new Name("Deadline1"), startDate, endDate,
                                           TagSetBuilder.getTagSet("tag1", "tag2")));
            entries.add(EntryBuilder.build("Deadline1", endDate, "tag1", "tag2"));
            entries.add(EntryBuilder.build("Deadline1", endDate, TagSetBuilder.getTagSet("tag1", "tag2")));
            entries.add(EntryBuilder.build(new Name("Deadline1"), endDate, "tag1", "tag2"));
            entries.add(EntryBuilder.build(new Name("Deadline1"), endDate, TagSetBuilder.getTagSet("tag1", "tag2")));
        } catch (IllegalValueException e) {
            fail("Building sample data cannot fail");
        }
        for (Entry entry : entries) {
            assertTrue(entry instanceof Deadline);
            Entry entry2 = EntryBuilder.build(entry);
            assertTrue(entry2 instanceof Deadline);
            assertTrue(entry.equals(entry2));
        }
    }

    // @@author A0125586X
    @Test
    public void entryBuilder_buildFloatingTask_success() {
        List<Entry> entries = new ArrayList<>();
        Calendar startDate = null;
        Calendar endDate = null;
        try {
            entries.add(EntryBuilder.build("Floating1", startDate, endDate, "tag1", "tag2"));
            entries.add(EntryBuilder.build("Floating1", startDate, endDate, TagSetBuilder.getTagSet("tag1", "tag2")));
            entries.add(EntryBuilder.build(new Name("Floating1"), startDate, endDate, "tag1", "tag2"));
            entries.add(EntryBuilder.build(new Name("Floating1"), startDate, endDate,
                                           TagSetBuilder.getTagSet("tag1", "tag2")));
            entries.add(EntryBuilder.build("Floating1", endDate, "tag1", "tag2"));
            entries.add(EntryBuilder.build("Floating1", endDate, TagSetBuilder.getTagSet("tag1", "tag2")));
            entries.add(EntryBuilder.build(new Name("Floating1"), endDate, "tag1", "tag2"));
            entries.add(EntryBuilder.build(new Name("Floating1"), endDate, TagSetBuilder.getTagSet("tag1", "tag2")));
            entries.add(EntryBuilder.build("Floating1", "tag1", "tag2"));
            entries.add(EntryBuilder.build("Floating1", TagSetBuilder.getTagSet("tag1", "tag2")));
            entries.add(EntryBuilder.build(new Name("Floating1"), "tag1", "tag2"));
            entries.add(EntryBuilder.build(new Name("Floating1"), TagSetBuilder.getTagSet("tag1", "tag2")));
            entries.add(EntryBuilder.build("Floating1"));
            entries.add(EntryBuilder.build(new Name("Floating1")));
        } catch (IllegalValueException e) {
            fail("Building sample data cannot fail");
        }
        for (Entry entry : entries) {
            assertTrue(entry instanceof FloatingTask);
            Entry entry2 = EntryBuilder.build(entry);
            assertTrue(entry2 instanceof FloatingTask);
            assertTrue(entry.equals(entry2));
        }
    }
}
