package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_CLEAN;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_MEETING;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_NAME_MEETING;
import static seedu.multitasky.testutil.SampleEntries.INDEX_FIRST_ENTRY;
import static seedu.multitasky.testutil.SampleEntries.INDEX_SECOND_ENTRY;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.util.match.PowerMatch;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.testutil.EditEntryDescriptorBuilder;
import seedu.multitasky.testutil.SampleEntries;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        ReadOnlyEntry targetEntry = model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Entry editedEntry = EntryBuilder.build();
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(editedEntry).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, descriptor);
        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, targetEntry, editedEntry);
        Model expectedModel = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(), new UserPrefs());
        expectedModel.updateEntry(targetEntry, editedEntry);
        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    /*
     * @Test
     * public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
     * Index indexLastEntry = Index.fromOneBased(model.getFilteredFloatingTaskList().size());
     * ReadOnlyEntry lastEntry = model.getFilteredFloatingTaskList().get(indexLastEntry.getZeroBased());
     * EntryBuilder entryInList = new EntryBuilder();
     * Entry editedEntry = entryInList.withName(VALID_NAME_BOB).withTags(VALID_TAG_HUSBAND).build();
     * EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_BOB)
     * .withTags(VALID_TAG_HUSBAND).build();
     * EditCommand editCommand = prepareCommand(indexLastEntry, descriptor);
     * String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, editedEntry);
     * Model expectedModel = new ModelManager(new EntryBook(model.getEntryBook()), new UserPrefs());
     * expectedModel.updateEntry(lastEntry, editedEntry);
     * CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
     * }
     */

    /*
     * @Test
     * public void execute_noFieldSpecifiedUnfilteredList_success() throws Exception {
     * EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, new EditEntryDescriptor());
     * ReadOnlyEntry editedEntry = model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased());
     * String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, editedEntry);
     * Model expectedModel = new ModelManager(new EntryBook(model.getEntryBook()), new UserPrefs());
     * CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
     * }
     */

    /*
     * @Test
     * public void execute_filteredList_success() throws Exception {
     * showFirstEntryOnly();
     * ReadOnlyEntry entryInFilteredList =
     * model.getFilteredFloatingTaskList()
     * .get(INDEX_FIRST_ENTRY.getZeroBased());
     * Entry editedEntry = new EntryBuilder(entryInFilteredList).withName(VALID_NAME_BOB).build();
     * EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY,
     * new EditEntryDescriptorBuilder().withName(VALID_NAME_BOB)
     * .build());
     * String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, editedEntry);
     * Model expectedModel = new ModelManager(new EntryBook(model.getEntryBook()), new UserPrefs());
     * expectedModel.updateEntry(model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased()),
     * editedEntry);
     * CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
     * }
     */

    @Test
    public void execute_invalidEntryIndexUnfilteredList_failure() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFloatingTaskList().size() + 1);
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        CommandTestUtil.assertCommandFailure(editCommand, model,
                                             Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of entry book
     */
    @Test
    public void execute_invalidEntryIndexFilteredList_failure() throws Exception {
        showFirstEntryOnly();
        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        // ensures that outOfBoundIndex is still in bounds of entry book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEntryBook().getFloatingTaskList().size());

        EditCommand editCommand = prepareCommand(outOfBoundIndex,
                                                 new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING)
                                                                                 .build());

        CommandTestUtil.assertCommandFailure(editCommand, model,
                                             Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditByIndexCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK,
                                                                   DESC_CLEAN);

        // same values -> returns true
        EditEntryDescriptor copyDescriptor = new EditEntryDescriptor(DESC_CLEAN);
        EditCommand commandWithSameValues = new EditByIndexCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK,
                                                                   copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditByIndexCommand(INDEX_SECOND_ENTRY, PREFIX_FLOATINGTASK,
                                                                  DESC_CLEAN)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditByIndexCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK,
                                                                  DESC_MEETING)));
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditCommand prepareCommand(Index index, EditEntryDescriptor descriptor) {
        EditCommand editCommand = new EditByIndexCommand(index, PREFIX_FLOATINGTASK, descriptor);
        editCommand.setData(model, new CommandHistory());
        return editCommand;
    }

    /**
     * Updates the filtered list to show only the first entry in the {@code model}'s entry book.
     */
    private void showFirstEntryOnly() {
        ReadOnlyEntry entry = model.getEntryBook().getFloatingTaskList().get(0);
        final String[] splitName = entry.getName().fullName.split("\\s+");
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)), null, null, Entry.State.ACTIVE,
                                             Model.Search.AND, PowerMatch.UNUSED);

        assertTrue(model.getFilteredFloatingTaskList().size() == 1);
    }
}
