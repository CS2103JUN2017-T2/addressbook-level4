package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.ui.CommandBox;

// @@author A0125586X
public class CommandBoxTest extends EntryBookGuiTest {

    private static final String COMMAND_THAT_SUCCEEDS = ListCommand.COMMAND_WORD;
    private static final String COMMAND_THAT_FAILS = "invalid command";

    private static final String[] DUMMY_COMMANDS = {
        "dummy command 1",
        "dummy command 2"
    };

    private ArrayList<String> defaultStyleOfCommandBox;
    private ArrayList<String> errorStyleOfCommandBox;

    @Before
    public void setUp() {
        defaultStyleOfCommandBox = new ArrayList<>(commandBox.getStyleClass());
        assertFalse("CommandBox default style classes should not contain error style class.",
                    defaultStyleOfCommandBox.contains(CommandBox.ERROR_STYLE_CLASS));

        // build style class for error
        errorStyleOfCommandBox = new ArrayList<>(defaultStyleOfCommandBox);
        errorStyleOfCommandBox.add(CommandBox.ERROR_STYLE_CLASS);
    }

    @Test
    public void commandBox_successfulThenFailedCommand_errorStyle() {
        // Reset style
        assertBehaviorForSuccessfulCommand();
        // Test for error style with failed command
        assertBehaviorForFailedCommand();
    }

    @Test
    public void commandBox_failedThenSuccessfulCommand_noErrorStyle() {
        // Error style
        assertBehaviorForFailedCommand();
        // Test for no error style with successful command
        assertBehaviorForSuccessfulCommand();
    }

    @Test
    public void commandBox_mixFailedSuccessfulCommand_correctStyle() {
        assertBehaviorForSuccessfulCommand();
        assertBehaviorForFailedCommand();
        assertBehaviorForSuccessfulCommand();
        assertBehaviorForFailedCommand();
        assertBehaviorForSuccessfulCommand();
    }

    /**
     * Using dummy command names as this test is only concerned with the
     * proper retrieval of previously entered commands.
     */
    @Test
    public void commandBox_upKey_retrievePreviousCommand() {
        commandBox.runCommand(DUMMY_COMMANDS[0]);
        commandBox.pressUpKey();
        assertCommandBox(DUMMY_COMMANDS[0]);
    }

    @Test
    public void commandBox_downKey_retrievePreviousCommand() {
        commandBox.runCommand(DUMMY_COMMANDS[0]);
        commandBox.runCommand(DUMMY_COMMANDS[1]);
        commandBox.pressUpKey();
        commandBox.pressUpKey();
        commandBox.pressDownKey();
        assertCommandBox(DUMMY_COMMANDS[1]);
    }

    @Test
    public void commandbox_downKey_retrievedTypedCommand() {
        commandBox.runCommand(DUMMY_COMMANDS[0]);
        commandBox.enterCommand(DUMMY_COMMANDS[1]);
        commandBox.pressUpKey();
        commandBox.pressDownKey();
        assertCommandBox(DUMMY_COMMANDS[1]);
    }

    /**
     * Runs a command that fails, then verifies that
     * - the return value of runCommand(...) is false,
     * - the text is cleared,
     * - the command box has only one ERROR_STYLE_CLASS, with other style classes untouched.
     */
    private void assertBehaviorForFailedCommand() {
        assertFalse(commandBox.runCommand(COMMAND_THAT_FAILS));
        assertCommandBox("");
        assertEquals(errorStyleOfCommandBox, commandBox.getStyleClass());
    }

    /**
     * Runs a command that succeeds, then verifies that
     * - the return value of runCommand(...) is true,
     * - the text is cleared,
     * - the command box does not have any ERROR_STYLE_CLASS, with style classes the same as default.
     */
    private void assertBehaviorForSuccessfulCommand() {
        assertTrue(commandBox.runCommand(COMMAND_THAT_SUCCEEDS));
        assertCommandBox("");
        assertEquals(defaultStyleOfCommandBox, commandBox.getStyleClass());
    }

}
