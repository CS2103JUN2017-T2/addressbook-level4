package seedu.multitasky.model.util;

import java.util.HashSet;
import java.util.Set;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.tag.Tag;

/**
 * Provides utility methods to build sets of Tags
 */
public class TagSetBuilder {

    // @@ author A0126623L
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
    // @@ author

}
