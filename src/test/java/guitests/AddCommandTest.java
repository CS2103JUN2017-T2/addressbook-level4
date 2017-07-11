package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import guitests.guihandles.EntryCardHandle;
import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.testutil.EntryUtil;
import seedu.multitasky.testutil.TestUtil;

//@@author A0125586X
public class AddCommandTest extends EntryBookGuiTest {

    @Test
    public void add_eventToEmptyList_success() {
        assertCleared();
        Entry[] currentList = new Entry[0];
        Entry entryToAdd = typicalEntries.dinner;
        currentList = addEvent(entryToAdd, currentList);
    }

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
    }

    @Test
    public void add_floatingTaskToExistingList_success() {
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Entry entryToAdd = typicalEntries.spectacles;
        currentList = addFloatingTask(entryToAdd, currentList);
    }

    @Test
    public void add_multipleUniqueFloatingTaskToExistingList_success() {
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Entry entryToAdd = typicalEntries.spectacles;
        currentList = addFloatingTask(entryToAdd, currentList);

        entryToAdd = typicalEntries.clean;
        currentList = addFloatingTask(entryToAdd, currentList);

        entryToAdd = typicalEntries.sell;
        currentList = addFloatingTask(entryToAdd, currentList);
    }

    /**
     * For all mixed-case tests only floating task entries are tested,
     * which should be suitable to test for all types since the type of task
     * doesn't affect the parsing of the command word.
     */
    @Test
    public void add_firstCharUppercase_success() {
        char[] commandWord = AddCommand.COMMAND_WORD.toCharArray();
        commandWord[0] = Character.toUpperCase(commandWord[0]);
        assertAddWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void add_lastCharUppercase_success() {
        char[] commandWord = AddCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length - 1] = Character.toUpperCase(commandWord[commandWord.length - 1]);
        assertAddWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void add_middleCharUppercase_success() {
        char[] commandWord = AddCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length / 2] = Character.toUpperCase(commandWord[commandWord.length / 2]);
        assertAddWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void add_allCharUppercase_success() {
        String commandWord = AddCommand.COMMAND_WORD.toUpperCase();
        assertAddWithCommandWord(commandWord);
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
    public void add_tabAutocomplete_success() {
        for (int i = 1; i < AddCommand.COMMAND_WORD.length(); ++i) {
            assertAddTabAutocomplete(AddCommand.COMMAND_WORD.substring(0, i));
        }
    }

    @Test
    public void add_invalidCommandFormat_errorMessage() {
        commandBox.runCommand("add");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        commandBox.runCommand("add ");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        commandBox.runCommand("add /tag");
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }


    @Test
    public void add_invalidEntryName_errorMessage() {
        commandBox.runCommand("add $");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);

        commandBox.runCommand("add /ta");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void add_invalidTags_errorMessage() {
        commandBox.runCommand("add task /tag");
        assertResultMessage(Tag.MESSAGE_TAG_CONSTRAINTS);

        commandBox.runCommand("add task /tag $");
        assertResultMessage(Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /**
     * Confirms that the given input string will autocomplete to the correct add command word.
     */
    private void assertAddTabAutocomplete(String input) {
        commandBox.enterCommand(input);
        commandBox.pressTabKey();
        assertCommandBox(AddCommand.COMMAND_WORD + " ");
    }

    /**
     * Helps with the testing of command words with different character cases
     */
    private void assertAddWithCommandWord(String commandWord) {
        Entry[] currentList = typicalEntries.getTypicalFloatingTasks();
        Entry entryToAdd = typicalEntries.spectacles;
        commandBox.runCommand(commandWord + " " + EntryUtil.getFloatingTaskDetailsForAdd(entryToAdd));
        assertFloatingTaskAdded(entryToAdd, currentList);
    }

    /**
     * Adds an event to the entry book as well as to the supplied array
     *
     * @return the
     */
    private Entry[] addEvent(Entry entryToAdd, Entry... currentList) {
        assertAddEventSuccess(entryToAdd, currentList);
        return TestUtil.addEntriesToSortedList(currentList, entryToAdd);
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
     * Attempts to add an entry as an event and confirms that it has been added.
     */
    private void assertAddEventSuccess(Entry entryToAdd, Entry... currentList) {
        commandBox.runCommand(EntryUtil.getEventAddCommand(entryToAdd));
        assertEventAdded(entryToAdd, currentList);
    }

    /**
     * Attempts to add an entry as a floating task and confirms that it has been added.
     */
    private void assertAddFloatingTaskSuccess(Entry entryToAdd, Entry... currentList) {
        commandBox.runCommand(EntryUtil.getFloatingTaskAddCommand(entryToAdd));
        assertFloatingTaskAdded(entryToAdd, currentList);
    }

    /**
     * Confirms that the added entry is in the expected list, and that the
     * expected list matches the displayed list.
     */
    private void assertEventAdded(Entry entryAdded, Entry... currentList) {
        EntryCardHandle addedCard = eventListPanel.navigateToEntry(entryAdded.getName().toString());
        assertMatching(entryAdded, addedCard);
        Entry[] expectedList = TestUtil.addEntriesToSortedList(currentList, entryAdded);
        assertTrue(eventListPanel.isListMatching(expectedList));
    }

    /**
     * Confirms that the added entry is in the expected list, and that the
     * expected list matches the displayed list.
     */
    private void assertFloatingTaskAdded(Entry entryAdded, Entry... currentList) {
        EntryCardHandle addedCard = floatingTaskListPanel.navigateToEntry(entryAdded.getName().toString());
        assertMatching(entryAdded, addedCard);

        Entry[] expectedList = TestUtil.addEntriesToList(currentList, entryAdded);
        assertTrue(floatingTaskListPanel.isListMatching(expectedList));
    }

}
