package seedu.multitasky.ui.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.multitasky.commons.util.PowerMatch;
import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.logic.commands.CompleteCommand;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.commands.ExitCommand;
import seedu.multitasky.logic.commands.FindCommand;
import seedu.multitasky.logic.commands.HelpCommand;
import seedu.multitasky.logic.commands.HistoryCommand;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.commands.RedoCommand;
import seedu.multitasky.logic.commands.RestoreCommand;
import seedu.multitasky.logic.commands.UndoCommand;

// @@author A0125586X
/**
 * Provides autocomplete functionality to the command box.
 * Uses {@code PowerMatch} from {@code seedu.multitasky.commons.util} to come up with matches.
 * Listens and responds to the {@code TAB} key pressed automatically, as long as it's pressed while
 * focus is on the command text field.
 */
public class CommandAutocomplete {

    private static final int COMMAND_WORD_IDX = 0;
    private static final int LAST_WORD_IDX = 1;

    private static final String[] commandWords = new String[] {
        AddCommand.COMMAND_WORD,
        ClearCommand.COMMAND_WORD,
        CompleteCommand.COMMAND_WORD,
        DeleteCommand.COMMAND_WORD,
        EditCommand.COMMAND_WORD,
        ExitCommand.COMMAND_WORD,
        FindCommand.COMMAND_WORD,
        HelpCommand.COMMAND_WORD,
        HistoryCommand.COMMAND_WORD,
        ListCommand.COMMAND_WORD,
        RedoCommand.COMMAND_WORD,
        RestoreCommand.COMMAND_WORD,
        UndoCommand.COMMAND_WORD,
    };

    private static final HashMap<String, String[]> commandKeywords;

    private static final Set<String> prefixOnlyCommands;

    static {
        commandKeywords = new HashMap<>();
        commandKeywords.put(AddCommand.COMMAND_WORD, AddCommand.VALID_PREFIXES);
        commandKeywords.put(ClearCommand.COMMAND_WORD, ClearCommand.VALID_PREFIXES);
        commandKeywords.put(CompleteCommand.COMMAND_WORD, CompleteCommand.VALID_PREFIXES);
        commandKeywords.put(DeleteCommand.COMMAND_WORD, DeleteCommand.VALID_PREFIXES);
        commandKeywords.put(EditCommand.COMMAND_WORD, EditCommand.VALID_PREFIXES);
        commandKeywords.put(ExitCommand.COMMAND_WORD, ExitCommand.VALID_PREFIXES);
        commandKeywords.put(FindCommand.COMMAND_WORD, FindCommand.VALID_PREFIXES);
        commandKeywords.put(HelpCommand.COMMAND_WORD, HelpCommand.VALID_PREFIXES);
        commandKeywords.put(HistoryCommand.COMMAND_WORD, HistoryCommand.VALID_PREFIXES);
        commandKeywords.put(ListCommand.COMMAND_WORD, ListCommand.VALID_PREFIXES);
        commandKeywords.put(RedoCommand.COMMAND_WORD, RedoCommand.VALID_PREFIXES);
        commandKeywords.put(RestoreCommand.COMMAND_WORD, RestoreCommand.VALID_PREFIXES);
        commandKeywords.put(UndoCommand.COMMAND_WORD, UndoCommand.VALID_PREFIXES);

        prefixOnlyCommands = new HashSet<>();
        prefixOnlyCommands.add(ClearCommand.COMMAND_WORD);
        prefixOnlyCommands.add(ExitCommand.COMMAND_WORD);
        prefixOnlyCommands.add(HelpCommand.COMMAND_WORD);
        prefixOnlyCommands.add(HistoryCommand.COMMAND_WORD);
        prefixOnlyCommands.add(ListCommand.COMMAND_WORD);
        prefixOnlyCommands.add(RedoCommand.COMMAND_WORD);
        prefixOnlyCommands.add(UndoCommand.COMMAND_WORD);
    }

    @FXML
    private Region commandBoxRegion;

    @FXML
    private TextField commandTextField;

