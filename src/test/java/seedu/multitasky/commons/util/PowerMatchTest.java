package seedu.multitasky.commons.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// @@author A0125586X
public class PowerMatchTest {

    /****************************************
     * Level 0 - substring, prefix, acronym *
     ***************************************/
    @Test
    public void powerMatch_matchLevel0Substring_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_0, "abc", "xyzabcxyz").equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_matchLevel0Substring_noMatch() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_0, "abc", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_matchLevel0Prefix_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_0, "abc", "abcxyz").equals("abcxyz"));
    }

    @Test
    public void powerMatch_matchLevel0Prefix_noMatch() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_0, "abc", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_matchLevel0Acronym_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_0, "abc", "axbycz").equals("axbycz"));
    }

    @Test
    public void powerMatch_matchLevel0Acronym_noMatch() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_0, "abc", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel0Substring_match() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_0, "abc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel0Substring_noMatch() {
        assertFalse(PowerMatch.isMatch(PowerMatch.Level.LEVEL_0, "abc", "xyzxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel0Prefix_match() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_0, "abc", "abcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel0Prefix_noMatch() {
        assertFalse(PowerMatch.isMatch(PowerMatch.Level.LEVEL_0, "abc", "xyzxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel0Acronym_match() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_0, "abc", "axbycz"));
    }

    @Test
    public void powerMatch_isMatchLevel0Acronym_noMatch() {
        assertFalse(PowerMatch.isMatch(PowerMatch.Level.LEVEL_0, "abc", "xyzxyz"));
    }

    /*************************
     * Level 1 - permutation *
     ************************/
    @Test
    public void powerMatch_matchLevel1PermutationSubstring_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_1, "acb", "abcxyz").equals("abcxyz"));
    }

    @Test
    public void powerMatch_matchLevel1PermutationPrefix_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_1, "acb", "abcxyz", "xyzabc").equals("abcxyz"));
    }

    @Test
    public void powerMatch_matchLevel1Permutation_noMatch() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_1, "acb", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel1Permutation_match() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_1, "acb", "abcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel1Permutation_noMatch() {
        assertFalse(PowerMatch.isMatch(PowerMatch.Level.LEVEL_1, "acb", "xyzxyz"));
    }

    /*********************
     * Level 2 - missing *
     ********************/
    @Test
    public void powerMatch_matchLevel2Missing_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_2, "acx", "abcxyz").equals("abcxyz"));
    }

    @Test
    public void powerMatch_matchLevel2Missing_noMatch() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_2, "acx", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel2Missing_match() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_2, "acx", "abcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel2Missing_noMatch() {
        assertFalse(PowerMatch.isMatch(PowerMatch.Level.LEVEL_2, "acx", "xyzxyz"));
    }

    /***************************
     * Level 3 - 1 wrong/extra *
     **************************/
    @Test
    public void powerMatch_matchLevel3WrongExtra1_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_3, "abdc", "abcxyz").equals("abcxyz"));
    }

    @Test
    public void powerMatch_matchLevel3WrongExtra1_noMatch() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_3, "abdc", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel3WrongExtra1_match() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_3, "abdc", "abcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel3WrongExtra1_noMatch() {
        assertFalse(PowerMatch.isMatch(PowerMatch.Level.LEVEL_3, "abdc", "xyzxyz"));
    }

    /***************************
     * Level 4 - 2 wrong/extra *
     **************************/
    @Test
    public void powerMatch_matchLevel4WrongExtra2_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_4, "adbec", "abcxyz").equals("abcxyz"));
    }

    @Test
    public void powerMatch_matchLevel4WrongExtra2_noMatch() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_4, "adbec", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel4WrongExtra2_match() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_4, "adbec", "abcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel4WrongExtra2_noMatch() {
        assertFalse(PowerMatch.isMatch(PowerMatch.Level.LEVEL_4, "adbec", "xyzxyz"));
    }

    /***************************
     * Level 5 - 3 wrong/extra *
     **************************/
    @Test
    public void powerMatch_matchLevel5WrongExtra3_match() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_5, "adbecf", "abcxyz").equals("abcxyz"));
    }

    @Test
    public void powerMatch_matchLevel5WrongExtra3_noMatch() {
        assertTrue(PowerMatch.match(PowerMatch.Level.LEVEL_5, "adbecf", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_matchWrongExtra3_match() {
        assertTrue(PowerMatch.match("adbecf", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel5WrongExtra3_match() {
        assertTrue(PowerMatch.isMatch(PowerMatch.Level.LEVEL_5, "adbecf", "abcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel5WrongExtra3_noMatch() {
        assertFalse(PowerMatch.isMatch(PowerMatch.Level.LEVEL_5, "adbecf", "xyzxyz"));
    }

    /*******************
     * Null conditions *
     ******************/
    @Test
    public void powerMatch_matchNullEmptyArguments_nullResult() {
        assertTrue(PowerMatch.match(null, null, (String[]) null) == null);
        assertTrue(PowerMatch.match(PowerMatch.HIGHEST_LEVEL, null, (String[]) null) == null);
        assertTrue(PowerMatch.match(PowerMatch.HIGHEST_LEVEL, null) == null);
        assertTrue(PowerMatch.match(PowerMatch.HIGHEST_LEVEL, "", "xyz", "abc") == null);
        assertTrue(PowerMatch.match(PowerMatch.HIGHEST_LEVEL, "abc", (String[]) null) == null);
        assertTrue(PowerMatch.match(null, (String[]) null) == null);
        assertTrue(PowerMatch.match(null) == null);
        assertTrue(PowerMatch.match("abc", (String[]) null) == null);
    }

    @Test
    public void powerMatch_isMatchNullArguments_falseResult() {
        assertFalse(PowerMatch.isMatch(PowerMatch.HIGHEST_LEVEL, null, null));
        assertFalse(PowerMatch.isMatch(PowerMatch.HIGHEST_LEVEL, null, ""));
    }

    @Test
    public void powerMatch_matchEmptyInputOnePotential_notNullResult() {
        assertTrue(PowerMatch.match(PowerMatch.HIGHEST_LEVEL, "", "xyzabc").equals("xyzabc"));
        assertTrue(PowerMatch.match("", "xyzabc").equals("xyzabc"));
    }

    @Test
    public void powerMatch_isMatchEmptyInput_trueResult() {
        assertTrue(PowerMatch.isMatch(PowerMatch.HIGHEST_LEVEL, "", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_matchHighestLevel_noMatches() {
        assertTrue(PowerMatch.match(PowerMatch.HIGHEST_LEVEL, "abcdef", "lmnopq") == null);
    }

    @Test
    public void powerMatch_match_noMatches() {
        assertTrue(PowerMatch.match("abcdef", "lmnopq") == null);
    }

    @Test
    public void powerMatch_isMatchHighestLevel_noMatches() {
        assertFalse(PowerMatch.isMatch(PowerMatch.HIGHEST_LEVEL, "abcdef", "lmnopq"));
    }

}
