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

    /**
     * Creates a copy of the given ReadOnlyEntry.
     * Pre-condition: source must be of type FloatingTask.
     */
    public FloatingTask(ReadOnlyEntry source) {
        super(source.getName(), source.getTags());
    }

    public Calendar getStartDateAndTimeString() {
        return null; // Floating tasks have no start time.
    }

    @Override
    public Calendar getStartDateAndTime() {
        Calendar startDateAndTime = null; // Floating tasks have no start time.
        return startDateAndTime;
    }

    public Calendar getEndDateAndTimeString() {
        return null; // Floating tasks have no end time.
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

    //@@author A0125586X
    /**
     * Compares this to another floating task.
     * @return 0 as there is no ordering to floating tasks at the moment.
     */
    public int compareTo(ReadOnlyEntry other) throws NullPointerException, ClassCastException {
        assert other instanceof FloatingTask : "FloatingTask::compareTo must receive FloatingTask object as argument";
        return 0;
    }
    //@@author

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
