package seedu.multitasky.storage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.model.util.EntryBuilder;

//@@author A0132788U
/**
 * JAXB-friendly version of the Entry.
 */
public class XmlAdaptedEntry {

    @XmlElement(required = true)
    private String name;

    @XmlElement
    private String startDateAndTime;
    @XmlElement
    private String endDateAndTime;
    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    private DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");

    /**
     * Constructs an XmlAdaptedEntry. This is the no-arg constructor that is
     * required by JAXB.
     */
    public XmlAdaptedEntry() {
    }

    /**
     * Converts a given Entry into this class for JAXB use. Future changes to
     * this will not affect the created XmlAdaptedEntry
     */
    public XmlAdaptedEntry(ReadOnlyEntry source) {
        name = source.getName().fullName;

        if (source.getStartDateAndTime() != null) {
            startDateAndTime = convertDateToString(source.getStartDateAndTime());
        }
        if (source.getEndDateAndTime() != null) {
            endDateAndTime = convertDateToString(source.getEndDateAndTime());
        }
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * This converts the Calendar object into a string type to be stored in XML file in a human editable
     * format.
     */
    public String convertDateToString(Calendar given) {
        String dateToString = df.format(given.getTime());
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
            toConvert = df.parse(given);
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

    /**
     * Converts this jaxb-friendly adapted entry object into the model's Entry
     * object.
     *
     * @throws Exception
     */

    public Entry toModelType() throws Exception {
        Name newName = new Name(this.name);
        final List<Tag> entryTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            entryTags.add(tag.toModelType());
        }
        Calendar startDateAndTimeToUse = null;
        Calendar endDateAndTimeToUse = null;

        if (startDateAndTime != null) {
            try {
                startDateAndTimeToUse = convertStringToDate(startDateAndTime);
            } catch (Exception e) {
                throw new Exception("Start time is invalid!");
            }
        }

        if (endDateAndTime != null) {
            try {
                endDateAndTimeToUse = convertStringToDate(endDateAndTime);
            } catch (Exception e) {
                throw new Exception("End time is invalid!");
            }
        }

        final Set<Tag> tags = new HashSet<>(entryTags);
        EntryBuilder buildObject = new EntryBuilder();
        Entry entry = buildObject.build(newName, startDateAndTimeToUse, endDateAndTimeToUse,
                                        tags);
        return entry;
    }
}
