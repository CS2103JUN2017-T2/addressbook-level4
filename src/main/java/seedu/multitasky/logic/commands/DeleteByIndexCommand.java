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
import seedu.multitasky.model.entry.exceptions.EntryOverdueException;
import seedu.multitasky.model.entry.exceptions.OverlappingAndOverdueEventException;
import seedu.multitasky.model.entry.exceptions.OverlappingEventException;

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
            throw new AssertionError("The target entry cannot be missing");
        } catch (OverlappingEventException oee) {
            throw new AssertionError("Overlap should not happen for deletion.");
        } catch (EntryOverdueException e) {
            throw new AssertionError("Overdue should not apply to deletion.");
        } catch (OverlappingAndOverdueEventException e) {
            throw new AssertionError("Overlap and overdue should not apply to deletion.");
        }
        // refresh list view after updating.
        model.updateAllFilteredLists(history.getPrevKeywords(), history.getPrevStartDate(),
                                     history.getPrevEndDate(), history.getPrevState(),
                                     history.getPrevSearches());

        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToDelete));
    }

}
