package seedu.multitasky.logic.commands;

import java.util.ArrayList;
import java.util.Arrays;

import seedu.multitasky.logic.parser.CliSyntax;

// @@author A0125586X
/**
 * Lists all entries in the entry book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists entries in the entry book. " + "\n"
                                               + "Format: " + COMMAND_WORD
                                               + " [" + "[" + CliSyntax.PREFIX_ARCHIVE + "]" + " |"
                                               + " [" + CliSyntax.PREFIX_BIN + "]" + "]"
                                               + " [" + CliSyntax.PREFIX_UPCOMING + "]"
                                               + " [" + CliSyntax.PREFIX_REVERSE + "]" + "\n"
                                               + "Example: " + COMMAND_WORD + " " + CliSyntax.PREFIX_UPCOMING;

    public static final String MESSAGE_ACTIVE_SUCCESS = "Listed all active entries";

    public static final String MESAGE_UPCOMING_SUCCESS = "Listed all upcoming entries";

    public static final String MESSAGE_REVERSE_SUCCESS = "Listed all entries in reverse";

    public static final String[] PREFIX_LIST = {CliSyntax.PREFIX_ARCHIVE.toString(),
                                                CliSyntax.PREFIX_BIN.toString(),
                                                CliSyntax.PREFIX_UPCOMING.toString(),
                                                CliSyntax.PREFIX_REVERSE.toString()};

    public enum ShowType {
        ACTIVE, ARCHIVE, BIN
    }

    public enum Ordering {
        DEFAULT, UPCOMING, REVERSE
    }

    private ShowType showType;
    private Ordering ordering;

    public ListCommand() {
        showType = ShowType.ACTIVE;
        ordering = Ordering.DEFAULT;
    }

    public ListCommand(ShowType showType, Ordering ordering) {
        this.showType = showType;
        this.ordering = ordering;
    }

    public ListCommand(String... flags) {
        ArrayList<String> flagList = new ArrayList<String>(Arrays.asList(flags));

        if (flagList.contains(CliSyntax.PREFIX_ARCHIVE.toString())) {
            showType = ShowType.ARCHIVE;
        } else if (flagList.contains(CliSyntax.PREFIX_BIN.toString())) {
            showType = ShowType.BIN;
        } else {
            showType = ShowType.ACTIVE;
        }

        if (flagList.contains(CliSyntax.PREFIX_UPCOMING.toString())) {
            ordering = Ordering.UPCOMING;
        } else if (flagList.contains(CliSyntax.PREFIX_REVERSE.toString())) {
            ordering = Ordering.REVERSE;
        } else {
            ordering = Ordering.DEFAULT;
        }

    }

    @Override
    public CommandResult execute() {
        model.updateAllFilteredListToShowAll();
        return new CommandResult(MESSAGE_ACTIVE_SUCCESS);
    }

}
