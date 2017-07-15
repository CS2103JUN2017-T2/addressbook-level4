package seedu.multitasky.commons.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

// @@author A0125586X
/**
 * Checks for matches using a variety of criteria between an input and a list of potential outputs.
 * Can be quite computationally intensive so is is best to call it with short inputs. Developer testing is
 * encouraged to confirm response time is reasonable for your application.
 */
public class PowerMatch {

    public static final int PERMUTATION_MATCH_MAX_ALLOWED_LENGTH = 8;
    public static final int MISSING_INNER_MATCH_MAX_ALLOWED_LENGTH = 8;
    public static final int WRONG_INNER_MATCH_MAX_ALLOWED_LENGTH = 6;

    public static final String REGEX_ANY_NON_WHITESPACE = "((\\S+)?)";
    public static final String REGEX_ANY_PRESENT_NON_WHITESPACE = "(\\S+)";
    public static final String REGEX_ANY_CHARACTER = "((.+)?)";

    /**
     * Attempts to find a match between the input and a single entry in {@code potentialMatches}.
     * Types of matches attempted are substring, prefix, permutation, missing inner characters,
     * wrong/extra inner characters.

     * @param input            the input to attempt to find a match for
     * @param potentialMatches the list of potential matches for {@code input}
     * @return the match for {@code input}, if one is found. Otherwise the original {@code input} is returned.
     *         {@code null} is returned if there is a null input string or {@code potentialMatches} is null.
     */
    public static String match(String input, final ArrayList<String> potentialMatches) {
        if (input == null || potentialMatches == null) {
            return null;
        }
        if (potentialMatches.size() == 0) {
            return input;
        }

        // Create copy to avoid modifying original input string
        String keyword = new String(input.trim().toLowerCase());
        String match;

        match = getSubstringMatch(keyword, potentialMatches);
        if (match != null) {
            return match;
        }

        match = getPrefixMatch(keyword, potentialMatches);
        if (match != null) {
            return match;
        }

        if (input.length() <= PERMUTATION_MATCH_MAX_ALLOWED_LENGTH) {
            match = getPermutationMatch(keyword, potentialMatches);
            if (match != null) {
                return match;
            }
        }

        if (input.length() <= MISSING_INNER_MATCH_MAX_ALLOWED_LENGTH) {
            match = getMissingInnerMatch(keyword, potentialMatches);
            if (match != null) {
                return match;
            }
        }

        if (input.length() <= WRONG_INNER_MATCH_MAX_ALLOWED_LENGTH) {
            match = getWrongInnerMatch(keyword, potentialMatches);
            if (match != null) {
                return match;
            }
        }

        return input;
    }

    private static String getRegexMatch(final String regex, final ArrayList<String> potentialMatches) {
        ArrayList<String> matches = new ArrayList<>();
        for (String potentialMatch : potentialMatches) {
            if (potentialMatch.matches(regex)) {
                matches.add(potentialMatch);
            }
        }
        return filterMatches(matches);
    }

    private static String getSubstringMatch(final String keyword, final ArrayList<String> potentialMatches) {
        final ArrayList<String> matches = new ArrayList<>();
        for (String potentialMatch : potentialMatches) {
            if (potentialMatch.contains(keyword)) {
                matches.add(potentialMatch);
            }
        }
        return filterMatches(matches);
    }

    private static String getPrefixMatch(final String keyword, final ArrayList<String> potentialMatches) {
        final ArrayList<String> matches = new ArrayList<>();
        for (String potentialMatch : potentialMatches) {
            if (potentialMatch.startsWith(keyword)) {
                matches.add(potentialMatch);
            }
        }
        return filterMatches(matches);
    }

