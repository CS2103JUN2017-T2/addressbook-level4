package seedu.multitasky.model.entry;

import static seedu.multitasky.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

import seedu.multitasky.model.tag.Tag;

public class Event extends Entry {

    private Calendar _startDateAndTime;
    private Calendar _endDateAndTime;

    /**
     * Every field must be present and not null.
     */
    public Event(Name name, Calendar startDateAndTime, Calendar endDateAndTime, Set<Tag> tags) {
        super(name, tags);
        requireAllNonNull(startDateAndTime, endDateAndTime);
        _startDateAndTime = startDateAndTime;
        _endDateAndTime = endDateAndTime;
    }

    /**
     * Creates a copy of the given ReadOnlyEntry.
     * Pre-condition: ReadOnlyEntry must be type Event.
     */
    public Event(ReadOnlyEntry source) {
        super(source.getName(), source.getTags());

        // Checks if source is really an Event type.
        assert (source instanceof Event);
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
    }

    @Override
    public Calendar getStartDateAndTime() {
        return _startDateAndTime;
    }

    public void setEndDateAndTime(Calendar endDateAndTime) {
        _endDateAndTime = endDateAndTime;
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
        return (other instanceof Event && this.getName().equals(other.getName()) // instanceof
                                                                                 // handles nulls
                && this.getStartDateAndTime().equals(other.getStartDateAndTime())
                && this.getEndDateAndTime().equals(other.getEndDateAndTime())
                && this.getTags().equals(other.getTags()));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(getName(), getStartDateAndTime(), getEndDateAndTime(), getTags());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName()).append(" Start: ").append(getStartDateAndTime()).append(" End: ")
               .append(getEndDateAndTime()).append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
