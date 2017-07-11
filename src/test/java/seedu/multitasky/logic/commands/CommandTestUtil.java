package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel)
            throws CommandException, DuplicateEntryException {
        CommandResult result = command.execute();

        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, actualModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the entry book and the filtered entry list in the {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage)
            throws DuplicateEntryException {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        EntryBook expectedEntryBook = new EntryBook(actualModel.getEntryBook());
        List<ReadOnlyEntry> expectedFilteredList = new ArrayList<>(actualModel.getFilteredFloatingTaskList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedEntryBook, actualModel.getEntryBook());
            assertEquals(expectedFilteredList, actualModel.getFilteredFloatingTaskList());
        }
    }
}
