package seedu.multitasky.logic.commands;

import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.storage.exception.NothingToRedoException;

//@@author A0132788U
/**
 * Redo a previous undo action.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_SUCCESS = "Redo previous undo action";
    public static final String MESSAGE_FAILURE = "Nothing to redo";

    public static final String[] VALID_PREFIXES = {};

    @Override
    public CommandResult execute() throws CommandException {
        try {
            model.redoPreviousAction();
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (NothingToRedoException e) {
            throw new CommandException(MESSAGE_FAILURE);
        } catch (Exception e) {
            throw new AssertionError("redo should not have other exceptions");
        }
    }

}
