package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.entry.Entry;

//@@author A0125586X
/**
 * A utility class for Entry.
 */
public class EntryUtil {

    /**
     * Returns an add command string for adding the {@code entry} as a floating task.
     */
    public static String getFloatingTaskAddCommand(Entry entry) {
        return AddCommand.COMMAND_WORD + " " + getFloatingTaskDetailsForAdd(entry);
    }

    /**
     * Returns the part of command string for the given {@code entry}'s details
     * for adding a floating task.
     */
    private static String getFloatingTaskDetailsForAdd(Entry entry) {
        StringBuilder builder = new StringBuilder();
        builder.append(entry.getName().toString() + " ");
        builder.append(PREFIX_TAG + " ");
        entry.getTags().stream().forEach(
            s -> builder.append(s.tagName + " ")
        );
        return builder.toString();
    }
}
