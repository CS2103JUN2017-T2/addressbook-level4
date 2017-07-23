package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.input.KeyCode;
import seedu.multitasky.logic.commands.ExitCommand;

// @@author A0125586X
public class ExitCommandTest extends EntryBookGuiTest {

    /*******************************
     * Mixed-case and autocomplete *
     ******************************/
    @Test
    public void exit_keyboardShortcut_success() {
        commandBox.pressKey(KeyCode.F4);
        assertTrue(commandBox.getCommandInput().equals(ExitCommand.COMMAND_WORD + " "));
    }

}
