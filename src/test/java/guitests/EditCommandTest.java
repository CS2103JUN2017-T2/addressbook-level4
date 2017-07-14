package guitests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import seedu.multitasky.commons.core.index.Index;
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

    /***************************
     * Editing by keyword find *
     **************************/
    @Test
    public void edit_eventKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleEvents();
        assertEditEventByKeywordSuccess("dinner", currentList, SampleEntries.DINNER, SampleEntries.OPENING);
    }


    private void assertEditEventByIndexSuccess(Index index, Entry[] currentList, Entry editedEntry) {
        commandBox.runCommand(CommandUtil.getEditEventByIndexCommand(index, editedEntry));
        Entry[] expectedList = TestUtil.removeEntryFromList(currentList, index);
        expectedList = TestUtil.addEntriesToList(expectedList, editedEntry);
        ArrayList<Entry> expected = new ArrayList<>(Arrays.asList(expectedList));
        Collections.sort(expected, Comparators.EVENT_DEFAULT);
        assertTrue(eventListPanel.isListMatching(expected.toArray(new Entry[expected.size()])));
    }

    private void assertEditEventByKeywordSuccess(String keywords, Entry[] currentList, Entry entryToEdit,
                                                 Entry editedEntry) {
        commandBox.runCommand(CommandUtil.getEditEventByKeywordCommand(keywords, editedEntry));
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryToEdit);
        expectedList = TestUtil.addEntriesToList(expectedList, editedEntry);
        ArrayList<Entry> expected = new ArrayList<>(Arrays.asList(expectedList));
        Collections.sort(expected, Comparators.EVENT_DEFAULT);
        assertTrue(eventListPanel.isListMatching(expected.toArray(new Entry[expected.size()])));
    }

}
