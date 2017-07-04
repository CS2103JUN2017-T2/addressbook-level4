package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE_BY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Adds a person to the entry book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";
    
    //@@kevinlamkb A0140633R
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an entry to the task manager. "
            + "Parameters: "
            + "NAME "
            + PREFIX_START_DATE_BY + "DDMMYY HHMM "
            + "[" + PREFIX_TAG + " TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + "dinner with parents "
            + PREFIX_START_DATE_BY + "230517 2400 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New entry added: %1$s";

    private final Entry toAdd;

    /**
     * Creates an AddCommand to add the specified {@code ReadOnlyPerson}
     */
    public AddCommand(ReadOnlyEntry entry) {
        toAdd = new Entry(entry);
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);
        try {
            model.addEntry(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } 
    }

}
