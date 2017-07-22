package seedu.multitasky.model;

import static seedu.multitasky.commons.util.CollectionUtil.requireAllNonNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import seedu.multitasky.commons.core.ComponentManager;
import seedu.multitasky.commons.core.LogsCenter;
import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.events.model.EntryBookChangedEvent;
import seedu.multitasky.commons.events.storage.EntryBookToRedoEvent;
import seedu.multitasky.commons.events.storage.EntryBookToUndoEvent;
import seedu.multitasky.commons.events.storage.FilePathChangedEvent;
import seedu.multitasky.commons.events.storage.LoadDataFromFilePathEvent;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.commons.util.match.PowerMatch;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.model.entry.exceptions.EntryOverdueException;
import seedu.multitasky.model.entry.exceptions.OverlappingAndOverdueEventException;
import seedu.multitasky.model.entry.exceptions.OverlappingEventException;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.storage.exception.NothingToRedoException;
import seedu.multitasky.storage.exception.NothingToUndoException;

//@@author A0126623L
/**
 * Represents the in-memory model of the entry book data. All changes to any
 * model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final EntryBook _entryBook;
    private final FilteredList<ReadOnlyEntry> _filteredEventList;
    private final FilteredList<ReadOnlyEntry> _filteredDeadlineList;
    private final FilteredList<ReadOnlyEntry> _filteredFloatingTaskList;

    // @@author A0126623L
    /**
     * Initializes a ModelManager with the given entryBook and userPrefs.
     * Note that the reference of the entries in the given {@code entryBook}
     * will be copied. Entries themselves will not be copied.
     */
    public ModelManager(ReadOnlyEntryBook entryBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(entryBook, userPrefs);

        logger.fine("Initializing with entry book: " + entryBook + " and user prefs " + userPrefs);

        this._entryBook = new EntryBook(entryBook);
        _filteredEventList = new FilteredList<>(this._entryBook.getEventList());
        _filteredDeadlineList = new FilteredList<>(this._entryBook.getDeadlineList());
        _filteredFloatingTaskList = new FilteredList<>(this._entryBook.getFloatingTaskList());
    }
    // @@author

    public ModelManager() {
        this(new EntryBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyEntryBook newData) {
        _entryBook.resetData(newData);
        indicateEntryBookChanged();
    }

    @Override
    public ReadOnlyEntryBook getEntryBook() {
        return _entryBook;
    }

    // =========== List Level Operations ===========

    // @@author A0126623L

    @Override
    public void clearStateSpecificEntries(Entry.State state) {
        _entryBook.clearStateSpecificEntries(state);
        indicateEntryBookChanged();
    }
    // @@author

    // =========== Entry Level Operations ===========

    @Override
    public synchronized void deleteEntry(ReadOnlyEntry target)
            throws EntryNotFoundException {
        _entryBook.removeEntry(target);

        indicateEntryBookChanged();
    }

    // @@author A0126623L
    @Override
    public synchronized void addEntry(ReadOnlyEntry entry)
            throws DuplicateEntryException, OverlappingEventException,
            OverlappingAndOverdueEventException, EntryOverdueException {
        try {
            _entryBook.addEntry(entry);
        } catch (OverlappingEventException | OverlappingAndOverdueEventException
                 | EntryOverdueException e) {
            indicateEntryBookChanged();
            throw e;
        }
        indicateEntryBookChanged();
    }
    // @@author

    // @@author A0126623L
    @Override
    public void changeEntryState(ReadOnlyEntry entryToChange, Entry.State newState)
            throws DuplicateEntryException, EntryNotFoundException, OverlappingEventException,
            OverlappingAndOverdueEventException, EntryOverdueException {
        try {
            _entryBook.changeEntryState(entryToChange, newState);
        } catch (EntryNotFoundException | OverlappingEventException
                 | OverlappingAndOverdueEventException | EntryOverdueException e) {
            indicateEntryBookChanged();
            throw e;
        }
        indicateEntryBookChanged();
    }

    // @@author A0126623L
    @Override
    public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry)
            throws DuplicateEntryException, EntryNotFoundException, OverlappingEventException,
            OverlappingAndOverdueEventException, EntryOverdueException {
        requireAllNonNull(target, editedEntry);
        try {
            if (target.getClass().equals(editedEntry.getClass())) { // updating to same instance of entry
                _entryBook.updateEntry(target, editedEntry);
            } else { // updating entry between lists
                _entryBook.addEntry(editedEntry);
                _entryBook.removeEntry(target);
            }
        } catch (EntryNotFoundException | OverlappingEventException
                 | OverlappingAndOverdueEventException | EntryOverdueException e) {
            indicateEntryBookChanged();
            throw e;
        }
        indicateEntryBookChanged();
    }
    // @@author

    // =========== Filtered Entry List Accessors ===========

    // @@author A0126623L
    /**
     * Return a list of {@code ReadOnlyEntry} backed by the internal event list of
     * {@code entryBook}
     */
    @Override
    public UnmodifiableObservableList<ReadOnlyEntry> getFilteredEventList() {
        return new UnmodifiableObservableList<>(_filteredEventList);
    }

    // @@author A0126623L
    /**
     * Return a list of {@code ReadOnlyEntry} backed by the internal deadline list of
     * {@code entryBook}
     */
    @Override
    public UnmodifiableObservableList<ReadOnlyEntry> getFilteredDeadlineList() {
        return new UnmodifiableObservableList<>(_filteredDeadlineList);
    }

    // @@author A0126623L
    /**
     * Return a list of {@code ReadOnlyEntry} backed by the internal floating task list of
     * {@code entryBook}
     */
    @Override
    public UnmodifiableObservableList<ReadOnlyEntry> getFilteredFloatingTaskList() {
        return new UnmodifiableObservableList<>(_filteredFloatingTaskList);
    }

    // @@author A0126623L
    @Override
    public UnmodifiableObservableList<ReadOnlyEntry> getActiveList() {
        return new UnmodifiableObservableList<>(_entryBook.getAllEntries());
    }

    // @@author A0126623L
    @Override
    public void updateFilteredEventListToShowAll() {
        _filteredEventList.setPredicate(null);
    }

    // @@author A0126623L
    @Override
    public void updateFilteredDeadlineListToShowAll() {
        _filteredDeadlineList.setPredicate(null);
    }

    // @@author A0126623L
    @Override
    public void updateFilteredFloatingTaskListToShowAll() {
        _filteredFloatingTaskList.setPredicate(null);
    }
    // @@author

    // @@author A0126623L
    /**
     * Updates all filtered list to show all entries.
     */
    @Override
    public void updateAllFilteredListToShowAll() {
        updateFilteredEventListToShowAll();
        updateFilteredDeadlineListToShowAll();
        updateFilteredFloatingTaskListToShowAll();
    }
    // @@author

    // @@author A0126623L
    @Override
    public void updateAllFilteredListToShowAllActiveEntries() {
        this.updateAllFilteredLists(new HashSet<>(), null, null, Entry.State.ACTIVE, Search.AND);
    }
    // @@author

    // @@author A0126623L
    @Override
    public void updateAllFilteredListToShowAllArchivedEntries() {
        this.updateAllFilteredLists(new HashSet<>(), null, null, Entry.State.ARCHIVED, Search.AND);
    }
    // @@author

    // @@author A0126623L
    @Override
    public void updateAllFilteredListToShowAllDeletedEntries() {
        this.updateAllFilteredLists(new HashSet<>(), null, null, Entry.State.DELETED, Search.AND);
    }

    // @@author A0125586X
    @Override
    public void updateAllFilteredLists(Set<String> keywords, Calendar startDate, Calendar endDate,
                                       Entry.State state, Search... searches) {
        List<Entry.State> states = new ArrayList<>(Arrays.asList(new Entry.State[] { state, null }));
        updateAllFilteredLists(keywords, startDate, endDate, states, searches);
    }

    // @@author A0125586X
    @Override
    public void updateAllFilteredLists(Set<String> keywords, Calendar startDate, Calendar endDate,
                                       Entry.State state, Entry.State state2, Search... searches) {
        List<Entry.State> states = new ArrayList<>(Arrays.asList(new Entry.State[] { state, state2 }));
        updateAllFilteredLists(keywords, startDate, endDate, states, searches);
    }

    // @@author A0125586X
    private void updateAllFilteredLists(Set<String> keywords, Calendar startDate, Calendar endDate,
                                        List<Entry.State> states, Search... searches) {
        NameDateStateQualifier qualifier;
        for (Search search : searches) {
            if (search == Search.POWER_AND || search == Search.POWER_OR) {
                for (int level = PowerMatch.MIN_LEVEL; level <= PowerMatch.MAX_LEVEL; ++level) {
                    qualifier = new NameDateStateQualifier(keywords, startDate, endDate, states, search,
                                                           level);
                    updateFilteredEventList(new PredicateExpression(qualifier));
                    updateFilteredDeadlineList(new PredicateExpression(qualifier));
                    updateFilteredFloatingTaskList(new PredicateExpression(qualifier));
                    if ((getFilteredEventList().size() + getFilteredDeadlineList().size()
                         + getFilteredFloatingTaskList().size()) > 0) {
                        break; // No need to search further
                    }
                }
            } else {
                qualifier = new NameDateStateQualifier(keywords, startDate, endDate, states, search, -1);
                updateFilteredEventList(new PredicateExpression(qualifier));
                updateFilteredDeadlineList(new PredicateExpression(qualifier));
                updateFilteredFloatingTaskList(new PredicateExpression(qualifier));
                if ((getFilteredEventList().size() + getFilteredDeadlineList().size()
                     + getFilteredFloatingTaskList().size()) > 0) {
                    break; // No need to search further
                }
            }
        }
    }

    // @@author A0126623L
    private void updateFilteredEventList(Expression expression) {
        _filteredEventList.setPredicate(expression::satisfies);
    }

    // @@author A0125586X
    @Override
    public void updateFilteredEventList(Set<String> keywords, Calendar startDate, Calendar endDate,
                                        Entry.State state, Search search, int level) {
        updateFilteredEventList(new PredicateExpression(new NameDateStateQualifier(keywords,
                                                                                   startDate, endDate, state,
                                                                                   search, level)));
    }

    // @@author A0126623L
    private void updateFilteredDeadlineList(Expression expression) {
        _filteredDeadlineList.setPredicate(expression::satisfies);
    }

    // @@author A0125586X
    @Override
    public void updateFilteredDeadlineList(Set<String> keywords, Calendar startDate,
                                           Calendar endDate, Entry.State state, Search search,
                                           int level) {
        updateFilteredDeadlineList(new PredicateExpression(new NameDateStateQualifier(keywords,
                                                                                      startDate, endDate,
                                                                                      state, search, level)));
    }

    // @@author A0126623L
    private void updateFilteredFloatingTaskList(Expression expression) {
        _filteredFloatingTaskList.setPredicate(expression::satisfies);
    }

    // @@author A0125586X
    @Override
    public void updateFilteredFloatingTaskList(Set<String> keywords, Calendar startDate,
                                               Calendar endDate, Entry.State state, Search search,
                                               int level) {
        updateFilteredFloatingTaskList(new PredicateExpression(new NameDateStateQualifier(keywords,
                                                                                          startDate, endDate,
                                                                                          state, search,
                                                                                          level)));
    }

    // @@author A0125586X
    /** Updates the sorting comparators used. */
    @Override
    public void updateSortingComparators(Comparator<ReadOnlyEntry> eventComparator,
                                         Comparator<ReadOnlyEntry> deadlineComparator,
                                         Comparator<ReadOnlyEntry> floatingTaskComparator) {
        _entryBook.setComparators(eventComparator, deadlineComparator, floatingTaskComparator);
    }
    // @@author

    // @@author A0126623L
    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return _entryBook.equals(other._entryBook) && _filteredEventList.equals(other._filteredEventList)
               && _filteredDeadlineList.equals(other._filteredDeadlineList)
               && _filteredFloatingTaskList.equals(other._filteredFloatingTaskList);
    }
    // @@author

    // ========== Inner classes/interfaces used for filtering ==========

    interface Expression {
        /**
         * Evaluates whether a ReadOnlyEntry satisfies a certain condition.
         */
        boolean satisfies(ReadOnlyEntry entry);

        @Override
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyEntry entry) {
            return qualifier.run(entry);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyEntry entry);

        @Override
        String toString();
    }

    // @@author A0125586X
    /**
     * Represents a qualifier can check the presence of all keywords in the name
     * and tags of a ReadOnlyEntry, if a ReadOnlyEntry falls within a given date range,
     * and if a ReadOnlyEntry matches the required state.
     */
    private class NameDateStateQualifier implements Qualifier {

        protected Set<String> nameAndTagKeywords;
        protected Calendar startDate;
        protected Calendar endDate;
        protected List<Entry.State> states;
        protected Search search;
        protected int level;

        protected DateFormat dateFormat;

        /**
         * Constructs the NameDateStateQualifier.
         *
         * @param nameAndTagKeywords the keywords to match against the entry's name and tags. cannot be null.
         * @param startDate the earliest date that will produce a match. if it is null then
         *                  there is no lower limit on the entry's date.
         * @param endDate   the latest date that will produce a match. if it is null then
         *                  there is no upper limit on the entry's date.
         * @param states    the required states to match against the entry's state. if it is null or empty
         *                  then entries of any state will match.
         * @param search    the type of search to use (AND, OR, POWER_AND, POWER_OR). cannot be null.
         */
        public NameDateStateQualifier(Set<String> nameAndTagKeywords,
                Calendar startDate, Calendar endDate,
                List<Entry.State> states, Search search, int level) {
            if (nameAndTagKeywords == null) {
                throw new AssertionError("nameAndTagKeywords for NameDateStateQualifier cannot be null");
            }
            if (search == null) {
                throw new AssertionError("search type for NameDateStateQualifier cannot be null");
            }
            if (states == null) {
                throw new AssertionError("States list cannot be null");
            }
            if (states.size() != 2) {
                throw new AssertionError("Number of state arguments to match must be 2");
            }

            this.nameAndTagKeywords = nameAndTagKeywords;
            this.startDate = startDate;
            this.endDate = endDate;
            this.states = states;
            this.search = search;
            this.level = level;
        }

        /**
         * Alternative constructor for just a single state
         */
        public NameDateStateQualifier(Set<String> nameAndTagKeywords,
                Calendar startDate, Calendar endDate,
                Entry.State state, Search search, int level) {
            this(nameAndTagKeywords, startDate, endDate,
                 new ArrayList<>(Arrays.asList(new Entry.State[] { state, null })), search, level);
        }

        @Override
        public boolean run(ReadOnlyEntry entry) {
            if (matchesState(entry) && matchesNameAndTagKeywords(entry)) {
                if (entry instanceof FloatingTask
                    || entry instanceof Deadline && isWithinRange(entry.getEndDateAndTime())
                    || entry instanceof Event && isWithinRange(entry.getStartDateAndTime())) {
                    return true;
                } else {
                    assert false : "DateAndStatusQualifier::run received ReadOnlyEntry of unknown type";
                }
            }
            return false;
        }

        protected boolean matchesState(ReadOnlyEntry entry) {
            return ((states.get(0) == null && states.get(1) == null)
                    || ((states.get(0) != null && entry.getState().equals(states.get(0)))
                        || (states.get(1) != null && entry.getState().equals(states.get(1)))));
        }

        protected boolean matchesNameAndTagKeywords(ReadOnlyEntry entry) {
            String nameAndTags = parseWordsInNameAndTags(entry).trim().toLowerCase();
            switch (search) {
            case AND:
                for (String keyword : nameAndTagKeywords) {
                    if (!nameAndTags.contains(keyword.trim().toLowerCase())) {
                        return false;
                    }
                }
                return true;
            case OR:
                if (nameAndTagKeywords.size() == 0) {
                    return true;
                }
                for (String keyword : nameAndTagKeywords) {
                    if (nameAndTags.contains(keyword.trim().toLowerCase())) {
                        return true;
                    }
                }
                return false;
            case POWER_AND:
                for (String keyword : nameAndTagKeywords) {
                    if (!new PowerMatch().isMatch(level, keyword, nameAndTags)) {
                        return false;
                    }
                }
                return true;
            case POWER_OR:
                if (nameAndTagKeywords.size() == 0) {
                    return true;
                }
                for (String keyword : nameAndTagKeywords) {
                    if (new PowerMatch().isMatch(level, keyword, nameAndTags)) {
                        return true;
                    }
                }
                return false;
            default:
                assert false : "DateAndStatusQualifier: unknown search type";
            }
            return false;
        }

        // @@author A0126623L
        /**
         * Parses and concatenates all words in an entry's name and tags.
         *
         * @param entry
         * @return String
         */
        protected String parseWordsInNameAndTags(ReadOnlyEntry entry) {
            StringBuilder builder = new StringBuilder();
            builder.append(entry.getName().fullName.replaceAll("\\s+", ""));
            for (Tag t : entry.getTags()) {
                builder.append(t.tagName);
            }
            return builder.toString();
        }

        // @@author A0125586X
        /**
         * Checks if the given date to check is within the start and end dates of this Qualifier.
         */
        protected boolean isWithinRange(Calendar checkDate) {
            if (startDate == null) {
                if (endDate == null) {
                    return true;
                } else {
                    return checkDate.compareTo(endDate) <= 0;
                }
            } else if (endDate == null) {
                return checkDate.compareTo(startDate) >= 0;
            } else {
                return (checkDate.compareTo(startDate) >= 0) && (checkDate.compareTo(endDate) <= 0);
            }
        }
        // @@author

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("NameDateStateQualifier: ")
                   .append("keywords = ");
            for (String keyword : nameAndTagKeywords) {
                builder.append(keyword).append(", ");
            }
            builder.append("startDate = ");
            if (startDate == null) {
                builder.append("null");
            } else {
                builder.append(dateFormat.format(startDate));
            }
            builder.append(", endDate = ");
            if (endDate == null) {
                builder.append("null");
            } else {
                builder.append(dateFormat.format(endDate));
            }
            builder.append(", states =");
            for (Entry.State state : states) {
                builder.append(state.toString());
            }
            return builder.toString();
        }
    }

    // ========== Event Raising Methods ==========

    /** Raises an event to indicate the model has changed */
    private void indicateEntryBookChanged() {
        raise(new EntryBookChangedEvent(_entryBook));
    }

    // @@author A0132788U
    /** Raises an event when undo is entered by user and resets data to previous state for updating the UI */
    private void indicateUndoAction() throws NothingToUndoException {
        EntryBookToUndoEvent undoEvent;
        raise(undoEvent = new EntryBookToUndoEvent(_entryBook, ""));
        if (undoEvent.getMessage().equals("undo successful")) {
            _entryBook.resetData(undoEvent.getData());
        } else {
            throw new NothingToUndoException("");
        }
    }

    /** Raises an event when redo is entered by user and resets data to next state for updating the UI */
    private void indicateRedoAction() throws NothingToRedoException {
        EntryBookToRedoEvent redoEvent;
        raise(redoEvent = new EntryBookToRedoEvent(_entryBook, ""));
        if (redoEvent.getMessage().equals("redo successful")) {
            _entryBook.resetData(redoEvent.getData());
        } else {
            throw new NothingToRedoException("");
        }
    }

    @Override
    public void undoPreviousAction() throws NothingToUndoException {
        indicateUndoAction();
    }

    @Override
    public void redoPreviousAction() throws NothingToRedoException {
        indicateRedoAction();
    }

    /** Raises an event when new file path is entered by user */
    @Override
    public void changeFilePath(String newFilePath) {
        raise(new FilePathChangedEvent(_entryBook, newFilePath));
    }

    /** Raises an event when filepath to load data from is entered by user */
    @Override
    public void openFilePath(String newFilePath) throws IllegalValueException {
        LoadDataFromFilePathEvent event;
        raise(event = new LoadDataFromFilePathEvent(_entryBook, newFilePath, ""));
        if (event.getMessage().equals("open successful")) {
            _entryBook.resetData(event.getData());
            indicateEntryBookChanged();
        } else {
            throw new IllegalValueException("load unsuccessful");
        }
    }
    // @@author

}
