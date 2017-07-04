package seedu.address.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import seedu.address.testutil.EntryBookBuilder;
import seedu.address.testutil.TypicalEntries;

//@@author A0126623L
public class ModelManagerTest {

    private TypicalEntries typicalEntries = new TypicalEntries();

    @Test
    public void equals() throws Exception {
        EntryBook entryBook = new EntryBookBuilder().withEntry(typicalEntries.alice)
                .withEntry(typicalEntries.benson).build();
        EntryBook differentEntryBook = new EntryBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        ModelManager modelManager = new ModelManager(entryBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(entryBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different entryBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentEntryBook, userPrefs)));

        // different filteredList -> returns false
        modelManager.updateFilteredFloatingTaskList(new HashSet<>(
                Arrays.asList(typicalEntries.alice.getName().fullName.split(" "))));
        assertFalse(modelManager.equals(new ModelManager(entryBook, userPrefs)));
        modelManager.updateFilteredListToShowAll(); // resets modelManager to initial state for upcoming tests

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setEntryBookName("differentName");
        assertTrue(modelManager.equals(new ModelManager(entryBook, differentUserPrefs)));
    }
}
