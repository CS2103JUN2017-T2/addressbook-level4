package seedu.multitasky.logic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.multitasky.commons.core.EventsCenter;
import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.commons.events.ui.ListTypeUpdateEvent;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.model.entry.exceptions.EntryOverdueException;
import seedu.multitasky.model.entry.exceptions.OverlappingAndOverdueEventException;
import seedu.multitasky.model.entry.exceptions.OverlappingEventException;

/**
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

            CommandResult commandResult = null;

            try {
                model.changeEntryState(entryToRestore, Entry.State.ACTIVE);
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS, entryToRestore));
            } catch (EntryNotFoundException e) {
                assert false : "The target entry cannot be missing";
            } catch (OverlappingEventException oee) {
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_ALERT,
                                                                entryToRestore.getName()));
            } catch (EntryOverdueException e) {
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERDUE_ALERT,
                                                                entryToRestore.getName()));
            } catch (OverlappingAndOverdueEventException e) {
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_AND_OVERDUE_ALERT,
                                                                entryToRestore.getName()));
            }

            // refresh list view after updating.
            model.updateAllFilteredLists(history.getPrevKeywords(), history.getPrevStartDate(),
                                         history.getPrevEndDate(), history.getPrevState(),
                                         history.getPrevSearches());
            if (commandResult == null) {
                throw new AssertionError("commandResult in RestoreByFindCommand shouldn't be null here.");
            }
            return commandResult;

        } else {
            history.setPrevSearch(keywords, null, null, history.getPrevState());

            if (allList.size() >= 2) { // multiple entries found
                throw new CommandException(MESSAGE_MULTIPLE_ENTRIES);
            } else {
                assert (allList.size() == 0); // no entries found
                throw new CommandException(MESSAGE_NO_ENTRIES);
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

        // Filter and collate archived and deleted entries that matches keywords
        model.updateAllFilteredLists(keywords, null, null, Entry.State.ARCHIVED, Entry.State.DELETED,
                                     Model.STRICT_SEARCHES);
        raise(new ListTypeUpdateEvent("archive and bin"));
        allList.addAll(model.getFilteredDeadlineList());
        allList.addAll(model.getFilteredEventList());
        allList.addAll(model.getFilteredFloatingTaskList());

        return allList;
    }
    // @@author

    private void raise(BaseEvent event) {
        EventsCenter.getInstance().post(event);
    }
}
