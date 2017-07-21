package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.RestoreByFindCommand;
import seedu.multitasky.logic.commands.RestoreCommand;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.testutil.CommandUtil;
import seedu.multitasky.testutil.SampleEntries;
import seedu.multitasky.testutil.TestUtil;

// @@author A0126623L-reused
public class RestoreCommandTest extends EntryBookGuiTest {

    // @@author A0126623L
    @Before
    public void listDeletedEntries() {
        commandBox.runCommand(CommandUtil.getListBinCommand());
    }
    // @@author

    // @@author A0126623L reused
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
        assertEventRestoredByKeyword(entryToRestore, currentList);
    }

    @Test
    public void restore_deadlineKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleDeletedDeadlines();
        Entry entryToRestore = SampleEntries.APPOINTMENT;
        commandBox.runCommand(RestoreCommand.COMMAND_WORD + " appointment");
        assertDeadlineRestoredByKeyword(entryToRestore, currentList);
    }

    @Test
    public void restore_floatingTaskKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleDeletedFloatingTasks();
        Entry entryToRestore = SampleEntries.BAKE;
        commandBox.runCommand(RestoreCommand.COMMAND_WORD + " baking");
        assertFloatingTaskRestoredByKeyword(entryToRestore, currentList);
    }

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
        assertFloatingTaskRestored(entryToRestore, currentList);
    }

    /**
     * Runs the restore command to restore the event at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of events (before deletion).
     */
    private void assertRestoreEventByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToRestore = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getRestoreEventByIndexCommand(index));
        assertEventRestored(entryToRestore, currentList);
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
        assertFloatingTaskRestored(entryToRestore, currentList);
    }

    private void assertEventRestored(Entry entryRestored, final Entry[] currentList) {
        assertEventRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertEventRestoredByKeyword(Entry entryRestored, final Entry[] currentList) {
        assertEventRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreByFindCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertDeadlineRestored(Entry entryRestored, final Entry[] currentList) {
        assertDeadlineRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertDeadlineRestoredByKeyword(Entry entryRestored, final Entry[] currentList) {
        assertDeadlineRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreByFindCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertFloatingTaskRestored(Entry entryRestored, final Entry[] currentList) {
        assertFloatingTaskRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertFloatingTaskRestoredByKeyword(Entry entryRestored, final Entry[] currentList) {
        assertFloatingTaskRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreByFindCommand.MESSAGE_SUCCESS, entryRestored));
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
