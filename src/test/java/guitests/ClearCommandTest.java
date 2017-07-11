package guitests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.testutil.EntryUtil;

//@@author A0125586X
public class ClearCommandTest extends EntryBookGuiTest {

    /*********************
     * Clearing the list *
     ********************/
    @Test
    public void clear_emptyList_success() {
        assertClearCommandSuccess();
        assertClearCommandSuccess();
    }

    @Test
    public void clear_nonEmptyList_success() {
        assertFalse(eventListPanel.isEmpty());
        assertFalse(deadlineListPanel.isEmpty());
        assertFalse(floatingTaskListPanel.isEmpty());
        assertClearCommandSuccess();
    }

    /**********************************
     * Adding after clearing the list *
     *********************************/
    @Test
    public void clear_addEventAfterClear_success() {
        assertClearCommandSuccess();
        commandBox.runCommand(EntryUtil.getEventAddCommand(typicalEntries.cat));
        assertTrue(eventListPanel.isListMatching(typicalEntries.cat));
    }

    @Test
    public void clear_addDeadlineAfterClear_success() {
        assertClearCommandSuccess();
        commandBox.runCommand(EntryUtil.getDeadlineAddCommand(typicalEntries.submission));
        assertTrue(deadlineListPanel.isListMatching(typicalEntries.submission));
    }

    @Test
    public void clear_addFloatingTaskAfterClear_success() {
        assertClearCommandSuccess();
        commandBox.runCommand(EntryUtil.getFloatingTaskAddCommand(typicalEntries.clean));
        assertTrue(floatingTaskListPanel.isListMatching(typicalEntries.clean));
    }

    /**************************************
     * Different types of invalid wording *
     **************************************/
    @Test
    public void clear_unknownCommandName_errorMessage() {
        commandBox.runCommand("clea");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand("clearr");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }


    /*******************************
     * Mixed-case and autocomplete *
     ******************************/
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
    public void clear_tabAutocomplete_success() {
        for (int i = 1; i < ClearCommand.COMMAND_WORD.length(); ++i) {
            assertClearTabAutocomplete(ClearCommand.COMMAND_WORD.substring(0, i));
        }
    }

    /**
     * Confirms that the given input string will autocomplete to the correct command word.
     */
    private void assertClearTabAutocomplete(String input) {
        commandBox.enterCommand(input);
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
