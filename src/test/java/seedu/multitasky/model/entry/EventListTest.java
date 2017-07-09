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
public class EventListTest {

    static Calendar calendar1;
    static Calendar calendar2;
    static Calendar calendar3;

    static Set<Tag> tagSet1;
    static Set<Tag> tagSet2;

    static Name eventName1;
    static Name eventName2;
    static Name eventName3;

    static Event event1;
    static Event event2;
    static Event event3;
    static Event event4;
    static Event event5;

    static EventList eventList1;
    static EventList eventList2; // This list will be meaningfully equal to eventList1
    static EventList eventList3; // This list will be different from eventList1 and eventList2

    @BeforeClass
    public static void setUp() throws Exception {
        eventName1 = new Name("sampleName1");
        eventName2 = new Name("sampleName2");
        eventName3 = new Name("sampleName3");

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
        event1 = new Event(eventName1, calendar1, calendar2, tagSet1);
        // Same fields as tester1
        event2 = new Event(eventName1, calendar1, calendar2, tagSet1);
        // Only name is different from tester1
        event3 = new Event(eventName2, calendar1, calendar2, tagSet1);
        // Only start time is different from tester1
        event4 = new Event(eventName1, calendar2, calendar3, tagSet1);
        // Only tags are different from tester1
        event5 = new Event(eventName1, calendar2, calendar3, tagSet2);

        eventList1 = new EventList();
        eventList2 = new EventList();
        eventList3 = new EventList();

        eventList1.add(event1);
        eventList1.add(event3);

        eventList2.add(event1);
        eventList2.add(event3);

        eventList3.add(event1);
        eventList3.add(event4);
    }

    // @@author A0126623L
    /**
     * Create an EventList with {event1, event3}
     */
    public static EventList createEventList1() {
        EventList eventList1clone = new EventList();
        eventList1clone.add(event1);
        eventList1clone.add(event3);

        return eventList1clone;
    }

    // @@author A0126623L
    @Test
    public void addTest() {
        ObservableList<Entry> observableList = eventList1.asObservableList();

        assertTrue(observableList.get(0).equals(event1));
        assertTrue(observableList.get(1).equals(event3));
        assertFalse(observableList.get(0).equals(event4));
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

        eventListToTest.remove(event1);
        assertTrue(eventListToTest.asObservableList().get(0).equals(event3));
    }

    // @@author A0126623L
    @Test(expected = Exception.class)
    public void removeTest_returnEntryNotFoundException() throws EntryNotFoundException {
        EventList eventListToTest = createEventList1();

        eventListToTest.remove(event4);
    }

    // @@author A0126623L
    @Test
    public void updateEntryTest() throws EntryNotFoundException {
        EventList eventListToTest = createEventList1();

        eventListToTest.updateEntry(event1, event4);
        assertFalse(eventList1.asObservableList().get(0).equals(event4));
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
    @Test
    public void setEntriesTest() {
        EventList eventListToTest = createEventList1();

        ArrayList<Event> eventArrayList = new ArrayList<>();
        eventArrayList.add(event1);
        eventArrayList.add(event4);
        // eventArrayList holds the same elements as that of eventList3.

        eventListToTest.setEntries(eventArrayList);
        assertTrue(eventListToTest.equals(eventList3));
    }

}
