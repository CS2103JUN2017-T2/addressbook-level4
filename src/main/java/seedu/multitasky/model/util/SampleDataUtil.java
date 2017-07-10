package seedu.multitasky.model.util;

import java.util.HashSet;
import java.util.Set;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.tag.Tag;

public class SampleDataUtil {
    //@@author A0125586X
    public static Entry[] getSampleEntries() {
        try {
            //CHECKSTYLE.OFF: LineLength
            return new Entry[] {
                new FloatingTask(new Name("Take lunch to work"), getTagSet()),
                new FloatingTask(new Name("Take dog for walk"), getTagSet()),
                new FloatingTask(new Name("Fill up cat food bowl"), getTagSet()),
                new FloatingTask(new Name("Write novel"), getTagSet()),
                new FloatingTask(new Name("Buy groceries"), getTagSet()),
                new FloatingTask(new Name("Refactor code"), getTagSet()),
                new FloatingTask(new Name("Write two more tasks"), getTagSet()),
                new FloatingTask(new Name("Import test cases"), getTagSet()),
                new FloatingTask(new Name("Scold Travis"), getTagSet())
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
