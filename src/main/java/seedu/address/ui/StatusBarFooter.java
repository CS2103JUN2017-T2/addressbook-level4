package seedu.address.ui;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Region> {

    private static final String FXML = "StatusBarFooter.fxml";

    @FXML
    private StatusBar saveLocationStatus;


    public StatusBarFooter(String saveLocation) {
        super(FXML);
        setSaveLocation("./" + saveLocation);
        registerAsAnEventHandler(this);
    }

    private void setSaveLocation(String location) {
        this.saveLocationStatus.setText(location);
    }
}
