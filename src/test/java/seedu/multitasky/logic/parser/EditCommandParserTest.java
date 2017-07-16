package seedu.multitasky.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_BY;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_EVENT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.multitasky.logic.parser.ParserUtil.MESSAGE_FAIL_PARSE_DATE;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_DATE_11_JULY_17;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_DATE_12_JULY_17;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_DATE_20_DEC_17;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_NAME_CLEAN;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_NAME_MEETING;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_TAG_FRIEND;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_TAG_URGENT;
import static seedu.multitasky.testutil.SampleEntries.INDEX_FIRST_ENTRY;
import static seedu.multitasky.testutil.SampleEntries.INDEX_SECOND_ENTRY;
import static seedu.multitasky.testutil.SampleEntries.INDEX_THIRD_ENTRY;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.Command;
import seedu.multitasky.logic.commands.EditByFindCommand;
import seedu.multitasky.logic.commands.EditByIndexCommand;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.testutil.EditEntryDescriptorBuilder;

public class EditCommandParserTest {

    private static final String PREFIX_DESC_DEADLINE = " " + PREFIX_DEADLINE + " ";
    private static final String PREFIX_DESC_EVENT = " " + PREFIX_EVENT + " ";
    private static final String PREFIX_DESC_FLOAT = " " + PREFIX_FLOATINGTASK + " ";
    private static final String NAME_DESC_CLEAN = " " + PREFIX_NAME + " " + VALID_NAME_CLEAN;
    private static final String NAME_DESC_MEETING = " " + PREFIX_NAME + " " + VALID_NAME_MEETING;
    private static final String DATE_DESC_START_12JULY = " " + PREFIX_FROM + " " + VALID_DATE_12_JULY_17;
    private static final String DATE_DESC_END_11JULY = " " + PREFIX_BY + " " + VALID_DATE_11_JULY_17;
    private static final String DATE_DESC_END_20DEC = " " + PREFIX_BY + " " + VALID_DATE_20_DEC_17;
    private static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + " " + VALID_TAG_FRIEND;
    private static final String TAG_DESC_URGENT = " " + PREFIX_TAG + " " + VALID_TAG_URGENT;
    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String INVALID_NAME_DESC = " " + PREFIX_NAME + ""; // empty names not allowed
    private static final String INVALID_TAG_DESC = " " + PREFIX_TAG + " >hubby*";
    private static final String INVALID_DATE_START_DESC = " " + PREFIX_FROM + " start of time";
    private static final String INVALID_DATE_END_DESC = " " + PREFIX_BY + " end of time";

