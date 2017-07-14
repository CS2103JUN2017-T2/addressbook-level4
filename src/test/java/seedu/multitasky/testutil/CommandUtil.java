package seedu.multitasky.testutil;

import java.util.Calendar;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;

// @@author A0125586X
/**
 * A utility class for commands, to get the correct command phrase for testing.
 */
public class CommandUtil {

    /**
     * Returns an edit command string for editing an event by index.
     */
    public static String getEditEventByIndexCommand(Index index, Entry editedEntry) {
        return EditCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_EVENT + " " + index.getOneBased() + " "
               + CliSyntax.PREFIX_NAME + " " + getEventDetails(editedEntry);
    }

    /**
     * Returns an edit command string for editing an event by keyword.
     */
    public static String getEditEventByKeywordCommand(String keywords, Entry editedEntry) {
        return EditCommand.COMMAND_WORD + " " + keywords + " "
               + CliSyntax.PREFIX_NAME + " " + getEventDetails(editedEntry);
    }

    /**
     * Returns the part of command string for the given {@code entry}'s details.
     */
    public static String getEventDetails(Entry entry) {
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
     * Returns a string version of the calendar date that is ready for use in input
     */
    public static String getDateDetailsAsInputString(Calendar calendar) {
        assert calendar != null;
        StringBuilder builder = new StringBuilder();
        // Currently prettyTime parses date strings in MM/DD/YY format
        builder.append(calendar.get(Calendar.MONTH) + 1).append("/") // +1 due to MONTH being 0-based (JAN = 0)
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

}
