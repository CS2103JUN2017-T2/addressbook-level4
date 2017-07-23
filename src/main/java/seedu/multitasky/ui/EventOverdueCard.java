package seedu.multitasky.ui;

import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.ReadOnlyEntry;

//@author A0125586X
/**
 * EventOverdueCard inherits from EntryCard which allows it to use its own .fxml
 * configuration file, allowing it to have its own separate colour and layout scheme.
 * Having separate EventCard and EventOverdueCard allows for different colours to be
 * used to indicate events that are over.
 */
public class EventOverdueCard extends EntryCard {
    private static final String FXML = "EventOverdueCard.fxml";

    public EventOverdueCard(ReadOnlyEntry entry, int displayedIndex) {
        super(FXML, entry, displayedIndex);
        assert entry instanceof Event : "Entry to display on EventListCard must be Event";

        startDateTime.setText(entry.getStartDateAndTimeString());
        endDateTime.setText(entry.getEndDateAndTimeString());
        additionalInfo.setText(prettyTimeFormatDate(entry.getStartDateAndTime().getTime()));
    }
}
