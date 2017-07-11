package seedu.multitasky.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;

import javafx.collections.ObservableList;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

//@@author A0126623L
/**
 * TODO: Make SetUp less dependent on actual classes, e.g. create utility classes to generate
 * Events, Deadlines and Floating Tasks.
 */
public class EventListTest {

    public static final EventList[] SAMPLE_EVENT_LISTS_ARRAY_DATA = getSampleEventListArrayData();

    static Event[] sampleEvents = EventTest.SAMPLE_EVENTS_ARRAY_DATA;

    // @@author A0126623L
    /**
     * Generates an array of EventList samples.
     * The first two EventList objects are meaningfully equivalent, the third one is unique.
     */
    public static EventList[] getSampleEventListArrayData() {

        Event[] sampleEvents = EventTest.SAMPLE_EVENTS_ARRAY_DATA;
        try {
            EventList eventList1 = new EventList();
            EventList eventList2 = new EventList();
            EventList eventList3 = new EventList();

            eventList1.add(sampleEvents[0]);
            eventList1.add(sampleEvents[2]);

            eventList2.add(sampleEvents[0]);
            eventList2.add(sampleEvents[2]);

            eventList3.add(sampleEvents[0]);
            eventList3.add(sampleEvents[3]);

            return new EventList[] { eventList1, eventList2, eventList3 };
        } catch (DuplicateEntryException e) {
            fail("Error in EventListTest.getSampleEventListArrayData() due to duplication.");
            return null;
        }
    }

    EventList eventList1 = SAMPLE_EVENT_LISTS_ARRAY_DATA[0];
    EventList eventList2 = SAMPLE_EVENT_LISTS_ARRAY_DATA[1];
    EventList eventList3 = SAMPLE_EVENT_LISTS_ARRAY_DATA[2];

    // @@author A0126623L
    /**
     * Create an EventList with {sampleEvents[0], sampleEvents[2]}
     */
    private EventList createEventList1() {
        try {
            EventList eventList1clone = new EventList();
            eventList1clone.add(sampleEvents[0]);
            eventList1clone.add(sampleEvents[2]);

            return eventList1clone;
        } catch (DuplicateEntryException e) {
            fail("EventListTest.createEventList1() fails due to duplicate entries.");
            return null;
        }
    }

    // @@author A0126623L
    @Test
    public void addTest() {
        ObservableList<Entry> observableList = eventList1.asObservableList();

        assertTrue(observableList.get(0).equals(sampleEvents[0]));
        assertTrue(observableList.get(1).equals(sampleEvents[2]));
        assertFalse(observableList.get(0).equals(sampleEvents[3]));
    }

    // @@author A0126623L
    @Test
    public void equalsTest() {
        assertTrue(eventList1.equals(eventList2));
        assertFalse(eventList1.equals(eventList3));
    }

    // @@author A0126623L
    @Test
    public void removeTest() throws EntryNotFoundException {
        EventList eventListToTest = createEventList1();

        eventListToTest.remove(sampleEvents[0]);
        assertTrue(eventListToTest.asObservableList().get(0).equals(sampleEvents[2]));
    }

    // @@author A0126623L
    @Test(expected = Exception.class)
    public void removeTest_returnEntryNotFoundException() throws EntryNotFoundException {
        EventList eventListToTest = createEventList1();

        eventListToTest.remove(sampleEvents[3]);
    }

    // @@author A0126623L
    @Test
    public void updateEntryTest() throws EntryNotFoundException, DuplicateEntryException {
        EventList eventListToTest = createEventList1();
        try {
            eventListToTest.updateEntry(sampleEvents[0], sampleEvents[3]);
            assertFalse(eventList1.asObservableList().get(0).equals(sampleEvents[3]));
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
        EventList eventListToTest = createEventList1();

        eventListToTest.setEntries(eventList3);
        assertFalse(eventListToTest.equals(createEventList1()));
    }

    // @@author A0126623L
    @Ignore
    @Test
    public void setEntriesTest() throws DuplicateEntryException {
        EventList eventListToTest = createEventList1();

        ArrayList<Event> eventArrayList = new ArrayList<>();
        eventArrayList.add(sampleEvents[0]);
        eventArrayList.add(sampleEvents[3]);
        // eventArrayList holds the same elements as that of eventList3.

        try {
            eventListToTest.setEntries(eventArrayList);
            assertTrue(eventListToTest.equals(eventList3));
        } catch (DuplicateEntryException e) {
            fail("EventListTest.setEntriesTest() failed due to duplicate entry.");
        }
    }

}
