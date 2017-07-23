package guitests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.commands.RestoreCommand;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.testutil.CommandUtil;
import seedu.multitasky.testutil.SampleEntries;
import seedu.multitasky.testutil.TestUtil;

// @@author A0126623L
public class RestoreCommandBinTest extends EntryBookGuiTest {

    @Before
    public void setUp() {
        commandBox.runCommand(CommandUtil.getListBinCommand());
    }

    /**********************
     * Restoring by Index *
     *********************/
    @Test
    public void restore_firstEventByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleDeletedEvents();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertRestoreEventByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_lastEventByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleDeletedEvents();
        Index targetIndex = Index.fromOneBased(currentList.length);
        assertRestoreEventByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_invalidEventIndex_errorMessage() {
        Entry[] currentList = SampleEntries.getSampleDeletedEvents();
        Index targetIndex = Index.fromOneBased(currentList.length + 1);
        commandBox.runCommand(CommandUtil.getRestoreEventByIndexCommand(targetIndex));
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void restore_firstDeadlineByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleDeletedDeadlines();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertRestoreDeadlineByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_lastDeadlineByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleDeletedDeadlines();
        Index targetIndex = Index.fromOneBased(currentList.length);
        assertRestoreDeadlineByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_invalidDeadlineIndex_errorMessage() {
        Entry[] currentList = SampleEntries.getSampleDeletedDeadlines();
        Index targetIndex = Index.fromOneBased(currentList.length + 1);
        commandBox.runCommand(CommandUtil.getRestoreDeadlineByIndexCommand(targetIndex));
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void restore_firstFloatingTaskByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleDeletedFloatingTasks();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertRestoreFloatingTaskByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_lastFloatingTaskByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleDeletedFloatingTasks();
        Index targetIndex = Index.fromOneBased(currentList.length);
        assertRestoreFloatingTaskByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_invalidFloatingTaskIndex_errorMessage() {
        Entry[] currentList = SampleEntries.getSampleDeletedFloatingTasks();
        Index targetIndex = Index.fromOneBased(currentList.length + 1);
        commandBox.runCommand(CommandUtil.getRestoreFloatingTaskByIndexCommand(targetIndex));
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /************************
     * Restoring by Keyword *
     ************************/
    @Test
    public void restore_eventKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleDeletedEvents();
        Entry entryToRestore = SampleEntries.GYM;
        commandBox.runCommand(RestoreCommand.COMMAND_WORD + " gym");
        assertEventRestoredNormally(entryToRestore, currentList);
    }

    @Test
    public void restore_deadlineKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleDeletedDeadlines();
        Entry entryToRestore = SampleEntries.APPOINTMENT;
        commandBox.runCommand(RestoreCommand.COMMAND_WORD + " appointment");
        assertDeadlineRestored(entryToRestore, currentList);
    }

    @Test
    public void restore_floatingTaskKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleDeletedFloatingTasks();
        Entry entryToRestore = SampleEntries.BAKE;
        commandBox.runCommand(RestoreCommand.COMMAND_WORD + " baking");
        assertFloatingTaskRestoredNormally(entryToRestore, currentList);
    }

    // @@author A0126623L
    /********************************
     * Restore Command's Exceptions *
     ********************************/

    @Test
    public void restoreByKeyword_duplicateFloatingTask_throwDuplicateEntryException() {
        Entry deletedEntryToRestore = SampleEntries.BAKE;
        Entry duplicatedActiveEntry = EntryBuilder.build(deletedEntryToRestore);
        commandBox.runCommand(CommandUtil.getAddFloatingTaskCommand(duplicatedActiveEntry));
        commandBox.runCommand(RestoreCommand.COMMAND_WORD + " baking");
        assertResultMessage(DuplicateEntryException.MESSAGE);
    }

