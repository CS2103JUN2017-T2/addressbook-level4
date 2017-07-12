package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.testutil.SampleEntries;
import seedu.multitasky.testutil.TestUtil;

public class SampleDataTest extends EntryBookGuiTest {
    @Override
    protected EntryBook getInitialData() {
        // return null to force test app to load data from file only
        return null;
    }

    @Override
    protected String getDataFileLocation() {
        // return a non-existent file location to force test app to load sample data
        return TestUtil.getFilePathInSandboxFolder("SomeFileThatDoesNotExist1234567890.xml");
    }

    @Test
    public void entryBook_dataFileDoesNotExist_loadSampleData() throws Exception {
        //TODO modify code to accommodate other types of entries in the future
        Entry[] expectedList = SampleEntries.getSampleEvents();
        assertTrue(eventListPanel.isListMatching(expectedList));
        expectedList = SampleEntries.getSampleDeadlines();
        assertTrue(deadlineListPanel.isListMatching(expectedList));
        expectedList = SampleEntries.getSampleFloatingTasks();
        assertTrue(floatingTaskListPanel.isListMatching(expectedList));
    }
}
