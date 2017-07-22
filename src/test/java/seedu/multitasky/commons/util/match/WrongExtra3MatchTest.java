package seedu.multitasky.commons.util.match;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// @@author A0125586X
public class WrongExtra3MatchTest {

    @Test
    public void wrongExtra3Match_matchNullInput_null() {
        assertTrue(new WrongExtra3Match().match(null, "abc", "xyz") == null);
        assertTrue(new WrongExtra3Match().match("abc") == null);
        assertFalse(new WrongExtra3Match().isMatch(null, "abc"));
        assertFalse(new WrongExtra3Match().isMatch("abc", null));
    }

    @Test
    public void wrongExtra3Match_matchNone_null() {
        assertTrue(new WrongExtra3Match().match("123", "abc") == null);
        assertFalse(new WrongExtra3Match().isMatch("123", "abc"));
    }

    @Test
    public void wrongExtra3Match_matchBelowMinLength_null() {
        assertTrue(new WrongExtra3Match().match("abcde", "abxyz", "xyz") == null);
        assertFalse(new WrongExtra3Match().isMatch("abcde", "abxyz"));
    }

    @Test
    public void wrongExtra3Match_matchAboveMaxLength_null() {
        assertTrue(new WrongExtra3Match().match("abcdefghijklmno", "abcdefghijklm", "xyz") == null);
        assertFalse(new WrongExtra3Match().isMatch("abcdefghijklmno", "abcdefghijklm"));
    }

    @Test
    public void wrongExtra3Match_matchWrongExtra3_match() {
        assertTrue(new WrongExtra3Match().match("abgcfde", "abcdxyz", "xyz").equals("abcdxyz"));
        assertTrue(new WrongExtra3Match().match("abcdefghijklmn", "abcdefghijk", "xyz").equals("abcdefghijk"));
        assertTrue(new WrongExtra3Match().isMatch("abgcfde", "abcdxyz"));
        assertTrue(new WrongExtra3Match().isMatch("abcdefghijklmn", "abcdefghijk"));
    }

}
