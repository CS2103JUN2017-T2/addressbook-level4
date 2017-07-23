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
import seedu.multitasky.testutil.SampleEntries;

// @@author A0126623L
public class EntryBookTest {

    private EventList[] listOfEventLists = EventListTest.getListOfSampleEventLists();
    private DeadlineList[] listOfDeadlineLists = DeadlineListTest.getListOfSampleDeadlineLists();
    private FloatingTaskList[] listOfFloatingTaskLists = FloatingTaskListTest.getListOfSampleFloatingTaskLists();

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

    @Test
    public void add_sampleEntryBook_success() {
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

    @Test
    public void remove_entriesInSampleEntryBook_success() throws EntryNotFoundException {
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

    @Test
    public void setAllEntriesList_validSampleEntryBook_success() {
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

    @Test(expected = NullPointerException.class)
    public void setAllEntries_null_nullPointerExceptionThrown() {
        EntryBook entryBookToSet = new EntryBook();
        EntryBook entryBookUsedForReset = null;
        assertFalse(entryBookToSet.equals(entryBookUsedForReset));

        entryBookToSet.resetData(entryBookUsedForReset);
        fail("Should not reach here. Reset data should fail.");
    }

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

        } catch (Exception e) {
            fail("Should not result in any exceptions here.");
        }
    }

    @Test(expected = OverlappingAndOverdueEventException.class)
    public void updateEntryTest_validEditedOverlappingAndOverdueEvent_success()
            throws OverlappingAndOverdueEventException {
        try {
            EntryBook entryBookUnderTest;
            entryBookUnderTest = EntryBookTest.getSampleEntryBook();

            ReadOnlyEntry targetEntryToEdit = entryBookUnderTest.getEventList().get(0);

            // Create edited Entry
            Entry editedEntry = EntryBuilder.build(targetEntryToEdit);
            editedEntry.setName(new Name("modifiedName"));
            try {
                entryBookUnderTest.updateEntry(targetEntryToEdit, editedEntry);
            } finally {
                assertTrue(editedEntry.equals(targetEntryToEdit));
            }
        } catch (EntryNotFoundException | EntryOverdueException
                 | IllegalValueException | OverlappingEventException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = EntryOverdueException.class)
    public void updateEntryTest_validEditedOverdueDeadline_success()
            throws EntryOverdueException {
        try {
            EntryBook entryBookUnderTest;
            entryBookUnderTest = EntryBookTest.getSampleEntryBook();

            ReadOnlyEntry targetEntryToEdit = entryBookUnderTest.getDeadlineList().get(0);

            // Create edited Entry
            Entry editedEntry = EntryBuilder.build(targetEntryToEdit);
            editedEntry.setName(new Name("modifiedName"));
            try {
                entryBookUnderTest.updateEntry(targetEntryToEdit, editedEntry);
            } finally {
                assertTrue(editedEntry.equals(targetEntryToEdit));
            }
        } catch (EntryNotFoundException | OverlappingAndOverdueEventException
                 | IllegalValueException | OverlappingEventException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void clearStateSpecificEntriesTest_clearActiveEntries_success() {
        EntryBook entryBookUnderTest = SampleEntries.getSampleEntryBook();
        entryBookUnderTest.clearStateSpecificEntries(Entry.State.ACTIVE);

        boolean activeEntriesPresent = entriesOfSpecificStatePresent(entryBookUnderTest,
                                                                     Entry.State.ACTIVE);
        assertFalse(activeEntriesPresent);
    }

    @Test
    public void clearStateSpecificEntriesTest_clearArchivedEntries_success() {
        EntryBook entryBookUnderTest = SampleEntries.getSampleEntryBook();
        boolean archivedEntriesPresent = entriesOfSpecificStatePresent(entryBookUnderTest,
                                                                       Entry.State.ARCHIVED);
        assertTrue(archivedEntriesPresent);

        entryBookUnderTest.clearStateSpecificEntries(Entry.State.ARCHIVED);

        archivedEntriesPresent = entriesOfSpecificStatePresent(entryBookUnderTest,
                                                               Entry.State.ARCHIVED);
        assertFalse(archivedEntriesPresent);
    }

    @Test
    public void clearStateSpecificEntriesTest_clearDeletedEntries_success() {
        EntryBook entryBookUnderTest = SampleEntries.getSampleEntryBook();
        boolean deletedEntriesPresent = entriesOfSpecificStatePresent(entryBookUnderTest,
                                                                      Entry.State.DELETED);
        assertTrue(deletedEntriesPresent);

        entryBookUnderTest.clearStateSpecificEntries(Entry.State.DELETED);

        deletedEntriesPresent = entriesOfSpecificStatePresent(entryBookUnderTest,
                                                              Entry.State.DELETED);
        assertFalse(deletedEntriesPresent);
    }

    private boolean entriesOfSpecificStatePresent(ReadOnlyEntryBook entryBook, Entry.State targetState) {
        List<ReadOnlyEntry> allEntries = entryBook.getAllEntries();

        switch (targetState) {
        case ACTIVE:
            for (ReadOnlyEntry e : allEntries) {
                if (e.isActive()) {
                    return true;
                }
            }
            break;
        case ARCHIVED:
            for (ReadOnlyEntry e : allEntries) {
                if (e.isArchived()) {
                    return true;
                }
            }
            break;
        case DELETED:
            for (ReadOnlyEntry e : allEntries) {
                if (e.isDeleted()) {
                    return true;
                }
            }
            break;
        default:
            break;
        }
        return false;
    }

    @Test
    public void changeEntryStateTest_changeActiveToDeleted_success() {
        EntryBook entryBookUnderTest = SampleEntries.getSampleEntryBookWithActiveEntries();
        assertFalse(this.entriesOfSpecificStatePresent(entryBookUnderTest, Entry.State.DELETED));

        Entry eventToChange = (Entry) entryBookUnderTest.getEventList().get(0);
        Entry deadlineToChange = (Entry) entryBookUnderTest.getDeadlineList().get(0);

        try {
            entryBookUnderTest.changeEntryState(eventToChange, Entry.State.ARCHIVED);
            assert (this.entriesOfSpecificStatePresent(entryBookUnderTest, Entry.State.ARCHIVED));
            entryBookUnderTest.changeEntryState(deadlineToChange, Entry.State.DELETED);
            assert (this.entriesOfSpecificStatePresent(entryBookUnderTest, Entry.State.DELETED));
        } catch (DuplicateEntryException | EntryNotFoundException | OverlappingEventException
                 | OverlappingAndOverdueEventException | EntryOverdueException e) {
            // These specific set of exceptions an be ignored in this test.
        }
    }

}
