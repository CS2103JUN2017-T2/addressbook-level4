package seedu.address.ui;

import java.util.ArrayList;
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

public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Logic logic;

    //@@author A0125586X
    private ArrayList<String> commandTextHistory;
    private int commandTextHistoryIdx;

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
        commandTextHistory = new ArrayList<>();
        commandTextHistory.add("");
        commandTextHistoryIdx = 0;
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
        try {
            String commandText = commandTextField.getText();
            saveCommandtoHistory(commandText);
            CommandResult commandResult = logic.execute(commandText);
            // process result of the command
            setStyleToIndicateCommandSuccess();
            commandTextField.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));

        } catch (CommandException | ParseException e) {
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    private void saveCommandtoHistory(String command) {
        commandTextHistory.add(command);
        commandTextHistoryIdx = commandTextHistory.size();
    }

    private void loadPreviousCommandIntoTextField() {
        // Stop at index 1 which is the first command entered
        if (commandTextHistoryIdx > 1) {
            --commandTextHistoryIdx;
        }
        commandTextField.setText(commandTextHistory.get(commandTextHistoryIdx));
        // Position cursor at the end for easy editing
        commandTextField.positionCaret(commandTextHistory.get(commandTextHistoryIdx).length());
    }

    private void loadNextCommandIntoTextField() {
        // Stop at index commandTextHistory.size() - 1 which is the most recent command entered
        if (commandTextHistoryIdx < commandTextHistory.size() - 1) {
            ++commandTextHistoryIdx;
        }
        commandTextField.setText(commandTextHistory.get(commandTextHistoryIdx));
        // Position cursor at the end for easy editing
        commandTextField.positionCaret(commandTextHistory.get(commandTextHistoryIdx).length());
    }
    //@@author

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
