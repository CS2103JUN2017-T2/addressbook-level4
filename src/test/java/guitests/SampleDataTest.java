package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.entry.Entry;
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
    public void entryBook_dataFileDoesNotExist_emptyEntryBook() throws Exception {
        Entry[] expectedList = new Entry[0];
        assertTrue(eventListPanel.isListMatching(expectedList));
        assertTrue(deadlineListPanel.isListMatching(expectedList));
        assertTrue(floatingTaskListPanel.isListMatching(expectedList));
    }
}
