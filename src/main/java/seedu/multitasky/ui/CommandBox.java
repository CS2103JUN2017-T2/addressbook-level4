package seedu.multitasky.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import seedu.multitasky.commons.core.LogsCenter;
import seedu.multitasky.commons.events.ui.NewCommandEvent;
import seedu.multitasky.commons.events.ui.NewCommandToExecuteEvent;
import seedu.multitasky.commons.events.ui.NewResultAvailableEvent;
import seedu.multitasky.commons.events.ui.ResultStyleChangeEvent;
import seedu.multitasky.logic.Logic;
import seedu.multitasky.logic.commands.CommandResult;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.ui.util.CommandAutocomplete;
import seedu.multitasky.ui.util.CommandHistory;

//@@author A0125586X
/**
 * Handles text input from the user into the command box.
 * Keeps a history of previous commands entered by the user to provide
 * Linux-style command history navigation.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "command-box-error";
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
        setCommandTextFieldFocus();
        onlyShowActiveEntries();
        registerAsAnEventHandler(this);
    }

    /**
     * Requests focus on the command text field once the main UI window is open,
     * so that the user can immediately begin typing.
     */
    private void setCommandTextFieldFocus() {
        Platform.runLater(new Runnable() {
            public void run() {
                commandTextField.requestFocus();
            }
        });
    }

    @FXML
    private void handleCommandInputChanged() throws DuplicateEntryException {
        commandHistory.saveCommand();
        executeLogic(commandTextField.getText().trim());
        commandTextField.setText("");
    }

    /**
     * Calls the logic component to execute the command given by the string.
     */
    private void executeLogic(String command) {
        try {
            CommandResult commandResult = logic.execute(command);
            // process result of the command
            setStyleToIndicateCommandSuccess();
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));
        } catch (CommandException | ParseException e) {
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + command);
            raise(new NewResultAvailableEvent(e.getMessage()));
        } catch (DuplicateEntryException e) {
            setStyleToIndicateCommandFailure();
            logger.info("Unable to add duplicate entry with command: " + command);
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    /**
     * Sets the command box style to indicate a successful command.
     */
    private void setStyleToIndicateCommandSuccess() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
        raise(new ResultStyleChangeEvent(true));
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();
        if (!styleClass.contains(ERROR_STYLE_CLASS)) {
            styleClass.add(ERROR_STYLE_CLASS);
        }
        raise(new ResultStyleChangeEvent(false));
    }

    private void onlyShowActiveEntries() {
        try {
            logic.execute(ListCommand.COMMAND_WORD);
        } catch (Exception e) {
            assert false : "Initial list of active entries cannot throw exceptions";
        }
    }

    @Subscribe
    private void handleNewCommandEvent(NewCommandEvent event) {
        commandTextField.setText(event.command);
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    @Subscribe
    private void handleNewCommandToExecuteEvent(NewCommandToExecuteEvent event) {
        executeLogic(event.command);
    }

}
