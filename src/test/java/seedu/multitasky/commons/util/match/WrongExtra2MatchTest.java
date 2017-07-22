package seedu.multitasky.commons.util.match;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// @@author A0125586X
public class WrongExtra2MatchTest {

    @Test
    public void wrongExtra2Match_matchNullInput_null() {
        assertTrue(new WrongExtra2Match().match(null, "abc", "xyz") == null);
        assertTrue(new WrongExtra2Match().match("abc") == null);
        assertFalse(new WrongExtra2Match().isMatch(null, "abc"));
        assertFalse(new WrongExtra2Match().isMatch("abc", null));
    }

    @Test
    public void wrongExtra2Match_matchNone_null() {
        assertTrue(new WrongExtra2Match().match("123", "abc") == null);
        assertFalse(new WrongExtra2Match().isMatch("123", "abc"));
    }

    @Test
    public void wrongExtra2Match_matchBelowMinLength_null() {
        assertTrue(new WrongExtra2Match().match("adb", "abcxyz", "xyz") == null);
        assertFalse(new WrongExtra2Match().isMatch("adb", "abcxyz"));
    }

    @Test
    public void wrongExtra2Match_matchAboveMaxLength_null() {
        assertTrue(new WrongExtra2Match().match("abcdefghijklmno", "abcdefghijklm", "xyz") == null);
        assertFalse(new WrongExtra2Match().isMatch("abcdefghijklmno", "abcdefghijklm"));
    }

    @Test
    public void wrongExtra2Match_matchWrongExtra2_match() {
        assertTrue(new WrongExtra2Match().match("aebdc", "abcxyz", "xyz").equals("abcxyz"));
        assertTrue(new WrongExtra2Match().match("abcdefghijklmn", "abcdefghijkl", "xyz").equals("abcdefghijkl"));
        assertTrue(new WrongExtra2Match().isMatch("aebdc", "abcxyz"));
        assertTrue(new WrongExtra2Match().isMatch("abcdefghijklmn", "abcdefghijkl"));
    }

}
