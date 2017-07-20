package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.DeleteByFindCommand;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.testutil.CommandUtil;
import seedu.multitasky.testutil.SampleEntries;
import seedu.multitasky.testutil.TestUtil;

// @@author A0125586X
public class DeleteCommandTest extends EntryBookGuiTest {

    /*********************
     * Deleting by Index *
     ********************/
    @Test
    public void delete_firstEventByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleActiveEvents();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertDeleteEventByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_lastEventByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleActiveEvents();
        Index targetIndex = Index.fromOneBased(currentList.length);
        assertDeleteEventByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_invalidEventIndex_errorMessage() {
        Entry[] currentList = SampleEntries.getSampleActiveEvents();
        Index targetIndex = Index.fromOneBased(currentList.length + 1);
        commandBox.runCommand(CommandUtil.getDeleteEventByIndexCommand(targetIndex));
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void delete_firstDeadlineByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleActiveDeadlines();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertDeleteDeadlineByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_lastDeadlineByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleActiveDeadlines();
        Index targetIndex = Index.fromOneBased(currentList.length);
        assertDeleteDeadlineByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_invalidDeadlineIndex_errorMessage() {
        Entry[] currentList = SampleEntries.getSampleActiveDeadlines();
        Index targetIndex = Index.fromOneBased(currentList.length + 1);
        commandBox.runCommand(CommandUtil.getDeleteDeadlineByIndexCommand(targetIndex));
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void delete_firstFloatingTaskByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleActiveFloatingTasks();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertDeleteFloatingTaskByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_lastFloatingTaskByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleActiveFloatingTasks();
        Index targetIndex = Index.fromOneBased(currentList.length);
        assertDeleteFloatingTaskByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_invalidFloatingTaskIndex_errorMessage() {
        Entry[] currentList = SampleEntries.getSampleActiveFloatingTasks();
        Index targetIndex = Index.fromOneBased(currentList.length + 1);
        commandBox.runCommand(CommandUtil.getDeleteFloatingTaskByIndexCommand(targetIndex));
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /***********************
     * Deleting by Keyword *
     **********************/
    @Test
    public void delete_eventKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleActiveEvents();
        Entry entryToDelete = SampleEntries.DINNER;
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " dinner");
        assertEventDeletedByKeyword(entryToDelete, currentList);
    }

    @Test
    public void delete_deadlineKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleActiveDeadlines();
        Entry entryToDelete = SampleEntries.TAX;
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " tax");
        assertDeadlineDeletedByKeyword(entryToDelete, currentList);
    }

    @Test
    public void delete_floatingTaskKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleActiveFloatingTasks();
        Entry entryToDelete = SampleEntries.PROGRAMMING;
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " programming");
        assertFloatingTaskDeletedByKeyword(entryToDelete, currentList);
    }

    /**************************************
     * Different types of invalid wording *
     *************************************/
    @Test
    public void delete_unknownCommandName_errorMessage() {
        commandBox.runCommand(DeleteCommand.COMMAND_WORD
                              .substring(0, DeleteCommand.COMMAND_WORD.length() - 1));
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand(DeleteCommand.COMMAND_WORD + "a");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void delete_invalidCommandFormat_errorMessage() {
        commandBox.runCommand(DeleteCommand.COMMAND_WORD);
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                          DeleteCommand.MESSAGE_USAGE));
    }

    /*******************************
     * Mixed-case and autocomplete *
     ******************************/
    /**
     * For all mixed-case tests only floating task entries are tested,
     * which should be suitable to test for all types since the type of task
     * doesn't affect the parsing of the command word.
     */
    @Test
    public void delete_firstCharUppercase_success() {
        char[] commandWord = DeleteCommand.COMMAND_WORD.toCharArray();
        commandWord[0] = Character.toUpperCase(commandWord[0]);
        assertDeleteWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void delete_lastCharUppercase_success() {
        char[] commandWord = DeleteCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length - 1] = Character.toUpperCase(commandWord[commandWord.length - 1]);
        assertDeleteWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void delete_middleCharUppercase_success() {
        char[] commandWord = DeleteCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length / 2] = Character.toUpperCase(commandWord[commandWord.length / 2]);
        assertDeleteWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void delete_allCharUppercase_success() {
        String commandWord = DeleteCommand.COMMAND_WORD.toUpperCase();
        assertDeleteWithCommandWord(commandWord);
    }

    @Test
    public void delete_tabAutocomplete_success() {
        for (int i = 1; i < DeleteCommand.COMMAND_WORD.length(); ++i) {
            assertDeleteTabAutocomplete(DeleteCommand.COMMAND_WORD.substring(0, i));
        }
    }

    /**
     * Confirms that the given input string will autocomplete to the correct delete command word.
     */
    private void assertDeleteTabAutocomplete(String input) {
        commandBox.enterCommand(input);
        commandBox.pressTabKey();
        assertCommandBox(DeleteCommand.COMMAND_WORD + " ");
    }

    private void assertDeleteWithCommandWord(String commandWord) {
        Entry[] currentList = SampleEntries.getSampleActiveFloatingTasks();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        Entry entryToDelete = currentList[targetIndex.getZeroBased()];
        commandBox.runCommand(commandWord + " " + CliSyntax.PREFIX_FLOATINGTASK
                + " " + targetIndex.getOneBased());
        assertFloatingTaskDeleted(entryToDelete, currentList);
    }

    /**
     * Runs the delete command to delete the event at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of events (before deletion).
     */
    private void assertDeleteEventByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToDelete = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getDeleteEventByIndexCommand(index));
        assertEventDeleted(entryToDelete, currentList);
    }

    /**
     * Runs the delete command to delete the deadline at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of deadlines (before deletion).
     */
    private void assertDeleteDeadlineByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToDelete = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getDeleteDeadlineByIndexCommand(index));
        assertDeadlineDeleted(entryToDelete, currentList);
    }

    /**
     * Runs the delete command to delete the floating task at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of floating tasks (before deletion).
     */
    private void assertDeleteFloatingTaskByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToDelete = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getDeleteFloatingTaskByIndexCommand(index));
        assertFloatingTaskDeleted(entryToDelete, currentList);
    }

    private void assertEventDeleted(Entry entryDeleted, final Entry[] currentList) {
        assertEventRemovedFromList(entryDeleted, currentList);
        assertResultMessage(String.format(DeleteCommand.MESSAGE_SUCCESS, entryDeleted));
    }

    private void assertEventDeletedByKeyword(Entry entryDeleted, final Entry[] currentList) {
        assertEventRemovedFromList(entryDeleted, currentList);
        assertResultMessage(String.format(DeleteByFindCommand.MESSAGE_SUCCESS, entryDeleted));
    }

    private void assertDeadlineDeleted(Entry entryDeleted, final Entry[] currentList) {
        assertDeadlineRemovedFromList(entryDeleted, currentList);
        assertResultMessage(String.format(DeleteCommand.MESSAGE_SUCCESS, entryDeleted));
    }

    private void assertDeadlineDeletedByKeyword(Entry entryDeleted, final Entry[] currentList) {
        assertDeadlineRemovedFromList(entryDeleted, currentList);
        assertResultMessage(String.format(DeleteByFindCommand.MESSAGE_SUCCESS, entryDeleted));
    }

    private void assertFloatingTaskDeleted(Entry entryDeleted, final Entry[] currentList) {
        assertFloatingTaskRemovedFromList(entryDeleted, currentList);
        assertResultMessage(String.format(DeleteCommand.MESSAGE_SUCCESS, entryDeleted));
    }

    private void assertFloatingTaskDeletedByKeyword(Entry entryDeleted, final Entry[] currentList) {
        assertFloatingTaskRemovedFromList(entryDeleted, currentList);
        assertResultMessage(String.format(DeleteByFindCommand.MESSAGE_SUCCESS, entryDeleted));
    }

    private void assertEventRemovedFromList(Entry entryDeleted, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryDeleted);
        assertTrue(eventListPanel.isListMatching(expectedList));
    }

    private void assertDeadlineRemovedFromList(Entry entryDeleted, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryDeleted);
        assertTrue(deadlineListPanel.isListMatching(expectedList));
    }

    private void assertFloatingTaskRemovedFromList(Entry entryDeleted, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryDeleted);
        assertTrue(floatingTaskListPanel.isListMatching(expectedList));
    }

}
