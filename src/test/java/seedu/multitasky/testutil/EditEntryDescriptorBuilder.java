package seedu.multitasky.testutil;

import java.util.Arrays;
import java.util.Optional;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.logic.parser.ParserUtil;
import seedu.multitasky.model.entry.ReadOnlyEntry;

//@@author A0126623L
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

    /**
     * Returns an {@code EditEntryDescriptor} with fields containing {@code entry}'s details
     */
    public EditEntryDescriptorBuilder(ReadOnlyEntry entry) throws IllegalValueException {
        descriptor = new EditEntryDescriptor();
        descriptor.setName(entry.getName());
        descriptor.setTags(entry.getTags());
    }

    public EditEntryDescriptorBuilder withName(String name) throws IllegalValueException {
        ParserUtil.parseName(Optional.of(name)).ifPresent(descriptor::setName);
        return this;
    }

    public EditEntryDescriptorBuilder withTags(String... tags) throws IllegalValueException {
        descriptor.setTags(ParserUtil.parseTags(Arrays.asList(tags)));
        return this;
    }

    public EditEntryDescriptor build() {
        return descriptor;
    }
}
