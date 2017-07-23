package seedu.multitasky.commons.util.match;

// @@author A0125586X
/**
 * Checks for matches using a variety of matching criteria. The different levels of matching available are:
 * 0: substring match
 * 1: prefix match
 * 2: acronym match
 * 3: permutation match
 * 4: 1 wrong/extra character match
 * 5: 2 wrong/extra character match
 * 6: 2 wrong/extra character match
 */
public class PowerMatch implements Matcher {

    public static final int UNUSED = -1;
    public static final int MIN_LEVEL = 0;
    public static final int MAX_LEVEL = 6;

    private Match[] matchLevels = {
        new SubstringMatch(),
        new PrefixMatch(),
        new AcronymMatch(),
        new PermutationMatch(),
        new WrongExtra1Match(),
        new WrongExtra2Match(),
        new WrongExtra3Match(),
    };

    /**
     * Attempts to find a match between the input and a single entry in {@code potentialMatches},
     * using the matching level specified.
     *
     * @param level            the level of matching to use.
     * @param input            the input to attempt to find a match for
     * @param potentialMatches the list of potential matches for {@code input}
     * @return the match for {@code input}, if one is found. Otherwise {@code null} is returned.
     *         {@code null} is also returned if there is a null input string, {@code potentialMatches} is null,
     *         or if {@code potentialMatches} is empty.
     */
    public String match(final int level, final String input, final String... potentialMatches) {
        if (level < 0 || level > MAX_LEVEL || input == null || potentialMatches == null
            || potentialMatches.length == 0) {
            return null;
        } else if (input.isEmpty()) {
            if (potentialMatches.length == 1) {
                return potentialMatches[0];
            } else {
                return null;
            }
        }
        return matchLevels[level].match(input, potentialMatches);
    }

    /**
     * Attempts to match the input with the potential match, using the matching level specified.
     *
     * @param level          the level of matching to use.
     * @param input          the input to attempt to find a match for
     * @param potentialMatch the potential match {@code input}
     * @return if {@code input} can be matched to {@code potentialMatch} using the PowerMatch algorithm
     *         at the specified level.
     */
    public boolean isMatch(final int level, final String input, final String potentialMatch) {
        if (level < 0 || level > MAX_LEVEL || input == null || potentialMatch == null
            || potentialMatch.isEmpty()) {
            return false;
        } else if (input.isEmpty()) {
            return true;
        }
        return matchLevels[level].isMatch(input, potentialMatch);
    }

}
