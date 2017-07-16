package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.util.CollectionUtil;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.tag.Tag;

// @@author A0140633R
/**
 * Abstract class that contains all of the utility methods used for EditCommand sub-types.
 */
public abstract class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " : Edits the entry identified by keywords"
            + " if it is the only entry found, or edits the entry identified by the index number of the last"
            + " entry listing.\n"
            + "Format: " + COMMAND_WORD + " [" + "[" + "KEYWORDS" + "]" + " |"
            + " [" + String.join(" | ", CliSyntax.PREFIX_EVENT.toString(), CliSyntax.PREFIX_DEADLINE.toString(),
            CliSyntax.PREFIX_FLOATINGTASK.toString()) + "]" + " INDEX" + "]"
            + " [" + "[" + CliSyntax.PREFIX_NAME + " NAME" + "]"
            + " |" + "[" + CliSyntax.PREFIX_BY + " DATE" + "]"
            + " |" + " [" + CliSyntax.PREFIX_FROM + " DATE"
            + " " + CliSyntax.PREFIX_TO + " DATE" + "]" + "]"
            + " [" + CliSyntax.PREFIX_TAG + " TAGS..." + "]" + "\n"
            + "All possible flags for Edit : 'name', 'tag','by', 'from', 'to', 'at', 'on', 'event',"
            + " 'deadline', 'float'" + "\n"
            + "Note: Existing values will be overwritten by the input values.";


    public static final String MESSAGE_SUCCESS = "Target entry: " + Messages.MESSAGE_ENTRY_DESCRIPTION
            + "%1$s\n" + "Entry edited:" + Messages.MESSAGE_ENTRY_DESCRIPTION + "%2$s";
    public static final String MESSAGE_SUCCESS_WITH_OVERLAP_ALERT = "Entry edited:" + "\n"
            + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
            + "Alert: Edited entry %1$s overlaps with existing event(s).";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.\n"
            + "Format: " + COMMAND_WORD + " [" + "[" + "KEYWORDS" + "]" + " |"
            + " [" + String.join(" | ", CliSyntax.PREFIX_EVENT.toString(), CliSyntax.PREFIX_DEADLINE.toString(),
            CliSyntax.PREFIX_FLOATINGTASK.toString()) + "]" + " INDEX" + "]"
            + " [" + "[" + CliSyntax.PREFIX_NAME + " NAME" + "]"
            + " |" + "[" + CliSyntax.PREFIX_BY + " DATE" + "]"
            + " |" + " [" + CliSyntax.PREFIX_FROM + " DATE"
            + " " + CliSyntax.PREFIX_TO + " DATE" + "]" + "]"
            + " [" + CliSyntax.PREFIX_TAG + " TAGS..." + "]" + "\n"
            + "All possible flags for Edit : 'name', 'tag', 'by', 'from', 'to', 'at', 'event',"
            + " 'deadline', 'float'" + "\n";

    public static final String MESSAGE_DUPLICATE_ENTRY = "This entry already exists in the task manager.";

    public static final String MESSAGE_ENDDATE_BEFORE_STARTDATE = "Can not have end date before start date!";

    public static final String[] VALID_PREFIXES = {CliSyntax.PREFIX_EVENT.toString(),
                                                   CliSyntax.PREFIX_DEADLINE.toString(),
                                                   CliSyntax.PREFIX_FLOATINGTASK.toString(),
                                                   CliSyntax.PREFIX_NAME.toString(),
                                                   CliSyntax.PREFIX_FROM.toString(),
                                                   CliSyntax.PREFIX_BY.toString(),
                                                   CliSyntax.PREFIX_AT.toString(),
                                                   CliSyntax.PREFIX_ON.toString(),
                                                   CliSyntax.PREFIX_TO.toString(),
                                                   CliSyntax.PREFIX_TAG.toString()};

    protected final EditEntryDescriptor editEntryDescriptor;
    protected ReadOnlyEntry entryToEdit;

    /**
     * @param editEntryDescriptor details to edit the entry with
     */
    public EditCommand(EditEntryDescriptor editEntryDescriptor) {
        requireNonNull(editEntryDescriptor);

        this.editEntryDescriptor = new EditEntryDescriptor(editEntryDescriptor);
    }

    /**
     * Creates and returns a {@code Entry} with the details of
     * {@code entryToEdit} edited with {@code editEntryDescriptor}.
     */
    protected static Entry createEditedEntry(ReadOnlyEntry entryToEdit,
            EditEntryDescriptor editEntryDescriptor) throws CommandException {
        assert entryToEdit != null;

        Name updatedName = editEntryDescriptor.getName().orElse(entryToEdit.getName());
        Set<Tag> updatedTags = editEntryDescriptor.getTags().orElse(entryToEdit.getTags());
        Calendar updatedStartDate = editEntryDescriptor.getStartDate()
                                                       .orElse(entryToEdit.getStartDateAndTime());
        Calendar updatedEndDate = editEntryDescriptor.getEndDate()
                                                     .orElse(entryToEdit.getEndDateAndTime());

        if (editToFloating(updatedStartDate, updatedEndDate) // floating task cases
            // deadline but reset end date
            || editToDeadline(updatedStartDate, updatedEndDate) && editEntryDescriptor.hasResetEndDate()
            // event but reset both start, end date
            || editToEvent(updatedStartDate, updatedEndDate) && editEntryDescriptor.hasResetEndDate()
               && editEntryDescriptor.hasResetStartDate()) {
            return new FloatingTask(updatedName, updatedTags);

        } else if (editToDeadline(updatedStartDate, updatedEndDate) // deadline cases
                   // event with start date removed
                   || (editToEvent(updatedStartDate, updatedEndDate))
                      && editEntryDescriptor.hasResetStartDate()
                   // event with startdate == enddate
                   || (editToEvent(updatedStartDate, updatedEndDate))
                      && updatedEndDate.compareTo(updatedStartDate) == 0
                   || editToEvent(updatedStartDate, updatedEndDate)
                      && editEntryDescriptor.hasResetStartDate()
                   || editToEvent(updatedStartDate, updatedEndDate)
                      && editEntryDescriptor.hasResetEndDate()) {
            updatedEndDate = updatedEndDate == null ? updatedStartDate : updatedEndDate;
            return new Deadline(updatedName, updatedEndDate, updatedTags);

        } else if (editToEvent(updatedStartDate, updatedEndDate)
                   && editEntryDescriptor.hasResetStartDate()) {
            return new Deadline(updatedName, updatedStartDate, updatedTags);

        } else if (editToEvent(updatedStartDate, updatedEndDate)) { //events cases
            if (updatedEndDate.compareTo(updatedStartDate) < 0) { // edited to invalid end date
                throw new CommandException(MESSAGE_ENDDATE_BEFORE_STARTDATE);
            }
            return new Event(updatedName, updatedStartDate, updatedEndDate, updatedTags);
        } else {
            throw new AssertionError("can only have event, deadline or floating task");
        }
    }

    private static boolean editToEvent(Calendar updatedStartDate, Calendar updatedEndDate) {
        return updatedStartDate != null && updatedEndDate != null;
    }

    private static boolean editToDeadline(Calendar updatedStartDate, Calendar updatedEndDate) {
        return updatedStartDate == null && updatedEndDate != null
                || updatedStartDate != null && updatedEndDate == null;
    }

    private static boolean editToFloating(Calendar updatedStartDate, Calendar updatedEndDate) {
        return updatedStartDate == null && updatedEndDate == null;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return editEntryDescriptor.equals(e.editEntryDescriptor);
    }

    /**
     * Stores the details to edit the entry with. Each non-empty field value
     * will replace the corresponding field value of the entry.
     *
     */
    public static class EditEntryDescriptor {
        private Name name;
        private Set<Tag> tags;
        private Calendar startDate;
        private Calendar endDate;
        private boolean resetStartDate;
        private boolean resetEndDate;

        public EditEntryDescriptor() {
            name = null;
            tags = null;
            startDate = null;
            endDate = null;
            resetStartDate = false;
            resetEndDate = false;
        }

        public EditEntryDescriptor(EditEntryDescriptor toCopy) {
            if (toCopy.getName().isPresent()) {
                this.name = toCopy.getName().get();
            }
            if (toCopy.getTags().isPresent()) {
                this.tags = toCopy.getTags().get();
            }
            if (toCopy.getStartDate().isPresent()) {
                this.startDate = toCopy.getStartDate().get();
            }
            if (toCopy.getEndDate().isPresent()) {
                this.endDate = toCopy.getEndDate().get();
            }
            resetStartDate = toCopy.hasResetStartDate();
            resetEndDate = toCopy.hasResetEndDate();
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.tags, this.startDate, this.endDate)
                    || resetStartDate || resetEndDate;
        }

        public Optional<Calendar> getStartDate() {
            return Optional.ofNullable(startDate);
        }

        public Optional<Calendar> getEndDate() {
            return Optional.ofNullable(endDate);
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public Optional<Set<Tag>> getTags() {
            return Optional.ofNullable(tags);
        }

        public boolean hasResetStartDate() {
            return resetStartDate;
        }

        public boolean hasResetEndDate() {
            return resetEndDate;
        }

        public void setTags(Set<Tag> tags) {
            this.tags = tags;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public void setStartDate(Calendar startDate) {
            this.startDate = startDate;
        }

        public void setEndDate(Calendar endDate) {
            this.endDate = endDate;
        }

        public void setResetStartDate(boolean value) {
            this.resetStartDate = value;
        }

        public void setResetEndDate(boolean value) {
            this.resetEndDate = value;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditEntryDescriptor)) {
                return false;
            }

            // state check
            EditEntryDescriptor e = (EditEntryDescriptor) other;
            return getName().equals(e.getName()) && getTags().equals(e.getTags())
                   && getStartDate().equals(e.getStartDate()) && getEndDate().equals(e.getEndDate())
                   && (hasResetStartDate() == e.hasResetStartDate()) && (hasResetEndDate() == e.hasResetEndDate());
        }
    }

    @Override
    public void setData(Model model, CommandHistory history) {
        requireNonNull(model);
        requireNonNull(history);
        this.model = model;
        this.history = history;
    }

}
