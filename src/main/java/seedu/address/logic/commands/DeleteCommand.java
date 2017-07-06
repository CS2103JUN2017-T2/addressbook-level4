package seedu.address.logic.commands;

import seedu.address.model.entry.ReadOnlyEntry;

//@@author A0140633R
/*
 * Abstract class that carries message format for all of the sub-types of delete commands.
 */
public abstract class DeleteCommand extends Command {
    public static final String COMMAND_WORD = "delete";
    public static final String MESSAGE_USAGE = "delete"
            + " : Deletes the entry identified by keywords if it is the only task found, "
            + "or deletes the task identified by the index number of the last entry listing.\n"
            + "Format: delete [keywords] or /float INDEX (must be a positive integer)\n"
            + "Example: delete /float 1";

    public static final String MESSAGE_DELETE_ENTRY_SUCCESS = "Deleted Entry: %1$s";

    protected ReadOnlyEntry entryToDelete;

}
