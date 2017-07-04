package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ENTRYBOOK_EVENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENTRYBOOK_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENTRYBOOK_FLOATINGTASK;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;

//@@kevinlamkb A0140633R
/**
 * Deletes an entry identified using the type of entry followed by displayed index from the last displayed list
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the entry identified by the index number used in the last entry listing.\n"
            + "Parameters: /float (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + PREFIX_ENTRYBOOK_FLOATINGTASK + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    
    
    public final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult execute() throws CommandException {

        UnmodifiableObservableList<ReadOnlyEntry> lastShownList = model.getFilteredFloatingTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        ReadOnlyEntry entryToDelete = lastShownList.get(targetIndex.getZeroBased());

        try {
            model.deleteEntry(entryToDelete);
        } catch (EntryNotFoundException pnfe) {
            assert false : "The target entry cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, entryToDelete));
    }

}
