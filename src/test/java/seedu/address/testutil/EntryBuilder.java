package seedu.address.testutil;

import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.entry.Address;
import seedu.address.model.entry.Email;
import seedu.address.model.entry.Name;
import seedu.address.model.entry.Person;
import seedu.address.model.entry.Phone;
import seedu.address.model.entry.ReadOnlyPerson;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class EntryBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_TAGS = "friends";

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
