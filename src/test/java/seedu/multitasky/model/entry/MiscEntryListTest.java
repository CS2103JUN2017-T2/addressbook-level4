package seedu.multitasky.model.entry;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import seedu.multitasky.model.tag.Tag;

//@@author A0126623L
/**
 * TODO: Make SetUp less dependent on actual classes, e.g. create utility classes to generate
 * Events, Deadlines and Floating Tasks.
 */
public class MiscEntryListTest {

    static Calendar calendar1;
    static Calendar calendar2;
    static Calendar calendar3;

    static Set<Tag> tagSet1;
    static Set<Tag> tagSet2;

    static Name name1;
    static Name name2;

    static Event event1;
    static Deadline deadline1;
    static FloatingTask floatingTask1;

    // @@author A0126623L
    @BeforeClass
    public static void setUp() throws Exception {

        calendar1 = Calendar.getInstance();
        calendar1.set(2017, 6, 7, 18, 30); // 7th July 2017, 6:30pm
        calendar2 = Calendar.getInstance();
        calendar2.set(2017, 6, 8, 18, 30); // 8th July 2017, 6:30pm

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

        event1 = new Event(name1, calendar1, calendar2, tagSet1);
        deadline1 = new Deadline(name1, calendar1, tagSet1);
        floatingTask1 = new FloatingTask(name1, tagSet1);
    }

    // @@author A0126623L
    public MiscEntryList createMiscEntryList() {
        MiscEntryList miscEntryListToTest = new MiscEntryList();
        try {
            miscEntryListToTest.add(event1);
            miscEntryListToTest.add(deadline1);
            miscEntryListToTest.add(floatingTask1);
            return miscEntryListToTest;
        } catch (DuplicateEntryException e) {
            fail("MiscEntryListTest.createMiscEntryList() failed due to duplicated entry.");
            return null;
        }
    }

    // @@author A0126623L
    @Test
    public void addTest_shouldAddReferenceAndNotCreateCopy() {
        MiscEntryList miscEntryListUnderTest = createMiscEntryList();

        assertSame("ActiveList add method doesn't actually add Entry reference",
                   event1, miscEntryListUnderTest.asObservableList().get(0));
        assertSame("ActiveList add method doesn't actually add Entry reference",
                   deadline1, miscEntryListUnderTest.asObservableList().get(1));
        try {
            event1.setName(new Name("newName"));
        } catch (Exception e) {
            fail("event1.setName() failed.");
        }
        assertSame("ActiveList add method doesn't actually add Entry reference",
                   miscEntryListUnderTest.asObservableList().get(0).getName(),
                   event1.getName());

    }

    // @@author A0126623L
    @Test
    public void setEntriesTest() throws DuplicateEntryException {
        MiscEntryList miscEntryListUnderTest = createMiscEntryList();
        MiscEntryList replacement = new MiscEntryList();
        replacement.add(floatingTask1);

        assertNotSame("createActiveList() is buggy",
                      miscEntryListUnderTest.asObservableList().get(0),
                      replacement.asObservableList().get(0));

        miscEntryListUnderTest.setEntries(replacement);
        assertSame("setEntries(EntryList) is buggy",
                   miscEntryListUnderTest.asObservableList().get(0),
                   replacement.asObservableList().get(0));
    }

    // @@author A0126623L
    @Test
    public void equalsTest() {
        MiscEntryList miscEntryList1 = createMiscEntryList();
        MiscEntryList miscEntryList2 = createMiscEntryList();
        // miscEntryList1 and 2 are different objects but they are meaningfully equivalent

        assertNotSame("miscEntryList1 and 2 are wrongly initialised.",
                      miscEntryList1,
                      miscEntryList2);
        assertTrue(miscEntryList1.equals(miscEntryList2));
    }

    // @@author A0126623L
    @Test
    public void removeTest() throws DuplicateEntryException {
        EventList eventList = new EventList();
        Event dummyEvent = new Event(name1, calendar1, calendar2, tagSet1);

        eventList.add(dummyEvent);

        MiscEntryList miscEntryListUnderTest = new MiscEntryList();
        miscEntryListUnderTest.add(eventList.asObservableList().get(0));
        miscEntryListUnderTest.add(event1);

        assertSame("Error in MiscEntryListTest.removeTest().",
                   eventList.asObservableList().get(0),
                   miscEntryListUnderTest.asObservableList().get(0));

        try {
            miscEntryListUnderTest.remove(dummyEvent);
            assertSame("Error during removal.",
                       miscEntryListUnderTest.asObservableList().get(0),
                       event1);
        } catch (Exception e) {
            fail("Entry not found during removal.");
        }

    }

}
