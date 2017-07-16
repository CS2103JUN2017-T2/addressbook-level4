package seedu.multitasky.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.DeadlineList;
import seedu.multitasky.model.entry.DeadlineListTest;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.EventList;
import seedu.multitasky.model.entry.EventListTest;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.FloatingTaskList;
import seedu.multitasky.model.entry.FloatingTaskListTest;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

public class EntryBookTest {

    private EventList[] listOfEventLists = EventListTest.getListOfSampleEventLists();
    private DeadlineList[] listOfDeadlineLists = DeadlineListTest.getListOfSampleDeadlineLists();
    private FloatingTaskList[] listOfFloatingTaskLists = FloatingTaskListTest.getListOfSampleFloatingTaskLists();

    // @@author A0126623L
    /**
     * @return EntryBook    with sample events, deadlines and floating tasks.
     * @throws DuplicateEntryException
     */
    public static EntryBook getSampleEntryBook() throws DuplicateEntryException {

        EventList[] listOfEventLists = EventListTest.getListOfSampleEventLists();
        DeadlineList[] listOfDeadlineLists = DeadlineListTest.getListOfSampleDeadlineLists();
        FloatingTaskList[] listOfFloatingTaskLists = FloatingTaskListTest.getListOfSampleFloatingTaskLists();

        EntryBook entryBook = new EntryBook();
        for (Entry event : listOfEventLists[0]) {
            assertTrue(event instanceof Event);
            entryBook.addEntry(event);
        }

        for (Entry event : listOfDeadlineLists[0]) {
            assertTrue(event instanceof Deadline);
            entryBook.addEntry(event);
        }

        for (Entry event : listOfFloatingTaskLists[0]) {
            assertTrue(event instanceof FloatingTask);
            entryBook.addEntry(event);
        }

        return entryBook;
    }
    // @@author

    // @@author A0126623L
    @Test
    public void addTest() {
        try {
            EntryBook entryBookUnderTest = EntryBookTest.getSampleEntryBook();

            List<ReadOnlyEntry> actualEventList = entryBookUnderTest.getEventList();
            List<Entry> eventListExpected = listOfEventLists[0].asObservableList();
            assertTrue(actualEventList.equals(eventListExpected));

            List<ReadOnlyEntry> actualDeadlineList = entryBookUnderTest.getDeadlineList();
            List<Entry> deadlineListExpected = listOfDeadlineLists[0].asObservableList();
            assertTrue(actualDeadlineList.equals(deadlineListExpected));

            List<ReadOnlyEntry> actualFloatingTaksList = entryBookUnderTest.getFloatingTaskList();
            List<Entry> floatingTaskListExpected = listOfFloatingTaskLists[0].asObservableList();
            assertTrue(actualFloatingTaksList.equals(floatingTaskListExpected));

        } catch (DuplicateEntryException e) {
            fail("Error in EntryBookTest.getSampleEntryBook().");
        }
    }
    // @@author

    // @@author A0126623L
    @Test
    public void removeTest() throws EntryNotFoundException {
        try {
            EntryBook entryBookUnderTest = EntryBookTest.getSampleEntryBook();

            Entry eventToRemove = listOfEventLists[0].asObservableList().get(0);
            assertTrue(entryBookUnderTest.getActiveList().contains(eventToRemove));
            Entry deadlineToRemove = listOfDeadlineLists[0].asObservableList().get(0);
            assertTrue(entryBookUnderTest.getActiveList().contains(deadlineToRemove));
            Entry floatingTaskToRemove = listOfFloatingTaskLists[0].asObservableList().get(0);
            assertTrue(entryBookUnderTest.getActiveList().contains(floatingTaskToRemove));

            entryBookUnderTest.removeEntry(eventToRemove);
            assertFalse(entryBookUnderTest.getActiveList().contains(eventToRemove));
            entryBookUnderTest.removeEntry(deadlineToRemove);
            assertFalse(entryBookUnderTest.getActiveList().contains(deadlineToRemove));
            entryBookUnderTest.removeEntry(floatingTaskToRemove);
            assertFalse(entryBookUnderTest.getActiveList().contains(floatingTaskToRemove));

            try {
                entryBookUnderTest.removeEntry(eventToRemove);
                fail("Should not reach here.");
            } catch (EntryNotFoundException e) {
                // Do nothing. EntryNotFoundException is to be expected here.
            }

            try {
                entryBookUnderTest.removeEntry(deadlineToRemove);
                fail("Should not reach here.");
            } catch (EntryNotFoundException e) {
                // Do nothing. EntryNotFoundException is to be expected here.
            }

            try {
                entryBookUnderTest.removeEntry(floatingTaskToRemove);
                fail("Should not reach here.");
            } catch (EntryNotFoundException e) {
                // Do nothing. EntryNotFoundException is to be expected here.
            }
        } catch (DuplicateEntryException e) {
            fail("Error in EntryBookTest.getSampleEntryBook().");
        }
    }
    // @@author

}
