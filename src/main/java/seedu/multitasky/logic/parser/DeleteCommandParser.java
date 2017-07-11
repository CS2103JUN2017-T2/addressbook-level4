package seedu.multitasky.logic.parser;

import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.DeleteByFindCommand;
import seedu.multitasky.logic.commands.DeleteByIndexCommand;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser {
    ArgumentMultimap argMultimap;

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand and returns an
     * DeleteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    // @@author A0140633R
    public DeleteCommand parse(String args) throws ParseException {
        argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FLOATINGTASK, PREFIX_DEADLINE,
                                                 PREFIX_EVENT, PREFIX_TAG);

        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                   DeleteCommand.MESSAGE_USAGE));
        }

        if (hasIndexFlag(argMultimap)) { // process to delete by indexes
            if (hasInvalidFlagCombination(argMultimap)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                       DeleteCommand.MESSAGE_USAGE));
            }

            try {
                Prefix listIndicatorPrefix = ParserUtil.getDatePrefix(argMultimap, PREFIX_FLOATINGTASK,
                                                                      PREFIX_DEADLINE, PREFIX_EVENT);
                Index index = ParserUtil.parseIndex(argMultimap.getValue(listIndicatorPrefix).get());
                return new DeleteByIndexCommand(index, listIndicatorPrefix);
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else { // process to delete by find.
            String trimmedArgs = argMultimap.getPreamble().get();

            final String[] keywords = trimmedArgs.split("\\s+");
            final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));

            return new DeleteByFindCommand(keywordSet);
        }
    }

    /**
     * A method that returns true if flags are given in an illogical manner for deleting commands.
     * illogical := any 2 of /float, /deadline, /event used together.
     */
    private boolean hasInvalidFlagCombination(ArgumentMultimap argMultimap) {
        assert argMultimap != null;
        return ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_DEADLINE)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_DEADLINE, PREFIX_EVENT)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_EVENT);
    }

    /**
     * A method that returns true if flags in given ArgumentMultimap has at least one index-indicating
     * Prefix mapped to some arguments.
     * Index-indicating := /float or /deadline or /event
     */
    private boolean hasIndexFlag(ArgumentMultimap argMultimap) {
        assert argMultimap != null;
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_DEADLINE,
                                             PREFIX_EVENT);
    }

}
