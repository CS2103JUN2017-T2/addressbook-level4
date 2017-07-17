package seedu.multitasky.commons.events.ui;

import seedu.multitasky.commons.events.BaseEvent;

/**
 * An event indicating a result box style change.
 */
public class ResultStyleChangeEvent extends BaseEvent {
    private boolean hasError;

    public ResultStyleChangeEvent(boolean hasError) {
        this.hasError = hasError;
    }

    public boolean hasError() {
        return hasError;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
