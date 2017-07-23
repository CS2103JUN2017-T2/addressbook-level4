package seedu.multitasky.commons.util.match;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// @@author A0125586X
public class WrongExtra1MatchTest {

    @Test
    public void wrongExtra1Match_matchNullInput_null() {
        assertTrue(new WrongExtra1Match().match(null, "abc", "xyz") == null);
        assertTrue(new WrongExtra1Match().match("abc") == null);
        assertFalse(new WrongExtra1Match().isMatch(null, "abc"));
        assertFalse(new WrongExtra1Match().isMatch("abc", null));
    }

    @Test
    public void wrongExtra1Match_matchNone_null() {
        assertTrue(new WrongExtra1Match().match("123", "abc") == null);
        assertFalse(new WrongExtra1Match().isMatch("123", "abc"));
    }

    @Test
    public void wrongExtra1Match_matchBelowMinLength_null() {
        assertTrue(new WrongExtra1Match().match("adb", "abcxyz", "xyz") == null);
        assertFalse(new WrongExtra1Match().isMatch("adb", "abcxyz"));
    }

    @Test
    public void wrongExtra1Match_matchWrongExtra1_match() {
        assertTrue(new WrongExtra1Match().match("abdc", "abcxyz", "xyz").equals("abcxyz"));
        assertTrue(new WrongExtra1Match().isMatch("abdc", "abcxyz"));
    }

}
