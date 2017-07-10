package seedu.multitasky.model;

import static seedu.multitasky.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import seedu.multitasky.commons.core.ComponentManager;
import seedu.multitasky.commons.core.LogsCenter;
import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.events.model.EntryBookChangedEvent;
import seedu.multitasky.commons.events.model.EntryBookToUndoEvent;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;
import seedu.multitasky.model.entry.exceptions.EntryNotFoundException;
import seedu.multitasky.model.tag.Tag;
import seedu.multitasky.model.entry.exceptions.*;

//@@author A0126623L
/**
 * Represents the in-memory model of the address book data. All changes to any
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

    /** Raises an event when undo is entered */
    private void indicateUndoAction() {
        raise(new EntryBookToUndoEvent(entryBook));
    }

    @Override
    public synchronized void deleteEntry(ReadOnlyEntry target) throws EntryNotFoundException {
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

    @Override
    public void updateEntry(ReadOnlyEntry target, ReadOnlyEntry editedEntry)
            throws DuplicateEntryException, EntryNotFoundException {
        requireAllNonNull(target, editedEntry);

        _entryBook.updateEntry(target, editedEntry);
        indicateEntryBookChanged();
    }

    @Override
    public void undoPreviousAction() {
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
    public UnmodifiableObservableList<ReadOnlyEntry> getArchive() {
        return new UnmodifiableObservableList<>(_entryBook.getArchive());
    }

    // @@author A0126623L
    @Override
    public UnmodifiableObservableList<ReadOnlyEntry> getBin() {
        return new UnmodifiableObservableList<>(_entryBook.getBin());
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
    public void updateFilteredEventList(Set<String> keywords) {
        updateFilteredEventList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredEventList(Expression expression) {
        _filteredEventList.setPredicate(expression::satisfies);
    }

    // @@author A0126623L
    @Override
    public void updateFilteredDeadlineList(Set<String> keywords) {
        updateFilteredDeadlineList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredDeadlineList(Expression expression) {
        _filteredDeadlineList.setPredicate(expression::satisfies);
    }

    // @@author A0126623L
    @Override
    public void updateFilteredFloatingTaskList(Set<String> keywords) {
        updateFilteredFloatingTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredFloatingTaskList(Expression expression) {
        _filteredFloatingTaskList.setPredicate(expression::satisfies);
    }

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
        return _entryBook.equals(other._entryBook)
               && _filteredEventList.equals(other._filteredEventList)
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
    private class NameQualifier implements Qualifier {

        // TODO:
        // change variable name to 'nameAndTagKeyWords'.
        private Set<String> nameAndTagKeywords;

        NameQualifier(Set<String> nameKeywords) {
            this.nameAndTagKeywords = nameKeywords;
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
            String wordsInNameAndTags = parseWordsInNameAndTags(entry);

            for (String keyword : nameAndTagKeywords) {
                if (!wordsInNameAndTags.toLowerCase().contains(keyword.toLowerCase())) {
                    return false;
                }
            }
            return true;
        }

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
