package seedu.multitasky.logic;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Entry.State;

// @@author A0140633R
/**
 * Stores the history of commands executed, and also the keywords used in the previous filters of inner lists.
 */
public class CommandHistory implements EditCommandHistory {
    private ArrayList<String> userInputHistory;
    private Set<String> previousSearchKeywords;
    private State previousState;
    private Calendar previousStartDate;
    private Calendar previousEndDate;
    private ArrayList<Model.Search> previousSearches;
    private EditEntryDescriptor editEntryDescriptor;

    public CommandHistory() {
        userInputHistory = new ArrayList<>();
        previousSearchKeywords = new HashSet<String>();
        previousState = Entry.State.ACTIVE;
        previousStartDate = null;
        previousEndDate = null;
        previousSearches = new ArrayList<>(Arrays.asList(Model.LENIENT_SEARCHES));
        editEntryDescriptor = null;
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
    public Set<String> getPrevKeywords() {
        return previousSearchKeywords;
    }

    public State getPrevState() {
        return previousState;
    }

    public Calendar getPrevStartDate() {
        return previousStartDate;
    }

    public Calendar getPrevEndDate() {
        return previousEndDate;
    }

    public Model.Search[] getPrevSearches() {
        return previousSearches.toArray(new Model.Search[previousSearches.size()]);
    }

    public void setPrevSearch(Set<String> searchKeywords, Calendar startDate, Calendar endDate, State state,
                              Model.Search... searches) {
        previousSearchKeywords = searchKeywords;
        previousStartDate = startDate;
        previousEndDate = endDate;
        previousState = state;
        previousSearches = new ArrayList<>(Arrays.asList(searches));

    @Override
    public boolean hasEditHistory() {
        return editEntryDescriptor != null;
    }

    @Override
    public void resetEditHistory() {
        editEntryDescriptor = null;
    }

    @Override
    public EditEntryDescriptor getEditHistory() {
        return editEntryDescriptor;
    }

    @Override
    public void setEditHistory(EditEntryDescriptor editEntryDescriptor) {
        this.editEntryDescriptor = editEntryDescriptor;
    }

}
