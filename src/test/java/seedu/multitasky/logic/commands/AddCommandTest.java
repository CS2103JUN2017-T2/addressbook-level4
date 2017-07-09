package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.testutil.EntryBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullEntry_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_entryAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingEntryAdded modelStub = new ModelStubAcceptingEntryAdded();
        Entry validEntry = new EntryBuilder().build();

        CommandResult commandResult = getAddCommandForEntry(validEntry, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validEntry), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validEntry), modelStub.entrysAdded);
    }

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
        public void addEntry(ReadOnlyEntry entry) {
            fail("This method should not be called.");
        }

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
        public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry) {
            fail("This method should not be called.");
        }

        @Override
        public UnmodifiableObservableList<ReadOnlyEntry> getFilteredFloatingTaskList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredListToShowAll() {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredFloatingTaskList(Set<String> keywords) {
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
            entrysAdded.add(new Entry(entry));
        }
    }

}
