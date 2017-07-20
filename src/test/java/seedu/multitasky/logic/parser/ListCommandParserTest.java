package seedu.multitasky.logic.parser;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;

public class ListCommandParserTest {

    private static final String VALID_DATE = "20 june 2017 8am";
    private static final String VALID_DATE_2 = "20 jun 10am";
    private static final String INVALID_DATE = "the full moon";
    private static final String INVALID_DATE_2 = "my birthday";

    private static final Calendar VALID_CALENDAR_DATE = new GregorianCalendar(2017, 5, 20, 8, 0);
    private static final Calendar VALID_CALENDAR_DATE_2 = new GregorianCalendar(2017, 5, 20, 10, 0);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ListCommandParser parser;

    @Before
    public void setUp() {
        parser = new ListCommandParser();
    }

    @Test
    public void listCommandParser_nullInput_defaultListCommand() {
        try {
            ListCommand listCommand = parser.parse(null);
            assertTrue(listCommand.equals(new ListCommand()));
        } catch (ParseException e) {
            fail("Test should not throw exceptions");
        }
    }

    @Test
    public void listCommandParser_emptyInput_defaultListCommand() {
        try {
            ListCommand listCommand = parser.parse("");
            assertTrue(listCommand.equals(new ListCommand()));
        } catch (ParseException e) {
            fail("Test should not throw exceptions");
        }
    }

    @Test
    public void listCommandParser_invalidTypeCombination_parseException() throws Exception {
        thrown.expect(ParseException.class);
        parser.parse(CliSyntax.PREFIX_ARCHIVE.toString() + " " + CliSyntax.PREFIX_BIN.toString());
    }

    @Test
    public void listCommandParser_invalidOrderCombination_parseException() throws Exception {
        thrown.expect(ParseException.class);
        parser.parse(CliSyntax.PREFIX_REVERSE.toString() + " " + CliSyntax.PREFIX_UPCOMING.toString());
    }

    @Test
    public void listCommandParser_correctTypeAndOrder_correctlyParsed() {
        try {
            String input = CliSyntax.PREFIX_ARCHIVE.toString();
            ListCommand listCommand = parser.parse(input);
            ListCommand expectedListCommand = new ListCommand(null, null, Arrays.asList(new String[]{input}));
            assertTrue(listCommand.equals(expectedListCommand));

            input = CliSyntax.PREFIX_BIN.toString();
            listCommand = parser.parse(input);
            expectedListCommand = new ListCommand(null, null, Arrays.asList(new String[]{input}));
            assertTrue(listCommand.equals(expectedListCommand));

            input = CliSyntax.PREFIX_ALL.toString();
            listCommand = parser.parse(input);
            expectedListCommand = new ListCommand(null, null, Arrays.asList(new String[]{input}));
            assertTrue(listCommand.equals(expectedListCommand));

            input = CliSyntax.PREFIX_REVERSE.toString();
            listCommand = parser.parse(input);
            expectedListCommand = new ListCommand(null, null, Arrays.asList(new String[]{input}));
            assertTrue(listCommand.equals(expectedListCommand));

            input = CliSyntax.PREFIX_UPCOMING.toString();
            listCommand = parser.parse(input);
            expectedListCommand = new ListCommand(null, null, Arrays.asList(new String[]{input}));
            assertTrue(listCommand.equals(expectedListCommand));
        } catch (ParseException e) {
            fail("Test should not throw exceptions");
        }
    }

    @Test
    public void listCommandParser_datePrefixMissingDate_noDate() throws ParseException {
        String input = CliSyntax.PREFIX_FROM.toString();
        ListCommand listCommand = parser.parse(input);
        ListCommand expectedListCommand = new ListCommand(null, null, new ArrayList<>());
        assertTrue(listCommand.equals(expectedListCommand));

        input = CliSyntax.PREFIX_TO.toString();
        listCommand = parser.parse(input);
        assertTrue(listCommand.equals(expectedListCommand));

        input = CliSyntax.PREFIX_FROM.toString() + CliSyntax.PREFIX_TO.toString();
        listCommand = parser.parse(input);
        assertTrue(listCommand.equals(expectedListCommand));
    }

    @Test
    public void listCommandParser_invalidDate_noDate() throws ParseException {
        String input = CliSyntax.PREFIX_FROM.toString() + " " + INVALID_DATE;
        ListCommand listCommand = parser.parse(input);
        ListCommand expectedListCommand = new ListCommand(null, null, new ArrayList<>());
        assertTrue(listCommand.equals(expectedListCommand));

        input = CliSyntax.PREFIX_TO.toString() + " " + INVALID_DATE_2;
        listCommand = parser.parse(input);
        assertTrue(listCommand.equals(expectedListCommand));

        input = CliSyntax.PREFIX_FROM.toString() + " " + INVALID_DATE + " "
                + CliSyntax.PREFIX_TO.toString() + " " + INVALID_DATE_2;
        listCommand = parser.parse(input);
        assertTrue(listCommand.equals(expectedListCommand));
    }

    @Test
    public void listCommandParser_validDate_parsed() throws ParseException {
        String input = CliSyntax.PREFIX_FROM.toString() + " " + VALID_DATE;
        ListCommand listCommand = parser.parse(input);
        ListCommand expectedListCommand = new ListCommand(VALID_CALENDAR_DATE, null, new ArrayList<>());
        assertTrue(listCommand.equals(expectedListCommand));

        input = CliSyntax.PREFIX_TO.toString() + " " + VALID_DATE_2;
        listCommand = parser.parse(input);
        expectedListCommand = new ListCommand(null, VALID_CALENDAR_DATE_2, new ArrayList<>());
        assertTrue(listCommand.equals(expectedListCommand));

        input = CliSyntax.PREFIX_FROM.toString() + " " + VALID_DATE + " "
                + CliSyntax.PREFIX_TO.toString() + " " + VALID_DATE_2;
        listCommand = parser.parse(input);
        expectedListCommand = new ListCommand(VALID_CALENDAR_DATE, VALID_CALENDAR_DATE_2, new ArrayList<>());
        assertTrue(listCommand.equals(expectedListCommand));
    }

}
