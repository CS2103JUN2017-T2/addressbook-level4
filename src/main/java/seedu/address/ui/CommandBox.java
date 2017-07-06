package seedu.address.ui;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Stack;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author A0125586X
/**
 * Handles text input from the user into the command box.
 * Keeps a history of previous commands entered by the user to provide
 * Linux-style command history navigation.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Logic logic;

    private Stack<String> commandTextPastStack;
    private Stack<String> commandTextFutureStack;

    @FXML
    private TextField commandTextField;

    public CommandBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        setupHistory();
    }

    /**
     * Sets up the components required to implement the command history function
     * by using the up arrow key.
     */
    @FXML
    private void setupHistory() {
        commandTextPastStack = new Stack<>();
        commandTextFutureStack = new Stack<>();
        getRoot().addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {
                    loadPreviousCommandIntoTextField();
                } else if (event.getCode() == KeyCode.DOWN) {
                    loadNextCommandIntoTextField();
                }
            }
        });
    }

    @FXML
    private void handleCommandInputChanged() {
        String commandText = commandTextField.getText().trim();
        commandTextField.setText("");
        saveCommandtoHistory(commandText);
        try {
            CommandResult commandResult = logic.execute(commandText);
            // process result of the command
            setStyleToIndicateCommandSuccess();
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));

        } catch (CommandException | ParseException e) {
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandText);
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    private void saveCommandtoHistory(String command) {
        // First push all commands to the past
        while (!commandTextFutureStack.empty()) {
            commandTextPastStack.push(commandTextFutureStack.pop());
        }
        // Save this command if it's the first one or if it's different from the last one
        if ((command.length() > 0)
            && (commandTextPastStack.empty() || !commandTextPastStack.peek().equals(command))) {
            commandTextPastStack.push(command);
        }
    }

    private void loadPreviousCommandIntoTextField() {
        // There's history to show to the user
        if (!commandTextPastStack.empty()) {
            // Store whatever the user was typing if it's different from the last one
            String command = commandTextField.getText().trim();
            if (commandTextFutureStack.empty()
                && !command.equals(commandTextPastStack.peek())) {
                commandTextFutureStack.push(command);
            }
            // Load the previous command from the past
            commandTextFutureStack.push(commandTextPastStack.pop());
            commandTextField.setText(commandTextFutureStack.peek());
        }
        setCursorToCommandTextFieldEnd();
    }

    private void loadNextCommandIntoTextField() {
        // There're newer commands to show to the user
        if (!commandTextFutureStack.empty()) {
            String temp = commandTextFutureStack.pop();
            // There's still at least one more command
            if (!commandTextFutureStack.empty()) {
                commandTextPastStack.push(temp);
            // This is the newest command
            } else {
                commandTextFutureStack.push(temp);
            }
            commandTextField.setText(commandTextFutureStack.peek());
        }
        setCursorToCommandTextFieldEnd();
    }

    private void setCursorToCommandTextFieldEnd() {
        // Position cursor at the end for easy editing
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Sets the command box style to indicate a successful command.
     */
    private void setStyleToIndicateCommandSuccess() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

}
//@@author
