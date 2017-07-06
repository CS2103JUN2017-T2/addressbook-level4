package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;

import java.util.Set;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;


//@@author A0140633R
/*
 * Deletes an entry identified using the type of entry followed by displayed.
 */
public class DeleteByFindCommand extends DeleteCommand {
    public static final String MESSAGE_DELETE_ENTRY_BY_FIND_FAIL = "More than one entry found! \n"
            + "Use " + COMMAND_WORD + " " + PREFIX_FLOATINGTASK + " INDEX to specify which entry to delete.";

    private Set<String> keywords;

    public DeleteByFindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() throws CommandException {
        UnmodifiableObservableList<ReadOnlyEntry> lastShownList = model.getFilteredFloatingTaskList();

        model.updateFilteredFloatingTaskList(keywords);
        if (model.getFilteredFloatingTaskList().size() == 1) {
            entryToDelete = lastShownList.get(0);
            try {
                model.deleteEntry(entryToDelete);
            } catch (EntryNotFoundException e) {
                assert false : "The target entry cannot be missing";
            }
            return new CommandResult(String.format(MESSAGE_DELETE_ENTRY_SUCCESS, entryToDelete));
        } else {
            return new CommandResult(MESSAGE_DELETE_ENTRY_BY_FIND_FAIL);
        }
    }
}
