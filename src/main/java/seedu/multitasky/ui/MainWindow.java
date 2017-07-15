package seedu.multitasky.ui;

import com.google.common.eventbus.Subscribe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
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
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.Entry;

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
    private Config config;
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

    @FXML
    private StackPane statusbarPlaceholder;

    public MainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;

        // Configure the UI
        setTitle(config.getAppTitle());
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        Scene scene = new Scene(getRoot());
        primaryStage.setScene(scene);

        setAccelerators();

        registerAsAnEventHandler(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    void fillInnerParts() {
        stateCurrentlyShown.setText(String.format(Messages.MESSAGE_CURRENTLY_DISPLAYING,
                                    stateToString(Entry.State.ACTIVE)));

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

        CommandBox commandBox = new CommandBox(logic);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

    }

    void hide() {
        primaryStage.hide();
    }

    /**
     * To format the state in a form that is consistent with command syntax.
     * ARCHIVED -> archive
     * DELETED -> bin
     */
    private String stateToString(Entry.State state) {
        switch (state) {
        case ACTIVE:
            return "active";
        case ARCHIVED:
            return "archive";
        case DELETED:
            return "bin";
        default:
            return "error";
        }
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
        stateCurrentlyShown.setText(String.format(Messages.MESSAGE_CURRENTLY_DISPLAYING,
                                    stateToString(event.state)));
    }

}
