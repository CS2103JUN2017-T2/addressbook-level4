package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_AMY;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_BOB;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_NAME_BOB;
import static seedu.multitasky.testutil.SampleEntries.INDEX_FIRST_ENTRY;
import static seedu.multitasky.testutil.SampleEntries.INDEX_SECOND_ENTRY;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.testutil.EditEntryDescriptorBuilder;
import seedu.multitasky.testutil.SampleEntries;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());

    /*
    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Entry editedEntry = new EntryBuilder().build();
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(editedEntry).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, editedEntry);

        Model expectedModel = new ModelManager(new EntryBook(model.getEntryBook()), new UserPrefs());
        expectedModel.updateEntry(model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased()),
        editedEntry);

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastEntry = Index.fromOneBased(model.getFilteredFloatingTaskList().size());
        ReadOnlyEntry lastEntry = model.getFilteredFloatingTaskList().get(indexLastEntry.getZeroBased());

        EntryBuilder entryInList = new EntryBuilder(lastEntry);
        Entry editedEntry = entryInList.withName(VALID_NAME_BOB).withTags(VALID_TAG_HUSBAND).build();

        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = prepareCommand(indexLastEntry, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, editedEntry);

        Model expectedModel = new ModelManager(new EntryBook(model.getEntryBook()), new UserPrefs());
        expectedModel.updateEntry(lastEntry, editedEntry);

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() throws Exception {
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, new EditEntryDescriptor());
        ReadOnlyEntry editedEntry = model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, editedEntry);

        Model expectedModel = new ModelManager(new EntryBook(model.getEntryBook()), new UserPrefs());

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showFirstEntryOnly();

        ReadOnlyEntry entryInFilteredList = model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Entry editedEntry = new EntryBuilder(entryInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY,
                new EditEntryDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, editedEntry);

        Model expectedModel = new ModelManager(new EntryBook(model.getEntryBook()), new UserPrefs());
        expectedModel.updateEntry(model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased()),
        editedEntry);

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }
    */

    @Test
    public void execute_invalidEntryIndexUnfilteredList_failure() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFloatingTaskList().size() + 1);
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        CommandTestUtil.assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
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
                new EditEntryDescriptorBuilder().withName(VALID_NAME_BOB).build());

        CommandTestUtil.assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditByIndexCommand(INDEX_FIRST_ENTRY, DESC_AMY);

        // same values -> returns true
        EditEntryDescriptor copyDescriptor = new EditEntryDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditByIndexCommand(INDEX_FIRST_ENTRY, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditByIndexCommand(INDEX_SECOND_ENTRY, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditByIndexCommand(INDEX_FIRST_ENTRY, DESC_BOB)));
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditCommand prepareCommand(Index index, EditEntryDescriptor descriptor) {
        EditCommand editCommand = new EditByIndexCommand(index, descriptor);
        editCommand.setData(model, new CommandHistory());
        return editCommand;
    }

    /**
     * Updates the filtered list to show only the first entry in the {@code model}'s entry book.
     */
    private void showFirstEntryOnly() {
        ReadOnlyEntry entry = model.getEntryBook().getFloatingTaskList().get(0);
        final String[] splitName = entry.getName().fullName.split("\\s+");
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)));

        assertTrue(model.getFilteredFloatingTaskList().size() == 1);
    }
}
