package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.EntryCardHandle;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.model.entry.Entry;
import seedu.address.testutil.EntryUtil;
import seedu.address.testutil.TestUtil;

public class AddCommandTest extends EntryBookGuiTest {

    @Test
    public void add_floatingTask_success() {
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Entry entryToAdd = typicalEntries.spectacles;
        assertAddFloatingTaskSuccess(entryToAdd, currentList);
        currentList = TestUtil.addEntriesToList(currentList, entryToAdd);
    }

    @Test
    public void add_multipleUniqueFloatingTask_success() {
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Entry entryToAdd = typicalEntries.spectacles;
        assertAddFloatingTaskSuccess(entryToAdd, currentList);
        currentList = TestUtil.addEntriesToList(currentList, entryToAdd);
        entryToAdd = typicalEntries.clean;
        assertAddFloatingTaskSuccess(entryToAdd, currentList);
        currentList = TestUtil.addEntriesToList(currentList, entryToAdd);
        entryToAdd = typicalEntries.sell;
        assertAddFloatingTaskSuccess(entryToAdd, currentList);
        currentList = TestUtil.addEntriesToList(currentList, entryToAdd);
    }

    /*@Test
    public void add() {
        //add one entry
        Entry[] currentList = td.getTypicalEntrys();
        Entry entryToAdd = td.hoon;
        assertAddSuccess(entryToAdd, currentList);
        currentList = TestUtil.addEntrysToList(currentList, entryToAdd);

        //add another entry
        entryToAdd = td.ida;
        assertAddSuccess(entryToAdd, currentList);
        currentList = TestUtil.addEntrysToList(currentList, entryToAdd);

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
