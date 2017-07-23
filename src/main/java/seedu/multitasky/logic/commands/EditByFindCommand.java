package seedu.multitasky.logic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

// @@author A0140633R
/**
 * Edits an entry identified using the type of entry followed by displayed index.
 */
public class EditByFindCommand extends EditCommand {
    public static final String MESSAGE_NO_ENTRIES = "No entries found! Please try again with different keywords";

    public static final String MESSAGE_MULTIPLE_ENTRIES = "More than one entry found! \n"
                                                          + "Use " + COMMAND_WORD + " ["
                                                          + String.join(" | ",
                                                                        CliSyntax.PREFIX_EVENT.toString(),
                                                                        CliSyntax.PREFIX_DEADLINE.toString(),
                                                                        CliSyntax.PREFIX_FLOATINGTASK.toString())
                                                          + "]"
                                                          + " INDEX to specify which entry to edit. \n"
                                                          + "The edit details have been saved for next edit try";

    private Set<String> keywords;

    /**
     * @param index of the entry in the filtered entry list to edit
     * @param editEntryDescriptor details to edit the entry with
     */
    public EditByFindCommand(Set<String> keywords, EditEntryDescriptor editEntryDescriptor) {
        super(editEntryDescriptor);
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {
        List<ReadOnlyEntry> allList = getListAfterSearch();

        if (allList.size() == 1) { // proceed to edit
            ReadOnlyEntry entryToEdit = allList.get(0);
            String targetEntryString = entryToEdit.toString();
            Entry editedEntry = createEditedEntry(entryToEdit, editEntryDescriptor);

            CommandResult commandResult = null;
            try {
                assert entryToEdit != null;
                assert editedEntry != null;

                model.updateEntry(entryToEdit, editedEntry);
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS, targetEntryString,
                                                                editedEntry));
            } catch (EntryNotFoundException pnfe) {
                throw new AssertionError("The target entry cannot be missing");
            } catch (OverlappingEventException oee) {
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_ALERT,
                                                                targetEntryString, editedEntry));
            } catch (EntryOverdueException e) {
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERDUE_ALERT,
                                                                targetEntryString, editedEntry));
            } catch (OverlappingAndOverdueEventException e) {
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_AND_OVERDUE_ALERT,
                                                                targetEntryString, editedEntry));
            }
            revertListView();
            assert commandResult != null : "commandResult in EditByFindCommand shouldn't be null here.";
            return commandResult;

        } else {
            history.setPrevSearch(keywords, null, null, Entry.State.ACTIVE);
            history.setEditHistory(editEntryDescriptor);
            if (allList.size() >= 2) {
                throw new CommandException(MESSAGE_MULTIPLE_ENTRIES);
            } else {
                assert allList.size() == 0;
                throw new CommandException(MESSAGE_NO_ENTRIES);
            }
        }
    }

    /**
     * reverts inner lists in model to how they were before by using command history
     */
    private void revertListView() {
        model.updateAllFilteredLists(history.getPrevKeywords(), history.getPrevStartDate(),
                                     history.getPrevEndDate(), history.getPrevState(),
                                     history.getPrevSearches());
    }

    /**
     * updates inner lists with new keyword terms and returns a collated list with all found entries.
     */
    private List<ReadOnlyEntry> getListAfterSearch() {
        model.updateAllFilteredLists(keywords, null, null, Entry.State.ACTIVE, Model.STRICT_SEARCHES);

        List<ReadOnlyEntry> allList = new ArrayList<>();
        allList.addAll(model.getFilteredDeadlineList());
        allList.addAll(model.getFilteredEventList());
        allList.addAll(model.getFilteredFloatingTaskList());
        return allList;
    }

}
