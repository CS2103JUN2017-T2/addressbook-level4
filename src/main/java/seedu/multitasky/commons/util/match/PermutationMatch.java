package seedu.multitasky.commons.util.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

// @@author A0125586X
/**
 * Class that performs permutation matching.
 */
public class PermutationMatch implements Match {

    public String match(final String input, final String... potentialMatches) {
        if (input == null || potentialMatches == null || potentialMatches.length == 0) {
            return null;
        }
        return MatchUtil.getRegexMatch(getRegexes(input), potentialMatches);
    }

    public boolean isMatch(final String input, final String potentialMatch) {
        if (input == null || potentialMatch == null) {
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

    private static ArrayList<String> getPermutations(final String keyword) {
        HashSet<String> permutations = new HashSet<>();
        ArrayList<String> chars = new ArrayList<>(Arrays.asList(keyword.split("")));
        generateUniquePermutations(chars, 0, keyword.length() - 1, permutations);
        return new ArrayList<String>(permutations);
    }

    private static void generateUniquePermutations(ArrayList<String> chars, int i, int permutationLength,
                                                   HashSet<String> permutations) {
        // Filled up the permutation to the specified length
        if (i == permutationLength) {
            permutations.add(MatchUtil.buildString(chars));
        } else {
            for (int j = i; j <= permutationLength; ++j) {
                Collections.swap(chars, i, j);
                generateUniquePermutations(chars, i + 1, permutationLength, permutations);
                Collections.swap(chars, i, j);
            }
        }
    }

}
