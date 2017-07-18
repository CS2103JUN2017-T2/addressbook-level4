package seedu.multitasky.logic.parser;

import seedu.multitasky.logic.commands.Command;
import seedu.multitasky.logic.commands.OpenCommand;

//@@author A0132788U

/**
 * Parses the input entered for Open Command and returns a new OpenCommand object.
 */
public class OpenCommandParser {

    public Command parse(String arguments) {
        return new OpenCommand(arguments);
    }

}
