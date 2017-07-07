package seedu.address.ui;

import seedu.address.model.entry.ReadOnlyEntry;

//@author A0125586X
/**
 * EventCard inherits from EntryCard which allows it to use its own .fxml
 * configuration file, allowing it to have its own separate colour and layout scheme.
 */
public class EventCard extends EntryCard {
    private static final String FXML = "EventCard.fxml";

    public EventCard(ReadOnlyEntry entry, int displayedIndex) {
        super(FXML, entry, displayedIndex);
        //TODO fill date time and information fields
    }
}
