package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.testutil.TypicalEntries;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;
    private ListCommand listCommand;

    @Before
    public void setUp() {
        model = new ModelManager(new TypicalEntries().getTypicalEntryBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getEntryBook(), new UserPrefs());

        listCommand = new ListCommand();
        listCommand.setData(model, new CommandHistory());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() throws Exception {
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() throws Exception {
        showFirstEntryOnly(model);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    /**
     * Updates the filtered list to show only the first entry in the {@code model}'s address book.
     */
    private void showFirstEntryOnly(Model model) {
        ReadOnlyEntry entry = model.getEntryBook().getFloatingTaskList().get(0);
        final String[] splitName = entry.getName().fullName.split("\\s+");
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)));

        assertTrue(model.getFilteredFloatingTaskList().size() == 1);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the address book and the filtered entry list in the {@code model} matches that of {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model model, String expectedMessage, Model expectedModel)
            throws CommandException, DuplicateEntryException {
        CommandResult result = command.execute();
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }
}
