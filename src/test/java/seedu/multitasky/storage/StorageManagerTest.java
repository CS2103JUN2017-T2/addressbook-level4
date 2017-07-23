package seedu.multitasky.storage;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.multitasky.commons.events.model.EntryBookChangedEvent;
import seedu.multitasky.commons.events.storage.DataSavingExceptionEvent;
import seedu.multitasky.commons.events.storage.EntryBookToRedoEvent;
import seedu.multitasky.commons.events.storage.EntryBookToUndoEvent;
import seedu.multitasky.commons.events.storage.FilePathChangedEvent;
import seedu.multitasky.commons.events.storage.LoadDataFromFilePathEvent;
import seedu.multitasky.commons.util.FileUtil;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.StorageUserPrefs;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.testutil.EventsCollector;
import seedu.multitasky.testutil.SampleEntries;

// @@author A0132788U
/**
 * Tests for StorageManager operations
 */
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
        StorageUserPrefs userPrefs = new UserPrefs();
        storageManager = new StorageManager(entryBookStorage, userPrefsStorage, userPrefs);
    }

    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
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
    public void setEntryBookFilePath() {
        storageManager.setEntryBookFilePath("default.xml");
        assertEquals(storageManager.getEntryBookFilePath(), "default.xml");
    }

    @Test
    public void getEntryBookSnapshotPath() {
        assertNotNull(storageManager.getEntryBookSnapshotPath());
    }

    @Test
    public void setIndex() {
        StorageManager.setIndex(100);
        assertEquals(StorageManager.getIndex(), 100);
    }

    @Test
    public void getPreviousEntryBookSnapshotPath() {
        assertNotNull(storageManager.getPreviousEntryBookSnapshotPath());
    }

    @Test
    public void getNextEntryBookSnapshotPath() {
        assertNotNull(storageManager.getNextEntryBookSnapshotPath());
    }

    @Test
    public void setEntryBookSnapshotPathAndUpdateIndex() {
        assertNotNull(storageManager.setEntryBookSnapshotPathAndUpdateIndex());
    }

    @Test
    public void saveEntryBook() throws IOException {
        ReadOnlyEntryBook currentList = SampleEntries.getSampleEntryBook();
        storageManager.saveEntryBook(currentList);
        assertNotNull(storageManager.getEntryBookFilePath());
    }

    @Test
    public void saveEntryBookSnapshot() throws IOException {
        ReadOnlyEntryBook currentList = SampleEntries.getSampleEntryBook();
        storageManager.saveEntryBookSnapshot(currentList);
        assertNotNull(storageManager.setEntryBookSnapshotPathAndUpdateIndex());
    }

    @Test
    public void loadDataFromFile_success() throws Exception {
        assertTrue(readEntryBook("sample.xml").isPresent());
        EntryBook loadedData = storageManager.loadDataFromFile("./src/test/data/StorageManagerTest/sample.xml");
        assertNotNull(loadedData);
    }

    @Test
    public void loadDataFromFile_error() throws Exception {
        thrown.expect(Exception.class);
        EntryBook loadedData = storageManager.loadDataFromFile("./src/test/data/StorageManagerTest/test.xml");
        assertNull(loadedData);
    }

    @Test
    public void handleEntryBookChangedEvent_success() {
        EntryBookChangedEvent event = new EntryBookChangedEvent(SampleEntries.getSampleEntryBook());
        storageManager.handleEntryBookChangedEvent(event);
        assertNotNull(event.data);
    }

    @Test
    public void handleEntryBookToUndoEvent_success() throws Exception {
        EntryBookToUndoEvent event = new EntryBookToUndoEvent(SampleEntries.getSampleEntryBook(), " ");
        storageManager.handleEntryBookToUndoEvent(event);
        assertNotNull(event.getData());
    }

    @Test
    public void handleEntryBookToUndoEvent_failure() throws Exception {
        storageManager.setPreviousEntryBookSnapshotPath("dummyUndo");
        EntryBookToUndoEvent event = new EntryBookToUndoEvent(new EntryBook(), "dummyUndo");
        storageManager.handleEntryBookToUndoEvent(event);
        assertEquals(event.getMessage(), "Nothing to undo");
    }

    @Test
    public void handleEntryBookToRedoEvent_success() throws Exception {
        EntryBookToUndoEvent eventUndo = new EntryBookToUndoEvent(SampleEntries.getSampleEntryBook(), " ");
        storageManager.handleEntryBookToUndoEvent(eventUndo);
        EntryBookToRedoEvent eventRedo = new EntryBookToRedoEvent(SampleEntries.getSampleEntryBook(), " ");
        storageManager.handleEntryBookToRedoEvent(eventRedo);
        assertNotNull(eventRedo.getData());
    }

    @Test
    public void handleEntryBookToRedoEvent_failure() throws Exception {
        storageManager.setNextEntryBookSnapshotPath("dummyRedo");
        EntryBookToRedoEvent event = new EntryBookToRedoEvent(new EntryBook(), "dummyRedo");
        storageManager.handleEntryBookToRedoEvent(event);
        assertEquals(event.getMessage(), "Nothing to redo");
    }

    @Test
    public void handleFilePathChangedEvent_success() throws Exception {
        FilePathChangedEvent event = new FilePathChangedEvent(SampleEntries.getSampleEntryBook(), "newfile.xml");
        storageManager.handleFilePathChangedEvent(event);
        assertEquals(storageManager.getEntryBookFilePath(), "newfile.xml");
    }

    @Test
    public void handleLoadDataFromFilePathEvent_success() throws Exception {
        LoadDataFromFilePathEvent event = new LoadDataFromFilePathEvent(SampleEntries.getSampleEntryBook(),
                " ", " ");
        event.setFilepath("./src/test/data/StorageManagerTest/sample.xml");
        storageManager.handleLoadDataFromFilePathEvent(event);
        assertEquals(event.getMessage(), "open successful");
    }

    @Test
    public void handleLoadDataFromFilePathEvent_failure() throws Exception {
        LoadDataFromFilePathEvent event = new LoadDataFromFilePathEvent(SampleEntries.getSampleEntryBook(),
                "./src/test/data/StorageManagerTest/test.xml", " ");
        storageManager.handleLoadDataFromFilePathEvent(event);
        assertEquals(event.getMessage(), "Error in loading data!");
    }

    // @@author
    @Test
    public void handleEntryBookChangedEvent_exceptionThrown_eventRaised() throws IOException {
        // Create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlEntryBookStorageExceptionThrowingStub("dummy"),
                new JsonUserPrefsStorage("dummy"), new UserPrefs());
        EventsCollector eventCollector = new EventsCollector();
        storage.handleEntryBookChangedEvent(new EntryBookChangedEvent(new EntryBook()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
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

    @Test
    public void readEntryBook_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readEntryBook(null);
    }

    // @@author A0132788U
    /***************************
     * Helper Classes and Methods *
     **************************/

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

    private java.util.Optional<ReadOnlyEntryBook> readEntryBook(String filePath) throws Exception {
        return new XmlEntryBookStorage(filePath).readEntryBook(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null ? TEST_DATA_FOLDER + prefsFileInTestDataFolder : null;
    }

}
