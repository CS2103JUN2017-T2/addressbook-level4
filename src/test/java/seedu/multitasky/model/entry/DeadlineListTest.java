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

//@@author A0126623L
public class DeadlineListTest {

    private DeadlineList deadlineList1, deadlineList2, deadlineList3;
    private Deadline[] sampleDeadlineArray = DeadlineTest.getSampleDeadlineArray();

    /**
     * Copies the deadlines in the given deadlines collection into an Deadline List.
     *
     * @param deadlines cannot be null
     * @return DeadlineList
     * @throws DuplicateEntryException if duplicate deadlines are given
     */
    public static DeadlineList getDeadlineList(Deadline... deadlines) throws DuplicateEntryException {
        DeadlineList deadlineList = new DeadlineList();
        for (Deadline deadline : deadlines) {
            Objects.requireNonNull(deadline);
            deadlineList.add(EntryBuilder.build(deadline));
        }
        return deadlineList;
    }

    /**
     * Copies all the elements of a given deadline list into a new deadline list
     *
     * @param deadlineListToCopy
     * @return copiedDeadlineList
     */
    public static DeadlineList copyDeadlineList(DeadlineList deadlineListToCopy) {
        DeadlineList copiedDeadlineList = new DeadlineList();
        try {
            for (Entry e : deadlineListToCopy) {
                Objects.requireNonNull(e);
                assert (e instanceof Deadline) : "DeadlineList copy error: e is not an Deadline";
                copiedDeadlineList.add(EntryBuilder.build(e));
            }
            return copiedDeadlineList;
        } catch (DuplicateEntryException e) {
            fail("List with duplicated entries was given to DeadlineListTest.copyDeadlineList().");
            return null;
        }
    }

    /**
     * Generates an array of 3 DeadlineList samples.
     * The first two deadlines are meaningfully equivalent, the third one is unique.
     */
    public static DeadlineList[] getListOfSampleDeadlineLists() {

        DeadlineList deadlineList1, deadlineList2, deadlineList3;

        Deadline[] sampleDeadlineArray = DeadlineTest.getSampleDeadlineArray();
        try {
            deadlineList1 = DeadlineListTest.getDeadlineList(sampleDeadlineArray[0], sampleDeadlineArray[2]);
            deadlineList2 = DeadlineListTest.getDeadlineList(sampleDeadlineArray[0], sampleDeadlineArray[2]);
            deadlineList3 = DeadlineListTest.getDeadlineList(sampleDeadlineArray[0], sampleDeadlineArray[3]);

            return new DeadlineList[] { deadlineList1, deadlineList2, deadlineList3 };
        } catch (DuplicateEntryException e) {
            fail("Error in DeadlineListTest.getSampleDeadlineListArrayData() due to duplication.");
            return null;
        }
    }

    @Before
    public void setUp() {
        DeadlineList[] listOfDeadlineList = DeadlineListTest.getListOfSampleDeadlineLists();
        deadlineList1 = listOfDeadlineList[0];
        deadlineList2 = listOfDeadlineList[1];
        deadlineList3 = listOfDeadlineList[2];
    }

    /**
     * Tests if sample entries used in this test class are considered equal when necessary.
     */
    @Test
    public void equals_millisecondsDifference_consideredEqual() {
        /*
         * Because of the way they are instantiated, the start time of
         * the deadlines of sampleDeadlineArray's and deadlineList1's first deadline
         * element are different by milliseconds. This should not be
         * considered different as the constructor reset milliseconds to
         * zero.
         */
        assertTrue(sampleDeadlineArray[0].equals(deadlineList1.asObservableList().get(0)));
    }

    // @@author A0126623L
    /**
     * Dependent on the correct functioning of the contains method.
     */
    @Test
    public void add_sampleDeadline_success() {
        DeadlineList deadlineListUnderTest = new DeadlineList();

        try {
            deadlineListUnderTest.add(EntryBuilder.build(sampleDeadlineArray[0]));
            deadlineListUnderTest.add(sampleDeadlineArray[2]);

            assertTrue(deadlineListUnderTest.contains(sampleDeadlineArray[0]));
            assertTrue(deadlineListUnderTest.contains(sampleDeadlineArray[2]));
            assertFalse(deadlineListUnderTest.contains(sampleDeadlineArray[3]));

        } catch (DuplicateEntryException e) {
            e.printStackTrace();
        }
    }

    // @@author A0126623L
    @Test(expected = DuplicateEntryException.class)
    public void add_duplicateDeadline_throwDuplicateEntryException() throws DuplicateEntryException {
        DeadlineList deadlineListUnderTest = DeadlineListTest.copyDeadlineList(deadlineList1);
        Deadline copiedDeadline = (Deadline) EntryBuilder.build(deadlineListUnderTest.asObservableList()
                                                                                     .get(0));
        deadlineListUnderTest.add(copiedDeadline);
    }

    // @@author A0126623L
    @Test
    public void equals_variousSampleDeadlines_success() {
        DeadlineList dummyDeadlineList = DeadlineListTest.copyDeadlineList(deadlineList2);
        assertTrue(deadlineList1.equals(deadlineList2));
        assertTrue(deadlineList1.equals(dummyDeadlineList));

        dummyDeadlineList.asObservableList().get(0).setState(Entry.State.ARCHIVED);
        assertFalse(deadlineList1.equals(dummyDeadlineList));

        assertFalse(deadlineList1.equals(deadlineList3));
    }

    // @@author A0126623L
    @Test
    public void remove_removeSampleDeadline_success() throws EntryNotFoundException {
        DeadlineList deadlineListToTest = DeadlineListTest.copyDeadlineList(deadlineList1);

        deadlineListToTest.remove(sampleDeadlineArray[0]);
        assertTrue(!deadlineListToTest.contains(sampleDeadlineArray[0]));
        assertTrue(deadlineListToTest.asObservableList().get(0).equals(sampleDeadlineArray[2]));
    }

    // @@author A0126623L
    @Test(expected = EntryNotFoundException.class)
    public void remove_nonExistentEntry_returnEntryNotFoundException() throws EntryNotFoundException {
        DeadlineList deadlineListUnderTest = DeadlineListTest.copyDeadlineList(deadlineList1);

        deadlineListUnderTest.remove(sampleDeadlineArray[3]);
    }

    // @@author A0126623L
    @Test
    public void update_updateSampleDeadline_success() throws EntryNotFoundException {
        DeadlineList deadlineListToTest = DeadlineListTest.copyDeadlineList(deadlineList1);
        assertTrue(deadlineListToTest.equals(deadlineList1));
        assertFalse(deadlineListToTest.contains(sampleDeadlineArray[3]));
        try {
            deadlineListToTest.updateEntry(sampleDeadlineArray[0], sampleDeadlineArray[3]);

            assertFalse(deadlineListToTest.equals(deadlineList1));
            assertTrue(deadlineListToTest.contains(sampleDeadlineArray[3]));

        } catch (DuplicateEntryException e) {
            fail("DeadlineListTest.updateEntryTest() failed due to duplicate entry.");
        }
    }

    // @@author A0126623L
    @Test
    public void setEntries_newDeadlineList_equalsMethodReturnsFalse() {
        DeadlineList deadlineListToTest = DeadlineListTest.copyDeadlineList(deadlineList3);
        assertTrue(deadlineListToTest.equals(deadlineList3));

        assertTrue(!deadlineListToTest.equals(deadlineList1));
        deadlineListToTest.setEntries(deadlineList1);
        assertTrue(deadlineListToTest.equals(deadlineList1));
    }

}
