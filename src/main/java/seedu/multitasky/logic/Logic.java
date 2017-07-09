package seedu.multitasky.logic;

import javafx.collections.ObservableList;
import seedu.multitasky.logic.commands.CommandResult;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.entry.ReadOnlyEntry;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText
     *            The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException
     *             If an error occurs during command execution.
     * @throws ParseException
     *             If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    // @@author A0140633R
    /** Returns the filtered list of floating tasks */
    ObservableList<ReadOnlyEntry> getFilteredFloatingTaskList();
}
