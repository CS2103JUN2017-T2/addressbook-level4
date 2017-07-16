package seedu.multitasky.testutil;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;

//@@author A0126623L
/**
 * Utility class containing the constants required for tests related to EditCommand
 */
public class EditCommandTestUtil {
    public static final String VALID_NAME_CLEAN = "Clean the house";
    public static final String VALID_NAME_MEETING = "Meet the boss";
    public static final String VALID_TAG_URGENT = "urgent";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_DATE_12_JULY_17 = "12 july 17";
    public static final String VALID_DATE_11_JULY_17 = "11 july 17";
    public static final String VALID_DATE_20_DEC_17 = "12 dec 17";

    public static final EditEntryDescriptor DESC_CLEAN;
    public static final EditEntryDescriptor DESC_MEETING;

    static {
        try {
            DESC_CLEAN = new EditEntryDescriptorBuilder().withName(VALID_NAME_CLEAN)
                    .withTags(VALID_TAG_FRIEND).build();
            DESC_MEETING = new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING)
                    .withTags(VALID_TAG_URGENT, VALID_TAG_FRIEND).build();
        } catch (IllegalValueException ive) {
            throw new AssertionError("Method should not fail.");
        }
    }
}
