package seedu.multitasky.commons.util.match;

import java.util.ArrayList;
import java.util.Arrays;

// @@author A0125586X
/**
 * Class that performs matching with 3 wrong or extra characters.
 */
public class WrongExtra3Match implements Match {

    public static final int MIN_LENGTH = 7;
    public static final int MAX_LENGTH = 14;

    public String match(final String input, final String... potentialMatches) {
        if (input == null || potentialMatches == null
            || input.length() < MIN_LENGTH || input.length() > MAX_LENGTH
            || potentialMatches.length == 0) {
            return null;
        }
        return MatchUtil.getRegexMatch(getRegexes(input), potentialMatches);
    }

    public boolean isMatch(final String input, final String potentialMatch) {
        if (input == null || potentialMatch == null
            || input.length() < MIN_LENGTH || input.length() > MAX_LENGTH) {
            return false;
        }
        return MatchUtil.getRegexMatch(getRegexes(input), potentialMatch) != null;
    }

    private static ArrayList<String> getRegexes(final String input) {
        final ArrayList<String> regexes = new ArrayList<>();
        final ArrayList<String> chars = new ArrayList<>(Arrays.asList(input.split("")));
        for (int i = 0; i < chars.size(); ++i) {
            for (int j = i + 1; j < chars.size(); ++j) {
                for (int k = j + 1; k < chars.size(); ++k) {
                    String iTemp = chars.get(i);
                    String jTemp = chars.get(j);
                    String kTemp = chars.get(k);
                    chars.set(i, MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE);
                    chars.set(j, MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE);
                    chars.set(k, MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE);
                    regexes.add(MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE + MatchUtil.buildString(chars)
                            + MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE);
                    chars.set(i, iTemp);
                    chars.set(j, jTemp);
                    chars.set(k, kTemp);
                }
            }
        }
        return regexes;
    }

}
