package seedu.multitasky.model.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import seedu.multitasky.model.tag.Tag;

public class EventTest {

    Calendar calendar1;
    Calendar calendar2;
    Calendar calendar3;

    Set<Tag> tagSet1;
    Set<Tag> tagSet2;

    Name eventName1;
    Name eventName2;

    Event tester1;
    Event tester2;
    Event tester3;
    Event tester4;
    Event tester5;
    Event tester6;

    @Before
    public void setUp() {
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
        tester1 = new Event(eventName1, calendar1, calendar2, tagSet1);
        // Same fields as tester1
        tester2 = new Event(eventName1, calendar1, calendar2, tagSet1);
        // Only name is different from tester1
        tester3 = new Event(eventName2, calendar1, calendar2, tagSet1);
        // Only start time is different from tester1
        tester4 = new Event(eventName1, calendar2, calendar3, tagSet1);
        // Only end time is different from tester1
        tester5 = new Event(eventName1, calendar1, calendar3, tagSet1);
        // Only tags are different from tester1
        tester6 = new Event(eventName1, calendar1, calendar2, tagSet2);
    }

    @Test
    public void getNameTest() {
        assertEquals("error at getName()", "SampleName1", tester1.getName().fullName);
    }

    @Test
    public void getTagsTest() {
        // Same tags
        assertTrue(tester1.getTags().equals(tester2.getTags()));

        // Different tags
        assertFalse(tester1.getTags().equals(tester6.getTags()));
    }

    @Test
    public void resetDataTest() {
        Event tester999 = tester1;
        assertFalse(tester999.equals(tester3));

        tester999.resetData(tester3);
        assertTrue(tester999.equals(tester3));
    }

    @Test
    public void toStringTest() {
        assertEquals("Event formatting is wrong",
                     "SampleName1 Start: Jul 7, 2017 6:30 PM End: Jul 8, 2017 6:30 PM Tags: [tag1set1]",
                     tester1.toString());
    }

    @Test
    public void equalsTest() {
        // Equal
        assertTrue(tester1.equals(tester2));

        // Not equal
        assertFalse(eventName1.equals(eventName2));
        assertFalse(calendar1.equals(calendar2));
        assertFalse(tester1.equals(tester3));
        assertFalse(tester1.equals(tester4));
        assertFalse(tester1.equals(tester5));
        assertFalse(tester1.equals(tester6));
    }

}
