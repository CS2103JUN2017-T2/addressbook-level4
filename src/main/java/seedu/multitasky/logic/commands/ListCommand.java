package seedu.multitasky.logic.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.util.Comparators;

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

    public static final String MESSAGE_ARCHIVE_SUCCESS = "Listed all entries in the archive";

    public static final String MESSAGE_BIN_SUCCESS = "Listed all entries in the bin";

    public static final String MESSAGE_DEFAULT_ORDER = "in default order";

    public static final String MESSAGE_REVERSE_ORDER = "in reverse order";

    public static final String MESSAGE_UPCOMING_ORDER = "in upcoming order";

    public static final String[] VALID_PREFIXES = {CliSyntax.PREFIX_ARCHIVE.toString(),
                                                   CliSyntax.PREFIX_BIN.toString(),
                                                   CliSyntax.PREFIX_UPCOMING.toString(),
                                                   CliSyntax.PREFIX_REVERSE.toString(),
                                                   CliSyntax.PREFIX_FROM.toString(),
                                                   CliSyntax.PREFIX_TO.toString()};

    public enum ShowType {
        ACTIVE, ARCHIVE, BIN
    }

    public enum Ordering {
        DEFAULT, REVERSE, UPCOMING
    }

    private static final Set<String> KEYWORD_SHOW_ALL;

    private ShowType showType;
    private Ordering ordering;

    static {
        KEYWORD_SHOW_ALL = new HashSet<>();
        KEYWORD_SHOW_ALL.add("");
    }

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

        if (flagList.contains(CliSyntax.PREFIX_REVERSE.toString())) {
            ordering = Ordering.REVERSE;
        } else if (flagList.contains(CliSyntax.PREFIX_UPCOMING.toString())) {
            ordering = Ordering.UPCOMING;
        } else {
            ordering = Ordering.DEFAULT;
        }

    }

    @Override
    public CommandResult execute() {
        StringBuilder commandResultBuilder = new StringBuilder();
        switch (showType) {
        case ARCHIVE:
            commandResultBuilder.append(MESSAGE_ARCHIVE_SUCCESS);
            model.updateFilteredEventList(KEYWORD_SHOW_ALL, Entry.State.ARCHIVED);
            model.updateFilteredDeadlineList(KEYWORD_SHOW_ALL, Entry.State.ARCHIVED);
            model.updateFilteredFloatingTaskList(KEYWORD_SHOW_ALL, Entry.State.ARCHIVED);
            break;
        case BIN:
            commandResultBuilder.append(MESSAGE_BIN_SUCCESS);
            model.updateFilteredEventList(KEYWORD_SHOW_ALL, Entry.State.DELETED);
            model.updateFilteredDeadlineList(KEYWORD_SHOW_ALL, Entry.State.DELETED);
            model.updateFilteredFloatingTaskList(KEYWORD_SHOW_ALL, Entry.State.DELETED);
            break;
        case ACTIVE:
            commandResultBuilder.append(MESSAGE_ACTIVE_SUCCESS);
            model.updateFilteredEventList(KEYWORD_SHOW_ALL, Entry.State.ACTIVE);
            model.updateFilteredDeadlineList(KEYWORD_SHOW_ALL, Entry.State.ACTIVE);
            model.updateFilteredFloatingTaskList(KEYWORD_SHOW_ALL, Entry.State.ACTIVE);
            break;
        default:
            throw new AssertionError("Unknown list show type");
        }

        switch (ordering) {
        case REVERSE:
            model.updateSortingComparators(Comparators.EVENT_REVERSE, Comparators.DEADLINE_REVERSE,
                                           Comparators.FLOATING_TASK_REVERSE);
            commandResultBuilder.append(" ").append(MESSAGE_REVERSE_ORDER);
            return new CommandResult(commandResultBuilder.toString());
        case UPCOMING:
            model.updateSortingComparators(Comparators.EVENT_UPCOMING, Comparators.DEADLINE_UPCOMING,
                                           Comparators.FLOATING_TASK_UPCOMING);
            commandResultBuilder.append(" ").append(MESSAGE_UPCOMING_ORDER);
            return new CommandResult(commandResultBuilder.toString());
        case DEFAULT:
            model.updateSortingComparators(Comparators.EVENT_DEFAULT, Comparators.DEADLINE_DEFAULT,
                                           Comparators.FLOATING_TASK_DEFAULT);
            return new CommandResult(commandResultBuilder.toString());
        default:
            throw new AssertionError("Unknown list command ordering type");
        }
    }

}
