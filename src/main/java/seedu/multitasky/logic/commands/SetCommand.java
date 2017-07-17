package seedu.multitasky.logic.commands;

import java.io.File;

// @@author A0132788U
/**
 * Checks validity of entered filepath and sets the filepath to store entrybook data in a user-defined location.
 */

public class SetCommand extends Command {

    public static final String COMMAND_WORD = "set";

    public static final String MESSAGE_SUCCESS = "File path set successfully to ";
    public static final String MESSAGE_FAILURE = "Invalid file path!\n";
    public static final String MESSAGE_EXISTS = "File already exists! Rename file.\n";
    public static final String SAMPLE_FILEPATH = " /Users/usernamehere/Desktop/entrybook.xml";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets file path of the entrybook\n" + "Format: "
                                               + COMMAND_WORD + " newfilepath\n" + "Example: " + COMMAND_WORD
                                               + SAMPLE_FILEPATH;
    private final String newFilePath;

    public SetCommand(String newFilePath) {
        this.newFilePath = newFilePath.trim();
    }

    /**
     * Executes the Set command if the path is valid and file doesn't already exist in this location.
     */
    @Override
    public CommandResult execute() {
        if (isValidPath(newFilePath)) {
            if ((new File(newFilePath)).isFile()) {
                return new CommandResult(MESSAGE_EXISTS);
            } else {
                model.changeFilePath(newFilePath);
                return new CommandResult(MESSAGE_SUCCESS + newFilePath);
            }
        } else {
            return new CommandResult(MESSAGE_FAILURE + MESSAGE_USAGE);
        }
    }

    /**
     * Method to check whether given file path is valid.
     * A file path is valid if :
     * 1. It has a non-null parent directory.
     * 2. It ends with xml.
     * 3. The parent directory can be written to.
     */
    private boolean isValidPath(String newFilePath) {
        File parent = (new File(newFilePath)).getParentFile();
        if (parent != null) {
            return (newFilePath.endsWith(".xml") && parent.canWrite());
        } else {
            return false;
        }
    }
}
