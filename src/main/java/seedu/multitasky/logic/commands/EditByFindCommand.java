package seedu.multitasky.logic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

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
                                                          + " INDEX to specify which entry to edit.";

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

        // update all 3 lists with new keywords.
        model.updateFilteredDeadlineList(keywords, Entry.State.ACTIVE);
        model.updateFilteredEventList(keywords, Entry.State.ACTIVE);
        model.updateFilteredFloatingTaskList(keywords, Entry.State.ACTIVE);

        // collate a combined list to measure how many entries are found.
        List<ReadOnlyEntry> allList = new ArrayList<>();
        allList.addAll(model.getFilteredDeadlineList());
        allList.addAll(model.getFilteredEventList());
        allList.addAll(model.getFilteredFloatingTaskList());

        if (allList.size() == 1) { // proceed to edit
            ReadOnlyEntry entryToEdit = allList.get(0);
            Entry editedEntry = createEditedEntry(entryToEdit, editEntryDescriptor);
            try {
                assert entryToEdit != null;
                assert editedEntry != null;
                if (entryToEdit.getClass().equals(editedEntry.getClass())) {
                    // editing within same type of entry
                    model.updateEntry(entryToEdit, editedEntry);
                } else { // moving entry from one list to another
                    model.deleteEntry(entryToEdit);
                    model.addEntry(editedEntry);
                }
                // refresh list view after updating
                model.updateFilteredDeadlineList(history.getPrevSearch(), history.getPrevState());
                model.updateFilteredEventList(history.getPrevSearch(), history.getPrevState());
                model.updateFilteredFloatingTaskList(history.getPrevSearch(), history.getPrevState());
            } catch (EntryNotFoundException pnfe) {
                throw new AssertionError("The target entry cannot be missing");
            }
            return new CommandResult(String.format(MESSAGE_SUCCESS, entryToEdit));
        } else {
            history.setPrevSearch(keywords, Entry.State.ACTIVE);
            if (allList.size() >= 2) {
                return new CommandResult(MESSAGE_MULTIPLE_ENTRIES);
            } else {
                assert allList.size() == 0;
                return new CommandResult(MESSAGE_NO_ENTRIES);
            }
        }
    }
}
