package seedu.multitasky.model.entry;

import static java.util.Objects.requireNonNull;
import static seedu.multitasky.commons.util.CollectionUtil.requireAllNonNull;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Set;

import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.model.tag.UniqueTagList;

/**
 * Represents a Entry in the entry book.
 * Guarantees: details are present and not null, field values are validated.
 */
public abstract class Entry implements ReadOnlyEntry {

    // @@author A0126623L
    /**
     * Date formatter for subclasses that need to format Date objects.
     */
    protected static DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                                                                               DateFormat.SHORT);
    // @@author

    private Name _name;
    private UniqueTagList _tags;

    /**
     * Every field must be present and not null.
     */
    public Entry(Name name, Set<Tag> tags) {
        requireAllNonNull(name, tags);
        this._name = name;
        this._tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
    }

    /**
     * Creates a copy of the given ReadOnlyEntry.
     */
    public Entry(ReadOnlyEntry source) {
        this(source.getName(), source.getTags());
    }

    public void setName(Name name) {
        this._name = requireNonNull(name);
    }

    @Override
    public Name getName() {
        return _name;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    @Override
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(_tags.toSet());
    }

    /**
     * Replaces this entry's tags with the tags in the argument tag set.
     */
    public void setTags(Set<Tag> replacement) {
        _tags.setTags(new UniqueTagList(replacement));
    }

    /**
     * Updates this entry with the details of {@code replacement}.
     */
    public void resetData(ReadOnlyEntry replacement) {
        requireNonNull(replacement);

        this.setName(replacement.getName());
        this.setTags(replacement.getTags());
    }

}
