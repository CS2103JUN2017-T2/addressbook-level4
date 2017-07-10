package seedu.multitasky.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import seedu.multitasky.commons.core.LogsCenter;
import seedu.multitasky.commons.events.ui.NewResultAvailableEvent;
import seedu.multitasky.logic.Logic;
import seedu.multitasky.logic.commands.CommandResult;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.ui.util.CommandAutocomplete;
import seedu.multitasky.ui.util.CommandHistory;

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

    private CommandAutocomplete commandAutocomplete;
    private CommandHistory commandHistory;

    @FXML
    private TextField commandTextField;

    public CommandBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        commandHistory = new CommandHistory(getRoot(), commandTextField);
        commandAutocomplete = new CommandAutocomplete(getRoot(), commandTextField);
    }

    @FXML
    private void handleCommandInputChanged() {
        commandHistory.saveCommand();
        try {
            CommandResult commandResult = logic.execute(commandTextField.getText().trim());
            // process result of the command
            setStyleToIndicateCommandSuccess();
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));

        } catch (CommandException | ParseException e) {
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText().trim());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
        commandTextField.setText("");
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
