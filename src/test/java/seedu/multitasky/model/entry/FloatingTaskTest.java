package seedu.multitasky.model.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
public class FloatingTaskTest {

    private FloatingTask floatingTask1, floatingTask2, floatingTask3, floatingTask4;

    /**
     * Gets an array of 4 sample floatingTasks.
     * The first two floatingTasks are meaningfully equivalent, the remaining are unique.
     * @return FloatingTask[] of 4 sample floatingTasks.
     */
    public static FloatingTask[] getSampleFloatingTaskArray() {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2017, 6, 7, 18, 30); // 7th July 2017, 6:30pm
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2017, 6, 8, 18, 30); // 8th July 2017, 6:30pm
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(2017, 6, 9, 18, 30); // 9th July 2017, 6:30pm

        try {
            return new FloatingTask[] {
                new FloatingTask(new Name("SampleName1"), TagSetBuilder.getTagSet("tag1")),
                new FloatingTask(new Name("SampleName1"), TagSetBuilder.getTagSet("tag1")),
                new FloatingTask(new Name("SampleName2"), TagSetBuilder.getTagSet("tag1")),
                new FloatingTask(new Name("SampleName3"), TagSetBuilder.getTagSet("tag2"))
            };
        } catch (Exception e) {
            fail("FloatingTask array initialisation failed.");
            return null;
        }
    }

    /**
     * Returns list of FloatingTasks of 4 sample elements.
     */
    public static List<FloatingTask> getSampleFloatingTaskList() {
        return Arrays.asList(FloatingTaskTest.getSampleFloatingTaskArray());
    }

    @Before
    public void setUp() {
        FloatingTask[] sampleFloatingTaskArrayData = getSampleFloatingTaskArray();

        floatingTask1 = sampleFloatingTaskArrayData[0];
        floatingTask2 = sampleFloatingTaskArrayData[1];
        floatingTask3 = sampleFloatingTaskArrayData[2];
        floatingTask4 = sampleFloatingTaskArrayData[3];
    }

    @Test
    public void getName_actualAndExpected_success() {
        assertEquals("error at getName()", "SampleName1", floatingTask1.getName().fullName);
    }

    @Test
    public void getTags_actualAndExpected_success() {
        // Same tags
        assertTrue(floatingTask1.getTags().equals(floatingTask2.getTags()));

        // Different tags
        assertFalse(floatingTask1.getTags().equals(floatingTask4.getTags()));
    }

    @Test
    public void resetData_sampleFloatingTaskToReset_success() {
        FloatingTask tester999 = (FloatingTask) EntryBuilder.build(floatingTask1);
        assertFalse(tester999.equals(floatingTask3));

        tester999.resetData(floatingTask3);
        assertTrue(tester999.equals(floatingTask3));
    }

    @Test
    public void getStartAndEndTime_invokeGetterMethods_nulls() {
        assertNull(floatingTask1.getStartDateAndTime());
        assertNull(floatingTask1.getEndDateAndTime());
        assertNull(floatingTask1.getStartDateAndTimeString());
        assertNull(floatingTask1.getEndDateAndTimeString());
    }

    @Test
    public void toString_expectedString_success() {
        assertEquals("FloatingTask formatting is wrong",
                     "SampleName1, Tags: [tag1]",
                     floatingTask1.toString());
    }

    @Test
    public void equals_variousCases_success() {
        // Equal
        assertTrue(floatingTask1.equals(floatingTask2));

        // Not equal
        assertFalse(floatingTask1.equals(floatingTask3));
        assertFalse(floatingTask1.equals(floatingTask4));
    }

}
