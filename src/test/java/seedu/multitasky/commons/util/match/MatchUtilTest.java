package seedu.multitasky.commons.util.match;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

// @@author A0125586X
public class MatchUtilTest {

    @Test
    public void matchUtil_getRegexMatchList_noSingleMatch() {
        List<String> regexes = Arrays.asList(new String[] { MatchUtil.REGEX_PRESENT_NON_WHITESPACE,
                                                            MatchUtil.REGEX_PRESENT_NON_WHITESPACE });
        assertTrue(MatchUtil.getRegexMatch(regexes, "abc", "xyz", "123") == null);
    }

    @Test
    public void matchUtil_getRegexMatchList_correct() {
        List<String> regexes = Arrays.asList(new String[] { MatchUtil.REGEX_PRESENT_NON_WHITESPACE,
                                                            MatchUtil.REGEX_PRESENT_NON_WHITESPACE,
                                                            "abc" });
        assertTrue(MatchUtil.getRegexMatch(regexes, "abc", "xyz", "123").equals("abc"));
    }

    @Test
    public void matchUtil_filterMatchesNotOne_null() {
        assertTrue(MatchUtil.filterMatches(null) == null);
        List<String> matches = Arrays.asList(new String[] { "abc", "xyz" });
        assertTrue(MatchUtil.filterMatches(matches) == null);
        matches = Arrays.asList(new String[] { "abc" });
        assertTrue(MatchUtil.filterMatches(matches).equals("abc"));
    }

}
