package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.HelpWindowHandle;
import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.logic.commands.HelpCommand;

//@@author A0125586X
public class HelpWindowTest extends EntryBookGuiTest {

    @Test
    public void helpWindow_usingAcceleratorInCommandBox_open() {
        commandBox.clickOnTextField();
        assertHelpWindowOpen(mainMenu.openHelpWindowUsingAccelerator());
    }

    @Test
    public void helpWindow_usingAcceleratorInResultDisplay_open() {
        resultDisplay.clickOnTextArea();
        assertHelpWindowOpen(mainMenu.openHelpWindowUsingAccelerator());
    }

    @Test
    public void helpWindow_usingAcceleratorInEventListPanel_open() {
        eventListPanel.clickOnListView();
        assertHelpWindowOpen(mainMenu.openHelpWindowUsingAccelerator());
    }

    @Test
    public void helpWindow_usingAcceleratorInDeadlineListPanel_open() {
        deadlineListPanel.clickOnListView();
        assertHelpWindowOpen(mainMenu.openHelpWindowUsingAccelerator());
    }

    @Test
    public void helpWindow_usingAcceleratorInFloatingTaskListPanel_open() {
        floatingTaskListPanel.clickOnListView();
        assertHelpWindowOpen(mainMenu.openHelpWindowUsingAccelerator());
    }

    @Test
    public void helpWindow_usingMenuButton_open() {
        assertHelpWindowOpen(mainMenu.openHelpWindowUsingMenu());
    }

    @Test
    public void helpWindow_usingCommand_open() {
        assertHelpWindowOpen(commandBox.runHelpCommand());
    }

    @Test
    public void help_tabAutocomplete_success() {
        assertHelpTabAutocompleteFailure(HelpCommand.COMMAND_WORD.substring(0, 1));
        for (int i = 2; i < HelpCommand.COMMAND_WORD.length(); ++i) {
            assertHelpTabAutocomplete(HelpCommand.COMMAND_WORD.substring(0, i));
        }
    }

    @Test
    public void help_unknownCommandName_errorMessage() {
        commandBox.runCommand(HelpCommand.COMMAND_WORD.substring(0, HelpCommand.COMMAND_WORD.length() - 1));
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand(HelpCommand.COMMAND_WORD + "a");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Confirms that the given input string will autocomplete to the correct help command word.
     */
    private void assertHelpTabAutocomplete(String input) {
        commandBox.enterCommand(input);
        commandBox.pressTabKey();
        assertCommandBox(HelpCommand.COMMAND_WORD + " ");
    }

    private void assertHelpTabAutocompleteFailure(String input) {
        commandBox.enterCommand(input);
        commandBox.pressTabKey();
        assertCommandBox(input);
    }

    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
        helpWindowHandle.closeWindow();
    }

}
