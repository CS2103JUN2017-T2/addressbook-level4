package seedu.multitasky.logic.commands;

import seedu.multitasky.commons.core.EventsCenter;
import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.events.ui.JumpToListRequestEvent;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.model.entry.ReadOnlyEntry;

/**
 * Selects an identified using it's last displayed index from the address book.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the entry identified by the index number used in the entry listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_ENTRY_SUCCESS = "Selected Entry: %1$s";

    public final Index targetIndex;

    public SelectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        UnmodifiableObservableList<ReadOnlyEntry> lastShownList = model.getFilteredFloatingTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_ENTRY_SUCCESS, targetIndex.getOneBased()));

    }

}
