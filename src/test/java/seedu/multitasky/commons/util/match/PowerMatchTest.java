package seedu.multitasky.commons.util.match;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// @@author A0125586X
public class PowerMatchTest {

    /******************************
     * null/false result expected *
     *****************************/
    @Test
    public void powerMatch_matchNullEmptyArguments_nullResult() {
        assertTrue(new PowerMatch().match(PowerMatch.MIN_LEVEL - 1, "input", "input") == null);
        assertTrue(new PowerMatch().match(PowerMatch.MAX_LEVEL + 1, "input", "input") == null);
        assertTrue(new PowerMatch().match(0, null, "input") == null);
        assertTrue(new PowerMatch().match(0, "input", (String[]) null) == null);
        assertTrue(new PowerMatch().match(0, "input") == null);
        assertTrue(new PowerMatch().match(0, "", "match", "match2") == null);
    }

    @Test
    public void powerMatch_isMatchNullEmptyArguments_falseResult() {
        assertFalse(new PowerMatch().isMatch(PowerMatch.MIN_LEVEL - 1, "input", "input"));
        assertFalse(new PowerMatch().isMatch(PowerMatch.MAX_LEVEL + 1, "input", "input"));
        assertFalse(new PowerMatch().isMatch(0, null, "input"));
        assertFalse(new PowerMatch().isMatch(0, "input", null));
        assertFalse(new PowerMatch().isMatch(0, "input", ""));
    }

    /******************
     * default result *
     *****************/
    @Test
    public void powerMatch_matchEmptyInputSinglePotential_match() {
        assertTrue(new PowerMatch().match(0, "", "match").equals("match"));
    }

    @Test
    public void powerMatch_isMatchEmptyInputSinglePotential_match() {
        assertTrue(new PowerMatch().isMatch(0, "", "match"));
    }

    /***********************
     * Level 0 - substring *
     **********************/
    @Test
    public void powerMatch_matchLevel0_match() {
        assertTrue(new PowerMatch().match(0, "abc", "xyzabcxyz").equals("xyzabcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel0_match() {
        assertTrue(new PowerMatch().isMatch(0, "abc", "xyzabcxyz"));
    }

    @Test
    public void powerMatch_matchLevel0_noMatch() {
        assertTrue(new PowerMatch().match(0, "abc", "xyzabxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel0_noMatch() {
        assertFalse(new PowerMatch().isMatch(0, "abc", "xyzabxyz"));
    }

    /********************
     * Level 1 - prefix *
     *******************/
    @Test
    public void powerMatch_matchLevel1_match() {
        assertTrue(new PowerMatch().match(1, "abc", "abcxyz", "xyzabc").equals("abcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel1_match() {
        assertTrue(new PowerMatch().isMatch(1, "abc", "abcxyz"));
    }

    @Test
    public void powerMatch_matchLevel1_noMatch() {
        assertTrue(new PowerMatch().match(1, "abc", "xyzabxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel1_noMatch() {
        assertFalse(new PowerMatch().isMatch(1, "abc", "xyzabxyz"));
    }

    /*********************
     * Level 2 - acronym *
     ********************/
    @Test
    public void powerMatch_matchLevel2_match() {
        assertTrue(new PowerMatch().match(2, "abc", "axbycz").equals("axbycz"));
    }

    @Test
    public void powerMatch_isMatchLevel2_match() {
        assertTrue(new PowerMatch().isMatch(2, "abc", "axbycz"));
    }

    @Test
    public void powerMatch_matchLevel2_noMatch() {
        assertTrue(new PowerMatch().match(2, "abc", "xyzabxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel2_noMatch() {
        assertFalse(new PowerMatch().isMatch(2, "abc", "xyzabxyz"));
    }

    /*************************
     * Level 3 - permutation *
     ************************/
    @Test
    public void powerMatch_matchLevel3_match() {
        assertTrue(new PowerMatch().match(3, "acb", "abcxyz", "acxyz").equals("abcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel3_match() {
        assertTrue(new PowerMatch().isMatch(3, "acb", "abcxyz"));
    }

    @Test
    public void powerMatch_matchLevel3_noMatch() {
        assertTrue(new PowerMatch().match(3, "acb", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel3_noMatch() {
        assertFalse(new PowerMatch().isMatch(3, "acb", "xyzxyz"));
    }

    /***************************
     * Level 4 - 1 wrong/extra *
     **************************/
    @Test
    public void powerMatch_matchLevel4_match() {
        assertTrue(new PowerMatch().match(4, "abdc", "abcxyz").equals("abcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel4_match() {
        assertTrue(new PowerMatch().isMatch(4, "abdc", "abcxyz"));
    }

    @Test
    public void powerMatch_matchLevel4_noMatch() {
        assertTrue(new PowerMatch().match(4, "abdc", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel4_noMatch() {
        assertFalse(new PowerMatch().isMatch(4, "abdc", "xyzxyz"));
    }

    /***************************
     * Level 5 - 2 wrong/extra *
     **************************/
    @Test
    public void powerMatch_matchLevel5_match() {
        assertTrue(new PowerMatch().match(5, "adbec", "abcxyz").equals("abcxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel5_match() {
        assertTrue(new PowerMatch().isMatch(5, "adbec", "abcxyz"));
    }

    @Test
    public void powerMatch_matchLevel5_noMatch() {
        assertTrue(new PowerMatch().match(5, "adbec", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel5_noMatch() {
        assertFalse(new PowerMatch().isMatch(5, "adbec", "xyzxyz"));
    }

    /***************************
     * Level 6 - 3 wrong/extra *
     **************************/
    @Test
    public void powerMatch_matchLevel6_match() {
        assertTrue(new PowerMatch().match(6, "adbecfd", "abcdxyz").equals("abcdxyz"));
    }

    @Test
    public void powerMatch_isMatchLevel6_match() {
        assertTrue(new PowerMatch().isMatch(6, "adbecfd", "abcdxyz"));
    }

    @Test
    public void powerMatch_matchLevel6_noMatch() {
        assertTrue(new PowerMatch().match(6, "adbecfd", "xyzxyz") == null);
    }

    @Test
    public void powerMatch_isMatchLevel6_noMatch() {
        assertFalse(new PowerMatch().isMatch(6, "adbecfd", "xyzxyz"));
    }

}
