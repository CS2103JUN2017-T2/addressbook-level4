package seedu.multitasky.commons.util.match;

import java.util.ArrayList;

// @@author A0125586X
/**
 * Class that performs permutation matching.
 */
public class PermutationMatch implements Match {

    public static final int MAX_LENGTH = 8;

    public String match(final String input, final String... potentialMatches) {
        if (input == null || potentialMatches == null || potentialMatches.length == 0
            || input.length() > MAX_LENGTH) {
            return null;
        }
        return MatchUtil.getRegexMatch(getRegexes(input), potentialMatches);
    }

    public boolean isMatch(final String input, final String potentialMatch) {
        if (input == null || potentialMatch == null
            || input.length() > MAX_LENGTH) {
            return false;
        }
        return MatchUtil.getRegexMatch(getRegexes(input), potentialMatch) != null;
    }

    private static ArrayList<String> getRegexes(final String input) {
        final ArrayList<String> permutations = MatchUtil.getPermutations(input);
        for (int i = 0; i < permutations.size(); ++i) {
            permutations.set(i, MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE + permutations.get(i)
                              + MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE);
        }
        return permutations;
    }

}
