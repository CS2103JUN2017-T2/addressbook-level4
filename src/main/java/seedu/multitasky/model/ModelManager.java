package seedu.multitasky.model;

import static seedu.multitasky.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import seedu.multitasky.commons.core.ComponentManager;
import seedu.multitasky.commons.core.LogsCenter;
import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.events.model.EntryBookChangedEvent;
import seedu.multitasky.commons.events.model.EntryBookToRedoEvent;
import seedu.multitasky.commons.events.model.EntryBookToUndoEvent;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
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

    /** Raises an event to indicate the model has changed */
    private void indicateEntryBookChanged() {
        raise(new EntryBookChangedEvent(_entryBook));
    }

    // @@author A0132788U
    /** Raises an event when undo is entered */
    private void indicateUndoAction() throws NothingToUndoException {
        EntryBookToUndoEvent undoEvent;
        raise(undoEvent = new EntryBookToUndoEvent(_entryBook, ""));
        if (undoEvent.getMessage().equals("undo successful")) {
            _entryBook.resetData(undoEvent.getData());
        } else {
            throw new NothingToUndoException("");
        }
    }

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

    // @@author

    @Override
    public synchronized void deleteEntry(ReadOnlyEntry target)
            throws DuplicateEntryException, EntryNotFoundException {
        _entryBook.removeEntry(target);

        indicateEntryBookChanged();
    }

    // @@author A0126623L
    @Override
    public synchronized void addEntry(ReadOnlyEntry entry) throws DuplicateEntryException {
        _entryBook.addEntry(entry);
        indicateEntryBookChanged();
    }
    // @@author

    // @@author A0126623L
    @Override
    public void changeEntryState(ReadOnlyEntry entryToChange, Entry.State newState)
            throws DuplicateEntryException, EntryNotFoundException {
        _entryBook.changeEntryState(entryToChange, newState);
        indicateEntryBookChanged();
    }

    @Override
    public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry) throws DuplicateEntryException,
            EntryNotFoundException {
        requireAllNonNull(target, editedEntry);

        _entryBook.updateEntry(target, editedEntry);
        indicateEntryBookChanged();
    }

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
        return new UnmodifiableObservableList<>(_entryBook.getActiveList());
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
        this.updateFilteredEventList(null, Entry.State.ACTIVE);
        this.updateFilteredDeadlineList(null, Entry.State.ACTIVE);
        this.updateFilteredFloatingTaskList(null, Entry.State.ACTIVE);
    }
    // @@author

    // @@author A0126623L
    @Override
    public void updateAllFilteredListToShowAllArchivedEntries() {
        this.updateFilteredEventList(null, Entry.State.ARCHIVED);
        this.updateFilteredDeadlineList(null, Entry.State.ARCHIVED);
        this.updateFilteredFloatingTaskList(null, Entry.State.ARCHIVED);
    }
    // @@author

    // @@author A0126623L
    @Override
    public void updateAllFilteredListToShowAllDeletedEntries() {
        this.updateFilteredEventList(new HashSet<String>(), Entry.State.DELETED);
        this.updateFilteredDeadlineList(new HashSet<String>(), Entry.State.DELETED);
        this.updateFilteredFloatingTaskList(new HashSet<String>(), Entry.State.DELETED);
    }
    // @@author

    // @@author A0126623L
    @Override
    public void updateFilteredEventList(Set<String> keywords, Entry.State state) {
        updateFilteredEventList(new PredicateExpression(new NameAndStatusQualifier(keywords, state)));
    }
    // @@author

    private void updateFilteredEventList(Expression expression) {
        _filteredEventList.setPredicate(expression::satisfies);
    }

    // @@author A0126623L
    @Override
    public void updateFilteredDeadlineList(Set<String> keywords, Entry.State state) {
        updateFilteredDeadlineList(new PredicateExpression(new NameAndStatusQualifier(keywords, state)));
    }

    private void updateFilteredDeadlineList(Expression expression) {
        _filteredDeadlineList.setPredicate(expression::satisfies);
    }

    // @@author A0126623L
    @Override
    public void updateFilteredFloatingTaskList(Set<String> keywords, Entry.State state) {
        updateFilteredFloatingTaskList(new PredicateExpression(new NameAndStatusQualifier(keywords, state)));
    }

    private void updateFilteredFloatingTaskList(Expression expression) {
        _filteredFloatingTaskList.setPredicate(expression::satisfies);
    }
    // @@author

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

    // @@author A0126623L
    /**
     * Represents a qualifier can check the presence of all keywords in the name
     * and tags of a ReadOnlyEntry.
     */
    private class NameAndStatusQualifier implements Qualifier {

        // TODO:
        // change variable name to 'nameAndTagKeyWords'.
        private Set<String> nameAndTagKeywords;
        private Entry.State state;

        NameAndStatusQualifier(Set<String> nameKeywords, Entry.State state) {
            this.nameAndTagKeywords = nameKeywords;
            this.state = state;
        }

        // @@author A0126623L
        /**
         * Matches words in an entry's name and tags and with all the keywords
         * of a Qualifier.
         *
         * @return boolean: true if all keywords are present in an entry's name
         *         and tags.
         */
        @Override
        public boolean run(ReadOnlyEntry entry) {
            if (!entry.getState().equals(state)) {
                return false;
            }

            String wordsInNameAndTags = parseWordsInNameAndTags(entry);

            for (String keyword : nameAndTagKeywords) {
                if (!wordsInNameAndTags.toLowerCase().contains(keyword.toLowerCase())) {
                    return false;
                }
            }
            return true;
        }
        // @@author A0126623L

        // @@author A0126623L
        /**
         * Parses and concatenates all words in an entry's name and tags. " " is
         * used as a delimiter.
         *
         * @param entry
         * @return String
         */
        private String parseWordsInNameAndTags(ReadOnlyEntry entry) {
            StringBuilder builder = new StringBuilder();
            builder.append(entry.getName().fullName);
            for (Tag t : entry.getTags()) {
                builder.append(" " + t.tagName);
            }
            return builder.toString();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameAndTagKeywords);
        }
    }
    // @@author
}
