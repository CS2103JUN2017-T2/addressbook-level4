package seedu.multitasky.logic.commands;

import java.io.File;

// @@author A0132788U
/**
 * Loads the XML data at a given filepath.
 */

public class LoadCommand extends Command {

    public static final String COMMAND_WORD = "load";

    public static final String MESSAGE_SUCCESS = "Loaded data from ";
    public static final String MESSAGE_FAILURE = "File does not exist!\n";
    public static final String SAMPLE_FILEPATH = " /Users/usernamehere/Desktop/entrybook.xml";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Loads entrybook data at given filepath\n" + "Format: "
                                               + COMMAND_WORD + " filepath\n" + "Example: " + COMMAND_WORD
                                               + SAMPLE_FILEPATH;
    private final String newFilePath;

    public LoadCommand(String newFilePath) {
        this.newFilePath = newFilePath.trim();
    }

    /**
     * Executes the Load command if the file already exists in this location.
     */
    @Override
    public CommandResult execute() {
        if ((new File(newFilePath)).exists()) {
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_FAILURE + MESSAGE_USAGE);
        }
    }
}
