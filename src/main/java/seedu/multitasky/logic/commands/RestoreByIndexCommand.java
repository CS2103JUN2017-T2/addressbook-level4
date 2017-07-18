package seedu.multitasky.logic.commands;

import java.util.Objects;

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

//@@author A0126623L-reused
/*
* Restores an entry identified using the type of entry followed by displayed index.
*/
public class RestoreByIndexCommand extends RestoreCommand {

    private Index targetIndex;
    private Prefix listIndicatorPrefix;

    public RestoreByIndexCommand(Index targetIndex, Prefix listIndicatorPrefix) {
        this.targetIndex = targetIndex;
        this.listIndicatorPrefix = listIndicatorPrefix;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {
        UnmodifiableObservableList<ReadOnlyEntry> listToRestoreFrom = ParserUtil
                .getListIndicatedByPrefix(model, listIndicatorPrefix);
        Objects.requireNonNull(listToRestoreFrom);

        if (targetIndex.getZeroBased() >= listToRestoreFrom.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        entryToRestore = listToRestoreFrom.get(targetIndex.getZeroBased());

        if (entryToRestore.isActive()) {
            throw new CommandException(RestoreCommand.MESSAGE_ENTRY_ALREADY_ACTIVE);
        }

        try {
            model.changeEntryState(entryToRestore, Entry.State.ACTIVE);;
        } catch (EntryNotFoundException enfe) {
            throw new AssertionError("The target entry cannot be missing");
        } catch (OverlappingEventException oee) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_ALERT,
                                                   entryToRestore.getName()));
        } catch (EntryOverdueException e) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERDUE_ALERT,
                                                   entryToRestore.getName()));
        } catch (OverlappingAndOverdueEventException e) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_AND_OVERDUE_ALERT,
                                                   entryToRestore.getName()));
        }

        // refresh list view after updating.
        model.updateAllFilteredLists(history.getPrevSearch(), history.getPrevStartDate(),
                                     history.getPrevEndDate(), history.getPrevState());

        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToRestore));
    }

}
