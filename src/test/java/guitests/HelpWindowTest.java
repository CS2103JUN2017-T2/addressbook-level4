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
    public void help_tabAutocompleteFromOneChar_failure() {
        assertHelpTabAutocompleteFailure(HelpCommand.COMMAND_WORD.substring(0, 1));
    }

    @Test
    public void help_tabAutocompleteFromTwoChars_success() {
        assertHelpTabAutocomplete(HelpCommand.COMMAND_WORD.substring(0, 2));
    }

    @Test
    public void help_unknownCommandName_errorMessage() {
        commandBox.runCommand("h");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand("hel");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand("helpp");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

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
