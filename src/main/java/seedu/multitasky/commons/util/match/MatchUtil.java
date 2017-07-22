package seedu.multitasky.commons.util.match;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// @@author A0125586X
/**
 * Utility class for performing matches.
 */
public class MatchUtil {

    public static final String REGEX_OPTIONAL_NON_WHITESPACE = "((\\S?)+)";
    public static final String REGEX_PRESENT_NON_WHITESPACE = "(\\S+)";

    /**
     * Attempts each regex in turn until one provides a single match.
     * If none provide a single match, {@code null} is returned.
     */
    public static String getRegexMatch(final List<String> regexes, final String... potentialMatches) {
        String match = null;
        for (String regex : regexes) {
            match = getRegexMatch(regex, potentialMatches);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    /**
     * Attempts to find a single match for the regex.
     * If multiple matches are found, {@code null} is returned.
     */
    public static String getRegexMatch(final String regex, final String... potentialMatches) {
        ArrayList<String> matches = new ArrayList<>();
        for (String potentialMatch : potentialMatches) {
            if (potentialMatch.matches(regex)) {
                matches.add(potentialMatch);
            }
        }
        return filterMatches(matches);
    }

    /**
     * Filters the matches found into a single match.
     * @return the match, if there is just one. otherwise {@code null} is returned. */
    public static String filterMatches(List<String> matches) {
        if (matches == null || matches.size() != 1) {
            return null;
        }
        return matches.get(0);
    }

    /**
     * Builds a string from a collection that can be iterated over
     */
    public static String buildString(final Collection<String> chars) {
        if (chars == null || chars.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String string : chars) {
            builder.append(string);
        }
        return builder.toString();
    }

}