    private static String getPermutationMatch(final String keyword,
                                              final ArrayList<String> potentialMatches) {
        final ArrayList<String> permutations = getPermutations(keyword);
        String match;
        for (String permutation : permutations) {
            match = getSubstringMatch(permutation, potentialMatches);
            if (match != null) {
                return match;
            }
            match = getPrefixMatch(permutation, potentialMatches);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    private static String getMissingInnerMatch(final String keyword,
                                               final ArrayList<String> potentialMatches) {
        final ArrayList<String> permutations = getMissingInnerPermutations(keyword);
        String match;
        for (String permutation : permutations) {
            // Use of regex instead of string literal comparison here
            match = getRegexMatch(permutation, potentialMatches);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    /**
     * Currently only accounts for a single wrong character (can be either mistyped or extra)
     */
    private static String getWrongInnerMatch(final String keyword,
                                             final ArrayList<String> potentialMatches) {
        final ArrayList<String> permutations = getWrongInnerPermutations(keyword);
        String match;
        for (String permutation : permutations) {
            // Use of regex instead of string literal comparison here
            match = getRegexMatch(permutation, potentialMatches);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    private static ArrayList<String> getPermutations(final String keyword) {
        HashSet<String> permutations = new HashSet<>();
        ArrayList<String> chars = new ArrayList<>(Arrays.asList(keyword.split("")));
        generateUniquePermutations(chars, 0, keyword.length() - 1, permutations);
        return new ArrayList<String>(permutations);
    }

    private static ArrayList<String> getMissingInnerPermutations(final String keyword) {
        HashSet<String> permutations = new HashSet<>();
        ArrayList<String> chars = new ArrayList<>(Arrays.asList(keyword.split("")));
        // Add in a regex expression for any missing non-whitespace character
        chars.add(REGEX_ANY_PRESENT_NON_WHITESPACE);
        generateUniquePermutations(chars, 0, keyword.length(), permutations); // No -1 due to the extra regex
        // Add in regex expressions before and after to match any substring
        ArrayList<String> permutationsList = new ArrayList<>(permutations);
        for (int i = 0; i < permutationsList.size(); ++i) {
            String permutation = permutationsList.get(i);
            permutation = REGEX_ANY_CHARACTER + permutation + REGEX_ANY_CHARACTER;
            permutationsList.set(i, permutation);
        }
        return permutationsList;
    }

    private static ArrayList<String> getWrongInnerPermutations(final String keyword) {
        HashSet<String> permutations = new HashSet<>();
        ArrayList<String> chars = new ArrayList<>(Arrays.asList(keyword.split("")));
        /**
         * For single wrong/extra character:
         * Replace each character in turn with a regex expression
         * that can match any non-whitespace character or no character at all
         */
        for (int i = 0; i < chars.size(); ++i) {
            String temp = chars.get(i);
            chars.set(i, REGEX_ANY_NON_WHITESPACE);
            HashSet<String> tempPermutations = new HashSet<>();
            generateUniquePermutations(chars, 0, keyword.length() - 1, tempPermutations);
            for (String permutation : tempPermutations) {
                permutations.add(permutation);
            }
            chars.set(i, temp);
        }
        /**
         * For two wrong/extra characters:
         * Replace each two-character combination in turn with a regex expression
         * that can match any non-whitespace character or no character at all
         */
        for (int i = 0; i < chars.size(); ++i) {
            for (int j = i + 1; j < chars.size(); ++j) {
                String iTemp = chars.get(i);
                String jTemp = chars.get(j);
                chars.set(i, REGEX_ANY_NON_WHITESPACE);
                chars.set(j, REGEX_ANY_NON_WHITESPACE);
                HashSet<String> tempPermutations = new HashSet<>();
                generateUniquePermutations(chars, 0, keyword.length() - 1, tempPermutations);
                for (String permutation : tempPermutations) {
                    permutations.add(permutation);
                }
                chars.set(i, iTemp);
                chars.set(j, jTemp);
            }
        }
        /**
         * For three wrong/extra characters:
         * Replace each three-character combination in turn with a regex expression
         * that can match any non-whitespace character or no character at all
         */
        for (int i = 0; i < chars.size(); ++i) {
            for (int j = i + 1; j < chars.size(); ++j) {
                for (int k = j + 1; k < chars.size(); ++k) {
                    String iTemp = chars.get(i);
                    String jTemp = chars.get(j);
                    String kTemp = chars.get(k);
                    chars.set(i, REGEX_ANY_NON_WHITESPACE);
                    chars.set(j, REGEX_ANY_NON_WHITESPACE);
                    chars.set(k, REGEX_ANY_NON_WHITESPACE);
                    HashSet<String> tempPermutations = new HashSet<>();
                    generateUniquePermutations(chars, 0, keyword.length() - 1, tempPermutations);
                    for (String permutation : tempPermutations) {
                        permutations.add(permutation);
                    }
                    chars.set(i, iTemp);
                    chars.set(j, jTemp);
                    chars.set(k, kTemp);
                }
            }
        }
        /**
         * Stopping at three since it's a bit unreasonable to expect the user to enter four or more
         * wrong/extra characters in a single word
         */
        // Add in regex expressions before and after to match any substring
        ArrayList<String> permutationsList = new ArrayList<>(permutations);
        for (int i = 0; i < permutationsList.size(); ++i) {
            String permutation = permutationsList.get(i);
            permutation = REGEX_ANY_CHARACTER + permutation + REGEX_ANY_CHARACTER;
            permutationsList.set(i, permutation);
        }
        return permutationsList;
    }

    private static void generateUniquePermutations(ArrayList<String> chars, int i, int permutationLength,
                                                   HashSet<String> permutations) {
        // Filled up the permutation to the specified length
        if (i == permutationLength) {
            permutations.add(buildString(chars));
        } else {
            for (int j = i; j <= permutationLength; ++j) {
                Collections.swap(chars, i, j);
                generateUniquePermutations(chars, i + 1, permutationLength, permutations);
                Collections.swap(chars, i, j);
            }
        }
    }

    private static String buildString(final ArrayList<String> chars) {
        StringBuilder builder = new StringBuilder();
        for (String string : chars) {
            builder.append(string);
        }
        return builder.toString();
    }

    private static String filterMatches(ArrayList<String> matches) {
        // For now, the match is only accepted if there is only one match.
        if (matches.size() == 1) {
            return matches.get(0);
        }
        return null;
    }
}
