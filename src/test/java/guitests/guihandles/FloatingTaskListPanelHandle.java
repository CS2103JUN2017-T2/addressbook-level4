package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import seedu.multitasky.model.entry.FloatingTask;

//@@author A0125586X
/**
 * Provides a handle for the panel containing the floating task list.
 */
public class FloatingTaskListPanelHandle extends EntryListPanelHandle {

    public FloatingTaskListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage);
        listViewId = "#floatingTaskListView";
    }

    /**
     * Gets an entry handle for a floating list entry in the list by index
     */
    @Override
    public EntryCardHandle getEntryCardHandle(int index) {
        return getEntryCardHandle(new FloatingTask(getListView().getItems().get(index)));
    }
}
