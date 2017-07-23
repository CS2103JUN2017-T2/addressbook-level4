package seedu.multitasky.ui.util;

import java.util.Arrays;
import java.util.List;

import seedu.multitasky.commons.util.match.Matcher;
import seedu.multitasky.commons.util.match.PowerMatch;

// @@author A0125586X
/**
 * Provides autocomplete functionality for command keywords, using {@code PowerMatch} from
 * {@code seedu.multitasky.commons.util} to come up with matches.
 */
public class CommandKeywordAutocomplete implements TextAutocomplete {

    private List<String> commandKeywords;
    private int numCommandKeywords;

    private Matcher matcher = new PowerMatch();

    public CommandKeywordAutocomplete(String[] commandKeywords) {
        if (commandKeywords == null) {
            throw new AssertionError("commandKeywords cannot be null");
        }
        this.commandKeywords = Arrays.asList(commandKeywords);
        numCommandKeywords = commandKeywords.length;
    }

    public String autocomplete(String input) {
        String match = null;
        for (int level = PowerMatch.MIN_LEVEL; level <= PowerMatch.MAX_LEVEL; ++level) {
            match = matcher.match(level, input, commandKeywords.toArray(new String[numCommandKeywords]));
            if (match != null) {
                return match;
            }
        }
        return input;
    }

    @Override
    public String getPossibilities(String input) {
        return null;
    }

}
