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

/*
* Finds entries from given keywords and restores entry if it is the only one found.
*/
public class RestoreByFindCommand extends RestoreCommand {

    public static final String MESSAGE_NO_ENTRIES = "No entries found! Please try again with different keywords";

    public static final String MESSAGE_MULTIPLE_ENTRIES = "More than one entry found! \n"
                                                          + "Use " + COMMAND_WORD + " ["
                                                          + String.join(" | ",
                                                                        CliSyntax.PREFIX_EVENT.toString(),
                                                                        CliSyntax.PREFIX_DEADLINE.toString(),
                                                                        CliSyntax.PREFIX_FLOATINGTASK.toString())
                                                          + "]"
                                                          + " INDEX to specify which entry to restore.";

    public static final String MESSAGE_SUCCESS = "Entry restored:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
                                                 + "One entry found and restored! Listing all entries now.";

    private Set<String> keywords;

    public RestoreByFindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    // @@author A0126623L-reused
    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {

        List<ReadOnlyEntry> allList = collateArchivedAndDeletedEntries();

        if (allList.size() == 1) { // proceed to restore
            entryToRestore = allList.get(0);

            if (entryToRestore.isActive()) {
                throw new CommandException(RestoreCommand.MESSAGE_ENTRY_ALREADY_ACTIVE);
            }

            try {
                model.changeEntryState(entryToRestore, Entry.State.ACTIVE);
            } catch (EntryNotFoundException e) {
                assert false : "The target entry cannot be missing";
            }

            /**
             * TODO: Possible refactoring of the following 4 lines of code to a utility
             * method since it's done by DeleteCommand, EditCommand, CompleteCommand,
             * and RestoreCommand.
             */
            // refresh list view after updating.
            model.updateFilteredDeadlineList(history.getPrevSearch(), null,
                                             null, history.getPrevState());
            model.updateFilteredEventList(history.getPrevSearch(), null,
                                             null, history.getPrevState());
            model.updateFilteredFloatingTaskList(history.getPrevSearch(), null,
                                                 null, history.getPrevState());

            history.setPrevSearch(keywords, history.getPrevState());

            return new CommandResult(String.format(MESSAGE_SUCCESS, entryToRestore));
        } else {
            history.setPrevSearch(keywords, history.getPrevState());

            if (allList.size() >= 2) { // multiple entries found
                return new CommandResult(MESSAGE_MULTIPLE_ENTRIES);
            } else {
                assert (allList.size() == 0); // no entries found
                return new CommandResult(MESSAGE_NO_ENTRIES);
            }
        }
    }
    // @@author

    // @@author A0126623L
    /**
     * Collates all archived and deleted entries that matches search keywords.
     * @return List of matched entries
     */
    private List<ReadOnlyEntry> collateArchivedAndDeletedEntries() {

        List<ReadOnlyEntry> allList = new ArrayList<>();
        // Filter and collate archived entries that matches keywords
        model.updateFilteredDeadlineList(keywords, Entry.State.ARCHIVED);
        model.updateFilteredEventList(keywords, Entry.State.ARCHIVED);
        model.updateFilteredFloatingTaskList(keywords, Entry.State.ARCHIVED);
        allList.addAll(model.getFilteredDeadlineList());
        allList.addAll(model.getFilteredEventList());
        allList.addAll(model.getFilteredFloatingTaskList());

        // Filter and collate deleted entries that matches keywords
        model.updateFilteredDeadlineList(keywords, Entry.State.DELETED);
        model.updateFilteredEventList(keywords, Entry.State.DELETED);
        model.updateFilteredFloatingTaskList(keywords, Entry.State.DELETED);
        allList.addAll(model.getFilteredDeadlineList());
        allList.addAll(model.getFilteredEventList());
        allList.addAll(model.getFilteredFloatingTaskList());

        return allList;
    }
    // @@author

}
