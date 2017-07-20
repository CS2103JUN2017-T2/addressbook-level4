package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.input.KeyCode;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.util.EntryBuilder;
import seedu.multitasky.testutil.CommandUtil;
import seedu.multitasky.testutil.SampleEntries;

// @@author A0125586X
public class ListCommandTest extends EntryBookGuiTest {

    /********************************
     * Listing the different states *
     *******************************/
    @Test
    public void list_active_showEverything() {
        commandBox.runCommand(ListCommand.COMMAND_WORD);
        assertTrue(eventListPanel.isListMatching(SampleEntries.getSampleActiveEvents()));
        assertTrue(deadlineListPanel.isListMatching(SampleEntries.getSampleActiveDeadlines()));
        assertTrue(floatingTaskListPanel.isListMatching(SampleEntries.getSampleActiveFloatingTasks()));
    }

    @Test
    public void list_archive_empty() {
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_ARCHIVE);
        assertTrue(eventListPanel.isListMatching(new Entry[0]));
        assertTrue(deadlineListPanel.isListMatching(new Entry[0]));
        assertTrue(floatingTaskListPanel.isListMatching(new Entry[0]));
    }

    @Test
    public void list_bin_empty() {
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        assertTrue(eventListPanel.isListMatching(new Entry[0]));
        assertTrue(deadlineListPanel.isListMatching(new Entry[0]));
        assertTrue(floatingTaskListPanel.isListMatching(new Entry[0]));
    }

    /********************************
     * Listing the different orders *
     *******************************/
    @Test
    public void list_activeReverse_success() {
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_REVERSE);
        assertTrue(eventListPanel.isListMatching(reverse(SampleEntries.getSampleActiveEvents())));
        assertTrue(deadlineListPanel.isListMatching(reverse(SampleEntries.getSampleActiveDeadlines())));
        assertTrue(floatingTaskListPanel.isListMatching(SampleEntries.getSampleActiveFloatingTasks()));
    }

    @Test
    public void list_activeUpcoming_success() {
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_UPCOMING);
        assertTrue(floatingTaskListPanel.isListMatching(SampleEntries.getSampleActiveFloatingTasks()));
    }

    /*****************************************
     * Deleting an entry and listing the bin *
     ****************************************/
    @Test
    public void list_deleteEvent_listedInBin() {
        Index index = SampleEntries.INDEX_FIRST_ENTRY;
        Entry[] currentList = SampleEntries.getSampleActiveEvents();
        Entry entryToDelete = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getDeleteEventByIndexCommand(index));
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        assertTrue(eventListPanel.isListMatching(entryToDelete));
    }

    @Test
    public void list_deleteDeadline_listedInBin() {
        Index index = SampleEntries.INDEX_FIRST_ENTRY;
        Entry[] currentList = SampleEntries.getSampleActiveDeadlines();
        Entry entryToDelete = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getDeleteDeadlineByIndexCommand(index));
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        assertTrue(deadlineListPanel.isListMatching(entryToDelete));
    }

    @Test
    public void list_deleteFloatingTask_listedInBin() {
        Index index = SampleEntries.INDEX_FIRST_ENTRY;
        Entry[] currentList = SampleEntries.getSampleActiveFloatingTasks();
        Entry entryToDelete = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getDeleteFloatingTaskByIndexCommand(index));
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        assertTrue(floatingTaskListPanel.isListMatching(entryToDelete));
    }

    @Test
    public void list_keyboardShortcut_success() {
        commandBox.pressKey(KeyCode.F5);
        assertTrue(commandBox.getCommandInput().equals(ListCommand.COMMAND_WORD + " "));
    }

    /*******************************
     * Mixed-case and autocomplete *
     ******************************/
    @Test
    public void list_tabAutocomplete_success() {
        for (int i = 1; i < ListCommand.COMMAND_WORD.length(); ++i) {
            assertListTabAutocomplete(ListCommand.COMMAND_WORD.substring(0, i));
        }
        assertListTabAutocomplete(ListCommand.COMMAND_WORD + "a");
        assertListTabAutocomplete(ListCommand.COMMAND_WORD + "aa");
    }

    @Test
    public void list_tabAutocompleteKeyword_success() {
        commandBox.enterCommand("l r");
        commandBox.pressTabKey();
        assertCommandBox("list reverse ");

        commandBox.enterCommand("l u f");
        commandBox.pressTabKey();
        assertCommandBox("list upcoming from ");
    }


    /*******************
     * Utility methods *
     ******************/
    private Entry[] reverse(Entry... entries) {
        Entry[] reversed = new Entry[entries.length];
        for (int i = 0; i < entries.length; ++i) {
            reversed[i] = EntryBuilder.build(entries[entries.length - 1 - i]);
        }
        return reversed;
    }

    /**
     * Confirms that the given input string will autocomplete to the correct command word.
     */
    private void assertListTabAutocomplete(String input) {
        commandBox.enterCommand(input);
        commandBox.pressTabKey();
        assertCommandBox(ListCommand.COMMAND_WORD + " ");
    }
}
