package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;

//@@author A0140633R
/**
 * Edits an entry identified using the type of entry followed by displayed index.
 */
public class EditByIndexCommand extends EditCommand {

    private final Index index;
    /**
     * @param index of the entry in the filtered entry list to edit
     * @param editEntryDescriptor details to edit the entry with
     */
    public EditByIndexCommand(Index index, EditEntryDescriptor editEntryDescriptor) {
        super(editEntryDescriptor);
        this.index = index;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyEntry> lastShownList = model.getFilteredFloatingTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        ReadOnlyEntry entryToEdit = lastShownList.get(index.getZeroBased());
        Entry editedEntry = createEditedEntry(entryToEdit, editEntryDescriptor);

        try {
            model.updateEntry(entryToEdit, editedEntry);
        } catch (EntryNotFoundException pnfe) {
            throw new AssertionError("The target entry cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToEdit));
    }
}
