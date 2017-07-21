package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_CLEAN;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_MEETING;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_DATE_11_JULY_17;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_DATE_12_JULY_17;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_DATE_20_DEC_17;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_NAME_CLEAN;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_NAME_MEETING;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_TAG_FRIEND;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_TAG_URGENT;
import static seedu.multitasky.testutil.SampleEntries.INDEX_FIRST_ENTRY;
import static seedu.multitasky.testutil.SampleEntries.INDEX_SECOND_ENTRY;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.commons.util.match.PowerMatch;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.ParserUtil;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.EntryOverdueException;
import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.testutil.EditEntryDescriptorBuilder;
import seedu.multitasky.testutil.SampleEntries;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditByIndexCommand.
 */
public class EditByFindCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(), new UserPrefs());

    // @@author A0140633R
    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        ReadOnlyEntry targetEntry = model.getFilteredEventList().get(0);
        String searchString = targetEntry.getName().fullName;
        HashSet<String> keywords = new HashSet<>(Arrays.asList(searchString.split("\\s+")));
        Entry editedEntry = EntryBuilder.build(VALID_NAME_CLEAN, ParserUtil.parseDate(VALID_DATE_12_JULY_17),
                ParserUtil.parseDate(VALID_DATE_20_DEC_17), VALID_TAG_URGENT, VALID_TAG_FRIEND);
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(editedEntry)
                .withName(VALID_NAME_CLEAN).withStartDate(VALID_DATE_12_JULY_17).withEndDate(VALID_DATE_20_DEC_17)
                .withTags(VALID_TAG_URGENT, VALID_TAG_FRIEND).build();
        EditCommand editCommand = prepareCommand(model, keywords, descriptor);
        String expectedMessage = String.format(EditByFindCommand.MESSAGE_SUCCESS_WITH_OVERDUE_ALERT,
                                               targetEntry, editedEntry);
        Model expectedModel = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(), new UserPrefs());
        try {
            expectedModel.updateEntry(expectedModel.getFilteredEventList().get(INDEX_FIRST_ENTRY.getZeroBased()),
                                      editedEntry);
        } catch (EntryOverdueException eoe) {
            // Do nothing. Accept overdue entries in test.
        }

        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }
    // @@author A0140633R

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        ReadOnlyEntry targetEntry = model.getFilteredDeadlineList().get(0);
        String searchString = targetEntry.getName().fullName;
        HashSet<String> keywords = new HashSet<>(Arrays.asList(searchString.split("\\s+")));
        Entry editedEntry = EntryBuilder.build(VALID_NAME_CLEAN, ParserUtil.parseDate(VALID_DATE_11_JULY_17),
                                               VALID_TAG_URGENT, VALID_TAG_FRIEND);
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(editedEntry)
                .withName(VALID_NAME_CLEAN).withEndDate(VALID_DATE_11_JULY_17)
                .withTags(VALID_TAG_URGENT, VALID_TAG_FRIEND).build();
        EditCommand editCommand = prepareCommand(model, keywords, descriptor);
        String expectedMessage = String.format(EditByFindCommand.MESSAGE_SUCCESS_WITH_OVERDUE_ALERT,
                                               targetEntry, editedEntry);
        Model expectedModel = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(), new UserPrefs());
        try {
            expectedModel.updateEntry(expectedModel.getFilteredDeadlineList().get(INDEX_FIRST_ENTRY.getZeroBased()),
                                      editedEntry);
        } catch (EntryOverdueException eoe) {
            // Do nothing. Accept overdue entries in test.
        }

        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showFirstEntryOnly();
        ReadOnlyEntry entryInFilteredList = model.getFilteredFloatingTaskList()
                                                 .get(INDEX_FIRST_ENTRY.getZeroBased());
        String searchString = entryInFilteredList.getName().fullName;
        HashSet<String> keywords = new HashSet<>(Arrays.asList(searchString.split("\\s+")));
        Entry editedEntry = EntryBuilder.build();
        editedEntry.setName(new Name(VALID_NAME_MEETING));
        editedEntry.setTags(entryInFilteredList.getTags());
        EditCommand editCommand = prepareCommand(model, keywords,
                                                 new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING)
                                                                                 .build());
        String expectedMessage = String.format(EditByFindCommand.MESSAGE_SUCCESS, entryInFilteredList, editedEntry);
        Model expectedModel = new ModelManager(new EntryBook(model.getEntryBook()), new UserPrefs());

        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    // @@author A0140633R
    @Test
    public void execute_multipleEntriesFound_returnsMultipleEntriesMessage() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(EditByFindCommand.MESSAGE_MULTIPLE_ENTRIES);

        String searchString = "try to find";
        HashSet<String> keywords = new HashSet<>(Arrays.asList(searchString.split("\\s+")));
        model.addEntry(EntryBuilder.build(searchString + " 1", "first_tag"));
        model.addEntry(EntryBuilder.build(searchString + " 2", "second_tag"));
        model.addEntry(EntryBuilder.build(searchString + " 3", "third_tag"));
        EditEntryDescriptor editEntryDescriptor = new EditEntryDescriptor();
        EditCommand editCommand = prepareCommand(model, keywords, editEntryDescriptor);

        editCommand.execute();
    }

    @Test
    public void execute_noEntriesFound_returnsNoEntriesMessage() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(EditByFindCommand.MESSAGE_NO_ENTRIES);

        String searchString = "a random string";
        HashSet<String> keywords = new HashSet<>(Arrays.asList(searchString.split("\\s+")));
        EditEntryDescriptor editEntryDescriptor = new EditEntryDescriptor();
        EditCommand editCommand = prepareCommand(model, keywords, editEntryDescriptor);

        editCommand.execute();
    }
    // @@author

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
        assertFalse(standardCommand == null);

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
    private EditCommand prepareCommand(Model model, Set<String> keywords,
                                       EditEntryDescriptor editEntryDescriptor) {
        EditCommand editCommand = new EditByFindCommand(keywords, editEntryDescriptor);
        editCommand.setData(model, new CommandHistory());
        return editCommand;
    }

    /**
     * Updates the filtered list to show only the first entry in the {@code model}'s entry book.
     */
    private void showFirstEntryOnly() {
        ReadOnlyEntry entry = model.getEntryBook().getFloatingTaskList().get(0);
        final String[] splitName = entry.getName().fullName.split("\\s+");
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)),
                                             null, null, Entry.State.ACTIVE, Model.Search.AND,
                                             PowerMatch.Level.LEVEL_0);

        assertTrue(model.getFilteredFloatingTaskList().size() == 1);
    }

}
