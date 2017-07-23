package seedu.multitasky.model.entry;

import java.util.Calendar;
import java.util.Set;

import seedu.multitasky.model.tag.Tag;

// @@author A0126623L
/**
 * A read-only immutable interface for a Entry in the entrybook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyEntry {

    Name getName();

    Calendar getStartDateAndTime();

    Calendar getEndDateAndTime();

    String getStartDateAndTimeString();

    String getEndDateAndTimeString();

    Entry.State getState();

    Set<Tag> getTags();

    boolean isActive();

    boolean isArchived();

    boolean isDeleted();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    boolean isSameStateAs(ReadOnlyEntry other);

}
