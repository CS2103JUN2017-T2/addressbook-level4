package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
    public void add_tabAutocompleteFromOneChar_success() {
        assertAddTabAutocomplete(AddCommand.COMMAND_WORD.substring(0, 1));
    }

    @Test
    public void add_tabAutocompleteFromTwoChars_success() {
        assertAddTabAutocomplete(AddCommand.COMMAND_WORD.substring(0, 2));
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
        assertEntryAdded(entryToAdd, currentList);
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
        assertEntryAdded(entryToAdd, currentList);
    }

    private void assertEntryAdded(Entry entryAdded, Entry... currentList) {
        // Confirm that added entry is in the list
        EntryCardHandle addedCard = floatingTaskListPanel.navigateToEntry(entryAdded.getName().toString());
        assertMatching(entryAdded, addedCard);

        // Confirm the list now contains all previous entries plus the new entry
        Entry[] expectedList = TestUtil.addEntriesToList(currentList, entryAdded);
        assertTrue(floatingTaskListPanel.isListMatching(expectedList));
    }

}
