package seedu.multitasky.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.multitasky.commons.core.ComponentManager;
import seedu.multitasky.commons.core.LogsCenter;
import seedu.multitasky.commons.events.model.EntryBookChangedEvent;
import seedu.multitasky.commons.events.model.EntryBookToUndoEvent;
import seedu.multitasky.commons.events.storage.DataSavingExceptionEvent;
import seedu.multitasky.commons.exceptions.DataConversionException;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.UserPrefs;

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

    // @@author A0132788U
    /**
     * Gets the proper filepath of the current snapshot with index
     */
    @Override
    public String getEntryBookSnapshotPath() {
        return UserPrefs.getEntryBookSnapshotPath() + UserPrefs.getIndex() + ".xml";
    }

    @Override
    public String getPreviousEntryBookSnapshotPath() {
        UserPrefs.decrementIndexByOne();
        return UserPrefs.getEntryBookSnapshotPath() + UserPrefs.getIndex() + ".xml";
    }

    // @@author
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
    public void saveEntryBook(ReadOnlyEntryBook entryBook, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        entryBookStorage.saveEntryBook(entryBook, filePath);
    }

    public void loadPreviousEntryBook(ReadOnlyEntryBook entryBook) throws IOException {
        saveEntryBook(entryBook, entryBookStorage.getPreviousEntryBookSnapshotPath());
    }

    // @@author A0132788U
    /**
     * Gets the filepath of the most current snapshot xml file and increments index by one.
     */
    public String setEntryBookSnapshotPathAndUpdateIndex() {
        String snapshotPath = getEntryBookSnapshotPath();
        UserPrefs.incrementIndexByOne();
        return snapshotPath;
    }

    public void saveEntryBookSnapshot(ReadOnlyEntryBook entryBook) throws IOException {
        saveEntryBook(entryBook, setEntryBookSnapshotPathAndUpdateIndex());
    }

    /**
     * Saves the data to the entrybook at the filepath specified and also creates a snapshot in data/snapshots.
     */
    @Override
    @Subscribe
    public void handleEntryBookChangedEvent(EntryBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveEntryBook(event.data);
            saveEntryBookSnapshot(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    // TO IMPLEMENT - change data to previous snapshot
    @Subscribe
    public void handleEntryBookToUndoEvent(EntryBookToUndoEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Load previous snapshot"));
        try {
            loadPreviousEntryBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
