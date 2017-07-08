package seedu.multitasky.ui.uiutils;

import java.util.Stack;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

//@@author A0125586X
/**
 * Keeps track of the history of commands entered by the user into a text field.
 */
public class CommandHistory {

    @FXML
    private Region commandBoxRegion;

    @FXML
    private TextField commandTextField;

    private Stack<String> commandTextPastStack;
    private Stack<String> commandTextFutureStack;

    private boolean wasUserTypedCommandSaved;

    private final EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.KP_UP) {
                event.consume();
                loadPreviousCommand();
            } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.KP_DOWN) {
                event.consume();
                loadNextCommand();
            }
        }
    };

    public CommandHistory(Region commandBoxRegion, TextField commandTextField) {
        assert commandBoxRegion != null : "commandBoxRegion cannot be null";
        assert commandTextField != null : "commandTextField cannot be null";
        this.commandBoxRegion = commandBoxRegion;
        this.commandTextField = commandTextField;
        commandTextPastStack = new Stack<>();
        commandTextFutureStack = new Stack<>();
        wasUserTypedCommandSaved = false;
        this.commandBoxRegion.addEventFilter(KeyEvent.KEY_PRESSED, eventHandler);
    }

    public void saveCommand() {
        String command = commandTextField.getText().trim();
        // First push all commands to the past
        while (!commandTextFutureStack.empty()) {
            commandTextPastStack.push(commandTextFutureStack.pop());
        }
        // Remove the user typed command since it wasn't executed
        if (wasUserTypedCommandSaved) {
            wasUserTypedCommandSaved = false;
            commandTextPastStack.pop();
        }
        // Save this command if it's the first one or if it's different from the last one
        if ((command.length() > 0)
                && (commandTextPastStack.empty() || !commandTextPastStack.peek().equals(command))) {
            commandTextPastStack.push(command);
        }
    }

    public void loadPreviousCommand() {
        // A previous command doesn't exist
        if (commandTextPastStack.empty()) {
            return;
        }
        // This is the first time the user requested the previous command
        if (commandTextFutureStack.empty()) {
            String command = commandTextField.getText().trim();
            // Save whatever the user was typing if it is different from the previous entered command
            if (!command.equals(commandTextPastStack.peek())) {
                commandTextFutureStack.push(command);
                wasUserTypedCommandSaved = true;
            }
        }
        // Load the previous command into the text field
        commandTextFutureStack.push(commandTextPastStack.pop());
        setText();
    }

    public void loadNextCommand() {
        // A newer command doesn't exist
        if (commandTextFutureStack.empty()) {
            return;
        }
        commandTextPastStack.push(commandTextFutureStack.pop());
        // This is the newest command
        if (commandTextFutureStack.empty()) {
            commandTextFutureStack.push(commandTextPastStack.pop());
        }
        setText();
    }

    public void setText() {
        commandTextField.setText(commandTextFutureStack.peek());
        setCursorToCommandTextFieldEnd();
    }

    private void setCursorToCommandTextFieldEnd() {
        // Position cursor at the end for consistency and easy editing
        commandTextField.positionCaret(commandTextField.getText().length());
    }

}
