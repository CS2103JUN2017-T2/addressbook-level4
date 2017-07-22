package seedu.multitasky.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.multitasky.commons.core.Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;
import static seedu.multitasky.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.multitasky.model.util.TagSetBuilder.getTagSet;
import static seedu.multitasky.testutil.SampleEntries.INDEX_THIRD_ENTRY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.eventbus.Subscribe;

import seedu.multitasky.commons.core.EventsCenter;
import seedu.multitasky.commons.events.model.EntryBookChangedEvent;
import seedu.multitasky.commons.events.ui.ShowHelpRequestEvent;
import seedu.multitasky.commons.util.PowerMatch;
import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.logic.commands.Command;
import seedu.multitasky.logic.commands.CommandResult;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.commands.ExitCommand;
import seedu.multitasky.logic.commands.FindCommand;
import seedu.multitasky.logic.commands.HelpCommand;
import seedu.multitasky.logic.commands.HistoryCommand;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.model.util.EntryBuilder;

public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    // These are for checking the correctness of the events raised
    private ReadOnlyEntryBook latestSavedEntryBook;
    private boolean helpShown;

    @Subscribe
    private void handleLocalModelChangedEvent(EntryBookChangedEvent abce) {
        latestSavedEntryBook = new EntryBook(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Before
    public void setUp() {
        model = new ModelManager();
        logic = new LogicManager(model, new UserPrefs());
        EventsCenter.getInstance().registerHandler(this);

        latestSavedEntryBook = new EntryBook(model.getEntryBook()); // last saved assumed to be up to date
        helpShown = false;
    }

    @After
    public void tearDown() {
        EventsCenter.clearSubscribers();
    }

    /**
     * Executes the command, confirms that no exceptions are thrown and that the result message is correct.
     * Also confirms that {@code expectedModel} is as specified.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage, Model expectedModel) {
        assertCommandBehavior(null, inputCommand, expectedMessage, expectedModel);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is
     * correct.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     *
     * @see #assertCommandBehavior(Class, String, String, Model)
     */
    private <T> void assertCommandFailure(String inputCommand, Class<T> expectedException,
                                          String expectedMessage) {
        Model expectedModel = new ModelManager(model.getEntryBook(), new UserPrefs());
        assertCommandBehavior(expectedException, inputCommand, expectedMessage, expectedModel);
    }

    /**
     * Executes the command, confirms that the result message is correct and that the expected exception is
     * thrown,
     * and also confirms that the following two parts of the LogicManager object's state are as expected:<br>
     * - the internal model manager data are same as those in the {@code expectedModel} <br>
     * - {@code expectedModel}'s entry book was saved to the storage file.
     */
    private <T> void assertCommandBehavior(Class<T> expectedException, String inputCommand,
                                           String expectedMessage, Model expectedModel) {

        try {
            CommandResult result = logic.execute(inputCommand);
            assertEquals(expectedException, null);
            assertEquals(expectedMessage, result.feedbackToUser);
        } catch (CommandException | ParseException | DuplicateEntryException e) {
            assertEquals(expectedException, e.getClass());
            assertEquals(expectedMessage, e.getMessage());
        }

        assertEquals(expectedModel, model);
        assertEquals(expectedModel.getEntryBook(), latestSavedEntryBook);
    }

    @Test
    public void execute_invalidCommand_parseException() {
        String invalidCommand = "       ";
        assertParseException(invalidCommand,
                             String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    @Test
    public void execute_unknownCommandWord_parseException() {
        String unknownCommand = "uicfhmowqewca";
        assertParseException(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_help_success() {
        assertCommandSuccess(HelpCommand.COMMAND_WORD, HelpCommand.SHOWING_HELP_MESSAGE, new ModelManager());
        assertTrue(helpShown);
    }

    @Test
    public void execute_exit_success() {
        assertCommandSuccess(ExitCommand.COMMAND_WORD, ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT,
                             new ModelManager());
    }

    @Test
    public void execute_clear_success() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addEntry(helper.generateEntry(1));
        model.addEntry(helper.generateEntry(2));
        model.addEntry(helper.generateEntry(3));
        assertCommandSuccess(ClearCommand.COMMAND_WORD + " all",
                             ClearCommand.MESSAGE_ALL_SUCCESS, new ModelManager());
    }

    @Test
    public void execute_addInvalidArgsFormat_parseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        // add command without args
        assertParseException(AddCommand.COMMAND_WORD, expectedMessage);
    }

    @Test
    public void execute_addInvalidEntryData_parseException() {
        // add entry without name
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertParseException(AddCommand.COMMAND_WORD + " " + PREFIX_TAG + " tagging without name",
                             expectedMessage);
    }

    @Test
    public void execute_addValid_success() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Entry toBeAdded = helper.adam();
        Model expectedModel = new ModelManager();
        expectedModel.addEntry(toBeAdded);

        // execute command and verify result
        assertCommandSuccess(helper.generateAddCommand(toBeAdded),
                             String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded), expectedModel);

    }

    @Test
    public void execute_listShowsAllEntries_success() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Model expectedModel = new ModelManager(helper.generateEntryBook(2), new UserPrefs());

        // prepare entry book state
        helper.addToModel(model, 2);

        assertCommandSuccess(ListCommand.COMMAND_WORD, ListCommand.MESSAGE_ACTIVE_SUCCESS, expectedModel);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single entry in the shown list, using visible index.
     *
     * @param commandWord to test assuming it targets a single entry in the last shown list
     *        based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Entry> entryList = helper.generateEntryList(2);

        // set AB state to 2 entries
        model.resetData(new EntryBook());
        for (Entry p : entryList) {
            model.addEntry(p);
        }

        assertCommandException(commandWord + " " + PREFIX_FLOATINGTASK + " "
                               + INDEX_THIRD_ENTRY.getOneBased(), expectedMessage);
    }

    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertParseException(DeleteCommand.COMMAND_WORD, expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand(DeleteCommand.COMMAND_WORD);
    }

    @Test
    public void execute_delete_removesCorrectEntry() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Entry> threeEntrys = helper.generateEntryList(3);

        Model expectedModel = new ModelManager(helper.generateEntryBook(threeEntrys), new UserPrefs());
        expectedModel.changeEntryState(threeEntrys.get(1), Entry.State.DELETED);
        expectedModel.updateAllFilteredListToShowAllActiveEntries();
        helper.addToModel(model, threeEntrys);

        assertCommandSuccess(DeleteCommand.COMMAND_WORD + " float 2",
                             String.format(DeleteCommand.MESSAGE_SUCCESS, threeEntrys.get(1)), expectedModel);
    }

    @Test
    public void execute_find_invalidArgsFormat() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertParseException(FindCommand.COMMAND_WORD + " ", expectedMessage);
    }

    @Test
    public void execute_find_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Entry p1 = EntryBuilder.build("bla bla KEY bla");
        Entry p2 = EntryBuilder.build("bla KEY bla bceofeia");
        Entry p3 = EntryBuilder.build("key key");
        Entry p4 = EntryBuilder.build("KEy sduauo");

        List<Entry> fourEntrys = helper.generateEntryList(p3, p1, p4, p2);
        Model expectedModel = new ModelManager(helper.generateEntryBook(fourEntrys), new UserPrefs());
        helper.addToModel(model, fourEntrys);

        assertCommandSuccess(FindCommand.COMMAND_WORD + " KEY",
                             Command.getMessageForEntryListShownSummary(expectedModel.getFilteredFloatingTaskList()
                                                                                     .size()),
                             expectedModel);
    }

    @Test
    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Entry pTarget1 = EntryBuilder.build("bla bla KEY bla");
        Entry pTarget2 = EntryBuilder.build("bla rAnDoM bla bceofeia");
        Entry pTarget3 = EntryBuilder.build("key key");
        Entry p1 = EntryBuilder.build("sduauo");

        List<Entry> fourEntrys = helper.generateEntryList(pTarget1, p1, pTarget2, pTarget3);
        Model expectedModel = new ModelManager(helper.generateEntryBook(fourEntrys), new UserPrefs());
        expectedModel.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList("key", "rAnDoM")),
                                                     null, null,
                                                     Entry.State.ACTIVE, Model.Search.OR, PowerMatch.Level.LEVEL_0);
        helper.addToModel(model, fourEntrys);

        assertCommandSuccess(FindCommand.COMMAND_WORD + " key rAnDoM",
                             Command.getMessageForEntryListShownSummary(expectedModel.getFilteredFloatingTaskList()
                                                                                     .size()),
                             expectedModel);
    }

    @Test
    public void execute_verifyHistory_success() throws Exception {
        String validCommand = "clear";
        logic.execute(validCommand);

        String invalidCommandParse = "   adds   Bob   ";
        try {
            logic.execute(invalidCommandParse);
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }

        String invalidCommandExecute = "delete float 1"; // entry book is of size 0; index out of bounds
        try {
            logic.execute(invalidCommandExecute);
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX, ce.getMessage());
        }

        String expectedMessage = String.format(HistoryCommand.MESSAGE_SUCCESS,
                                               String.join("\n", invalidCommandExecute, invalidCommandParse,
                                                           validCommand));
        assertCommandSuccess("history", expectedMessage, model);
    }

    /**
     * A utility class to generate test data.
     */
    class TestDataHelper {

        Entry adam() throws Exception {
            Name name = new Name("dinner with Adam Brown");

            return new FloatingTask(name, getTagSet("tag1", "longertag2"));
        }

        /**
         * Generates a valid entry using the given seed.
         * Running this function with the same parameter values guarantees the returned entry will have the
         * same state.
         * Each unique seed will generate a unique Entry object.
         *
         * @param seed used to generate the entry data field values
         */
        Entry generateEntry(int seed) throws Exception {
            return new FloatingTask(
                                    new Name("Entry " + seed),
                                    getTagSet("tag" + Math.abs(seed), "tag" + Math.abs(seed + 1)));
        }

        /** Generates the correct add command based on the entry given */
        String generateAddCommand(Entry p) {
            StringBuffer cmd = new StringBuffer();

            cmd.append(AddCommand.COMMAND_WORD);

            cmd.append(" ").append(p.getName());

            Set<Tag> tags = p.getTags();
            for (Tag t : tags) {
                cmd.append(" " + PREFIX_TAG.getPrefix()).append(" ").append(t.tagName);
            }

            return cmd.toString();
        }

        /**
         * Generates an EntryBook with auto-generated entries.
         */
        EntryBook generateEntryBook(int numGenerated) throws Exception {
            EntryBook entryBook = new EntryBook();
            addToEntryBook(entryBook, numGenerated);
            return entryBook;
        }

        /**
         * Generates an EntryBook based on the list of Entrys given.
         */
        EntryBook generateEntryBook(List<Entry> entries) throws Exception {
            EntryBook entryBook = new EntryBook();
            addToEntryBook(entryBook, entries);
            return entryBook;
        }

        /**
         * Adds auto-generated Entry objects to the given EntryBook
         *
         * @param entryBook The EntryBook to which the Entrys will be added
         */
        void addToEntryBook(EntryBook entryBook, int numGenerated) throws Exception {
            addToEntryBook(entryBook, generateEntryList(numGenerated));
        }

        /**
         * Adds the given list of Entrys to the given EntryBook
         */
        void addToEntryBook(EntryBook entryBook, List<Entry> entriesToAdd) throws Exception {
            for (Entry p : entriesToAdd) {
                entryBook.addEntry(p);
            }
        }

        /**
         * Adds auto-generated Entry objects to the given model
         *
         * @param model The model to which the Entrys will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception {
            addToModel(model, generateEntryList(numGenerated));
        }

        /**
         * Adds the given list of Entrys to the given model
         */
        void addToModel(Model model, List<Entry> entriesToAdd) throws Exception {
            for (Entry p : entriesToAdd) {
                model.addEntry(p);
            }
        }

        /**
         * Generates a list of Entrys based on the flags.
         */
        List<Entry> generateEntryList(int numGenerated) throws Exception {
            List<Entry> entries = new ArrayList<>();
            for (int i = 1; i <= numGenerated; i++) {
                entries.add(generateEntry(i));
            }
            return entries;
        }

        List<Entry> generateEntryList(Entry... entries) {
            return Arrays.asList(entries);
        }
    }
}
