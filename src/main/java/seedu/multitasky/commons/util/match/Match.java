package seedu.multitasky.commons.util.match;

// @@author A0125586X
/**
 * Interface for different types of objects that perform matching.
 */
public interface Match {

    public static String match(final String input, final String... potentialMatches);

    public static boolean isMatch(final String input, final String potentialMatch);

}
