package seedu.multitasky.model.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import seedu.multitasky.model.util.TagSetBuilder;

//@@author A0126623L
/**
 * TODO: Make SetUp less dependent on actual classes, e.g. create utility classes and stubs.
 */
public class EventTest {

    public static final Event[] SAMPLE_EVENTS_ARRAY_DATA = getSampleEventArrayData();

    Event event1 = SAMPLE_EVENTS_ARRAY_DATA[0];
    Event event2 = SAMPLE_EVENTS_ARRAY_DATA[1];
    Event event3 = SAMPLE_EVENTS_ARRAY_DATA[2];
    Event event4 = SAMPLE_EVENTS_ARRAY_DATA[3];
    Event event5 = SAMPLE_EVENTS_ARRAY_DATA[4];
    Event event6 = SAMPLE_EVENTS_ARRAY_DATA[5];

    // @@author A0126623L
    /**
     * Gets an array of 6 sample events.
     * The first two events are meaningfully equivalent, the remaining are unique.
     *
     * @return Event[] of 6 sample events.
     */
    public static Event[] getSampleEventArrayData() {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2017, 6, 7, 18, 30); // 7th July 2017, 6:30pm
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2017, 6, 8, 18, 30); // 8th July 2017, 6:30pm
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2017, 6, 9, 18, 30); // 9th July 2017, 6:30pm

        try {
            return new Event[] {
                new Event(new Name("SampleName1"), calendar1, calendar2,
                          TagSetBuilder.generateTagSet("tag1")),
                new Event(new Name("SampleName1"), calendar1, calendar2,
                          TagSetBuilder.generateTagSet("tag1")),
                new Event(new Name("SampleName2"), calendar1, calendar2,
                          TagSetBuilder.generateTagSet("tag1")),
                new Event(new Name("SampleName1"), calendar2, calendar3,
                          TagSetBuilder.generateTagSet("tag1")),
                new Event(new Name("SampleName1"), calendar1, calendar3,
                          TagSetBuilder.generateTagSet("tag1")),
                new Event(new Name("SampleName1"), calendar1, calendar2,
                          TagSetBuilder.generateTagSet("tag2"))
            };
        } catch (Exception e) {
            fail("Event array initialisation failed.");
            return null;
        }
    }
    // @@author

    // @@author A0126623L
    /**
     * @return List<Event> of 10 sample elements.
     */
    public static List<Event> getSampleEventListData() {
        return Arrays.asList(SAMPLE_EVENTS_ARRAY_DATA);
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
        Event tester999 = new Event(event1);
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
        // Equal
        assertTrue(event1.equals(event2));

        // Not equal
        assertFalse(event1.equals(event3));
        assertFalse(event1.equals(event4));
        assertFalse(event1.equals(event5));
        assertFalse(event1.equals(event6));
    }

}
