package seedu.multitasky.logic.parser;

import static org.junit.Assert.assertTrue;
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
import seedu.multitasky.model.LogicUserPrefs;
import seedu.multitasky.model.UserPrefs;


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
    private static final String VALID_DATE_TIME_25JULY6PM = "25 july 6pm";
    private static final String VALID_TIME_9PM = "9pm";
    private static final String INVALID_DATE = "end of time";

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private final LogicUserPrefs userprefs = new UserPrefs();
    private final AddCommandParser parser = new AddCommandParser();

    //@A0140633R
    @Test
    public void parse_emptyArgs_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(AddCommand.MESSAGE_USAGE));
        parser.parse("", userprefs);
    }

    @Test
    public void parse_validArgs_returnsAddCommand() throws Exception {
        // floating task
        Command command = parser.parse(" a typical task name", userprefs);
        assertTrue(command instanceof AddCommand);
        command = parser.parse(" a special task with \\by in its name", userprefs);
        assertTrue(command instanceof AddCommand);

        // deadline
        command = parser.parse(" a typical task with enddate " + PREFIX_BY
                               + " " + VALID_DATE_17JULY, userprefs);
        assertTrue(command instanceof AddCommand);
        command = parser.parse(" a special task with enddate and \\by in name "
                               + PREFIX_BY + " " + VALID_DATE_17JULY, userprefs);
        assertTrue(command instanceof AddCommand);

        // event
        command = parser.parse(" a typical task with both start and end date " + PREFIX_FROM + " "
                               + VALID_DATE_17JULY + " " + PREFIX_TO + " " + VALID_DATE_20JULY, userprefs);
        assertTrue(command instanceof AddCommand);
        command = parser.parse(" a special task with many start date and end date " + PREFIX_FROM + " "
                + VALID_DATE_17JULY + " " + PREFIX_AT + " " + VALID_DATE_17JULY
                + PREFIX_TO + " " + VALID_DATE_20JULY, userprefs);
        assertTrue(command instanceof AddCommand);

        // start date == end date
        command = parser.parse("a special full day task " + PREFIX_FROM + " " + VALID_DATE_17JULY
                               + " " + PREFIX_TO + " " + VALID_DATE_17JULY, userprefs);
        assertTrue(command instanceof AddCommand);

        // only start date variant event
        command = parser.parse(" a special task with only start date " + PREFIX_FROM
                               + " " + VALID_DATE_17JULY, userprefs);
        assertTrue(command instanceof AddCommand);
        command = parser.parse(" a special task with only start date " + PREFIX_AT
                               + " " + VALID_DATE_20JULY, userprefs);
        assertTrue(command instanceof AddCommand);

        // only end date variant event
        command = parser.parse("task with only \\to flag " + PREFIX_TO + " " + VALID_DATE_17JULY, userprefs);
        assertTrue(command instanceof AddCommand);
        // only end date variant event
        command = parser.parse(" a special event with only end date " + PREFIX_TO + " " + VALID_DATE_17JULY, userprefs);
        assertTrue(command instanceof AddCommand);

        // both start and end date given but end date only has time fields
        command = parser.parse(" an event with end date infered to be same date as start date "
                               + PREFIX_FROM + " " + VALID_DATE_TIME_25JULY6PM + " " + PREFIX_TO
                               + " " + VALID_TIME_9PM, userprefs);
        assertTrue(command instanceof AddCommand);
    }

    public void parse_invalidArgsFollowedByValidArgs_returnsAddCommand() throws Exception {
        Command command = parser.parse(" a special task with many start date and end date " + PREFIX_FROM + " "
                + INVALID_DATE + " " + PREFIX_AT + " " + VALID_DATE_17JULY + PREFIX_TO
                + " " + VALID_DATE_20JULY, userprefs);
        assertTrue(command instanceof AddCommand);
    }

    @Test
    public void parse_invalidDate_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(ParserUtil.MESSAGE_FAIL_PARSE_DATE, INVALID_DATE));
        parser.parse("task with invalid date " + PREFIX_BY + " " + INVALID_DATE, userprefs);
    }

    @Test
    public void parse_eventEndDateBeforeStartDate_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(AddCommand.MESSAGE_ENDDATE_BEFORE_STARTDATE);
        parser.parse("task with enddate before startdate " + PREFIX_ON + " " + VALID_DATE_20JULY
                     + " " + PREFIX_TO + " " + VALID_DATE_17JULY, userprefs);
    }

}
