package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.entry.Entry;

// @@author A0126623L
/**
 * Clears the entry book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Clear entries in the entry book. " + "\n"
                                               + "Format: " + COMMAND_WORD
                                               + " [" + CliSyntax.PREFIX_ARCHIVE + " | "
                                               + CliSyntax.PREFIX_BIN + " | "
                                               + CliSyntax.PREFIX_ALL + "]" + "\n"
                                               + "Example: " + COMMAND_WORD + " " + CliSyntax.PREFIX_BIN;
    public static final String MESSAGE_ALL_SUCCESS = "Entry book has been cleared!";
    public static final String MESSAGE_ACTIVE_SUCCESS = "Active entries have been cleared!";
    public static final String MESSAGE_ARCHIVE_SUCCESS = "Archive has been cleared!";
    public static final String MESSAGE_BIN_SUCCESS = "Bin has been cleared!";

    public static final int NUMBER_ALLOWABLE_PREFIX = 1;

    public static final String[] VALID_PREFIXES = { CliSyntax.PREFIX_ACTIVE.toString(),
        CliSyntax.PREFIX_ARCHIVE.toString(),
        CliSyntax.PREFIX_BIN.toString(),
        CliSyntax.PREFIX_ALL.toString() };

    public enum ClearType {
        ACTIVE, ARCHIVE, BIN, ALL
    }

    private ClearType clearType;

    public ClearCommand() {
        clearType = ClearType.ACTIVE;
    }

    // @@author A0126623L
    public ClearCommand(String prefix) {
        if (prefix.equals(CliSyntax.PREFIX_ALL.toString())) {
            clearType = ClearType.ALL;
        } else if (prefix.equals(CliSyntax.PREFIX_ARCHIVE.toString())) {
            clearType = ClearType.ARCHIVE;
        } else if (prefix.equals(CliSyntax.PREFIX_BIN.toString())) {
            clearType = ClearType.BIN;
        } else if (prefix.equals(CliSyntax.PREFIX_ACTIVE.toString())) {
            clearType = ClearType.ACTIVE;
        } else {
            throw new AssertionError("ClearCommand constructor shouldn't reach this point.");
        }
    }
    // @@author

    // @@author A0126623L
    @Override
    public CommandResult execute() {
        requireNonNull(model);

        switch (clearType) {
        case ALL:
            model.resetData(new EntryBook());
            return new CommandResult(MESSAGE_ALL_SUCCESS);
        case ACTIVE:
            model.clearStateSpecificEntries(Entry.State.ACTIVE);
            return new CommandResult(MESSAGE_ACTIVE_SUCCESS);
        case ARCHIVE:
            model.clearStateSpecificEntries(Entry.State.ARCHIVED);
            return new CommandResult(MESSAGE_ARCHIVE_SUCCESS);
        case BIN:
            model.clearStateSpecificEntries(Entry.State.DELETED);
            return new CommandResult(MESSAGE_BIN_SUCCESS);
        default:
            throw new AssertionError("Unknown clear command option given.");
        }
    }
    // @@author
}
