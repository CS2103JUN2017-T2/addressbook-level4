package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.testutil.SampleEntries;

public class ClearCommandTest {

    @Test
    public void execute_emptyEntryBook_success() {
        Model model = new ModelManager();
        assertCommandSuccess(model);
    }

    @Test
    public void execute_nonEmptyEntryBook_success() {
        Model model = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(), new UserPrefs());
        assertCommandSuccess(model);
    }

    /**
     * Executes {@code ClearCommand} on the given {@code model}, confirms that <br>
     * - the result message matches {@code ClearCommand.MESSAGE_ACTIVE_SUCCESS} <br>
     * - the address book and filtered entry list in {@code model} is empty <br>
     */
    private void assertCommandSuccess(Model model) {
        ClearCommand command = new ClearCommand("all");
        command.setData(model, new CommandHistory());
        CommandResult result = command.execute();

        assertEquals(ClearCommand.MESSAGE_ALL_SUCCESS, result.feedbackToUser);
        assertEquals(new ModelManager(), model);
    }
}
