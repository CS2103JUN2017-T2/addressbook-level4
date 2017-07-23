package seedu.multitasky.commons.util.match;

import java.util.ArrayList;
import java.util.Arrays;

// @@author A0125586X
/**
 * Class that performs matching with 1 wrong or extra character.
 */
public class WrongExtra1Match implements Match {

    public static final int MIN_LENGTH = 4;

    public String match(final String input, final String... potentialMatches) {
        if (input == null || potentialMatches == null
            || input.length() < MIN_LENGTH || potentialMatches.length == 0) {
            return null;
        }
        return MatchUtil.getRegexMatch(getRegexes(input), potentialMatches);
    }

    public boolean isMatch(final String input, final String potentialMatch) {
        if (input == null || potentialMatch == null
            || input.length() < MIN_LENGTH) {
            return false;
        }
        return MatchUtil.getRegexMatch(getRegexes(input), potentialMatch) != null;
    }

    private static ArrayList<String> getRegexes(final String input) {
        final ArrayList<String> regexes = new ArrayList<>();
        final ArrayList<String> chars = new ArrayList<>(Arrays.asList(input.split("")));
        for (int i = 0; i < chars.size(); ++i) {
            String temp = chars.get(i);
            chars.set(i, MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE);
            regexes.add(MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE + MatchUtil.buildString(chars)
                      + MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE);
            chars.set(i, temp);
        }
        return regexes;
    }

}
