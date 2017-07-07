package seedu.multitasky.logic.commands;

import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;

import java.util.List;
import java.util.Set;

import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

//@@author A0140633R
/**
 * Edits an entry identified using the type of entry followed by displayed index.
 */
public class EditByFindCommand extends EditCommand {
    public static final String MESSAGE_NO_ENTRIES = "No entries found! Please try again with different keywords.";

    public static final String MESSAGE_MULTIPLE_ENTRIES = "More than one entry found! \n"
            + "Use " + COMMAND_WORD + " " + PREFIX_FLOATINGTASK + " INDEX to specify which entry to edit.";

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
    public CommandResult execute() throws CommandException {
        List<ReadOnlyEntry> lastShownList = model.getFilteredFloatingTaskList();

        model.updateFilteredFloatingTaskList(keywords);
        if (model.getFilteredFloatingTaskList().size() == 1) {
            ReadOnlyEntry entryToEdit = lastShownList.get(0);
            Entry editedEntry = createEditedEntry(entryToEdit, editEntryDescriptor);
            try {
                model.updateEntry(entryToEdit, editedEntry);
            } catch (EntryNotFoundException pnfe) {
                throw new AssertionError("The target entry cannot be missing");
            }
            model.updateFilteredListToShowAll();
            return new CommandResult(String.format(MESSAGE_SUCCESS, entryToEdit));
        }
        if (model.getFilteredFloatingTaskList().size() >= 2) {
            return new CommandResult(MESSAGE_MULTIPLE_ENTRIES);
        } else {
            return new CommandResult(MESSAGE_NO_ENTRIES);
        }
    }
}
