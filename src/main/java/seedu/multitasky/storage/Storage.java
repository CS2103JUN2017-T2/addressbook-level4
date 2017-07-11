package seedu.multitasky.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.multitasky.commons.events.model.EntryBookChangedEvent;
import seedu.multitasky.commons.events.model.EntryBookToUndoEvent;
import seedu.multitasky.commons.events.storage.DataSavingExceptionEvent;
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
    void handleEntryBookChangedEvent(EntryBookChangedEvent abce);

    /**
     * Changes the current version of the Entry Book to the previous one on the hard disk.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     *
     * @throws DataConversionException
     * @throws Exception
     */
    void handleEntryBookToUndoEvent(EntryBookToUndoEvent defg) throws DataConversionException, Exception;
}
