package seedu.multitasky.ui.util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.logic.commands.CompleteCommand;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.commands.ExitCommand;
import seedu.multitasky.logic.commands.FindCommand;
import seedu.multitasky.logic.commands.HelpCommand;
import seedu.multitasky.logic.commands.HistoryCommand;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.commands.OpenCommand;
import seedu.multitasky.logic.commands.RedoCommand;
import seedu.multitasky.logic.commands.RestoreCommand;
import seedu.multitasky.logic.commands.SaveCommand;
import seedu.multitasky.logic.commands.UndoCommand;
import seedu.multitasky.logic.commands.util.CommandUtil;

// @@author A0125586X
public class CommandWordAutocompleteTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TextAutocomplete commandWordAutoComplete;

    @Before
    public void setUp() {
        commandWordAutoComplete = new CommandWordAutocomplete(CommandUtil.COMMAND_WORDS);
    }

    @Test
    public void commandKeywordAutocomplete_nullArgument_exception() {
        thrown.expect(AssertionError.class);
        new CommandWordAutocomplete(null);
    }

    @Test
    public void commandKeywordAutocomplete_invalidCommandKeywords_noChange() {
        assertStringEquals(commandWordAutoComplete.autocomplete("arstarsta"), "arstarsta");
    }

    @Test
    public void commandKeywordAutocomplete_validCommandWords_complete() {
        assertAutocomplete(commandWordAutoComplete, "a", AddCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "cl", ClearCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "co", CompleteCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "d", DeleteCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "ed", EditCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "x", ExitCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "f", FindCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "lp", HelpCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "y", HistoryCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "l", ListCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "o", OpenCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "red", RedoCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "res", RestoreCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "s", SaveCommand.COMMAND_WORD);
        assertAutocomplete(commandWordAutoComplete, "u", UndoCommand.COMMAND_WORD);
    }

    private void assertAutocomplete(TextAutocomplete textAutoComplete, String input, String expected) {
        assertStringEquals(textAutoComplete.autocomplete(input), expected);
    }

    private void assertStringEquals(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("Expected " + expected + ", actual " + actual);
        }
    }


}
