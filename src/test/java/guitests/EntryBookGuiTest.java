package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.testfx.api.FxToolkit;

import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.DeadlineListPanelHandle;
import guitests.guihandles.EntryCardHandle;
import guitests.guihandles.EventListPanelHandle;
import guitests.guihandles.FloatingTaskListPanelHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;

import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.multitasky.TestApp;
import seedu.multitasky.commons.core.EventsCenter;
import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.testutil.TestUtil;
import seedu.multitasky.testutil.TypicalEntries;

//@@author A0125586X
/**
 * A GUI Test class for EntryBook.
 */
public abstract class EntryBookGuiTest {

    /* The TestName Rule makes the current test name available inside test methods */
    @Rule
    public TestName name = new TestName();

    protected TypicalEntries typicalEntries = new TypicalEntries();

    /*
     *   Handles to GUI elements present at the start up are created in advance
     *   for easy access from child classes.
     */
    protected MainWindowHandle mainGui;
    protected MainMenuHandle mainMenu;
    protected EventListPanelHandle eventListPanel;
    protected DeadlineListPanelHandle deadlineListPanel;
    protected FloatingTaskListPanelHandle floatingTaskListPanel;
    protected ResultDisplayHandle resultDisplay;
    protected CommandBoxHandle commandBox;
    protected StatusBarFooterHandle statusBarFooter;

    protected Stage stage;

    @BeforeClass
    public static void setupSpec() {
        try {
            FxToolkit.registerPrimaryStage();
            FxToolkit.hideStage();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupStage((stage) -> {
            mainGui = new MainWindowHandle(new GuiRobot(), stage);
            mainMenu = mainGui.getMainMenu();
            eventListPanel = mainGui.getEventListPanel();
            deadlineListPanel = mainGui.getDeadlineListPanel();
            floatingTaskListPanel = mainGui.getFloatingTaskListPanel();
            resultDisplay = mainGui.getResultDisplay();
            commandBox = mainGui.getCommandBox();
            statusBarFooter = mainGui.getStatusBarFooter();
            this.stage = stage;
        });
        EventsCenter.clearSubscribers();
        FxToolkit.setupApplication(() -> new TestApp(this::getInitialData, getDataFileLocation()));
        FxToolkit.showStage();
        while (!stage.isShowing());
        mainGui.focusOnMainApp();
    }

    /**
     * Override this in child classes to set the initial local data.
     * Return null to use the data in the file specified in {@link #getDataFileLocation()}
     */
    protected EntryBook getInitialData() {
        EntryBook entryBook = new EntryBook();
        TypicalEntries.loadEntryBookWithSampleData(entryBook);
        return entryBook;
    }

    /**
     * Override this in child classes to set the data file location.
     */
    protected String getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    /**
     * Asserts the entry shown in the card is same as the given entry
     */
    public void assertMatching(ReadOnlyEntry entry, EntryCardHandle card) {
        assertTrue(TestUtil.compareCardAndEntry(card, entry));
    }

    /**
     * Asserts the size of the event list is equal to the given number.
     */
    protected void assertEventListSize(int size) {
        int numberOfEntries = eventListPanel.getNumberOfEntries();
        assertEquals(size, numberOfEntries);
    }

    /**
     * Asserts the size of the deadline list is equal to the given number.
     */
    protected void assertDeadlineListSize(int size) {
        int numberOfEntries = deadlineListPanel.getNumberOfEntries();
        assertEquals(size, numberOfEntries);
    }

    /**
     * Asserts the size of the floating task list is equal to the given number.
     */
    protected void floatingTaskListSize(int size) {
        int numberOfEntries = floatingTaskListPanel.getNumberOfEntries();
        assertEquals(size, numberOfEntries);
    }

    /**
     * Asserts the message shown in the Result Display area is same as the given string.
     */
    protected void assertResultMessage(String expected) {
        assertEquals(expected, resultDisplay.getText());
    }

    public void raise(BaseEvent e) {
        //JUnit doesn't run its test cases on the UI thread. Platform.runLater is used to post event on the UI thread.
        Platform.runLater(() -> EventsCenter.getInstance().post(e));
    }
}
