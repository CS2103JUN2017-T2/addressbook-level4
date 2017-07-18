package seedu.multitasky.logic.parser;

import java.util.ArrayList;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;

//@@author A0126623L
/**
 * Parses input arguments and creates an appropriate ClearCommand object.
 */
public class ClearCommandParser {

    // @@author A0126623L
    /** Parses the given arguments in the context of a clear command.
     *
     * @param args the arguments for the clear command in a single String
     * @return     the ClearCommand object for execution
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ClearCommand parse(String args) throws ParseException {

        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            return new ClearCommand();
        }

        /*
         * TODO modify ArgumentTokenizer to be able to detect without needing a whitespace in front of
         * trimmedArgs
         */
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(" " + trimmedArgs,
                                                                       toPrefixArray(ClearCommand.VALID_PREFIXES));

        ArrayList<String> prefixesPresent = argumentMultimap.getPresentPrefixes(ClearCommand.VALID_PREFIXES);
        if (!hasValidPrefixNumber(prefixesPresent)) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                                   ClearCommand.MESSAGE_USAGE));
        }

        return new ClearCommand(prefixesPresent.get(0));
    }
    // @@author

    // @@author A0126623L
    private boolean hasValidPrefixNumber(ArrayList<String> prefixes) {
        return (prefixes.size() == ClearCommand.NUMBER_ALLOWABLE_PREFIX);
    }
    // @@author

    // @@author A0126623L-reused
    private Prefix[] toPrefixArray(String... stringPrefixes) {
        Prefix[] prefixes = new Prefix[stringPrefixes.length];
        for (int i = 0; i < stringPrefixes.length; ++i) {
            prefixes[i] = new Prefix(stringPrefixes[i]);
        }
        return prefixes;
    }
    // @@author

}
