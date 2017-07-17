package seedu.multitasky.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// @@author A0125586X
public class PowerMatchTest {

    @Test
    public void powerMatch_substring_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_0, "abc", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_substring_isMatch() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_0, "abc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_prefix_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_0, "abc", new String[] {"xyzabcxyz", "abcdef"})
                             .equals("abcdef"));
    }

    @Test
    public void powerMatch_prefix_isMatch() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_0, "abc", "abcdef"));
    }

    @Test
    public void powerMatch_acronym_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_0, "abc", new String[] {"axybxycxy"}).equals("axybxycxy"));
    }

    @Test
    public void powerMatch_acronym_ismatch() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_0, "abc", "axyzbxyzcxyz"));
    }

    @Test
    public void powerMatch_permutation_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_1, "cba", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_permutation_isMatch() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_1, "abc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_missingChar_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_2, "ac", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_missingChar_isMatch() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_2, "ac", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra1_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_3, "amc", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra1_isMatch() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_3, "amc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra2_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_4, "amnc", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra2_isMatch() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_4, "amnc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra3_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_5, "amnoc", new String[] {"xyzabcxyz"}).equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_wrongExtra3_isMatch() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_5, "amnoc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_matchNullEmptyArguments_nullResult() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_6, null, (String[]) null) == null);
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_6, null, new String[] {"xyzabcxyz"}) == null);
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_6, "", new String[] {"xyzabcxyz"}) == null);
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_6, "abc", (String[]) null) == null);
    }

    @Test
    public void powerMatch_isMatchNullArguments_falseResult() {
        assertFalse(PowerMatch.isMatch(PowerMatch.Level.LEVEL_6, null, null));
        assertFalse(PowerMatch.isMatch(PowerMatch.Level.LEVEL_6, null, ""));
    }

    @Test
    public void powerMatch_isMatchEmptyInput_trueResult() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_6, "", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_match_noMatches() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_6, "abcdef", "lmnopq") == null);
    }

    @Test
    public void powerMatch_isMatch_noMatches() {
        assertFalse(PowerMatch.isMatch(PowerMatch.Level.LEVEL_6, "abcdef", "lmnopq"));
    }

}
