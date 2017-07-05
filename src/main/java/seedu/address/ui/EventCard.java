package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import seedu.address.model.entry.ReadOnlyEntry;

//@author A0125586X
/**
 * EventCard inherits from EntryCard which allows it to use its own .fxml
 * configuration file, allowing it to have its own separate colour and layout scheme.
 */
public class EventCard extends EntryCard {
    private static String FXML = "EventCard.fxml";

    public EventCard(ReadOnlyEntry entry, int displayedIndex) {
        super(FXML, entry, displayedIndex);
        start_date_time.setText("0000");
        end_date_time.setText("1000");
        additional_info.setText("Coming up tomorrow!");
    }
}
