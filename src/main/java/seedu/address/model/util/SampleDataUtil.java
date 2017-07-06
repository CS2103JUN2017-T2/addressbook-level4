package seedu.address.model.util;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.EntryBook;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Name;
import seedu.address.model.tag.Tag;

public class SampleDataUtil {
    //@@author A0125586X
    public static Entry[] getSampleEntries() {
        try {
            //CHECKSTYLE.OFF: LineLength
            return new Entry[] {
                new Entry(new Name("Take lunch to work"), getTagSet()),
                new Entry(new Name("Take dog for walk"), getTagSet()),
                new Entry(new Name("Fill up cat food bowl"), getTagSet()),
                new Entry(new Name("Write novel"), getTagSet()),
                new Entry(new Name("Buy groceries"), getTagSet()),
                new Entry(new Name("Refactor code"), getTagSet()),
                new Entry(new Name("Write two more tasks"), getTagSet()),
                new Entry(new Name("Import test cases"), getTagSet()),
                new Entry(new Name("Scold Travis"), getTagSet())
            };
            //CHECKSTYLE.ON: LineLength
        } catch (IllegalValueException e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }
    //@@author

    public static ReadOnlyEntryBook getSampleEntryBook() {
        //TODO write catch block for DuplicateEntryException
        EntryBook sampleEntryBook = new EntryBook();
        for (Entry sampleEntry : getSampleEntries()) {
            sampleEntryBook.addEntry(sampleEntry);
        }
        return sampleEntryBook;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) throws IllegalValueException {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
