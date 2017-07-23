package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.scene.input.KeyCode;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.logic.commands.ClearCommand;
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
        commandBox.runCommand(ClearCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_ARCHIVE);
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_ARCHIVE);
        assertTrue(eventListPanel.isListMatching(new Entry[0]));
        assertTrue(deadlineListPanel.isListMatching(new Entry[0]));
        assertTrue(floatingTaskListPanel.isListMatching(new Entry[0]));
    }

    @Test
    public void list_bin_empty() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        assertTrue(eventListPanel.isListMatching(new Entry[0]));
        assertTrue(deadlineListPanel.isListMatching(new Entry[0]));
        assertTrue(floatingTaskListPanel.isListMatching(new Entry[0]));
    }

    // @@author A0126623L
    @Test
    public void list_archive_success() {
        commandBox.runCommand(CommandUtil.getListArchiveCommand());
        assertTrue(eventListPanel.isListMatching(SampleEntries.getSampleArchivedEvents()));
        assertTrue(deadlineListPanel.isListMatching(SampleEntries.getSampleArchivedDeadlines()));
        assertTrue(floatingTaskListPanel.isListMatching(SampleEntries.getSampleArchivedFloatingTasks()));
    }

    @Test
    public void list_bin_success() {
        commandBox.runCommand(CommandUtil.getListBinCommand());
        assertTrue(eventListPanel.isListMatching(SampleEntries.getSampleDeletedEvents()));
        assertTrue(deadlineListPanel.isListMatching(SampleEntries.getSampleDeletedDeadlines()));
        assertTrue(floatingTaskListPanel.isListMatching(SampleEntries.getSampleDeletedFloatingTasks()));
    }

    @Test
    public void list_allEntries_success() {
        commandBox.runCommand(CommandUtil.getListAllCommand());
        assertTrue(eventListPanel.isListMatching(SampleEntries.getAllEvents()));
        assertTrue(deadlineListPanel.isListMatching(SampleEntries.getAllDeadlines()));
        assertTrue(floatingTaskListPanel.isListMatching(SampleEntries.getAllFloatingTasks()));
    }

    // @@author A0125586X
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
        commandBox.runCommand(ClearCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        commandBox.runCommand(CommandUtil.getDeleteEventByIndexCommand(index));
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        assertTrue(eventListPanel.isListMatching(entryToDelete));
    }

    @Test
    public void list_deleteDeadline_listedInBin() {
        Index index = SampleEntries.INDEX_FIRST_ENTRY;
        Entry[] currentList = SampleEntries.getSampleActiveDeadlines();
        Entry entryToDelete = currentList[index.getZeroBased()];
        commandBox.runCommand(ClearCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        commandBox.runCommand(CommandUtil.getDeleteDeadlineByIndexCommand(index));
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        assertTrue(deadlineListPanel.isListMatching(entryToDelete));
    }

    @Test
    public void list_deleteFloatingTask_listedInBin() {
        Index index = SampleEntries.INDEX_FIRST_ENTRY;
        Entry[] currentList = SampleEntries.getSampleActiveFloatingTasks();
        Entry entryToDelete = currentList[index.getZeroBased()];
        commandBox.runCommand(ClearCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        commandBox.runCommand(CommandUtil.getDeleteFloatingTaskByIndexCommand(index));
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_BIN);
        assertTrue(floatingTaskListPanel.isListMatching(entryToDelete));
    }

    /****************************
     * Listing over date ranges *
     ***************************/
    @Test
    public void list_lowerDateRange_filtered() {
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_FROM + " Jan 1 2031");
        Entry[] expectedList = {};
        assertTrue(eventListPanel.isListMatching(expectedList));
        assertTrue(deadlineListPanel.isListMatching(expectedList));
    }

    @Test
    public void list_upperDateRange_filtered() {
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_TO + " Jan 1 2029");
        Entry[] expectedList = {};
        assertTrue(eventListPanel.isListMatching(expectedList));
        assertTrue(deadlineListPanel.isListMatching(expectedList));
    }

    @Test
    public void list_wholeDateRange_filtered() {
        commandBox.runCommand(ListCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_FROM + " Jun 1 2030"
                                + " " +  CliSyntax.PREFIX_TO + " December 31 2030");
        Entry[] expectedEventList = { SampleEntries.DINNER };
        Entry[] expectedDeadlineList = { SampleEntries.TAX,
                                         SampleEntries.PAPER };
        assertTrue(eventListPanel.isListMatching(expectedEventList));
        assertTrue(deadlineListPanel.isListMatching(expectedDeadlineList));
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

        commandBox.enterCommand("l u t");
        commandBox.pressTabKey();
        assertCommandBox("list upcoming to ");
    }

    @Test
    public void list_keyboardShortcut_success() {
        commandBox.pressKey(KeyCode.F5);
        assertTrue(commandBox.getCommandInput().equals(ListCommand.COMMAND_WORD + " "));
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
