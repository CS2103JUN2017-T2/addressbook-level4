package seedu.multitasky.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.model.util.EntryBuilder;

public class FloatingTaskListTest {

    private FloatingTaskList floatingTaskList1, floatingTaskList2, floatingTaskList3;
    private FloatingTask[] sampleFloatingTaskArray = FloatingTaskTest.getSampleFloatingTaskArray();

    // @@author A0126623L
    /**
     * Copies the floatingTasks in the given floatingTasks collection into an FloatingTask List.
     *
     * @param floatingTasks cannot be null
     * @return FloatingTaskList
     * @throws DuplicateEntryException if duplicate floatingTasks are given
     */
    public static FloatingTaskList getFloatingTaskList(FloatingTask... floatingTasks)
            throws DuplicateEntryException {
        FloatingTaskList floatingTaskList = new FloatingTaskList();
        for (FloatingTask floatingTask : floatingTasks) {
            Objects.requireNonNull(floatingTask);
            floatingTaskList.add(EntryBuilder.build(floatingTask));
        }
        return floatingTaskList;
    }

    // @@author A0126623L
    /**
     * Copies all the elements of a given floatingTask list into a new floatingTask list
     *
     * @param floatingTaskListToCopy
     * @return copiedFloatingTaskList
     */
    public static FloatingTaskList copyFloatingTaskList(FloatingTaskList floatingTaskListToCopy) {
        FloatingTaskList copiedFloatingTaskList = new FloatingTaskList();
        try {
            for (Entry e : floatingTaskListToCopy) {
                Objects.requireNonNull(e);
                assert (e instanceof FloatingTask) : "FloatingTaskList copy error: e is not an FloatingTask";
                copiedFloatingTaskList.add(EntryBuilder.build(e));
            }
            return copiedFloatingTaskList;
        } catch (DuplicateEntryException e) {
            fail("List with duplicated entries was given to FloatingTaskListTest.copyFloatingTaskList().");
            return null;
        }
    }

    // @@author A0126623L
    /**
     * Generates an array of 3 FloatingTaskList samples.
     * The first two floatingTasks are meaningfully equivalent, the third one is unique.
     */
    public static FloatingTaskList[] getListOfSampleFloatingTaskLists() {

        FloatingTaskList floatingTaskList1, floatingTaskList2, floatingTaskList3;

        FloatingTask[] sampleFloatingTaskArray = FloatingTaskTest.getSampleFloatingTaskArray();
        try {
            floatingTaskList1 = FloatingTaskListTest.getFloatingTaskList(sampleFloatingTaskArray[0],
                                                                         sampleFloatingTaskArray[2]);
            floatingTaskList2 = FloatingTaskListTest.getFloatingTaskList(sampleFloatingTaskArray[0],
                                                                         sampleFloatingTaskArray[2]);
            floatingTaskList3 = FloatingTaskListTest.getFloatingTaskList(sampleFloatingTaskArray[0],
                                                                         sampleFloatingTaskArray[3]);

            return new FloatingTaskList[] { floatingTaskList1, floatingTaskList2, floatingTaskList3 };
        } catch (DuplicateEntryException e) {
            fail("Error in FloatingTaskListTest.getSampleFloatingTaskListArrayData() due to duplication.");
            return null;
        }
    }

    @Before
    public void setUp() {
        FloatingTaskList[] listOfFloatingTaskList = FloatingTaskListTest.getListOfSampleFloatingTaskLists();
        floatingTaskList1 = listOfFloatingTaskList[0];
        floatingTaskList2 = listOfFloatingTaskList[1];
        floatingTaskList3 = listOfFloatingTaskList[2];
    }

    // @@author A0126623L
    /**
     * Tests if sample entries used in this test class are considered equal when necessary.
     */
    @Test
    public void millisecondsDiffNotConsideredDifferent() {
        /*
         * Because of the way they are instantiated, the start time of
         * the floatingTasks of sampleFloatingTaskArray's and floatingTaskList1's first floatingTask
         * element are different by milliseconds. This should not be
         * considered different as the constructor reset milliseconds to
         * zero.
         */
        System.out.println(sampleFloatingTaskArray[0].getEndDateAndTime().toString());
        System.out.println(floatingTaskList1.asObservableList().get(0).getEndDateAndTime().toString());
        assertTrue(sampleFloatingTaskArray[0].equals(floatingTaskList1.asObservableList().get(0)));
    }

