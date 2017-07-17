package seedu.multitasky.logic.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;

// @@author A0125586X
/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser {

    /** Parses the given arguments in the context of a list command.
     *
     * @param args the arguments for the list command in a single String
     * @return     the ListCommand object for execution
     * @throws ParseException if the user input does not confirm to the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            return new ListCommand();
        }
        // Preamble is necessary due to how ArgumentTokenizer works
        // TODO modify ArgumentTokenizer to be able to detect without preamble
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize("preamble " + trimmedArgs,
                                                                ParserUtil.toPrefixArray(ListCommand.VALID_PREFIXES));
        ArrayList<String> prefixesPresent = argumentMultimap.getPresentPrefixes(ListCommand.VALID_PREFIXES);
        if (!hasValidPrefixCombination(prefixesPresent)) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                                   ListCommand.MESSAGE_USAGE));
        }
        Calendar startDate = getStartDate(argumentMultimap);
        Calendar endDate = getEndDate(argumentMultimap);

        return new ListCommand(startDate, endDate, prefixesPresent);
    }

    private Calendar getStartDate(ArgumentMultimap argumentMultimap) {
        return getDate(argumentMultimap.getValue(CliSyntax.PREFIX_FROM));
    }

    private Calendar getEndDate(ArgumentMultimap argumentMultimap) {
        return getDate(argumentMultimap.getValue(CliSyntax.PREFIX_TO));
    }

    private Calendar getDate(Optional<String> rawDate) {
        try {
            Calendar date = ParserUtil.parseDate(rawDate).get();
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean hasValidPrefixCombination(ArrayList<String> prefixes) {
        // Cannot have any unknown prefixes
        if (!Arrays.asList(ListCommand.VALID_PREFIXES).containsAll(prefixes)) {
            return false;
        }
        // Check for invalid flag combinations
        if (prefixes.contains(CliSyntax.PREFIX_ARCHIVE.toString())
            && prefixes.contains(CliSyntax.PREFIX_BIN.toString())) {
            return false;
        } else if (prefixes.contains(CliSyntax.PREFIX_UPCOMING.toString())
                   && prefixes.contains(CliSyntax.PREFIX_REVERSE.toString())) {
            return false;
        }
        return true;
    }

}
