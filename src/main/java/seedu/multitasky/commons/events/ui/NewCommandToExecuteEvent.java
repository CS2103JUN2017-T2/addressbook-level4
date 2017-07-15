package seedu.multitasky.commons.events.ui;

import seedu.multitasky.commons.events.BaseEvent;

// @@author A0125586X
/**
 * Indicates that there is a new command to execute.
 */
public class NewCommandToExecuteEvent extends BaseEvent {

    public final String command;

    public NewCommandToExecuteEvent(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
