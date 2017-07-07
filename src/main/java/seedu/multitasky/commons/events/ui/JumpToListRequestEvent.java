package seedu.multitasky.commons.events.ui;

import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of entries
 */
public class JumpToListRequestEvent extends BaseEvent {

    public final int targetIndex;

    public JumpToListRequestEvent(Index targetIndex) {
        this.targetIndex = targetIndex.getZeroBased();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
