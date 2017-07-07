package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import seedu.address.TestApp;

//@@author A0125586X
/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends GuiHandle {

    private final EventListPanelHandle eventListPanel;
    private final DeadlineListPanelHandle deadlineListPanel;
    private final FloatingTaskListPanelHandle floatingTaskListPanel;
    private final ResultDisplayHandle resultDisplay;
    private final CommandBoxHandle commandBox;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;

    public MainWindowHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);

        eventListPanel = new EventListPanelHandle(guiRobot, primaryStage);
        deadlineListPanel = new DeadlineListPanelHandle(guiRobot, primaryStage);
        floatingTaskListPanel = new FloatingTaskListPanelHandle(guiRobot, primaryStage);

        resultDisplay = new ResultDisplayHandle(guiRobot, primaryStage);
        commandBox = new CommandBoxHandle(guiRobot, primaryStage, TestApp.APP_TITLE);
        statusBarFooter = new StatusBarFooterHandle(guiRobot, primaryStage);
        mainMenu = new MainMenuHandle(guiRobot, primaryStage);
    }

    public EventListPanelHandle getEventListPanel() {
        return eventListPanel;
    }

    public DeadlineListPanelHandle getDeadlineListPanel() {
        return deadlineListPanel;
    }

    public FloatingTaskListPanelHandle getFloatingTaskListPanel() {
        return floatingTaskListPanel;
    }

    public ResultDisplayHandle getResultDisplay() {
        return resultDisplay;
    }

    public CommandBoxHandle getCommandBox() {
        return commandBox;
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return statusBarFooter;
    }

    public MainMenuHandle getMainMenu() {
        return mainMenu;
    }

}
