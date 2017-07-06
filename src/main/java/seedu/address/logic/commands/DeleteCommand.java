package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ENTRYBOOK_FLOATINGTASK;

import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;

//@@author A0140633R
/*
 * Deletes an entry identified using the type of entry followed by displayed.
 */
public class DeleteCommand extends Command {
    public static final String COMMAND_WORD = "delete";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the entry identified by keywords if it is the only task found, "
            + "or deletes the task identified by the index number of the last entry listing.\n"
            + "Format: delete + [keywords] or " + PREFIX_ENTRYBOOK_FLOATINGTASK
            + " INDEX (must be a positive integer)\n" + "Example: " + COMMAND_WORD
            + " " + PREFIX_ENTRYBOOK_FLOATINGTASK
            + " 1";

    public static final String MESSAGE_DELETE_ENTRY_SUCCESS = "Deleted Entry: %1$s";

    private Index targetIndex;
    private boolean doSearch = false;
    private Set<String> keywords;
    private boolean doDelete = false;

    public DeleteCommand(Index targetIndex, Set<String> keywords) {
        this.targetIndex = targetIndex;
        this.keywords = keywords;
    }

    /*
     * adjusts doSearch / doDelete variables in the DeleteCommand to switch the types of execute() it will carry out.
     */
    public void checkDeleteCommand(Model model) {
        if (keywords.size() >= 1) {
            model.updateFilteredFloatingTaskList(keywords);
            this.doSearch = true;
            if (model.getFilteredFloatingTaskList().size() == 1) {
                this.doDelete = true;
            }
        }
    }

    @Override
    public CommandResult execute() throws CommandException {
        UnmodifiableObservableList<ReadOnlyEntry> lastShownList = model.getFilteredFloatingTaskList();
        checkDeleteCommand(model);
        if (!doSearch) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
            }
            ReadOnlyEntry entryToDelete = lastShownList.get(targetIndex.getZeroBased());
            try {
                model.deleteEntry(entryToDelete);
            } catch (EntryNotFoundException pnfe) {
                assert false : "The target entry cannot be missing";
            }
            return new CommandResult(String.format(MESSAGE_DELETE_ENTRY_SUCCESS, entryToDelete));
        } else {
            if (doDelete) {
                ReadOnlyEntry entryToDelete = lastShownList.get(0);
                try {
                    model.deleteEntry(entryToDelete);
                } catch (EntryNotFoundException e) {
                    assert false : "The target entry cannot be missing";
                }
                return new CommandResult(String.format(MESSAGE_DELETE_ENTRY_SUCCESS, entryToDelete));
            } else {
                model.updateFilteredFloatingTaskList(keywords);
                return new CommandResult(getMessageForEntryListShownSummary(lastShownList.size()));
            }
        }
    }

}
