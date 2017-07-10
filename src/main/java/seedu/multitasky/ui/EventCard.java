package seedu.multitasky.ui;

import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.ReadOnlyEntry;

//@author A0125586X
/**
 * EventCard inherits from EntryCard which allows it to use its own .fxml
 * configuration file, allowing it to have its own separate colour and layout scheme.
 */
public class EventCard extends EntryCard {
    private static final String FXML = "EventCard.fxml";

    public EventCard(ReadOnlyEntry entry, int displayedIndex) {
        super(FXML, entry, displayedIndex);
        assert entry instanceof Event : "Entry to display on EventListCard must be Event";

        startDateTime.setText(formatDate(entry.getStartDateAndTime().getTime()));
        endDateTime.setText(formatDate(entry.getEndDateAndTime().getTime()));
        additionalInfo.setText(prettyTimeFormatDate(entry.getStartDateAndTime().getTime()));
    }
}
