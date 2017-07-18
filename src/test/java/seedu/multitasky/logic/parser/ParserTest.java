package seedu.multitasky.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.multitasky.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import org.junit.Test;

import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.logic.commands.HistoryCommand;
import seedu.multitasky.logic.commands.RedoCommand;
import seedu.multitasky.logic.commands.UndoCommand;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.LogicUserPrefs;
import seedu.multitasky.model.UserPrefs;

public class ParserTest {
    private final Parser parser = new Parser();
    private final LogicUserPrefs userprefs = new UserPrefs();
    private final CommandHistory history = new CommandHistory();

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD, userprefs, history) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3", userprefs,
                                       history) instanceof HistoryCommand);

        try {
            parser.parseCommand("histories", userprefs, history);
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    // @@author A0140633R
    @Test
    public void parseCommand_clear_success() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD, userprefs, history) instanceof ClearCommand);
    }

    @Test
    public void parseCommand_undo_success() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD, userprefs, history) instanceof UndoCommand);
    }

    @Test
    public void parseCommand_redo_success() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD, userprefs, history) instanceof RedoCommand);
    }

}
