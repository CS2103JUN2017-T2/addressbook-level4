package seedu.multitasky.model;

// @@author A0140633R
/**
 * Represents the default values in UserPrefs object that logic has access to.
 */
public interface LogicUserPrefs {

    // returns default hour durations from preferences.json used to extend event entries' date time
    public int getDurationHour();

}
