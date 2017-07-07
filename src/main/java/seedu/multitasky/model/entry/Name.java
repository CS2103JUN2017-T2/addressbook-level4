package seedu.multitasky.model.entry;

import static java.util.Objects.requireNonNull;

import seedu.multitasky.commons.exceptions.IllegalValueException;

/**
 * Represents an Entry's name in the entry book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_NAME_CONSTRAINTS = "Entry names should only start with "
                                                          + "alphanumeric characters and it should "
                                                          + "not be blank, subsequent spaces and "
                                                          + "non-alphanumeric characters are allowed.";

    /*
     * The first character of the entry must not be a whitespace, otherwise " " (a blank string)
     * becomes a valid input.
     */
    // public static final String NAME_VALIDATION_REGEX =
    // "[\\p{Alnum}][\\p{Alnum} ]*";
    public static final String NAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum}\\p{S}\\p{P} ]*";

    public final String fullName;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public Name(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!isValidName(trimmedName)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.fullName = trimmedName;
    }

    /**
     * Returns true if a given string is a valid entry name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
               || (other instanceof Name // instanceof handles nulls
                   && this.fullName.equals(((Name) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
