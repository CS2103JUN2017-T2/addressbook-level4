package seedu.multitasky.logic;

import javafx.collections.ObservableList;
import seedu.multitasky.logic.commands.CommandResult;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText the command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException if an error occurs during command execution.
     * @throws ParseException if an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException,
            DuplicateEntryException;

    /** Returns the filtered list of floating tasks */
    ObservableList<ReadOnlyEntry> getFilteredFloatingTaskList();

    /** Returns the filtered list of deadlines */
    ObservableList<ReadOnlyEntry> getFilteredDeadlineList();

    /** Returns the filtered list of events */
    ObservableList<ReadOnlyEntry> getFilteredEventList();
}
