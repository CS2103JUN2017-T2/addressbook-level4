package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.entry.Entry;
import seedu.address.testutil.EntryUtil;
import seedu.address.testutil.TestUtil;
import seedu.address.testutil.TypicalEntries;

public class DeleteCommandTest extends EntryBookGuiTest {

    @Test
    public void delete_firstFloatingTaskByIndex_success() {
        Entry[] currentList = typicalEntries.getTypicalEntries();
        Index targetIndex = TypicalEntries.INDEX_FIRST_ENTRY;
        assertDeleteFloatingTaskSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_lastFloatingTaskByIndex_success() {
        Entry[] currentList = typicalEntries.getTypicalEntries();
        Index targetIndex = Index.fromOneBased(currentList.length);
        assertDeleteFloatingTaskSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_middleFloatingTaskByIndex_success() {
        Entry[] currentList = typicalEntries.getTypicalEntries();
        Index targetIndex = Index.fromOneBased(currentList.length / 2);
        assertDeleteFloatingTaskSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_invalidFloatingTaskIndex_errorMessage() {
        Entry[] currentList = typicalEntries.getTypicalEntries();
        Index targetIndex = Index.fromOneBased(currentList.length + 1);
        commandBox.runCommand(EntryUtil.getFloatingTaskDeleteByIndexCommand(targetIndex));
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /**
     * Runs the delete command to delete the floating task at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of floating tasks (before deletion).
     */
    private void assertDeleteFloatingTaskSuccess(Index index, final Entry[] currentList) {
        Entry entryToDelete = currentList[index.getZeroBased()];
        Entry[] expectedRemainder = TestUtil.removeEntryFromList(currentList, index);

        commandBox.runCommand(EntryUtil.getFloatingTaskDeleteByIndexCommand(index));

        //confirm the list now contains all previous persons except the deleted person
        assertTrue(floatingTaskListPanel.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(DeleteCommand.MESSAGE_DELETE_ENTRY_SUCCESS, entryToDelete));
    }

}
