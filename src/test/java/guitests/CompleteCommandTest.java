package guitests;

import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.CompleteByFindCommand;
import seedu.multitasky.logic.commands.CompleteCommand;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.logic.parser.ParserUtil;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.testutil.SampleEntries;

// @@author A0132788U
public class CompleteCommandTest extends EntryBookGuiTest {

    /***************************
     * Complete Command Parser Tests *
     **************************/

    @Test
    public void complete_noArgsEmptyCompleteCommand_failure() {
        commandBox.runCommand(CompleteCommand.COMMAND_WORD);
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void complete_invalidFlagCombination_failure() {
        commandBox.runCommand(CompleteCommand.COMMAND_WORD + " "
                              + CliSyntax.PREFIX_FLOATINGTASK + " "
                              + CliSyntax.PREFIX_DEADLINE);
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompleteCommand.MESSAGE_USAGE));

        commandBox.runCommand(CompleteCommand.COMMAND_WORD + " "
                              + CliSyntax.PREFIX_EVENT + " "
                              + CliSyntax.PREFIX_DEADLINE);
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompleteCommand.MESSAGE_USAGE));

        commandBox.runCommand(CompleteCommand.COMMAND_WORD + " "
                              + CliSyntax.PREFIX_FLOATINGTASK + " "
                              + CliSyntax.PREFIX_EVENT);
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void complete_nonIntegerIndexTypeString_failure() {
        String stringIndex = "RANDOM";
        commandBox.runCommand(CompleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_FLOATINGTASK + " " + stringIndex);
        assertResultMessage(ParserUtil.MESSAGE_INVALID_INDEX);
    }

    /***************************
     * Complete By Index Tests *
     **************************/
    @Test
    public void complete_firstDeadline_success() {
        Entry[] currentList = SampleEntries.getSampleDeadlines();
        Entry entryToComplete = currentList[0];
        commandBox.runCommand(CompleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DEADLINE + " 1");
        assertResultMessage(String.format(CompleteCommand.MESSAGE_SUCCESS, entryToComplete));
    }

    @Test
    public void complete_invalidIndex_failure() {
        Entry[] currentList = SampleEntries.getSampleEvents();
        int index = currentList.length + 1;
        commandBox.runCommand(CompleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_EVENT + " " + index);
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /***************************
     * Complete By Find Tests *
     **************************/

    @Test
    public void complete_firstDeadlineByKeyword_success() {
        Entry entryToComplete = SampleEntries.TAX;
        commandBox.runCommand(CompleteCommand.COMMAND_WORD + " tax");
        assertResultMessage(String.format(CompleteCommand.MESSAGE_SUCCESS, entryToComplete));
    }

    @Test
    public void complete_noEntriesExist_failure() {
        commandBox.runCommand(CompleteCommand.COMMAND_WORD + " doesn't exist");
        assertResultMessage(CompleteByFindCommand.MESSAGE_NO_ENTRIES);
    }

    @Test
    public void complete_multipleEntriesExist_failure() {
        commandBox.runCommand(CompleteCommand.COMMAND_WORD + " cook");
        assertResultMessage(CompleteByFindCommand.MESSAGE_MULTIPLE_ENTRIES);
    }

}
