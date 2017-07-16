package seedu.multitasky.model.entry.exceptions;

import seedu.multitasky.model.entry.ReadOnlyEntry;

/**
 * Signals that the operation results in overlapping events.
 */
public class OverlappingEventException extends Exception {
    public OverlappingEventException() {
    }

    public OverlappingEventException(ReadOnlyEntry existingEvent, ReadOnlyEntry newEvent) {
        super("Warning: The time of " + newEvent.getName() + " clashes with the existing event "
              + existingEvent.getName() + ".");
    }

}
