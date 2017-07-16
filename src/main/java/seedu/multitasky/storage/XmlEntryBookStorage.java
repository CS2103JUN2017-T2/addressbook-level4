package seedu.multitasky.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.multitasky.commons.core.LogsCenter;
import seedu.multitasky.commons.exceptions.DataConversionException;
import seedu.multitasky.commons.util.FileUtil;
import seedu.multitasky.model.ReadOnlyEntryBook;

/**
 * A class to access EntryBook data stored as an xml file on the hard disk.
 */
public class XmlEntryBookStorage implements EntryBookStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlEntryBookStorage.class);

    private String filePath;
    private String snapshotPath;
    private String previousSnapshotPath;

    public XmlEntryBookStorage(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getEntryBookFilePath() {
        return filePath;
    }

    @Override
    public void setEntryBookFilePath(String newFilePath) {
        filePath = newFilePath;
    }

    @Override
    public String getEntryBookSnapshotPath() {
        return snapshotPath;
    }

    public String getPreviousEntryBookSnapshotPath() {
        return previousSnapshotPath;
    }

    @Override
    public Optional<ReadOnlyEntryBook> readEntryBook() throws DataConversionException, IOException {
        return readEntryBook(filePath);
    }

    /**
     * Similar to {@link #readEntryBook()}
     *
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    @Override
    public Optional<ReadOnlyEntryBook> readEntryBook(String filePath) throws DataConversionException,
            FileNotFoundException {
        requireNonNull(filePath);

        File entryBookFile = new File(filePath);

        if (!entryBookFile.exists()) {
            logger.info("EntryBook file " + entryBookFile + " not found");
            return Optional.empty();
        }

        ReadOnlyEntryBook entryBookOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(entryBookOptional);
    }

    @Override
    public void saveEntryBook(ReadOnlyEntryBook entryBook) throws IOException {
        saveEntryBook(entryBook, filePath);
    }

    /**
     * Similar to {@link #saveEntryBook(ReadOnlyEntryBook)}
     *
     * @param filePath location of the data. Cannot be null
     */
    @Override
    public void saveEntryBook(ReadOnlyEntryBook entryBook, String filePath) throws IOException {
        requireNonNull(entryBook);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableEntryBook(entryBook));
    }

}
