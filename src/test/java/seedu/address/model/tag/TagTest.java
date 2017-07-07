package seedu.address.model.tag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TagTest {

    // @@author A0126623L
    @Test
    public void isValidTagName() {
        // invalid tagName
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName(" ")); // spaces only
        assertFalse(Tag.isValidTagName("^")); // only non-alphanumeric characters
        assertFalse(Tag.isValidTagName("/test")); // non-alphanumeric as first character
        assertFalse(Tag.isValidTagName("cs2103 project")); // alphanumeric with space
        assertFalse(Tag.isValidTagName("abc ")); // trailing whitespace

        // valid tagName
        assertTrue(Tag.isValidTagName("abc")); // joined alphabets only
        assertTrue(Tag.isValidTagName("12345")); // numbers only
        assertTrue(Tag.isValidTagName("cs2103_project")); // underscore
    }

    // @@author A0126623L
    @Test
    public void equalityTests() {

        // initialize
        try {
            Tag tag1 = new Tag("cs2103");
            Tag tag2 = new Tag("cs2103");
            Tag tag3 = new Tag("cs2010");

            // equality
            assertTrue(tag1.equals(tag2)); // Meaningfully equal

            // inequality
            assertFalse(tag1 == tag3);
            assertFalse(tag1.equals(tag3));

        } catch (Exception e) {
            fail("Invalid name used in test.");
        }
    }
}
