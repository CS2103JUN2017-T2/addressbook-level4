package seedu.multitasky.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_AT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_BY;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_ON;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.LogicUserPrefs;
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
    public AddCommand parse(String args, LogicUserPrefs userprefs) throws ParseException {
        argMultimap = ArgumentTokenizer.tokenize(args, ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES));
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

        if (isFloatingTask()) {
            try {
                // parse entry details
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
                if (requiresSmartParsing(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES))) {
                    name = new Name(doSmartParsingPreamble(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES)));
                }
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

                ReadOnlyEntry entry = new FloatingTask(name, tagList);
                return new AddCommand(entry);

            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else if (isDeadline()) {
            try {
                // parse all entry details
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
                if (requiresSmartParsing(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES))) {
                    name = new Name(doSmartParsingPreamble(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES)));
                }
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
                // parse entry details
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
                if (requiresSmartParsing(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES))) {
                    name = new Name(doSmartParsingPreamble(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES)));
                }
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                // only reads using flag indicated by the last occurrence of prefix.
                Prefix startDatePrefix = requireNonNull(ParserUtil.getLastPrefix(
                        args, PREFIX_FROM, PREFIX_ON, PREFIX_AT));
                Prefix endDatePrefix = requireNonNull(ParserUtil.getLastPrefix(args, PREFIX_TO, PREFIX_BY));
                startDate = ParserUtil.parseDate(argMultimap.getValue(startDatePrefix).get());
                endDate = ParserUtil.parseExtendedDate(argMultimap.getValue(startDatePrefix).get(),
                                                       argMultimap.getValue(endDatePrefix).get());

                // check for special cases of date
                endDate.set(Calendar.SECOND, 0);
                endDate.set(Calendar.MILLISECOND, 0);
                startDate.set(Calendar.SECOND, 0);
                startDate.set(Calendar.MILLISECOND, 0);
                if (endDate.compareTo(startDate) < 0) {
                    throw new ParseException(AddCommand.MESSAGE_ENDDATE_BEFORE_STARTDATE);
                } else if (endDate.compareTo(startDate) == 0) {
                    // convert automatically to full day event
                    startDate.set(Calendar.HOUR_OF_DAY, 0);
                    startDate.set(Calendar.MINUTE, 0);
                    endDate.set(Calendar.HOUR_OF_DAY, 23);
                    endDate.set(Calendar.MINUTE, 59);
                    ReadOnlyEntry entry = new Event(name, startDate, endDate, tagList);

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
                if (requiresSmartParsing(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES))) {
                    name = new Name(doSmartParsingPreamble(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES)));
                }
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                // only reads using flag indicated by the last occurrence of prefix.
                Prefix startDatePrefix = requireNonNull(
                        ParserUtil.getLastPrefix(args, PREFIX_FROM, PREFIX_ON, PREFIX_AT));
                startDate = ParserUtil.parseDate(argMultimap.getValue(startDatePrefix).get());
                endDate = (Calendar) startDate.clone();

                // get default duration from preferences.json file
                int addDurationHour = userprefs.getDurationHour();
                if (addDurationHour <= 0) {
                    throw new ParseException(AddCommand.MESSAGE_INVALID_CONFIG_DURATION);
                }
                endDate.add(Calendar.HOUR_OF_DAY, addDurationHour);
                ReadOnlyEntry entry = new Event(name, startDate, endDate, tagList);

                return new AddCommand(entry);
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }
        } else if (isEventEndOnlyVariant()) {
            try {
                Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
                if (requiresSmartParsing(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES))) {
                    name =
                         new Name(doSmartParsingPreamble(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES)));
                }
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                // this variant only accepts PREFIX_TO
                Prefix endDatePrefix = PREFIX_TO;
                endDate = ParserUtil.parseDate(argMultimap.getValue(endDatePrefix).get());
                startDate = (Calendar) endDate.clone();

                // get default duration from preferences.json file
                int negAddDurationHour = 0 - userprefs.getDurationHour();
                if (negAddDurationHour >= 0) {
                    throw new ParseException(AddCommand.MESSAGE_INVALID_CONFIG_DURATION);
                }
                startDate.add(Calendar.HOUR_OF_DAY, negAddDurationHour);
                if (endDate.compareTo(startDate) < 0) {
                    throw new ParseException(AddCommand.MESSAGE_ENDDATE_BEFORE_STARTDATE);
                }
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
     * Returns true if flags present in argMultimap indicate to add an event entry by giving only
     * end date.
     * MUST have /to only
     * Precondition : argumentMultimap within parser has been properly initialised beforehand.
     */
    private boolean isEventEndOnlyVariant() {
        assert argMultimap != null;
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_TO)
               && !ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY, PREFIX_FROM, PREFIX_AT, PREFIX_ON);
    }

    /**
     * Returns true if flags present in argMultimap indicate to add a deadline entry.
     * Precondition : argumentMultimap within parser has been properly initialised beforehand.
     */
    private boolean isDeadline() {
        assert argMultimap != null;
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY)
               && (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_AT, PREFIX_ON, PREFIX_FROM, PREFIX_TO));
    }

    private boolean requiresSmartParsing(Prefix...prefixes)  {
        assert argMultimap != null;
        for (Prefix prefix : prefixes) {
            if (argMultimap.getAllValues(prefix).size() > 1) {
                return true;
            }
        }
        return false;
    }

    private String doSmartParsingPreamble(Prefix...prefixes) {
        assert argMultimap != null;
        PrettyTimeParser ptp = new PrettyTimeParser();
        List<Date> dateList;
        StringBuilder builder = new StringBuilder();
        builder.append(argMultimap.getPreamble().get());
        for (Prefix prefix : prefixes) {
            List<String> argList = argMultimap.getAllValues(prefix);
            if (argList.size() > 1) {
                for (int i = 0; i < argList.size() - 1; i++) {
                    String args = argList.get(i);
                    dateList = ptp.parse(args);
                    if (dateList.size() < 1 && !args.equals("")) {
                        builder.append(" ").append(prefix.toString())
                        .append(" ").append(argList.get(i));
                    }
                }
            }
        }
        return builder.toString();
    }

}
