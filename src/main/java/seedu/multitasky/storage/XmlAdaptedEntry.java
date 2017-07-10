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

/**
 * JAXB-friendly version of the Entry.
 */
public class XmlAdaptedEntry {

    @XmlElement(required = true)
    private String name;

    @XmlElement
    private Calendar startDateAndTime;
    @XmlElement
    private Calendar endDateAndTime;
    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    private EntryBuilder entryBuilder;

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
        startDateAndTime = source.getStartDateAndTime();
        endDateAndTime = source.getEndDateAndTime();
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
        final List<Tag> entryTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            entryTags.add(tag.toModelType());
        }
        final Name name = new Name(this.name);
        final Set<Tag> tags = new HashSet<>(entryTags);
        return entryBuilder.build();
    }
}
