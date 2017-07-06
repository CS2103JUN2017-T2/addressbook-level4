package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;

//@@author A0125586
/**
 * Provides a handle for the panel containing the event list.
 */
public class EventListPanelHandle extends EntryListPanelHandle {

    public EventListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage);
        listViewId = "#eventListView";
    }
}
