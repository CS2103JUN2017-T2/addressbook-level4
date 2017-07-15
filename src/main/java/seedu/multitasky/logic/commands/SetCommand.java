package seedu.multitasky.logic.commands;

import java.io.File;

// @@author A0132788U
/**
 * Sets the file path to store/load entrybook data.
 */
public class SetCommand extends Command {

    public static final String COMMAND_WORD = "set";

    public static final String MESSAGE_SUCCESS = "File path set successfully";
    public static final String MESSAGE_FAILURE = "Invalid file path";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets file path of the entrybook. " + "\n" + "Format: "
                                               + COMMAND_WORD + "Filepath to be set to \n" + "Example: " + COMMAND_WORD
                                               + " Users/usernamehere/Desktop/entrybook.xml";
    private final String newFilePath;

    public SetCommand(String newFilePath) {
        this.newFilePath = newFilePath.trim();
    }

    @Override
    public CommandResult execute() {
        if (isValidPath(newFilePath)) {
            model.changeFilePath(newFilePath);
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_FAILURE);
        }
    }

    private boolean isValidPath(String newFilePath) {
        File file = new File(newFilePath);
        return ((file.canWrite()) && (newFilePath.endsWith(".xml")));
    }
}
