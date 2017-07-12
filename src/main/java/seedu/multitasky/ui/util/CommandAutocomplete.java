package seedu.multitasky.ui.util;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.commands.ExitCommand;
import seedu.multitasky.logic.commands.FindCommand;
import seedu.multitasky.logic.commands.HelpCommand;
import seedu.multitasky.logic.commands.HistoryCommand;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.commands.UndoCommand;


//@@author A0125586X
/**
 * Provides autocomplete functionality to the command box.
 * If the string that the user has typed so far matches the beginning of only one command word,
 * pressing the tab key will complete that command word automatically
 */
public class CommandAutocomplete {

    private static final ArrayList<String> allCommandWords = new ArrayList<String>(Arrays.asList(new String[] {
        AddCommand.COMMAND_WORD,
        ClearCommand.COMMAND_WORD,
        DeleteCommand.COMMAND_WORD,
        EditCommand.COMMAND_WORD,
        ExitCommand.COMMAND_WORD,
        FindCommand.COMMAND_WORD,
        HelpCommand.COMMAND_WORD,
        HistoryCommand.COMMAND_WORD,
        ListCommand.COMMAND_WORD,
        UndoCommand.COMMAND_WORD,
    }));

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
        final String[] input = commandTextField.getText().trim().split(" ");
        if (input.length == 1) {
            ArrayList<String> commandMatches = getCommandSubstringMatches(input[0]);
            if (commandMatches.size() == 1) {
                setText(commandMatches.get(0) + " ");
            } else {
                commandMatches = getCommandStartingMatches(input[0]);
                if (commandMatches.size() == 1) {
                    setText(commandMatches.get(0) + " ");
                }
            }
        }
    }

    private ArrayList<String> getCommandSubstringMatches(String input) {
        final ArrayList<String> commandMatches = new ArrayList<>();
        // Allow for case-insensitive match
        input = input.trim().toLowerCase();
        for (String commandWord : allCommandWords) {
            if (commandWord.indexOf(input) != -1) {
                commandMatches.add(commandWord);
            }
        }
        return commandMatches;
    }

    private ArrayList<String> getCommandStartingMatches(String input) {
        final ArrayList<String> commandMatches = new ArrayList<>();
        // Allow for case-insensitive match
        input = input.trim().toLowerCase();
        for (String commandWord : allCommandWords) {
            if (commandWord.startsWith(input)) {
                commandMatches.add(commandWord);
            }
        }
        return commandMatches;
    }

    private void setText(String text) {
        commandTextField.setText(text);
        setCursorToCommandTextFieldEnd();
    }

    private void setCursorToCommandTextFieldEnd() {
        // Position cursor at the end for consistency and easy editing
        commandTextField.positionCaret(commandTextField.getText().length());
    }

}
