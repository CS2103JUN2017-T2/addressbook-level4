package seedu.multitasky;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.commons.events.model.EntryBookChangedEvent;
import seedu.multitasky.commons.events.storage.EntryBookToRedoEvent;
import seedu.multitasky.commons.events.storage.EntryBookToUndoEvent;
import seedu.multitasky.commons.events.storage.FilePathChangedEvent;
import seedu.multitasky.commons.events.storage.LoadDataFromFilePathEvent;
import seedu.multitasky.commons.exceptions.DataConversionException;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.storage.JsonUserPrefsStorage;
import seedu.multitasky.storage.Storage;
import seedu.multitasky.storage.StorageManager;
import seedu.multitasky.storage.XmlEntryBookStorage;

//@@author A0132788U
public class MainAppTest extends MainApp {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**********************
     * Corrupted XML file *
     *********************/
    @Test
    public void initModelManager_corruptedFile_exception() {
        // Create a Model which called initModelManager to open a corrupted data file
        UserPrefs userPrefs = new UserPrefs();
        Storage storage = new StorageManager(new XmlEntryBookStorage("./src/test/data/MainAppTest/corrupted.xml"),
                new JsonUserPrefsStorage("dummy"), new UserPrefs());
        Model model = initModelManager(storage, userPrefs);
        assertEquals(model, new ModelManager(new EntryBook(), userPrefs));
    }

    /*******************************
     * Catching storage exceptions *
     ******************************/
    @Test
    public void initModelManager_dataConversionException_newEntryBook() {
        UserPrefs userPrefs = new UserPrefs();
        storage = new StorageStub(true);
        Model model = initModelManager(storage, userPrefs);
        assertEquals(model, new ModelManager(new EntryBook(), userPrefs));
    }

    @Test
    public void initModelManager_ioException_newEntryBook() {
        UserPrefs userPrefs = new UserPrefs();
        storage = new StorageStub(false);
        Model model = initModelManager(storage, userPrefs);
        assertEquals(model, new ModelManager(new EntryBook(), userPrefs));
    }


    /**
     * Stub class for throwing exceptions.
     */
    private class StorageStub implements Storage {

        private boolean toThrowDataConversionException = false;

        public StorageStub(boolean toThrowDataConversionException) {
            this.toThrowDataConversionException = toThrowDataConversionException;
        }

        @Override
        public void setEntryBookFilePath(String newFilePath) {
            fail("This method should not be called");
        }

        @Override
        public Optional<ReadOnlyEntryBook> readEntryBook(String filePath)
                throws DataConversionException, IOException {
            fail("This method should not be called");
            return null;
        }

        public Optional<ReadOnlyEntryBook> readEntryBook() throws DataConversionException, IOException {
            if (toThrowDataConversionException) {
                throw new DataConversionException(null);
            } else {
                throw new IOException();
            }
        }

        @Override
        public void saveEntryBook(ReadOnlyEntryBook entryBook, String filePath) throws IOException {
            fail("This method should not be called");
        }

        @Override
        public void saveEntryBook(ReadOnlyEntryBook entryBook) throws IOException {
            fail("This method should not be called");
        }

        @Override
        public String getUserPrefsFilePath() {
            fail("This method should not be called");
            return null;
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
            fail("This method should not be called");
            return null;
        }

        @Override
        public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
            fail("This method should not be called");
        }

        @Override
        public String getEntryBookFilePath() {
            fail("This method should not be called");
            return null;
        }

        @Override
        public void handleEntryBookChangedEvent(EntryBookChangedEvent e) {

        }

        @Override
        public void handleEntryBookToUndoEvent(EntryBookToUndoEvent e) throws Exception {
            fail("This method should not be called");
        }

        @Override
        public void handleEntryBookToRedoEvent(EntryBookToRedoEvent e) throws Exception {
            fail("This method should not be called");
        }

        @Override
        public void handleFilePathChangedEvent(FilePathChangedEvent event) throws IOException {
            fail("This method should not be called");
        }

        @Override
        public void handleLoadDataFromFilePathEvent(LoadDataFromFilePathEvent event)
                throws Exception {
            fail("This method should not be called");
        }


    }

}
