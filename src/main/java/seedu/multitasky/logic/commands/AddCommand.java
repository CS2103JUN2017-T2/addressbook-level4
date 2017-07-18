package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryOverdueException;
import seedu.multitasky.model.entry.exceptions.OverlappingAndOverdueEventException;
import seedu.multitasky.model.entry.exceptions.OverlappingEventException;

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
            + "All possible flags for Add : 'by', 'from', 'to', 'at', 'tag'";

    public static final String MESSAGE_SUCCESS = "New entry added:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s";

    public static final String MESSAGE_SUCCESS_WITH_OVERLAP_ALERT = "New entry added:" + "\n"
            + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
            + "Alert: New entry overlaps with existing event(s).";
    public static final String MESSAGE_SUCCESS_WITH_OVERDUE_ALERT = "New entry added:" + "\n"
            + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
            + "Alert: New entry is overdue.";
    public static final String MESSAGE_SUCCESS_WITH_OVERLAP_AND_OVERDUE_ALERT = "New entry added:" + "\n"
            + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
            + "Alert: New entry is overdue and overlaps with existing event(s).";

    public static final String[] VALID_PREFIXES = {CliSyntax.PREFIX_FROM.toString(),
                                                   CliSyntax.PREFIX_BY.toString(),
                                                   CliSyntax.PREFIX_AT.toString(),
                                                   CliSyntax.PREFIX_ON.toString(),
                                                   CliSyntax.PREFIX_TO.toString(),
                                                   CliSyntax.PREFIX_TAG.toString()};

    public static final String MESSAGE_INVALID_CONFIG_DURATION = "default addDuration cannot be zero or negative!";

    public static final String MESSAGE_ENDDATE_BEFORE_STARTDATE = "Can not have end date before start date!";

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

        try {
            model.addEntry(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (OverlappingEventException oee) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_ALERT, toAdd.getName()));
        } catch (EntryOverdueException e) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERDUE_ALERT, toAdd.getName()));
        } catch (OverlappingAndOverdueEventException e) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_AND_OVERDUE_ALERT, toAdd.getName()));
        }
    }

}
