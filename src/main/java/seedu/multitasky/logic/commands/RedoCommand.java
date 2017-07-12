package seedu.multitasky.logic.commands;

import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.storage.exception.NothingToRedoException;

//@@author A0132788U
/**
 * Lists all entries in the entry book to the user.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_SUCCESS = "Redo previous undo action";
    public static final String MESSAGE_FAILURE = "Nothing to redo";

    @Override
    public CommandResult execute() throws CommandException {
        try {
            model.redoPreviousAction();
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (NothingToRedoException e) {
            return new CommandResult(MESSAGE_FAILURE);
        } catch (Exception e) {
            throw new CommandException(MESSAGE_FAILURE);
        }
    }

}
