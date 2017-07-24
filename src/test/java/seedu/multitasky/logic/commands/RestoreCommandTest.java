package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertTrue;
import static seedu.multitasky.testutil.SampleEntries.INDEX_FIRST_ENTRY;
import static seedu.multitasky.testutil.SampleEntries.INDEX_SECOND_ENTRY;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.util.match.PowerMatch;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.parser.Prefix;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.testutil.SampleEntries;


//@@author A0126623L-reused
/**
* Contains integration tests (interaction with the Model) and unit tests for {@code RestoreCommand}.
*/
public class RestoreCommandTest {

    private Model model = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(),
                                           new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyEntry entryToRestore = model.getFilteredFloatingTaskList()
                                            .get(INDEX_FIRST_ENTRY.getZeroBased());
        RestoreCommand restoreCommand = prepareCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(RestoreCommand.MESSAGE_SUCCESS, entryToRestore);

        ModelManager expectedModel = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(),
                                                      new UserPrefs());
        model.changeEntryState(entryToRestore, Entry.State.DELETED);
        expectedModel.updateAllFilteredListToShowAllActiveEntries();

        CommandTestUtil.assertCommandSuccess(restoreCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFloatingTaskList().size() + 1);
        RestoreCommand restoreCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(restoreCommand, model,
                                             Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }


    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() throws Exception {
        showFirstEntryOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        // ensures that outOfBoundIndex is still in bounds of entry book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEntryBook().getFloatingTaskList().size());

        RestoreCommand restoreCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(restoreCommand, model,
                                             Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /**
     * Returns a {@code RestoreCommand} with the parameter {@code index}.
     */
    private RestoreCommand prepareCommand(Index index) {
        RestoreCommand restoreCommand = new RestoreByIndexCommand(index, new Prefix("float"));
        restoreCommand.setData(model, new CommandHistory());
        return restoreCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show only the first entry from the entry book.
     */
    private void showFirstEntryOnly(Model model) {
        ReadOnlyEntry entry = model.getEntryBook().getFloatingTaskList().get(0);
        final String[] splitName = entry.getName().fullName.split("\\s+");
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)), null, null,
                                             Entry.State.ACTIVE,
                                             Model.Search.AND, PowerMatch.UNUSED);

        assert model.getFilteredFloatingTaskList().size() == 1;
    }

}
