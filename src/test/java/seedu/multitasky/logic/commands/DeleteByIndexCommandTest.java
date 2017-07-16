package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertTrue;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.multitasky.testutil.SampleEntries.INDEX_FIRST_ENTRY;
import static seedu.multitasky.testutil.SampleEntries.INDEX_SECOND_ENTRY;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.parser.Prefix;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.testutil.SampleEntries;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteByIndexCommand}.
 */
public class DeleteByIndexCommandTest {

    private Model model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyEntry entryToDelete = model.getFilteredFloatingTaskList()
                                           .get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_SUCCESS, entryToDelete);

        ModelManager expectedModel = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());
        expectedModel.changeEntryState(entryToDelete, Entry.State.DELETED);
        expectedModel.updateAllFilteredListToShowAllActiveEntries();

        CommandTestUtil.assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFloatingTaskList().size() + 1);
        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex, PREFIX_FLOATINGTASK);

        CommandTestUtil.assertCommandFailure(deleteCommand, model,
                                             Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showFirstEntryOnly(model);
        ReadOnlyEntry entryToDelete = model.getFilteredFloatingTaskList()
                                           .get(INDEX_FIRST_ENTRY.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_SUCCESS, entryToDelete);
        Model expectedModel = new ModelManager(model.getEntryBook(), new UserPrefs());
        expectedModel.changeEntryState(entryToDelete, Entry.State.DELETED);
        expectedModel.updateAllFilteredListToShowAllActiveEntries();

        CommandTestUtil.assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() throws Exception {
        showFirstEntryOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        // ensures that outOfBoundIndex is still in bounds of entry book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEntryBook().getFloatingTaskList().size());

        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex, PREFIX_FLOATINGTASK);

        CommandTestUtil.assertCommandFailure(deleteCommand, model,
                                             Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareCommand(Index index, Prefix prefix) {
        DeleteCommand deleteCommand = new DeleteByIndexCommand(index, prefix);
        deleteCommand.setData(model, new CommandHistory());
        return deleteCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show only the first entry from the entry book.
     */
    private void showFirstEntryOnly(Model model) {
        ReadOnlyEntry entry = model.getEntryBook().getFloatingTaskList().get(0);
        final String[] splitName = entry.getName().fullName.split("\\s+");
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)), null, null, Entry.State.ACTIVE,
                                             Model.Search.AND);

        assert model.getFilteredFloatingTaskList().size() == 1;
    }

}
