package seedu.multitasky.model.entry.exceptions;

import seedu.multitasky.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Entry objects.
 */
public class DuplicateEntryException extends DuplicateDataException {
    public static final String MESSAGE = "Operation would result in duplicate entries";

    public DuplicateEntryException() {
        super(DuplicateEntryException.MESSAGE);
    }
}
