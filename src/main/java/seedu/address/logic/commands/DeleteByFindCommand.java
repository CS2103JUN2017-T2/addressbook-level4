package seedu.address.logic.commands;

import java.util.Set;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;


//@@author A0140633R
/*
 * Finds entries from given keywords and deletes entry if it is the only one found.
 */
public class DeleteByFindCommand extends DeleteCommand {
    public static final String MESSAGE_NO_ENTRIES = "No entries found! Please try again with different keywords.";
    public static final String MESSAGE_MULTIPLE_ENTRIES = "More than one entry found! \n"
            + "Use delete /float INDEX to specify which entry to delete.";

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
            if (model.getFilteredFloatingTaskList().size() >= 2) {
                return new CommandResult(String.format(MESSAGE_MULTIPLE_ENTRIES, entryToDelete));
            } else {
                return new CommandResult(String.format(MESSAGE_NO_ENTRIES, entryToDelete));
            }
        }
    }
}
