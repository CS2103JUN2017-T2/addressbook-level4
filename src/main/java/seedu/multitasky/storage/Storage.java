package seedu.multitasky.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.multitasky.commons.events.model.EntryBookChangedEvent;
import seedu.multitasky.commons.events.storage.DataSavingExceptionEvent;
import seedu.multitasky.commons.events.storage.EntryBookToRedoEvent;
import seedu.multitasky.commons.events.storage.EntryBookToUndoEvent;
import seedu.multitasky.commons.events.storage.FilePathChangedEvent;
import seedu.multitasky.commons.events.storage.LoadDataFromFilePathEvent;
import seedu.multitasky.commons.exceptions.DataConversionException;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends EntryBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getEntryBookFilePath();

    @Override
    Optional<ReadOnlyEntryBook> readEntryBook() throws DataConversionException, IOException;

    @Override
    void saveEntryBook(ReadOnlyEntryBook entryBook) throws IOException;

    /**
     * Saves the current version of the Entry Book to the hard disk.
     * Creates the data file if it is missing.
     */
    void handleEntryBookChangedEvent(EntryBookChangedEvent e);

    // @@author A0132788U
    /**
     * Changes the current version of the Entry Book to the previous one on the hard disk.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     *
     * @throws Exception
     */
    void handleEntryBookToUndoEvent(EntryBookToUndoEvent e) throws Exception;

    /**
     * Changes the current version of the Entry Book to the next one (saved as a snapshot) on the hard disk.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     *
     * @throws Exception
     */
    void handleEntryBookToRedoEvent(EntryBookToRedoEvent e) throws Exception;

    /**
     * Changes file path in Preferences.json to save data in a new location.
     *
     * @throws IOException
     */
    void handleFilePathChangedEvent(FilePathChangedEvent event) throws IOException;

    /**
     * Loads data from a given filepath.
     *
     * @throws Exception
     */
    void handleLoadDataFromFilePathEvent(LoadDataFromFilePathEvent event) throws Exception;
}
