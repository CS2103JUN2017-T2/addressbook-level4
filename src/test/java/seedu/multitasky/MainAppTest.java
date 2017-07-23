package seedu.multitasky;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.storage.JsonUserPrefsStorage;
import seedu.multitasky.storage.Storage;
import seedu.multitasky.storage.StorageManager;
import seedu.multitasky.storage.XmlEntryBookStorage;
//@@author A0132788U

public class MainAppTest extends MainApp {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /************************
     * Corrupted XML file *
     ***********************/
    @Test
    public void initModelManager_corruptedFile_exception() {
        // Create a Model which called initModelManager to open a corrupted data file
        UserPrefs userPrefs = new UserPrefs();
        Storage storage = new StorageManager(new XmlEntryBookStorage("./src/test/data/MainAppTest/corrupted.xml"),
                new JsonUserPrefsStorage("dummy"), new UserPrefs());
        Model model = initModelManager(storage, userPrefs);
        assertEquals(model, new ModelManager(new EntryBook(), userPrefs));
    }
}
