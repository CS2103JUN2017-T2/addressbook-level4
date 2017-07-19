package seedu.multitasky.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.commons.util.StringUtil;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INSUFFICIENT_PARTS = "Number of parts must be more than 1.";
    public static final String MESSAGE_FAIL_PARSE_DATE = "Unable to parse date: %1$s";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces
     * will be trimmed.
     *
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     */
    public static Optional<Name> parseName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        if (!name.isPresent()) {
            return Optional.empty();
        } else {
            return Optional.of(new Name(name.get().replaceAll("\\" + CliSyntax.PREFIX_ESCAPE.toString(), "")));
        }
    }

    // @@author A0140633R
    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Calendar>} if {@code name} is present.
     */
    public static Optional<Calendar> parseDate(Optional<String> inputArgs) throws IllegalValueException {
        requireNonNull(inputArgs);
        return inputArgs.isPresent() ? Optional.of(parseDate(inputArgs.get()))
                                     : Optional.empty();
    }

    /**
     * Converts input string to Calendar if format conforms to standard format and returns the Calendar.
     *
     * @throws IllegalValueException if input args String cannot be parsed into a Date.
     */
    public static Calendar parseDate(String args) throws ParseException {
        PrettyTimeParser ptp = new PrettyTimeParser();
        Calendar calendar = new GregorianCalendar();

        List<Date> dates = ptp.parse(args);
        if (dates.size() != 1) {
            throw new ParseException(String.format(MESSAGE_FAIL_PARSE_DATE, args));
        }
        Date date = dates.get(0);
        calendar.setTime(date);
        return calendar;

    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Calendar>} if both {@code firstArgs}
     * & {@code secondArgs} is present.
     */
    public static Optional<Calendar> parseExtendedDate(Optional<String> firstArgs,
            Optional<String> secondArgs) throws IllegalValueException {
        return firstArgs.isPresent() && secondArgs.isPresent()
                ? Optional.of(parseExtendedDate(firstArgs.get(), secondArgs.get())) : Optional.empty();
    }

    /**
     * converts second arguments into Calendar using required missing Date fields (such as month) taken from the
     * first date parameters.
     * first date arguments should be valid
     * @throws IllegalValueException if either input args String cannot be parsed into a Date.
     */
    public static Calendar parseExtendedDate(String firstDateArgs, String secondDateArgs) throws ParseException {
        String combinedDateArgs = firstDateArgs.trim() + " to " + secondDateArgs.trim();
        PrettyTimeParser ptp = new PrettyTimeParser();
        Calendar calendar = new GregorianCalendar();

        List<Date> dates = ptp.parse(firstDateArgs);
        if (dates.size() != 1) {
            throw new ParseException(String.format(MESSAGE_FAIL_PARSE_DATE, firstDateArgs));
        }
        dates = ptp.parse(combinedDateArgs);
        if (dates.size() != 2) {
            throw new ParseException(String.format(MESSAGE_FAIL_PARSE_DATE, secondDateArgs));
        }
        Date secondDate = dates.get(1);
        calendar.setTime(secondDate);
        return calendar;

    }
    // @@author

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws IllegalValueException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagString : tags) {
            for (String tagName : tagString.split("\\s+")) {
                tagSet.add(new Tag(tagName));
            }
        }
        return tagSet;
    }

    /**
     * Returns true if any of the prefixes contain non-empty values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    // @@author A0140633R
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean areAllPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
    // @@author

    /**
     * Filters out Prefix's not mapped to anything in ArgumentMultimap parameter, and returns prefix that has
     * arguments mapped to it.
     * Precondition: 1 and only 1 Prefix of the given argument prefixes have arguments mapped to it.
     */
    public static Prefix getMainPrefix(ArgumentMultimap argMultimap, Prefix... prefixes) {
        List<Prefix> tempList = Stream.of(prefixes).filter(prefix -> argMultimap.getValue(prefix).isPresent())
                                  .collect(Collectors.toList());
        if (tempList.size() != 1) {
            assert false : "More than one or zero Prefixes found in getMainPrefix";
        }
        return tempList.get(0);
    }

    /**
     * Method to obtain the correct UnmodifiableObservableList from Model according to input Prefix parameter
     * and return it.
     */
    public static UnmodifiableObservableList<ReadOnlyEntry> getListIndicatedByPrefix(
            Model model, Prefix listIndicatorPrefix) {
        UnmodifiableObservableList<ReadOnlyEntry> indicatedList;
        assert listIndicatorPrefix != null;

        if (listIndicatorPrefix.equals(PREFIX_FLOATINGTASK)) {
            indicatedList = model.getFilteredFloatingTaskList();
        } else if (listIndicatorPrefix.equals(PREFIX_DEADLINE)) {
            indicatedList = model.getFilteredDeadlineList();
        } else if (listIndicatorPrefix.equals(PREFIX_EVENT)) {
            indicatedList = model.getFilteredEventList();
        } else {
            indicatedList = null;
            assert false : "Indexes should only be indicated by float, deadline or event";
        }
        return indicatedList;
    }

    /**
     * searches through the input string for for prefixes and returns the prefix that has the
     * last occurence
     * returns null if not found
     */
    public static Prefix getLastPrefix(String stringToSearch, Prefix...prefixes) {
        // to deal with cases with prefix right at end or start
        String extendedSearchString = " " + stringToSearch + " ";
        Prefix foundPrefix = null;
        int maxIndex = 0;
        for (Prefix prefix : prefixes) {
            Integer lastOccurence = extendedSearchString.lastIndexOf(" " + prefix + " ");
            if (lastOccurence > maxIndex) {
                maxIndex = lastOccurence;
                foundPrefix = prefix;
            }
        }
        return foundPrefix;
    }

    // @@author A0125586X
    /**
     * converts a String array of prefixes into a Prefix array
     */
    public static Prefix[] toPrefixArray(String... stringPrefixes) {
        Prefix[] prefixes = new Prefix[stringPrefixes.length];
        for (int i = 0; i < stringPrefixes.length; ++i) {
            prefixes[i] = new Prefix(stringPrefixes[i]);
        }
        return prefixes;
    }

}
