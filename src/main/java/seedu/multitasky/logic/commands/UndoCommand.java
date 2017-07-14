package seedu.multitasky.logic.commands;

import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.storage.exception.NothingToUndoException;

//@@author A0132788U
/**
 * Undo a previous data-mutating action in the entrybook.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_SUCCESS = "Undo previous action";
    public static final String MESSAGE_FAILURE = "Nothing to undo";

    public static final String[] VALID_PREFIXES = {};

    @Override
    public CommandResult execute() throws CommandException {
        try {
            model.undoPreviousAction();
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (NothingToUndoException e) {
            return new CommandResult(MESSAGE_FAILURE);
        } catch (Exception e) {
            throw new CommandException(MESSAGE_FAILURE);
        }
    }

}
