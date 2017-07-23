package seedu.multitasky.commons.util.match;

// @@author A0125586X
/**
 * Interface for different types of objects that perform matching.
 */
public interface Match {

    /**
     * Attempts to find a single potential match for the input.
     * If multiple matches are found, {@code null} is returned.
     */
    public String match(final String input, final String... potentialMatches);

    /**
     * Attempts to match the potential match with the input.
     * @return if the input can be matched to the potential match.
     */
    public boolean isMatch(final String input, final String potentialMatch);

}
