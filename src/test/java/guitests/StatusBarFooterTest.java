package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.testutil.EntryUtil;
import seedu.multitasky.testutil.SampleEntries;
import seedu.multitasky.ui.StatusBarFooter;

public class StatusBarFooterTest extends EntryBookGuiTest {

    private Clock originalClock;
    private Clock injectedClock;

    @Before
    public void injectFixedClock() {
        originalClock = StatusBarFooter.getClock();
        injectedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        StatusBarFooter.setClock(injectedClock);
    }

    @After
    public void restoreOriginalClock() {
        StatusBarFooter.setClock(originalClock);
    }

    @Test
    public void syncStatus_startup_initial() {
        assertEquals(StatusBarFooter.SYNC_STATUS_INITIAL, statusBarFooter.getSyncStatus());
    }

    @Test
    public void syncStatus_mutatingCommandSucceeds_syncStatusUpdated() {
        String timestamp = new Date(injectedClock.millis()).toString();
        String expected = String.format(StatusBarFooter.SYNC_STATUS_UPDATED, timestamp);
        // Mutating command succeeds
        assertTrue(commandBox.runCommand(EntryUtil.getFloatingTaskAddCommand(SampleEntries.SPECTACLES)));
        assertEquals(expected, statusBarFooter.getSyncStatus());
    }

    @Test
    public void syncStatus_nonMutatingCommandSucceeds_syncStatusRemainsUnchanged() {
        assertTrue(commandBox.runCommand(ListCommand.COMMAND_WORD)); // non-mutating command succeeds
        assertEquals(StatusBarFooter.SYNC_STATUS_INITIAL, statusBarFooter.getSyncStatus());
    }

    @Test
    public void syncStatus_commandFails_syncStatusRemainsUnchanged() {
        assertFalse(commandBox.runCommand("invalid command")); // invalid command fails
        assertEquals(StatusBarFooter.SYNC_STATUS_INITIAL, statusBarFooter.getSyncStatus());
    }

}
