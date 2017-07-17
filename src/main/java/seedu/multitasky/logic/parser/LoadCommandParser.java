package seedu.multitasky.logic.parser;

import seedu.multitasky.logic.commands.Command;
import seedu.multitasky.logic.commands.LoadCommand;

//@@author A0132788U

/**
 * Parses the input entered for Load Command and returns a new LoadCommand object.
 */
public class LoadCommandParser {

    public Command parse(String arguments) {
        return new LoadCommand(arguments);
    }

}
