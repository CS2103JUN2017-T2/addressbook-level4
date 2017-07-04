package seedu.address.ui;

import java.util.logging.Logger;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(StatusBarFooter.class);

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
