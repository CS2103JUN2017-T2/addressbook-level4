package guitests.guihandles;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import guitests.GuiRobot;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import seedu.address.TestApp;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.testutil.TestUtil;

//@@author A0125586
/**
 * Provides a handle for the panel containing the event entry list.
 */
public class EntryListPanelHandle extends GuiHandle {

    public static final int NOT_FOUND = -1;
    public static final String CARD_PANE_ID = "#cardPane";

    protected String listViewId;

    public EntryListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
        listViewId = "#entryListView";
    }

    public List<ReadOnlyEntry> getSelectedEntries() {
        ListView<ReadOnlyEntry> entryList = getListView();
        return entryList.getSelectionModel().getSelectedItems();
    }

    public ListView<ReadOnlyEntry> getListView() {
        return getNode(listViewId);
    }

    /**
     * Returns true if the list is showing the entry details correctly and in correct order.
     * @param entries A list of entries in the correct order.
     */
    public boolean isListMatching(ReadOnlyEntry... entries) {
        return this.isListMatching(0, entries);
    }

    /**
     * Returns true if the list is showing the entry details correctly and in correct order.
     * @param startPosition The starting position of the sub list.
     * @param entries A list of entries in the correct order.
     */
    public boolean isListMatching(int startPosition, ReadOnlyEntry... entries) throws IllegalArgumentException {
        if (entries.length + startPosition != getListView().getItems().size()) {
            throw new IllegalArgumentException("List size mismatched\n"
                    + "Expected " + (getListView().getItems().size() - 1) + " entries, "
                    + "got" + (entries.length + startPosition - 1));
        }
        assertTrue(this.containsInOrder(startPosition, entries));
        for (int i = 0; i < entries.length; i++) {
            final int scrollTo = i + startPosition;
            guiRobot.interact(() -> getListView().scrollTo(scrollTo));
            guiRobot.sleep(200);
            if (!TestUtil.compareCardAndEntry(getEntryCardHandle(startPosition + i), entries[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clicks on the ListView.
     */
    public void clickOnListView() {
        Point2D point = TestUtil.getScreenMidPoint(getListView());
        guiRobot.clickOn(point.getX(), point.getY());
    }

    /**
     * Returns true if the {@code entries} appear as the sub list (in that order) at position {@code startPosition}.
     */
    public boolean containsInOrder(int startPosition, ReadOnlyEntry... entries) {
        List<ReadOnlyEntry> entriesInList = getListView().getItems();

        // Return false if the list in panel is too short to contain the given list
        if (startPosition + entries.length > entriesInList.size()) {
            return false;
        }

        // Return false if any of the entries doesn't match
        for (int i = 0; i < entries.length; i++) {
            if (!entriesInList.get(startPosition + i).getName().toString().equals(entries[i].getName().toString())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Navigates the listview to display and select the entry that matches the {@code name}.
     */
    public EntryCardHandle navigateToEntry(String name) {
        guiRobot.sleep(500); //Allow a bit of time for the list to be updated
        final Optional<ReadOnlyEntry> entry = getListView().getItems().stream()
                                                .filter(p -> p.getName().toString().equals(name))
                                                .findAny();
        if (!entry.isPresent()) {
            throw new IllegalStateException("Name of entry not found: " + name);
        }

        return navigateToEntry(entry.get());
    }

    /**
     * Navigates the listview to display and select the entry.
     */
    public EntryCardHandle navigateToEntry(ReadOnlyEntry entry) {
        int index = getEntryIndex(entry);

        guiRobot.interact(() -> {
            getListView().scrollTo(index);
            guiRobot.sleep(150);
            getListView().getSelectionModel().select(index);
        });
        guiRobot.sleep(100);
        return getEntryCardHandle(entry);
    }


    /**
     * Returns the position index of the entry given, {@code NOT_FOUND} if not found in the list.
     */
    public int getEntryIndex(ReadOnlyEntry target) {
        List<ReadOnlyEntry> entriesInList = getListView().getItems();
        for (int i = 0; i < entriesInList.size(); i++) {
            if (entriesInList.get(i).getName().equals(target.getName())) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Gets an entry from the list by index
     */
    public ReadOnlyEntry getEntry(int index) {
        return getListView().getItems().get(index);
    }

    /**
     * Gets an entry handle for an entry in the list by index
     */
    public EntryCardHandle getEntryCardHandle(int index) {
        return getEntryCardHandle(new Entry(getListView().getItems().get(index)));
    }

    /**
     * Gets an entry handle for an entry in the list
     */
    public EntryCardHandle getEntryCardHandle(ReadOnlyEntry entry) {
        Set<Node> nodes = getAllCardNodes();
        Optional<Node> entryCardNode = nodes.stream()
                .filter(n -> new EntryCardHandle(guiRobot, primaryStage, n).isSameEntry(entry))
                .findFirst();
        if (entryCardNode.isPresent()) {
            return new EntryCardHandle(guiRobot, primaryStage, entryCardNode.get());
        } else {
            return null;
        }
    }

    protected Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    public int getNumberOfEntries() {
        return getListView().getItems().size();
    }
}
