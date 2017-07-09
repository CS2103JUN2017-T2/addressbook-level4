package seedu.multitasky.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.multitasky.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.multitasky.testutil.TypicalEntries.INDEX_FIRST_ENTRY;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final Prefix VALID_PREFIX_TAG = new Prefix("/tag");
    private static final Prefix VALID_PREFIX_FLOAT = new Prefix("/float");

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseIndex_invalidInput_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseIndex("10 a");
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(MESSAGE_INVALID_INDEX);
        ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_ENTRY, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_ENTRY, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseName(null);
    }

    @Test
    public void parseName_optionalEmpty_returnsOptionalEmpty() throws Exception {
        assertFalse(ParserUtil.parseName(Optional.empty()).isPresent());
    }

    @Test
    public void parseName_validValue_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        Optional<Name> actualName = ParserUtil.parseName(Optional.of(VALID_NAME));

        assertEquals(expectedName, actualName.get());
    }

    @Test
    public void parseTags_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseTags(null);
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    //@@author A0140633R
    @Test
    public void arePrefixesPresent_emptyArgMap_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ArgumentMultimap argMultimap = null;
        ParserUtil.arePrefixesPresent(argMultimap, VALID_PREFIX_FLOAT, VALID_PREFIX_TAG);
    }

    @Test
    public void arePrefixesPresent_noPrefixFound_returnFalse() {
        String argString = " typical argument string without flags";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argString, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT);
        assertFalse(ParserUtil.arePrefixesPresent(argMultimap, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT));
    }

    @Test
    public void arePrefixesPresent_prefixFound_returnTrue() {
        String argString = " typical argument string with flags /float 1 /tag flagged tagged";
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argString, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT);
        assertTrue(ParserUtil.arePrefixesPresent(argMultimap, VALID_PREFIX_TAG, VALID_PREFIX_FLOAT));
    }

}
