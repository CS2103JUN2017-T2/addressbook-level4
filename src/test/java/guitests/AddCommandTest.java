package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.EntryCardHandle;
import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Name;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EntryUtil;
import seedu.address.testutil.TestUtil;

//@@author A0125586X
public class AddCommandTest extends EntryBookGuiTest {

    @Test
    public void add_floatingTaskToEmptyList_success() {
        assertCleared();
        Entry[] currentList = new Entry[0];
        Entry entryToAdd = typicalEntries.spectacles;
        currentList = addFloatingTask(entryToAdd, currentList);
    }

    @Test
    public void add_multipleFloatingTaskToEmptyList_success() {
        assertCleared();
        Entry[] currentList = new Entry[0];
        Entry entryToAdd = typicalEntries.spectacles;
        currentList = addFloatingTask(entryToAdd, currentList);

        entryToAdd = typicalEntries.clean;
        currentList = addFloatingTask(entryToAdd, currentList);

        entryToAdd = typicalEntries.sell;
        currentList = addFloatingTask(entryToAdd, currentList);
        assertCleared();
    }

    @Test
    public void add_floatingTaskToExistingList_success() {
        Entry[] currentList = typicalEntries.getTypicalEntries();
        Entry entryToAdd = typicalEntries.spectacles;
        currentList = addFloatingTask(entryToAdd, currentList);
        assertCleared();
    }

    @Test
    public void add_multipleUniqueFloatingTaskToExistingList_success() {
        Entry[] currentList = typicalEntries.getTypicalEntries();
        Entry entryToAdd = typicalEntries.spectacles;
        currentList = addFloatingTask(entryToAdd, currentList);

        entryToAdd = typicalEntries.clean;
        currentList = addFloatingTask(entryToAdd, currentList);

        entryToAdd = typicalEntries.sell;
        currentList = addFloatingTask(entryToAdd, currentList);
        assertCleared();
    }

    @Test
    public void add_unknownCommandName_errorMessage() {
        commandBox.runCommand("a task");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand("ad task");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand("addd task");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void add_invalidName_errorMessage() {
        commandBox.runCommand("add");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);

        commandBox.runCommand("add ");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);

        commandBox.runCommand("add /");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);

        commandBox.runCommand("add $");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);

        commandBox.runCommand("add ?");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);

        commandBox.runCommand("add /ta");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);

        commandBox.runCommand("add /tag");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void add_invalidTags_errorMessage() {
        commandBox.runCommand("add task /tag");
        assertResultMessage(Tag.MESSAGE_TAG_CONSTRAINTS);

        commandBox.runCommand("add task /tag /");
        assertResultMessage(Tag.MESSAGE_TAG_CONSTRAINTS);

        commandBox.runCommand("add task /tag $");
        assertResultMessage(Tag.MESSAGE_TAG_CONSTRAINTS);

        commandBox.runCommand("add task /tag ?");
        assertResultMessage(Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /**
     * Adds a floating task to the entry book as well as to the supplied array
     */
    private Entry[] addFloatingTask(Entry entryToAdd, Entry... currentList) {
        assertAddFloatingTaskSuccess(entryToAdd, currentList);
        return TestUtil.addEntriesToList(currentList, entryToAdd);
    }

    /**
     * Clears all tasks and asserts that they have been cleared
     */
    private void assertCleared() {
        //TODO check archive and bin as well, or check all at once
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertTrue(eventListPanel.isEmpty());
        assertTrue(deadlineListPanel.isEmpty());
        assertTrue(floatingTaskListPanel.isEmpty());
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
