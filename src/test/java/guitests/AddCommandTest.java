package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.EntryCardHandle;
import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.testutil.CommandUtil;
import seedu.multitasky.testutil.SampleEntries;
import seedu.multitasky.testutil.TestUtil;

// @@author A0125586X
public class AddCommandTest extends EntryBookGuiTest {

    /***************************
     * Adding to an empty list *
     **************************/
    @Test
    public void add_eventsToEmptyList_success() {
        assertCleared();
        Entry[] currentList = new Entry[0];
        Entry entryToAdd = SampleEntries.DINNER;
        currentList = assertAddEvent(entryToAdd, currentList);

        entryToAdd = SampleEntries.CAT;
        currentList = assertAddEvent(entryToAdd, currentList);
    }

    @Test
    public void add_deadlinesToEmptyList_success() {
        assertCleared();
        Entry[] currentList = new Entry[0];
        Entry entryToAdd = SampleEntries.PAPER;
        currentList = assertAddDeadline(entryToAdd, currentList);

        entryToAdd = SampleEntries.SUBMISSION;
        currentList = assertAddDeadline(entryToAdd, currentList);
    }

    @Test
    public void add_floatingTasksToEmptyList_success() {
        assertCleared();
        Entry[] currentList = new Entry[0];
        Entry entryToAdd = SampleEntries.SPECTACLES;
        currentList = assertAddFloatingTask(entryToAdd, currentList);

        entryToAdd = SampleEntries.CLEAN;
        currentList = assertAddFloatingTask(entryToAdd, currentList);
    }

    /***************************
     * Adding an existing list *
     **************************/
    @Test
    public void add_eventsToExistingList_success() {
        Entry[] currentList = SampleEntries.getSampleActiveEvents();
        Entry entryToAdd = SampleEntries.MOVIE;
        currentList = assertAddEvent(entryToAdd, currentList);

        entryToAdd = SampleEntries.OPENING;
        currentList = assertAddEvent(entryToAdd, currentList);
    }

    @Test
    public void add_deadlinesToExistingList_success() {
        Entry[] currentList = SampleEntries.getSampleActiveDeadlines();
        Entry entryToAdd = SampleEntries.SUBMISSION;
        currentList = assertAddDeadline(entryToAdd, currentList);

        entryToAdd = SampleEntries.UPGRADE;
        currentList = assertAddDeadline(entryToAdd, currentList);
    }

    @Test
    public void add_floatingTaskToExistingList_success() {
        Entry[] currentList = SampleEntries.getSampleActiveFloatingTasks();
        Entry entryToAdd = SampleEntries.SPECTACLES;
        currentList = assertAddFloatingTask(entryToAdd, currentList);

        entryToAdd = SampleEntries.CLEAN;
        currentList = assertAddFloatingTask(entryToAdd, currentList);
    }

    /**************************************
     * Different types of invalid wording *
     **************************************/
    @Test
    public void add_unknownCommandName_errorMessage() {
        commandBox.runCommand(AddCommand.COMMAND_WORD.substring(0, AddCommand.COMMAND_WORD.length() - 1));
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand(AddCommand.COMMAND_WORD + "a");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void add_invalidCommandFormat_errorMessage() {
        commandBox.runCommand(AddCommand.COMMAND_WORD);
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }

    @Test
    public void add_invalidEntryName_errorMessage() {
        commandBox.runCommand(AddCommand.COMMAND_WORD + " $");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);

        commandBox.runCommand(AddCommand.COMMAND_WORD + " /");
        assertResultMessage(Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void add_invalidTags_errorMessage() {
        commandBox.runCommand(AddCommand.COMMAND_WORD + " task " + CliSyntax.PREFIX_TAG);
        assertResultMessage(Tag.MESSAGE_TAG_CONSTRAINTS);

        commandBox.runCommand(AddCommand.COMMAND_WORD + " task " + CliSyntax.PREFIX_TAG + " $");
        assertResultMessage(Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    /*******************************
     * Mixed-case and autocomplete *
     ******************************/
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
    public void add_tabAutocomplete_success() {
        for (int i = 1; i < AddCommand.COMMAND_WORD.length(); ++i) {
            assertAddTabAutocomplete(AddCommand.COMMAND_WORD.substring(0, i));
        }
        assertAddTabAutocomplete(AddCommand.COMMAND_WORD + "aaa");
    }

    /**
     * Confirms that the given input string will autocomplete to the correct command word.
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
        Entry[] currentList = SampleEntries.getSampleActiveFloatingTasks();
        Entry entryToAdd = SampleEntries.SPECTACLES;
        commandBox.runCommand(commandWord + " " + CommandUtil.getFloatingTaskDetails(entryToAdd));
        assertFloatingTaskAdded(entryToAdd, currentList);
    }

    /**
     * Adds an event to the entry book as well as to the supplied array
     */
    private Entry[] assertAddEvent(Entry entryToAdd, Entry... currentList) {
        assertAddEventSuccess(entryToAdd, currentList);
        return TestUtil.addEntriesToSortedList(currentList, entryToAdd);
    }

    /**
     * Adds a deadline to the entry book as well as to the supplied array
     */
    private Entry[] assertAddDeadline(Entry entryToAdd, Entry... currentList) {
        assertAddDeadlineSuccess(entryToAdd, currentList);
        return TestUtil.addEntriesToSortedList(currentList, entryToAdd);
    }

    /**
     * Adds a floating task to the entry book as well as to the supplied array
     */
    private Entry[] assertAddFloatingTask(Entry entryToAdd, Entry... currentList) {
        assertAddFloatingTaskSuccess(entryToAdd, currentList);
        return TestUtil.addEntriesToList(currentList, entryToAdd);
    }

    /**
     * Clears all tasks and asserts that they have been cleared
     */
    private void assertCleared() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        // commandBox.runCommand(ClearCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_ARCHIVE);
        // commandBox.runCommand(ClearCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        assertTrue(eventListPanel.isEmpty());
        assertTrue(deadlineListPanel.isEmpty());
        assertTrue(floatingTaskListPanel.isEmpty());
    }

    /**
     * Attempts to add an entry as an event and confirms that it has been added.
     */
    private void assertAddEventSuccess(Entry entryToAdd, Entry... currentList) {
        commandBox.runCommand(CommandUtil.getAddEventCommand(entryToAdd));
        assertEventAdded(entryToAdd, currentList);
    }

    /**
     * Attempts to add an entry as a deadline and confirms that it has been added.
     */
    private void assertAddDeadlineSuccess(Entry entryToAdd, Entry... currentList) {
        commandBox.runCommand(CommandUtil.getAddDeadlineCommand(entryToAdd));
        assertDeadlineAdded(entryToAdd, currentList);
    }

    /**
     * Attempts to add an entry as a floating task and confirms that it has been added.
     */
    private void assertAddFloatingTaskSuccess(Entry entryToAdd, Entry... currentList) {
        commandBox.runCommand(CommandUtil.getAddFloatingTaskCommand(entryToAdd));
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
    private void assertDeadlineAdded(Entry entryAdded, Entry... currentList) {
        EntryCardHandle addedCard = deadlineListPanel.navigateToEntry(entryAdded.getName().toString());
        assertMatching(entryAdded, addedCard);
        Entry[] expectedList = TestUtil.addEntriesToSortedList(currentList, entryAdded);
        assertTrue(deadlineListPanel.isListMatching(expectedList));
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
