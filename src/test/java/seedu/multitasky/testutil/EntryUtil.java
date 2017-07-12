package seedu.multitasky.testutil;

import java.util.Calendar;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;

//@@author A0125586X
/**
 * A utility class for Entry.
 */
public class EntryUtil {

    /**
     * Returns an add command string for adding the {@code entry} as an event.
     */
    public static String getEventAddCommand(Entry entry) {
        return AddCommand.COMMAND_WORD + " " + getEventDetailsForAdd(entry);
    }

    /**
     * Returns an add command string for adding the {@code entry} as a deadline.
     */
    public static String getDeadlineAddCommand(Entry entry) {
        return AddCommand.COMMAND_WORD + " " + getDeadlineDetailsForAdd(entry);
    }

    /**
     * Returns an add command string for adding the {@code entry} as a floating task.
     */
    public static String getFloatingTaskAddCommand(Entry entry) {
        return AddCommand.COMMAND_WORD + " " + getFloatingTaskDetailsForAdd(entry);
    }

    /**
     * Returns a delete command string for deleting an event by index.
     */
    public static String getEventDeleteByIndexCommand(Index index) {
        return DeleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_EVENT + " " + index.getOneBased();
    }

    /**
     * Returns a delete command string for deleting a deadline by index.
     */
    public static String getDeadlineDeleteByIndexCommand(Index index) {
        return DeleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DEADLINE + " " + index.getOneBased();
    }

    /**
     * Returns a delete command string for deleting a floating task by index.
     */
    public static String getFloatingTaskDeleteByIndexCommand(Index index) {
        return DeleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_FLOATINGTASK + " " + index.getOneBased();
    }

    /**
     * Returns the part of command string for the given {@code entry}'s details
     * for adding an event.
     */
    public static String getEventDetailsForAdd(Entry entry) {
        assert entry instanceof Event;
        StringBuilder builder = new StringBuilder();
        builder.append(entry.getName().toString() + " ")
               .append(CliSyntax.PREFIX_FROM + " ")
               .append(getDateDetailsAsInputString(entry.getStartDateAndTime()) + " ")
               .append(CliSyntax.PREFIX_TO + " ")
               .append(getDateDetailsAsInputString(entry.getEndDateAndTime()) + " ");
        builder.append(CliSyntax.PREFIX_TAG + " ");
        entry.getTags().stream().forEach(s -> builder.append(s.tagName + " "));
        return builder.toString();
    }

    /**
     * Returns the part of command string for the given {@code entry}'s details
     * for adding an event.
     */
    public static String getDeadlineDetailsForAdd(Entry entry) {
        assert entry instanceof Deadline;
        StringBuilder builder = new StringBuilder();
        builder.append(entry.getName().toString() + " ")
               .append(CliSyntax.PREFIX_BY + " ")
               .append(getDateDetailsAsInputString(entry.getEndDateAndTime()) + " ");
        builder.append(CliSyntax.PREFIX_TAG + " ");
        entry.getTags().stream().forEach(s -> builder.append(s.tagName + " "));
        return builder.toString();
    }

    /**
     * Returns the part of command string for the given {@code entry}'s details
     * for adding a floating task.
     */
    public static String getFloatingTaskDetailsForAdd(Entry entry) {
        assert entry instanceof FloatingTask;
        StringBuilder builder = new StringBuilder();
        builder.append(entry.getName().toString() + " ");
        builder.append(CliSyntax.PREFIX_TAG + " ");
        entry.getTags().stream().forEach(s -> builder.append(s.tagName + " "));
        return builder.toString();
    }

    /**
     * Returns a string version of the calendar date that is ready for use in input
     */
    public static String getDateDetailsAsInputString(Calendar calendar) {
        assert calendar != null;
        StringBuilder builder = new StringBuilder();
        builder.append(calendar.get(Calendar.DAY_OF_MONTH) + "/")
               .append((calendar.get(Calendar.MONTH) + 1) + "/") // +1 due to MONTH being 0-based (JAN = 0)
               .append(calendar.get(Calendar.YEAR) + " ")
               .append(calendar.get(Calendar.HOUR_OF_DAY) + ":")
               .append(calendar.get(Calendar.MINUTE));
        return builder.toString();
    }
}
