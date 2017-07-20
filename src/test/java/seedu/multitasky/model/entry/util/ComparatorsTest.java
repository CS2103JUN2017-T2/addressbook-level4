package seedu.multitasky.model.entry.util;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.util.EntryBuilder;

// @@author A0125586X
public class ComparatorsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Entry overdueEvent;
    private Entry upcomingEvent;
    private Entry futureEvent;

    private Entry overdueDeadline;
    private Entry upcomingDeadline;
    private Entry futureDeadline;

    private Entry floatingTask1;
    private Entry floatingTask2;

    @Before
    public void setUp() {
        Calendar overdueDate = new GregorianCalendar();
        Calendar overdueDate2 = new GregorianCalendar();
        Calendar upcomingDate = new GregorianCalendar();
        Calendar upcomingDate2 = new GregorianCalendar();
        Calendar futureDate = new GregorianCalendar();
        Calendar futureDate2 = new GregorianCalendar();
        overdueDate.setTime((new PrettyTimeParser().parse("yesterday 8am")).get(0));
        overdueDate2.setTime((new PrettyTimeParser().parse("yesterday 10am")).get(0));
        upcomingDate.setTime((new PrettyTimeParser().parse("tomorrow 8am")).get(0));
        upcomingDate2.setTime((new PrettyTimeParser().parse("tomorrow 10am")).get(0));
        futureDate.setTime((new PrettyTimeParser().parse("next friday 8am")).get(0));
        futureDate2.setTime((new PrettyTimeParser().parse("next friday 10am")).get(0));

        try {
            overdueEvent = EntryBuilder.build("overdue event", overdueDate, overdueDate2);
            upcomingEvent = EntryBuilder.build("upcoming event", upcomingDate, upcomingDate2);
            futureEvent = EntryBuilder.build("future event", futureDate, futureDate2);
            assertTrue(overdueEvent instanceof Event);
            assertTrue(upcomingEvent instanceof Event);
            assertTrue(futureEvent instanceof Event);

            overdueDeadline = EntryBuilder.build("overdue deadline", overdueDate);
            upcomingDeadline = EntryBuilder.build("upcoming deadline", upcomingDate);
            futureDeadline = EntryBuilder.build("future deadline", futureDate);
            assertTrue(overdueDeadline instanceof Deadline);
            assertTrue(upcomingDeadline instanceof Deadline);
            assertTrue(futureDeadline instanceof Deadline);

            floatingTask1 = EntryBuilder.build("floating task 1");
            floatingTask2 = EntryBuilder.build("floating task 2");
            assertTrue(floatingTask1 instanceof FloatingTask);
            assertTrue(floatingTask2 instanceof FloatingTask);
        } catch (Exception e) {
            throw new AssertionError("Test set up cannot throw exceptions");
        }
    }

    /****************************
     * Default Entry Comparator *
     ***************************/
    @Test
    public void comparators_entryDefaultMismatchType1_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.ENTRY_DEFAULT.compare(upcomingEvent, upcomingDeadline);
    }

    @Test
    public void comparators_entryDefaultMismatchType2_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.ENTRY_DEFAULT.compare(upcomingEvent, floatingTask1);
    }

    @Test
    public void comparators_entryDefaultMismatchType3_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.ENTRY_DEFAULT.compare(floatingTask1, upcomingDeadline);
    }

    @Test
    public void comparators_entryDefault_noOrdering() {
        assertEqual(Comparators.ENTRY_DEFAULT, overdueEvent, upcomingEvent);
        assertEqual(Comparators.ENTRY_DEFAULT, upcomingEvent, futureEvent);
        assertEqual(Comparators.ENTRY_DEFAULT, overdueEvent, futureEvent);

        assertEqual(Comparators.ENTRY_DEFAULT, overdueDeadline, upcomingDeadline);
        assertEqual(Comparators.ENTRY_DEFAULT, upcomingDeadline, futureDeadline);
        assertEqual(Comparators.ENTRY_DEFAULT, overdueDeadline, futureDeadline);

        assertEqual(Comparators.ENTRY_DEFAULT, floatingTask1, floatingTask2);
    }

    /****************************
     * Default Event Comparator *
     ***************************/
    @Test
    public void comparators_eventDefaultMismatchType1_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_DEFAULT.compare(upcomingEvent, upcomingDeadline);
    }

    @Test
    public void comparators_eventDefaultMismatchType2_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_DEFAULT.compare(upcomingEvent, floatingTask1);
    }

    @Test
    public void comparators_eventDefaultMismatchType3_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_DEFAULT.compare(upcomingDeadline, upcomingEvent);
    }

    @Test
    public void comparators_eventDefaultMismatchType4_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_DEFAULT.compare(floatingTask1, upcomingEvent);
    }

    @Test
    public void comparators_eventDefault_correctOrdering() {
        assertBefore(Comparators.EVENT_DEFAULT, overdueEvent, upcomingEvent);
        assertBefore(Comparators.EVENT_DEFAULT, upcomingEvent, futureEvent);
        assertBefore(Comparators.EVENT_DEFAULT, overdueEvent, futureEvent);
        assertEqual(Comparators.EVENT_DEFAULT, overdueEvent, overdueEvent);
        assertEqual(Comparators.EVENT_DEFAULT, upcomingEvent, upcomingEvent);
        assertEqual(Comparators.EVENT_DEFAULT, futureEvent, futureEvent);
        assertAfter(Comparators.EVENT_DEFAULT, upcomingEvent, overdueEvent);
        assertAfter(Comparators.EVENT_DEFAULT, futureEvent, upcomingEvent);
        assertAfter(Comparators.EVENT_DEFAULT, futureEvent, overdueEvent);
    }

    /*******************************
     * Default Deadline Comparator *
     ******************************/
    @Test
    public void comparators_deadlineDefaultMismatchType1_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_DEFAULT.compare(upcomingDeadline, upcomingEvent);
    }

    @Test
    public void comparators_deadlineDefaultMismatchType2_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_DEFAULT.compare(upcomingDeadline, floatingTask1);
    }

    @Test
    public void comparators_deadlineDefaultMismatchType3_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_DEFAULT.compare(upcomingEvent, upcomingDeadline);
    }

    @Test
    public void comparators_deadlineDefaultMismatchType4_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_DEFAULT.compare(floatingTask1, upcomingDeadline);
    }

    @Test
    public void comparators_deadlineDefault_correctOrdering() {
        assertBefore(Comparators.DEADLINE_DEFAULT, overdueDeadline, upcomingDeadline);
        assertBefore(Comparators.DEADLINE_DEFAULT, upcomingDeadline, futureDeadline);
        assertBefore(Comparators.DEADLINE_DEFAULT, overdueDeadline, futureDeadline);
        assertEqual(Comparators.DEADLINE_DEFAULT, overdueDeadline, overdueDeadline);
        assertEqual(Comparators.DEADLINE_DEFAULT, upcomingDeadline, upcomingDeadline);
        assertEqual(Comparators.DEADLINE_DEFAULT, futureDeadline, futureDeadline);
        assertAfter(Comparators.DEADLINE_DEFAULT, upcomingDeadline, overdueDeadline);
        assertAfter(Comparators.DEADLINE_DEFAULT, futureDeadline, upcomingDeadline);
        assertAfter(Comparators.DEADLINE_DEFAULT, futureDeadline, overdueDeadline);
    }

    /***********************************
     * Default FloatingTask Comparator *
     **********************************/
    @Test
    public void comparators_floatingTaskDefaultMismatchType1_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_DEFAULT.compare(upcomingEvent, floatingTask1);
    }

    @Test
    public void comparators_floatingTaskDefaultMismatchType2_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_DEFAULT.compare(upcomingDeadline, floatingTask1);
    }

    @Test
    public void comparators_floatingTaskDefaultMismatchType3_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_DEFAULT.compare(floatingTask1, upcomingEvent);
    }

    @Test
    public void comparators_floatingTaskDefaultMismatchType4_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_DEFAULT.compare(floatingTask1, upcomingDeadline);
    }

    @Test
    public void comparators_floatingTaskDefault_correctOrdering() {
        assertEqual(Comparators.FLOATING_TASK_DEFAULT, floatingTask1, floatingTask2);
        assertEqual(Comparators.FLOATING_TASK_DEFAULT, floatingTask2, floatingTask1);
    }

    /****************************
     * Reverse Event Comparator *
     ***************************/
    @Test
    public void comparators_eventReverseMismatchType1_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_REVERSE.compare(upcomingEvent, upcomingDeadline);
    }

    @Test
    public void comparators_eventReverseMismatchType2_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_REVERSE.compare(upcomingEvent, floatingTask1);
    }

    @Test
    public void comparators_eventReverseMismatchType3_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_REVERSE.compare(upcomingDeadline, upcomingEvent);
    }

    @Test
    public void comparators_eventReverseMismatchType4_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_REVERSE.compare(floatingTask1, upcomingEvent);
    }

    @Test
    public void comparators_eventReverse_correctOrdering() {
        assertAfter(Comparators.EVENT_REVERSE, overdueEvent, upcomingEvent);
        assertAfter(Comparators.EVENT_REVERSE, upcomingEvent, futureEvent);
        assertAfter(Comparators.EVENT_REVERSE, overdueEvent, futureEvent);
        assertEqual(Comparators.EVENT_REVERSE, overdueEvent, overdueEvent);
        assertEqual(Comparators.EVENT_REVERSE, upcomingEvent, upcomingEvent);
        assertEqual(Comparators.EVENT_REVERSE, futureEvent, futureEvent);
        assertBefore(Comparators.EVENT_REVERSE, upcomingEvent, overdueEvent);
        assertBefore(Comparators.EVENT_REVERSE, futureEvent, upcomingEvent);
        assertBefore(Comparators.EVENT_REVERSE, futureEvent, overdueEvent);
    }

    /*******************************
     * Reverse Deadline Comparator *
     ******************************/
    @Test
    public void comparators_deadlineReverseMismatchType1_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_REVERSE.compare(upcomingDeadline, upcomingEvent);
    }

    @Test
    public void comparators_deadlineReverseMismatchType2_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_REVERSE.compare(upcomingDeadline, floatingTask1);
    }

    @Test
    public void comparators_deadlineReverseMismatchType3_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_REVERSE.compare(upcomingEvent, upcomingDeadline);
    }

    @Test
    public void comparators_deadlineReverseMismatchType4_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_REVERSE.compare(floatingTask1, upcomingDeadline);
    }

    @Test
    public void comparators_deadlineReverse_correctOrdering() {
        assertAfter(Comparators.DEADLINE_REVERSE, overdueDeadline, upcomingDeadline);
        assertAfter(Comparators.DEADLINE_REVERSE, upcomingDeadline, futureDeadline);
        assertAfter(Comparators.DEADLINE_REVERSE, overdueDeadline, futureDeadline);
        assertEqual(Comparators.DEADLINE_REVERSE, overdueDeadline, overdueDeadline);
        assertEqual(Comparators.DEADLINE_REVERSE, upcomingDeadline, upcomingDeadline);
        assertEqual(Comparators.DEADLINE_REVERSE, futureDeadline, futureDeadline);
        assertBefore(Comparators.DEADLINE_REVERSE, upcomingDeadline, overdueDeadline);
        assertBefore(Comparators.DEADLINE_REVERSE, futureDeadline, upcomingDeadline);
        assertBefore(Comparators.DEADLINE_REVERSE, futureDeadline, overdueDeadline);
    }

    /***********************************
     * Reverse FloatingTask Comparator *
     **********************************/
    @Test
    public void comparators_floatingTaskReverseMismatchType1_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_REVERSE.compare(upcomingEvent, floatingTask1);
    }

    @Test
    public void comparators_floatingTaskReverseMismatchType2_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_REVERSE.compare(upcomingDeadline, floatingTask1);
    }

    @Test
    public void comparators_floatingTaskReverseMismatchType3_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_REVERSE.compare(floatingTask1, upcomingEvent);
    }

    @Test
    public void comparators_floatingTaskReverseMismatchType4_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_REVERSE.compare(floatingTask1, upcomingDeadline);
    }

    @Test
    public void comparators_floatingTaskReverse_correctOrdering() {
        assertEqual(Comparators.FLOATING_TASK_REVERSE, floatingTask1, floatingTask2);
        assertEqual(Comparators.FLOATING_TASK_REVERSE, floatingTask2, floatingTask1);
    }

    /*****************************
     * Upcoming Event Comparator *
     ****************************/
    @Test
    public void comparators_eventUpcomingMismatchType1_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_UPCOMING.compare(upcomingEvent, upcomingDeadline);
    }

    @Test
    public void comparators_eventUpcomingMismatchType2_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_UPCOMING.compare(upcomingEvent, floatingTask1);
    }

    @Test
    public void comparators_eventUpcomingMismatchType3_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_UPCOMING.compare(upcomingDeadline, upcomingEvent);
    }

    @Test
    public void comparators_eventUpcomingMismatchType4_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.EVENT_UPCOMING.compare(floatingTask1, upcomingEvent);
    }

    @Test
    public void comparators_eventUpcoming_correctOrdering() {
        assertAfter(Comparators.EVENT_UPCOMING, overdueEvent, upcomingEvent);
        assertAfter(Comparators.EVENT_UPCOMING, overdueEvent, futureEvent);
        assertAfter(Comparators.EVENT_UPCOMING, futureEvent, upcomingEvent);
        assertEqual(Comparators.EVENT_UPCOMING, overdueEvent, overdueEvent);
        assertEqual(Comparators.EVENT_UPCOMING, upcomingEvent, upcomingEvent);
        assertEqual(Comparators.EVENT_UPCOMING, futureEvent, futureEvent);
        assertBefore(Comparators.EVENT_UPCOMING, upcomingEvent, overdueEvent);
        assertBefore(Comparators.EVENT_UPCOMING, futureEvent, overdueEvent);
        assertBefore(Comparators.EVENT_UPCOMING, upcomingEvent, futureEvent);
    }

    /********************************
     * Upcoming Deadline Comparator *
     *******************************/
    @Test
    public void comparators_deadlineUpcomingMismatchType1_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_UPCOMING.compare(upcomingDeadline, upcomingEvent);
    }

    @Test
    public void comparators_deadlineUpcomingMismatchType2_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_UPCOMING.compare(upcomingDeadline, floatingTask1);
    }

    @Test
    public void comparators_deadlineUpcomingMismatchType3_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_UPCOMING.compare(upcomingEvent, upcomingDeadline);
    }

    @Test
    public void comparators_deadlineUpcomingMismatchType4_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.DEADLINE_UPCOMING.compare(floatingTask1, upcomingDeadline);
    }

    @Test
    public void comparators_deadlineUpcoming_correctOrdering() {
        assertAfter(Comparators.DEADLINE_UPCOMING, overdueDeadline, upcomingDeadline);
        assertAfter(Comparators.DEADLINE_UPCOMING, futureDeadline, upcomingDeadline);
        assertAfter(Comparators.DEADLINE_UPCOMING, overdueDeadline, futureDeadline);
        assertEqual(Comparators.DEADLINE_UPCOMING, overdueDeadline, overdueDeadline);
        assertEqual(Comparators.DEADLINE_UPCOMING, upcomingDeadline, upcomingDeadline);
        assertEqual(Comparators.DEADLINE_UPCOMING, futureDeadline, futureDeadline);
        assertBefore(Comparators.DEADLINE_UPCOMING, upcomingDeadline, overdueDeadline);
        assertBefore(Comparators.DEADLINE_UPCOMING, upcomingDeadline, futureDeadline);
        assertBefore(Comparators.DEADLINE_UPCOMING, futureDeadline, overdueDeadline);
    }

    /************************************
     * Upcoming FloatingTask Comparator *
     ***********************************/
    @Test
    public void comparators_floatingTaskUpcomingMismatchType1_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_UPCOMING.compare(upcomingEvent, floatingTask1);
    }

    @Test
    public void comparators_floatingTaskUpcomingMismatchType2_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_UPCOMING.compare(upcomingDeadline, floatingTask1);
    }

    @Test
    public void comparators_floatingTaskUpcomingMismatchType3_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_UPCOMING.compare(floatingTask1, upcomingEvent);
    }

    @Test
    public void comparators_floatingTaskUpcomingMismatchType4_exceptionThrown() {
        thrown.expect(AssertionError.class);
        Comparators.FLOATING_TASK_UPCOMING.compare(floatingTask1, upcomingDeadline);
    }

    @Test
    public void comparators_floatingTaskUpcoming_correctOrdering() {
        assertEqual(Comparators.FLOATING_TASK_UPCOMING, floatingTask1, floatingTask2);
        assertEqual(Comparators.FLOATING_TASK_UPCOMING, floatingTask2, floatingTask1);
    }

    private void assertBefore(Comparator<ReadOnlyEntry> comparator, ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
        assertTrue(comparator.compare(entry1, entry2) < 0);
    }

    private void assertEqual(Comparator<ReadOnlyEntry> comparator, ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
        assertTrue(comparator.compare(entry1, entry2) == 0);
    }

    private void assertAfter(Comparator<ReadOnlyEntry> comparator, ReadOnlyEntry entry1, ReadOnlyEntry entry2) {
        assertTrue(comparator.compare(entry1, entry2) > 0);
    }

}
