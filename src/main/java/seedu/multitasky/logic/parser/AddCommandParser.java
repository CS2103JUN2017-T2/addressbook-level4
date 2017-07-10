package seedu.multitasky.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_AT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_BY;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TO;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;

// @@author A0140633R
/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * AddCommand and returns an AddCommand object for execution.
     * throws ParseException if the user input does not conform the expected format.
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_BY, PREFIX_AT, PREFIX_FROM,
                                                                  PREFIX_TO, PREFIX_TAG);
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        // check for no args input
        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        // check for empty name field
        if (argMultimap.getPreamble().get().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        if (hasInvalidFlagCombination(argMultimap)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        if (isFloatingTask(argMultimap)) {
            try {
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                ReadOnlyEntry entry = new FloatingTask(name, tagList);
                return new AddCommand(entry);

            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else if (isEvent(argMultimap)) {
            try {
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();

                Prefix datePrefix = ParserUtil.getDatePrefix(argMultimap, PREFIX_AT,
                                                             PREFIX_BY, PREFIX_FROM);
                Date firstDate = DateUtil.stringToDate(argMultimap.getValue(datePrefix).get());
                requireNonNull(firstDate);
                startDate.setTime(firstDate);
                Date secondDate = DateUtil.stringToDate(argMultimap.getValue(PREFIX_TO).get());
                requireNonNull(secondDate);
                endDate.setTime(secondDate);
                System.out.println(startDate.getTime());
                System.out.println(endDate.getTime());

                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                ReadOnlyEntry entry = new FloatingTask(name, tagList);
                return new AddCommand(entry);

            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else if (isDeadline(argMultimap)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                   AddCommand.MESSAGE_USAGE));

        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                   AddCommand.MESSAGE_USAGE));
        }
    }

    /*
     * A method that returns true if flags are given in an illogical manner.
     */
    private boolean hasInvalidFlagCombination(ArgumentMultimap argMultimap) {
        return ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FROM, PREFIX_AT)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FROM, PREFIX_BY)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_BY, PREFIX_AT);
    }

    /*
     * Returns true if flags present in argMultimap indicate to add a floating task entry.
     */
    private boolean isFloatingTask(ArgumentMultimap argMultimap) {
        return !ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY, PREFIX_AT, PREFIX_FROM, PREFIX_TO);
    }

    /*
     * Returns true if flags present in argMultimap indicate to add an event entry.
     * MUST have ONE of /by, /at, /from AND /to
     */
    private boolean isEvent(ArgumentMultimap argMultimap) {
        return ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_TO)
               && ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY, PREFIX_AT, PREFIX_FROM);
    }

    /*
     * Returns true if flags present in argMultimap indicate to add a deadline entry.
     * Either
     */
    private boolean isDeadline(ArgumentMultimap argMultimap) {
        return ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_BY)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_AT)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FROM)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_TO);
    }

}
