package seedu.multitasky.logic.commands;

import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.model.entry.ReadOnlyEntry;

// @@author A0140633R
/*
 * Abstract class that represents what Command word and confirmation messages a delete command will be using.
 */
public abstract class DeleteCommand extends Command {
    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " : Deletes the entry identified by keywords"
                                               + " if it is the only task found, or deletes the task "
                                               + "identified by the index number of the last"
                                               + " entry listing.\n" + "Format: " + COMMAND_WORD
                                               + " [keywords] or " + PREFIX_FLOATINGTASK + " INDEX"
                                               + " (must be a positive integer)\n"
                                               + "Example: " + COMMAND_WORD + " "
                                               + PREFIX_FLOATINGTASK + " 1";

    public static final String MESSAGE_SUCCESS = "New entry deleted:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s";

    protected ReadOnlyEntry entryToDelete;

}
