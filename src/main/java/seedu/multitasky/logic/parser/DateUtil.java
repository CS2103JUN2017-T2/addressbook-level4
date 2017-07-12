package seedu.multitasky.logic.parser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.parser.exceptions.ParseException;

// @@author A0140633R
/**
 * Contains utility methods used for parsing strings into dates in the various parser classes.
 */
public class DateUtil {
    //TODO add in support for "[.]" and "\\s", for some reason not working.
    private static final String[] dateSeparators = { "/", "-" };
    private static final String[] timeSeparators = { ":", "-", "/" };

    // {sep} represents a date separator and {tsep} represents time separator.
    private static final String DATETIME_REGEX_TEMPLATE = "\\d{1,2}{sep}\\d{1,2}{sep}\\d{1,4}"
                                                          + "\\s+\\d{1,2}{tsep}\\d{1,2}";
    private static final String DATETIME_TEMPLATE = "d{sep}M{sep}y H{tsep}m";
    private static final String MESSAGE_FAIL = "\nAccepted DateTime Format: dd/MM/yy HH:mm";

    /**
     * Converts input string to Calendar if format conforms to standard format and returns the Calendar.
     *
     * @throws IllegalValueException if the user input does not conform to standard format, or if calendar
     *         date falls after input Calendar endDate, if endDate was given.
     */
    public static Calendar stringToCalendar(String input, Calendar endDate)
            throws IllegalValueException {
        String trimmedInput = input.trim();
        String dateTimeFormat = checkDateTimeFormat(trimmedInput);

        if (dateTimeFormat == null) {
            throw new ParseException("Date is not in an accepted format " + input + MESSAGE_FAIL);
        }

        Calendar calendar = new GregorianCalendar();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
            sdf.setLenient(false);
            Date date = sdf.parse(trimmedInput);
            calendar.setTime(date);
        } catch (java.text.ParseException e) {
            throw new ParseException("Date is not an accepted format " + input + MESSAGE_FAIL);
        }

        // endDate has been initiated, startDate should not be initiated after endDate.
        if (endDate != null) {
            if (endDate.compareTo(calendar) < 0) { // end date is equal or later than start date
                throw new ParseException("End Date cannot be before Start Date!");
            }
        }

        return calendar;
    }

    /**
     * Checks regex of input String date against REGEX_TEMPLATE string with {sep} replaced by separators in
     * dateSeparators and {tsep} replaced by separators from timeSeparators
     *
     * @return {@templateDateTimeSeparatorInserted} which is DATETIME_TEMPLATE with sep replaced as described above.
     */
    private static String checkDateTimeFormat(String input) {
        for (String dateSeparator : dateSeparators) {
            String regexDateSeparatorInserted = replaceDateSeparator(DATETIME_REGEX_TEMPLATE, dateSeparator);
            String templateDateSeparatorInserted = replaceDateSeparator(DATETIME_TEMPLATE, dateSeparator);

            for (String timeSeparator : timeSeparators) {
                String regexDateTimeSeparatorInserted = replaceTimeSeparator(regexDateSeparatorInserted,
                                                                             timeSeparator);
                String templateDateTimeSeparatorInserted = replaceTimeSeparator(templateDateSeparatorInserted,
                                                                                timeSeparator);

                if (input.matches(regexDateTimeSeparatorInserted)) {
                    return templateDateTimeSeparatorInserted;
                } // continue
            }
        }
        // failed to find a match. return null
        return null;
    }

    /**
     * replaces all "{sep}" placeholders in String template to given input String sep.
     */
    private static String replaceDateSeparator(String template, String sep) {
        return template.replace("{sep}", sep);
    }

    /**
     * replaces all "{tsep}" placeholders in String template to given input String tsep.
     */
    private static String replaceTimeSeparator(String template, String tsep) {
        return template.replace("{tsep}", tsep);
    }

}