    @Test
    public void restoreByKeyword_overdueDeadline_overdueEntryAlertDisplayed() {
        try {
            Entry overdueDeadline = SampleEntries.createOverdueDeadline();

            commandBox.runCommand(CommandUtil.getAddDeadlineCommand(overdueDeadline));
            commandBox.runCommand(CommandUtil.getDeleteByFullNameCommand(overdueDeadline));
            assertResultMessage(String.format(DeleteCommand.MESSAGE_SUCCESS, overdueDeadline));
            commandBox.runCommand(CommandUtil.getRestoreByFullNameCommand(overdueDeadline));
            assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS_WITH_OVERDUE_ALERT,
                                              overdueDeadline.getName()));
        } catch (IllegalValueException e) {
            fail("IllegalValueException should not be thrown here, or something is wrong.");
        }
    }

    @Test
    public void restoreByIndex_duplicateFloatingTask_throwDuplicateEntryException() {
        Entry deletedEntryToRestore = SampleEntries.BAKE;
        Entry duplicatedActiveEntry = EntryBuilder.build(deletedEntryToRestore);
        commandBox.runCommand(CommandUtil.getClearBinCommand());
        commandBox.runCommand(CommandUtil.getAddFloatingTaskCommand(duplicatedActiveEntry));
        commandBox.runCommand(CommandUtil.getDeleteByFullNameCommand(duplicatedActiveEntry));
        commandBox.runCommand(CommandUtil.getAddFloatingTaskCommand(duplicatedActiveEntry));
        commandBox.runCommand(RestoreCommand.COMMAND_WORD + " baking");
        assertResultMessage(DuplicateEntryException.MESSAGE);
    }

    @Test
    public void restoreByIndex_overdueDeadline_overdueEntryAlertDisplayed() {
        try {
            Entry overdueDeadline = SampleEntries.createOverdueDeadline();

            commandBox.runCommand(CommandUtil.getClearBinCommand());
            commandBox.runCommand(CommandUtil.getAddDeadlineCommand(overdueDeadline));
            commandBox.runCommand(CommandUtil.getDeleteByFullNameCommand(overdueDeadline));
            commandBox.runCommand(CommandUtil.getRestoreDeadlineByIndexCommand(Index.fromOneBased(1)));
            assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS_WITH_OVERDUE_ALERT,
                                              overdueDeadline.getName()));
        } catch (IllegalValueException e) {
            fail("IllegalValueException should not be thrown here, or something is wrong.");
        }
    }

    @Test
    public void restoreByKeyword_overlappingEvent_successWithAppropriateAlert() {
        Entry overlappingEntryToRestore = SampleEntries.GYM;
        Entry overlappingEventInActiveList = SampleEntries.createOverlappingEvent(overlappingEntryToRestore);
        commandBox.runCommand(CommandUtil.getAddEventCommand(overlappingEventInActiveList));
        commandBox.runCommand(CommandUtil.getRestoreByFullNameCommand(overlappingEntryToRestore));
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS_WITH_OVERLAP_ALERT,
                                          overlappingEntryToRestore.getName().toString()));
    }

    @Test
    public void restoreByIndex_overlappingEvent_successWithAppropriateAlert() {
        // Set up deleted entry that will result in overlap alert when restored
        commandBox.runCommand(CommandUtil.getClearBinCommand());
        Entry overlappingEntryToRestore = SampleEntries.GYM;
        commandBox.runCommand(CommandUtil.getAddEventCommand(overlappingEntryToRestore));
        commandBox.runCommand(CommandUtil.getDeleteByFullNameCommand(overlappingEntryToRestore));

        // Create entry in active list that will overlap with overlappingEntryToRestore
        Entry overlappingEventInActiveList = SampleEntries.createOverlappingEvent(overlappingEntryToRestore);
        commandBox.runCommand(CommandUtil.getAddEventCommand(overlappingEventInActiveList));

        commandBox.runCommand(CommandUtil.getRestoreEventByIndexCommand(Index.fromOneBased(1)));
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS_WITH_OVERLAP_ALERT,
                                          overlappingEntryToRestore.getName().toString()));
    }

    @Test
    public void restoreByKeyword_overlappingAndOverdueEvent_successWithAppropriateAlert()
            throws IllegalValueException {
        Entry overdueEvent = SampleEntries.createOverdueEvent();
        Entry overlappingAndOverdueEvent = SampleEntries.createOverlappingEvent(overdueEvent);

        commandBox.runCommand(CommandUtil.getAddEventCommand(overdueEvent));
        commandBox.runCommand(CommandUtil.getAddEventCommand(overlappingAndOverdueEvent));
        commandBox.runCommand(CommandUtil.getDeleteByFullNameCommand(overlappingAndOverdueEvent));
        commandBox.runCommand(CommandUtil.getRestoreByFullNameCommand(overlappingAndOverdueEvent));
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS_WITH_OVERLAP_AND_OVERDUE_ALERT,
                                          overlappingAndOverdueEvent.getName().toString()));
    }

    @Test
    public void restoreByIndex_overlappingAndOverdueEvent_successWithAppropriateAlert()
            throws IllegalValueException {
        Entry overdueEvent = SampleEntries.createOverdueEvent();
        Entry overlappingAndOverdueEvent = SampleEntries.createOverlappingEvent(overdueEvent);

        commandBox.runCommand(CommandUtil.getClearBinCommand());
        commandBox.runCommand(CommandUtil.getAddEventCommand(overdueEvent));
        commandBox.runCommand(CommandUtil.getAddEventCommand(overlappingAndOverdueEvent));
        commandBox.runCommand(CommandUtil.getDeleteByFullNameCommand(overlappingAndOverdueEvent));
        commandBox.runCommand(CommandUtil.getRestoreEventByIndexCommand(Index.fromOneBased(1)));
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS_WITH_OVERLAP_AND_OVERDUE_ALERT,
                                          overlappingAndOverdueEvent.getName().toString()));
    }

    // @@author A0126623L-reused
    /**************************************
     * Different types of invalid wording *
     **************************************/
    @Test
    public void restore_unknownCommandName_errorMessage() {
        commandBox.runCommand(RestoreCommand.COMMAND_WORD.substring(0, RestoreCommand.COMMAND_WORD.length()
                                                                       - 1));
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand(RestoreCommand.COMMAND_WORD + "a");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void restore_invalidCommandFormat_errorMessage() {
        commandBox.runCommand(RestoreCommand.COMMAND_WORD);
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                          RestoreCommand.MESSAGE_USAGE));
    }

    /*******************************
     * Mixed-case and autocomplete *
     *******************************/
    /**
     * For all mixed-case tests only floating task entries are tested,
     * which should be suitable to test for all types since the type of task
     * doesn't affect the parsing of the command word.
     */
    @Test
    public void restore_firstCharUppercase_success() {
        char[] commandWord = RestoreCommand.COMMAND_WORD.toCharArray();
        commandWord[0] = Character.toUpperCase(commandWord[0]);
        assertRestoreWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void restore_lastCharUppercase_success() {
        char[] commandWord = RestoreCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length - 1] = Character.toUpperCase(commandWord[commandWord.length - 1]);
        assertRestoreWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void restore_middleCharUppercase_success() {
        char[] commandWord = RestoreCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length / 2] = Character.toUpperCase(commandWord[commandWord.length / 2]);
        assertRestoreWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void restore_allCharUppercase_success() {
        String commandWord = RestoreCommand.COMMAND_WORD.toUpperCase();
        assertRestoreWithCommandWord(commandWord);
    }

    @Test
    public void restore_tabAutocomplete_success() {
        for (int i = 3; i < RestoreCommand.COMMAND_WORD.length(); ++i) {
            assertRestoreTabAutocomplete(RestoreCommand.COMMAND_WORD.substring(0, i));
        }
    }

    /**
     * Confirms that the given input string will autocomplete to the correct restore command word.
     */
    private void assertRestoreTabAutocomplete(String input) {
        commandBox.enterCommand(input);
        commandBox.pressTabKey();
        assertCommandBox(RestoreCommand.COMMAND_WORD + " ");
    }

    private void assertRestoreWithCommandWord(String commandWord) {
        Entry[] currentList = SampleEntries.getSampleDeletedFloatingTasks();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        Entry entryToRestore = currentList[targetIndex.getZeroBased()];
        commandBox.runCommand(commandWord + " " + CliSyntax.PREFIX_FLOATINGTASK
                              + " " + targetIndex.getOneBased());
        assertFloatingTaskRestoredNormally(entryToRestore, currentList);
    }

    /**
     * Runs the restore command to restore the event at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of events (before deletion).
     */
    private void assertRestoreEventByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToRestore = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getRestoreEventByIndexCommand(index));
        assertEventRestoredNormally(entryToRestore, currentList);
    }

    /**
     * Runs the restore command to restore the deadline at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of deadlines (before deletion).
     */
    private void assertRestoreDeadlineByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToRestore = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getRestoreDeadlineByIndexCommand(index));
        assertDeadlineRestored(entryToRestore, currentList);
    }

    /**
     * Runs the restore command to restore the floating task at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of floating tasks (before deletion).
     */
    private void assertRestoreFloatingTaskByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToRestore = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getRestoreFloatingTaskByIndexCommand(index));
        assertFloatingTaskRestoredNormally(entryToRestore, currentList);
    }

    private void assertEventRestoredNormally(Entry entryRestored, final Entry[] currentList) {
        assertEventRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertDeadlineRestored(Entry entryRestored, final Entry[] currentList) {
        assertDeadlineRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertFloatingTaskRestoredNormally(Entry entryRestored, final Entry[] currentList) {
        assertFloatingTaskRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertEventRemovedFromList(Entry entryRestored, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryRestored);
        assertTrue(eventListPanel.isListMatching(expectedList));
    }

    private void assertDeadlineRemovedFromList(Entry entryRestored, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryRestored);
        assertTrue(deadlineListPanel.isListMatching(expectedList));
    }

    private void assertFloatingTaskRemovedFromList(Entry entryRestored, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryRestored);
        assertTrue(floatingTaskListPanel.isListMatching(expectedList));
    }

}
