package seedu.multitasky.logic.parser;

import seedu.multitasky.logic.commands.Command;
import seedu.multitasky.logic.commands.SaveCommand;

//@@author A0132788U

/**
 * Parses the input entered for Save Command and returns a new SaveCommand object.
 */
public class SaveCommandParser {

    public Command parse(String arguments) {
        return new SaveCommand(arguments);
    }

}
