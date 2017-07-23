package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.ReadOnlyEntry;

// @@author A0132788U-reused
/**
 * Abstract class that represents a complete command. It contains the command keyword, usage instructions,
 * prefixes and the success message.
 */
public abstract class CompleteCommand extends Command {
    public static final String COMMAND_WORD = "complete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
                                               + " : Completes the entry identified by keywords in the active list"
                                               + " if it is the only entry found, or completes the entry identified"
                                               + " by the index number of the last entry listing"
                                               + " and moves it to archive.\n"
                                               + "Format: " + COMMAND_WORD + " <" + "KEYWORDS" + ">" + " |"
                                               + " <<" + String.join(" | ", CliSyntax.PREFIX_EVENT.toString(),
                                                       CliSyntax.PREFIX_DEADLINE.toString(),
                                                       CliSyntax.PREFIX_FLOATINGTASK.toString())
                                               + ">" + " INDEX" + ">" + "\n"
                                               + "All possible flags for Complete : 'event', 'deadline', 'float'";

    public static final String MESSAGE_SUCCESS = "Entry completed:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
                                                 + "Entry has been moved to archive";

    public static final String[] VALID_PREFIXES = { CliSyntax.PREFIX_EVENT.toString(),
                                                    CliSyntax.PREFIX_DEADLINE.toString(),
                                                    CliSyntax.PREFIX_FLOATINGTASK.toString() };

    protected ReadOnlyEntry entryToComplete;

    @Override
    public void setData(Model model, CommandHistory history) {
        requireNonNull(history);
        requireNonNull(model);
        this.model = model;
        this.history = history;
    }

}