    private final EventHandler<KeyEvent> tabKeyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.TAB) {
                event.consume();
                autocomplete();
            }
        }
    };

    public CommandAutocomplete(Region commandBoxRegion, TextField commandTextField) {
        assert commandBoxRegion != null : "commandBoxRegion cannot be null";
        assert commandTextField != null : "commandTextField cannot be null";
        this.commandBoxRegion = commandBoxRegion;
        this.commandTextField = commandTextField;
        this.commandBoxRegion.addEventFilter(KeyEvent.KEY_PRESSED, tabKeyEventHandler);
    }

    private void autocomplete() {
        StringBuilder commandResult = new StringBuilder();
        String[] splitCommand = extractCommandWord(commandTextField.getText().trim());
        String commandMatch = autocompleteCommandWord(splitCommand[COMMAND_WORD_IDX]);
        commandResult.append(commandMatch).append(" ");

        // Managed to autocomplete to a valid command word
        if (Arrays.asList(commandWords).contains(commandMatch)) {
            // We have other words to autocomplete
            if (splitCommand[1].length() > 0) {
                if (prefixOnlyCommands.contains(commandMatch)) {
                    // We can attempt to autocomplete each of the words into prefixes
                    String[] matches = matchPrefixes(commandMatch, separateWords(splitCommand[1]));
                    for (String match : matches) {
                        commandResult.append(match).append(" ");
                    }
                } else {
                    // We can only attempt to autocomplete the last word into a prefix
                    splitCommand = extractLastWord(splitCommand[1]);
                    // The middle portion of the input remains unchanged, append if present
                    if (splitCommand[0].trim().length() > 0) {
                        commandResult.append(splitCommand[0]).append(" ");
                    }
                    String autoCompletedPrefix = autocompletePrefix(splitCommand[LAST_WORD_IDX], commandMatch);
                    if (autoCompletedPrefix.length() > 0) {
                        commandResult.append(autoCompletedPrefix).append(" ");
                    }
                }
            }
        } else {
            // No information to go on to autocomplete anything else
            return;
        }
        setCommandFieldText(commandResult.toString());
        return;
    }

    private String[] matchPrefixes(String commandWord, String... keywords) {
        String[] results = new String[keywords.length];
        for (int i = 0; i < keywords.length; ++i) {
            results[i] = autocompletePrefix(keywords[i], commandWord);
        }
        return results;
    }

    private String autocompleteCommandWord(String keyword) {
        String match = PowerMatch.match(keyword, commandWords);
        if (match != null) {
            return match;
        } else {
            return keyword;
        }
    }

    private String autocompletePrefix(String keyword, String commandWord) {
        String match = PowerMatch.match(keyword, commandKeywords.get(commandWord));
        if (match != null) {
            return match;
        } else {
            return keyword;
        }
    }

    /**
     * Separates the command word from the rest of the input string.
     * @return an array of strings with two elements. The first element is the extracted command word
     * and the second is the rest of the input string with the command word removed.
     */
    private String[] extractCommandWord(String input) {
        // Command word should be followed by whitespace
        String[] words = input.split("\\s+");
        if (words.length > 0) {
            return new String[] {
                words[COMMAND_WORD_IDX].trim(),
                input.substring(words[COMMAND_WORD_IDX].length()).trim()
            };
        }
        return new String[] { "", "" };
    }

    /**
     * Separates the last word from the rest of the input string.
     * @return an array of strings with two elements. The first element is the input string except for the
     * last word, and the second is the last word.
     */
    private String[] extractLastWord(String input) {
        // Words should be delimited by whitespace
        String[] words = input.split("\\s+");
        if (words.length > 0) {
            return new String[] {
                input.substring(0, input.length() - words[words.length - 1].length()).trim(),
                words[words.length - 1].trim()
            };
        }
        return new String[] { "", "" };
    }

    /**
     * Separates the words from the input string, delimited by whitespace.
     */
    private String[] separateWords(String input) {
        return input.split("\\s+");
    }

    private void setCommandFieldText(String text) {
        commandTextField.setText(text);
        setCursorToCommandTextFieldEnd();
    }

    private void setCursorToCommandTextFieldEnd() {
        // Position cursor at the end for consistency and easy editing
        commandTextField.positionCaret(commandTextField.getText().length());
    }

}
