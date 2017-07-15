package seedu.multitasky.logic.parser;

import seedu.multitasky.logic.commands.Command;
import seedu.multitasky.logic.commands.SetCommand;

//@@author A0132788U

public class SetCommandParser {

    public Command parse(String arguments) {
        return new SetCommand(arguments);
    }

}
