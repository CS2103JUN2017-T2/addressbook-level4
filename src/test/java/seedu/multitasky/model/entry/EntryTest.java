package seedu.multitasky.model.entry;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.util.EntryBuilder;

public class EntryTest {

    // @@author A0126623L
    @Test
    public void setState_invokeSetState_success() throws IllegalValueException {
        Entry eventUnderTest = EntryBuilder.build("dummyName");
        assertTrue(eventUnderTest.getState().toString().equals("ACTIVE"));

        eventUnderTest.setState(Entry.State.ARCHIVED);
        assertTrue(eventUnderTest.getState().toString().equals("ARCHIVED"));

        eventUnderTest.setState(Entry.State.DELETED);
        assertTrue(eventUnderTest.getState().toString().equals("DELETED"));
    }

}
