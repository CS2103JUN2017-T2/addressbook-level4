package seedu.multitasky.logic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

// @@author A0140633R
/*
 * Finds entries from given keywords and deletes entry if it is the only one found.
 */
public class DeleteByFindCommand extends DeleteCommand {
    public static final String MESSAGE_NO_ENTRIES = "No entries found! Please try again with different keywords";

    public static final String MESSAGE_MULTIPLE_ENTRIES = "More than one entry found! \n"
                                                          + "Use " + COMMAND_WORD + " ["
                                                          + String.join(" | ",
                                                                        CliSyntax.PREFIX_EVENT.toString(),
                                                                        CliSyntax.PREFIX_DEADLINE.toString(),
                                                                        CliSyntax.PREFIX_FLOATINGTASK.toString())
                                                          + "]"
                                                          + " INDEX to specify which entry to delete.";

    public static final String MESSAGE_SUCCESS = "Entry deleted:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
                                                 + "One entry found and deleted! Listing all entries now.";

    private Set<String> keywords;

    public DeleteByFindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {

        // update all 3 lists with new keywords.
        model.updateFilteredDeadlineList(keywords, Entry.State.ACTIVE);
        model.updateFilteredEventList(keywords, Entry.State.ACTIVE);
        model.updateFilteredFloatingTaskList(keywords, Entry.State.ACTIVE);

        // collate a combined list to measure how many entries are found.
        List<ReadOnlyEntry> allList = new ArrayList<>();
        allList.addAll(model.getFilteredDeadlineList());
        allList.addAll(model.getFilteredEventList());
        allList.addAll(model.getFilteredFloatingTaskList());

        if (allList.size() == 1) { // proceed to delete
            entryToDelete = allList.get(0);
            try {
                model.changeEntryState(entryToDelete, Entry.State.DELETED);
            } catch (EntryNotFoundException e) {
                assert false : "The target entry cannot be missing";
            }
            // refresh list view after updating.
            model.updateFilteredDeadlineList(history.getPrevSearch(), history.getPrevState());
            model.updateFilteredEventList(history.getPrevSearch(), history.getPrevState());
            model.updateFilteredFloatingTaskList(history.getPrevSearch(), history.getPrevState());
            // set search terms to what i searched with in this rendition
            history.setNextSearch(keywords, Entry.State.ACTIVE);

            return new CommandResult(String.format(MESSAGE_SUCCESS, entryToDelete));
        } else {
            // save what search i did
            history.setNextSearch(keywords, Entry.State.ACTIVE);
            if (allList.size() >= 2) { // multiple entries found
                return new CommandResult(MESSAGE_MULTIPLE_ENTRIES);
            } else {
                assert (allList.size() == 0); // no entries found
                return new CommandResult(MESSAGE_NO_ENTRIES);
            }
        }
    }

}
