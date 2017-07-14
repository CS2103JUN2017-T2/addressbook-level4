package seedu.multitasky.logic.parser;

import static org.junit.Assert.assertTrue;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.logic.commands.DeleteByFindCommand;
import seedu.multitasky.logic.commands.DeleteByIndexCommand;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;


/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs " float 1" and " float 1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private DeleteCommandParser parser = new DeleteCommandParser();

    //@A0140633R
    @Test
    public void parse_emptyArgs_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        parser.parse("");
    }

    @Test
    public void parse_validIndexArgs_returnsDeleteByIndexCommand() throws Exception {
        DeleteCommand command = parser.parse(" float 1");
        assertTrue(command instanceof DeleteByIndexCommand);

        command = parser.parse(" deadline 2");
        assertTrue(command instanceof DeleteByIndexCommand);

        command = parser.parse(" event 3");
        assertTrue(command instanceof DeleteByIndexCommand);
    }

    @Test
    public void parse_validFindArgs_returnsDeleteByFindCommand() throws Exception {
        DeleteCommand command = parser.parse("keywords to search with");
        assertTrue(command instanceof DeleteByFindCommand);

        // accidently adding invalid tags will default to performing a find with those tags
        command = parser.parse(" archive "); // prefix used for list command
        assertTrue(command instanceof DeleteByFindCommand);

        // parser should be able to parse strings with escaped terms.
        command = parser.parse("keyword but with \\escaped \\terms");
        assertTrue(command instanceof DeleteByFindCommand);
    }

}
