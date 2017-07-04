package seedu.address.storage;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.XmlUtil;

/**
 * Stores entrybook data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given entrybook data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableEntryBook entryBook)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, entryBook);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Returns entry book in the file or an empty entry book
     */
    public static XmlSerializableEntryBook loadDataFromSaveFile(File file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableEntryBook.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
