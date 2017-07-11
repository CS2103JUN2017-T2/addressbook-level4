package seedu.multitasky.logic.commands;

/**
 * Lists all entries in the entry book to the user.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_SUCCESS = "Undo previous action";

    @Override
    public CommandResult execute() {
        model.undoPreviousAction();
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
