package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;

import seedu.address.commons.core.Messages;
import seedu.address.model.entry.ReadOnlyEntry;

//@@author A0140633R
/*
 * Abstract class that carries message format for all of the sub-types of delete commands.
 */
public abstract class DeleteCommand extends Command {
    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " : Deletes the entry identified by keywords"
            + " if it is the only task found, or deletes the task identified by the index number of the last"
            + " entry listing.\n"
            + "Format: " + COMMAND_WORD + " [keywords] or " + PREFIX_FLOATINGTASK + " INDEX"
            + " (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_FLOATINGTASK + " 1";

    public static final String MESSAGE_SUCCESS_DELETE = "New entry deleted:" + "\n"
                                    + Messages.MESSAGE_ENTRY_DESCRIPTION +  "%1$s";

    protected ReadOnlyEntry entryToDelete;

}
