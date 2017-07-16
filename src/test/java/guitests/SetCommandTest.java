package guitests;

import java.io.File;

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
        String validPath = TestUtil.getFilePathInSandboxFolder("abc.xml");
        File writeableFolder = new File(validPath).getParentFile();
        writeableFolder.setWritable(true);
        commandBox.runCommand("set " + validPath);
        assertResultMessage(String.format(SetCommand.MESSAGE_SUCCESS + validPath));
    }

    /***************************
     * End file path with non XML extension *
     **************************/
    @Test
    public void setFilePathToNonXml_failure() {
        String nonXmlFilePath = TestUtil.getFilePathInSandboxFolder("taskmanager.txt");
        commandBox.runCommand("set " + nonXmlFilePath);
        assertResultMessage(String.format(SetCommand.MESSAGE_FAILURE + SetCommand.MESSAGE_USAGE));
    }

    /***************************
     * Set non-writable file *
     **************************/
    @Test
    public void setFileToNonWritable_failure() {
        String nonWriteableFilePath = TestUtil.getFilePathInSandboxFolder("unwritable.xml");
        File nonWriteableFolder = new File(nonWriteableFilePath).getParentFile();
        nonWriteableFolder.setReadOnly();
        commandBox.runCommand("set " + nonWriteableFilePath);
        assertResultMessage(String.format(SetCommand.MESSAGE_FAILURE + SetCommand.MESSAGE_USAGE));
    }

    /***************************
     * Set invalid parent *
     **************************/
    @Test
    public void serToInvalidParent_failure() {
        String invalidParentFile = TestUtil.getFilePathInSandboxFolder("Desktop/noparent.xml");
        commandBox.runCommand("set " + invalidParentFile);
        assertResultMessage(String.format(SetCommand.MESSAGE_FAILURE + SetCommand.MESSAGE_USAGE));
    }
}
