package guitests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.util.Comparators;
import seedu.multitasky.testutil.CommandUtil;
import seedu.multitasky.testutil.SampleEntries;
import seedu.multitasky.testutil.TestUtil;

// @@author A0125586X
public class EditCommandTest extends EntryBookGuiTest {

    /********************
     * Editing by Index *
     *******************/
    @Test
    public void edit_firstEventByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleEvents();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertEditEventByIndexSuccess(targetIndex, currentList, SampleEntries.OPENING);
    }

    @Test
    public void edit_firstDeadlineByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleDeadlines();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertEditDeadlineByIndexSuccess(targetIndex, currentList, SampleEntries.UPGRADE);
    }

    @Test
    public void edit_firstFloatingTaskByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleFloatingTasks();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertEditFloatingTaskByIndexSuccess(targetIndex, currentList, SampleEntries.SELL);
    }

    /***************************
     * Editing by keyword find *
     **************************/
    @Test
    public void edit_eventKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleEvents();
        assertEditEventByKeywordSuccess("dinner", currentList, SampleEntries.DINNER, SampleEntries.OPENING);
    }

    @Test
    public void edit_deadlineKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleDeadlines();
        assertEditDeadlineByKeywordSuccess("tax", currentList, SampleEntries.TAX, SampleEntries.UPGRADE);
    }


    /*******************
     * Utility methods *
     ******************/
    private void assertEditEventByIndexSuccess(Index index, Entry[] currentList, Entry editedEntry) {
        commandBox.runCommand(CommandUtil.getEditEventByIndexCommand(index, editedEntry));
        ArrayList<Entry> expectedList = new ArrayList<>(Arrays.asList(currentList));
        expectedList.set(index.getZeroBased(), editedEntry);
        Collections.sort(expectedList, Comparators.EVENT_DEFAULT);
        commandBox.runCommand(ListCommand.COMMAND_WORD);
        assertTrue(eventListPanel.isListMatching(expectedList.toArray(new Entry[expectedList.size()])));
    }

    private void assertEditDeadlineByIndexSuccess(Index index, Entry[] currentList, Entry editedEntry) {
        commandBox.runCommand(CommandUtil.getEditDeadlineByIndexCommand(index, editedEntry));
        ArrayList<Entry> expectedList = new ArrayList<>(Arrays.asList(currentList));
        expectedList.set(index.getZeroBased(), editedEntry);
        Collections.sort(expectedList, Comparators.DEADLINE_DEFAULT);
        commandBox.runCommand(ListCommand.COMMAND_WORD);
        assertTrue(deadlineListPanel.isListMatching(expectedList.toArray(new Entry[expectedList.size()])));
    }

    private void assertEditFloatingTaskByIndexSuccess(Index index, Entry[] currentList, Entry editedEntry) {
        commandBox.runCommand(CommandUtil.getEditFloatingTaskByIndexCommand(index, editedEntry));
        ArrayList<Entry> expectedList = new ArrayList<>(Arrays.asList(currentList));
        expectedList.set(index.getZeroBased(), editedEntry);
        Collections.sort(expectedList, Comparators.FLOATING_TASK_DEFAULT);
        commandBox.runCommand(ListCommand.COMMAND_WORD);
        assertTrue(floatingTaskListPanel.isListMatching(expectedList.toArray(new Entry[expectedList.size()])));
    }

    private void assertEditEventByKeywordSuccess(String keywords, Entry[] currentList, Entry entryToEdit,
                                                 Entry editedEntry) {
        commandBox.runCommand(CommandUtil.getEditEventByKeywordCommand(keywords, editedEntry));
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryToEdit);
        expectedList = TestUtil.addEntriesToList(expectedList, editedEntry);
        ArrayList<Entry> expected = new ArrayList<>(Arrays.asList(expectedList));
        Collections.sort(expected, Comparators.EVENT_DEFAULT);
        commandBox.runCommand(ListCommand.COMMAND_WORD);
        assertTrue(eventListPanel.isListMatching(expected.toArray(new Entry[expected.size()])));
    }

    private void assertEditDeadlineByKeywordSuccess(String keywords, Entry[] currentList, Entry entryToEdit,
                                                    Entry editedEntry) {
        commandBox.runCommand(CommandUtil.getEditDeadlineByKeywordCommand(keywords, editedEntry));
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryToEdit);
        expectedList = TestUtil.addEntriesToList(expectedList, editedEntry);
        ArrayList<Entry> expected = new ArrayList<>(Arrays.asList(expectedList));
        Collections.sort(expected, Comparators.DEADLINE_DEFAULT);
        commandBox.runCommand(ListCommand.COMMAND_WORD);
        assertTrue(deadlineListPanel.isListMatching(expected.toArray(new Entry[expected.size()])));
    }

}
