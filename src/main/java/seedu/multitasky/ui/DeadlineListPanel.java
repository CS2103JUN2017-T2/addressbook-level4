package seedu.multitasky.ui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.ReadOnlyEntry;

//@@author A0125586X
/**
 * Panel containing the list of deadlines.
 */
public class DeadlineListPanel extends UiPart<Region> {
    private static final String FXML = "DeadlineListPanel.fxml";

    @FXML
    private ListView<ReadOnlyEntry> deadlineListView;

    public DeadlineListPanel(ObservableList<ReadOnlyEntry> deadlineList) {
        super(FXML);
        setConnections(deadlineList);
    }

    private void setConnections(ObservableList<ReadOnlyEntry> deadlineList) {
        deadlineListView.setItems(deadlineList);
        deadlineListView.setCellFactory(listView -> new DeadlineListViewCell());
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            deadlineListView.scrollTo(index);
            deadlineListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class DeadlineListViewCell extends ListCell<ReadOnlyEntry> {

        @Override
        protected void updateItem(ReadOnlyEntry entry, boolean empty) {
            super.updateItem(entry, empty);

            if (empty || entry == null) {
                setGraphic(null);
                setText(null);
            } else {
                int index = getIndex() + 1;
                assert index > 0 : "getIndex returned invalid index";

                Calendar current = new GregorianCalendar();
                // Deadline is over
                if (current.compareTo(entry.getEndDateAndTime()) > 0) {
                    setGraphic(new DeadlineOverdueCard(entry, index).getRoot());
                } else {
                    setGraphic(new DeadlineCard(entry, index).getRoot());
                }
            }
        }
    }

}
