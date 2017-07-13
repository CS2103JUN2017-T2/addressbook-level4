package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.util.CollectionUtil;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.CliSyntax;
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

    public static final String MESSAGE_SUCCESS = "Entry edited:" + "\n" + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s";

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

        if (updatedStartDate == null && updatedEndDate == null) {
            return new FloatingTask(updatedName, updatedTags);
        } else if (updatedStartDate == null && updatedEndDate != null) {
            return new Deadline(updatedName, updatedEndDate, updatedTags);
        } else if (updatedStartDate != null && updatedEndDate != null) {
            if (updatedEndDate.compareTo(updatedStartDate) < 0) {
                throw new CommandException("Can not have end date before start date!");
            }
            return new Event(updatedName, updatedStartDate, updatedEndDate, updatedTags);
        } else {
            assert false : "Cannot edit to entry that is not float, deadline or event.";
            return null;
        }
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
     */
    public static class EditEntryDescriptor {
        private Name name;
        private Set<Tag> tags;
        private Calendar startDate;
        private Calendar endDate;

        public EditEntryDescriptor() {
            name = null;
            tags = null;
            startDate = null;
            endDate = null;
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
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.tags, this.startDate, this.endDate);
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
                   && getStartDate().equals(e.getStartDate()) && getEndDate().equals(e.getEndDate());
        }
    }

}
