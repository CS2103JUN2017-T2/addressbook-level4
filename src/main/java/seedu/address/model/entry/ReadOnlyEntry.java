package seedu.address.model.entry;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * A read-only immutable interface for a Entry in the entrybook. Implementations
 * should guarantee: details are present and not null, field values are
 * validated.
 */
public interface ReadOnlyEntry {

	Name getName();

	Set<Tag> getTags();

	/**
	 * Returns true if both have the same state. (interfaces cannot override
	 * .equals)
	 */
	default boolean isSameStateAs(ReadOnlyEntry other) {
		return other == this // short circuit if same object
				|| (other != null // this is first to avoid NPE below
						&& other.getName().equals(this.getName())); // state
																	// checks
																	// here
																	// onwards
	}

	/**
	 * Formats the entry as text, showing all contact details.
	 */
	default String getAsText() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getName()).append(" Tags: ");
		getTags().forEach(builder::append);
		return builder.toString();
	}

}
