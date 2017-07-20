package seedu.multitasky.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import seedu.multitasky.commons.exceptions.IllegalValueException;
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
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.model.entry.exceptions.EntryOverdueException;
import seedu.multitasky.model.entry.exceptions.OverlappingAndOverdueEventException;
import seedu.multitasky.model.entry.exceptions.OverlappingEventException;
import seedu.multitasky.model.util.EntryBuilder;

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
            try {
                entryBook.addEntry(event);
            } catch (OverlappingEventException oee) {
                // Do nothing. OverlappingEventException is to be expected here.
            } catch (OverlappingAndOverdueEventException e) {
                // Do nothing. Overlapping and overdue entries are fine.
            } catch (EntryOverdueException e) {
                // Do nothing. Overdue entries are fine.
            }
        }

        for (Entry event : listOfDeadlineLists[0]) {
            assertTrue(event instanceof Deadline);
            try {
                entryBook.addEntry(event);
            } catch (OverlappingEventException oee) {
                // Do nothing. OverlappingEventException is to be expected here.
            } catch (OverlappingAndOverdueEventException e) {
                // Do nothing. Overlapping and overdue entries are fine.
            } catch (EntryOverdueException e) {
                // Do nothing. Overdue entries are fine.
            }
        }

        for (Entry event : listOfFloatingTaskLists[0]) {
            assertTrue(event instanceof FloatingTask);
            try {
                entryBook.addEntry(event);
            } catch (OverlappingEventException oee) {
                // Do nothing. OverlappingEventException is to be expected here.
            } catch (OverlappingAndOverdueEventException e) {
                // Do nothing. Overlapping and overdue entries are fine.
            } catch (EntryOverdueException e) {
                // Do nothing. Overdue entries are fine.
            }
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
            assertTrue(entryBookUnderTest.getAllEntries().contains(eventToRemove));
            Entry deadlineToRemove = listOfDeadlineLists[0].asObservableList().get(0);
            assertTrue(entryBookUnderTest.getAllEntries().contains(deadlineToRemove));
            Entry floatingTaskToRemove = listOfFloatingTaskLists[0].asObservableList().get(0);
            assertTrue(entryBookUnderTest.getAllEntries().contains(floatingTaskToRemove));

            entryBookUnderTest.removeEntry(eventToRemove);
            assertFalse(entryBookUnderTest.getAllEntries().contains(eventToRemove));
            entryBookUnderTest.removeEntry(deadlineToRemove);
            assertFalse(entryBookUnderTest.getAllEntries().contains(deadlineToRemove));
            entryBookUnderTest.removeEntry(floatingTaskToRemove);
            assertFalse(entryBookUnderTest.getAllEntries().contains(floatingTaskToRemove));

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

    // @@author A0126623L
    @Test
    public void setAllEntriesList_sampleEntryBook_success() {
        try {
            EntryBook entryBookToSet = new EntryBook();
            EntryBook expectedEntryBook = EntryBookTest.getSampleEntryBook();
            assertFalse(entryBookToSet.equals(expectedEntryBook));

            entryBookToSet.resetData(expectedEntryBook);
            assertTrue(entryBookToSet.equals(expectedEntryBook));
        } catch (DuplicateEntryException e) {
            e.printStackTrace();
        }
    }
    // @@author

    // @@author A0126623L
    @Test(expected = NullPointerException.class)
    public void setAllEntries_null_nullPointerExceptionThrown() {
        EntryBook entryBookToSet = new EntryBook();
        EntryBook entryBookUsedForReset = null;
        assertFalse(entryBookToSet.equals(entryBookUsedForReset));

        entryBookToSet.resetData(entryBookUsedForReset);
        fail("Should not reach here. Reset data should fail.");
    }
    // @@author

    // @@author A0126623L
    @Test
    public void updateEntryTest_validEditedFloatingTask_success() {
        try {
            EntryBook entryBookUnderTest = EntryBookTest.getSampleEntryBook();
            ReadOnlyEntry targetEntryToEdit = entryBookUnderTest.getFloatingTaskList().get(0);

            // Create edited Entry
            Entry editedEntry = EntryBuilder.build(targetEntryToEdit);
            editedEntry.setName(new Name("modifiedName"));

            entryBookUnderTest.updateEntry(targetEntryToEdit, editedEntry);
            assertTrue(editedEntry.equals(targetEntryToEdit));

        } catch (DuplicateEntryException e) {
            e.printStackTrace();
        } catch (IllegalValueException e) {
            e.printStackTrace();
        } catch (EntryNotFoundException e) {
            e.printStackTrace();
        } catch (OverlappingEventException e) {
            e.printStackTrace();
        } catch (OverlappingAndOverdueEventException e) {
            e.printStackTrace();
        } catch (EntryOverdueException e) {
            e.printStackTrace();
        }
    }

}
