package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.testutil.EntryUtil;
import seedu.multitasky.testutil.TestUtil;
import seedu.multitasky.testutil.TypicalEntries;

public class DeleteCommandTest extends EntryBookGuiTest {

    @Test
    public void delete_firstFloatingTaskByIndex_success() {
        Entry[] currentList = typicalEntries.getTypicalEntries();
        Index targetIndex = TypicalEntries.INDEX_FIRST_ENTRY;
        assertDeleteFloatingTaskSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_secondFloatingTaskByIndex_success() {
        Entry[] currentList = typicalEntries.getTypicalEntries();
        Index targetIndex = TypicalEntries.INDEX_SECOND_ENTRY;
        assertDeleteFloatingTaskSuccess(targetIndex, currentList);
    }

    @Test
    public void delete_thirdFloatingTaskByIndex_success() {
        Entry[] currentList = typicalEntries.getTypicalEntries();
        Index targetIndex = TypicalEntries.INDEX_THIRD_ENTRY;
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

        //confirm the list now contains all previous entries except the deleted entry
        assertTrue(floatingTaskListPanel.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(DeleteCommand.MESSAGE_SUCCESS, entryToDelete));
    }

}
