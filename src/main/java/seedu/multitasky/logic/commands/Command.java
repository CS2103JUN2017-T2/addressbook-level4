package seedu.multitasky.logic.commands;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;
    protected CommandHistory history;

    /**
     * Constructs a feedback message to summarize an operation that displayed a listing of entries.
     *
     * @param displaySize used to generate summary
     * @return summary message for entries displayed
     */
    public static String getMessageForEntryListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_ENTRIES_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute() throws CommandException;

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setData(Model model, CommandHistory history) {
        this.model = model;
    }
}
