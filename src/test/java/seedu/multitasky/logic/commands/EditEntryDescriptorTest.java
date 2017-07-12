package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_AMY;
import static seedu.multitasky.testutil.EditCommandTestUtil.DESC_BOB;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_NAME_BOB;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_TAG_HUSBAND;

import org.junit.Test;

import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.testutil.EditEntryDescriptorBuilder;

public class EditEntryDescriptorTest {

    @Test
    public void equals() throws Exception {
        // same values -> returns true
        EditEntryDescriptor descriptorWithSameValues = new EditEntryDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditEntryDescriptor editedAmy = new EditEntryDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditEntryDescriptorBuilder(DESC_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }
}
