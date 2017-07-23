package seedu.multitasky.model.entry;

import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

import seedu.multitasky.model.tag.Tag;

// @@author A0126623L
public class FloatingTask extends Entry {

    private Calendar startDateAndTime;
    private Calendar endDateAndTime;

    /**
     * Every field must be present and not null.
     */
    public FloatingTask(Name name, Set<Tag> tags) {
        super(name, tags);
        startDateAndTime = null;
        endDateAndTime = null;
    }

    /**
     * Creates a copy of the given ReadOnlyEntry.
     * Pre-condition: source must be of type FloatingTask.
     */
    public FloatingTask(ReadOnlyEntry source) {
        super(source.getName(), source.getTags());
    }

    public String getStartDateAndTimeString() {
        return null; // Floating tasks have no start time.
    }

    @Override
    public Calendar getStartDateAndTime() {
        return startDateAndTime;
    }

    public String getEndDateAndTimeString() {
        return null; // Floating tasks have no end time.
    }

    @Override
    public Calendar getEndDateAndTime() {
        return endDateAndTime;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
               || this.isSameStateAs((ReadOnlyEntry) other);
    }

    /**
     * Compares the state with another Floating Task.
     */
    @Override
    public boolean isSameStateAs(ReadOnlyEntry other) {
        return (other instanceof FloatingTask // instanceof handles nulls
                && this.getName().equals(other.getName())
                && this.getState().equals(other.getState()));
    }

    // @@author A0126623L-reused
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getState(), getTags());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(getName()).append(", Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
