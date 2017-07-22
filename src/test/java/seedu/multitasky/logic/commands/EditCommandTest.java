package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_FULL;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_MEETING;
import static seedu.multitasky.testutil.EditCommandTestUtil.INVALID_CALENDAR_END;
import static seedu.multitasky.testutil.EditCommandTestUtil.INVALID_CALENDAR_START;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_NAME_MEETING;
import static seedu.multitasky.testutil.SampleEntries.INDEX_FIRST_ENTRY;
import static seedu.multitasky.testutil.SampleEntries.INDEX_SECOND_ENTRY;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.util.PowerMatch;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.Prefix;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.testutil.EditCommandTestUtil;
import seedu.multitasky.testutil.EditEntryDescriptorBuilder;
import seedu.multitasky.testutil.SampleEntries;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(), new UserPrefs());

    // @@author A0140633R
    @Test
    public void execute_editFloatingToEvent_success() throws Exception {
        ReadOnlyEntry targetEntry = model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Entry editedEntry = EditCommandTestUtil.EVENT_BOWLING;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(editedEntry).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK, descriptor);
        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, targetEntry, editedEntry);
        Model expectedModel = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(),
                                               new UserPrefs());
        expectedModel.updateEntry(targetEntry, editedEntry);
        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_editEventToDeadline_success() throws Exception {
        ReadOnlyEntry targetEntry = model.getFilteredEventList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Entry editedEntry = EditCommandTestUtil.DEADLINE_EXAM;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(editedEntry).withResetStartDate().build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_EVENT, descriptor);
        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, targetEntry, editedEntry);
        Model expectedModel = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(),
                                               new UserPrefs());
        expectedModel.updateEntry(targetEntry, editedEntry);
        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_editEventToFloating_success() throws Exception {
        ReadOnlyEntry targetEntry = model.getFilteredEventList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Entry editedEntry = EditCommandTestUtil.FLOATINGTASK_DOG;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(editedEntry)
                .withResetStartDate().withResetEndDate().build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_EVENT, descriptor);
        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, targetEntry, editedEntry);
        Model expectedModel = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(),
                                               new UserPrefs());
        expectedModel.updateEntry(targetEntry, editedEntry);
        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_editDeadlineToFullDayEvent_success() throws Exception {
        ReadOnlyEntry targetEntry = model.getFilteredDeadlineList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Entry editedEntry = EditCommandTestUtil.EVENT_FULLDAY;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(editedEntry).build();
        editedEntry = EditCommandTestUtil.EVENT_FULLDAY_CORRECTED;
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_DEADLINE, descriptor);
        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, targetEntry, editedEntry);
        Model expectedModel = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(),
                                               new UserPrefs());
        expectedModel.updateEntry(targetEntry, editedEntry);
        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }
    // @@author

    @Test
    public void execute_editFloatingNameAddTag_success() throws Exception {
        ReadOnlyEntry targetEntry = model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Set<Tag> newTagSet = new HashSet<Tag>();
        newTagSet.addAll(targetEntry.getTags());
        newTagSet.add(new Tag("extratag"));
        Entry editedEntry = EntryBuilder.build(new Name("changed name"), newTagSet);
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(editedEntry).withAddTags("extratag").build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK, descriptor);
        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, targetEntry, editedEntry);
        Model expectedModel = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(), new UserPrefs());
        expectedModel.updateEntry(targetEntry, editedEntry);
        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }
    // @@author

    @Test
    public void execute_filteredListFloatingToEvent_success() throws Exception {
        showFirstEntryOnly();
        ReadOnlyEntry targetEntry = model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Entry editedEntry = EditCommandTestUtil.EVENT_BOWLING;
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK,
                                                 new EditEntryDescriptorBuilder(editedEntry).build());
        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, targetEntry, editedEntry);
        Model expectedModel = new ModelManager(new EntryBook(model.getEntryBook()), new UserPrefs());
        expectedModel.updateEntry(targetEntry, editedEntry);

        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    // @@author A0140633R
    @Test
    public void execute_editToInvalidDate_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(EditCommand.MESSAGE_ENDDATE_BEFORE_STARTDATE);

        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder()
                .withStartDate(INVALID_CALENDAR_START).withEndDate(INVALID_CALENDAR_END).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK, descriptor);
        editCommand.execute();
    }
    // @@author

    @Test
    public void execute_invalidEntryIndexUnfilteredList_failure() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFloatingTaskList().size() + 1);
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, PREFIX_FLOATINGTASK, descriptor);

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

        EditCommand editCommand = prepareCommand(outOfBoundIndex, PREFIX_FLOATINGTASK,
                                                 new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING)
                                                                                 .build());

        CommandTestUtil.assertCommandFailure(editCommand, model,
                                             Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditByIndexCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK,
                                                                   DESC_FULL);

        // same values -> returns true
        EditEntryDescriptor copyDescriptor = new EditEntryDescriptor(DESC_FULL);
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
                                                                  DESC_FULL)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditByIndexCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK,
                                                                  DESC_MEETING)));
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditCommand prepareCommand(Index index, Prefix prefix, EditEntryDescriptor descriptor) {
        EditCommand editCommand = new EditByIndexCommand(index, prefix, descriptor);
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
                                             Model.Search.AND, PowerMatch.Level.LEVEL_0);

        assertTrue(model.getFilteredFloatingTaskList().size() == 1);
    }
}
