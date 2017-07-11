package seedu.multitasky.logic.commands;

import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.Prefix;
import seedu.multitasky.model.entry.ReadOnlyEntry;
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
    public CommandResult execute() throws CommandException {
        UnmodifiableObservableList<ReadOnlyEntry> listToDeleteFrom;
        assert listIndicatorPrefix != null;
        if (listIndicatorPrefix.equals(PREFIX_FLOATINGTASK)) {
            listToDeleteFrom = model.getFilteredFloatingTaskList();
        } else if (listIndicatorPrefix.equals(PREFIX_DEADLINE)) {
            listToDeleteFrom = model.getFilteredDeadlineList();
        } else {
            assert (listIndicatorPrefix.equals(PREFIX_EVENT));
            listToDeleteFrom = model.getFilteredEventList();
        }

        if (targetIndex.getZeroBased() >= listToDeleteFrom.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }
        entryToDelete = listToDeleteFrom.get(targetIndex.getZeroBased());
        try {
            model.deleteEntry(entryToDelete);
        } catch (EntryNotFoundException enfe) {
            assert false : "The target entry cannot be missing";
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToDelete));
    }
}
