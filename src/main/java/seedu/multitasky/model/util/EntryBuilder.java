package seedu.multitasky.model.util;

import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;

// @@author A0125586X
/**
 * A utility class containing methods to construct Event, Deadline or FloatingTask objects.
 */
public class EntryBuilder {

    public static final String DEFAULT_NAME = "defaultName";
    public static final String DEFAULT_TAGS = "defaultTag";

    /**
     * Builds an entry with the class-level default parameters
     */
    public static Entry build() {
        Entry entry;
        try {
            entry = build(DEFAULT_NAME, DEFAULT_TAGS);
            return entry;
        } catch (IllegalValueException e) {
            throw new AssertionError("Sample data cannot be invalid");
        }
    }

    /******************
     * Multi-builders *
     *****************/
    /**
     * Builds the appropriate Event, Deadline or FloatingTask object as a copy of {@code entryToCopy}.
     * @param entryToCopy the entry that data will be read from to create the new object
     * @return            a copy of {@code entryToCopy}
     */
    public static Entry build(final ReadOnlyEntry entryToCopy) {
        Objects.requireNonNull(entryToCopy);
        Entry entry;
        if (entryToCopy instanceof Event) {
            entry = new Event(entryToCopy);
        } else if (entryToCopy instanceof Deadline) {
            entry = new Deadline(entryToCopy);
        } else if (entryToCopy instanceof FloatingTask) {
            entry = new FloatingTask(entryToCopy);
        } else {
            entry = null;
            throw new AssertionError("entryToCopy must be Event, Deadline or FloatingTask");
        }
        return entry;
    }

    /******************
     * Event builders *
     *****************/
    /**
     * Builds an Event object from the provided arguments.
     * @param name             the name of the event
     * @param startDateAndTime the starting date and time of the event
     * @param endDateAndTime   the ending date and time of the event
     * @param tags             the tags associated with the event
     * @return                 the constructed Event object - returns a Deadline or FloatingTask object if
     *                         there are null Calendar parameters
     * @throws IllegalValueException if any of the arguments are invalid or if there is a wrong combination of
     *                               non-null start date and null end date.
     */
    public static Entry build(String name, Calendar startDateAndTime, Calendar endDateAndTime, String... tags)
            throws IllegalValueException {
        return build(new Name(name), startDateAndTime, endDateAndTime, TagSetBuilder.getTagSet(tags));
    }

    /**
     * Builds an Event object from the provided arguments.
     * @param name             the name of the event
     * @param startDateAndTime the starting date and time of the event
     * @param endDateAndTime   the ending date and time of the event
     * @param tags             the tags associated with the event
     * @return                 the constructed Event object - returns a Deadline or FloatingTask object if
     *                         there are null Calendar parameters
     * @throws IllegalValueException if any of the arguments are invalid or if there is a wrong combination of
     *                               non-null start date and null end date.
     */
    public static Entry build(String name, Calendar startDateAndTime, Calendar endDateAndTime, Set<Tag> tags)
            throws IllegalValueException {
        return build(new Name(name), startDateAndTime, endDateAndTime, tags);
    }

    /**
     * Builds an Event object from the provided arguments.
     * @param name             the name of the event
     * @param startDateAndTime the starting date and time of the event
     * @param endDateAndTime   the ending date and time of the event
     * @param tags             the tags associated with the event
     * @return                 the constructed Event object - returns a Deadline or FloatingTask object if
     *                         there are null Calendar parameters
     * @throws IllegalValueException if any of the arguments are invalid or if there is a wrong combination of
     *                               non-null start date and null end date.
     */
    public static Entry build(Name name, Calendar startDateAndTime, Calendar endDateAndTime, String... tags)
            throws IllegalValueException {
        return build(name, startDateAndTime, endDateAndTime, TagSetBuilder.getTagSet(tags));
    }

    /**
     * Builds an Event object from the provided arguments.
     * @param name             the name of the event
     * @param startDateAndTime the starting date and time of the event
     * @param endDateAndTime   the ending date and time of the event
     * @param tags             the tags associated with the event
     * @return                 the constructed Event object - returns a Deadline or FloatingTask object if
     *                         there are null Calendar parameters
     * @throws IllegalValueException if any of the arguments are invalid or if there is a wrong combination of
     *                               non-null start date and null end date.
     */
    public static Entry build(Name name, Calendar startDateAndTime, Calendar endDateAndTime, Set<Tag> tags)
            throws IllegalValueException {
        if (startDateAndTime == null) {
            if (endDateAndTime == null) {
                Entry entry = build(name, tags);
                return entry;
            } else {
                Entry entry = build(name, endDateAndTime, tags);
                return entry;
            }
        } else if (endDateAndTime != null) {
            Entry entry = new Event(name, startDateAndTime, endDateAndTime, tags);
            return entry;
        } else {
            throw new IllegalValueException("Wrong combination of non-null start date and null end date");
        }
    }

