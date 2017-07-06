package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteByFindCommand;
import seedu.address.logic.commands.DeleteByIndexCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand and returns an
     * DeleteCommand object for execution.
     *
     * @throws ParseException
     *             if the user input does not conform the expected format
     */
    // @@author A0140633R
    public Command parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FLOATINGTASK);

        if (ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FLOATINGTASK)) {
            try {
                Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_FLOATINGTASK).get());
                return new DeleteByIndexCommand(index);
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }
        } else {
            String trimmedArgs = argMultimap.getPreamble().get();
            if (trimmedArgs.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
            final String[] keywords = trimmedArgs.split("\\s+");
            final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));

            return new DeleteByFindCommand(keywordSet);
        }
    }

}
