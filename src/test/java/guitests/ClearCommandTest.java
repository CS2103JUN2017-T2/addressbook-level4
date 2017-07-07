package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.testutil.EntryUtil;

public class ClearCommandTest extends EntryBookGuiTest {

    @Test
    public void clear_emptyList_success() {
        assertClearCommandSuccess();
        assertClearCommandSuccess();
    }

    @Test
    public void clear_nonEmptyList_success() {
        //TODO add support to check other types of tasks in the future
        assertTrue(eventListPanel.isEmpty());
        assertTrue(deadlineListPanel.isEmpty());
        assertClearCommandSuccess();
    }

    @Test
    public void clear_addAfterClear_success() {
        assertClearCommandSuccess();
        commandBox.runCommand(EntryUtil.getFloatingTaskAddCommand(typicalEntries.clean));
        assertTrue(floatingTaskListPanel.isListMatching(typicalEntries.clean));
    }

    private void assertCleared() {
        assertTrue(eventListPanel.isEmpty());
        assertTrue(deadlineListPanel.isEmpty());
        assertTrue(floatingTaskListPanel.isEmpty());
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertCleared();
        assertResultMessage("Entry book has been cleared!");
    }
}
