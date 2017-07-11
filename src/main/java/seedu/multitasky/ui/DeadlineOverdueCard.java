package seedu.multitasky.ui;

import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.ReadOnlyEntry;

//@author A0125586X
/**
 * DeadlineOverdueCard inherits from EntryCard which allows it to use its own .fxml
 * configuration file, allowing it to have its own separate colour and layout scheme.
 * Having separate DeadlineCard and DeadlineOverdueCard allows for different colours to be
 * used to indicate events that are over.
 */
public class DeadlineOverdueCard extends EntryCard {
    private static final String FXML = "DeadlineOverdueCard.fxml";

    public DeadlineOverdueCard(ReadOnlyEntry entry, int displayedIndex) {
        super(FXML, entry, displayedIndex);
        assert entry instanceof Deadline : "Entry to display on DeadlineListCard must be Deadline";

        endDateTime.setText(formatDate(entry.getEndDateAndTime().getTime()));
        additionalInfo.setText(prettyTimeFormatDate(entry.getEndDateAndTime().getTime()));
    }
}
