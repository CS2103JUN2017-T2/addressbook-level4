package seedu.multitasky.model.entry;

import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

import seedu.multitasky.model.tag.Tag;

public class FloatingTask extends Entry {

    /**
     * Every field must be present and not null.
     */
    public FloatingTask(Name name, Set<Tag> tags) {
        super(name, tags);
    }

    @Override
    public Calendar getStartDateAndTime() {
        Calendar startDateAndTime = null; // Floating tasks have no start time.
        return startDateAndTime;
    }

    @Override
    public Calendar getEndDateAndTime() {
        Calendar endDateAndTime = null; // Floating tasks have no end time.
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
                && this.getName().equals(other.getName()) && this.getTags().equals(other.getTags()));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(getName(), getTags());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName()).append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
