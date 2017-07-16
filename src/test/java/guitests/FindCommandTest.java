package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.input.KeyCode;
import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.logic.commands.FindCommand;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.testutil.SampleEntries;

// @@author A0125586X
public class FindCommandTest extends EntryBookGuiTest {

    /**************
     * No matches *
     *************/
    @Test
    public void find_nonMatchingKeyword_noResult() {
        commandBox.runCommand(FindCommand.COMMAND_WORD + " " + "arstatrs");
        assertFindEventListMessage(0);
        assertFindDeadlineListMessage(0);
        assertFindFloatingTaskListMessage(0);
    }

    @Test
    public void find_emptyList_noResult() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        commandBox.runCommand(FindCommand.COMMAND_WORD + " " + "e");
        assertFindEventListMessage(0);
        assertFindDeadlineListMessage(0);
        assertFindFloatingTaskListMessage(0);
    }

    /*******************************
     * Single result for each type *
     ******************************/
    @Test
    public void find_matchingPartialKeyword_singleEventResult() {
        assertFindEventResult("nne", 1, SampleEntries.DINNER);
    }

    @Test
    public void find_matchingFullKeyword_singleEventResult() {
        assertFindEventResult("dinner", 1, SampleEntries.DINNER);
    }

    @Test
    public void find_matchingPartialKeyword_singleDeadlineResult() {
        assertFindDeadlineResult("rms", 1, SampleEntries.TAX);
    }

    @Test
    public void find_matchingFullKeyword_singleDeadlineResult() {
        assertFindDeadlineResult("forms", 1, SampleEntries.TAX);
    }

    @Test
    public void find_matchingPartialKeyword_singleFloatingTaskResult() {
        assertFindFloatingTaskResult("gra", 1, SampleEntries.PROGRAMMING);
    }

    @Test
    public void find_matchingFullKeyword_singleFloatingTaskResult() {
        assertFindFloatingTaskResult("programming", 1, SampleEntries.PROGRAMMING);
    }

    /**********************************
     * Multiple results for each type *
     *********************************/
    @Test
    public void find_matchingPartialKeyword_multipleDeadlineResults() {
        assertFindDeadlineResult("s", 4, SampleEntries.TAX, SampleEntries.PAPER);
    }

    @Test
    public void find_matchingFullKeyword_multipleFloatingTaskResults() {
        assertFindFloatingTaskResult("learn", 2, SampleEntries.COOK, SampleEntries.PROGRAMMING);
    }

    /**************************************
     * Different types of invalid wording *
     **************************************/
    @Test
    public void find_unknownCommandName_errorMessage() {
        commandBox.runCommand(FindCommand.COMMAND_WORD.substring(0, FindCommand.COMMAND_WORD.length() - 1));
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand(FindCommand.COMMAND_WORD + "a");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void find_invalidCommandFormat_errorMessage() {
        commandBox.runCommand(FindCommand.COMMAND_WORD);
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                          FindCommand.MESSAGE_USAGE));
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
    public void find_firstCharUppercase_success() {
        char[] commandWord = FindCommand.COMMAND_WORD.toCharArray();
        commandWord[0] = Character.toUpperCase(commandWord[0]);
        assertFindWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void find_lastCharUppercase_success() {
        char[] commandWord = FindCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length - 1] = Character.toUpperCase(commandWord[commandWord.length - 1]);
        assertFindWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void find_middleCharUppercase_success() {
        char[] commandWord = FindCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length / 2] = Character.toUpperCase(commandWord[commandWord.length / 2]);
        assertFindWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void find_allCharUppercase_success() {
        String commandWord = FindCommand.COMMAND_WORD.toUpperCase();
        assertFindWithCommandWord(commandWord);
    }

    @Test
    public void find_tabAutocomplete_success() {
        for (int i = 1; i < FindCommand.COMMAND_WORD.length(); ++i) {
            assertFindTabAutocomplete(FindCommand.COMMAND_WORD.substring(0, i));
        }
        assertFindTabAutocomplete(FindCommand.COMMAND_WORD + "a");
        assertFindTabAutocomplete(FindCommand.COMMAND_WORD + "aa");
    }

    @Test
    public void find_keyboardShortcut_success() {
        commandBox.pressKey(KeyCode.F3);
        assertTrue(commandBox.getCommandInput().equals(FindCommand.COMMAND_WORD + " "));
    }

    /*******************
     * Utility methods *
     ******************/
    /**
     * Confirms that the given input string will autocomplete to the correct find command word.
     */
    private void assertFindTabAutocomplete(String input) {
        commandBox.enterCommand(input);
        commandBox.pressTabKey();
        assertCommandBox(FindCommand.COMMAND_WORD + " ");
    }

    private void assertFindWithCommandWord(String commandWord) {
        commandBox.runCommand(commandWord + " programming");
        assertFindFloatingTaskListMessage(1, SampleEntries.PROGRAMMING);
    }

    private void assertFindEventResult(String keywords, int numExpectedTotalResults,
                                       Entry... expectedEventResults) {
        commandBox.runCommand(FindCommand.COMMAND_WORD + " " + keywords);
        assertFindEventListMessage(numExpectedTotalResults, expectedEventResults);
    }

    private void assertFindDeadlineResult(String keywords, int numExpectedTotalResults,
                                           Entry... expectedDeadlineResults) {
        commandBox.runCommand(FindCommand.COMMAND_WORD + " " + keywords);
        assertFindDeadlineListMessage(numExpectedTotalResults, expectedDeadlineResults);
    }

    private void assertFindFloatingTaskResult(String keywords, int numExpectedTotalResults,
                                              Entry... expectedFloatingTaskResults) {
        commandBox.runCommand(FindCommand.COMMAND_WORD + " " + keywords);
        assertFindFloatingTaskListMessage(numExpectedTotalResults, expectedFloatingTaskResults);
    }

    private void assertFindEventListMessage(int numExpectedTotalResults, Entry... expectedEventResults) {
        assertEventListSize(expectedEventResults.length);
        assertResultMessage(numExpectedTotalResults + " entries listed!");
        assertTrue(eventListPanel.isListMatching(expectedEventResults));
    }

    private void assertFindDeadlineListMessage(int numExpectedTotalResults,
                                               Entry... expectedDeadlineResults) {
        assertDeadlineListSize(expectedDeadlineResults.length);
        assertResultMessage(numExpectedTotalResults + " entries listed!");
        assertTrue(deadlineListPanel.isListMatching(expectedDeadlineResults));
    }

    private void assertFindFloatingTaskListMessage(int numExpectedTotalResults,
                                                   Entry... expectedFloatingTaskResults) {
        assertFloatingTaskListSize(expectedFloatingTaskResults.length);
        assertResultMessage(numExpectedTotalResults + " entries listed!");
        assertTrue(floatingTaskListPanel.isListMatching(expectedFloatingTaskResults));
    }

}
