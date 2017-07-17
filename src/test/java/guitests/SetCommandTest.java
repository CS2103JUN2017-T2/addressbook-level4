package guitests;

import java.io.File;
import java.util.Random;

import org.junit.Test;

import seedu.multitasky.logic.commands.SetCommand;
import seedu.multitasky.testutil.TestUtil;

//@@author A0132788U
/**
 * Tests for Set Command. Include valid path tests, non XML test, non-writable file and invalid parent.
 */
public class SetCommandTest extends EntryBookGuiTest {
    /***************************
     * Set to a valid file path *
     **************************/
    @Test
    public void setToValidPath_success() {
        String validPath = TestUtil.getFilePathInSandboxFolder(Integer.toString(new Random().nextInt(250000)) + ".xml");
        new File(validPath).getParentFile().setWritable(true);
        commandBox.runCommand("set " + validPath);
        assertResultMessage(String.format(SetCommand.MESSAGE_SUCCESS + validPath));
    }

    /***************************
     * End file path with non XML extension *
     **************************/
    @Test
    public void setFilePathToNonXml_failure() {
        String nonXmlFilePath = TestUtil.getFilePathInSandboxFolder("entrybook.fxml");
        commandBox.runCommand("set " + nonXmlFilePath);
        assertResultMessage(String.format(SetCommand.MESSAGE_FAILURE + SetCommand.MESSAGE_USAGE));
    }

    /***************************
     * Save at location of existing file *
     **************************/
    public void setToExistingPath_failure() {
        String validPath = TestUtil.getFilePathInSandboxFolder("exists.xml");
        new File(validPath).getParentFile().setWritable(true);
        commandBox.runCommand("set " + validPath);
        commandBox.runCommand("set " + validPath);
        assertResultMessage(String.format(SetCommand.MESSAGE_EXISTS));
    }

    /***************************
     * Set non-writable file *
     **************************/

    @Test
    public void setFileToNonWritable_failure() {
        String nonWriteableFilePath = TestUtil.getFilePathInSandboxFolder("unwritable.xml");
        new File(nonWriteableFilePath).getParentFile().setWritable(false);
        commandBox.runCommand("set " + nonWriteableFilePath);
        assertResultMessage(String.format(SetCommand.MESSAGE_FAILURE + SetCommand.MESSAGE_USAGE));
    }

    /***************************
     * Set invalid parent *
     **************************/
    @Test
    public void setToInvalidParent_failure() {
        String invalidParentFile = TestUtil.getFilePathInSandboxFolder("folderdoesntexist/noparent.xml");
        commandBox.runCommand("set " + invalidParentFile);
        assertResultMessage(String.format(SetCommand.MESSAGE_FAILURE + SetCommand.MESSAGE_USAGE));
    }
}
