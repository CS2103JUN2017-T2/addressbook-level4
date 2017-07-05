package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENTRYBOOK_FLOATINGTASK;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@kevinlamkb A0140633R
/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * DeleteCommand and returns an DeleteCommand object for execution.
     *
     * @throws ParseException
     * if the user input does not conform the expected format
     */
    public Command parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ENTRYBOOK_FLOATINGTASK);

        if (!arePrefixesPresent(argMultimap, PREFIX_ENTRYBOOK_FLOATINGTASK)) {
            String trimmedArgs = argMultimap.getPreamble().get();
            if (trimmedArgs.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
            final String[] keywords = trimmedArgs.split("\\s+");
            final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
            Index index = Index.fromZeroBased(0);

            return new DeleteCommand(index, keywordSet);
        }

        try {
            final Set<String> keywordSet = new HashSet<>();
            Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_ENTRYBOOK_FLOATINGTASK).get());
            System.out.println(argMultimap.getValue(PREFIX_ENTRYBOOK_FLOATINGTASK).get());
            return new DeleteCommand(index, keywordSet);

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional}
     * values in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
