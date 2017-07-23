package seedu.multitasky.model;

import java.util.Objects;

import seedu.multitasky.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs implements LogicUserPrefs, StorageUserPrefs {

    private String entryBookName = "MyEntryBook";
    private String entryBookFilePath = "data/entrybook.xml";
    /** Snapshot file path without index and xml */
    private String entryBookSnapshotPath = "data/snapshots/entrybook";
    private GuiSettings guiSettings;
    // @@author A0140633R
    private int defaultDurationHour;
    // @@author

    public UserPrefs() {
        this.setGuiSettings(500, 500, 0, 0);
        this.setDurationHour();
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
  
    @Override
    public int getDurationHour() {
        return defaultDurationHour;
    }

    private void setDurationHour() {
        this.defaultDurationHour = defaultDurationHour == 0 ? 1 : defaultDurationHour;
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

    @Override
    public String getEntryBookSnapshotPath() {
        return entryBookSnapshotPath;
    }

    @Override
    public void setEntryBookSnapshotPath(String entryBookSnapshotPath) {
        this.entryBookSnapshotPath = entryBookSnapshotPath;
    }

    // @@author A0140633R
    // ================ Logic UserPrefs methods ==============================
    @Override
    public int getDurationHour() {
        return defaultDurationHour;
    }
}
