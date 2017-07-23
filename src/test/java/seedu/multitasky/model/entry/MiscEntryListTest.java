package seedu.multitasky.model.entry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.testutil.SampleEntries;

//@@author A0126623L
public class MiscEntryListTest {

    /**
     * @return MiscEntryList containing samples of active entries
     * @throws DuplicateEntryException
     */
    public static MiscEntryList getSampleMiscActiveEntryList() throws DuplicateEntryException {
        Entry[] activeEventArray = SampleEntries.getSampleActiveEvents();
        Entry[] activeDeadlineArray = SampleEntries.getSampleActiveDeadlines();
        Entry[] activeFloatingTaskArray = SampleEntries.getSampleActiveFloatingTasks();

        MiscEntryList miscEntryList = new MiscEntryList();
        miscEntryList.add(activeEventArray[0]);
        miscEntryList.add(activeDeadlineArray[0]);
        miscEntryList.add(activeFloatingTaskArray[0]);

        return miscEntryList;
    }

    @Test
    public void add_sampleEntries_success() throws DuplicateEntryException {
        Entry[] activeEventArray = SampleEntries.getSampleActiveEvents();
        Entry[] activeDeadlineArray = SampleEntries.getSampleActiveDeadlines();
        Entry[] activeFloatingTaskArray = SampleEntries.getSampleActiveFloatingTasks();

        MiscEntryList miscEntryListUnderTest = MiscEntryListTest.getSampleMiscActiveEntryList();

        assertTrue(miscEntryListUnderTest.contains(activeEventArray[0]));
        assertTrue(miscEntryListUnderTest.contains(activeDeadlineArray[0]));
        assertTrue(miscEntryListUnderTest.contains(activeFloatingTaskArray[0]));
    }

    @Test
    public void setEntries_resetWithSingleFloatingTask_success() throws IllegalValueException {
        MiscEntryList miscEntryListUnderTest = MiscEntryListTest.getSampleMiscActiveEntryList();
        MiscEntryList referenceEntryList = MiscEntryListTest.getSampleMiscActiveEntryList();

        FloatingTask sampleFloatingTask = (FloatingTask) EntryBuilder.build("randomEntry");
        assertTrue(sampleFloatingTask instanceof FloatingTask);
        ArrayList<ReadOnlyEntry> listForReset = new ArrayList<ReadOnlyEntry>();
        listForReset.add(sampleFloatingTask);

        miscEntryListUnderTest.setEntries(listForReset);

        assertTrue(miscEntryListUnderTest.contains(sampleFloatingTask));
        assertFalse(miscEntryListUnderTest.equals(referenceEntryList));
    }
}
