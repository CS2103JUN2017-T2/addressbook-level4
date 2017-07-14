package seedu.multitasky.storage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//@@author A0132788U
/**
 * Class that implements methods to convert Date to String to enable Storage to store dates in a human-editable form in
 * the XML file.
 * Parses String back to Date and returns to Model.
 */
public class StorageDateConverter {

    /** Formatter to parse date into a human-editable string to store in the XML file */
    private DateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");

    public StorageDateConverter() {
        super();
        formatter.setLenient(false);
    }

    /**
     * This converts the Calendar object into a string type to be stored in XML file in a human editable
     * format.
     */
    public String convertDateToString(Calendar given) {
        String dateToString = formatter.format(given.getTime());
        return dateToString;
    }

    /**
     * This converts a String to a Calendar object to be passed back to Model.
     *
     * @throws Exception
     */
    public Calendar convertStringToDate(String given) throws Exception {
        Calendar setDate = null;
        Date toConvert = new Date();
        try {
            toConvert = formatter.parse(given);
            setDate = setTheTime(toConvert);
        } catch (ParseException e) {
            throw new Exception("Unable to set the time!");
        }
        setDate.setTime(toConvert);
        return setDate;
    }

    /**
     * Sub-method to convert Date to String.
     */
    public Calendar setTheTime(Date given) {
        Calendar toBeSet = Calendar.getInstance();
        toBeSet.setTime(given);
        return toBeSet;
    }

}
