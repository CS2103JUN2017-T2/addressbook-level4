package seedu.multitasky.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.multitasky.commons.core.LogsCenter;
import seedu.multitasky.commons.events.ui.NewResultAvailableEvent;
import seedu.multitasky.commons.events.ui.ResultStyleChangeEvent;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FAILURE_STYLE = "-fx-background-color: yellow; -fx-text-fill: yellow;";
    private static final String SUCCESS_STYLE = "-fx-background-color: derive(#1d1d1d, 20%) -fx-text-fill: white;";

    private static final Logger logger = LogsCenter.getLogger(ResultDisplay.class);
    private static final String FXML = "ResultDisplay.fxml";

    private final StringProperty displayed = new SimpleStringProperty("");

    @FXML
    private TextArea resultDisplay;

    public ResultDisplay() {
        super(FXML);
        resultDisplay.textProperty().bind(displayed);
        registerAsAnEventHandler(this);
    }

    private void setStyleToIndicateCommandSuccess() {
        resultDisplay.setStyle(SUCCESS_STYLE);
    }

    private void setStyleToIndicateCommandFailure() {
        resultDisplay.setStyle(FAILURE_STYLE);
    }

    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        displayed.setValue(event.message);
    }

    @Subscribe
    private void handleResultStyleChangeEvent(ResultStyleChangeEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (event.isSuccess()) {
            setStyleToIndicateCommandSuccess();
        } else {
            setStyleToIndicateCommandFailure();
        }
    }

}
