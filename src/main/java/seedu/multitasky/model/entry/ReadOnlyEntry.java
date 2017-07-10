package seedu.multitasky.model.entry;

import java.util.Calendar;
import java.util.Set;

import seedu.multitasky.model.tag.Tag;

/**
 * A read-only immutable interface for a Entry in the entrybook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyEntry {

    Name getName();

    Calendar getStartDateAndTime();

    Calendar getEndDateAndTime();

    Set<Tag> getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    boolean isSameStateAs(ReadOnlyEntry other);

}
