package seedu.multitasky.commons.util.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

// @@author A0125586X
/**
 * Checks for matches using a variety of criteria between an input and a list of potential outputs.
 * Can be quite computationally intensive so is is best to call it with short inputs. Developer testing is
 * encouraged to confirm response time is reasonable for your application.
 * PowerMatch offers different levels of matching, each more powerful than the one before.
 * LEVEL_0: substring, prefix and acronym matches
 * LEVEL_1: permutation match
 * LEVEL_2: missing character match
 * LEVEL_3: 1 wrong/extra character match
 * LEVEL_4: 2 wrong/extra characters match
 * LEVEL_5: 3 wrong/extra characters match
 */
public class PowerMatch {

    public enum Level { LEVEL_0, LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5 };

    public static final Level HIGHEST_LEVEL = Level.LEVEL_5;

    public static final int PERMUTATION_MATCH_MAX_ALLOWED_LENGTH = 8;
    public static final int MISSING_CHAR_MATCH_MIN_ALLOWED_LENGTH = 3;
    public static final int MISSING_CHAR_MATCH_MAX_ALLOWED_LENGTH = 7;
    public static final int WRONG_EXTRA_CHAR_1_MATCH_MIN_ALLOWED_LENGTH = 4;
    public static final int WRONG_EXTRA_CHAR_1_MATCH_MAX_ALLOWED_LENGTH = 6;
    public static final int WRONG_EXTRA_CHAR_2_MATCH_MIN_ALLOWED_LENGTH = 5;
    public static final int WRONG_EXTRA_CHAR_2_MATCH_MAX_ALLOWED_LENGTH = 6;
    public static final int WRONG_EXTRA_CHAR_3_MATCH_MIN_ALLOWED_LENGTH = 6;
    public static final int WRONG_EXTRA_CHAR_3_MATCH_MAX_ALLOWED_LENGTH = 6;

    public static final String REGEX_ANY_NON_WHITESPACE = "((\\S?)+)";
    public static final String REGEX_ANY_PRESENT_NON_WHITESPACE = "(\\S+)";

