package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.entry.Entry;

/**
 * A utility class for Entry.
 */
public class EntryUtil {
	//@author A0126623
    /**
     * Returns an add command string for adding the {@code entry}.
     */
    public static String getAddCommand(Entry entry) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(entry);
    }
    
  //@author A0126623
    /**
     * Returns the part of command string for the given {@code entry}'s details.
     */
    private static String getEntryDetails(Entry entry) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + entry.getName().fullName + " ");
        entry.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
