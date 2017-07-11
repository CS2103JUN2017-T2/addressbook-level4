package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import seedu.multitasky.model.entry.Deadline;

//@@author A0125586X
/**
 * Provides a handle for the panel containing the deadline list.
 */
public class DeadlineListPanelHandle extends EntryListPanelHandle {

    public DeadlineListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage);
        listViewId = "#deadlineListView";
    }

    /**
     * Gets an entry handle for a deadline entry in the list by index
     */
    @Override
    public EntryCardHandle getEntryCardHandle(int index) {
        return getEntryCardHandle(new Deadline(getListView().getItems().get(index)));
    }
}
