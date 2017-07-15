package seedu.multitasky.commons.events.ui;

import seedu.multitasky.commons.events.BaseEvent;

// @@author A0125586X
/**
 * Indicates that there is a new command to put into the text box.
 */
public class NewCommandEvent extends BaseEvent {

    public final String command;

    public NewCommandEvent(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
