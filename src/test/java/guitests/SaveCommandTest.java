package guitests;

import java.io.File;
import java.util.Random;

import org.junit.Test;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import seedu.multitasky.logic.commands.SaveCommand;
import seedu.multitasky.testutil.TestUtil;

//@@author A0132788U
/**
 * Tests for Save Command. Include valid path tests, non XML file, existing file, non-writable file and invalid parent.
 */
public class SaveCommandTest extends EntryBookGuiTest {
    /***************************
     * Set to a valid file path *
     **************************/
    @Test
    public void saveToValidPath_success() {
        String validPath = TestUtil.getFilePathInSandboxFolder(Integer.toString(new Random().nextInt(250000)) + ".xml");
        new File(validPath).getParentFile().setWritable(true);
        commandBox.runCommand("save " + validPath);
        assertResultMessage(String.format(SaveCommand.MESSAGE_SUCCESS + validPath));
    }

    /***************************
     * End file path with non XML extension *
     **************************/
    @Test
    public void saveFilePathToNonXml_failure() {
        String nonXmlFilePath = TestUtil.getFilePathInSandboxFolder("entrybook.fxml");
        commandBox.runCommand("save " + nonXmlFilePath);
        assertResultMessage(String.format(SaveCommand.MESSAGE_FAILURE + SaveCommand.MESSAGE_USAGE));
    }

    /***************************
     * Save at location of existing file *
     **************************/
    @Test
    public void saveToExistingPath_failure() {
        String validPath = TestUtil.getFilePathInSandboxFolder("exists.xml");
        new File(validPath).getParentFile().setWritable(true);
        commandBox.runCommand("save " + validPath);
        commandBox.runCommand("save " + validPath);
        assertResultMessage(String.format(SaveCommand.MESSAGE_EXISTS));
    }

    /*******************************
     * Mixed-case and autocomplete *
     ******************************/
    @Test
    public void save_shortcut_commandWord() {
        commandBox.pressKeyCombination(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        assertCommandBox(SaveCommand.COMMAND_WORD + " ");
    }

    /***************************
     * Set non-writable file *
     **************************/
    // Currently failing Appveyor but passing Travis.
    /*
     * @Test
     * public void setFileToNonWritable_failure() {
     * String nonWriteableFilePath = TestUtil.getFilePathInSandboxFolder("unwritable.xml");
     * new File(nonWriteableFilePath).getParentFile().setWritable(false);
     * commandBox.runCommand("save " + nonWriteableFilePath);
     * assertResultMessage(String.format(SaveCommand.MESSAGE_FAILURE + SaveCommand.MESSAGE_USAGE));
     * }
     */

}
