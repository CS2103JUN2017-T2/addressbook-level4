package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;

/**
 * Adds an entry to the entry book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    // @@author A0140633R
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an entry to MultiTasky. " + "\n"
            + "Format: " + COMMAND_WORD + " NAME" + " ["
            + "[" + CliSyntax.PREFIX_BY + " DATE" + "]"
            + " |" + " [" + CliSyntax.PREFIX_FROM + " DATE" + " "
            + CliSyntax.PREFIX_TO + " DATE" + "]" + "]"
            + " [" + CliSyntax.PREFIX_TAG + " TAGS..." + "]" + "\n"
            + "All possible flags for Add : 'by', 'from', 'to', 'at', 'on', 'tag'";

    public static final String MESSAGE_SUCCESS = "New entry added:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s";

    private final ReadOnlyEntry toAdd;

    /**
     * Creates an AddCommand to add the specified {@code ReadOnlyEntry}
     */
    public AddCommand(ReadOnlyEntry entry) {
        requireNonNull(entry);
        toAdd = entry;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {
        requireNonNull(model);

        model.addEntry(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

}
