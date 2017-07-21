package seedu.multitasky.testutil;

import java.util.Calendar;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.CompleteCommand;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.ReadOnlyEntry;

// @@author A0125586X
/**
 * A utility class for commands, to get the correct command phrase for testing.
 */
public class CommandUtil {

    /**
     * Returns an add command string for adding an event.
     */
    public static String getAddEventCommand(Entry entry) {
        return AddCommand.COMMAND_WORD + " " + getEventDetails(entry);
    }

    /**
     * Returns an add command string for adding a deadline.
     */
    public static String getAddDeadlineCommand(Entry entry) {
        return AddCommand.COMMAND_WORD + " " + getDeadlineDetails(entry);
    }

    /**
     * Returns an add command string for adding a floating task.
     */
    public static String getAddFloatingTaskCommand(Entry entry) {
        return AddCommand.COMMAND_WORD + " " + getFloatingTaskDetails(entry);
    }
    // @@author

    // @@author A0126623L
    /**
     * Returns a delete command string for deleting by an entry's full name.
     */
    public static String getDeleteEventByFullNameCommand(ReadOnlyEntry entry) {
        return DeleteCommand.COMMAND_WORD + " " + entry.getName().toString();
    }

    // @@author A0125586X
    /**
     * Returns a delete command string for deleting an event by index.
     */
    public static String getDeleteEventByIndexCommand(Index index) {
        return DeleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_EVENT + " " + index.getOneBased();
    }

    /**
     * Returns a delete command string for deleting a deadline by index.
     */
    public static String getDeleteDeadlineByIndexCommand(Index index) {
        return DeleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DEADLINE + " " + index.getOneBased();
    }

    /**
     * Returns a delete command string for deleting a floating task by index.
     */
    public static String getDeleteFloatingTaskByIndexCommand(Index index) {
        return DeleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_FLOATINGTASK + " " + index.getOneBased();
    }

    /**
     * Returns an edit command string for editing an event by index.
     */
    public static String getEditEventByIndexCommand(Index index, Entry editedEntry) {
        return EditCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_EVENT + " " + index.getOneBased() + " "
               + CliSyntax.PREFIX_NAME + " " + getEventDetails(editedEntry);
    }

    /**
     * Returns an edit command string for editing a deadline by index.
     */
    public static String getEditDeadlineByIndexCommand(Index index, Entry editedEntry) {
        return EditCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DEADLINE + " " + index.getOneBased() + " "
               + CliSyntax.PREFIX_NAME + " " + getDeadlineDetails(editedEntry);
    }

    /**
     * Returns an edit command string for editing a floating task by index.
     */
    public static String getEditFloatingTaskByIndexCommand(Index index, Entry editedEntry) {
        return EditCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_FLOATINGTASK + " " + index.getOneBased()
               + " "
               + CliSyntax.PREFIX_NAME + " " + getFloatingTaskDetails(editedEntry);
    }

    /**
     * Returns an edit command string for editing an event by keyword.
     */
    public static String getEditEventByKeywordCommand(String keywords, Entry editedEntry) {
        return EditCommand.COMMAND_WORD + " " + keywords + " "
               + CliSyntax.PREFIX_NAME + " " + getEventDetails(editedEntry);
    }

    /**
     * Returns an edit command string for editing a deadline by keyword.
     */
    public static String getEditDeadlineByKeywordCommand(String keywords, Entry editedEntry) {
        return EditCommand.COMMAND_WORD + " " + keywords + " "
               + CliSyntax.PREFIX_NAME + " " + getDeadlineDetails(editedEntry);
    }

    /**
     * Returns an edit command string for editing a deadline by keyword.
     */
    public static String getEditFloatingTaskByKeywordCommand(String keywords, Entry editedEntry) {
        return EditCommand.COMMAND_WORD + " " + keywords + " "
               + CliSyntax.PREFIX_NAME + " " + getFloatingTaskDetails(editedEntry);
    }

