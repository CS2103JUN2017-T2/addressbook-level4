package seedu.multitasky.testutil;

import java.util.Set;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.model.util.SampleDataUtil;

//@@author A0126623L
/**
 * A utility class to help with building Entry objects.
 */
public class EntryBuilder {

    public static final String DEFAULT_NAME = "defaultName";
    public static final String DEFAULT_TAGS = "defaultTag";

    private Entry entry;

    public EntryBuilder() throws IllegalValueException {
        Name defaultName = new Name(DEFAULT_NAME);
        Set<Tag> defaultTags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
        this.entry = new FloatingTask(defaultName, defaultTags);
    }

    /**
     * Initializes the EntryBuilder with the data of {@code entryToCopy}.
     */
    public EntryBuilder(ReadOnlyEntry entryToCopy) {
        this.entry = new FloatingTask(entryToCopy);
    }

    public EntryBuilder withName(String name) throws IllegalValueException {
        this.entry.setName(new Name(name));
        return this;
    }

    public EntryBuilder withTags(String... tags) throws IllegalValueException {
        this.entry.setTags(SampleDataUtil.getTagSet(tags));
        return this;
    }

    public Entry build() {
        return this.entry;
    }

}
