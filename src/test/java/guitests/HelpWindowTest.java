package guitests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.HelpWindowHandle;

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

    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
        helpWindowHandle.closeWindow();
    }

    private void assertHelpWindowNotOpen(HelpWindowHandle helpWindowHandle) {
        assertFalse(helpWindowHandle.isWindowOpen());
    }

}
