package seedu.multitasky.model.entry;

import static seedu.multitasky.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

import seedu.multitasky.model.tag.Tag;

//@@author A0126623L
public class Deadline extends Entry implements OverdueCapable {

    private Calendar startDateAndTime;
    private Calendar endDateAndTime;

    /**
     * Every field must be present and not null.
     */
    public Deadline(Name name, Calendar endDateAndTime, Set<Tag> tags) {
        super(name, tags);
        requireAllNonNull(endDateAndTime);
        startDateAndTime = null;
        this.setEndDateAndTime(endDateAndTime);
    }

    /**
     * Creates a copy of the given ReadOnlyEntry.
     * @param source must be type Deadline.
     */
    public Deadline(ReadOnlyEntry source) {
        super(source.getName(), source.getTags());

        if (!(source instanceof Deadline)) {
            throw new AssertionError("Deadline construction failed.");
        }

        requireAllNonNull(source.getEndDateAndTime());
        setEndDateAndTime(source.getEndDateAndTime());
    }

    /**
     * Updates this entry with the details of {@code replacement}.
     * @param replacement must be of type Deadline.
     */
    @Override
    public void resetData(ReadOnlyEntry replacement) {
        super.resetData(replacement);

        if (!(replacement instanceof Deadline)) {
            throw new AssertionError("Argument must be a deadline.");
        }

        setEndDateAndTime(replacement.getEndDateAndTime());
    }

    public String getStartDateAndTimeString() {
        return null; // Deadlines has no start time.
    }

    @Override
    public Calendar getStartDateAndTime() {
        return startDateAndTime; // Deadlines has no start time.
    }

    public void setEndDateAndTime(Calendar endDateAndTime) {
        this.endDateAndTime = endDateAndTime;

        // Ignore differences in milliseconds or seconds and seconds
        this.endDateAndTime.set(Calendar.MILLISECOND, 0);
        this.endDateAndTime.set(Calendar.SECOND, 0);
    }

    public String getEndDateAndTimeString() {
        return dateFormatter.format(getEndDateAndTime().getTime());
    }

    @Override
    public Calendar getEndDateAndTime() {
        return this.endDateAndTime;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
               || this.isSameStateAs((ReadOnlyEntry) other);
    }

    @Override
    public boolean isSameStateAs(ReadOnlyEntry other) {
        return (other instanceof Deadline
                && this.getName().equals(other.getName()) // instanceof handles nulls
                && this.getEndDateAndTime().equals(other.getEndDateAndTime())
                && this.getState().equals(other.getState()));
    }

    @Override
    public boolean isOverdue() {
        if (!(this.isActive())) {
            return false;
        }

        Calendar currentCalendar = Calendar.getInstance();
        return ((this.getEndDateAndTime().compareTo(currentCalendar)) < 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEndDateAndTime(), getState(), getTags());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(getName())
               .append(", Deadline: ")
               .append(dateFormatter.format(getEndDateAndTime().getTime()))
               .append(", Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
