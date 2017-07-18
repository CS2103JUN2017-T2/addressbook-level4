package seedu.multitasky.logic.commands;

import java.io.File;

import seedu.multitasky.commons.exceptions.IllegalValueException;

// @@author A0132788U
/**
 * Opens the entered XML path and loads data in the given file.
 */

public class OpenCommand extends Command {

    public static final String COMMAND_WORD = "open";

    public static final String MESSAGE_SUCCESS = "Open data from ";
    public static final String MESSAGE_FAILURE = "File does not exist!\n";
    public static final String MESSAGE_INVALID_XML_FILE = "File is not in readable XML format!\n";
    public static final String SAMPLE_FILEPATH = " /Users/usernamehere/Desktop/entrybook.xml";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Opens given file to loads entrybook data\n"
                                               + "Format: "
                                               + COMMAND_WORD + " filepath.xml\n" + "Example: " + COMMAND_WORD
                                               + SAMPLE_FILEPATH;
    private final String filepath;

    public OpenCommand(String filePath) {
        this.filepath = filePath.trim();
    }

    /**
     * Executes the Load command if the file already exists in this location.
     */
    @Override
    public CommandResult execute() {
        if ((new File(filepath)).exists()) {
            try {
                model.openFilePath(filepath);
                return new CommandResult(MESSAGE_SUCCESS + filepath);
            } catch (IllegalValueException e) {
                return new CommandResult(MESSAGE_INVALID_XML_FILE);
            }
        } else {
            return new CommandResult(MESSAGE_FAILURE + MESSAGE_USAGE);
        }
    }
}