    /**
     * Attempts to find a match between the input and a single entry in {@code potentialMatches}.
     * Automatically tries all matching levels until that level provides a single match.
     *
     * @param input            the input to attempt to find a match for
     * @param potentialMatches the list of potential matches for {@code input}
     * @return the match for {@code input}, if one is found. Otherwise {@code null} is returned.
     *         {@code null} is also returned if there is a null input string or {@code potentialMatches} is null.
     */
    public static String match(final String input, final String... potentialMatches) {
        if (input == null || potentialMatches == null || potentialMatches.length == 0) {
            return null;
        } else if (input.isEmpty()) {
            if (potentialMatches.length == 1) {
                return potentialMatches[0];
            } else {
                return null;
            }
        }
        String match = null;
        for (Level level : Level.values()) {
            match = match(level, input, potentialMatches);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    /**
     * Attempts to find a match between the input and a single entry in {@code potentialMatches},
     * using the matching level provided.
     *
     * @param input            the input to attempt to find a match for
     * @param potentialMatches the list of potential matches for {@code input}
     * @param level            the level of matching to use.
     * @return the match for {@code input}, if one is found. Otherwise {@code null} is returned.
     *         {@code null} is also returned if there is a null input string or {@code potentialMatches} is null.
     */
    public static String match(Level level, final String input, final String... potentialMatches) {
        if (level == null || input == null || potentialMatches == null || potentialMatches.length == 0) {
            return null;
        } else if (input.isEmpty()) {
            if (potentialMatches.length == 1) {
                return potentialMatches[0];
            } else {
                return null;
            }
        }
        // Create copy to avoid modifying original input string
        String keyword = new String(input.trim().toLowerCase());
        String match = null;

        switch (level) {
        case LEVEL_0:
            match = getSubstringMatch(keyword, potentialMatches);
            if (match != null) {
                return match;
            }
            match = getPrefixMatch(keyword, potentialMatches);
            if (match != null) {
                return match;
            }
            match = getAcronymMatch(keyword, potentialMatches);
            if (match != null) {
                return match;
            }
            break;
        case LEVEL_1:
            if (input.length() <= PERMUTATION_MATCH_MAX_ALLOWED_LENGTH) {
                match = getPermutationMatch(keyword, potentialMatches);
                if (match != null) {
                    return match;
                }
            }
            break;
        case LEVEL_2:
            if (input.length() >= MISSING_CHAR_MATCH_MIN_ALLOWED_LENGTH
                && input.length() <= MISSING_CHAR_MATCH_MAX_ALLOWED_LENGTH) {
                match = getMissingCharMatch(keyword, potentialMatches);
                if (match != null) {
                    return match;
                }
            }
            break;
        case LEVEL_3:
            if (input.length() >= WRONG_EXTRA_CHAR_1_MATCH_MIN_ALLOWED_LENGTH
                && input.length() <= WRONG_EXTRA_CHAR_1_MATCH_MAX_ALLOWED_LENGTH) {
                match = getWrongExtraCharMatch(1, keyword, potentialMatches);
                if (match != null) {
                    return match;
                }
            }
            break;
        case LEVEL_4:
            if (input.length() >= WRONG_EXTRA_CHAR_2_MATCH_MIN_ALLOWED_LENGTH
                && input.length() <= WRONG_EXTRA_CHAR_2_MATCH_MAX_ALLOWED_LENGTH) {
                match = getWrongExtraCharMatch(2, keyword, potentialMatches);
                if (match != null) {
                    return match;
                }
            }
            break;
        case LEVEL_5:
            if (input.length() >= WRONG_EXTRA_CHAR_3_MATCH_MIN_ALLOWED_LENGTH
                && input.length() <= WRONG_EXTRA_CHAR_3_MATCH_MAX_ALLOWED_LENGTH) {
                match = getWrongExtraCharMatch(3, keyword, potentialMatches);
                if (match != null) {
                    return match;
                }
            }
            break;
        default:
            return null;
        }
        return null;
    }

    /**
     * Attempts to match the input with the potential match, using the matching level provided.
     *
     * @param input          the input to attempt to find a match for
     * @param potentialMatch the potential match {@code input}
     * @return if {@code input} can be matched to {@code potentialMatch} using the PowerMatch algorithm.
     */
    public static boolean isMatch(Level level, final String input, final String potentialMatch) {
        if (level == null || input == null || potentialMatch == null || potentialMatch.isEmpty()) {
            return false;
        } else if (input.isEmpty()) {
            return true;
        }
        // Create copy to avoid modifying original input string
        String keyword = new String(input.trim().toLowerCase());
        switch (level) {
        case LEVEL_0:
            return getSubstringMatch(keyword, potentialMatch) != null
                || getPrefixMatch(keyword, potentialMatch) != null
                || getAcronymMatch(keyword, potentialMatch) != null;
        case LEVEL_1:
            return keyword.length() <= PERMUTATION_MATCH_MAX_ALLOWED_LENGTH
                && getPermutationMatch(keyword, potentialMatch) != null;
        case LEVEL_2:
            return keyword.length() >= MISSING_CHAR_MATCH_MIN_ALLOWED_LENGTH
                && keyword.length() <= MISSING_CHAR_MATCH_MAX_ALLOWED_LENGTH
                && getMissingCharMatch(keyword, potentialMatch) != null;
        case LEVEL_3:
            return keyword.length() >= WRONG_EXTRA_CHAR_1_MATCH_MIN_ALLOWED_LENGTH
                && keyword.length() <= WRONG_EXTRA_CHAR_1_MATCH_MAX_ALLOWED_LENGTH
                && getWrongExtraCharMatch(1, keyword, potentialMatch) != null;
        case LEVEL_4:
            return keyword.length() >= WRONG_EXTRA_CHAR_2_MATCH_MIN_ALLOWED_LENGTH
                && keyword.length() <= WRONG_EXTRA_CHAR_2_MATCH_MAX_ALLOWED_LENGTH
                && getWrongExtraCharMatch(2, keyword, potentialMatch) != null;
        case LEVEL_5:
            return keyword.length() >= WRONG_EXTRA_CHAR_3_MATCH_MIN_ALLOWED_LENGTH
                && keyword.length() <= WRONG_EXTRA_CHAR_3_MATCH_MAX_ALLOWED_LENGTH
                && getWrongExtraCharMatch(3, keyword, potentialMatch) != null;
        default:
            return false;
        }
    }

    /*
     * Returns as soon as any one of the regexes returns as a single match
     */
    private static String getRegexMatch(final ArrayList<String> regexes, final String... potentialMatches) {
        String match = null;
        for (String regex : regexes) {
            match = getRegexMatch(regex, potentialMatches);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    private static String getRegexMatch(final String regex, final String... potentialMatches) {
        ArrayList<String> matches = new ArrayList<>();
        for (String potentialMatch : potentialMatches) {
            if (potentialMatch.matches(regex)) {
                matches.add(potentialMatch);
            }
        }
        return filterMatches(matches);
    }

    private static String getSubstringMatch(final String keyword, final String... potentialMatches) {
        String regex = REGEX_ANY_NON_WHITESPACE + keyword + REGEX_ANY_NON_WHITESPACE;
        return getRegexMatch(regex, potentialMatches);
    }

    private static String getPrefixMatch(final String keyword, final String... potentialMatches) {
        String regex = keyword + REGEX_ANY_NON_WHITESPACE;
        return getRegexMatch(regex, potentialMatches);
    }

    private static String getAcronymMatch(final String keyword, final String... potentialMatches) {
        String regex = getAcronymRegex(keyword);
        return getRegexMatch(regex, potentialMatches);
    }

    private static String getPermutationMatch(final String keyword,
                                              final String... potentialMatches) {
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

    private static String getMissingCharMatch(final String keyword,
                                               final String... potentialMatches) {
        final ArrayList<String> permutations = getMissingInnerPermutations(keyword);
        return getRegexMatch(permutations, potentialMatches);
    }

    private static String getWrongExtraCharMatch(int numChars, final String keyword,
                                                 final String... potentialMatches) {
        final ArrayList<String> permutations = getWrongExtraPermutations(keyword, numChars);
        return getRegexMatch(permutations, potentialMatches);
    }

    private static String getAcronymRegex(String keyword) {
        final ArrayList<String> chars = new ArrayList<>(Arrays.asList(keyword.split("")));
        final StringBuilder regex = new StringBuilder();
        // Alternate keyword characters and any non whitespace
        regex.append(REGEX_ANY_NON_WHITESPACE);
        for (String singleChar : chars) {
            regex.append(singleChar);
            regex.append(REGEX_ANY_NON_WHITESPACE);
        }
        return regex.toString();
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
            permutation = REGEX_ANY_NON_WHITESPACE + permutation + REGEX_ANY_NON_WHITESPACE;
            permutationsList.set(i, permutation);
        }
        return permutationsList;
    }

    private static ArrayList<String> getWrongExtraPermutations(final String keyword, int numChars) {
        HashSet<String> permutations = new HashSet<>();
        ArrayList<String> chars = new ArrayList<>(Arrays.asList(keyword.split("")));
        /**
         * For single wrong/extra character:
         * Replace each character in turn with a regex expression
         * that can match any non-whitespace character or no character at all
         */
        if (numChars >= 1 && chars.size() > 1) {
            for (int i = 0; i < chars.size(); ++i) {
                String temp = chars.get(i);
                chars.set(i, REGEX_ANY_NON_WHITESPACE);
                HashSet<String> tempPermutations = new HashSet<>();
                generateUniquePermutations(chars, 0, keyword.length() - 1, tempPermutations);
                for (String permutation : tempPermutations) {
                    // Add in regex expressions before and after to match any substring
                    permutations.add(REGEX_ANY_NON_WHITESPACE + permutation + REGEX_ANY_NON_WHITESPACE);
                }
                chars.set(i, temp);
            }
        }
        /**
         * For two wrong/extra characters:
         * Replace each two-character combination in turn with a regex expression
         * that can match any non-whitespace character or no character at all
         */
        if (numChars >= 2 && chars.size() > 2) {
            for (int i = 0; i < chars.size(); ++i) {
                for (int j = i + 1; j < chars.size(); ++j) {
                    String iTemp = chars.get(i);
                    String jTemp = chars.get(j);
                    chars.set(i, REGEX_ANY_NON_WHITESPACE);
                    chars.set(j, REGEX_ANY_NON_WHITESPACE);
                    HashSet<String> tempPermutations = new HashSet<>();
                    generateUniquePermutations(chars, 0, keyword.length() - 1, tempPermutations);
                    for (String permutation : tempPermutations) {
                        // Add in regex expressions before and after to match any substring
                        permutations.add(REGEX_ANY_NON_WHITESPACE + permutation + REGEX_ANY_NON_WHITESPACE);
                    }
                    chars.set(i, iTemp);
                    chars.set(j, jTemp);
                }
            }
        }
        /**
         * For three wrong/extra characters:
         * Replace each three-character combination in turn with a regex expression
         * that can match any non-whitespace character or no character at all
         */
        if (numChars >= 3 && chars.size() > 3) {
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
                            // Add in regex expressions before and after to match any substring
                            permutations.add(REGEX_ANY_NON_WHITESPACE + permutation + REGEX_ANY_NON_WHITESPACE);
                        }
                        chars.set(i, iTemp);
                        chars.set(j, jTemp);
                        chars.set(k, kTemp);
                    }
                }
            }
        }
        return new ArrayList<>(permutations);
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
        if (matches == null || matches.size() != 1) {
            return null;
        }
        // For now, the match is only accepted if there is only one match.
        return matches.get(0);
    }

}
