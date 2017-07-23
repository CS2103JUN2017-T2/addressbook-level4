package seedu.multitasky.logic.commands;

import java.io.File;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.exceptions.CommandException;

// @@author A0132788U
/**
 * Opens the entered filepath of the readable XML file to load data.
 */

public class OpenCommand extends Command {

    public static final String COMMAND_WORD = "open";

    public static final String MESSAGE_SUCCESS = "Open data from ";
    public static final String MESSAGE_FAILURE = "File does not exist!\n";
    public static final String MESSAGE_INVALID_XML_FILE = "File is not in readable XML format!\n";
    public static final String SAMPLE_FILEPATH = " /Users/usernamehere/Desktop/entrybook.xml";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Opens given file to load entrybook data\n"
                                               + "Format: "
                                               + COMMAND_WORD + " /filepath/filename.xml\n" + "Example: " + COMMAND_WORD
                                               + SAMPLE_FILEPATH;

    public static final String[] VALID_PREFIXES = new String[] {};

    private final String filepath;

    public OpenCommand(String filePath) {
        this.filepath = filePath.trim();
    }

    /**
     * Executes the Open command if the file exists in this location.
     *
     * @throws CommandException
     */
    @Override
    public CommandResult execute() throws CommandException {
        if ((new File(filepath)).exists()) {
            try {
                model.openFilePath(filepath);
                return new CommandResult(MESSAGE_SUCCESS + filepath);
            } catch (IllegalValueException e) {
                throw new CommandException(MESSAGE_INVALID_XML_FILE);
            }
        } else {
            throw new CommandException(MESSAGE_FAILURE + MESSAGE_USAGE);
        }
    }
}
