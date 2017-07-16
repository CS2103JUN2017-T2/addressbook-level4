package seedu.multitasky.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// @@author A0125586X
public class PowerMatchTest {

    @Test
    public void powerMatch_substring_match() {
        assertTrue(PowerMatch.match("abc", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_substring_isMatch() {
        assertTrue(PowerMatch.isMatch("abc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_prefix_match() {
        assertTrue(PowerMatch.match("abc", new String[] {"xyzabcxyz", "abcdef"}).equals("abcdef"));
    }

    @Test
    public void powerMatch_prefix_isMatch() {
        assertTrue(PowerMatch.isMatch("abc", "abcdef"));
    }

    @Test
    public void powerMatch_acronym_match() {
        assertTrue(PowerMatch.match("abc", new String[] {"axyzbxyzcxyz"}).equals("axyzbxyzcxyz"));
    }

    @Test
    public void powerMatch_acronym_ismatch() {
        assertTrue(PowerMatch.isMatch("abc", "axyzbxyzcxyz"));
    }

    @Test
    public void powerMatch_permutation_match() {
        assertTrue(PowerMatch.match("cba", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_permutation_isMatch() {
        assertTrue(PowerMatch.isMatch("abc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_missingInner_match() {
        assertTrue(PowerMatch.match("ac", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_missingInner_isMatch() {
        assertTrue(PowerMatch.isMatch("ac", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra1Inner_match() {
        assertTrue(PowerMatch.match("amc", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra1Inner_isMatch() {
        assertTrue(PowerMatch.isMatch("amc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra2Inner_match() {
        assertTrue(PowerMatch.match("amnc", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra2Inner_isMatch() {
        assertTrue(PowerMatch.isMatch("amnc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra3Inner_match() {
        assertTrue(PowerMatch.match("amnoc", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra3Inner_isMatch() {
        assertTrue(PowerMatch.isMatch("amnoc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_matchNullEmptyArguments_nullResult() {
        assertTrue(PowerMatch.match(null, (String[]) null) == null);
        assertTrue(PowerMatch.match(null, new String[] {"xyzabcxyz"}) == null);
        assertTrue(PowerMatch.match("", new String[] {"xyzabcxyz"}) == null);
        assertTrue(PowerMatch.match("abc", (String[]) null) == null);
    }

    @Test
    public void powerMatch_isMatchNullArguments_falseResult() {
        assertFalse(PowerMatch.isMatch(null, null));
        assertFalse(PowerMatch.isMatch(null, ""));
    }

    @Test
    public void powerMatch_isMatchEmptyInput_trueResult() {
        assertTrue(PowerMatch.isMatch("", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_match_noMatches() {
        assertTrue(PowerMatch.match("abcdef", "lmnopq") == null);
    }

    @Test
    public void powerMatch_isMatch_noMatches() {
        assertFalse(PowerMatch.isMatch("abcdef", "lmnopq"));
    }

}
