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
    public Event(Name name, Calendar startDateAndTime, Calendar endDateAndTime, Entry.State state,
            Set<Tag> tags) {
        super(name, state, tags);
        requireAllNonNull(startDateAndTime, endDateAndTime);
        _startDateAndTime = startDateAndTime;
        _endDateAndTime = endDateAndTime;
    }

    /**
     * Creates a copy of the given ReadOnlyEntry.
     * Pre-condition: ReadOnlyEntry must be of type Event.
     */
    public Event(ReadOnlyEntry source) {
        super(source.getName(), source.getState(), source.getTags());

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

    //@@author A0125586X
    /**
     * Compares this to another event for sorting by starting date.
     * @return <0 if this event is sooner, 0 if they're the same, and >0 if this event is later
     */
    public int compareTo(ReadOnlyEntry other) throws NullPointerException, ClassCastException {
        assert other instanceof Event : "Event::compareTo must receive Event object as argument";
        return this.getStartDateAndTime().compareTo(other.getStartDateAndTime());
    }
    //@@author

    @Override
    public boolean isSameStateAs(ReadOnlyEntry other) {
        return (other instanceof Event && this.getName().equals(other.getName()) // instanceof handles nulls
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
