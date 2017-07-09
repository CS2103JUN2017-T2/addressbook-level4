package seedu.multitasky.logic.commands;

import java.util.List;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

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

    public Index getIndex() {
        return index;
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

        model.updateFilteredListToShowAll();
        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToEdit));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls and other subclasses of EditCommands
        if (!(other instanceof EditByIndexCommand)) {
            return false;
        }

        // state check
        EditByIndexCommand e = (EditByIndexCommand) other;

        // check for index to edit
        if (!this.getIndex().equals(e.getIndex())) {
            return false;
        }

        // check equality in editEntryDescriptor.
        return editEntryDescriptor.equals(e.editEntryDescriptor);
    }

}
