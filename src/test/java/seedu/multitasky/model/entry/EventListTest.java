package seedu.multitasky.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
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

    /**
     * Tests if sample entries used in this test class are considered equal when necessary.
     */
    @Test
    public void equals_millisecondsDifference_consideredEqual() {
        /*
         * Because of the way they are instantiated, the start time of
         * the events of sampleEventArray's and eventList1's first event
         * element are different by milliseconds. This should not be
         * considered different as the constructor reset milliseconds to
         * zero.
         */
        assertTrue(sampleEventArray[0].equals(eventList1.asObservableList().get(0)));
    }

    /**
     * Dependent on the correct functioning of the contains method.
     */
    @Test
    public void add_sampleEvent_success() {
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

    @Test(expected = DuplicateEntryException.class)
    public void add_duplicateEvent_throwDuplicateEntryException() throws DuplicateEntryException {
        EventList eventListUnderTest = EventListTest.copyEventList(eventList1);
        Event copiedEvent = (Event) EntryBuilder.build(eventListUnderTest.asObservableList().get(0));
        eventListUnderTest.add(copiedEvent);
    }

    @Test
    public void equals_variousSampleEvents_success() {
        EventList dummyEventList = EventListTest.copyEventList(eventList2);
        assertTrue(eventList1.equals(eventList2));
        assertTrue(eventList1.equals(dummyEventList));

        dummyEventList.asObservableList().get(0).setState(Entry.State.ARCHIVED);
        assertFalse(eventList1.equals(dummyEventList));

        assertFalse(eventList1.equals(eventList3));
    }

    @Test
    public void remove_removeSampleEvent_success() throws EntryNotFoundException {
        EventList eventListToTest = EventListTest.copyEventList(eventList1);

        eventListToTest.remove(sampleEventArray[0]);
        assertTrue(!eventListToTest.contains(sampleEventArray[0]));
        assertTrue(eventListToTest.asObservableList().get(0).equals(sampleEventArray[2]));
    }

    @Test(expected = Exception.class)
    public void remove_nonExistentEntry_returnEntryNotFoundException() throws EntryNotFoundException {
        EventList eventListUnderTest = EventListTest.copyEventList(eventList1);

        eventListUnderTest.remove(sampleEventArray[3]);
    }

    @Test
    public void update_updateSampleEvent_success() throws EntryNotFoundException {
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

    @Test
    public void setEntries_newEventList_equalsMethodReturnsFalse() {
        EventList eventListToTest = EventListTest.copyEventList(eventList3);
        assertTrue(eventListToTest.equals(eventList3));

        assertTrue(!eventListToTest.equals(eventList1));
        eventListToTest.setEntries(eventList1);
        assertTrue(eventListToTest.equals(eventList1));
    }

    @Test
    public void hasOverlapping_overlappingAndNonOverlappingSampleEvents_success() {
        int offsetAmount = 1000;

        Entry overlappingEventToAdd = this.sampleEventArray[0];
        assertTrue(eventList1.hasOverlappingEvent(overlappingEventToAdd));
        try {
            Entry nonOverlappingEvent = EntryBuilder.build(new Name("nonOverlappingEventName"),
                                                           Calendar.getInstance(),
                                                           Calendar.getInstance(),
                                                           "tag1");
            nonOverlappingEvent.getEndDateAndTime().add(Calendar.YEAR, offsetAmount + 1);
            nonOverlappingEvent.getStartDateAndTime().add(Calendar.YEAR, offsetAmount);
            assertFalse(eventList1.hasOverlappingEvent(nonOverlappingEvent));
        } catch (Exception e) {
            fail("Should not fail.");
        }
    }

    @Test
    public void hasOverlappingEventAfterUpdate_overlappingEventAfterUpdate_success() {
        Entry eventToManipulate = eventList1.asObservableList().get(0);
        eventToManipulate.setState(Entry.State.DELETED);
        assertTrue(eventList1.asObservableList().get(0).isDeleted());

        Entry activatedManipulatedEvent = EntryBuilder.build(eventToManipulate);
        activatedManipulatedEvent.setState(Entry.State.ACTIVE);

        assertFalse(activatedManipulatedEvent.equals(eventToManipulate));
        assertTrue(eventList1.hasOverlappingEventAfterUpdate(eventToManipulate, activatedManipulatedEvent));
    }

}
