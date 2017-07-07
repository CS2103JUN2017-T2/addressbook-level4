package seedu.multitasky.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.multitasky.model.entry.ReadOnlyEntry;

//@@author A0125586X
/**
 * Panel containing the list of floating tasks.
 */
public class FloatingTaskListPanel extends UiPart<Region> {
    private static final String FXML = "FloatingTaskListPanel.fxml";

    @FXML
    private ListView<ReadOnlyEntry> floatingTaskListView;

    public FloatingTaskListPanel(ObservableList<ReadOnlyEntry> floatingTaskList) {
        super(FXML);
        setConnections(floatingTaskList);
    }

    private void setConnections(ObservableList<ReadOnlyEntry> floatingTaskList) {
        floatingTaskListView.setItems(floatingTaskList);
        floatingTaskListView.setCellFactory(listView -> new FloatingTaskListViewCell());
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            floatingTaskListView.scrollTo(index);
            floatingTaskListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class FloatingTaskListViewCell extends ListCell<ReadOnlyEntry> {

        @Override
        protected void updateItem(ReadOnlyEntry entry, boolean empty) {
            super.updateItem(entry, empty);

            if (empty || entry == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new FloatingTaskCard(entry, getIndex() + 1).getRoot());
            }
        }
    }

}
