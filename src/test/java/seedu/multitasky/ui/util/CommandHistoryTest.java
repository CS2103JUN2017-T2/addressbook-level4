package seedu.multitasky.ui.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

// @@author A0125586X
public class CommandHistoryTest {

    @Test
    public void commandHistory_noMorePrevious_same() {
        TextHistory commandHistory = new CommandHistory();
        commandHistory.save("a");
        assertTrue(commandHistory.getPrevious("b").equals("a"));
        assertTrue(commandHistory.getPrevious("a").equals("a"));
        assertTrue(commandHistory.getPrevious("a").equals("a"));
    }

    @Test
    public void commandHistory_noNext_same() {
        TextHistory commandHistory = new CommandHistory();
        assertTrue(commandHistory.getNext("a").equals("a"));
        assertTrue(commandHistory.getNext("a").equals("a"));
    }

    @Test
    public void commandHistory_noMoreNext_same() {
        TextHistory commandHistory = new CommandHistory();
        commandHistory.save("a");
        assertTrue(commandHistory.getPrevious("b").equals("a"));
        assertTrue(commandHistory.getNext("a").equals("b"));
        assertTrue(commandHistory.getNext("b").equals("b"));
        assertTrue(commandHistory.getNext("b").equals("b"));
    }

    @Test
    public void commandHistory_methods_success() {
        TextHistory commandHistory = new CommandHistory();
        commandHistory.save("a");
        commandHistory.save("b");
        commandHistory.save("c");
        assertTrue(commandHistory.getPrevious("d").equals("c"));
        assertTrue(commandHistory.getPrevious("c").equals("b"));
        assertTrue(commandHistory.getPrevious("b").equals("a"));

        assertTrue(commandHistory.getNext("a").equals("b"));
        assertTrue(commandHistory.getNext("b").equals("c"));
        assertTrue(commandHistory.getNext("c").equals("d"));

        assertTrue(commandHistory.getPrevious("d").equals("c"));
        assertTrue(commandHistory.getPrevious("c").equals("b"));
        commandHistory.save("d");
        assertTrue(commandHistory.getPrevious("e").equals("d"));
        assertTrue(commandHistory.getNext("d").equals("e"));
    }

}
