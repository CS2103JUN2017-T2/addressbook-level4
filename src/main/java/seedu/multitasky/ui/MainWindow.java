package seedu.multitasky.ui;

import com.google.common.eventbus.Subscribe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.multitasky.commons.core.Config;
import seedu.multitasky.commons.core.GuiSettings;
import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.events.ui.ExitAppRequestEvent;
import seedu.multitasky.commons.events.ui.ListTypeUpdateEvent;
import seedu.multitasky.commons.util.FxViewUtil;
import seedu.multitasky.logic.Logic;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.commands.ExitCommand;
import seedu.multitasky.logic.commands.FindCommand;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.commands.OpenCommand;
import seedu.multitasky.logic.commands.RedoCommand;
import seedu.multitasky.logic.commands.SaveCommand;
import seedu.multitasky.logic.commands.UndoCommand;
import seedu.multitasky.model.UserPrefs;

//@@author A0125586X
/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Region> {

    private static final String ICON = "/images/calendar.png";
    private static final String FXML = "MainWindow.fxml";
    private static final int MIN_HEIGHT = 600;
    private static final int MIN_WIDTH = 450;

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private EventListPanel eventListPanel;
    private DeadlineListPanel deadlineListPanel;
    private FloatingTaskListPanel floatingTaskListPanel;
    private UserPrefs prefs;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private Label stateCurrentlyShown;

    @FXML
    private StackPane eventListPanelPlaceholder;

    @FXML
    private StackPane deadlineListPanelPlaceholder;

    @FXML
    private StackPane floatingTaskListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane commandBoxPlaceholder;
    private CommandBox commandBox;

    @FXML
    private StackPane statusbarPlaceholder;

    public MainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.prefs = prefs;

        // Configure the UI
        setTitle(config.getAppTitle());
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        Scene scene = new Scene(getRoot());
        primaryStage.setScene(scene);

        setAccelerators();
        setCommandShortcuts();

        registerAsAnEventHandler(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    private void setCommandShortcuts() {
        setCommandExecuteShortcut(UndoCommand.COMMAND_WORD,
                                  new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        setCommandExecuteShortcut(RedoCommand.COMMAND_WORD,
                                  new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));

        setCommandShortcut(EditCommand.COMMAND_WORD + " ", new KeyCodeCombination(KeyCode.F2));
        setCommandShortcut(FindCommand.COMMAND_WORD + " ", new KeyCodeCombination(KeyCode.F3));
        setCommandShortcut(ExitCommand.COMMAND_WORD + " ", new KeyCodeCombination(KeyCode.F4));
        setCommandShortcut(ListCommand.COMMAND_WORD + " ", new KeyCodeCombination(KeyCode.F5));
        setCommandShortcut(SaveCommand.COMMAND_WORD + " ",
                           new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        setCommandShortcut(OpenCommand.COMMAND_WORD + " ",
                           new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

        setCommandBoxFocusShortcut(new KeyCodeCombination(KeyCode.F6));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Sets up a command execute shortcut.
     * When the shortcut key combination is pressed, the command string is executed.
     */
    private void setCommandExecuteShortcut(String command, KeyCodeCombination keyCodeCombination) {
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (keyCodeCombination.match(event)) {
                commandBox.executeCommand(command);
                event.consume();
            }
        });
    }

    /**
     * Sets up a command shortcut.
     * When the shortcut key combination is pressed, the command string is entered into the command box,
     * but not executed.
     */
    private void setCommandShortcut(String command, KeyCodeCombination keyCodeCombination) {
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (keyCodeCombination.match(event)) {
                commandBox.setCommand(command);
                event.consume();
            }
        });
    }

    /**
     * Sets up the shortcut to bring the command box into focus
     */
    private void setCommandBoxFocusShortcut(KeyCodeCombination keyCodeCombination) {
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (keyCodeCombination.match(event)) {
                commandBox.requestFocus();
                event.consume();
            }
        });
    }


    void fillInnerParts() {
        stateCurrentlyShown.setText(String.format(Messages.MESSAGE_CURRENTLY_DISPLAYING, "active"));

        eventListPanel = new EventListPanel(logic.getFilteredEventList());
        eventListPanelPlaceholder.getChildren().add(eventListPanel.getRoot());

        deadlineListPanel = new DeadlineListPanel(logic.getFilteredDeadlineList());
        deadlineListPanelPlaceholder.getChildren().add(deadlineListPanel.getRoot());

        floatingTaskListPanel = new FloatingTaskListPanel(logic.getFilteredFloatingTaskList());
        floatingTaskListPanelPlaceholder.getChildren().add(floatingTaskListPanel.getRoot());

        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(prefs.getEntryBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        commandBox = new CommandBox(logic);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the given image as the icon of the main window.
     * @param iconSource e.g. {@code "/images/help_icon.png"}
     */
    private void setIcon(String iconSource) {
        FxViewUtil.setStageIcon(primaryStage, iconSource);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.show();
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public EventListPanel getEventListPanel() {
        return this.eventListPanel;
    }

    public DeadlineListPanel getDeadlineListPanel() {
        return this.deadlineListPanel;
    }

    public FloatingTaskListPanel getFloatingTaskListPanel() {
        return this.floatingTaskListPanel;
    }

    @Subscribe
    private void handleListTypeUpdateEvent(ListTypeUpdateEvent event) {
        stateCurrentlyShown.setText(String.format(Messages.MESSAGE_CURRENTLY_DISPLAYING, event.state));
    }

}
