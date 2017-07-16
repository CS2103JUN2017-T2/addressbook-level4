package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_EVENT;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.logic.parser.Prefix;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.testutil.EditEntryDescriptorBuilder;
import seedu.multitasky.testutil.SampleEntries;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditByIndexCommand.
 */
public class EditByIndexCommandTest {

    private Model model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());

    // @@author A0140633R
    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Entry editedEntry = EntryBuilder.build(VALID_NAME_CLEAN, parseDate(VALID_DATE_12_JULY_17),
                parseDate(VALID_DATE_20_DEC_17), VALID_TAG_URGENT, VALID_TAG_FRIEND);
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(editedEntry)
                .withName(VALID_NAME_CLEAN).withStartDate(VALID_DATE_12_JULY_17).withEndDate(VALID_DATE_20_DEC_17)
                .withTags(VALID_TAG_URGENT, VALID_TAG_FRIEND).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_EVENT, descriptor);
        Model expectedModel = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());
        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS,
                                               model.getFilteredEventList().get(0), editedEntry);
        CommandResult result = editCommand.execute();
        expectedModel.updateEntry(expectedModel.getFilteredEventList().get(INDEX_FIRST_ENTRY.getZeroBased()),
                                  editedEntry);

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }
    // @@author

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        ReadOnlyEntry targetEntry = model.getFilteredDeadlineList().get(INDEX_FIRST_ENTRY.getZeroBased());
        Entry editedEntry = EntryBuilder.build(VALID_NAME_CLEAN, parseDate(VALID_DATE_11_JULY_17),
                                               VALID_TAG_URGENT, VALID_TAG_FRIEND);
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder(editedEntry)
                .withName(VALID_NAME_CLEAN).withEndDate(VALID_DATE_11_JULY_17)
                .withTags(VALID_TAG_URGENT, VALID_TAG_FRIEND).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_DEADLINE, descriptor);
        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, targetEntry, editedEntry);
        Model expectedModel = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());
        expectedModel.updateEntry(expectedModel.getFilteredDeadlineList().get(INDEX_FIRST_ENTRY.getZeroBased()),
                                  editedEntry);

        CommandTestUtil.assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showFirstEntryOnly();
        ReadOnlyEntry entryInFilteredList = model.getFilteredFloatingTaskList()
                                                 .get(INDEX_FIRST_ENTRY.getZeroBased());
        Entry editedEntry = EntryBuilder.build();
        editedEntry.setName(new Name(VALID_NAME_MEETING));
        editedEntry.setTags(entryInFilteredList.getTags());
        EditCommand editCommand = prepareCommand(INDEX_FIRST_ENTRY, PREFIX_FLOATINGTASK,
                new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING).build());
        String expectedMessage = String.format(EditByIndexCommand.MESSAGE_SUCCESS, entryInFilteredList, editedEntry);
        Model expectedModel = new ModelManager(new EntryBook(model.getEntryBook()), new UserPrefs());
        CommandResult result = editCommand.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_invalidEntryIndexUnfilteredList_failure() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFloatingTaskList().size() + 1);
        EditEntryDescriptor descriptor =
                                       new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING).build();
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
    private EditCommand prepareCommand(Index index, Prefix prefix , EditEntryDescriptor descriptor) {
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
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)),
                                             null, null, Entry.State.ACTIVE, Model.Search.AND);

        assertTrue(model.getFilteredFloatingTaskList().size() == 1);
    }

    /**
     * Converts date to a calendar
     */
    private Calendar parseDate(String args) throws Exception {
        PrettyTimeParser ptp = new PrettyTimeParser();
        Calendar calendar = new GregorianCalendar();
        try {
            List<Date> dates = ptp.parse(args);
            Date date = dates.get(0);
            calendar.setTime(date);
            return calendar;

        } catch (Exception e) {
            // double exception catching as a fail-safe
            fail("should not give invalid dates to parse");
            return null;
        }
    }

}
