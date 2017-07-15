package seedu.multitasky.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.testutil.SampleEntries;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());
    }

    @Test
    public void execute_newEntry_success() throws Exception, DuplicateEntryException {
        Entry validEntry = EntryBuilder.build();

        Model expectedModel = new ModelManager(model.getEntryBook(), new UserPrefs());
        expectedModel.addEntry(validEntry);

        CommandResult commandResult = prepareCommand(validEntry, model).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validEntry), commandResult.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds {@code entry} into the {@code model}.
     */
    private AddCommand prepareCommand(Entry entry, Model model) {
        AddCommand command = new AddCommand(entry);
        command.setData(model, new CommandHistory());
        return command;
    }
}
