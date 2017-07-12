package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.multitasky.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

import seedu.multitasky.commons.core.Messages;
import seedu.multitasky.commons.util.CollectionUtil;
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

    public static final String MESSAGE_USAGE = COMMAND_WORD
                                               + " : Edits the details of the entry either identified "
                                               + "by keywords given or the index number used in the last"
                                               + " entry listing. Existing values will be overwritten by "
                                               + "the input values.\n" + "Format: " + COMMAND_WORD
                                               + " KEYWORDS or " + PREFIX_FLOATINGTASK + " INDEX "
                                               + PREFIX_NAME + " NEW NAME " + PREFIX_TAG + " NEWTAGS\n"
                                               + "Example: " + COMMAND_WORD + " " + PREFIX_FLOATINGTASK
                                               + " 1 " + PREFIX_NAME + " walk the dog " + PREFIX_TAG + "\n"
                                               + "tip: this example clears all tags on the task!";

    public static final String MESSAGE_SUCCESS = "Entry edited:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.\n"
                                                    + "Format: " + COMMAND_WORD + " [keywords] or "
                                                    + PREFIX_FLOATINGTASK + " INDEX "
                                                    + PREFIX_NAME + " NEW NAME " + PREFIX_TAG + " NEWTAGS";

    public static final String MESSAGE_DUPLICATE_ENTRY = "This entry already exists in the address book.";

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
                                             EditEntryDescriptor editEntryDescriptor) {
        assert entryToEdit != null;

        Name updatedName = editEntryDescriptor.getName().orElse(entryToEdit.getName());
        Set<Tag> updatedTags = editEntryDescriptor.getTags().orElse(entryToEdit.getTags());

        // TODO (entryToEdit.getEndDateAndTime()) != null
        if (!editEntryDescriptor.getEndDate().isPresent() && entryToEdit.getEndDateAndTime() == null) {
            return new FloatingTask(updatedName, updatedTags);
        } else if (!editEntryDescriptor.getStartDate().isPresent() && entryToEdit.getStartDateAndTime() == null) {
            Calendar updatedEndDate = editEntryDescriptor.getEndDate().orElse(entryToEdit.getEndDateAndTime());
            return new Deadline(updatedName, updatedEndDate, updatedTags);
        } else {
            Calendar updatedStartDate = editEntryDescriptor.getStartDate().orElse(entryToEdit.getStartDateAndTime());
            Calendar updatedEndDate = editEntryDescriptor.getEndDate().orElse(entryToEdit.getEndDateAndTime());
            return new Event(updatedName, updatedStartDate, updatedEndDate, updatedTags);
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
        private Name name = null;
        private Set<Tag> tags = null;
        private Calendar startDate = null;
        private Calendar endDate = null;

        public EditEntryDescriptor() {
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
            return CollectionUtil.isAnyNonNull(this.name, this.tags);
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
