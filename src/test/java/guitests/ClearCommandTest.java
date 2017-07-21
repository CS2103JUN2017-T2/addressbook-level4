package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.testutil.CommandUtil;
import seedu.multitasky.testutil.SampleEntries;

// @@author A0125586X
public class ClearCommandTest extends EntryBookGuiTest {

    /*********************
     * Clearing the list *
     ********************/
    @Test
    public void clearAll_emptyList_success() {
        assertClearAllCommandSuccess();
        assertClearAllCommandSuccess();
    }
    // @@author

    // @@author A0126623L
    @Test
    public void clearActive_emptyList_success() {
        assertClearActiveCommandSuccess();
        assertClearActiveCommandSuccess();
    }
    // @@author

    // @@author A0126623L
    @Test
    public void clearArchive_emptyList_success() {
        assertClearArchiveCommandSuccess();
        assertClearArchiveCommandSuccess();
    }
    // @@author

    // @@author A0126623L
    @Test
    public void clearBin_emptyList_success() {
        assertClearBinCommandSuccess();
        assertClearBinCommandSuccess();
    }
    // @@author

    /*************************************
     * Adding after clearing all entries *
     *************************************/
    // @@author A0125586X
    @Test
    public void clear_addEventAfterClearAll_success() {
        assertClearAllCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddEventCommand(SampleEntries.CAT));
        assertTrue(eventListPanel.isListMatching(SampleEntries.CAT));
    }

    @Test
    public void clear_addDeadlineAfterClearAll_success() {
        assertClearAllCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddDeadlineCommand(SampleEntries.SUBMISSION));
        assertTrue(deadlineListPanel.isListMatching(SampleEntries.SUBMISSION));
    }

    @Test
    public void clear_addFloatingTaskAfterClearAll_success() {
        assertClearAllCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddFloatingTaskCommand(SampleEntries.CLEAN));
        assertTrue(floatingTaskListPanel.isListMatching(SampleEntries.CLEAN));
    }
    // @@author

    /********************************************
     * Adding after clearing all active entries *
     ********************************************/
    // @@author A0126623L
    @Test
    public void clear_addEventAfterClearActive_success() {
        assertClearActiveCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddEventCommand(SampleEntries.CAT));
        commandBox.runCommand(CommandUtil.getListCommand());
        assertTrue(eventListPanel.isListMatching(SampleEntries.CAT));
    }
    // @@author

    // @@author A0126623L
    @Test
    public void clear_addDeadlineAfterClearActive_success() {
        assertClearActiveCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddDeadlineCommand(SampleEntries.SUBMISSION));
        commandBox.runCommand(CommandUtil.getListCommand());
        assertTrue(deadlineListPanel.isListMatching(SampleEntries.SUBMISSION));
    }
    // @@author

    // @@author A0126623L
    @Test
    public void clear_addFloatingTaskAfterClearActive_success() {
        assertClearActiveCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddFloatingTaskCommand(SampleEntries.CLEAN));
        commandBox.runCommand(CommandUtil.getListCommand());
        assertTrue(floatingTaskListPanel.isListMatching(SampleEntries.CLEAN));
    }
    // @@author

    /*********************************
     * Adding after clearing archive *
     *********************************/
    // @@author A0126623L
    @Test
    public void clear_completeEventAfterClearArchive_success() {
        assertClearArchiveCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddEventCommand(SampleEntries.CAT));
        commandBox.runCommand(CommandUtil.getCompleteByFullNameCommand(SampleEntries.CAT));
        commandBox.runCommand(CommandUtil.getListArchiveCommand());
        assertTrue(eventListPanel.isListMatching(SampleEntries.CAT));
    }
    // @@author

    // @@author A0126623L
    @Test
    public void clear_completeDeadlineAfterClearArchive_success() {
        assertClearArchiveCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddDeadlineCommand(SampleEntries.SUBMISSION));
        commandBox.runCommand(CommandUtil.getCompleteByFullNameCommand(SampleEntries.SUBMISSION));
        commandBox.runCommand(CommandUtil.getListArchiveCommand());
        assertTrue(deadlineListPanel.isListMatching(SampleEntries.SUBMISSION));
    }
    // @@author

    // @@author A0126623L
    @Test
    public void clear_completeFloatingTaskAfterClearArchive_success() {
        assertClearArchiveCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddFloatingTaskCommand(SampleEntries.CLEAN));
        commandBox.runCommand(CommandUtil.getCompleteByFullNameCommand(SampleEntries.CLEAN));
        commandBox.runCommand(CommandUtil.getListArchiveCommand());
        assertTrue(floatingTaskListPanel.isListMatching(SampleEntries.CLEAN));
    }
    // @@author

    /*****************************
     * Adding after clearing bin *
     *****************************/
    // @@author A0126623L
    @Test
    public void clear_completeEventAfterClearBin_success() {
        assertClearBinCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddEventCommand(SampleEntries.CAT));
        commandBox.runCommand(CommandUtil.getDeleteByFullNameCommand(SampleEntries.CAT));
        commandBox.runCommand(CommandUtil.getListBinCommand());
        assertTrue(eventListPanel.isListMatching(SampleEntries.CAT));
    }
    // @@author

    // @@author A0126623L
    @Test
    public void clear_completeDeadlineAfterClearBin_success() {
        assertClearBinCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddDeadlineCommand(SampleEntries.SUBMISSION));
        commandBox.runCommand(CommandUtil.getDeleteByFullNameCommand(SampleEntries.SUBMISSION));
        commandBox.runCommand(CommandUtil.getListBinCommand());
        assertTrue(deadlineListPanel.isListMatching(SampleEntries.SUBMISSION));
    }
    // @@author

    // @@author A0126623L
    @Test
    public void clear_completeFloatingTaskAfterClearBin_success() {
        assertClearBinCommandSuccess();
        commandBox.runCommand(CommandUtil.getAddFloatingTaskCommand(SampleEntries.CLEAN));
        commandBox.runCommand(CommandUtil.getDeleteByFullNameCommand(SampleEntries.CLEAN));
        commandBox.runCommand(CommandUtil.getListBinCommand());
        assertTrue(floatingTaskListPanel.isListMatching(SampleEntries.CLEAN));
    }
    // @@author

    /**************************************
     * Different types of invalid wording *
     **************************************/
    // @@author A0125586X
    @Test
    public void clear_unknownCommandName_errorMessage() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD.substring(0, ClearCommand.COMMAND_WORD.length() - 1));
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand(ClearCommand.COMMAND_WORD + "a");
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
        assertCurrentlyViewedPanelCleared();
    }

    @Test
    public void clear_lastCharUppercase_success() {
        char[] commandWord = ClearCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length - 1] = Character.toUpperCase(commandWord[commandWord.length - 1]);
        commandBox.runCommand(String.copyValueOf(commandWord));
        assertCurrentlyViewedPanelCleared();
    }

    @Test
    public void clear_middleCharUppercase_success() {
        char[] commandWord = ClearCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length / 2] = Character.toUpperCase(commandWord[commandWord.length / 2]);
        commandBox.runCommand(String.copyValueOf(commandWord));
        assertCurrentlyViewedPanelCleared();
    }

    @Test
    public void clear_allCharUppercase_success() {
        String commandWord = ClearCommand.COMMAND_WORD.toUpperCase();
        commandBox.runCommand(commandWord);
        assertCurrentlyViewedPanelCleared();
    }

    @Test
    public void clear_tabAutocomplete_success() {
        assertClearTabAutocompleteFailure(ClearCommand.COMMAND_WORD.substring(0, 1));
        for (int i = 2; i < ClearCommand.COMMAND_WORD.length(); ++i) {
            assertClearTabAutocomplete(ClearCommand.COMMAND_WORD.substring(0, i));
        }
        assertClearTabAutocomplete(ClearCommand.COMMAND_WORD + "a");
    }

    /**
     * Confirms that the given input string will autocomplete to the correct command word.
     */
    private void assertClearTabAutocomplete(String input) {
        commandBox.enterCommand(input);
        commandBox.pressTabKey();
        assertCommandBox(ClearCommand.COMMAND_WORD + " ");
    }

    private void assertClearTabAutocompleteFailure(String input) {
        commandBox.enterCommand(input);
        commandBox.pressTabKey();
        assertCommandBox(input);
    }

    private void assertCurrentlyViewedPanelCleared() {
        assertTrue(eventListPanel.isEmpty());
        assertTrue(deadlineListPanel.isEmpty());
        assertTrue(floatingTaskListPanel.isEmpty());
    }

    private void assertClearAllCommandSuccess() {
        commandBox.runCommand(CommandUtil.getClearAllCommand());
        assertCurrentlyViewedPanelCleared();
        assertResultMessage(ClearCommand.MESSAGE_ALL_SUCCESS);
    }
    // @@author

    // @@A0126623L
    /**
     * Executes {@code list}, followed by {@code clear}
     */
    private void assertClearActiveCommandSuccess() {
        commandBox.runCommand(CommandUtil.getListCommand());
        commandBox.runCommand(CommandUtil.getClearCommand());
        assertCurrentlyViewedPanelCleared();
        assertResultMessage(ClearCommand.MESSAGE_ACTIVE_SUCCESS);
    }
    // @@author

    // @@author A0126623L
    /**
     * Executes {@code list archive}, followed by {@code clear archive}
     */
    private void assertClearArchiveCommandSuccess() {
        commandBox.runCommand(CommandUtil.getListArchiveCommand());
        commandBox.runCommand(CommandUtil.getClearArchiveCommand());
        assertCurrentlyViewedPanelCleared();
        assertResultMessage(ClearCommand.MESSAGE_ARCHIVE_SUCCESS);
    }
    // @@author

    // @@author A0126623L
    /**
     * Executes {@code list bin}, followed by {@code clear bin}
     */
    private void assertClearBinCommandSuccess() {
        commandBox.runCommand(CommandUtil.getListBinCommand());
        commandBox.runCommand(CommandUtil.getClearBinCommand());
        assertCurrentlyViewedPanelCleared();
        assertResultMessage(ClearCommand.MESSAGE_BIN_SUCCESS);
    }
    // @@author
}
