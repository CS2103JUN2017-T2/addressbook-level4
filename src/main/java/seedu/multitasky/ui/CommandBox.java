package seedu.multitasky.ui;

import java.util.Date;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.multitasky.commons.core.LogsCenter;
import seedu.multitasky.commons.events.ui.NewResultAvailableEvent;
import seedu.multitasky.commons.events.ui.ResultStyleChangeEvent;
import seedu.multitasky.logic.Logic;
import seedu.multitasky.logic.commands.CommandResult;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.commands.util.CommandUtil;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.ui.util.CommandAutocomplete;
import seedu.multitasky.ui.util.CommandHistory;
import seedu.multitasky.ui.util.TextAutocomplete;

//@@author A0125586X
/**
 * Handles text input from the user into the command box.
 * Keeps a history of previous commands entered by the user to provide
 * Linux-style command history navigation.
 */
public class CommandBox extends UiPart<Region> {

    // Two tab presses in 0.5 second is considered double
    public static final int DOUBLE_TAB_PRESS_INTERVAL_MILLIS = 500;

    public static final String ERROR_STYLE_CLASS = "command-box-error";

    private static final String FXML = "CommandBox.fxml";

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Logic logic;

    private TextAutocomplete autocomplete;
    private CommandHistory commandHistory;

    @FXML
    private TextField commandTextField;

    private Date tabPressTime;

    public CommandBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        commandHistory = new CommandHistory(getRoot(), commandTextField);
        autocomplete = new CommandAutocomplete(CommandUtil.COMMAND_WORDS, CommandUtil.COMMAND_KEYWORDS,
                                               CommandUtil.PREFIX_ONLY_COMMANDS);
        tabPressTime = new Date();
        setCommandTextFieldFocus();
        onlyShowActiveEntries();
        setupCommandAutocompleteShortcut();
        registerAsAnEventHandler(this);
    }

    public void setCommand(String command) {
        commandTextField.setText(command);
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    public void requestFocus() {
        commandTextField.requestFocus();
    }

    /**
     * Calls the logic component to execute the command given by the string.
     */
    public void executeCommand(String command) {
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

    @FXML
    private void handleCommandInputChanged() {
        commandHistory.saveCommand();
        executeCommand(commandTextField.getText().trim());
        commandTextField.setText("");
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

    /**
     * Executes an initial list command to show all active entries.
     */
    private void onlyShowActiveEntries() {
        try {
            logic.execute(ListCommand.COMMAND_WORD);
        } catch (Exception e) {
            throw new AssertionError("Initial list of active entries cannot throw exceptions");
        }
    }

    /**
     * Sets up the {@code TAB} key as the autocomplete keyboard shortcut.
     */
    private void setupCommandAutocompleteShortcut() {
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB) {
                    event.consume();
                    if (isDoubleTabPress()) {
                        String possibilities = autocomplete.getPossibilities(commandTextField.getText().trim());
                        if (possibilities != null) {
                            raise(new NewResultAvailableEvent(possibilities));
                        }
                    } else {
                        setText(autocomplete.autocomplete(commandTextField.getText().trim()));
                    }
                }
            }
        });
    }

    /**
     * Checks if this should be considered as a double tab press.
     */
    private boolean isDoubleTabPress() {
        Date now = new Date();
        if (now.getTime() - tabPressTime.getTime() <= DOUBLE_TAB_PRESS_INTERVAL_MILLIS) {
            return true;
        }
        tabPressTime = now;
        return false;
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

    /**
     * Sets the command box text and positions the cursor at the end of the text.
     */
    private void setText(String text) {
        commandTextField.setText(text);
        commandTextField.positionCaret(commandTextField.getText().length());
    }

}
