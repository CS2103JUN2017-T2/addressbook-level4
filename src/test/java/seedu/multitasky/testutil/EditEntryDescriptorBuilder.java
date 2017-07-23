package seedu.multitasky.testutil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.logic.parser.ParserUtil;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.ReadOnlyEntry;

// @@author A0126623L
/**
 * A utility class to help with building EditEntryDescriptor objects.
 */
public class EditEntryDescriptorBuilder {

    private EditEntryDescriptor descriptor;

    public EditEntryDescriptorBuilder() {
        descriptor = new EditEntryDescriptor();
    }

    public EditEntryDescriptorBuilder(EditEntryDescriptor descriptor) {
        this.descriptor = new EditEntryDescriptor(descriptor);
    }

    // @@author A0140633R
    /**
     * Returns an {@code EditEntryDescriptor} with fields containing {@code entry}'s details
     */
    public EditEntryDescriptorBuilder(ReadOnlyEntry entry) throws IllegalValueException {
        descriptor = new EditEntryDescriptor();
        descriptor.setName(entry.getName());
        descriptor.setTags(entry.getTags());
        if (entry instanceof Deadline) {
            descriptor.setEndDate(entry.getEndDateAndTime());
        } else if (entry instanceof Event) {
            descriptor.setStartDate(entry.getStartDateAndTime());
            descriptor.setEndDate(entry.getEndDateAndTime());
        }
    }

    public EditEntryDescriptorBuilder withStartDate(String startDate) throws IllegalValueException {
        ParserUtil.parseDate(Optional.of(startDate)).ifPresent(descriptor::setStartDate);
        return this;
    }

    public EditEntryDescriptorBuilder withStartDate(Calendar startDate) throws IllegalValueException {
        descriptor.setStartDate(startDate);
        return this;
    }

    public EditEntryDescriptorBuilder withEndDate(String endDate) throws IllegalValueException {
        ParserUtil.parseDate(Optional.of(endDate)).ifPresent(descriptor::setEndDate);
        return this;
    }

    public EditEntryDescriptorBuilder withEndDate(Calendar endDate) throws IllegalValueException {
        descriptor.setEndDate(endDate);
        return this;
    }

    public EditEntryDescriptorBuilder withName(String name) throws IllegalValueException {
        ParserUtil.parseName(Optional.of(name)).ifPresent(descriptor::setName);
        return this;
    }

    public EditEntryDescriptorBuilder withTags(String... tags) throws IllegalValueException {
        descriptor.setTags(ParserUtil.parseTags(Arrays.asList(tags)));
        return this;
    }

    public EditEntryDescriptorBuilder withAddTags(String... tags) throws IllegalValueException {
        descriptor.setAddTags(ParserUtil.parseTags(Arrays.asList(tags)));
        return this;
    }

    public EditEntryDescriptorBuilder withResetStartDate() throws IllegalValueException {
        descriptor.setResetStartDate(true);
        return this;
    }

    public EditEntryDescriptorBuilder withResetEndDate() throws IllegalValueException {
        descriptor.setResetEndDate(true);
        return this;
    }

    public EditEntryDescriptor build() {
        return descriptor;
    }
}
