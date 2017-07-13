package seedu.multitasky.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_NAME_AMY;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_NAME_BOB;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_TAG_FRIEND;
import static seedu.multitasky.testutil.EditCommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.multitasky.testutil.SampleEntries.INDEX_FIRST_ENTRY;
import static seedu.multitasky.testutil.SampleEntries.INDEX_SECOND_ENTRY;
import static seedu.multitasky.testutil.SampleEntries.INDEX_THIRD_ENTRY;

import org.junit.Test;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.Command;
import seedu.multitasky.logic.commands.EditByIndexCommand;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.testutil.EditEntryDescriptorBuilder;

// TODO implement edit by find portions
public class EditCommandParserTest {

    private static final String PREFIX_FLOAT = " " + PREFIX_FLOATINGTASK.getPrefix() + " ";
    private static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    private static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    private static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    private static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;
    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String INVALID_NAME_DESC = " " + PREFIX_NAME + ""; // empty names not allowed
    private static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    private static final String MESSAGE_INVALID_FORMAT =
                                                       String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                                     EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no field specified
        assertParseFailure(PREFIX_FLOAT + "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure("", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(PREFIX_FLOAT + "1" + INVALID_NAME_DESC,
                           Name.MESSAGE_NAME_CONSTRAINTS); // invalid name

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Entry} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(PREFIX_FLOAT + "1" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY,
                           Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(PREFIX_FLOAT + "1" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND,
                           Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(PREFIX_FLOAT + "1" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND,
                           Tag.MESSAGE_TAG_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(PREFIX_FLOAT + "1" + INVALID_NAME_DESC + INVALID_TAG_DESC,
                           Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() throws Exception {
        Index targetIndex = INDEX_SECOND_ENTRY;
        String userInput = PREFIX_FLOAT + targetIndex.getOneBased() + TAG_DESC_HUSBAND
                           + NAME_DESC_AMY + TAG_DESC_FRIEND;

        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_AMY)
                                                                         .withTags(VALID_TAG_HUSBAND,
                                                                                   VALID_TAG_FRIEND)
                                                                         .build();
        EditCommand expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() throws Exception {
        Index targetIndex = INDEX_FIRST_ENTRY;
        String userInput = PREFIX_FLOAT + targetIndex.getOneBased() + NAME_DESC_BOB + TAG_DESC_HUSBAND;

        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_BOB)
                                                                         .withTags(VALID_TAG_HUSBAND).build();
        EditCommand expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() throws Exception {
        // name
        Index targetIndex = INDEX_THIRD_ENTRY;
        String userInput = PREFIX_FLOAT + targetIndex.getOneBased() + NAME_DESC_AMY;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // tags
        userInput = PREFIX_FLOAT + targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditEntryDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);
        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() throws Exception {
        Index targetIndex = INDEX_FIRST_ENTRY;
        String userInput = PREFIX_FLOAT + targetIndex.getOneBased() + TAG_DESC_FRIEND
                           + TAG_DESC_FRIEND + TAG_DESC_HUSBAND;

        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withTags(VALID_TAG_FRIEND,
                                                                                   VALID_TAG_HUSBAND)
                                                                         .build();
        EditCommand expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);

        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() throws Exception {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_ENTRY;
        String userInput = PREFIX_FLOAT + targetIndex.getOneBased() + INVALID_NAME_DESC + NAME_DESC_BOB;
        EditEntryDescriptor descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);
        assertParseSuccess(userInput, expectedCommand);

        // other valid values specified
        userInput = PREFIX_FLOAT + targetIndex.getOneBased() + INVALID_NAME_DESC
                    + TAG_DESC_HUSBAND + NAME_DESC_BOB;
        descriptor = new EditEntryDescriptorBuilder().withName(VALID_NAME_BOB)
                                                     .withTags(VALID_TAG_HUSBAND).build();
        expectedCommand = new EditByIndexCommand(targetIndex, PREFIX_FLOATINGTASK, descriptor);
        assertParseSuccess(userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() throws Exception {
        Index targetIndex = INDEX_THIRD_ENTRY;
        String userInput = PREFIX_FLOAT + targetIndex.getOneBased() + TAG_EMPTY;

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
