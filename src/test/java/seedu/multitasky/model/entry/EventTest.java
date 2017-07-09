package seedu.multitasky.model.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import seedu.multitasky.model.tag.Tag;

//@@author A0126623L
/**
 * TODO: Make SetUp less dependent on actual classes, e.g. create utility classes and stubs.
 */
public class EventTest {

    static Calendar calendar1;
    static Calendar calendar2;
    static Calendar calendar3;

    static Set<Tag> tagSet1;
    static Set<Tag> tagSet2;

    static Name eventName1;
    static Name eventName2;

    static Event event1;
    static Event event2;
    static Event event3;
    static Event event4;
    static Event event5;
    static Event event6;

    // @@author A0126623L
    @BeforeClass
    public static void setUp() {
        calendar1 = Calendar.getInstance();
        calendar1.set(2017, 6, 7, 18, 30); // 7th July 2017, 6:30pm

        calendar2 = Calendar.getInstance();
        calendar2.set(2017, 6, 8, 18, 30); // 8th July 2017, 6:30pm

        calendar3 = Calendar.getInstance();
        calendar3.set(2017, 6, 9, 18, 30); // 9th July 2017, 6:30pm

        try {
            tagSet1 = new HashSet<>();
            tagSet1.add(new Tag("tag1set1"));

            tagSet2 = new HashSet<>();
            tagSet2.add(new Tag("tag1set2"));
        } catch (Exception e) {
            fail("Tags initialisation failed.");
        }

        try {
            eventName1 = new Name("SampleName1");
            eventName2 = new Name("SampleName2");
        } catch (Exception e) {
            fail("Event name initialisation failed.");
        }

        // First tester, used for reference
        event1 = new Event(eventName1, calendar1, calendar2, tagSet1);
        // Same fields as tester1
        event2 = new Event(eventName1, calendar1, calendar2, tagSet1);
        // Only name is different from tester1
        event3 = new Event(eventName2, calendar1, calendar2, tagSet1);
        // Only start time is different from tester1
        event4 = new Event(eventName1, calendar2, calendar3, tagSet1);
        // Only end time is different from tester1
        event5 = new Event(eventName1, calendar1, calendar3, tagSet1);
        // Only tags are different from tester1
        event6 = new Event(eventName1, calendar1, calendar2, tagSet2);
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
        Event tester999 = new Event(eventName1, calendar1, calendar2, tagSet1);
        assertFalse(tester999.equals(event3));

        tester999.resetData(event3);
        assertTrue(tester999.equals(event3));
    }

    // @@author A0126623L
    @Test
    public void toStringTest() {
        assertEquals("Event formatting is wrong",
                     "SampleName1 Start: Jul 7, 2017 6:30 PM End: Jul 8, 2017 6:30 PM Tags: [tag1set1]",
                     event1.toString());
    }

    // @@author A0126623L
    @Test
    public void equalsTest() {
        // Equal
        assertTrue(event1.equals(event2));

        // Not equal
        assertFalse(eventName1.equals(eventName2));
        assertFalse(calendar1.equals(calendar2));
        assertFalse(event1.equals(event3));
        assertFalse(event1.equals(event4));
        assertFalse(event1.equals(event5));
        assertFalse(event1.equals(event6));
    }

}
