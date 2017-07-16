package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.multitasky.model.EntryBook;

/**
 * Clears the entry book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Entry book has been cleared!";

    public static final String[] VALID_PREFIXES = {};

    @Override
    public CommandResult execute() {
        requireNonNull(model);

        model.resetData(new EntryBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
