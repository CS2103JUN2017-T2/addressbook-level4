package seedu.multitasky.logic.commands;

import java.util.Objects;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.ReadOnlyEntry;

//@@author A0126623L-reused
/*
* Abstract class that represents a restore command. Holds command_word and confirmation messages a restore
* command will be using.
*/
public abstract class RestoreCommand extends Command {

    public static final String COMMAND_WORD = "restore";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " : Restores an archived/deleted entry identified"
            + " by keywords if it is the only entry found, or restores the entry identified by the index number of"
            + " the last archived/deleted entry listing.\n"
            + "Format: " + COMMAND_WORD + " [" + "[" + "KEYWORDS" + "]" + " |"
            + " [" + String.join(" | ", CliSyntax.PREFIX_EVENT.toString(), CliSyntax.PREFIX_DEADLINE.toString(),
            CliSyntax.PREFIX_FLOATINGTASK.toString()) + "]" + " INDEX" + "]" + "\n"
            + "All possible flags for Restore : 'event', 'deadline', 'float'";

    public static final String MESSAGE_SUCCESS = "Entry restored:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
                                                 + "Entry has been restored to active list";

    public static final String MESSAGE_SUCCESS_WITH_OVERLAP_ALERT = "Entry restored:" + "\n"
            + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
            + "Alert: Restored entry %1$s overlaps with existing event(s).";
    public static final String MESSAGE_SUCCESS_WITH_OVERDUE_ALERT = "Entry restored:" + "\n"
            + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
            + "Alert: Restored entry %1$s is overdue.";
    public static final String MESSAGE_SUCCESS_WITH_OVERLAP_AND_OVERDUE_ALERT = "Entry restored:" + "\n"
            + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
            + "Alert: Restored entry %1$s is overdue and overlaps with existing event(s).";

    public static final String MESSAGE_ENTRY_ALREADY_ACTIVE = "The provided entry is already active.";

    public static final String[] VALID_PREFIXES = {CliSyntax.PREFIX_EVENT.toString(),
                                                   CliSyntax.PREFIX_DEADLINE.toString(),
                                                   CliSyntax.PREFIX_FLOATINGTASK.toString()};

    protected ReadOnlyEntry entryToRestore;

    @Override
    public void setData(Model model, CommandHistory history) {
        //TODO: Consider refactoring these to the superclass's setData();
        Objects.requireNonNull(history);
        Objects.requireNonNull(model);
        this.model = model;
        this.history = history;
    }

}
