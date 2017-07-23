package seedu.multitasky.model.entry;

import static java.util.Objects.requireNonNull;
import static seedu.multitasky.commons.util.CollectionUtil.requireAllNonNull;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Set;

import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.model.tag.UniqueTagList;

// @@ author A0126623L
/**
 * Represents a Entry in the entry book.
 * Guarantees: details are present and not null, field values are validated.
 */
public abstract class Entry implements ReadOnlyEntry {

    /**
     * Represents the state of an entry.
     * The three possible states are: ACTIVE, ARCHIVED, DELETED
     */
    public enum State {
        ACTIVE {
            @Override
            public String toString() {
                return "ACTIVE";
            }
        },
        ARCHIVED {
            @Override
            public String toString() {
                return "ARCHIVED";
            }
        },
        DELETED {
            @Override
            public String toString() {
                return "DELETED";
            }
        };
    }

    /**
     * Date formatter for subclasses that need to format Date objects.
     */
    protected static DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                                                                               DateFormat.SHORT);

    private Name name;
    private UniqueTagList tags;
    private State state;

    /**
     * Every field must be present and not null.
     * The instantiated Entry will be considered an active entry.
     */
    protected Entry(Name name, Set<Tag> tags) {
        requireAllNonNull(name, tags);
        this.name = name;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
        this.state = State.ACTIVE;
    }

    /**
     * Creates a copy of the given ReadOnlyEntry.
     */
    public Entry(ReadOnlyEntry source) {
        this(source.getName(), source.getTags());
        this.setState(source.getState());
    }

    public void setName(Name name) {
        this.name = requireNonNull(name);
    }

    @Override
    public Name getName() {
        return name;
    }

    /**
     * @param state cannot be null
     */
    public void setState(State state) {
        this.state = requireNonNull(state);
    }

    @Override
    public State getState() {
        return state;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    @Override
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    /**
     * Replaces this entry's tags with the tags in the argument tag set.
     */
    public void setTags(Set<Tag> replacement) {
        tags.setTags(new UniqueTagList(replacement));
    }

    /**
     * Updates this entry with the details of {@code replacement}.
     * @param replacement
     */
    public void resetData(ReadOnlyEntry replacement) {
        requireNonNull(replacement);

        this.setName(replacement.getName());
        this.setTags(replacement.getTags());
        this.setState(replacement.getState());
    }

    @Override
    public boolean isActive() {
        return this.getState().equals(Entry.State.ACTIVE);
    }

    @Override
    public boolean isArchived() {
        return this.getState().equals(Entry.State.ARCHIVED);
    }

    @Override
    public boolean isDeleted() {
        return this.getState().equals(Entry.State.DELETED);
    }

}
