package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.testutil.EntryUtil;
import seedu.multitasky.testutil.TestUtil;
import seedu.multitasky.testutil.TypicalEntries;

//@@author A0125586X
public class DeleteCommandTest extends EntryBookGuiTest {

    @Test
    public void delete_firstFloatingTaskByIndex_success() {
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Index targetIndex = TypicalEntries.INDEX_FIRST_ENTRY;
        assertDeleteFloatingTaskSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_lastFloatingTaskByIndex_success() {
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Index targetIndex = Index.fromOneBased(currentList.length);
        assertDeleteFloatingTaskSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_middleFloatingTaskByIndex_success() {
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Index targetIndex = Index.fromOneBased(currentList.length / 2);
        assertDeleteFloatingTaskSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_invalidFloatingTaskIndex_errorMessage() {
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Index targetIndex = Index.fromOneBased(currentList.length + 1);
        commandBox.runCommand(EntryUtil.getFloatingTaskDeleteByIndexCommand(targetIndex));
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

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
    public void delete_unknownCommandName_errorMessage() {
        commandBox.runCommand("d task");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand("del task");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand("deletee task");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void delete_invalidCommandFormat_errorMessage() {
        commandBox.runCommand("delete");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                          DeleteCommand.MESSAGE_USAGE));
        commandBox.runCommand("delete ");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                          DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void delete_tabAutocompleteFromOneChar_success() {
        assertDeleteTabAutocomplete(DeleteCommand.COMMAND_WORD.substring(0, 1));
    }

    @Test
    public void delete_tabAutocompleteFromTwoCharr_success() {
        assertDeleteTabAutocomplete(DeleteCommand.COMMAND_WORD.substring(0, 2));
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
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Index targetIndex = TypicalEntries.INDEX_FIRST_ENTRY;
        Entry entryToDelete = currentList[targetIndex.getZeroBased()];
        commandBox.runCommand(commandWord + " " + CliSyntax.PREFIX_FLOATINGTASK
                + " " + targetIndex.getOneBased());
        assertFloatingTaskDeleted(entryToDelete, currentList);
    }

    /**
     * Runs the delete command to delete the floating task at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of floating tasks (before deletion).
     */
    private void assertDeleteFloatingTaskSuccess(Index index, final Entry[] currentList) {
        Entry entryToDelete = currentList[index.getZeroBased()];
        commandBox.runCommand(EntryUtil.getFloatingTaskDeleteByIndexCommand(index));
        assertFloatingTaskDeleted(entryToDelete, currentList);
    }

    private void assertFloatingTaskDeleted(Entry entryDeleted, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryDeleted);
        assertTrue(floatingTaskListPanel.isListMatching(expectedList));
        assertResultMessage(String.format(DeleteCommand.MESSAGE_SUCCESS, entryDeleted));
    }

}
