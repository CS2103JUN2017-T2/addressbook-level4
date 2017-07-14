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

// @@author A0126623L
public class EventListTest {

    private EventList eventList1, eventList2, eventList3;
    private Event[] sampleEventArray = EventTest.getSampleEventArray();

    // @@author A0126623L
    /**
     * Copies the events in the given events collection into an Event List.
     *
     * @param events cannot be null
     * @return EventList
     * @throws DuplicateEntryException if duplicate events are given
     */
    public static EventList getEventList(Event... events) throws DuplicateEntryException {
        EventList eventList = new EventList();
        for (Event event : events) {
            Objects.requireNonNull(event);
            eventList.add(EntryBuilder.build(event));
        }
        return eventList;
    }

    // @@author A0126623L
    /**
     * Copies all the elements of a given event list into a new event list
     *
     * @param eventListToCopy
     * @return copiedEventList
     */
    public static EventList copyEventList(EventList eventListToCopy) {
        EventList copiedEventList = new EventList();
        try {
            for (Entry e : eventListToCopy) {
                Objects.requireNonNull(e);
                assert (e instanceof Event) : "EventList copy error: e is not an Event";
                copiedEventList.add(EntryBuilder.build(e));
            }
            return copiedEventList;
        } catch (DuplicateEntryException e) {
            fail("List with duplicated entries was given to EventListTest.copyEventList().");
            return null;
        }
    }

    // @@author A0126623L
    /**
     * Generates an array of 3 EventList samples.
     * The first two EventList objects are meaningfully equivalent, the third one is unique.
     */
    public static EventList[] getListOfSampleEventLists() {

        EventList eventList1, eventList2, eventList3;

        Event[] sampleEventArray = EventTest.getSampleEventArray();
        try {
            eventList1 = EventListTest.getEventList(sampleEventArray[0], sampleEventArray[2]);
            eventList2 = EventListTest.getEventList(sampleEventArray[0], sampleEventArray[2]);
            eventList3 = EventListTest.getEventList(sampleEventArray[0], sampleEventArray[3]);

            return new EventList[] { eventList1, eventList2, eventList3 };
        } catch (DuplicateEntryException e) {
            fail("Error in EventListTest.getSampleEventListArrayData() due to duplication.");
            return null;
        }
    }

    @Before
    public void setUp() {
        EventList[] listOfEventList = EventListTest.getListOfSampleEventLists();
        eventList1 = listOfEventList[0];
        eventList2 = listOfEventList[1];
        eventList3 = listOfEventList[2];
    }

    // @@author A0126623L
    /**
     * Tests if sample entries used in this test class are considered equal when necessary.
     */
    @Test
    public void millisecondsDiffNotConsideredDifferent() {
        /*
         * Because of the way they are instantiated, the start time of
         * the events of sampleEventArray's and eventList1's first event
         * element are different by milliseconds. This should not be
         * considered different as the constructor reset milliseconds to
         * zero.
         */
        assertTrue(sampleEventArray[0].equals(eventList1.asObservableList().get(0)));
    }

    // @@author A0126623L
    /**
     * Dependent on the correct functioning of the contains method.
     */
    @Test
    public void addAndContainsTest() {
        EventList eventListUnderTest = new EventList();

        try {
            eventListUnderTest.add(EntryBuilder.build(sampleEventArray[0]));
            eventListUnderTest.add(sampleEventArray[2]);

            assertTrue(eventListUnderTest.contains(sampleEventArray[0]));
            assertTrue(eventListUnderTest.contains(sampleEventArray[2]));
            assertFalse(eventListUnderTest.contains(sampleEventArray[3]));

        } catch (DuplicateEntryException e) {
            e.printStackTrace();
        }
    }

    // @@author A0126623L
    @Test(expected = DuplicateEntryException.class)
    public void addTest_duplicateEvent_throwDuplicateEntryException() throws DuplicateEntryException {
        EventList eventListUnderTest = EventListTest.copyEventList(eventList1);
        Event copiedEvent = (Event) EntryBuilder.build(eventListUnderTest.asObservableList().get(0));
        eventListUnderTest.add(copiedEvent);
    }

    // @@author A0126623L
    @Test
    public void equalsTest() {
        EventList dummyEventList = EventListTest.copyEventList(eventList2);
        assertTrue(eventList1.equals(eventList2));
        assertTrue(eventList1.equals(dummyEventList));

        dummyEventList.asObservableList().get(0).setState(Entry.State.ARCHIVED);
        assertFalse(eventList1.equals(dummyEventList));

        assertFalse(eventList1.equals(eventList3));
    }

    // @@author A0126623L
    @Test
    public void removeTest() throws EntryNotFoundException {
        EventList eventListToTest = EventListTest.copyEventList(eventList1);

        eventListToTest.remove(sampleEventArray[0]);
        assertTrue(!eventListToTest.contains(sampleEventArray[0]));
        assertTrue(eventListToTest.asObservableList().get(0).equals(sampleEventArray[2]));
    }

    // @@author A0126623L
    @Test(expected = Exception.class)
    public void removeTest_returnEntryNotFoundException() throws EntryNotFoundException {
        EventList eventListUnderTest = EventListTest.copyEventList(eventList1);

        eventListUnderTest.remove(sampleEventArray[3]);
    }

    // @@author A0126623L
    @Test
    public void updateEntryAndEqualsTest() throws EntryNotFoundException {
        EventList eventListToTest = EventListTest.copyEventList(eventList1);
        assertTrue(eventListToTest.equals(eventList1));
        assertFalse(eventListToTest.contains(sampleEventArray[3]));
        try {
            eventListToTest.updateEntry(sampleEventArray[0], sampleEventArray[3]);

            assertFalse(eventListToTest.equals(eventList1));
            assertTrue(eventListToTest.contains(sampleEventArray[3]));

        } catch (DuplicateEntryException e) {
            fail("EventListTest.updateEntryTest() failed due to duplicate entry.");
        }
    }

    // @@author A0126623L
    /**
     * Note: This test method relies on the correct functioning of the equals() method.
     */
    @Test
    public void setEntriesTest_newEventList_equalsMethodReturnsFalse() {
        EventList eventListToTest = EventListTest.copyEventList(eventList3);
        assertTrue(eventListToTest.equals(eventList3));

        assertTrue(!eventListToTest.equals(eventList1));
        eventListToTest.setEntries(eventList1);
        assertTrue(eventListToTest.equals(eventList1));
    }
}
