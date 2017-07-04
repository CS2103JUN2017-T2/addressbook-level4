package seedu.address.testutil;

import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.entry.Name;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

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
        this.entry = new Entry(defaultName, defaultTags);
    }

    /**
     * Initializes the EntryBuilder with the data of {@code entryToCopy}.
     */
    public EntryBuilder(ReadOnlyEntry entryToCopy) {
        this.entry = new Entry(entryToCopy);
    }

    public EntryBuilder withName(String name) throws IllegalValueException {
        this.entry.setName(new Name(name));
        return this;
    }

    public EntryBuilder withTags(String ... tags) throws IllegalValueException {
        this.entry.setTags(SampleDataUtil.getTagSet(tags));
        return this;
    }

    public Entry build() {
        return this.entry;
    }

}
