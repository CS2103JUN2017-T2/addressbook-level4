package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.ReadOnlyEntry;

// @@author A0140633R
/*
 * Abstract class that represents a delete command. Holds command_word and confirmation messages a delete
 * command will be using.
 */
public abstract class DeleteCommand extends Command {
    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " : Deletes the entry identified by keywords"
            + " if it is the only entry found, or deletes the entry identified by the index number of the last"
            + " entry listing.\n"
            + "Format: " + COMMAND_WORD + " [" + "[" + "KEYWORDS" + "]" + " |"
            + " [" + String.join(" | ", CliSyntax.PREFIX_EVENT.toString(), CliSyntax.PREFIX_DEADLINE.toString(),
            CliSyntax.PREFIX_FLOATINGTASK.toString()) + "]" + " INDEX" + "]" + "\n"
            + "All possible flags for Delete : 'event', 'deadline', 'float'";

    public static final String MESSAGE_SUCCESS = "Entry deleted:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
                                                 + "Entry has been moved to bin";

    public static final String[] VALID_PREFIXES = {CliSyntax.PREFIX_EVENT.toString(),
                                                   CliSyntax.PREFIX_DEADLINE.toString(),
                                                   CliSyntax.PREFIX_FLOATINGTASK.toString()};

    protected ReadOnlyEntry entryToDelete;

    @Override
    public void setData(Model model, CommandHistory history) {
        requireNonNull(history);
        requireNonNull(model);
        this.model = model;
        this.history = history;
    }

}
