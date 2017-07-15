package seedu.multitasky.logic;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Entry.State;

/**
 * Stores the history of commands executed, and also the keywords used in the previous filters of inner lists.
 */
public class CommandHistory {
    private ArrayList<String> userInputHistory;
    private Set<String> previousSearchKeywords;
    private State previousState;

    public CommandHistory() {
        userInputHistory = new ArrayList<>();
        previousSearchKeywords = new HashSet<String>();
        previousState = Entry.State.ACTIVE;
    }

    /**
     * Appends {@code userInput} to the list of user input entered.
     */
    public void add(String userInput) {
        requireNonNull(userInput);
        userInputHistory.add(userInput);
    }

    /**
     * Returns a defensive copy of {@code userInputHistory}.
     */
    public List<String> getHistory() {
        return new ArrayList<>(userInputHistory);
    }

    // @@author A0140633R
    public Set<String> getPrevSearch() {
        return previousSearchKeywords;
    }

    public State getPrevState() {
        return previousState;
    }

    public void setPrevSearch(Set<String> nextSearch, State nextState) {
        previousSearchKeywords = nextSearch;
        previousState = nextState;
    }

}
