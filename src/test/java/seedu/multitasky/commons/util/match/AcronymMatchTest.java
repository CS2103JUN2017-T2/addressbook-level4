package seedu.multitasky.commons.util.match;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// @@author A0125586X
public class AcronymMatchTest {

    @Test
    public void acronymMatch_matchNullInput_null() {
        assertTrue(new AcronymMatch().match(null, "abc", "xyz") == null);
        assertTrue(new AcronymMatch().match("abc") == null);
        assertFalse(new AcronymMatch().isMatch(null, "abc"));
        assertFalse(new AcronymMatch().isMatch("abc", null));
    }

    @Test
    public void acronymMatch_matchNone_null() {
        assertTrue(new AcronymMatch().match("123", "abc") == null);
        assertFalse(new AcronymMatch().isMatch("123", "abc"));
    }

    @Test
    public void acronymMatch_matchAcronym_match() {
        assertTrue(new AcronymMatch().match("abc", "xyzab", "axbycz").equals("axbycz"));
        assertTrue(new AcronymMatch().isMatch("abc", "axbycz"));
    }

}
