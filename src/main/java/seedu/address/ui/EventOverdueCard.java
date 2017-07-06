package seedu.address.ui;

import seedu.address.model.entry.ReadOnlyEntry;

//@author A0125586X
/**
 * EventOverdueCard inherits from EntryCard which allows it to use its own .fxml
 * configuration file, allowing it to have its own separate colour and layout scheme.
 * Having separate EventCard and EventOverdueCard allows for different colours to be
 * used to indicate events that are over.
 */
public class EventOverdueCard extends EntryCard {
    private static String FXML = "EventOverdueCard.fxml";

    public EventOverdueCard(ReadOnlyEntry entry, int displayedIndex) {
        super(FXML, entry, displayedIndex);
        start_date_time.setText("0000");
        end_date_time.setText("1000");
        additional_info.setText("Last week!");
    }
}
