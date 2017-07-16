package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.multitasky.testutil.SampleEntries.INDEX_FIRST_ENTRY;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.testutil.SampleEntries;

// @@author A0140633R
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteByFindCommand}.
 */
public class DeleteByFindCommandTest {

    @Test
    public void execute_validKeywordsUnfilteredList_success() throws Exception {
        Model model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());

        ReadOnlyEntry entryToDelete = model.getFilteredFloatingTaskList()
                                           .get(INDEX_FIRST_ENTRY.getZeroBased());

        String searchString = entryToDelete.getName().fullName.replaceAll("\\\\", "");
        HashSet<String> keywords = new HashSet<>(Arrays.asList(searchString.split("\\s+")));

        DeleteCommand deleteCommand = prepareCommand(model, keywords);
        String expectedMessage = String.format(DeleteByFindCommand.MESSAGE_SUCCESS, entryToDelete);
        ModelManager expectedModel = new ModelManager(model.getEntryBook(), new UserPrefs());

        CommandResult result = deleteCommand.execute();
        assertEquals(result.feedbackToUser, expectedMessage);

        expectedModel.changeEntryState(entryToDelete, Entry.State.DELETED);
        expectedModel.updateAllFilteredListToShowAllActiveEntries();
        assertEquals(model, expectedModel);
    }

    @Test
    public void execute_noEntryFoundUnfilteredList_returnsNoEntriesMessage() throws Exception {
        Model model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());
        String searchString = "randomstring";
        HashSet<String> keywords = new HashSet<>(Arrays.asList(searchString.split("\\s+")));
        DeleteCommand deleteCommand = prepareCommand(model, keywords);
        CommandResult result = deleteCommand.execute();

        assertEquals(result.feedbackToUser, DeleteByFindCommand.MESSAGE_NO_ENTRIES);
    }

    @Test
    public void execute_multipleEntriesFoundFilteredList_returnsMultipleEntriesMessage() throws Exception {
        Model model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());
        String searchString = "try to find";
        HashSet<String> keywords = new HashSet<>(Arrays.asList(searchString.split("\\s+")));
        model.addEntry(EntryBuilder.build(searchString + " 1", "first_tag"));
        model.addEntry(EntryBuilder.build(searchString + " 2", "second_tag"));
        model.addEntry(EntryBuilder.build(searchString + " 3", "third_tag"));
        DeleteCommand deleteCommand = prepareCommand(model, keywords);
        CommandResult result = deleteCommand.execute();

        assertEquals(result.feedbackToUser, DeleteByFindCommand.MESSAGE_MULTIPLE_ENTRIES);
    }

    @Test
    public void execute_validKeywordsFilteredList_success() throws Exception {
        Model model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());

        showFirstEntryOnly(model);
        ReadOnlyEntry entryToDelete = model.getFilteredFloatingTaskList()
                                           .get(INDEX_FIRST_ENTRY.getZeroBased());

        String searchString = entryToDelete.getName().fullName.replaceAll("\\\\", "");
        HashSet<String> keywords = new HashSet<>(Arrays.asList(searchString.split("\\s+")));

        DeleteCommand deleteCommand = prepareCommand(model, keywords);
        String expectedMessage = String.format(DeleteByFindCommand.MESSAGE_SUCCESS, entryToDelete);
        ModelManager expectedModel = new ModelManager(model.getEntryBook(), new UserPrefs());

        CommandResult result = deleteCommand.execute();
        assertEquals(result.feedbackToUser, expectedMessage);

        expectedModel.changeEntryState(entryToDelete, Entry.State.DELETED);
        expectedModel.updateAllFilteredListToShowAllActiveEntries();
        assertEquals(model, expectedModel);
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareCommand(Model model, Set<String> keywords) {
        DeleteCommand deleteCommand = new DeleteByFindCommand(keywords);
        deleteCommand.setData(model, new CommandHistory());
        return deleteCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show only the first entry from the entry book.
     */
    private void showFirstEntryOnly(Model model) {
        ReadOnlyEntry entry = model.getEntryBook().getFloatingTaskList().get(0);
        final String[] splitName = entry.getName().fullName.split("\\s+");
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)),
                                             null, null, Entry.State.ACTIVE, Model.Search.AND);

        assert model.getFilteredFloatingTaskList().size() == 1;
    }

}
