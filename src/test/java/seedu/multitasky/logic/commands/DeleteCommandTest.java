package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertTrue;
import static seedu.multitasky.testutil.SampleEntries.INDEX_FIRST_ENTRY;
import static seedu.multitasky.testutil.SampleEntries.INDEX_SECOND_ENTRY;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.parser.Prefix;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.testutil.SampleEntries;

//TODO implement delete by find tests and separate out delete by index / abstract class delete tests from here.
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyEntry entryToDelete = model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_SUCCESS, entryToDelete);

        ModelManager expectedModel = new ModelManager(model.getEntryBook(), new UserPrefs());
        expectedModel.deleteEntry(entryToDelete);

        CommandTestUtil.assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFloatingTaskList().size() + 1);
        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    //TODO fix bug from ModelManager updateFilteredList. look at NameQualifier.run(),
    // right now if emptySet is given as keywords, the for loop is not executed and returns true. (should be false)
    // reimplement test after fixing.
    /*@Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showFirstEntryOnly(model);

        ReadOnlyEntry entryToDelete = model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_SUCCESS, entryToDelete);

        Model expectedModel = new ModelManager(model.getEntryBook(), new UserPrefs());
        expectedModel.deleteEntry(entryToDelete);
        showNoEntry(expectedModel);

        CommandTestUtil.assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }
    */

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() throws Exception {
        showFirstEntryOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        // ensures that outOfBoundIndex is still in bounds of entry book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEntryBook().getFloatingTaskList().size());

        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareCommand(Index index) {
        DeleteCommand deleteCommand = new DeleteByIndexCommand(index, new Prefix("/float"));
        deleteCommand.setData(model, new CommandHistory());
        return deleteCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show only the first entry from the entry book.
     */
    private void showFirstEntryOnly(Model model) {
        ReadOnlyEntry entry = model.getEntryBook().getFloatingTaskList().get(0);
        final String[] splitName = entry.getName().fullName.split("\\s+");
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)));

        assert model.getFilteredFloatingTaskList().size() == 1;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoEntry(Model model) {
        model.updateFilteredFloatingTaskList(Collections.emptySet());
        assert model.getFilteredFloatingTaskList().isEmpty();
    }
}
