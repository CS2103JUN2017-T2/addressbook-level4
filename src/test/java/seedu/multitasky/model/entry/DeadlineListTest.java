package seedu.multitasky.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import javafx.collections.ObservableList;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.model.tag.Tag;

//@@author A0126623L
/**
 * TODO: Make SetUp less dependent on actual classes, e.g. create utility classes to generate
 * Events, Deadlines and Floating Tasks.
 */
public class DeadlineListTest {

    static Calendar calendar1;
    static Calendar calendar2;
    static Calendar calendar3;

    static Set<Tag> tagSet1;
    static Set<Tag> tagSet2;

    static Name deadlineName1;
    static Name deadlineName2;
    static Name deadlineName3;

    static Deadline deadline1;
    static Deadline deadline2;
    static Deadline deadline3;
    static Deadline deadline4;
    static Deadline deadline5;

    static DeadlineList deadlineList1;
    static DeadlineList deadlineList2; // This list will be meaningfully equal to deadlineList1
    static DeadlineList deadlineList3; // This list will be different from deadlineList1 and deadlineList2

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        deadlineName1 = new Name("sampleName1");
        deadlineName2 = new Name("sampleName2");
        deadlineName3 = new Name("sampleName3");

        calendar1 = Calendar.getInstance();
        calendar1.set(2017, 6, 7, 18, 30); // 7th July 2017, 6:30pm
        calendar2 = Calendar.getInstance();
        calendar2.set(2017, 6, 8, 18, 30); // 8th July 2017, 6:30pm
        calendar3 = Calendar.getInstance();
        calendar3.set(2017, 6, 9, 18, 30); // 9th July 2017, 6:30pm

        tagSet1 = new HashSet<>();
        tagSet2 = new HashSet<>();
        try {
            tagSet1.add(new Tag("tag1set1"));

            tagSet2.add(new Tag("tag1set2"));
        } catch (Exception e) {
            fail("Tags initialisation failed.");
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

        deadlineList1 = new DeadlineList();
        deadlineList2 = new DeadlineList();
        deadlineList3 = new DeadlineList();

        deadlineList1.add(deadline1);
        deadlineList1.add(deadline3);

        deadlineList2.add(deadline1);
        deadlineList2.add(deadline3);

        deadlineList3.add(deadline1);
        deadlineList3.add(deadline4);
    }

    // @@author A0126623L
    /**
     * Create a DeadlineList with {deadline1, deadline3}
     */
    public static DeadlineList createDeadlineList1() {
        DeadlineList deadlineList1clone = new DeadlineList();
        deadlineList1clone.add(deadline1);
        deadlineList1clone.add(deadline3);

        return deadlineList1clone;
    }

    // @@author A0126623L
    @Test
    public void addTest() {
        ObservableList<Entry> observableList = deadlineList1.asObservableList();

        assertTrue(observableList.get(0).equals(deadline1));
        assertTrue(observableList.get(1).equals(deadline3));
        assertFalse(observableList.get(0).equals(deadline4));
    }

    // @@author A0126623L
    @Test
    public void equalsTest() {
        assertTrue(deadlineList1.equals(deadlineList2));
        assertFalse(deadlineList1.equals(deadlineList3));
    }

    // @@author A0126623L
    @Test
    public void removeTest() throws EntryNotFoundException {
        DeadlineList deadlineListToTest = createDeadlineList1();

        deadlineListToTest.remove(deadline1);
        assertTrue(deadlineListToTest.asObservableList().get(0).equals(deadline3));
    }

    // @@author A0126623L
    @Test(expected = Exception.class)
    public void removeTest_returnEntryNotFoundException() throws EntryNotFoundException {
        DeadlineList deadlineListToTest = createDeadlineList1();

        deadlineListToTest.remove(deadline4);
    }

    // @@author A0126623L
    @Test
    public void updateEntryTest() throws EntryNotFoundException {
        DeadlineList deadlineListToTest = createDeadlineList1();

        deadlineListToTest.updateEntry(deadline1, deadline4);
        assertFalse(deadlineList1.asObservableList().get(0).equals(deadline4));
    }

    // @@author A0126623L
    /**
     * Note: This test method relies on the correct functioning of the equals() method.
     */
    @Test
    public void setEntriesTest_newDeadlineList_equalsMethodReturnsFalse() {
        DeadlineList deadlineListToTest = createDeadlineList1();

        deadlineListToTest.setEntries(deadlineList3);
        assertFalse(deadlineListToTest.equals(createDeadlineList1()));
    }

    // @@author A0126623L
    @Test
    public void setEntriesTest() {
        DeadlineList deadlineListToTest = createDeadlineList1();

        ArrayList<Deadline> deadlineArrayList = new ArrayList<>();
        deadlineArrayList.add(deadline1);
        deadlineArrayList.add(deadline4);
        // deadlineArrayList holds the same elements as that of deadlineList3.

        deadlineListToTest.setEntries(deadlineArrayList);
        assertTrue(deadlineListToTest.equals(deadlineList3));
    }

}
