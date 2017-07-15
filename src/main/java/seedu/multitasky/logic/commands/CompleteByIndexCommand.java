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

// @@author A0132788U-reused
/*
 * Completes an entry identified using the type of entry followed by displayed index.
 */
public class CompleteByIndexCommand extends CompleteCommand {

    private Index targetIndex;
    private Prefix listIndicatorPrefix;

    public CompleteByIndexCommand(Index targetIndex, Prefix listIndicatorPrefix) {
        this.targetIndex = targetIndex;
        this.listIndicatorPrefix = listIndicatorPrefix;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {
        UnmodifiableObservableList<ReadOnlyEntry> listToCompleteFrom = ParserUtil
                .getListIndicatedByPrefix(model, listIndicatorPrefix);
        assert listToCompleteFrom != null;

        if (targetIndex.getZeroBased() >= listToCompleteFrom.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        entryToComplete = listToCompleteFrom.get(targetIndex.getZeroBased());
        try {
            model.changeEntryState(entryToComplete, Entry.State.ARCHIVED);
        } catch (EntryNotFoundException enfe) {
            assert false : "The target entry cannot be missing";
        }
        // refresh list view after updating.
        model.updateFilteredDeadlineList(history.getPrevSearch(), Entry.State.ACTIVE);
        model.updateFilteredEventList(history.getPrevSearch(), Entry.State.ACTIVE);
        model.updateFilteredFloatingTaskList(history.getPrevSearch(), Entry.State.ACTIVE);

        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToComplete));
    }

}
