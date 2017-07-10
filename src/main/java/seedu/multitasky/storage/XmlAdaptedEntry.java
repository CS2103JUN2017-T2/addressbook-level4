package seedu.multitasky.storage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.model.util.EntryBuilder;

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

    private SimpleDateFormat formatter = new SimpleDateFormat("d/M/y H:mm");

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
        formatter.setLenient(false);
        name = source.getName().fullName;
        startDateAndTime = formatter.format(source.getStartDateAndTime().getTime());
        endDateAndTime = formatter.format(source.getEndDateAndTime().getTime());
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
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }
        Calendar startDateAndTimeToUse = null;
        Calendar endDateAndTimeToUse = null;
        if (this.startDateAndTime != null) {
            startDateAndTimeToUse.setTime(formatter.parse(this.startDateAndTime));
        }
        if (this.endDateAndTime != null) {
            endDateAndTimeToUse.setTime(formatter.parse(this.endDateAndTime));
        }
        final Set<Tag> tags = new HashSet<>(personTags);
        Entry entry = new EntryBuilder().withName(this.name).withStartDateAndTime(startDateAndTimeToUse)
                .withEndDateAndTime(endDateAndTimeToUse).withTags(tags).build();
        return entry;
    }
}