    /*********************
     * Deadline builders *
     ********************/
    /**
     * Builds a Deadline object from the provided arguments.
     * @param name             the name of the deadline
     * @param endDateAndTime   the date and time of the deadline
     * @param tags             the tags associated with the deadline
     * @return                 the constructed Deadline object - returns a FloatingTask object if
     *                         {@code endDateAndTime} is null
     * @throws IllegalValueException if any of the arguments are invalid
     */
    public static Entry build(String name, Calendar endDateAndTime, String... tags)
            throws IllegalValueException {
        return build(new Name(name), endDateAndTime, TagSetBuilder.getTagSet(tags));
    }

    /**
     * Builds a Deadline object from the provided arguments.
     * @param name             the name of the deadline
     * @param endDateAndTime   the date and time of the deadline
     * @param tags             the tags associated with the deadline
     * @return                 the constructed Deadline object - returns a FloatingTask object if
     *                         {@code endDateAndTime} is null
     * @throws IllegalValueException if any of the arguments are invalid
     */
    public static Entry build(String name, Calendar endDateAndTime, Set<Tag> tags)
            throws IllegalValueException {
        return build(new Name(name), endDateAndTime, tags);
    }

    /**
     * Builds a Deadline object from the provided arguments.
     * @param name             the name of the deadline
     * @param endDateAndTime   the date and time of the deadline
     * @param tags             the tags associated with the deadline
     * @return                 the constructed Deadline object - returns a FloatingTask object if
     *                         {@code endDateAndTime} is null
     * @throws IllegalValueException if any of the arguments are invalid
     */
    public static Entry build(Name name, Calendar endDateAndTime, String... tags)
            throws IllegalValueException {
        return build(name, endDateAndTime, TagSetBuilder.getTagSet(tags));
    }

    /**
     * Builds a Deadline object from the provided arguments.
     * @param name             the name of the deadline
     * @param endDateAndTime   the date and time of the deadline
     * @param tags             the tags associated with the deadline
     * @return                 the constructed Deadline object - returns a FloatingTask object if
     *                         {@code endDateAndTime} is null
     * @throws IllegalValueException if any of the arguments are invalid
     */
    public static Entry build(Name name, Calendar endDateAndTime, Set<Tag> tags)
            throws IllegalValueException {
        if (endDateAndTime == null) {
            return build(name, tags);
        } else {
            return new Deadline(name, endDateAndTime, tags);
        }
    }

    /*************************
     * FloatingTask builders *
     ************************/
    /**
     * Builds a FloatingTask object from the provided arguments.
     * @param name             the name of the floating task
     * @return                 the constructed FloatingTask object
     * @throws IllegalValueException if any of the arguments are invalid
     */
    public static Entry build(String name) throws IllegalValueException {
        return build(new Name(name), TagSetBuilder.getTagSet());
    }

    /**
     * Builds a FloatingTask object from the provided arguments.
     * @param name             the name of the floating task
     * @return                 the constructed FloatingTask object
     * @throws IllegalValueException if any of the arguments are invalid
     */
    public static Entry build(Name name) throws IllegalValueException {
        return build(name, TagSetBuilder.getTagSet());
    }

    /**
     * Builds a FloatingTask object from the provided arguments.
     * @param name             the name of the floating task
     * @param tags             the tags associated with the floating task
     * @return                 the constructed FloatingTask object
     * @throws IllegalValueException if any of the arguments are invalid
     */
    public static Entry build(String name, String... tags) throws IllegalValueException {
        return build(new Name(name), TagSetBuilder.getTagSet(tags));
    }

    /**
     * Builds a FloatingTask object from the provided arguments.
     * @param name             the name of the floating task
     * @param tags             the tags associated with the floating task
     * @return                 the constructed FloatingTask object
     * @throws IllegalValueException if any of the arguments are invalid
     */
    public static Entry build(String name, Set<Tag> tags) throws IllegalValueException {
        return build(new Name(name), tags);
    }

    /**
     * Builds a FloatingTask object from the provided arguments.
     * @param name             the name of the floating task
     * @param tags             the tags associated with the floating task
     * @return                 the constructed FloatingTask object
     * @throws IllegalValueException if any of the arguments are invalid
     */
    public static Entry build(Name name, String... tags) throws IllegalValueException {
        return build(name, TagSetBuilder.getTagSet(tags));
    }

    /**
     * Builds a FloatingTask object from the provided arguments.
     * @param name             the name of the floating task
     * @param tags             the tags associated with the floating task
     * @return                 the constructed FloatingTask object
     * @throws IllegalValueException if any of the arguments are invalid
     */
    public static Entry build(Name name, Set<Tag> tags) throws IllegalValueException {
        return new FloatingTask(name, tags);
    }
}
