package seedu.multitasky.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.multitasky.commons.exceptions.DataConversionException;
import seedu.multitasky.model.ReadOnlyEntryBook;

/**
 * Represents a storage for {@link seedu.multitasky.model.EntryBook}.
 */
public interface EntryBookStorage {

    /**
     * Returns the file path of the data file.
     */
    String getEntryBookFilePath();

    /**
     * Returns the file path of the snapshot folder.
     */
    String getEntryBookSnapshotPath();

    /**
     * Returns EntryBook data as a {@link ReadOnlyEntryBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyEntryBook> readEntryBook() throws DataConversionException, IOException;

    /**
     * @see #getEntryBookFilePath()
     */
    Optional<ReadOnlyEntryBook> readEntryBook(String filePath) throws DataConversionException, IOException;

    /**
     * Sets the file path of the new entrybook.
     */
    void setEntryBookFilePath(String newFilePath);

    /**
     * Saves the given {@link ReadOnlyEntryBook} to the storage.
     *
     * @param entryBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveEntryBook(ReadOnlyEntryBook entryBook) throws IOException;

    /**
     * @see #saveEntryBook(ReadOnlyEntryBook)
     */
    void saveEntryBook(ReadOnlyEntryBook entryBook, String filePath) throws IOException;

}
