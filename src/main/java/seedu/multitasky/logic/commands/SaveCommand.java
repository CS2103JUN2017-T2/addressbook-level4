package seedu.multitasky.logic.commands;

import java.io.File;

import seedu.multitasky.logic.commands.exceptions.CommandException;

// @@author A0132788U
/**
 * Checks validity of entered path and saves data at the given file.
 */

public class SaveCommand extends Command {

    public static final String COMMAND_WORD = "save";

    public static final String MESSAGE_SUCCESS = "Entrybook data saved successfully to ";
    public static final String MESSAGE_FAILURE = "Invalid file path!\n";
    public static final String MESSAGE_EXISTS = "File already exists! Rename file.\n";
    public static final String SAMPLE_FILEPATH = " /Users/usernamehere/Desktop/entrybook.xml";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Saves entrybook data to given filepath\n" + "Format: "
                                               + COMMAND_WORD + " newfilepath.xml\n" + "Example: " + COMMAND_WORD
                                               + SAMPLE_FILEPATH;

    public static final String[] VALID_PREFIXES = new String[] {};

    private final String newFilePath;

    public SaveCommand(String newFilePath) {
        this.newFilePath = newFilePath.trim();
    }

    /**
     * Executes the Set command if the path is valid and file doesn't already exist in this location.
     *
     * @throws CommandException
     */
    @Override
    public CommandResult execute() throws CommandException {
        if (isValidPath(newFilePath)) {
            if ((new File(newFilePath)).isFile()) {
                throw new CommandException(MESSAGE_EXISTS);
            } else {
                model.changeFilePath(newFilePath);
                return new CommandResult(MESSAGE_SUCCESS + newFilePath);
            }
        } else {
            throw new CommandException(MESSAGE_FAILURE + MESSAGE_USAGE);
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
