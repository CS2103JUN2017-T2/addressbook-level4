package seedu.address.model.entry.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Entry objects.
 */
public class DuplicateEntryException extends DuplicateDataException {
    public DuplicateEntryException() {
        super("Operation would result in duplicate entries");
    }
}
