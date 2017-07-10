package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.ReadOnlyEntry;

/**
 * Adds an entry to the entry book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    // @@author A0140633R
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an entry to the entry book. " + "\n"
                                               + "Format: " + COMMAND_WORD + " NAME " + "[" + PREFIX_TAG
                                               + " TAGS...]\n"
                                               + "Example: " + COMMAND_WORD + " " + "dinner with friends "
                                               + PREFIX_TAG + "friends " + "owes_money";

    public static final String MESSAGE_SUCCESS = "New entry added:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s";

    private final Entry toAdd;

    /**
     * Creates an AddCommand to add the specified {@code ReadOnlyEntry}
     */
    public AddCommand(ReadOnlyEntry entry) {
        toAdd = new FloatingTask(entry);
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);

        model.addEntry(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

}
