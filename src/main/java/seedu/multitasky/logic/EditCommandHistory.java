package seedu.multitasky.logic;

import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;

// @@author A0140633R
/**
 * Represents the portions of CommandHistory that EditCommand and EditCommandParser will be working with
 */
public interface EditCommandHistory {

    public boolean hasEditHistory();

    public EditEntryDescriptor getEditHistory();

    public void setEditHistory(EditEntryDescriptor editEntryDescriptor);

    public void resetEditHistory();

}
