package seedu.multitasky.logic.commands;

import java.util.List;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.ParserUtil;
import seedu.multitasky.logic.parser.Prefix;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

// @@author A0140633R
/**
 * Edits an entry identified using the type of entry followed by displayed index.
 */
public class EditByIndexCommand extends EditCommand {
    private final Index index;
    private final Prefix listIndicatorPrefix;

    /**
     * @param index of the entry in the filtered entry list to edit
     * @param editEntryDescriptor details to edit the entry with
     */
    public EditByIndexCommand(Index index, Prefix listIndicatorPrefix,
            EditEntryDescriptor editEntryDescriptor) {
        super(editEntryDescriptor);
        this.index = index;
        this.listIndicatorPrefix = listIndicatorPrefix;
    }

    public Index getIndex() {
        return index;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {
        List<ReadOnlyEntry> listToEditFrom = ParserUtil.getListIndicatedByPrefix(model, listIndicatorPrefix);
        assert listToEditFrom != null;

        if (index.getZeroBased() >= listToEditFrom.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        ReadOnlyEntry entryToEdit = listToEditFrom.get(index.getZeroBased());
        Entry editedEntry = createEditedEntry(entryToEdit, editEntryDescriptor);

        try {
            model.updateEntry(entryToEdit, editedEntry);
            // TODO remove once confirmed we really dont need this any more.
            /*model.updateFilteredEventList(new HashSet<>(Arrays.asList("change filter")),
                                          Entry.State.ACTIVE);
            model.updateFilteredDeadlineList(new HashSet<>(Arrays.asList("change filter")),
                                             Entry.State.ACTIVE);
            model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList("change filter")),
                                                 Entry.State.ACTIVE);
            model.updateAllFilteredListToShowAllActiveEntries(); */
        } catch (EntryNotFoundException pnfe) {
            throw new AssertionError("The target entry cannot be missing");
        }
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
