package seedu.multitasky.logic.parser;

import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_AT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_BY;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TO;

import java.util.Calendar;
import java.util.Set;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.entry.Entry;
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
        Calendar startDate = null;
        Calendar endDate = null;

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
                ReadOnlyEntry entry = new Entry(name, tagList);
                return new AddCommand(entry);

            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else if (isDeadline(argMultimap)) {
            try {
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();

                // only PREFIX_BY to indicate deadline.
                Prefix datePrefix = PREFIX_BY;
                endDate = DateUtil.stringToCalendar(argMultimap.getValue(datePrefix).get(),
                                                    null);
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                ReadOnlyEntry entry = new Entry(name, tagList);

                return new AddCommand(entry);

            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else if (isEvent(argMultimap)) {
            try {
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
                //TODO clean up method to allow more prefixes entered.
                // assumes only from and at for start date prefix.
                Prefix startDatePrefix = argMultimap.getValue(PREFIX_FROM).isPresent() ? PREFIX_FROM : PREFIX_AT;
                // assumes only by and to for end date prefix.
                Prefix endDatePrefix = argMultimap.getValue(PREFIX_TO).isPresent() ? PREFIX_TO : PREFIX_BY;

                endDate = DateUtil.stringToCalendar(argMultimap.getValue(endDatePrefix).get(),
                                                    null);
                startDate = DateUtil.stringToCalendar(argMultimap.getValue(startDatePrefix).get(), endDate);
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                ReadOnlyEntry entry = new Entry(name, tagList);

                return new AddCommand(entry);

            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

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
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_TO, PREFIX_BY);
    }

    //TODO to improve by giving ArgumentMultimap the method instead, use contains()?
    /*
     * Returns true if flags present in argMultimap indicate to add a floating task entry.
     */
    private boolean isFloatingTask(ArgumentMultimap argMultimap) {
        return !ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY, PREFIX_AT, PREFIX_FROM, PREFIX_TO);
    }

    /*
     * Returns true if flags present in argMultimap indicate to add an event entry.
     * MUST have ONE of /from or /at AND ONE of /by or /to, but should not have both tgt.
     */
    private boolean isEvent(ArgumentMultimap argMultimap) {
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FROM, PREFIX_AT)
               && ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY, PREFIX_TO);
    }

    /*
     * Returns true if flags present in argMultimap indicate to add a deadline entry.
     * MUST have /by ONLY
     */
    private boolean isDeadline(ArgumentMultimap argMultimap) {
        return ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_BY)
               && (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_AT, PREFIX_FROM, PREFIX_TO));
    }

}
