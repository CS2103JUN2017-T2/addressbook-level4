package seedu.multitasky.ui.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// @@author A0125586X
/**
 * Provides autocomplete functionality for commands, using {@code PowerMatch} from
 * {@code seedu.multitasky.commons.util} to come up with matches.
 */
public class CommandAutocomplete implements TextAutocomplete {

    private TextAutocomplete commandWordAutocomplete;

    private List<String> commandWords;
    private Map<String, String[]> commandKeywords;
    private List<String> prefixOnlyCommands;

    private class SplitCommand {
        private String commandWord;
        private String remainder;

        public SplitCommand(String commandWord, String remainder) {
            this.commandWord = commandWord;
            this.remainder = remainder;
        }

        public String getCommandWord() {
            return commandWord;
        }

        public String getRemainder() {
            return remainder;
        }
    }

    public CommandAutocomplete(String[] commandWords, Map<String, String[]> commandKeywords,
                               String[] prefixOnlyCommands) {
        if (commandWords == null) {
            throw new AssertionError("commandWords cannot be null");
        }
        if (commandKeywords == null) {
            throw new AssertionError("commandKeywords cannot be null");
        }
        if (prefixOnlyCommands == null) {
            throw new AssertionError("prefixOnlyCommands cannot be null");
        }
        this.commandKeywords = commandKeywords;
        this.commandWords = Arrays.asList(commandWords);
        this.prefixOnlyCommands = Arrays.asList(prefixOnlyCommands);
        commandWordAutocomplete = new CommandWordAutocomplete(commandWords);
    }

    public String autocomplete(String input) {
        StringBuilder commandResult = new StringBuilder();
        SplitCommand splitCommand = split(input);
        String commandWordMatch = commandWordAutocomplete.autocomplete(splitCommand.getCommandWord());
        commandResult.append(commandWordMatch);

        // Managed to autocomplete to a valid command word
        if (commandWords.contains(commandWordMatch)) {
            commandResult.append(" ")
                         .append(autocompleteKeywords(commandWordMatch, splitCommand.getRemainder()));
        } else {
            if (!splitCommand.getRemainder().isEmpty()) {
                commandResult.append(" ").append(splitCommand.getRemainder());
            }
        }

        return commandResult.toString();
    }

    private String autocompleteKeywords(String commandWord, String remainder) {
        StringBuilder keywordsResult = new StringBuilder();
        if (remainder.isEmpty()) {
            return keywordsResult.toString();
        }
        // Keywords are separated by whitespace
        String[] keywords = remainder.split("\\s+");
        if (prefixOnlyCommands.contains(commandWord)) {
            for (int i = 0; i < keywords.length; ++i) {
                keywordsResult.append(new CommandKeywordAutocomplete(commandKeywords.get(commandWord))
                                            .autocomplete(keywords[i]))
                              .append(" ");
            }
        } else {
            for (int i = 0; i < keywords.length - 1; ++i) {
                keywordsResult.append(keywords[i]).append(" ");
            }
            keywordsResult.append(new CommandKeywordAutocomplete(commandKeywords.get(commandWord))
                                            .autocomplete(keywords[keywords.length - 1]))
                          .append(" ");
        }
        return keywordsResult.toString();
    }

    /**
     * Splits a command word from the rest of the input string.
     */
    private SplitCommand split(String command) {
        // Command word should be followed by whitespace
        String[] words = command.split("\\s+");
        if (words.length > 0) {
            return new SplitCommand(words[0].trim(), command.substring(words[0].length()).trim());
        }
        return new SplitCommand("", "");
    }

}
