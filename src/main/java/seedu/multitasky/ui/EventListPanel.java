package seedu.multitasky.ui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.ReadOnlyEntry;

//@@author A0125586X
/**
 * Panel containing the list of events.
 */
public class EventListPanel extends UiPart<Region> {
    private static final String FXML = "EventListPanel.fxml";

    @FXML
    private ListView<ReadOnlyEntry> eventListView;

    public EventListPanel(ObservableList<ReadOnlyEntry> eventList) {
        super(FXML);
        setConnections(eventList);
    }

    private void setConnections(ObservableList<ReadOnlyEntry> eventList) {
        eventListView.setItems(eventList);
        eventListView.setCellFactory(listView -> new EventListViewCell());
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            eventListView.scrollTo(index);
            eventListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class EventListViewCell extends ListCell<ReadOnlyEntry> {

        @Override
        protected void updateItem(ReadOnlyEntry entry, boolean empty) {
            //assert entry instanceof Event : "Entry to display on EventListPanel must be Event";
            super.updateItem(entry, empty);

            if (empty || entry == null) {
                setGraphic(null);
                setText(null);
            } else {
                int index = getIndex() + 1;
                assert index > 0 : "getIndex returned invalid index";

                Calendar current = new GregorianCalendar();
                // Event has already started
                if (current.compareTo(entry.getStartDateAndTime()) > 0) {
                    setGraphic(new EventOverdueCard(entry, index).getRoot());
                } else {
                    setGraphic(new EventCard(entry, index).getRoot());
                }
            }
        }
    }

}
