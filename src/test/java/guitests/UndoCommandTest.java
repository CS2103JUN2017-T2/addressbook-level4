package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.EntryCardHandle;
import seedu.multitasky.logic.commands.UndoCommand;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.testutil.EntryUtil;
import seedu.multitasky.testutil.SampleEntries;
import seedu.multitasky.testutil.TestUtil;

// @@author A0125586X
public class UndoCommandTest extends EntryBookGuiTest {

    /*******************
     * Nothing to undo *
     ******************/
    @Test
    public void undo_noCommands_errorMessage() {
        // TODO make sure that on startup, all the snapshots are deleted
        //commandBox.runCommand(UndoCommand.COMMAND_WORD);
        //assertResultMessage(UndoCommand.MESSAGE_FAILURE);
    }

    /********************
     * undo add command *
     *******************/
    @Test
    public void undo_addEvents_success() {
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
