package seedu.multitasky.model.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import seedu.multitasky.model.tag.Tag;

public class FloatingTaskTest {

    Set<Tag> tagSet1;
    Set<Tag> tagSet2;

    Name name1;
    Name name2;

    FloatingTask tester1;
    FloatingTask tester2;
    FloatingTask tester3;
    FloatingTask tester4;

    @Before
    public void setUp() throws Exception {
        try {
            tagSet1 = new HashSet<>();
            tagSet1.add(new Tag("tag1set1"));

            tagSet2 = new HashSet<>();
            tagSet2.add(new Tag("tag1set2"));
        } catch (Exception e) {
            fail("Tags initialisation failed.");
        }

        try {
            name1 = new Name("SampleName1");
            name2 = new Name("SampleName2");
        } catch (Exception e) {
            fail("Floating task name initialisation failed.");
        }

        // First tester, used for reference
        tester1 = new FloatingTask(name1, tagSet1);
        // Same fields as tester1
        tester2 = new FloatingTask(name1, tagSet1);
        // Only name is different from tester1
        tester3 = new FloatingTask(name2, tagSet1);
        // Only tags are different from tester1
        tester4 = new FloatingTask(name1, tagSet2);
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
        assertFalse(tester1.getTags().equals(tester4.getTags()));
    }

    @Test
    public void resetDataTest() {
        FloatingTask tester999 = tester1;
        assertFalse(tester999.equals(tester3));

        tester999.resetData(tester3);
        assertTrue(tester999.equals(tester3));
    }

    @Test
    public void toStringTest() {
        assertEquals("FloatingTask formatting is wrong",
                     "SampleName1 Tags: [tag1set1]",
                     tester1.toString());
    }

    @Test
    public void equalsTest() {
        // Equal
        assertTrue(tester1.equals(tester2));

        // Not equal
        assertFalse(tester1.equals(tester3));
        assertFalse(tester1.equals(tester4));
    }

}
