package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FLOATINGTASK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Name;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.tag.Tag;

//@@author A0140633R
/**
 * Abstract class that contains all of the utility methods used for EditCommand sub-types.
 */
public abstract class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " : Edits the details of the entry either identified "
            + "by keywords given or the index number used in the last entry listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Format: " + COMMAND_WORD + " KEYWORDS or "
            + PREFIX_FLOATINGTASK + " INDEX (must be a positive integer) "
            + PREFIX_NAME + " NEW NAME "
            + PREFIX_TAG + " NEWTAGS\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_FLOATINGTASK + " 1 "
            + PREFIX_NAME + "walk the dog " + PREFIX_TAG + "\n"
            + "tip: this example clears all tags on the task!";

    public static final String MESSAGE_SUCCESS = "Entry edited:" + "\n"
            + Messages.MESSAGE_ENTRY_DESCRIPTION +  "%1$s";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.\n"
            + "Format: " + COMMAND_WORD + " [keywords] or " + PREFIX_FLOATINGTASK + " INDEX "
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
    protected static Entry createEditedEntry(ReadOnlyEntry entryToEdit, EditEntryDescriptor editEntryDescriptor) {
        assert entryToEdit != null;

        Name updatedName = editEntryDescriptor.getName().orElse(entryToEdit.getName());
        Set<Tag> updatedTags = editEntryDescriptor.getTags().orElse(entryToEdit.getTags());

        return new Entry(updatedName, updatedTags);
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

        public EditEntryDescriptor() {
        }

        public EditEntryDescriptor(EditEntryDescriptor toCopy) {
            this.name = toCopy.name;
            this.tags = toCopy.tags;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setTags(Set<Tag> tags) {
            this.tags = tags;
        }

        public Optional<Set<Tag>> getTags() {
            return Optional.ofNullable(tags);
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

            return getName().equals(e.getName()) && getTags().equals(e.getTags());
        }
    }
}
