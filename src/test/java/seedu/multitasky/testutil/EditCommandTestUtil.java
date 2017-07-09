package seedu.multitasky.testutil;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;

//@@author A0126623L
/**
 * Utility class containing the constants required for tests related to EditCommand
 */
public class EditCommandTestUtil {
    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final EditEntryDescriptor DESC_AMY;
    public static final EditEntryDescriptor DESC_BOB;

    static {
        try {
            DESC_AMY = new EditEntryDescriptorBuilder().withName(VALID_NAME_AMY)
                    .withTags(VALID_TAG_FRIEND).build();
            DESC_BOB = new EditEntryDescriptorBuilder().withName(VALID_NAME_BOB)
                    .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        } catch (IllegalValueException ive) {
            throw new AssertionError("Method should not fail.");
        }
    }
}
