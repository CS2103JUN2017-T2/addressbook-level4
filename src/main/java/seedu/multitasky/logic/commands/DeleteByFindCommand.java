package seedu.multitasky.logic.commands;

import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

// @@author A0140633R
/*
 * Finds entries from given keywords and deletes entry if it is the only one found.
 */
public class DeleteByFindCommand extends DeleteCommand {
    public static final String MESSAGE_NO_ENTRIES = "No entries found! Please try again "
                                                    + "with different keywords.";

    public static final String MESSAGE_MULTIPLE_ENTRIES = "More than one entry found! \n"
                                                          + "Use " + COMMAND_WORD + " [ "
                                                          + String.join(" , ", PREFIX_EVENT.toString(),
                                                                        PREFIX_DEADLINE.toString(),
                                                                        PREFIX_FLOATINGTASK.toString())
                                                          + " ]"
                                                          + " INDEX to specify which entry to delete.";

    // TODO find out how to bring this message into the ui window
    public static final String MESSAGE_AFTER_KEYWORD_DELETE = "\nOne entry found and deleted! Listing all entries now.";

    private Set<String> keywords;

    public DeleteByFindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() throws CommandException {

        // update all 3 lists with new keywords.
        model.updateFilteredDeadlineList(keywords);
        model.updateFilteredEventList(keywords);
        model.updateFilteredFloatingTaskList(keywords);

        // find out whether only 1 entry is found.
        List<ReadOnlyEntry> tempAllList = new ArrayList<>();
        tempAllList.addAll(model.getFilteredDeadlineList());
        tempAllList.addAll(model.getFilteredEventList());
        tempAllList.addAll(model.getFilteredFloatingTaskList());

        if (tempAllList.size() == 1) {
            entryToDelete = tempAllList.get(0);
            try {
                model.deleteEntry(entryToDelete);
            } catch (EntryNotFoundException e) {
                assert false : "The target entry cannot be missing";
            }
            model.updateAllFilteredListToShowAll();
            return new CommandResult(String.format(MESSAGE_SUCCESS, entryToDelete));
        } else {
            if (tempAllList.size() >= 2) {
                return new CommandResult(MESSAGE_MULTIPLE_ENTRIES);
            } else {
                assert (tempAllList.size() == 0);
                return new CommandResult(MESSAGE_NO_ENTRIES);
            }
        }
    }

}
