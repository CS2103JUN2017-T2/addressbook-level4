package seedu.multitasky.storage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.storage.util.StorageDateConverter;

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
    private String state;
    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /** To convert Date to String to store in XML file and String back to Date to return to Model */
    private StorageDateConverter converter = new StorageDateConverter();

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
            startDateAndTime = converter.convertDateToString(source.getStartDateAndTime());
        }

        if (source.getEndDateAndTime() != null) {
            endDateAndTime = converter.convertDateToString(source.getEndDateAndTime());
        }

        state = source.getState().toString();

        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
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
                startDateAndTimeToUse = converter.convertStringToDate(startDateAndTime);
            } catch (Exception e) {
                throw new Exception("Start time is invalid!");
            }
        }

        if (endDateAndTime != null) {
            try {
                endDateAndTimeToUse = converter.convertStringToDate(endDateAndTime);
            } catch (Exception e) {
                throw new Exception("End time is invalid!");
            }
        }

        final Set<Tag> tags = new HashSet<>(entryTags);
        Entry entry = EntryBuilder.build(newName, startDateAndTimeToUse, endDateAndTimeToUse, tags);

        setEntryState(entry);

        return entry;
    }

    private void setEntryState(Entry entry) {
        switch (state) {
        case "ACTIVE":
            return;
        case "ARCHIVED":
            entry.setState(Entry.State.ARCHIVED);
            return;
        case "DELETED":
            entry.setState(Entry.State.DELETED);
            return;
        default:
            throw new AssertionError(state);
        }
    }
}
