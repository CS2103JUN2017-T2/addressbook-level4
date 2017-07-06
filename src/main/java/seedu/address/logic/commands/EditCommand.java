package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Name;
import seedu.address.model.entry.ReadOnlyEntry;
import seedu.address.model.entry.exceptions.EntryNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing entry in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the entry identified "
            + "by the index number used in the last entry listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "

            + "[" + PREFIX_TAG + "TAG]...\n" + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_EDIT_ENTRY_SUCCESS = "Edited Entry: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_ENTRY = "This entry already exists in the address book.";

    private final Index index;
    private final EditEntryDescriptor editEntryDescriptor;

    /**
     * @param index
     *            of the entry in the filtered entry list to edit
     * @param editEntryDescriptor
     *            details to edit the entry with
     */
    public EditCommand(Index index, EditEntryDescriptor editEntryDescriptor) {
        requireNonNull(index);
        requireNonNull(editEntryDescriptor);

        this.index = index;
        this.editEntryDescriptor = new EditEntryDescriptor(editEntryDescriptor);
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyEntry> lastShownList = model.getFilteredFloatingTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        ReadOnlyEntry entryToEdit = lastShownList.get(index.getZeroBased());
        Entry editedEntry = createEditedEntry(entryToEdit, editEntryDescriptor);

        try {
            model.updateEntry(entryToEdit, editedEntry);
        } catch (EntryNotFoundException pnfe) {
            throw new AssertionError("The target entry cannot be missing");
        }
        model.updateFilteredListToShowAll();
        return new CommandResult(String.format(MESSAGE_EDIT_ENTRY_SUCCESS, entryToEdit));
    }

    /**
     * Creates and returns a {@code Entry} with the details of
     * {@code entryToEdit} edited with {@code editEntryDescriptor}.
     */
    private static Entry createEditedEntry(ReadOnlyEntry entryToEdit, EditEntryDescriptor editEntryDescriptor) {
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
        return index.equals(e.index) && editEntryDescriptor.equals(e.editEntryDescriptor);
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
