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
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;

// @@author A0140633R
/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser {
    ArgumentMultimap argMultimap;

    /**
     * Parses the given {@code String} of arguments in the context of the
     * AddCommand and returns an AddCommand object for execution.
     * throws ParseException if the user input does not conform the expected format.
     */
    public AddCommand parse(String args) throws ParseException {
        argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_BY, PREFIX_AT, PREFIX_FROM,
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

        if (isFloatingTask()) {
            try {
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                ReadOnlyEntry entry = new FloatingTask(name, tagList);
                return new AddCommand(entry);

            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else if (isDeadline()) {
            try {
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();

                // only PREFIX_BY to indicate deadline.
                Prefix datePrefix = PREFIX_BY;
                endDate = DateUtil.stringToCalendar(argMultimap.getValue(datePrefix).get(),
                                                    null);
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                ReadOnlyEntry entry = new Deadline(name, endDate, tagList);

                return new AddCommand(entry);

            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else if (isEvent()) {
            try {
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
                Prefix startDatePrefix = ParserUtil.getDatePrefix(argMultimap, PREFIX_FROM, PREFIX_AT);
                Prefix endDatePrefix = ParserUtil.getDatePrefix(argMultimap, PREFIX_TO, PREFIX_BY);

                endDate = DateUtil.stringToCalendar(argMultimap.getValue(endDatePrefix).get(),
                                                    null);
                startDate = DateUtil.stringToCalendar(argMultimap.getValue(startDatePrefix).get(), endDate);
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                ReadOnlyEntry entry = new Event(name, startDate, endDate, tagList);

                return new AddCommand(entry);

            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else { // not event, not deadline, not floating task
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                   AddCommand.MESSAGE_USAGE));
        }
    }

    /**
     * A method that returns true if flags are given in an illogical manner for add commands.
     */
    private boolean hasInvalidFlagCombination(ArgumentMultimap argMultimap) {
        return ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FROM, PREFIX_AT)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_TO, PREFIX_BY);
    }

    /**
     * Returns true if flags present in argMultimap indicate to add a floating task entry.
     */
    private boolean isFloatingTask() {
        assert argMultimap != null;
        return !ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY, PREFIX_AT, PREFIX_FROM, PREFIX_TO);
    }

    /**
     * Returns true if flags present in argMultimap indicate to add an event entry.
     * MUST have ONE of /from or /at AND ONE of /by or /to, but should not have both tgt.
     */
    private boolean isEvent() {
        assert argMultimap != null;
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FROM, PREFIX_AT)
               && ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY, PREFIX_TO);
    }

    /**
     * Returns true if flags present in argMultimap indicate to add a deadline entry.
     * MUST have /by ONLY
     */
    private boolean isDeadline() {
        assert argMultimap != null;
        return ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_BY)
               && (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_AT, PREFIX_FROM, PREFIX_TO));
    }

}