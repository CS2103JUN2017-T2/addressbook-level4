package seedu.multitasky.logic.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.FindCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;

// @@author A0125586X
/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * FindCommand and returns an FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                                   FindCommand.MESSAGE_USAGE));
        }
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(trimmedArgs,
                                                                ParserUtil.toPrefixArray(FindCommand.VALID_PREFIXES));
        ArrayList<String> prefixesPresent = argumentMultimap.getPresentPrefixes(FindCommand.VALID_PREFIXES);
        if (!hasValidPrefixCombination(prefixesPresent)) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                                   FindCommand.MESSAGE_USAGE));
        }
        Set<String> keywords;
        Optional<String> optKeywords = argumentMultimap.getPreamble();
        if (optKeywords.isPresent()) {
            keywords = new HashSet<>(Arrays.asList(optKeywords.get().split("\\s+")));
        } else {
            keywords = new HashSet<>();
        }
        Calendar startDate = getStartDate(argumentMultimap);
        Calendar endDate = getEndDate(argumentMultimap);

        return new FindCommand(keywords, startDate, endDate, prefixesPresent);
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
        } catch (IllegalValueException e) {
            return null;
        }
    }

    private boolean hasValidPrefixCombination(ArrayList<String> prefixes) {
        // Cannot have any unknown prefixes
        if (!Arrays.asList(FindCommand.VALID_PREFIXES).containsAll(prefixes)) {
            return false;
        }
        // Check for invalid flag combinations
        if (prefixes.contains(CliSyntax.PREFIX_ARCHIVE.toString())
            && prefixes.contains(CliSyntax.PREFIX_BIN.toString())) {
            return false;
        }
        return true;
    }

    private String getString(ArrayList<String> parts) {
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            builder.append(part);
        }
        return builder.toString();
    }

}
