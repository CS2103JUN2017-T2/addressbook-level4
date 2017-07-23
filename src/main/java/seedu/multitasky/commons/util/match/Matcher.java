package seedu.multitasky.commons.util.match;

// @@author A0125586X
/**
 * Interface for objects that perform different levels of matching.
 */
public interface Matcher {

    public String match(final int level, final String input, final String... potentialMatches);

    public boolean isMatch(final int level, final String input, final String potentialMatch);

}
