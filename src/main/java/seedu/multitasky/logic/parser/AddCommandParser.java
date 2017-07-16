package seedu.multitasky.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_AT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_BY;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_ON;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TO;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ArgumentMultimap argMultimap;

    /**
     * Parses the given {@code String} of arguments in the context of the
     * AddCommand and returns an AddCommand object for execution.
     * throws ParseException if the user input does not conform the expected format.
     */
    public AddCommand parse(String args) throws ParseException {
        argMultimap = ArgumentTokenizer.tokenize(args, ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES));
        Calendar startDate = null;
        Calendar endDate = null;

        // check for no args input
        if (args.trim().isEmpty()) {
            throw new ParseException(AddCommand.MESSAGE_USAGE);
        }

        // TODO check whether need this or not
        // check for empty name field
        if (argMultimap.getPreamble().get().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        // TODO check whether i need this or not. not really apt anymore for smart command parsing
        ArrayList<String> prefixesPresent = argMultimap.getPresentPrefixes(AddCommand.VALID_PREFIXES);
        if (!hasValidPrefixCombination(prefixesPresent)) {
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
                endDate = ParserUtil.parseDate(argMultimap.getValue(datePrefix).get());
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                ReadOnlyEntry entry = new Deadline(name, endDate, tagList);

                return new AddCommand(entry);
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else if (isEventStartEndVariant()) {
            try {
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
                // only reads using flag indicated by the last occurrence of prefix.
                Prefix startDatePrefix = requireNonNull(ParserUtil.getLastPrefix(
                        args, PREFIX_FROM, PREFIX_ON, PREFIX_AT));
                Prefix endDatePrefix = requireNonNull(ParserUtil.getLastPrefix(args, PREFIX_TO, PREFIX_BY));
                endDate = ParserUtil.parseDate(argMultimap.getValue(endDatePrefix).get());
                startDate = ParserUtil.parseDate(argMultimap.getValue(startDatePrefix).get());
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                if (endDate.compareTo(startDate) < 0) {
                    throw new ParseException("End date should not be before start date!");
                } else if (endDate.compareTo(startDate) == 0) {
                    // convert automatically to deadline
                    ReadOnlyEntry entry = new Deadline(name, endDate, tagList);
                    return new AddCommand(entry);
                } else { // end date is later than start date as it should be
                    ReadOnlyEntry entry = new Event(name, startDate, endDate, tagList);
                    return new AddCommand(entry);
                }

            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }
        } else if (isEventStartOnlyVariant()) {
            try {
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
                // only reads using flag indicated by the last occurrence of prefix.
                Prefix startDatePrefix = requireNonNull(
                        ParserUtil.getLastPrefix(args, PREFIX_FROM, PREFIX_ON, PREFIX_AT));
                startDate = ParserUtil.parseDate(argMultimap.getValue(startDatePrefix).get());
                endDate = (Calendar) startDate.clone();
                // TODO implement import from config
                endDate.add(Calendar.HOUR, 1);
                if (endDate.compareTo(startDate) < 0) {
                    throw new ParseException("End date should not be before start date!");
                }
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
     * Returns true if flags present in argMultimap indicate to add a floating task entry.
     * Precondition : argumentMultimap within parser has been properly initialised beforehand.
     */
    private boolean isFloatingTask() {
        assert argMultimap != null;
        return !ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY, PREFIX_AT, PREFIX_ON, PREFIX_FROM, PREFIX_TO);
    }

    /**
     * Returns true if flags present in argMultimap indicate to add an event entry by giving both
     * start and end date.
     * MUST have ONE of /from or /at AND ONE of /by or /to, but should not have both tgt.
     * Precondition : argumentMultimap within parser has been properly initialised beforehand.
     */
    private boolean isEventStartEndVariant() {
        assert argMultimap != null;
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FROM, PREFIX_AT, PREFIX_ON)
               && ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY, PREFIX_TO);
    }

    /**
     * Returns true if flags present in argMultimap indicate to add an event entry by giving both
     * start and end date.
     * MUST have ONE of /from or /at AND ONE of /by or /to, but should not have both tgt.
     * Precondition : argumentMultimap within parser has been properly initialised beforehand.
     */
    private boolean isEventStartOnlyVariant() {
        assert argMultimap != null;
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FROM, PREFIX_AT, PREFIX_ON)
               && !ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY, PREFIX_TO);
    }

    /**
     * Returns true if flags present in argMultimap indicate to add a deadline entry.
     * Precondition : argumentMultimap within parser has been properly initialised beforehand.
     */
    private boolean isDeadline() {
        assert argMultimap != null;
        return ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_BY)
               && (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_AT, PREFIX_ON, PREFIX_FROM, PREFIX_TO));
    }

    /**
     * A method that returns false if flags are given in an illogical manner for add commands.
     */
    private boolean hasValidPrefixCombination(ArrayList<String> prefixes) {
        // Cannot have any unknown prefixes
        if (!Arrays.asList(AddCommand.VALID_PREFIXES).containsAll(prefixes)) {
            return false;
        }
        return true;
    }

}
