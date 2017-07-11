package seedu.multitasky.model.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import seedu.multitasky.model.tag.Tag;

public class FloatingTaskTest {

    private static Set<Tag> tagSet1;
    private static Set<Tag> tagSet2;

    private static Name name1;
    private static Name name2;

    private static FloatingTask floatingTask1;
    private static FloatingTask floatingTask2;
    private static FloatingTask floatingTask3;
    private static FloatingTask floatingTask4;

    // @@author A0126623L
    @BeforeClass
    public static void setUp() throws Exception {
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
        floatingTask1 = new FloatingTask(name1, tagSet1);
        // Same fields as tester1
        floatingTask2 = new FloatingTask(name1, tagSet1);
        // Only name is different from tester1
        floatingTask3 = new FloatingTask(name2, tagSet1);
        // Only tags are different from tester1
        floatingTask4 = new FloatingTask(name1, tagSet2);
    }

    // @@author A0126623L
    /**
     * Creates a sample FloatingTask object.
     */
    public static FloatingTask createFloatingTask() {
        return floatingTask1;
    }

    // @@author A0126623L
    @Test
    public void getNameTest() {
        assertEquals("error at getName()", "SampleName1", floatingTask1.getName().fullName);
    }

    // @@author A0126623L
    @Test
    public void getTagsTest() {
        // Same tags
        assertTrue(floatingTask1.getTags().equals(floatingTask2.getTags()));

        // Different tags
        assertFalse(floatingTask1.getTags().equals(floatingTask4.getTags()));
    }

    // @@author A0126623L
    @Test
    public void resetDataTest() {
        FloatingTask tester999 = new FloatingTask(name1, tagSet1);
        assertFalse(tester999.equals(floatingTask3));

        tester999.resetData(floatingTask3);
        assertTrue(tester999.equals(floatingTask3));
    }

    // @@author A0126623L
    @Test
    public void toStringTest() {
        assertEquals("FloatingTask formatting is wrong",
                     "SampleName1 Tags: [tag1set1]",
                     floatingTask1.toString());
    }

    // @@author A0126623L
    @Test
    public void equalsTest() {
        // Equal
        assertTrue(floatingTask1.equals(floatingTask2));

        // Not equal
        assertFalse(floatingTask1.equals(floatingTask3));
        assertFalse(floatingTask1.equals(floatingTask4));
    }

}
