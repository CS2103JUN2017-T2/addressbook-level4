package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.EntryCardHandle;
import seedu.multitasky.logic.commands.RedoCommand;
import seedu.multitasky.logic.commands.UndoCommand;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.testutil.EntryUtil;
import seedu.multitasky.testutil.SampleEntries;
import seedu.multitasky.testutil.TestUtil;

// @@author A0125586X
public class UndoRedoCommandTest extends EntryBookGuiTest {

    /************************
     * Nothing to undo/redo *
     ***********************/
    @Test
    public void undo_noSnapshot_errorMessage() {
        commandBox.runCommand(UndoCommand.COMMAND_WORD);
        assertResultMessage(UndoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void redo_noSnapshot_errorMessage() {
        commandBox.runCommand(RedoCommand.COMMAND_WORD);
        assertResultMessage(RedoCommand.MESSAGE_FAILURE);
    }

    /*************************
     * Undo/redo add command *
     ************************/
    @Test
    public void undoRedo_addEvents_success() {
        Entry[] currentList = SampleEntries.getSampleEvents();
        Entry[] startingList = currentList.clone();
        Entry entryToAdd = SampleEntries.MOVIE;
        commandBox.runCommand(EntryUtil.getEventAddCommand(entryToAdd));
        assertEventAdded(entryToAdd, currentList);
        currentList = TestUtil.addEntriesToSortedList(currentList, entryToAdd);

        Entry[] middleList = currentList.clone();
        entryToAdd = SampleEntries.OPENING;
        commandBox.runCommand(EntryUtil.getEventAddCommand(entryToAdd));
        assertEventAdded(entryToAdd, currentList);
        currentList = TestUtil.addEntriesToSortedList(currentList, entryToAdd);

        commandBox.runCommand(UndoCommand.COMMAND_WORD);
        assertTrue(eventListPanel.isListMatching(middleList));

        commandBox.runCommand(UndoCommand.COMMAND_WORD);
        assertTrue(eventListPanel.isListMatching(startingList));

        commandBox.runCommand(RedoCommand.COMMAND_WORD);
        assertTrue(eventListPanel.isListMatching(middleList));

        commandBox.runCommand(RedoCommand.COMMAND_WORD);
        assertTrue(eventListPanel.isListMatching(currentList));
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
}
