package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.EntryBookChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of EntryBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private EntryBookStorage entryBookStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(EntryBookStorage entryBookStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.entryBookStorage = entryBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public String getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ EntryBook methods ==============================

    @Override
    public String getEntryBookFilePath() {
        return entryBookStorage.getEntryBookFilePath();
    }

    @Override
    public Optional<ReadOnlyEntryBook> readEntryBook() throws DataConversionException, IOException {
        return readEntryBook(entryBookStorage.getEntryBookFilePath());
    }

    @Override
    public Optional<ReadOnlyEntryBook> readEntryBook(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return entryBookStorage.readEntryBook(filePath);
    }

    @Override
    public void saveEntryBook(ReadOnlyEntryBook entryBook) throws IOException {
        saveEntryBook(entryBook, entryBookStorage.getEntryBookFilePath());
    }

    @Override
    public void saveEntryBook(ReadOnlyEntryBook addressBook, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        entryBookStorage.saveEntryBook(addressBook, filePath);
    }


    @Override
    @Subscribe
    public void handleEntryBookChangedEvent(EntryBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveEntryBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
    
}
