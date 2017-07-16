package seedu.multitasky.logic.commands;

import java.io.File;

// @@author A0132788U
/**
 * Sets the file path to store/load entrybook data.
 */
public class SetCommand extends Command {

    public static final String COMMAND_WORD = "set";

    public static final String MESSAGE_SUCCESS = "File path set successfully to ";
    public static final String MESSAGE_FAILURE = "Invalid file path!\n";
    public static final String SAMPLE_FILEPATH = " /Users/usernamehere/Desktop/entrybook.xml";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets file path of the entrybook\n" + "Format: "
                                               + COMMAND_WORD + " newfilepath\n" + "Example: " + COMMAND_WORD
                                               + SAMPLE_FILEPATH;
    private final String newFilePath;

    public SetCommand(String newFilePath) {
        this.newFilePath = newFilePath.trim();
    }

    @Override
    public CommandResult execute() {
        if (isValidPath(newFilePath)) {
            model.changeFilePath(newFilePath);
            return new CommandResult(MESSAGE_SUCCESS + newFilePath);
        } else {
            return new CommandResult(MESSAGE_FAILURE + MESSAGE_USAGE);
        }
    }

    private boolean isValidPath(String newFilePath) {
        File parent = (new File(newFilePath)).getParentFile();
        return newFilePath.endsWith(".xml") && parent.canWrite() && parent != null;
    }
}
