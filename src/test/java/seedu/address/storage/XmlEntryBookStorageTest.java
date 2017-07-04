package seedu.address.storage;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.EntryBook;
import seedu.address.model.ReadOnlyEntryBook;
import seedu.address.model.entry.Entry;
import seedu.address.testutil.TypicalEntries;

public class XmlEntryBookStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlEntryBookStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readEntryBook_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readEntryBook(null);
    }

    private java.util.Optional<ReadOnlyEntryBook> readEntryBook(String filePath) throws Exception {
        return new XmlEntryBookStorage(filePath).readEntryBook(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readEntryBook("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readEntryBook("NotXmlFormatEntryBook.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readAndSaveEntryBook_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempEntryBook.xml";
        TypicalEntries td = new TypicalEntries();
        EntryBook original = td.getTypicalEntryBook();
        XmlEntryBookStorage xmlEntryBookStorage = new XmlEntryBookStorage(filePath);

        //Save in new file and read back
        xmlEntryBookStorage.saveEntryBook(original, filePath);
        ReadOnlyEntryBook readBack = xmlEntryBookStorage.readEntryBook(filePath).get();
        assertEquals(original, new EntryBook(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addEntry(new Entry(td.hoon)); //wait for Ping Chan Chua to modify his tests
        original.removeEntry(new Entry(td.alice)); //wait for Ping Chan Chua to modify his tests
        xmlEntryBookStorage.saveEntryBook(original, filePath);
        readBack = xmlEntryBookStorage.readEntryBook(filePath).get();
        assertEquals(original, new EntryBook(readBack));

        //Save and read without specifying file path
        original.addEntry(new Entry(td.ida));
        xmlEntryBookStorage.saveEntryBook(original); //file path not specified
        readBack = xmlEntryBookStorage.readEntryBook().get(); //file path not specified
        assertEquals(original, new EntryBook(readBack));

    }

    @Test
    public void saveEntryBook_nullEntryBook_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveEntryBook(null, "SomeFile.xml");
    }

    private void saveEntryBook(ReadOnlyEntryBook addressBook, String filePath) throws IOException {
        new XmlEntryBookStorage(filePath).saveEntryBook(addressBook, addToTestDataPathIfNotNull(filePath));
    }

    @Test
    public void saveEntryBook_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveEntryBook(new EntryBook(), null);
    }


}
