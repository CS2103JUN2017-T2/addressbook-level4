package seedu.multitasky.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.EditByFindCommand;
import seedu.multitasky.logic.commands.EditByIndexCommand;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.tag.Tag;

//@@author A0140633R
/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand and returns an
     * EditCommand object for execution.
     *
     * @throws ParseException
     *             if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FLOATINGTASK, PREFIX_NAME,
                                                                  PREFIX_TAG);
        String trimmedArgs = argMultimap.getPreamble().get();
        EditEntryDescriptor editEntryDescriptor = new EditEntryDescriptor();

        if (ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FLOATINGTASK)) {
            Index index;
            initEntryEditor(argMultimap, editEntryDescriptor);

            try {
                index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_FLOATINGTASK).get());
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
            }
            return new EditByIndexCommand(index, editEntryDescriptor);
        } else {

            if (trimmedArgs.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
            }
            initEntryEditor(argMultimap, editEntryDescriptor);
            final String[] keywords = trimmedArgs.split("\\s+");
            final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));

            return new EditByFindCommand(keywordSet, editEntryDescriptor);
        }
    }

    /*
     * Intializes the entry editor by parsing new values to replace old data. throws ParseException if entry data
     * are of wrong format or no fields are edited.
     */
    private void initEntryEditor(ArgumentMultimap argMultimap, EditEntryDescriptor editEntryDescriptor)
            throws ParseException {
        try {
            ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME))
                    .ifPresent(editEntryDescriptor::setName);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG))
                    .ifPresent(editEntryDescriptor::setTags);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editEntryDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty. If
     * {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
