package seedu.address.ui;

import seedu.address.model.entry.ReadOnlyEntry;

//@author A0125586X
/**
 * DeadlineCard inherits from EntryCard which allows it to use its own .fxml
 * configuration file, allowing it to have its own separate colour and layout scheme.
 */
public class DeadlineCard extends EntryCard {
    private static String FXML = "DeadlineCard.fxml";

    public DeadlineCard(ReadOnlyEntry entry, int displayedIndex) {
        super(FXML, entry, displayedIndex);
        start_date_time.setText("0000");
        additional_info.setText("Coming up tomorrow!");
    }
}
