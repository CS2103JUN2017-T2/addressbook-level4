package seedu.multitasky.ui.util;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.logic.commands.util.CommandUtil;

// @@author A0125586X
public class CommandAutocompleteTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TextAutocomplete commandAutoComplete;

    @Before
    public void setUp() {
        commandAutoComplete = new CommandAutocomplete(CommandUtil.COMMAND_WORDS, CommandUtil.COMMAND_KEYWORDS,
                                                      CommandUtil.PREFIX_ONLY_COMMANDS);
    }

    @Test
    public void commandAutocomplete_nullCommandWords_exception() {
        thrown.expect(AssertionError.class);
        new CommandAutocomplete(null, CommandUtil.COMMAND_KEYWORDS, CommandUtil.PREFIX_ONLY_COMMANDS);
    }

    @Test
    public void commandAutocomplete_nullCommandKeywords_exception() {
        thrown.expect(AssertionError.class);
        new CommandAutocomplete(CommandUtil.COMMAND_WORDS, null, CommandUtil.PREFIX_ONLY_COMMANDS);
    }

    @Test
    public void commandAutocomplete_nullPrefixOnlyCommands_exception() {
        thrown.expect(AssertionError.class);
        new CommandAutocomplete(CommandUtil.COMMAND_WORDS, CommandUtil.COMMAND_KEYWORDS, null);
    }

    @Test
    public void commandAutocomplete_invalidCommandAndKeywords_noChange() {
        assertStringEquals(commandAutoComplete.autocomplete("arstarsta tsrats"), "arstarsta tsrats");
    }

    @Test
    public void commandKeywordAutocomplete_validCommands_complete() {
        assertAutocomplete(commandAutoComplete, "", "");
        assertAutocomplete(commandAutoComplete, "d", "delete ");
        assertAutocomplete(commandAutoComplete, "a float ta", "add float tag ");
        assertAutocomplete(commandAutoComplete, "l u f", "list upcoming from ");
    }

    @Test
    public void commandAutocomplete_getPossibilities_correct() {
        assertStringEquals(commandAutoComplete.getPossibilities("e"), "e:  clear        complete        delete        "
                                                                    + "edit        exit        help        "
                                                                    + "open        redo        restore        "
                                                                    + "save        ");
        assertStringEquals(commandAutoComplete.getPossibilities("its"), "its:  history        list        ");
        assertTrue(commandAutoComplete.getPossibilities("list") == null);
        assertTrue(commandAutoComplete.getPossibilities("vy") == null);
    }

    private void assertAutocomplete(TextAutocomplete textAutoComplete, String input, String expected) {
        assertStringEquals(textAutoComplete.autocomplete(input), expected);
    }

    private void assertStringEquals(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("Expected <" + expected + ">, actual <" + actual + ">");
        }
    }


}
