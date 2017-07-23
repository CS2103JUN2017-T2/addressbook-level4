package seedu.multitasky.ui.util;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.util.CommandUtil;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.logic.parser.Prefix;

// @@author A0125586X
public class CommandKeywordAutocompleteTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TextAutocomplete commandKeywordAutoComplete;

    private Map<String, String[]> commandKeywords;

    @Before
    public void setUp() {
        commandKeywords = CommandUtil.COMMAND_KEYWORDS;
    }

    @Test
    public void commandKeywordAutocomplete_nullArgument_exception() {
        thrown.expect(AssertionError.class);
        commandKeywordAutoComplete = new CommandKeywordAutocomplete(null);
    }

    @Test
    public void commandKeywordAutocomplete_invalidCommandKeywords_noChange() {
        commandKeywordAutoComplete = new CommandKeywordAutocomplete(commandKeywords.get(AddCommand.COMMAND_WORD));
        assertStringEquals(commandKeywordAutoComplete.autocomplete("arstarsta"), "arstarsta");
    }

    @Test
    public void commandKeywordAutocomplete_validCommandKeywords_complete() {
        commandKeywordAutoComplete = new CommandKeywordAutocomplete(commandKeywords.get(AddCommand.COMMAND_WORD));
        assertAutocomplete(commandKeywordAutoComplete, "f", CliSyntax.PREFIX_FROM);
        assertAutocomplete(commandKeywordAutoComplete, "b", CliSyntax.PREFIX_BY);
        assertAutocomplete(commandKeywordAutoComplete, "a", CliSyntax.PREFIX_AT);
        assertAutocomplete(commandKeywordAutoComplete, "o", CliSyntax.PREFIX_ON);
        assertAutocomplete(commandKeywordAutoComplete, "to", CliSyntax.PREFIX_TO);
        assertAutocomplete(commandKeywordAutoComplete, "g", CliSyntax.PREFIX_TAG);
    }

    @Test
    public void commandKeywordAutocomplete_getPossibilities_null() {
        commandKeywordAutoComplete = new CommandKeywordAutocomplete(commandKeywords.get(AddCommand.COMMAND_WORD));
        assertTrue(commandKeywordAutoComplete.getPossibilities("") == null);
    }

    private void assertAutocomplete(TextAutocomplete textAutoComplete, String input, Prefix expected) {
        assertStringEquals(textAutoComplete.autocomplete(input), expected.toString());
    }

    private void assertStringEquals(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("Expected " + expected + ", actual " + actual);
        }
    }


}
