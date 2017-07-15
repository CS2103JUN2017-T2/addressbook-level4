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
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.testutil.SampleEntries;

//@@author A0132788U-reused
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code CompleteCommand}.
 */
public class CompleteCommandTest {

    private Model model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyEntry entryToComplete = model.getFilteredFloatingTaskList()
                .get(INDEX_FIRST_ENTRY.getZeroBased());

        CompleteCommand completeCommand = prepareCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(CompleteCommand.MESSAGE_SUCCESS, entryToComplete);

        ModelManager expectedModel = new ModelManager(model.getEntryBook(), new UserPrefs());
        model.changeEntryState(entryToComplete, Entry.State.ARCHIVED);
        expectedModel.updateAllFilteredListToShowAllActiveEntries();
        CommandTestUtil.assertCommandSuccess(completeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() throws Exception {
        showFirstEntryOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        // ensures that outOfBoundIndex is still in bounds of entry book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEntryBook().getFloatingTaskList().size());

        CompleteCommand completeCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(completeCommand, model,
                Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /**
     * Returns a {@code CompleteCommand} with the parameter {@code index}.
     */
    private CompleteCommand prepareCommand(Index index) {
        CompleteCommand completeCommand = new CompleteByIndexCommand(index, new Prefix("float"));
        completeCommand.setData(model, new CommandHistory());
        return completeCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show only the first entry from the entry book.
     */
    private void showFirstEntryOnly(Model model) {
        ReadOnlyEntry entry = model.getEntryBook().getFloatingTaskList().get(0);
        final String[] splitName = entry.getName().fullName.split("\\s+");
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)), Entry.State.ACTIVE);

        assert model.getFilteredFloatingTaskList().size() == 1;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoEntry(Model model) {
        model.updateFilteredFloatingTaskList(Collections.emptySet(), Entry.State.ACTIVE);
        assert model.getFilteredFloatingTaskList().isEmpty();
    }

}
