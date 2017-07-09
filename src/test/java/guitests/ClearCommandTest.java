package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.testutil.EntryUtil;

//@@author A0125586X
public class ClearCommandTest extends EntryBookGuiTest {

    @Test
    public void clear_emptyList_success() {
        assertClearCommandSuccess();
        assertClearCommandSuccess();
    }

    @Test
    public void clear_nonEmptyList_success() {
        //TODO add support to check other types of tasks in the future
        assertTrue(eventListPanel.isEmpty());
        assertTrue(deadlineListPanel.isEmpty());
        assertClearCommandSuccess();
    }

    @Test
    public void clear_addAfterClear_success() {
        assertClearCommandSuccess();
        commandBox.runCommand(EntryUtil.getFloatingTaskAddCommand(typicalEntries.clean));
        assertTrue(floatingTaskListPanel.isListMatching(typicalEntries.clean));
    }

    @Test
    public void clear_firstCharUppercase_success() {
        char[] commandWord = ClearCommand.COMMAND_WORD.toCharArray();
        commandWord[0] = Character.toUpperCase(commandWord[0]);
        commandBox.runCommand(String.copyValueOf(commandWord));
        assertCleared();
    }

    @Test
    public void clear_lastCharUppercase_success() {
        char[] commandWord = ClearCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length - 1] = Character.toUpperCase(commandWord[commandWord.length - 1]);
        commandBox.runCommand(String.copyValueOf(commandWord));
        assertCleared();
    }

    @Test
    public void clear_middleCharUppercase_success() {
        char[] commandWord = ClearCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length / 2] = Character.toUpperCase(commandWord[commandWord.length / 2]);
        commandBox.runCommand(String.copyValueOf(commandWord));
        assertCleared();
    }

    @Test
    public void clear_allCharUppercase_success() {
        String commandWord = ClearCommand.COMMAND_WORD.toUpperCase();
        commandBox.runCommand(commandWord);
        assertCleared();
    }

    @Test
    public void clear_unknownCommandName_errorMessage() {
        commandBox.runCommand("c");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand("cle");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand("clearr");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void clear_tabAutocompleteFromOneChar_success() {
        String commandWord = ClearCommand.COMMAND_WORD.substring(0, 1);
        commandBox.enterCommand(commandWord);
        commandBox.pressTabKey();
        assertCommandBox(ClearCommand.COMMAND_WORD + " ");
    }

    @Test
    public void add_tabAutocompleteFromTwoChars_success() {
        String commandWord = ClearCommand.COMMAND_WORD.substring(0, 2);
        commandBox.enterCommand(commandWord);
        commandBox.pressTabKey();
        assertCommandBox(ClearCommand.COMMAND_WORD + " ");
    }

    private void assertCleared() {
        assertTrue(eventListPanel.isEmpty());
        assertTrue(deadlineListPanel.isEmpty());
        assertTrue(floatingTaskListPanel.isEmpty());
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertCleared();
        assertResultMessage("Entry book has been cleared!");
    }
}
