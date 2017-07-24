package guitests.guihandles;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;

//@@author A0125586X
/**
 * Provides a handle to a entry card in the entry list panel.
 */
public class EntryCardHandle extends GuiHandle {
    private static final String NAME_FIELD_ID = "#name";
    private static final String START_DATE_TIME_ID = "#startDateTime";
    private static final String END_DATE_TIME_ID = "#endDateTime";
    private static final String ADDITIONAL_INFO_ID = "#additionalInfo";
    private static final String TAGS_FIELD_ID = "#tags";

    private Node node;

    public EntryCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node) {
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    public String getName() {
        return getTextFromLabel(NAME_FIELD_ID);
    }

    public String getStartDateTime() {
        return getTextFromLabel(START_DATE_TIME_ID);
    }

    public String getEndDateTime() {
        return getTextFromLabel(END_DATE_TIME_ID);
    }

    public String getAdditionalInfo() {
        return getTextFromLabel(ADDITIONAL_INFO_ID);
    }

    public List<String> getTags() {
        return getTags(getTagsContainer());
    }

    private List<String> getTags(Region tagsContainer) {
        return tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(node -> ((Labeled) node).getText())
                .collect(Collectors.toList());
    }

    private List<String> getTags(Set<Tag> tags) {
        return tags
                .stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.toList());
    }

    private Region getTagsContainer() {
        return guiRobot.from(node).lookup(TAGS_FIELD_ID).query();
    }

    public boolean isSameEntry(ReadOnlyEntry entry) {
        return getName().equals(entry.getName().toString())
                && getTags().equals(getTags(entry.getTags()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EntryCardHandle) {
            EntryCardHandle handle = (EntryCardHandle) obj;
            return getName().equals(handle.getName())
                && getTags().equals(handle.getTags());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getName();
    }
}