    // @@author A0126623L
    /**
     * Dependent on the correct functioning of the contains method.
     */
    @Test
    public void addAndContainsTest() {
        FloatingTaskList floatingTaskListUnderTest = new FloatingTaskList();

        try {
            floatingTaskListUnderTest.add(EntryBuilder.build(sampleFloatingTaskArray[0]));
            floatingTaskListUnderTest.add(sampleFloatingTaskArray[2]);

            assertTrue(floatingTaskListUnderTest.contains(sampleFloatingTaskArray[0]));
            assertTrue(floatingTaskListUnderTest.contains(sampleFloatingTaskArray[2]));
            assertFalse(floatingTaskListUnderTest.contains(sampleFloatingTaskArray[3]));

        } catch (DuplicateEntryException e) {
            e.printStackTrace();
        }
    }

    // @@author A0126623L
    @Test(expected = DuplicateEntryException.class)
    public void addTest_duplicateFloatingTask_throwDuplicateEntryException() throws DuplicateEntryException {
        FloatingTaskList floatingTaskListUnderTest = FloatingTaskListTest.copyFloatingTaskList(floatingTaskList1);
        FloatingTask copiedFloatingTask = (FloatingTask) EntryBuilder.build(floatingTaskListUnderTest.asObservableList()
                                                                                                     .get(0));
        floatingTaskListUnderTest.add(copiedFloatingTask);
    }

    // @@author A0126623L
    @Test
    public void equalsTest() {
        FloatingTaskList dummyFloatingTaskList = FloatingTaskListTest.copyFloatingTaskList(floatingTaskList2);
        assertTrue(floatingTaskList1.equals(floatingTaskList2));
        assertTrue(floatingTaskList1.equals(dummyFloatingTaskList));

        dummyFloatingTaskList.asObservableList().get(0).setState(Entry.State.ARCHIVED);
        assertFalse(floatingTaskList1.equals(dummyFloatingTaskList));

        assertFalse(floatingTaskList1.equals(floatingTaskList3));
    }

    // @@author A0126623L
    @Test
    public void removeTest() throws EntryNotFoundException {
        FloatingTaskList floatingTaskListToTest = FloatingTaskListTest.copyFloatingTaskList(floatingTaskList1);

        floatingTaskListToTest.remove(sampleFloatingTaskArray[0]);
        assertTrue(!floatingTaskListToTest.contains(sampleFloatingTaskArray[0]));
        assertTrue(floatingTaskListToTest.asObservableList().get(0).equals(sampleFloatingTaskArray[2]));
    }

    // @@author A0126623L
    @Test(expected = Exception.class)
    public void removeTest_returnEntryNotFoundException() throws EntryNotFoundException {
        FloatingTaskList floatingTaskListUnderTest = FloatingTaskListTest.copyFloatingTaskList(floatingTaskList1);

        floatingTaskListUnderTest.remove(sampleFloatingTaskArray[3]);
    }

    // @@author A0126623L
    @Test
    public void updateEntryAndEqualsTest() throws EntryNotFoundException {
        FloatingTaskList floatingTaskListToTest = FloatingTaskListTest.copyFloatingTaskList(floatingTaskList1);
        assertTrue(floatingTaskListToTest.equals(floatingTaskList1));
        assertFalse(floatingTaskListToTest.contains(sampleFloatingTaskArray[3]));
        try {
            floatingTaskListToTest.updateEntry(sampleFloatingTaskArray[0], sampleFloatingTaskArray[3]);

            assertFalse(floatingTaskListToTest.equals(floatingTaskList1));
            assertTrue(floatingTaskListToTest.contains(sampleFloatingTaskArray[3]));

        } catch (DuplicateEntryException e) {
            fail("FloatingTaskListTest.updateEntryTest() failed due to duplicate entry.");
        }
    }

    // @@author A0126623L
    /**
     * Note: This test method relies on the correct functioning of the equals() method.
     */
    @Test
    public void setEntriesTest_newFloatingTaskList_equalsMethodReturnsFalse() {
        FloatingTaskList floatingTaskListToTest = FloatingTaskListTest.copyFloatingTaskList(floatingTaskList3);
        assertTrue(floatingTaskListToTest.equals(floatingTaskList3));

        assertTrue(!floatingTaskListToTest.equals(floatingTaskList1));
        floatingTaskListToTest.setEntries(floatingTaskList1);
        assertTrue(floatingTaskListToTest.equals(floatingTaskList1));
    }

}
