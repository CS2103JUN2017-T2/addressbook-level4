package seedu.multitasky.model;

import java.util.Objects;

import seedu.multitasky.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private static int index = 1;
    private static String entryBookSnapshotPath = "data/snapshots/entrybook";
    private GuiSettings guiSettings;
    private String entryBookFilePath = "data/entrybook.xml";
    private String entryBookName = "MyEntryBook";

    public UserPrefs() {
        this.setGuiSettings(500, 500, 0, 0);
    }

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }

    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
    }

    public String getEntryBookFilePath() {
        return entryBookFilePath;
    }

    public void setEntryBookFilePath(String entryBookFilePath) {
        this.entryBookFilePath = entryBookFilePath;
    }

    public static String getEntryBookSnapshotPath() {
        return entryBookSnapshotPath;
    }

    public static void setEntryBookSnapshotPath(String entryBookSnapshotPath) {
        UserPrefs.entryBookSnapshotPath = entryBookSnapshotPath;
    }

    public static void incrementIndexByOne() {
        UserPrefs.setIndex(UserPrefs.getIndex() + 1);
    }

    public static void decrementIndexByOne() {
        UserPrefs.setIndex(UserPrefs.getIndex() - 1);
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        UserPrefs.index = index;
    }

    public String getEntryBookName() {
        return entryBookName;
    }

    public void setEntryBookName(String entryBookName) {
        this.entryBookName = entryBookName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { // this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return Objects.equals(guiSettings, o.guiSettings) && Objects.equals(entryBookFilePath, o.entryBookFilePath)
               && Objects.equals(entryBookName, o.entryBookName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, entryBookFilePath, entryBookName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings.toString());
        sb.append("\nLocal data file location : " + entryBookFilePath);
        sb.append("\nEntryBook name : " + entryBookName);
        return sb.toString();
    }

}
