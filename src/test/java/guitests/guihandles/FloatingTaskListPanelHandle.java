package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;

//@@author A0125586
/**
 * Provides a handle for the panel containing the floating task list.
 */
public class FloatingTaskListPanelHandle extends EntryListPanelHandle {

    private static final String LIST_VIEW_ID = "#floatingTaskListView";

    public FloatingTaskListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage);
    }
}
