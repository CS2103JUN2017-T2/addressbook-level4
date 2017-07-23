package seedu.multitasky.ui.util;

import java.util.Arrays;
import java.util.List;

import seedu.multitasky.commons.util.match.PowerMatch;

// @@author A0125586X
/**
 * Provides autocomplete functionality for command words, using {@code PowerMatch} from
 * {@code seedu.multitasky.commons.util} to come up with matches.
 */
public class CommandWordAutocomplete implements TextAutocomplete {

    private List<String> commandWords;
    private int numCommandWords;

    public CommandWordAutocomplete(String[] commandWords) {
        if (commandWords == null) {
            throw new AssertionError("commandWords cannot be null");
        }
        this.commandWords = Arrays.asList(commandWords);
        numCommandWords = commandWords.length;
    }

    public String autocomplete(String input) {
        String match = null;
        for (int level = PowerMatch.MIN_LEVEL; level <= PowerMatch.MAX_LEVEL; ++level) {
            match = new PowerMatch().match(level, input, commandWords.toArray(new String[numCommandWords]));
            if (match != null) {
                return match;
            }
        }
        return input;
    }

    @Override
    public String getPossibilities(String input) {
        StringBuilder possibilities = new StringBuilder();
        for (int level = PowerMatch.MIN_LEVEL; level <= PowerMatch.MAX_LEVEL; ++level) {
            for (String commandWord : commandWords) {
                if (new PowerMatch().isMatch(level, input, commandWord)) {
                    possibilities.append(commandWord).append(" ");
                }
            }
            if (!possibilities.toString().isEmpty() && possibilities.toString().split(" ").length > 1) {
                // Multiple possibilities
                return possibilities.toString();
            } else if (!possibilities.toString().isEmpty() && possibilities.toString().trim().indexOf(" ") == -1) {
                // Only one possibility, not relevant
                return null;
            }
            possibilities = new StringBuilder();
        }
        return null;
    }

}
