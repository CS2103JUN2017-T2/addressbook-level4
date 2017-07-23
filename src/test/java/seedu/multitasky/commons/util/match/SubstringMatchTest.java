package seedu.multitasky.commons.util.match;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// @@author A0125586X
public class SubstringMatchTest {

    @Test
    public void substringMatch_matchNullInput_null() {
        assertTrue(new SubstringMatch().match(null, "abc", "xyz") == null);
        assertTrue(new SubstringMatch().match("abc") == null);
        assertFalse(new SubstringMatch().isMatch(null, "abc"));
        assertFalse(new SubstringMatch().isMatch("abc", null));
    }

    @Test
    public void substringMatch_matchNone_null() {
        assertTrue(new SubstringMatch().match("123", "abc") == null);
        assertFalse(new SubstringMatch().isMatch("123", "abc"));
    }

    @Test
    public void substringMatch_matchSubstring_match() {
        assertTrue(new SubstringMatch().match("abc", "xyz", "xyabcxy").equals("xyabcxy"));
        assertTrue(new SubstringMatch().isMatch("abc", "xyabcxy"));
    }

}
