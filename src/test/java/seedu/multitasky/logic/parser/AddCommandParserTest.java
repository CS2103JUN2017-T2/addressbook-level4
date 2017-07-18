package seedu.multitasky.logic.parser;

import static org.junit.Assert.assertTrue;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_AT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_BY;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_ON;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TO;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.Command;
import seedu.multitasky.logic.parser.exceptions.ParseException;


/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the AddCommand code. For example, inputs " float 1" and " float 1 abc" take the
 * same path through the AddCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class AddCommandParserTest {
    private static final String VALID_DATE_17JULY = "17 july 2017";
    private static final String VALID_DATE_20JULY = "20 july 2017";
    private static final String INVALID_DATE = "end of time";


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AddCommandParser parser = new AddCommandParser();

    //@A0140633R
    @Test
    public void parse_emptyArgs_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(AddCommand.MESSAGE_USAGE));
        parser.parse("");
    }

    @Test
    public void parse_validArgs_returnsAddCommand() throws Exception {
        // floating task
        Command command = parser.parse(" a typical task name");
        assertTrue(command instanceof AddCommand);
        command = parser.parse(" a special task with \\by in its name");
        assertTrue(command instanceof AddCommand);

        // deadline
        command = parser.parse(" a typical task with enddate " + PREFIX_BY
                               + " " + VALID_DATE_17JULY);
        assertTrue(command instanceof AddCommand);
        command = parser.parse(" a special task with enddate and \\by in name "
                               + PREFIX_BY + " " + VALID_DATE_17JULY);
        assertTrue(command instanceof AddCommand);

        // event
        command = parser.parse(" a typical task with both start and end date " + PREFIX_FROM + " "
                               + VALID_DATE_17JULY + " " + PREFIX_TO + " " + VALID_DATE_20JULY);
        assertTrue(command instanceof AddCommand);
        command = parser.parse(" a special task with many start date and end date " + PREFIX_FROM + " "
                + VALID_DATE_17JULY + " " + PREFIX_AT + " " + VALID_DATE_17JULY + PREFIX_TO + " " + VALID_DATE_20JULY);
        assertTrue(command instanceof AddCommand);
        // only start date variant event
        command = parser.parse(" a special task with only start date " + PREFIX_FROM + " " + VALID_DATE_17JULY);
        assertTrue(command instanceof AddCommand);
        command = parser.parse(" a special task with only start date " + PREFIX_AT + " " + VALID_DATE_20JULY);
        assertTrue(command instanceof AddCommand);
        // only end date variant event
        command = parser.parse(" a special event with only end date " + PREFIX_TO + " " + VALID_DATE_17JULY);
        assertTrue(command instanceof AddCommand);
    }

    public void parse_invalidArgsFollowedByValidArgs_returnsAddCommand() throws Exception {
        Command command = parser.parse(" a special task with many start date and end date " + PREFIX_FROM + " "
                + INVALID_DATE + " " + PREFIX_AT + " " + VALID_DATE_17JULY + PREFIX_TO
                + " " + VALID_DATE_20JULY);
        assertTrue(command instanceof AddCommand);
    }

    @Test
    public void parse_invalidDate_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(ParserUtil.MESSAGE_FAIL_PARSE_DATE, INVALID_DATE));
        parser.parse("task with invalid date " + PREFIX_BY + " " + INVALID_DATE);
    }

    @Test
    public void parse_invalidFlagCombination_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        parser.parse("task with only \\to flag " + PREFIX_TO + " " + VALID_DATE_17JULY);
    }

    @Test
    public void parse_eventEndDateBeforeStartDate_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(AddCommand.MESSAGE_ENDDATE_BEFORE_STARTDATE);
        parser.parse("task with enddate before startdate " + PREFIX_ON + " " + VALID_DATE_20JULY
                     + " " + PREFIX_TO + " " + VALID_DATE_17JULY);
    }

}
