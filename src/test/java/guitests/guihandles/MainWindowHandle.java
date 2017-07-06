package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import seedu.address.TestApp;

//@@author A0125586
/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends GuiHandle {

    private final FloatingTaskListPanelHandle floatingTaskListPanel;

    /*private final ResultDisplayHandle resultDisplay;
    private final CommandBoxHandle commandBox;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;*/

    public MainWindowHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);

        eventListPanel = new EventListPanelHandle(guiRobot, primaryStage);

        personListPanel = new PersonListPanelHandle(guiRobot, primaryStage);
        resultDisplay = new ResultDisplayHandle(guiRobot, primaryStage);
        commandBox = new CommandBoxHandle(guiRobot, primaryStage, TestApp.APP_TITLE);
        statusBarFooter = new StatusBarFooterHandle(guiRobot, primaryStage);
        mainMenu = new MainMenuHandle(guiRobot, primaryStage);
    }

    public PersonListPanelHandle getPersonListPanel() {
        return personListPanel;
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
