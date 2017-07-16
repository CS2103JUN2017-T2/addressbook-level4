package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_CLEAN;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_MEETING;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_NAME_MEETING;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_TAG_URGENT;

import org.junit.Test;

import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.testutil.EditEntryDescriptorBuilder;

public class EditEntryDescriptorTest {

    @Test
    public void equals() throws Exception {
        // same values -> returns true
        EditEntryDescriptor descriptorWithSameValues = new EditEntryDescriptor(DESC_CLEAN);
        assertTrue(DESC_CLEAN.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_CLEAN.equals(DESC_CLEAN));

        // null -> returns false
        assertFalse(DESC_CLEAN == null);

        // different types -> returns false
        assertFalse(DESC_CLEAN.equals(5));

        // different values -> returns false
        assertFalse(DESC_CLEAN.equals(DESC_MEETING));

        // different name -> returns false
        EditEntryDescriptor editedAmy = new EditEntryDescriptorBuilder(DESC_CLEAN).withName(VALID_NAME_MEETING).build();
        assertFalse(DESC_CLEAN.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditEntryDescriptorBuilder(DESC_CLEAN).withTags(VALID_TAG_URGENT).build();
        assertFalse(DESC_CLEAN.equals(editedAmy));
    }
}
