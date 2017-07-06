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
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.testutil.TestUtil;

/**
 * Provides a handle for the panel containing the event entry list.
 */
public class FloatingTaskListPanelHandle extends GuiHandle {

    public static final int NOT_FOUND = -1;
    public static final String CARD_PANE_ID = "#cardPane";

    private static final String FLOATING_TASK_LIST_VIEW_ID = "#floatingTaskListView";

    public FloatingTaskListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public List<ReadOnlyEntry> getSelectedEntries() {
        ListView<ReadOnlyEntry> entryList = getListView();
        return entryList.getSelectionModel().getSelectedItems();
    }

    public ListView<ReadOnlyEntry> getListView() {
        return getNode(FLOATING_TASK_LIST_VIEW_ID);
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
                    + "Expected " + (getListView().getItems().size() - 1) + " events");
        }
        assertTrue(this.containsInOrder(startPosition, entries));
        for (int i = 0; i < entries.length; i++) {
            final int scrollTo = i + startPosition;
            guiRobot.interact(() -> getListView().scrollTo(scrollTo));
            guiRobot.sleep(200);
            if (!TestUtil.compareCardAndPerson(getPersonCardHandle(startPosition + i), entries[i])) {
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
        List<ReadOnlyEntry> personsInList = getListView().getItems();

        // Return false if the list in panel is too short to contain the given list
        if (startPosition + entries.length > personsInList.size()) {
            return false;
        }

        // Return false if any of the entries doesn't match
        for (int i = 0; i < entries.length; i++) {
            if (!personsInList.get(startPosition + i).getName().fullName.equals(entries[i].getName().fullName)) {
                return false;
            }
        }

        return true;
    }

    public PersonCardHandle navigateToPerson(String name) {
        guiRobot.sleep(500); //Allow a bit of time for the list to be updated
        final Optional<ReadOnlyEntry> person = getListView().getItems().stream()
                                                    .filter(p -> p.getName().fullName.equals(name))
                                                    .findAny();
        if (!person.isPresent()) {
            throw new IllegalStateException("Name not found: " + name);
        }

        return navigateToPerson(person.get());
    }

    /**
     * Navigates the listview to display and select the person.
     */
    public PersonCardHandle navigateToPerson(ReadOnlyEntry person) {
        int index = getPersonIndex(person);

        guiRobot.interact(() -> {
            getListView().scrollTo(index);
            guiRobot.sleep(150);
            getListView().getSelectionModel().select(index);
        });
        guiRobot.sleep(100);
        return getPersonCardHandle(person);
    }


    /**
     * Returns the position of the person given, {@code NOT_FOUND} if not found in the list.
     */
    public int getPersonIndex(ReadOnlyEntry targetPerson) {
        List<ReadOnlyEntry> personsInList = getListView().getItems();
        for (int i = 0; i < personsInList.size(); i++) {
            if (personsInList.get(i).getName().equals(targetPerson.getName())) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Gets a person from the list by index
     */
    public ReadOnlyEntry getPerson(int index) {
        return getListView().getItems().get(index);
    }

    public PersonCardHandle getPersonCardHandle(int index) {
        return getPersonCardHandle(new Person(getListView().getItems().get(index)));
    }

    public PersonCardHandle getPersonCardHandle(ReadOnlyEntry person) {
        Set<Node> nodes = getAllCardNodes();
        Optional<Node> personCardNode = nodes.stream()
                .filter(n -> new PersonCardHandle(guiRobot, primaryStage, n).isSamePerson(person))
                .findFirst();
        if (personCardNode.isPresent()) {
            return new PersonCardHandle(guiRobot, primaryStage, personCardNode.get());
        } else {
            return null;
        }
    }

    protected Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    public int getNumberOfPeople() {
        return getListView().getItems().size();
    }
}
