package seedu.multitasky.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class StorageUserPrefsTest {
    private StorageUserPrefs storageUserPrefs;

    @Before
    public void setUp() {
        storageUserPrefs = new UserPrefs();
    }

    /***************************
     * Unit Test
     **************************/
    @Test
    public void getEntryBookSnapshotPath() {
        assertNotNull(StorageUserPrefs.getEntryBookSnapshotPath());
    }

    @Test
    public void setEntryBookSnapshotPath() {
        StorageUserPrefs.setEntryBookSnapshotPath("test.xml");
        assertEquals(StorageUserPrefs.getEntryBookSnapshotPath(), "test.xml");
    }

    @Test
    public void indexTests() {
        int index = StorageUserPrefs.getIndex();
        StorageUserPrefs.incrementIndexByOne();
        assertEquals(index + 1, StorageUserPrefs.getIndex());
        StorageUserPrefs.decrementIndexByOne();
        assertEquals(index, StorageUserPrefs.getIndex());
        index = 100;
        StorageUserPrefs.setIndex(index);
        assertEquals(StorageUserPrefs.getIndex(), index);
    }

}
