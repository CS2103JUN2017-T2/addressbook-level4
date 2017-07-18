package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import seedu.multitasky.commons.core.EventsCenter;
import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.commons.events.ui.ListTypeUpdateEvent;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.util.Comparators;

// @@author A0125586X
/**
 * Lists all entries in the entry book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists entries in the active/archive/bin list"
            + ", filtered by an optional starting and ending date. The entries can also be shown in "
            + "different sorted orders." + "\n"
            + "Format: " + COMMAND_WORD
            + " [" + CliSyntax.PREFIX_ARCHIVE + " | " + CliSyntax.PREFIX_BIN + "]"
            + " [" + CliSyntax.PREFIX_UPCOMING + " | " + CliSyntax.PREFIX_REVERSE + "]"
            + " [" + CliSyntax.PREFIX_FROM + " start date]" + " [" + CliSyntax.PREFIX_TO + "end date]" + "\n"
            + "Example: " + COMMAND_WORD + " " + CliSyntax.PREFIX_UPCOMING;

    public static final String MESSAGE_ALL_SUCCESS = "Listed all entries";

    public static final String MESSAGE_ACTIVE_SUCCESS = "Listed all active entries";

    public static final String MESSAGE_ARCHIVE_SUCCESS = "Listed all entries in the archive";

    public static final String MESSAGE_BIN_SUCCESS = "Listed all entries in the bin";

    public static final String MESSAGE_DEFAULT_ORDER = "in default order";

    public static final String MESSAGE_REVERSE_ORDER = "in reverse order";

    public static final String MESSAGE_UPCOMING_ORDER = "in upcoming order";

    public static final String[] VALID_PREFIXES = { CliSyntax.PREFIX_ARCHIVE.toString(),
                                                    CliSyntax.PREFIX_BIN.toString(),
                                                    CliSyntax.PREFIX_ALL.toString(),
                                                    CliSyntax.PREFIX_UPCOMING.toString(),
                                                    CliSyntax.PREFIX_REVERSE.toString(),
                                                    CliSyntax.PREFIX_FROM.toString(),
                                                    CliSyntax.PREFIX_TO.toString() };

    public enum ShowType { ACTIVE, ARCHIVE, BIN, ALL }

    public enum Ordering { DEFAULT, REVERSE, UPCOMING }

    private static final HashSet<String> MATCH_ALL_KEYWORDS = new HashSet<>();

    private ShowType showType;
    private Ordering ordering;

    private Calendar startDate;
    private Calendar endDate;

    public ListCommand() {
        showType = ShowType.ACTIVE;
        ordering = Ordering.DEFAULT;
    }

    public ListCommand(Calendar startDate, Calendar endDate, ArrayList<String> prefixes) {
        this.startDate = startDate;
        this.endDate = endDate;

        if (prefixes.contains(CliSyntax.PREFIX_ARCHIVE.toString())) {
            showType = ShowType.ARCHIVE;
        } else if (prefixes.contains(CliSyntax.PREFIX_BIN.toString())) {
            showType = ShowType.BIN;
        } else if (prefixes.contains(CliSyntax.PREFIX_ALL.toString())) {
            showType = ShowType.ALL;
        } else {
            showType = ShowType.ACTIVE;
        }

        if (prefixes.contains(CliSyntax.PREFIX_REVERSE.toString())) {
            ordering = Ordering.REVERSE;
        } else if (prefixes.contains(CliSyntax.PREFIX_UPCOMING.toString())) {
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
            model.updateAllFilteredLists(MATCH_ALL_KEYWORDS, startDate, endDate, Entry.State.ARCHIVED,
                                         Model.LENIENT_SEARCHES);
            raise(new ListTypeUpdateEvent(Entry.State.ARCHIVED));
            history.setPrevSearch(MATCH_ALL_KEYWORDS, startDate, endDate, Entry.State.ARCHIVED,
                                  Model.LENIENT_SEARCHES);
            break;
        case BIN:
            commandResultBuilder.append(MESSAGE_BIN_SUCCESS);
            model.updateAllFilteredLists(MATCH_ALL_KEYWORDS, startDate, endDate, Entry.State.DELETED,
                                         Model.LENIENT_SEARCHES);
            raise(new ListTypeUpdateEvent(Entry.State.DELETED));
            history.setPrevSearch(MATCH_ALL_KEYWORDS, startDate, endDate, Entry.State.DELETED,
                                  Model.LENIENT_SEARCHES);
            break;
        case ACTIVE:
            commandResultBuilder.append(MESSAGE_ACTIVE_SUCCESS);
            model.updateAllFilteredLists(MATCH_ALL_KEYWORDS, startDate, endDate, Entry.State.ACTIVE,
                                         Model.LENIENT_SEARCHES);
            raise(new ListTypeUpdateEvent(Entry.State.ACTIVE));
            history.setPrevSearch(MATCH_ALL_KEYWORDS, startDate, endDate, Entry.State.ACTIVE,
                                  Model.LENIENT_SEARCHES);
            break;
        case ALL:
            commandResultBuilder.append(MESSAGE_ALL_SUCCESS);
            model.updateAllFilteredLists(MATCH_ALL_KEYWORDS, startDate, endDate, (Entry.State) null,
                                         Model.LENIENT_SEARCHES);
            raise(new ListTypeUpdateEvent(null));
            history.setPrevSearch(MATCH_ALL_KEYWORDS, startDate, endDate, null,
                                  Model.LENIENT_SEARCHES);
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

    private void raise(BaseEvent event) {
        EventsCenter.getInstance().post(event);
    }

    @Override
    public void setData(Model model, CommandHistory history) {
        requireNonNull(model);
        requireNonNull(history);
        this.model = model;
        this.history = history;
    }

}
