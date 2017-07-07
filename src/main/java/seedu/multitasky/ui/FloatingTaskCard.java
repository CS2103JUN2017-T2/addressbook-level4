package seedu.multitasky.ui;

import seedu.multitasky.model.entry.ReadOnlyEntry;

//@author A0125586X
/**
 * FloatingTaskCard inherits from EntryCard which allows it to use its own .fxml
 * configuration file, allowing it to have its own separate colour and layout scheme.
 */
public class FloatingTaskCard extends EntryCard {
    private static final String FXML = "FloatingTaskCard.fxml";

    public FloatingTaskCard(ReadOnlyEntry entry, int displayedIndex) {
        super(FXML, entry, displayedIndex);
    }
}
