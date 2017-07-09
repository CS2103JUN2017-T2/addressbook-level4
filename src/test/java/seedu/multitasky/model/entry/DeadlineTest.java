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

//@@author A0126623L
public class DeadlineTest {

    static Calendar calendar1;
    static Calendar calendar2;
    static Calendar calendar3;

    static Set<Tag> tagSet1;
    static Set<Tag> tagSet2;

    static Name deadlineName1;
    static Name deadlineName2;

    static Deadline deadline1;
    static Deadline deadline2;
    static Deadline deadline3;
    static Deadline deadline4;
    static Deadline deadline5;
    static Deadline deadline6;

    // @@author A0126623L
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
            deadlineName1 = new Name("SampleName1");
            deadlineName2 = new Name("SampleName2");
        } catch (Exception e) {
            fail("Deadline name initialisation failed.");
        }

        // First tester, used for reference
        deadline1 = new Deadline(deadlineName1, calendar1, tagSet1);
        // Same fields as tester1
        deadline2 = new Deadline(deadlineName1, calendar1, tagSet1);
        // Only name is different from tester1
        deadline3 = new Deadline(deadlineName2, calendar1, tagSet1);
        // Only end time is different from tester1
        deadline4 = new Deadline(deadlineName1, calendar2, tagSet1);
        // Only tags are different from tester1
        deadline5 = new Deadline(deadlineName1, calendar1, tagSet2);
    }

    // @@author A0126623L
    @Test
    public void getNameTest() {
        assertEquals("error at getName()", "SampleName1", deadline1.getName().fullName);
    }

    // @@author A0126623L
    @Test
    public void getTagsTest() {
        // Same tags
        assertTrue(deadline1.getTags().equals(deadline2.getTags()));

        // Different tags
        assertFalse(deadline1.getTags().equals(deadline5.getTags()));
    }

    // @@author A0126623L
    @Test
    public void resetDataTest() {
        Deadline tester999 = new Deadline(deadlineName1, calendar1, tagSet1);
        assertFalse(tester999.equals(deadline3));

        tester999.resetData(deadline3);
        assertTrue(tester999.equals(deadline3));
    }

    // @@author A0126623L
    @Test
    public void toStringTest() {
        assertEquals("Deadline formatting is wrong",
                     "SampleName1 Deadline: Jul 7, 2017 6:30 PM Tags: [tag1set1]",
                     deadline1.toString());
    }

    // @@author A0126623L
    @Test
    public void equalsTest() {
        // Equal
        assertTrue(deadline1.equals(deadline2));

        // Not equal
        assertFalse(deadlineName1.equals(deadlineName2));
        assertFalse(calendar1.equals(calendar2));
        assertFalse(deadline1.equals(deadline3));
        assertFalse(deadline1.equals(deadline4));
        assertFalse(deadline1.equals(deadline5));
    }

}
