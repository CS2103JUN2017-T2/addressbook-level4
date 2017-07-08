package seedu.multitasky.logic.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.parser.exceptions.ParseException;

// @@author A0140633R
/**
 * Contains utility methods used for parsing strings into dates in the various parser classes.
 */
public class DateUtil {
    private static final String[] timeFormats = { "HH/mm", "HH:mm", "HH-mm", "HH mm" };
    private static final String[] dateSeparators = { "/", "-", " " };

    private static final String DMY_FORMAT = "dd{sep}MM{sep}yyyy";

    private static final String dmy_template = "\\d{1,2}{sep}\\d{1,2}{sep}\\d{2,4}.*";
    //TODO to fix MESSAGE_FAIL to all be in 1 line after i find out how
    private static final String MESSAGE_FAIL = "\nFormat: dd/MM/yy HH/mm";

    /**
     * Converts input string to Date if format conforms to standard format and returns the Date,
     * or returns null if it fails to do so.
     *
     * @throws IllegalValueException if the user input does not conform to standard format.
     */
    public static Date stringToDate(String input) throws IllegalValueException {
        Date date = null;
        String dateFormat = getDateFormat(input);
        //TODO implement a check for (optional) time input
        if (dateFormat == null) {
            throw new ParseException("Date is not in an accepted format " + input + MESSAGE_FAIL);
        }

        for (String sep : dateSeparators) {
            String actualDateFormat = patternForSeparator(dateFormat, sep);
            // try to format input string that has time given
            for (String time : timeFormats) {
                date = tryParse(input, actualDateFormat + " " + time);
                if (date != null) {
                    return date;
                }
            }
            // didn't work, try without the time formats
            date = tryParse(input, actualDateFormat);
            if (date != null) {
                return date;
            }
        }
        return date;
    }

    /**
     * Checks regex of input String date against dmy_template string with {sep} replaced by separators in
     * dateSeparators.
     * @return {@DMY_FORMAT} if found , else null
     */
    private static String getDateFormat(String date) {
        for (String sep : dateSeparators) {
            String dmyPattern = patternForSeparator(dmy_template, sep);
            if (date.matches(dmyPattern)) {
                return DMY_FORMAT;
            }
        }
        return null;
    }

    /**
     * replaces all "{sep}" placeholders in String template to given input String sep.
     */
    private static String patternForSeparator(String template, String sep) {
        return template.replace("{sep}", sep);
    }

    /**
     * Method that parses String input in the format of String pattern and returns Date if succeeded
     * or null if ParseException
     */
    private static Date tryParse(String input, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(input);
        } catch (java.text.ParseException e) {
            // continue to try other patterns when method is called again
        }
        return null;
    }

}
