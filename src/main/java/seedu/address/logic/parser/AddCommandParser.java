package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE_ON;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE_AT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE_FROM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE_BY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECUR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECUR_CUSTOMIZE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENTRYBOOK_ARCHIVE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENTRYBOOK_BIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

//@@kevinlamkb A0140633R
/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_START_DATE_ON, PREFIX_START_DATE_AT,
                        PREFIX_START_DATE_FROM, PREFIX_START_DATE_BY, PREFIX_END_DATE,
                        PREFIX_RECUR, PREFIX_RECUR_CUSTOMIZE,PREFIX_ENTRYBOOK_ARCHIVE,
                        PREFIX_ENTRYBOOK_BIN, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap,PREFIX_START_DATE_BY) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        try {
            Name name = ParserUtil.parseName(argMultimap.getPreamble()).get();
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            ReadOnlyEntry entry = new Entry(name, tagList);

            return new AddCommand(entry);
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
