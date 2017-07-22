package seedu.multitasky.commons.util.match;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// @@author A0125586X
public class PrefixMatchTest {

    @Test
    public void prefixMatch_matchNullInput_null() {
        assertTrue(new PrefixMatch().match(null, "abc", "xyz") == null);
        assertTrue(new PrefixMatch().match("abc") == null);
        assertFalse(new PrefixMatch().isMatch(null, "abc"));
        assertFalse(new PrefixMatch().isMatch("abc", null));
    }

    @Test
    public void prefixMatch_matchNone_null() {
        assertTrue(new PrefixMatch().match("123", "abc") == null);
        assertFalse(new PrefixMatch().isMatch("123", "abc"));
    }

    @Test
    public void prefixMatch_matchPrefix_match() {
        assertTrue(new PrefixMatch().match("abc", "xyzabc", "abcxyz").equals("abcxyz"));
        assertTrue(new PrefixMatch().isMatch("abc", "abcxyz"));
    }

}
