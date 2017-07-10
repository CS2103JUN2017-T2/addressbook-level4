package seedu.multitasky.model.util;

import java.util.Calendar;
import java.util.Set;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;

//@@author A0125586X
/**
 * A utility class to help with building Entry objects.
 */
public class EntryBuilder {

    public class IncorrectEntryArgumentsException extends Exception {
        public IncorrectEntryArgumentsException(String message) {
            super(message);
        }
    }

    public static final String DEFAULT_NAME = "defaultName";
    public static final String DEFAULT_TAGS = "defaultTag";

    private String name;
    private Set<Tag> tags;
    private Calendar startDateAndTime;
    private Calendar endDateAndTime;

    private Entry entry;

    public EntryBuilder() throws IllegalValueException {
        Name defaultName = new Name(DEFAULT_NAME);
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
        this.entry = new FloatingTask(defaultName, tags);
    }

    /**
     * Initializes the EntryBuilder with the data of {@code entryToCopy}.
     */
    public EntryBuilder(ReadOnlyEntry entryToCopy) {
        this.entry = new FloatingTask(entryToCopy);
    }

    public EntryBuilder withName(String name) throws IllegalValueException {
        this.name = name;
        this.entry.setName(new Name(name));
        return this;
    }

    public EntryBuilder withTags(String... tags) throws IllegalValueException {
        this.entry.setTags(SampleDataUtil.getTagSet(tags));
        return this;
    }

    public EntryBuilder withStartDateAndTime(Calendar startDateAndTime) {
        this.startDateAndTime = startDateAndTime;
        return this;
    }

    public EntryBuilder withEndDateAndTime(Calendar endDateAndTime) {
        this.endDateAndTime = endDateAndTime;
        return this;
    }

    public Entry build() throws Exception {
        if (startDateAndTime == null) {
            // Floating task
            if (endDateAndTime == null) {
                entry = new FloatingTask(new Name(name), tags);
            // Deadline
            } else {
                entry = new Deadline(new Name(name), endDateAndTime, tags);
            }
        // Event
        } else if (endDateAndTime != null) {
            entry = new Event(new Name(name), startDateAndTime, endDateAndTime, tags);
        // Unknown combination of present start date but no end date
        } else {

        }
        return entry;
    }

}
