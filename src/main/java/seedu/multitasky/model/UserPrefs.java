package seedu.multitasky.model;

import java.util.Objects;

import seedu.multitasky.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
// @@author A0132788U
public class UserPrefs implements ConfigurableUserPrefs {
    /** Index to maintain snapshot file number */
    private static int index = 0;
    /** Snapshot file path without index and xml */
    private static String entryBookSnapshotPath = "data/snapshots/entrybook";
    private GuiSettings guiSettings;
    private String entryBookName = "MyEntryBook";
    private String entryBookFilePath = "data/entrybook.xml";
    // @@author A0140633R
    private final int defaultDurationHour = 1;
    // @@author

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

    // @@author A0132788U
    // ================ Storage UserPrefs methods ==============================

    @Override
    public String getEntryBookFilePath() {
        return entryBookFilePath;
    }

    @Override
    public void setEntryBookFilePath(String entryBookFilePath) {
        this.entryBookFilePath = entryBookFilePath;
    }

    /**
     * The getter and setter for the snapshot file path
     */
    public static String getEntryBookSnapshotPath() {
        return entryBookSnapshotPath;
    }

    public static void setEntryBookSnapshotPath(String entryBookSnapshotPath) {
        UserPrefs.entryBookSnapshotPath = entryBookSnapshotPath;
    }

    /**
     * Methods to update the indices when files are created during mutation/deleted during exit
     */
    public static void incrementIndexByOne() {
        index++;
    }

    public static void decrementIndexByOne() {
        index--;
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        UserPrefs.index = index;
    }

    // @@author A0140633R
    // ================ Logic UserPrefs methods ==============================
    @Override
    public int getDurationHour() {
        return defaultDurationHour;
    }
}
