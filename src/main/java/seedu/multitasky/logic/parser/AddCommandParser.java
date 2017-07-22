package seedu.multitasky.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_ARRAY_ENDDATE;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_ARRAY_STARTDATE;
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
import seedu.multitasky.model.entry.Entry;
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

        // show message usage if no args were given with command
        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        if (isFloatingTask()) {
            try {
                Name name = doParseName();
                Set<Tag> tagList = doParseTags();

                Entry entry = new FloatingTask(name, tagList);
                return new AddCommand(entry);
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else if (isDeadline()) {
            try {
                Name name = doParseName();
                Set<Tag> tagList = doParseTags();
                Calendar endDate = doParseDate(args, PREFIX_BY);

                ReadOnlyEntry entry = new Deadline(name, endDate, tagList);
                return new AddCommand(entry);
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else if (isEventStartEndVariant()) {
            try {
                Name name = doParseName();
                Set<Tag> tagList = doParseTags();
                Calendar startDate = doParseDate(args, PREFIX_FROM, PREFIX_ON, PREFIX_AT);
                Calendar endDate = doParseExtendedDate(args, PREFIX_ARRAY_STARTDATE, PREFIX_ARRAY_ENDDATE);

                calibrateCalendarsForComparison(startDate, endDate);
                checkEndDateBeforeStartDate(endDate, startDate);
                if (endDate.compareTo(startDate) == 0) {
                    setToFullDay(startDate, endDate);

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
                Name name = doParseName();
                Set<Tag> tagList = doParseTags();
                Calendar startDate = doParseDate(args, PREFIX_ARRAY_STARTDATE);
                Calendar endDate = setUpEndDateWithDefaults(userprefs, startDate);

                ReadOnlyEntry entry = new Event(name, startDate, endDate, tagList);
                return new AddCommand(entry);
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else if (isEventEndOnlyVariant()) {
            try {
                Name name = doParseName();
                Set<Tag> tagList = doParseTags();
                Calendar endDate = doParseDate(args, PREFIX_TO);
                Calendar startDate = setUpStartDateWithDefaults(userprefs, endDate);
                checkEndDateBeforeStartDate(endDate, startDate);
                ReadOnlyEntry entry = new Event(name, startDate, endDate, tagList);

                return new AddCommand(entry);
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else { // not event, not deadline, not floating task
            throw new AssertionError("entries should only be event, deadline or floatingtask");
        }
    }

    private void checkEndDateBeforeStartDate(Calendar endDate, Calendar startDate) throws ParseException {
        if (endDate.compareTo(startDate) < 0) {
            throw new ParseException(AddCommand.MESSAGE_ENDDATE_BEFORE_STARTDATE);
        }
    }

    /**
     * Method that creates and returns a {@code Calendar} by extending the Date from {@code endDate} with
     * default duration from preferences.json file
     */
    private Calendar setUpStartDateWithDefaults(LogicUserPrefs userprefs, Calendar endDate)
            throws ParseException {
        Calendar startDate = (Calendar) endDate.clone();

        int negAddDurationHour = 0 - userprefs.getDurationHour();
        if (negAddDurationHour >= 0) {
            throw new ParseException(AddCommand.MESSAGE_INVALID_CONFIG_DURATION);
        }
        startDate.add(Calendar.HOUR_OF_DAY, negAddDurationHour);
        return startDate;
    }

    /**
     * Method that creates and returns a {@code Calendar} by extending the Date from {@code startDate} with
     * default duration from preferences.json file
     */
    private Calendar setUpEndDateWithDefaults(LogicUserPrefs userprefs, Calendar startDate)
            throws ParseException {
        Calendar endDate = (Calendar) startDate.clone();

        int addDurationHour = userprefs.getDurationHour();
        if (addDurationHour <= 0) {
            throw new ParseException(AddCommand.MESSAGE_INVALID_CONFIG_DURATION);
        }
        endDate.add(Calendar.HOUR_OF_DAY, addDurationHour);
        return endDate;
    }

    /**
     * method that parses date by using flag indicated by last occurrence of prefix amongst {@code endDatePrefixes},
     * and extended if required by using any missing date fields such as month, date from the Calendar parsed from
     * using the last occurrence of prefix amongst {@code startDatePrefixes}.
     *
     * @throws ParseException if raw date strings are not able to be parsed into Calendar
     */
    private Calendar doParseExtendedDate(String stringToSearch, Prefix[] startDatePrefixes,
                                         Prefix[] endDatePrefixes) throws ParseException {
        assert argMultimap != null;
        Prefix startDatePrefix = ParserUtil.getLastPrefix(stringToSearch, startDatePrefixes);
        Prefix endDatePrefix = ParserUtil.getLastPrefix(stringToSearch, endDatePrefixes);
        return ParserUtil.parseExtendedDate(argMultimap.getValue(startDatePrefix).get(),
                                            argMultimap.getValue(endDatePrefix).get());
    }

    /**
     * method that parses date by using flag indicated by the last occurrence of prefix amongst input {@code Prefix[]}.
     * Precondition: input raw string must contain at least one of Prefix in input Prefix[], {@code ArgumentMultimap}
     * has been initialized beforehand.
     *
     * @throws ParseException if raw date string is not able to be parsed into Calendar
     */
    private Calendar doParseDate(String stringToSearch, Prefix... prefixes) throws ParseException {
        assert argMultimap != null;
        Prefix datePrefix = requireNonNull(ParserUtil.getLastPrefix(stringToSearch, prefixes));
        return ParserUtil.parseDate(argMultimap.getValue(datePrefix).get());
    }

    /**
     * converts input {@code Calendar} HOUR and MINUTE fields to 12am and 11.59pm.
     * @param startDate
     * @param endDate
     */
    private void setToFullDay(Calendar startDate, Calendar endDate) {
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.HOUR_OF_DAY, 23);
        endDate.set(Calendar.MINUTE, 59);
    }

    /**
     * method that sets input {@code Calendar} to be less precise in order to facilitate comparison
     * @param startDate
     * @param endDate
     */
    private void calibrateCalendarsForComparison(Calendar startDate, Calendar endDate) {
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);
    }

    private Set<Tag> doParseTags() throws IllegalValueException {
        return ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
    }

    /**
     * Parses name of entry from {@code ArgumentMultimap} initialized from input args.
     * @throws IllegalValueException if name
     */
    private Name doParseName() throws IllegalValueException {
        assert argMultimap != null;
        Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
        if (hasPrefixesMappedToMultipleValues(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES))) {
            name = new Name(appendExcessArgsToPreambleName(ParserUtil.toPrefixArray(AddCommand.VALID_PREFIXES)));
        }
        return name;
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
     * MUST have to only
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

    /**
     * Method that goes through each Prefix in input {@code Prefix[]} and returns true if {@code ArgumentMultimap}
     * has any key-value pairs that have multiple values per key
     */
    private boolean hasPrefixesMappedToMultipleValues(Prefix...prefixes)  {
        assert argMultimap != null;
        for (Prefix prefix : prefixes) {
            if (argMultimap.getAllValues(prefix).size() > 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that loops through all Prefixes in input {@code Prefix[]} and appends preamble and all the excess
     * prefix + valid arguments contained in {@code ArgumentMultimap} to a {@code StringBuilder}, before returning it.
     */
    private String appendExcessArgsToPreambleName(Prefix...prefixes) {
        assert argMultimap != null;
        StringBuilder builder = new StringBuilder();
        builder.append(argMultimap.getPreamble().get());
        for (Prefix prefix : prefixes) {
            List<String> argList = argMultimap.getAllValues(prefix);
            if (argList.size() > 1) {
                appendAllExceptLastNonDateString(builder, prefix, argList);
            }
        }
        return builder.toString();
    }

    /**
     * Method that loops through all but the last string in input {@code List<String>} and appends prefix + args to
     * input {@code StringBuilder} if {@code PrettyTimeParser} fails to parse it.
     */
    private void appendAllExceptLastNonDateString(StringBuilder builder, Prefix prefix,
                                     List<String> argList) {
        List<Date> dateList;
        PrettyTimeParser ptp = new PrettyTimeParser();
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
