package seedu.multitasky.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_ADDTAG;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_AT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_BY;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_ON;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TO;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.EditCommandHistory;
import seedu.multitasky.logic.commands.EditByFindCommand;
import seedu.multitasky.logic.commands.EditByIndexCommand;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.tag.Tag;

// @@author A0140633R
/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser {
    private ArgumentMultimap argMultimap;

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand and returns an
     * EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args, EditCommandHistory history) throws ParseException {
        requireNonNull(args);
        argMultimap = ArgumentTokenizer.tokenize(args, ParserUtil.toPrefixArray(EditCommand.VALID_PREFIXES));
        EditEntryDescriptor editEntryDescriptor = new EditEntryDescriptor();

        if (args.trim().isEmpty()) { // print help message if command word used without args
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        if (!hasValidFlags()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        if (!history.hasEditHistory()) {
            // initialise edit descriptor
            Prefix startDatePrefix = null;
            Prefix endDatePrefix = null;
            if (hasStartDatePrefix()) {
                startDatePrefix = ParserUtil.getLastPrefix(args, PREFIX_FROM, PREFIX_AT, PREFIX_ON);
                if (argMultimap.getValue(startDatePrefix).get().equals("")) { // indicates reset
                    editEntryDescriptor.setResetStartDate(true);
                    startDatePrefix = null; // prevent parsing of date since it will throw exception there
                }
            }
            if (hasEndDatePrefix()) {
                endDatePrefix = ParserUtil.getLastPrefix(args, PREFIX_BY, PREFIX_TO);
                if (argMultimap.getValue(endDatePrefix).get().equals("")) { // indicates reset
                    editEntryDescriptor.setResetEndDate(true);
                    endDatePrefix = null; // prevent parsing of date since it will throw exception there
                }
            }
            initEntryEditor(argMultimap, editEntryDescriptor, startDatePrefix, endDatePrefix);
        } else { // load up editEntryDescriptor from previous try
            editEntryDescriptor = history.getEditHistory();
            history.resetEditHistory();
        }

        if (hasIndexFlag(argMultimap)) { // edit by index
            try {
                Prefix listIndicatorPrefix = ParserUtil.getMainPrefix(argMultimap, PREFIX_FLOATINGTASK,
                                                                      PREFIX_DEADLINE, PREFIX_EVENT);
                Index index = ParserUtil.parseIndex(argMultimap.getValue(listIndicatorPrefix).get());
                return new EditByIndexCommand(index, listIndicatorPrefix, editEntryDescriptor);
            } catch (IllegalValueException ive) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                       EditCommand.MESSAGE_USAGE));
            }

        } else { // search by find

            String searchString = argMultimap.getPreamble().get()
                                             .replaceAll("\\" + CliSyntax.PREFIX_ESCAPE, "");
            final String[] keywords = searchString.split("\\s+");
            final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));

            return new EditByFindCommand(keywordSet, editEntryDescriptor);
        }
    }

    /*
     * Intializes the entry editor by parsing new values that will replace old data. throws ParseException if
     * entry
     * data are of wrong format or no fields are edited.
     */
    private void initEntryEditor(ArgumentMultimap argMultimap, EditEntryDescriptor editEntryDescriptor,
                                 Prefix startDatePrefix, Prefix endDatePrefix) throws ParseException {
        try {
            ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME))
                      .ifPresent(editEntryDescriptor::setName);
            if (startDatePrefix != null) {
                ParserUtil.parseDate(argMultimap.getValue(startDatePrefix))
                          .ifPresent(editEntryDescriptor::setStartDate);
            }
            if (endDatePrefix != null) {
                ParserUtil.parseDate(argMultimap.getValue(endDatePrefix))
                          .ifPresent(editEntryDescriptor::setEndDate);
            }
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG))
                      .ifPresent(editEntryDescriptor::setTags);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_ADDTAG))
                      .ifPresent(editEntryDescriptor::setAddTags);
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

    /**
     * A method that returns true if flags in given ArgumentMultimap has at least one index-indicating
     * Prefix mapped to some arguments.
     * Index-indicating := /float or /deadline or /event
     */
    private boolean hasIndexFlag(ArgumentMultimap argMultimap) {
        assert argMultimap != null;
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_DEADLINE,
                                             PREFIX_EVENT);
    }

    /**
     * Returns true if flags present in argMultimap indicate start date arguments.
     * Precondition : argumentMultimap within parser has been properly initialised beforehand.
     */
    private boolean hasStartDatePrefix() {
        assert argMultimap != null;
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FROM, PREFIX_AT, PREFIX_ON);
    }

    /**
     * Returns true if flags present in argMultimap indicate end date arguments.
     * Precondition : argumentMultimap within parser has been properly initialised beforehand.
     */
    private boolean hasEndDatePrefix() {
        assert argMultimap != null;
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_BY, PREFIX_TO);
    }

    /**
     * returns false if flags present in argMultimap indicate invalid flags are present
     * invalid for edit: cannot use tag and addtag at the same time, cannot use more than 2 of any
     * float, deadline, event tags.
     */
    private boolean hasValidFlags() {
        assert argMultimap != null;
        return !(ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_TAG, PREFIX_ADDTAG)
                 || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_DEADLINE)
                 || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_DEADLINE, PREFIX_EVENT)
                 || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_EVENT));
    }

}
