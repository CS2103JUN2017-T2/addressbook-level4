package seedu.multitasky.logic.commands;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.ParserUtil;
import seedu.multitasky.logic.parser.Prefix;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;

// @@author A0140633R
/*
 * Deletes an entry identified using the type of entry followed by displayed index.
 */
public class DeleteByIndexCommand extends DeleteCommand {

    private Index targetIndex;
    private Prefix listIndicatorPrefix;

    public DeleteByIndexCommand(Index targetIndex, Prefix listIndicatorPrefix) {
        this.targetIndex = targetIndex;
        this.listIndicatorPrefix = listIndicatorPrefix;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {
        UnmodifiableObservableList<ReadOnlyEntry> listToDeleteFrom = ParserUtil
                .getListIndicatedByPrefix(model, listIndicatorPrefix);
        assert listToDeleteFrom != null;

        if (targetIndex.getZeroBased() >= listToDeleteFrom.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        entryToDelete = listToDeleteFrom.get(targetIndex.getZeroBased());
        try {
            model.changeEntryState(entryToDelete, Entry.State.DELETED);
        } catch (EntryNotFoundException enfe) {
            assert false : "The target entry cannot be missing";
        }
        // refresh list view after updating.
        model.updateAllFilteredLists(history.getPrevSearch(), null, null, history.getPrevState());

        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToDelete));
    }

}
