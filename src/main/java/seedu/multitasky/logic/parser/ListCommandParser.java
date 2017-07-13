package seedu.multitasky.logic.parser;

import java.util.ArrayList;
import java.util.Arrays;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;

// @@author A0125586X
/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser {

    /** Parses the given arguments in the context of a list command.
     *
     * @param args the arguments for the list command in a single String
     * @return     the ListCommand object for execution
     * @throws ParseException if the user input does not confirm to the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            return new ListCommand();
        }

        // Flags are delimited by whitespace
        final String[] flags = trimmedArgs.split("\\s+");
        if (!hasValidFlagCombination(flags)) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                                   ListCommand.MESSAGE_USAGE));
        }

        return new ListCommand(flags);
    }

    private boolean hasValidFlagCombination(String... flags) {
        ArrayList<String> flagList = new ArrayList<String>(Arrays.asList(flags));
        // Cannot have any unknown flags
        if (!Arrays.asList(ListCommand.PREFIX_LIST).containsAll(flagList)) {
            return false;
        }
        // Check for invalid flag combinations
        if (flagList.contains(CliSyntax.PREFIX_ARCHIVE.toString())
            && flagList.contains(CliSyntax.PREFIX_BIN.toString())) {
            return false;
        }
        return true;
    }
}