    /**
     * Returns the part of command string for the given {@code entry}'s details.
     */
    public static String getEventDetails(Entry entry) {
        assert entry instanceof Event;
        StringBuilder builder = new StringBuilder();
        builder.append(entry.getName().toString()).append(" ")
               .append(CliSyntax.PREFIX_FROM).append(" ")
               .append(getDateDetailsAsInputString(entry.getStartDateAndTime())).append(" ")
               .append(CliSyntax.PREFIX_TO).append(" ")
               .append(getDateDetailsAsInputString(entry.getEndDateAndTime())).append(" ");
        builder.append(CliSyntax.PREFIX_TAG).append(" ");
        entry.getTags().stream().forEach(s -> builder.append(s.tagName).append(" "));
        return builder.toString();
    }

    /**
     * Returns the part of command string for the given {@code entry}'s details.
     */
    public static String getDeadlineDetails(Entry entry) {
        assert entry instanceof Deadline;
        StringBuilder builder = new StringBuilder();
        builder.append(entry.getName().toString()).append(" ")
               .append(CliSyntax.PREFIX_BY).append(" ")
               .append(getDateDetailsAsInputString(entry.getEndDateAndTime())).append(" ");
        builder.append(CliSyntax.PREFIX_TAG).append(" ");
        entry.getTags().stream().forEach(s -> builder.append(s.tagName).append(" "));
        return builder.toString();
    }

    /**
     * Returns the part of command string for the given {@code entry}'s details.
     */
    public static String getFloatingTaskDetails(Entry entry) {
        assert entry instanceof FloatingTask;
        StringBuilder builder = new StringBuilder();
        builder.append(entry.getName().toString()).append(" ");
        builder.append(CliSyntax.PREFIX_TAG).append(" ");
        entry.getTags().stream().forEach(s -> builder.append(s.tagName).append(" "));
        return builder.toString();
    }

    /**
     * Returns a string version of the calendar date that is ready for use in input
     */
    public static String getDateDetailsAsInputString(Calendar calendar) {
        assert calendar != null;
        StringBuilder builder = new StringBuilder();
        // Currently prettyTime parses date strings in MM/DD/YY format
        builder.append(calendar.get(Calendar.MONTH) + 1).append("/") // MONTH being 0-based (JAN = 0)
               .append(calendar.get(Calendar.DAY_OF_MONTH)).append("/")
               .append(calendar.get(Calendar.YEAR)).append(" ");
        if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
            builder.append("0").append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
        } else {
            builder.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
        }
        if (calendar.get(Calendar.MINUTE) < 10) {
            builder.append("0").append(calendar.get(Calendar.MINUTE));
        } else {
            builder.append(calendar.get(Calendar.MINUTE));
        }
        return builder.toString();
    }

    // @@author A0126623L
    /**
     * Returns an list command string for listing active entries.
     */
    public static String getListCommand() {
        return ListCommand.COMMAND_WORD;
    }
    // @@ author

    // @@author A0126623L
    /**
     * Returns an list command string for listing archive.
     */
    public static String getListArchiveCommand() {
        return ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_ARCHIVE;
    }
    // @@ author

    // @@author A0126623L
    /**
     * Returns an list command string for listing bin.
     */
    public static String getListBinCommand() {
        return ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN;
    }
    // @@ author

    // @@author A0126623L
    /**
     * Returns an list command string for listing all entries.
     */
    public static String getListAllCommand() {
        return ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_ALL;
    }
    // @@ author

    // @@author A0126623L
    /**
     * Returns an complete command string for complete by matching an entry's full name.
     */
    public static String getCompleteByFullNameCommand(Entry entry) {
        return CompleteCommand.COMMAND_WORD + " " + entry.getName().toString();
    }
    // @@ author

    // @@author A0126623L
    /**
     * Returns an list command string for listing all entries.
     */
    public static String getCompleteEventByIndexCommand(int index) {
        return CompleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_EVENT + " " + index;
    }
    // @@ author

    // @@author A0126623L
    /**
     * Returns an list command string for listing all entries.
     */
    public static String getCompleteDeadlineByIndexCommand(int index) {
        return CompleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DEADLINE + " " + index;
    }
    // @@ author

    // @@author A0126623L
    /**
     * Returns an list command string for listing all entries.
     */
    public static String getCompleteFloatingTaskByIndexCommand(int index) {
        return CompleteCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_FLOATINGTASK + " " + index;
    }
    // @@ author

}
