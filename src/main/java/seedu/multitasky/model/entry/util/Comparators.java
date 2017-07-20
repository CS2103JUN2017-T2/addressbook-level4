package seedu.multitasky.model.entry.util;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.ReadOnlyEntry;

// @@author A0125586X
/**
 * Utility class of different comparators to use when comparing two entries
 */
public class Comparators {

    /**
     * Default ordering in general is to have no change
     */
    public static final Comparator<ReadOnlyEntry> ENTRY_DEFAULT = new Comparator<ReadOnlyEntry>() {
        @Override
        public int compare(ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
            if (!(entry1.getClass().equals(entry2.getClass()))) {
                throw new AssertionError("Both entries to compare must be the same type");
            }
            return 0;
        }
    };

    /**
     * Default ordering for events is by starting date and time, earliest first
     */
    public static final Comparator<ReadOnlyEntry> EVENT_DEFAULT = new Comparator<ReadOnlyEntry>() {
        @Override
        public int compare(ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
            if (!(entry1 instanceof Event)) {
                throw new AssertionError("Event comparator must receive Event object as argument");
            }
            if (!(entry2 instanceof Event)) {
                throw new AssertionError("Event comparator must receive Event object as argument");
            }
            // Order by increasing starting date and time
            return entry1.getStartDateAndTime().compareTo(entry2.getStartDateAndTime());
        }
    };

    /**
     * Default ordering for deadlines is by ending date and time, earliest first
     */
    public static final Comparator<ReadOnlyEntry> DEADLINE_DEFAULT = new Comparator<ReadOnlyEntry>() {
        @Override
        public int compare(ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
            if (!(entry1 instanceof Deadline)) {
                throw new AssertionError("Deadline comparator must receive Deadline object as argument");
            }
            if (!(entry2 instanceof Deadline)) {
                throw new AssertionError("Deadline comparator must receive Deadline object as argument");
            }
            // Order by increasing ending(deadline) date and time
            return entry1.getEndDateAndTime().compareTo(entry2.getEndDateAndTime());
        }
    };

    /**
     * Default ordering for floating tasks is the order in which they were added, earliest first
     */
    public static final Comparator<ReadOnlyEntry> FLOATING_TASK_DEFAULT = new Comparator<ReadOnlyEntry>() {
        @Override
        public int compare(ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
            if (!(entry1 instanceof FloatingTask)) {
                throw new AssertionError("FloatingTask comparator must receive FloatingTask object as argument");
            }
            if (!(entry2 instanceof FloatingTask)) {
                throw new AssertionError("FloatingTask comparator must receive FloatingTask object as argument");
            }
            // No re-ordering of floating tasks
            return 0;
        }
    };

    /**
     * Reverse ordering for events is by starting date and time, latest first
     */
    public static final Comparator<ReadOnlyEntry> EVENT_REVERSE = new Comparator<ReadOnlyEntry>() {
        @Override
        public int compare(ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
            if (!(entry1 instanceof Event)) {
                throw new AssertionError("Event comparator must receive Event object as argument");
            }
            if (!(entry2 instanceof Event)) {
                throw new AssertionError("Event comparator must receive Event object as argument");
            }
            // Order by increasing starting date and time
            return entry2.getStartDateAndTime().compareTo(entry1.getStartDateAndTime());
        }
    };

    /**
     * Reverse ordering for deadlines is by ending date and time, latest first
     */
    public static final Comparator<ReadOnlyEntry> DEADLINE_REVERSE = new Comparator<ReadOnlyEntry>() {
        @Override
        public int compare(ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
            if (!(entry1 instanceof Deadline)) {
                throw new AssertionError("Deadline comparator must receive Deadline object as argument");
            }
            if (!(entry2 instanceof Deadline)) {
                throw new AssertionError("Deadline comparator must receive Deadline object as argument");
            }
            // Order by increasing ending(deadline) date and time
            return entry2.getEndDateAndTime().compareTo(entry1.getEndDateAndTime());
        }
    };

    /**
     * Reverse ordering for floating tasks is the order in which they were added, earliest first
     */
    public static final Comparator<ReadOnlyEntry> FLOATING_TASK_REVERSE = new Comparator<ReadOnlyEntry>() {
        @Override
        public int compare(ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
            if (!(entry1 instanceof FloatingTask)) {
                throw new AssertionError("FloatingTask comparator must receive FloatingTask object as argument");
            }
            if (!(entry2 instanceof FloatingTask)) {
                throw new AssertionError("FloatingTask comparator must receive FloatingTask object as argument");
            }
            // No re-ordering of floating tasks
            return 0;
        }
    };

    /**
     * Upcoming ordering for events is to show events that are not over yet first,
     * followed by events that are over.
     */
    public static final Comparator<ReadOnlyEntry> EVENT_UPCOMING = new Comparator<ReadOnlyEntry>() {
        @Override
        public int compare(ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
            if (!(entry1 instanceof Event)) {
                throw new AssertionError("Event comparator must receive Event object as argument");
            }
            if (!(entry2 instanceof Event)) {
                throw new AssertionError("Event comparator must receive Event object as argument");
            }
            Calendar currentTime = new GregorianCalendar();
            currentTime.setTime(new Date());
            if (entry1.getStartDateAndTime().before(currentTime)) {
                if (entry2.getStartDateAndTime().before(currentTime)) {
                    // Both events are in the past, perform normal sort relative to each other
                    return entry1.getStartDateAndTime().compareTo(entry2.getStartDateAndTime());
                } else {
                    // wrong order: entry1 should be after entry2 since it is in the past and entry2 is not
                    return 1;
                }
            } else if (entry2.getStartDateAndTime().before(currentTime)) {
                // correct order: entry2 should be after entry1 since it is in the past and entry1 is not
                return -1;
            } else {
                // Both events are upcoming, perform normal sort relative to each other
                return entry1.getStartDateAndTime().compareTo(entry2.getStartDateAndTime());
            }
        }
    };

    /**
     * Upcoming ordering for deadlines is to show deadlines that are not over yet first,
     * followed by deadlines that are over.
     */
    public static final Comparator<ReadOnlyEntry> DEADLINE_UPCOMING = new Comparator<ReadOnlyEntry>() {
        @Override
        public int compare(ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
            if (!(entry1 instanceof Deadline)) {
                throw new AssertionError("Deadline comparator must receive Deadline object as argument");
            }
            if (!(entry2 instanceof Deadline)) {
                throw new AssertionError("Deadline comparator must receive Deadline object as argument");
            }
            Calendar currentTime = new GregorianCalendar();
            currentTime.setTime(new Date());
            if (entry1.getEndDateAndTime().before(currentTime)) {
                if (entry2.getEndDateAndTime().before(currentTime)) {
                    // Both deadlines are in the past, perform normal sort relative to each other
                    return entry1.getEndDateAndTime().compareTo(entry2.getEndDateAndTime());
                } else {
                    // wrong order: entry1 should be after entry2 since it is in the past and entry2 is not
                    return 1;
                }
            } else if (entry2.getEndDateAndTime().before(currentTime)) {
                // correct order: entry2 should be after entry1 since it is in the past and entry1 is not
                return -1;
            } else {
                // Both deadlines are upcoming, perform normal sort relative to each other
                return entry1.getEndDateAndTime().compareTo(entry2.getEndDateAndTime());
            }
        }
    };

    /**
     * Upcoming ordering for floating tasks is the order in which they were added
     */
    public static final Comparator<ReadOnlyEntry> FLOATING_TASK_UPCOMING = FLOATING_TASK_DEFAULT;
}
