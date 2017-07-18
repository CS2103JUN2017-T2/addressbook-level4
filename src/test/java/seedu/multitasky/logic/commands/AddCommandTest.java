package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Entry.State;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.storage.exception.NothingToRedoException;
import seedu.multitasky.testutil.SampleEntries;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullEntry_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    // @@author A0140633R
    @Test
    public void execute_entryAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingEntryAdded modelStub = new ModelStubAcceptingEntryAdded();
        ArrayList<Entry> expectedEntryList = new ArrayList<>();
        // Floating task
        Entry validEntry = SampleEntries.COOK;
        expectedEntryList.add(validEntry);
        CommandResult commandResult = getAddCommandForEntry(validEntry, modelStub).execute();
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validEntry), commandResult.feedbackToUser);
        assertEquals(expectedEntryList, modelStub.entrysAdded);

        // Deadline
        validEntry = SampleEntries.TAX;
        expectedEntryList.add(validEntry);
        commandResult = getAddCommandForEntry(validEntry, modelStub).execute();
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validEntry), commandResult.feedbackToUser);
        assertEquals(expectedEntryList, modelStub.entrysAdded);

        // Event
        validEntry = SampleEntries.TAX;
        expectedEntryList.add(validEntry);
        commandResult = getAddCommandForEntry(validEntry, modelStub).execute();
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validEntry), commandResult.feedbackToUser);
        assertEquals(expectedEntryList, modelStub.entrysAdded);
    }
    // @@author A0140633R

    /**
     * Generates a new AddCommand with the details of the given entry.
     */
    private AddCommand getAddCommandForEntry(Entry entry, Model model) throws IllegalValueException {
        AddCommand command = new AddCommand(entry);
        command.setData(model, new CommandHistory());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {

        @Override
        public void resetData(ReadOnlyEntryBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyEntryBook getEntryBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deleteEntry(ReadOnlyEntry target) throws EntryNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void addEntry(ReadOnlyEntry entry) {
            fail("This method should not be called.");
        }

        @Override
        public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry)
                throws EntryNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public UnmodifiableObservableList<ReadOnlyEntry> getFilteredEventList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public UnmodifiableObservableList<ReadOnlyEntry> getFilteredDeadlineList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public UnmodifiableObservableList<ReadOnlyEntry> getFilteredFloatingTaskList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public UnmodifiableObservableList<ReadOnlyEntry> getActiveList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredEventListToShowAll() {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredDeadlineListToShowAll() {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredFloatingTaskListToShowAll() {
            fail("This method should not be called.");
        }

        @Override
        public void updateAllFilteredListToShowAll() {
            fail("This method should not be called.");
        }

        @Override
        public void updateAllFilteredLists(Set<String> keywords, Calendar startDate, Calendar endDate,
                                           Entry.State state) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredEventList(Set<String> keywords, Calendar startDate, Calendar endDate,
                                            Entry.State state, Search search) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredDeadlineList(Set<String> keywords, Calendar startDate, Calendar endDate,
                                               Entry.State state, Search search) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredFloatingTaskList(Set<String> keywords, Calendar startDate, Calendar endDate,
                                                   Entry.State state, Search search) {
            fail("This method should not be called.");
        }

        @Override
        public void undoPreviousAction() {
            fail("This method should not be called.");
        }

        @Override
        public void redoPreviousAction() throws NothingToRedoException {
            fail("This method should not be called.");
        }

        @Override
        public void updateSortingComparators(Comparator<ReadOnlyEntry> eventComparator,
                                             Comparator<ReadOnlyEntry> deadlineComparator,
                                             Comparator<ReadOnlyEntry> floatingTaskComparator) {
            fail("This method should not be called.");
        }

        @Override
        public void updateAllFilteredListToShowAllActiveEntries() {
            fail("This method should not be called.");
        }

        @Override
        public void updateAllFilteredListToShowAllArchivedEntries() {
            fail("This method should not be called.");
        }

        @Override
        public void updateAllFilteredListToShowAllDeletedEntries() {
            fail("This method should not be called.");
        }

        @Override
        public void changeEntryState(ReadOnlyEntry entryToChange, State newState)
                throws DuplicateEntryException, EntryNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void changeFilePath(String newFilePath) {
            fail("This method should not be called.");
        }

        @Override
        public void openFilePath(String newFilePath) {
            fail("This method should not be called.");
        }

        @Override
        public void clearStateSpecificEntries(State state) {
            fail("This method should not be called.");
        }

    }

    /**
     * A Model stub that always accept the entry being added.
     */
    private class ModelStubAcceptingEntryAdded extends ModelStub {
        final ArrayList<Entry> entrysAdded = new ArrayList<>();

        @Override
        public void addEntry(ReadOnlyEntry entry) {
            if (entry instanceof FloatingTask) {
                entrysAdded.add(new FloatingTask(entry));
            } else if (entry instanceof Deadline) {
                entrysAdded.add(new Deadline(entry));
            } else if (entry instanceof Event) {
                entrysAdded.add(new Event(entry));
            } else {
                throw new AssertionError("can only add float,deadline or event");
            }
        }
    }

}
