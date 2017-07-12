package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import seedu.multitasky.model.entry.Event;

//@@author A0125586X
/**
 * Provides a handle for the panel containing the event list.
 */
public class EventListPanelHandle extends EntryListPanelHandle {

    public EventListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage);
        listViewId = "#eventListView";
    }

    /**
     * Gets an entry handle for a event entry in the list by index
     */
    @Override
    public EntryCardHandle getEntryCardHandle(int index) {
        return getEntryCardHandle(new Event(getListView().getItems().get(index)));
    }
}
