package seedu.multitasky.commons.events.storage;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.ReadOnlyEntryBook;

// @@author A0125586X
public class FilePathChangedEventTest {

    @Test
    public void filePathChangedEvent_constructor_success() {
        ReadOnlyEntryBook book = new EntryBook();
        String newFilePath = "newfilepath.xml";
        String expectedString = "File path is: " + newFilePath;
        FilePathChangedEvent event = new FilePathChangedEvent(book, newFilePath);
        assertTrue(event.getNewFilePath().equals(newFilePath));
        assertTrue(event.data.equals(book));
        assertTrue(event.toString().equals(expectedString));
    }

    @Test
    public void filePathChangedEvent_setter_changed() {
        ReadOnlyEntryBook book = new EntryBook();
        String newFilePath = "newfilepath.xml";
        String changedFilePath = "changedfilepath.xml";
        FilePathChangedEvent event = new FilePathChangedEvent(book, newFilePath);
        assertTrue(event.getNewFilePath().equals(newFilePath));
        event.setNewFilePath(changedFilePath);
        assertTrue(event.getNewFilePath().equals(changedFilePath));
    }

}
