package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.EntryCardHandle;
import seedu.address.model.entry.Entry;
import seedu.address.testutil.EntryUtil;
import seedu.address.testutil.TestUtil;

//@@author A0125586X
public class AddCommandTest extends EntryBookGuiTest {

    @Test
    public void add_floatingTask_success() {
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Entry entryToAdd = typicalEntries.spectacles;
        currentList = addFloatingTask(entryToAdd, currentList);
    }

    @Test
    public void add_multipleUniqueFloatingTask_success() {
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Entry entryToAdd = typicalEntries.spectacles;
        currentList = addFloatingTask(entryToAdd, currentList);
        entryToAdd = typicalEntries.clean;
        currentList = addFloatingTask(entryToAdd, currentList);
        entryToAdd = typicalEntries.sell;
        currentList = addFloatingTask(entryToAdd, currentList);
    }

    /*@Test
    public void add() {
        //add duplicate entry
        commandBox.runCommand(EntryUtil.getAddCommand(td.hoon));
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);
        assertTrue(entryListPanel.isListMatching(currentList));

        //add to empty list
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertAddSuccess(td.alice);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }*/

    private Entry[] addFloatingTask(Entry entryToAdd, Entry[] currentList) {
        assertAddFloatingTaskSuccess(entryToAdd, currentList);
        return TestUtil.addEntriesToList(currentList, entryToAdd);
    }

    private void assertEventListEmpty() {
        assertTrue(eventListPanel.getNumberOfEntries() == 0);
    }

    private void assertDeadlineListEmpty() {
        assertTrue(deadlineListPanel.getNumberOfEntries() == 0);
    }

    private void assertFloatingTaskListEmpty() {
        assertTrue(floatingTaskListPanel.getNumberOfEntries() == 0);
    }

    /**
     * Attempts to add an entry as a floating task and confirms that it has been added.
     */
    private void assertAddFloatingTaskSuccess(Entry entryToAdd, Entry... currentList) {
        commandBox.runCommand(EntryUtil.getFloatingTaskAddCommand(entryToAdd));

        //confirm the new card contains the right data
        EntryCardHandle addedCard = floatingTaskListPanel.navigateToEntry(entryToAdd.getName().toString());
        assertMatching(entryToAdd, addedCard);

        //confirm the list now contains all previous entries plus the new entry
        Entry[] expectedList = TestUtil.addEntriesToList(currentList, entryToAdd);
        assertTrue(floatingTaskListPanel.isListMatching(expectedList));
    }

}