    private static final String MESSAGE_INVALID_FORMAT = String.format(
            MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // by index but no field specified
        assertParseFailure(PREFIX_DESC_FLOAT + " 1", EditCommand.MESSAGE_NOT_EDITED);

        // edit by keywords but no field specified
        assertParseFailure("try keyword search", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure("", MESSAGE_INVALID_FORMAT);
    }

    // @@author A0140633R
    @Test
    public void parse_invalidValue_failure() {
        // what index specified does not matter in these tests.
        assertParseFailure(PREFIX_DESC_FLOAT + " 1" + INVALID_NAME_DESC,
                           Name.MESSAGE_NAME_CONSTRAINTS); // invalid name

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Entry} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(PREFIX_DESC_FLOAT + " 1" + TAG_DESC_FRIEND + TAG_DESC_URGENT + TAG_EMPTY,
                           Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(PREFIX_DESC_FLOAT + " 1" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_URGENT,
                           Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(PREFIX_DESC_FLOAT + " 1" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_URGENT,
                           Tag.MESSAGE_TAG_CONSTRAINTS);

        // does not allow multiple list indicating index flags to be parsed
        assertParseFailure(PREFIX_DESC_FLOAT + " 1" + PREFIX_DESC_EVENT + " 1", MESSAGE_INVALID_FORMAT);

        // does not allow parsing of terms prettyTime lib does not recognize.
        assertParseFailure(PREFIX_DESC_FLOAT + " 1" + INVALID_DATE_END_DESC,
                           String.format(MESSAGE_FAIL_PARSE_DATE, "end of time"));
        // catches all start date invalid values before end date ones
        assertParseFailure(PREFIX_DESC_FLOAT + " 1" + INVALID_DATE_START_DESC + INVALID_DATE_END_DESC,
                           String.format(MESSAGE_FAIL_PARSE_DATE, "start of time"));
        assertParseFailure(PREFIX_DESC_FLOAT + " 1" + INVALID_DATE_END_DESC + INVALID_DATE_START_DESC,
                           String.format(MESSAGE_FAIL_PARSE_DATE, "start of time"));

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(PREFIX_DESC_FLOAT + " 1" + INVALID_NAME_DESC + INVALID_TAG_DESC,
                           Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_byIndexAllFieldsSpecified_success() throws Exception {
        Index targetIndex = INDEX_SECOND_ENTRY;
        String userInput = PREFIX_DESC_EVENT + targetIndex.getOneBased()
                + DATE_DESC_START_12JULY + DATE_DESC_END_20DEC + TAG_DESC_URGENT
                + NAME_DESC_CLEAN + TAG_DESC_FRIEND;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_CLEAN)
                .withTags(VALID_TAG_URGENT, VALID_TAG_FRIEND).withStartDate(VALID_DATE_12_JULY_17)
                .withEndDate(VALID_DATE_20_DEC_17).build();
        EditCommand expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_EVENT, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_byFindAllFieldsSpecified_success() throws Exception {
        String searchString = "typical entryname";
        String[] keywords = searchString.split("\\s+");
        Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        String userInput = searchString + DATE_DESC_START_12JULY + DATE_DESC_END_20DEC + TAG_DESC_URGENT
                + NAME_DESC_CLEAN + TAG_DESC_FRIEND;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_CLEAN)
                .withTags(VALID_TAG_URGENT, VALID_TAG_FRIEND).withStartDate(VALID_DATE_12_JULY_17)
                .withEndDate(VALID_DATE_20_DEC_17).build();
        EditCommand expectedCommand = new EditByFindCommand(keywordSet, descriptor);

        assertParseSuccess(userInput, expectedCommand);

        // search with escape words
        searchString = "typical entry \\name \\by \\at \\on";
        keywords = searchString.split("\\s+");
        keywordSet = new HashSet<>(Arrays.asList(keywords));
        userInput = searchString + DATE_DESC_START_12JULY + DATE_DESC_END_20DEC + TAG_DESC_URGENT
                + NAME_DESC_CLEAN + TAG_DESC_FRIEND;
        // descriptor unchanged
        expectedCommand = new EditByFindCommand(keywordSet, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }
    // @@author

    @Test
    public void parse_byIndexSomeFieldsSpecified_success() throws Exception {
        Index targetIndex = INDEX_FIRST_ENTRY;
        String userInput = PREFIX_DESC_FLOAT + targetIndex.getOneBased() + NAME_DESC_MEETING + TAG_DESC_URGENT;

        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING)
                                                                         .withTags(VALID_TAG_URGENT).build();
        EditCommand expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }

    // @@author A0140633R
    @Test
    public void parse_byIndexOneFieldSpecified_success() throws Exception {
        // name
        Index targetIndex = INDEX_THIRD_ENTRY;
        String userInput = PREFIX_DESC_FLOAT + targetIndex.getOneBased() + NAME_DESC_CLEAN;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_CLEAN).build();
        EditCommand expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // tags
        userInput = PREFIX_DESC_FLOAT + targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditEntryDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // start date
        userInput = PREFIX_DESC_EVENT + targetIndex.getOneBased() + DATE_DESC_START_12JULY;
        descriptor = new EditEntryDescriptorBuilder().withStartDate(VALID_DATE_12_JULY_17).build();
        expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_EVENT, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // end date
        userInput = PREFIX_DESC_DEADLINE + targetIndex.getOneBased() + DATE_DESC_END_11JULY;
        descriptor = new EditEntryDescriptorBuilder().withEndDate(VALID_DATE_11_JULY_17).build();
        expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_DEADLINE, descriptor);
        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_byFindOneFieldSpecified_success() throws Exception {
        String searchString = "typical entryname";
        String[] keywords = searchString.split("\\s+");
        Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));

        // name
        String userInput = searchString + NAME_DESC_CLEAN;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_CLEAN).build();
        EditCommand expectedCommand = new EditByFindCommand(keywordSet, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // tags
        userInput = searchString + TAG_DESC_URGENT;
        descriptor = new EditEntryDescriptorBuilder().withTags(VALID_TAG_URGENT).build();
        expectedCommand = new EditByFindCommand(keywordSet, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // start date
        userInput = searchString + DATE_DESC_START_12JULY;
        descriptor = new EditEntryDescriptorBuilder().withStartDate(VALID_DATE_12_JULY_17).build();
        expectedCommand = new EditByFindCommand(keywordSet, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // end date
        userInput = searchString + DATE_DESC_END_11JULY;
        descriptor = new EditEntryDescriptorBuilder().withEndDate(VALID_DATE_11_JULY_17).build();
        expectedCommand = new EditByFindCommand(keywordSet, descriptor);
        assertParseSuccess(userInput, expectedCommand);
    }
    // @@author

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() throws Exception {
        Index targetIndex = INDEX_FIRST_ENTRY;
        String userInput = PREFIX_DESC_FLOAT + targetIndex.getOneBased() + TAG_DESC_FRIEND
                           + TAG_DESC_FRIEND + TAG_DESC_URGENT;

        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder()
                .withTags(VALID_TAG_FRIEND, VALID_TAG_URGENT).build();
        EditCommand expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() throws Exception {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_ENTRY;
        String userInput = PREFIX_DESC_FLOAT + targetIndex.getOneBased() + INVALID_NAME_DESC + NAME_DESC_MEETING;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING).build();
        EditCommand expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // other valid values specified
        userInput = PREFIX_DESC_FLOAT + targetIndex.getOneBased() + INVALID_NAME_DESC
                    + TAG_DESC_URGENT + NAME_DESC_MEETING;
        descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING)
                                                     .withTags(VALID_TAG_URGENT).build();
        expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);
        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() throws Exception {
        Index targetIndex = INDEX_THIRD_ENTRY;
        String userInput = PREFIX_DESC_FLOAT + targetIndex.getOneBased() + TAG_EMPTY;

        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }

    /**
     * Asserts the parsing of {@code userInput} is unsuccessful and the error message
     * equals to {@code expectedMessage}
     */
    private void assertParseFailure(String userInput, String expectedMessage) {
        try {
            parser.parse(userInput);
            fail("An exception should have been thrown.");
        } catch (ParseException pe) {
            assertEquals(expectedMessage, pe.getMessage());
        }
    }

    /**
     * Asserts the parsing of {@code userInput} is successful and the result matches {@code expectedCommand}
     */
    private void assertParseSuccess(String userInput, EditCommand expectedCommand) throws Exception {
        Command command = parser.parse(userInput);
        assert expectedCommand.equals(command);
    }
}
