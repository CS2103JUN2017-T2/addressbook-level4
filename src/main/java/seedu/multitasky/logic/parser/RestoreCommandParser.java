package seedu.multitasky.logic.parser;

import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.RestoreByFindCommand;
import seedu.multitasky.logic.commands.RestoreByIndexCommand;
import seedu.multitasky.logic.commands.RestoreCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;

// @@author A0126623L-reused
/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class RestoreCommandParser {
    private ArgumentMultimap argMultimap;

    public ArgumentMultimap getArgMultimap() {
        return argMultimap;
    }

    // @@author A0126623L-reused
    /**
     * Parses the given {@code String} of arguments in the context of the RestoreCommand and returns a
     * RestoreCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public RestoreCommand parse(String args) throws ParseException {
        argMultimap = ArgumentTokenizer.tokenize(args,
                                                 ParserUtil.toPrefixArray(RestoreCommand.VALID_PREFIXES));

        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                   RestoreCommand.MESSAGE_USAGE));
        }

        if (hasIndexFlag(argMultimap)) { // process to restore by indexes
            if (!ParserUtil.hasValidListTypeCombination(argMultimap)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                       RestoreCommand.MESSAGE_USAGE));
            }

            try {
                Prefix listIndicatorPrefix = ParserUtil.getMainPrefix(argMultimap, PREFIX_FLOATINGTASK,
                                                                      PREFIX_DEADLINE, PREFIX_EVENT);
                Index index = ParserUtil.parseIndex(argMultimap.getValue(listIndicatorPrefix).get());
                return new RestoreByIndexCommand(index, listIndicatorPrefix);
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else { // process to restore by find.
            String searchString = argMultimap.getPreamble().get()
                                             .replaceAll("\\" + CliSyntax.PREFIX_ESCAPE, "");
            final String[] keywords = searchString.split("\\s+");
            final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));

            return new RestoreByFindCommand(keywordSet);
        }
    }
    // @@author
    // @@author A0126623L-reused
    /**
     * A method that returns true if flags in given ArgumentMultimap has at least one index-indicating
     * Prefix mapped to some arguments.
     * Index-indicating := /float or /deadline or /event
     */
    private boolean hasIndexFlag(ArgumentMultimap argMultimap) {
        Objects.requireNonNull(argMultimap);
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_DEADLINE,
                                             PREFIX_EVENT);
    }
    // @@author

}
