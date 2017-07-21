package seedu.multitasky.storage;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.multitasky.commons.events.model.EntryBookChangedEvent;
import seedu.multitasky.commons.events.storage.DataSavingExceptionEvent;
import seedu.multitasky.commons.util.FileUtil;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.testutil.EventsCollector;
import seedu.multitasky.testutil.SampleEntries;

public class StorageManagerTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/StorageManagerTest/");
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlEntryBookStorage entryBookStorage = new XmlEntryBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        UserPrefs userPrefs = new UserPrefs();
        storageManager = new StorageManager(entryBookStorage, userPrefsStorage, userPrefs);
    }

    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null ? TEST_DATA_FOLDER + prefsFileInTestDataFolder : null;
    }

    private void saveEntryBook(ReadOnlyEntryBook entryBook, String filePath) throws IOException {
        new XmlEntryBookStorage(filePath).saveEntryBook(entryBook, addToTestDataPathIfNotNull(filePath));
    }

    /***************************
     * Unit Tests *
     **************************/
    @Test
    public void getEntryBookFilePath() {
        assertNotNull(storageManager.getEntryBookFilePath());
    }

    @Test
    public void getUserPrefsFilePath() {
        assertNotNull(storageManager.getUserPrefsFilePath());
    }

    @Test
    public void getEntryBookSnapshotPath() {
        assertNotNull(storageManager.getEntryBookSnapshotPath());
    }

    @Test
    public void setEntryBookFilePath() {
        storageManager.setEntryBookFilePath("default.xml");
        assertEquals(storageManager.getEntryBookFilePath(), "default.xml");
    }

    @Test
    public void getPreviousEntryBookSnapshotPath() {
        assertNotNull(StorageManager.getPreviousEntryBookSnapshotPath());
    }

    @Test
    public void getNextEntryBookSnapshotPath() {
        assertNotNull(StorageManager.getNextEntryBookSnapshotPath());
    }

    @Test
    public void setEntryBookSnapshotPathAndUpdateIndex() {
        assertNotNull(storageManager.setEntryBookSnapshotPathAndUpdateIndex());
    }

    @Test
    public void handleEntryBookChangedEvent_exceptionThrown_eventRaised() throws IOException {
        // Create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlEntryBookStorageExceptionThrowingStub("dummy"),
                new JsonUserPrefsStorage("dummy"), new UserPrefs());
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

    /***************************
     * Integration Tests *
     **************************/
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
        EntryBook original = SampleEntries.getSampleEntryBookWithActiveEntries();
        storageManager.saveEntryBook(original);
        ReadOnlyEntryBook retrieved = storageManager.readEntryBook().get();
        assertEquals(original, new EntryBook(retrieved));
    }
}
