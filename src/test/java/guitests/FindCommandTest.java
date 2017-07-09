package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.commands.FindCommand;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Entry;

//@@author A0125586X
public class FindCommandTest extends EntryBookGuiTest {

    @Test
    public void find_nonmatchingKeyword_noResult() {
        assertFindFloatingTaskResult("making", 0);
    }

    @Test
    public void find_matchingKeyword_singleResult() {
        assertFindFloatingTaskResult("programming", 1, typicalEntries.programming);
    }

    @Test
    public void find_matchingKeyword_multipleResults() {
        assertFindFloatingTaskResult("learn", 2, typicalEntries.cook, typicalEntries.programming);
    }

    @Test
    public void find_matchingKeywordAfterDeleting_singleResult() {
        assertFindFloatingTaskResult("learn", 2, typicalEntries.cook, typicalEntries.programming);
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_FLOATINGTASK + " 1");
        assertFindFloatingTaskResult("learn", 1, typicalEntries.programming);
    }

    @Test
    public void find_emptyList_noResult() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertFindFloatingTaskResult("learn", 0);
    }

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
    public void find_unknownCommandName_errorMessage() {
        commandBox.runCommand("f");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand("fin");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand("findd");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void find_invalidCommandFormat_errorMessage() {
        commandBox.runCommand("find");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                          FindCommand.MESSAGE_USAGE));

        commandBox.runCommand("find ");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                          FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void find_tabAutocompleteFromOneChar_success() {
        assertFindTabAutocomplete(FindCommand.COMMAND_WORD.substring(0, 1));
    }

    @Test
    public void find_tabAutocompleteFromTwoCharr_success() {
        assertFindTabAutocomplete(FindCommand.COMMAND_WORD.substring(0, 2));
    }

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
        assertFindFloatingTaskListMessage(1, typicalEntries.programming);
    }

    private void assertFindFloatingTaskResult(String keywords, int numExpectedTotalResults,
                                              Entry... expectedFloatingTaskResults) {
        commandBox.runCommand(FindCommand.COMMAND_WORD + " " + keywords);
        assertFindFloatingTaskListMessage(numExpectedTotalResults, expectedFloatingTaskResults);
    }

    private void assertFindFloatingTaskListMessage(int numExpectedTotalResults,
                                                   Entry... expectedFloatingTaskResults) {
        assertFloatingTaskListSize(expectedFloatingTaskResults.length);
        assertResultMessage(numExpectedTotalResults + " entries listed!");
        assertTrue(floatingTaskListPanel.isListMatching(expectedFloatingTaskResults));
    }

}
