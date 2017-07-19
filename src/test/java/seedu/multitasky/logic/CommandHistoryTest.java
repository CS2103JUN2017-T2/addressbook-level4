package seedu.multitasky.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.model.entry.Entry;

// @@author A0140633R
public class CommandHistoryTest {
    private CommandHistory history;

    @Before
    public void setUp() {
        history = new CommandHistory();
    }

    @Test
    public void add_anyCommand_success() {
        final String validCommand = "clear";
        final String invalidCommand = "adds Task";

        history.add(validCommand);
        history.add(invalidCommand);
        assertEquals(Arrays.asList(validCommand, invalidCommand), history.getHistory());
    }

    @Test
    public void setPrevSearch_validArgs_success() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Set<String> keywords = new HashSet<String>(Arrays.asList("new keywords"));
        Entry.State state = Entry.State.ARCHIVED;
        history.setPrevSearch(keywords, startDate, endDate, state);

        assertEquals(history.getPrevKeywords(), keywords);
        assertEquals(history.getPrevStartDate(), startDate);
        assertEquals(history.getPrevEndDate(), endDate);
        assertEquals(history.getPrevState(), state);
    }

    @Test
    public void setEditHistory_validArgs_success() {
        assertFalse(history.hasEditHistory());

        EditEntryDescriptor editEntryDescriptor = new EditEntryDescriptor();
        history.setEditHistory(editEntryDescriptor);
        assertTrue(history.hasEditHistory());
        assertEquals(history.getEditHistory(), editEntryDescriptor);
    }

}
