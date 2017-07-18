package seedu.multitasky.model.entry;

import static seedu.multitasky.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

import seedu.multitasky.model.tag.Tag;

//@@author A0126623L
public class Event extends Entry implements OverdueCapable {

    private Calendar _startDateAndTime;
    private Calendar _endDateAndTime;

    /**
     * Every field must be present and not null.
     * Events are instantiated to be active events by default.
     * @param name
     * @param startDateAndTime
     * @param endDateAndTime
     * @param state
     * @param tags
     */
    public Event(Name name, Calendar startDateAndTime, Calendar endDateAndTime, Set<Tag> tags) {
        super(name, tags);
        requireAllNonNull(startDateAndTime, endDateAndTime);
        this.setStartDateAndTime(startDateAndTime);
        this.setEndDateAndTime(endDateAndTime);
    }

    /**
     * Creates a copy of the given ReadOnlyEntry.
     * @param source must be of type Event.
     */
    public Event(ReadOnlyEntry source) {
        super(source.getName(), source.getTags());

        // Checks if source has start and end time.
        requireAllNonNull(source.getStartDateAndTime(), source.getEndDateAndTime());

        setStartDateAndTime(source.getStartDateAndTime());
        setEndDateAndTime(source.getEndDateAndTime());
    }

    /**
     * Updates this entry with the details of {@code replacement}.
     * Pre-condition: ReadOnlyEntry must be type Event.
     */
    @Override
    public void resetData(ReadOnlyEntry replacement) {
        super.resetData(replacement);

        assert (replacement instanceof Event);
        setStartDateAndTime(replacement.getStartDateAndTime());
        setEndDateAndTime(replacement.getEndDateAndTime());
    }

    private void setStartDateAndTime(Calendar startDateAndTime) {
        _startDateAndTime = startDateAndTime;

        // Ignore difference in millisecond and seconds
        _startDateAndTime.set(Calendar.MILLISECOND, 0);
        _startDateAndTime.set(Calendar.SECOND, 0);
    }

    public String getStartDateAndTimeString() {
        return dateFormatter.format(getStartDateAndTime().getTime());
    }

    @Override
    public Calendar getStartDateAndTime() {
        return _startDateAndTime;
    }

    public void setEndDateAndTime(Calendar endDateAndTime) {
        _endDateAndTime = endDateAndTime;

        // Ignore difference in millisecond and seconds
        _endDateAndTime.set(Calendar.MILLISECOND, 0);
        _endDateAndTime.set(Calendar.SECOND, 0);
    }

    public String getEndDateAndTimeString() {
        return dateFormatter.format(getEndDateAndTime().getTime());
    }

    @Override
    public Calendar getEndDateAndTime() {
        return _endDateAndTime;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
               || this.isSameStateAs((ReadOnlyEntry) other);
    }

    @Override
    public boolean isSameStateAs(ReadOnlyEntry other) {
        return (other instanceof Event && this.getName().equals(other.getName()) // instanceof handles nulls
                && this.getStartDateAndTime().equals(other.getStartDateAndTime())
                && this.getEndDateAndTime().equals(other.getEndDateAndTime())
                && this.getState().equals(other.getState())
                && this.getTags().equals(other.getTags()));
    }

    // @@author A0126623L
    /**
     * Checks whether a given {@code event}'s time overlaps with this {@code event}'s.
     * @param {@code entry} must be an event.
     * @return boolean
     */
    public boolean hasOverlappingTime(ReadOnlyEntry other) {
        if (!(other instanceof Event)) {
            throw new AssertionError("Non-event object is given to Event.hasOverlappingTime().");
        }

        if (!(this.isActive())) {
            return false;
        }

        return !(other.getEndDateAndTime().compareTo(this.getStartDateAndTime()) < 0
                 || other.getStartDateAndTime().compareTo(this.getEndDateAndTime()) > 0);
    }
    // @@author

    // @@author A0126623L
    @Override
    public boolean isOverdue() {
        if (!(this.isActive())) {
            return false;
        }

        Calendar currentCalendar = Calendar.getInstance();
        return ((this.getEndDateAndTime().compareTo(currentCalendar)) < 0
                || (this.getStartDateAndTime().compareTo(currentCalendar) < 0));

    }
    // @@author

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(getName(), getStartDateAndTime(), getEndDateAndTime(), getState(), getTags());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        // TODO: Include state in string?
        builder.append(getName())
               .append(" Start: ")
               .append(dateFormatter.format(getStartDateAndTime().getTime()))
               .append(" End: ")
               .append(dateFormatter.format(getEndDateAndTime().getTime()))
               .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
