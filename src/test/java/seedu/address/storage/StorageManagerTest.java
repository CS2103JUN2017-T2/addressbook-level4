package seedu.address.storage;


import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.events.model.EntryBookChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.model.EntryBook;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.EventsCollector;
import seedu.address.testutil.TypicalEntries;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlEntryBookStorage entryBookStorage = new XmlEntryBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(entryBookStorage, userPrefsStorage);
    }


    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void entryBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlEntryBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlEntryBookStorageTest} class.
         */
        EntryBook original = new TypicalEntries().getTypicalEntryBook();
        storageManager.saveEntryBook(original);
        ReadOnlyEntryBook retrieved = storageManager.readEntryBook().get();
        assertEquals(original, new EntryBook(retrieved));
    }

    @Test
    public void getEntryBookFilePath() {
        assertNotNull(storageManager.getEntryBookFilePath());
    }

    @Test
    public void handleEntryBookChangedEvent_exceptionThrown_eventRaised() throws IOException {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlEntryBookStorageExceptionThrowingStub("dummy"),
                                             new JsonUserPrefsStorage("dummy"));
        EventsCollector eventCollector = new EventsCollector();
        storage.handleEntryBookChangedEvent(new EntryBookChangedEvent(new EntryBook()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlEntryBookStorageExceptionThrowingStub extends XmlEntryBookStorage {

        public XmlEntryBookStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveEntryBook(ReadOnlyEntryBook entryBook, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
