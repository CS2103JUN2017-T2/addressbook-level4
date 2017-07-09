package seedu.multitasky.model.entry;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import seedu.multitasky.model.tag.Tag;

public class ActiveListTest {

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

    public ActiveList createActiveList() {
        ActiveList activeListToTest = new ActiveList();
        activeListToTest.add(event1);
        activeListToTest.add(deadline1);
        activeListToTest.add(floatingTask1);
        return activeListToTest;
    }

    @Test
    public void addTest_shouldAddReferenceAndNotCreateNewEntry() {
        ActiveList activeListUnderTest = createActiveList();

        assertSame("ActiveList add method doesn't actually add Entry reference",
                   event1, activeListUnderTest.asObservableList().get(0));
        assertSame("ActiveList add method doesn't actually add Entry reference",
                   deadline1, activeListUnderTest.asObservableList().get(1));
    }

    @Test
    public void setEntriesTest() {
        ActiveList activeListUnderTest = createActiveList();
        ActiveList replacement = new ActiveList();
        replacement.add(floatingTask1);

        assertNotSame("createActiveList() is buggy",
                      activeListUnderTest.asObservableList().get(0),
                      replacement.asObservableList().get(0));

        activeListUnderTest.setEntries(replacement);
        assertSame("setEntries(EntryList) is buggy",
                   activeListUnderTest.asObservableList().get(0),
                   replacement.asObservableList().get(0));
    }

}
