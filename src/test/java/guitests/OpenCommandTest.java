package guitests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

import org.junit.Test;

import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.OpenCommand;
import seedu.multitasky.logic.commands.SaveCommand;
import seedu.multitasky.testutil.TestUtil;

//@@author A0132788U
/**
 * Tests for Open Command. Include valid path tests, non XML test, non-writable file and invalid parent.
 */
public class OpenCommandTest extends EntryBookGuiTest {

    /***************************
     * Open a readable XML file*
     **************************/
    @Test
    public void saveToExistingPath_success() {
        String validPath = TestUtil.getFilePathInSandboxFolder("exists.xml");
        new File(validPath).getParentFile().setWritable(true);
        commandBox.runCommand(SaveCommand.COMMAND_WORD + " " + validPath);
        commandBox.runCommand(AddCommand.COMMAND_WORD + " sample");
        commandBox.runCommand("open " + validPath);
        assertResultMessage(String.format(OpenCommand.MESSAGE_SUCCESS + validPath));
    }

    /***************************
     * Non-existing filepath *
     **************************/
    @Test
    public void openNonExistingFile_failure() {
        String validPath = TestUtil.getFilePathInSandboxFolder(Integer.toString(new Random().nextInt(250000)) + ".xml");
        new File(validPath).getParentFile().setWritable(true);
        commandBox.runCommand("open " + validPath);
        assertResultMessage(String.format(OpenCommand.MESSAGE_FAILURE + OpenCommand.MESSAGE_USAGE));
    }

    /***************************
     * End file path with non XML extension *
     **************************/
    @Test
    public void setFilePathToNonXml_failure() {
        String nonXmlFilePath = TestUtil.getFilePathInSandboxFolder("entrybook.fxml");
        commandBox.runCommand("open " + nonXmlFilePath);
        assertResultMessage(String.format(OpenCommand.MESSAGE_FAILURE + OpenCommand.MESSAGE_USAGE));
    }

    /***************************
     * Open a corrupted XML file
     *
     * @throws FileNotFoundException *
     **************************/
    @Test
    public void openCorruptedXmlFile_failure() throws FileNotFoundException {
        String corruptedFile = TestUtil.getFilePathInSandboxFolder("invalid.xml");
        new File(corruptedFile).getParentFile().setWritable(true);
        PrintStream out = new PrintStream(new FileOutputStream(corruptedFile));
        out.println("Corrupted data");
        out.close();
        commandBox.runCommand("open " + corruptedFile);
        assertResultMessage(String.format(OpenCommand.MESSAGE_INVALID_XML_FILE));
    }

}
