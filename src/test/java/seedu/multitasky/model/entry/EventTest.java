package seedu.multitasky.model.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.model.util.TagSetBuilder;

// @@author A0126623L
/**
 * TODO: Make SetUp less dependent on actual classes, e.g. create utility classes and stubs.
 */
public class EventTest {

    private Event event1, event2, event3, event4, event5, event6;

    // @@author A0126623L
    /**
     * Gets an array of 6 sample events.
     * The first two events are meaningfully equivalent, the remaining are unique.
     * @return Event[] of 6 sample events.
     */
    public static Event[] getSampleEventArray() {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2017, 6, 7, 18, 30); // 7th July 2017, 6:30pm
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2017, 6, 8, 18, 30); // 8th July 2017, 6:30pm
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2017, 6, 9, 18, 30); // 9th July 2017, 6:30pm

        try {
            return new Event[] {
                new Event(new Name("SampleName1"), calendar1, calendar2,
                          TagSetBuilder.getTagSet("tag1")),
                new Event(new Name("SampleName1"), calendar1, calendar2,
                          TagSetBuilder.getTagSet("tag1")),
                new Event(new Name("SampleName2"), calendar1, calendar2,
                          TagSetBuilder.getTagSet("tag1")),
                new Event(new Name("SampleName1"), calendar2, calendar3,
                          TagSetBuilder.getTagSet("tag1")),
                new Event(new Name("SampleName1"), calendar1, calendar3,
                          TagSetBuilder.getTagSet("tag1")),
                new Event(new Name("SampleName1"), calendar1, calendar2,
                          TagSetBuilder.getTagSet("tag2"))
            };
        } catch (Exception e) {
            fail("Event array initialisation failed.");
            return null;
        }
    }
    // @@author

    // @@author A0126623L
    /**
     * method that returns list of Events of 6 sample elements.
     */
    public static List<Event> getSampleEventList() {
        return Arrays.asList(EventTest.getSampleEventArray());
    }
    // @@author

    @Before
    public void setUp() {
        Event[] sampleEventArrayData = getSampleEventArray();

        event1 = (Event) EntryBuilder.build(sampleEventArrayData[0]);
        event2 = (Event) EntryBuilder.build(sampleEventArrayData[1]);
        event3 = (Event) EntryBuilder.build(sampleEventArrayData[2]);
        event4 = (Event) EntryBuilder.build(sampleEventArrayData[3]);
        event5 = (Event) EntryBuilder.build(sampleEventArrayData[4]);
        event6 = (Event) EntryBuilder.build(sampleEventArrayData[5]);
    }

    // @@author A0126623L
    @Test
    public void getNameTest() {
        assertEquals("error at getName()", "SampleName1", event1.getName().fullName);
    }

    // @@author A0126623L
    @Test
    public void getTagsTest() {
        // Same tags
        assertTrue(event1.getTags().equals(event2.getTags()));

        // Different tags
        assertFalse(event1.getTags().equals(event6.getTags()));
    }

    // @@author A0126623L
    @Test
    public void resetDataTest() {
        Event tester999 = (Event) EntryBuilder.build(event1);
        assertFalse(tester999.equals(event3));

        tester999.resetData(event3);
        assertTrue(tester999.equals(event3));
    }

    // @@author A0126623L
    @Test
    public void toStringTest() {
        assertEquals("Event formatting is wrong",
                     "SampleName1 Start: Jul 7, 2017 6:30 PM End: Jul 8, 2017 6:30 PM Tags: [tag1]",
                     event1.toString());
    }

    // @@author A0126623L
    @Test
    public void equalsTest() {
        Event dummyEvent = (Event) EntryBuilder.build(event2);

        // Equal
        assertTrue(event1.equals(event2));
        assertTrue(dummyEvent.equals(event1));

        dummyEvent.setState(Entry.State.ARCHIVED);

        // Not equal
        assertFalse(dummyEvent.equals(event1));
        assertFalse(event1.equals(event3));
        assertFalse(event1.equals(event4));
        assertFalse(event1.equals(event5));
        assertFalse(event1.equals(event6));

        dummyEvent.setState(Entry.State.ACTIVE);
        assertTrue(dummyEvent.equals(event1));

        // Test EventTest.getSampleEventArray()
        Event[] array1 = EventTest.getSampleEventArray();
        Event[] array2 = EventTest.getSampleEventArray();
        assertFalse(array1[0] == array2[0]);
        assertNotSame(array1[0], array2[0]);
        assertTrue(array1[0].equals(array2[0]));
    }

    // @@author A0126623L
    @Test
    public void hasOverlappingTimeTest() {
        final int offsetAmount = 2;

        // Make an event with start time overlapping with event1.
        Entry eventWithStartTimeOverlapped = EntryBuilder.build(event1);
        eventWithStartTimeOverlapped.getStartDateAndTime().add(Calendar.HOUR, offsetAmount);
        eventWithStartTimeOverlapped.getEndDateAndTime().add(Calendar.HOUR, offsetAmount);
        assertTrue(event1.hasOverlappingTime(eventWithStartTimeOverlapped));

        // Make an event with start time overlapping with event1.
        Entry eventWithEndTimeOverlapped = EntryBuilder.build(event1);
        eventWithEndTimeOverlapped.getStartDateAndTime().add(Calendar.HOUR, -offsetAmount);
        eventWithEndTimeOverlapped.getEndDateAndTime().add(Calendar.HOUR, -offsetAmount);
        assertTrue(event1.hasOverlappingTime(eventWithEndTimeOverlapped));

        // Make an event fully overlapped with event1.
        Entry eventFullyOverlapped = EntryBuilder.build(event1);
        assertTrue(event1.hasOverlappingTime(eventWithEndTimeOverlapped));

        try {
            // Make an event that doesn't overlap with event1.
            Entry eventWithNoOverlap = EntryBuilder.build(new Name("noOverlapEvent"),
                                                          Calendar.getInstance(),
                                                          Calendar.getInstance(),
                                                          "tag1");
            eventWithNoOverlap.getStartDateAndTime().add(Calendar.YEAR, offsetAmount);
            eventWithNoOverlap.getEndDateAndTime().add(Calendar.YEAR, offsetAmount + 1);
            assertFalse(event1.hasOverlappingTime(eventWithNoOverlap));
        } catch (Exception e) {
            fail("Should not fail.");
        }
    }
    // @@author

    // @@author A0126623L
    @Test
    public void isOverdueTest() {
        final int offsetAmount = 10;
        try {
            // Overdue event
            Entry overdueEvent = EntryBuilder.build(new Name("eventSample"),
                                                    Calendar.getInstance(),
                                                    Calendar.getInstance(),
                                                    "tag1");
            overdueEvent.getEndDateAndTime().add(Calendar.YEAR, -offsetAmount);
            overdueEvent.getStartDateAndTime().add(Calendar.YEAR, -offsetAmount - 1);
            assertTrue(((Event) overdueEvent).isOverdue());

            // Current event should be considered overdue
            Entry currentEvent = EntryBuilder.build(new Name("eventSample"),
                                                    Calendar.getInstance(),
                                                    Calendar.getInstance(),
                                                    "tag1");
            assertTrue(((Event) currentEvent).isOverdue());

            // Future event
            // Overdue event
            Entry futureEvent = EntryBuilder.build(new Name("eventSample"),
                                                    Calendar.getInstance(),
                                                    Calendar.getInstance(),
                                                    "tag1");
            futureEvent.getEndDateAndTime().add(Calendar.YEAR, offsetAmount + 1);
            futureEvent.getStartDateAndTime().add(Calendar.YEAR, offsetAmount);
            assertFalse(((Event) futureEvent).isOverdue());

        } catch (Exception e) {
            fail("Should not fail.");
        }

    }
    // @@author

}
