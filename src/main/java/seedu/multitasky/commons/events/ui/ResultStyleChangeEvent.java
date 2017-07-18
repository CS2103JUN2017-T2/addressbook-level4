package seedu.multitasky.commons.events.ui;

import seedu.multitasky.commons.events.BaseEvent;

/**
 * An event indicating a result box style change.
 */
public class ResultStyleChangeEvent extends BaseEvent {
    private boolean isSuccess;

    public ResultStyleChangeEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
