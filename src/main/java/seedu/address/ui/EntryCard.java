package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.entry.ReadOnlyEntry;

//@@author A0125586X
public class EntryCard extends UiPart<Region> {

    private static final String FXML = "EntryListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private FlowPane tags;

    public PersonCard(ReadOnlyEntry entry, int displayedIndex) {
        super(FXML);
        name.setText(entry.getName().toString());
        id.setText(displayedIndex + ". ");
        initTags(entry);
    }

    private void initTags(ReadOnlyEntry entry) {
        entry.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
