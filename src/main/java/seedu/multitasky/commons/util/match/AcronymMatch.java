package seedu.multitasky.commons.util.match;

import java.util.ArrayList;
import java.util.Arrays;

// @@author A0125586X
/**
 * Class that performs acronym matching.
 */
public class AcronymMatch implements Match {

    public String match(final String input, final String... potentialMatches) {
        if (input == null || potentialMatches == null || potentialMatches.length == 0) {
            return null;
        }
        return MatchUtil.getRegexMatch(getRegex(input), potentialMatches);
    }

    public boolean isMatch(final String input, final String potentialMatch) {
        if (input == null || potentialMatch == null) {
            return false;
        }
        return MatchUtil.getRegexMatch(getRegex(input), potentialMatch) != null;
    }

    private static String getRegex(final String input) {
        final ArrayList<String> chars = new ArrayList<>(Arrays.asList(input.split("")));
        final StringBuilder regex = new StringBuilder();
        // Alternate keyword characters and any non whitespace
        regex.append(MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE);
        for (String singleChar : chars) {
            regex.append(singleChar);
            regex.append(MatchUtil.REGEX_OPTIONAL_NON_WHITESPACE);
        }
        return regex.toString();
    }

}
