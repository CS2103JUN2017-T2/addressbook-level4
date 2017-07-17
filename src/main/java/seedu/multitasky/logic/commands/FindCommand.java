package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

import seedu.multitasky.commons.core.EventsCenter;
import seedu.multitasky.commons.events.BaseEvent;
import seedu.multitasky.commons.events.ui.ListTypeUpdateEvent;
import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.logic.parser.CliSyntax;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.Entry;

/**
 * Finds and lists all entries in entry book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all entries whose names/tags contain any "
            + "of the specified keywords (non case-sensitive) and displays them as a list with index numbers.\n"
            + "Format: " + COMMAND_WORD + " KEYWORD [MORE_KEYWORDS]..."
            + " [" + CliSyntax.PREFIX_ARCHIVE + " | " + CliSyntax.PREFIX_BIN + "]"
            + " [" + CliSyntax.PREFIX_FROM + " START_DATE]" + " [" + CliSyntax.PREFIX_TO + " END_DATE]" + "\n";

    public static final String[] VALID_PREFIXES = {CliSyntax.PREFIX_ARCHIVE.toString(),
                                                   CliSyntax.PREFIX_BIN.toString(),
                                                   CliSyntax.PREFIX_FROM.toString(),
                                                   CliSyntax.PREFIX_TO.toString()};

    public enum FindType { ACTIVE, ARCHIVE, BIN }

    private final Set<String> keywords;
    private FindType findType;

    private Calendar startDate;
    private Calendar endDate;

    public FindCommand(Set<String> keywords, Calendar startDate, Calendar endDate, ArrayList<String> prefixes) {
        this.keywords = keywords;
        this.startDate = startDate;
        this.endDate = endDate;

        if (prefixes.contains(CliSyntax.PREFIX_ARCHIVE.toString())) {
            findType = FindType.ARCHIVE;
        } else if (prefixes.contains(CliSyntax.PREFIX_BIN.toString())) {
            findType = FindType.BIN;
        } else {
            findType = FindType.ACTIVE;
        }
    }

    @Override
    public CommandResult execute() {
        Entry.State state;
        switch (findType) {
        case ARCHIVE:
            state = Entry.State.ARCHIVED;
            break;
        case BIN:
            state = Entry.State.DELETED;
            break;
        default:
            state = Entry.State.ACTIVE;
        }
        raise(new ListTypeUpdateEvent(state));
        // Update all 3 lists with new search parameters until at least 1 result is found.
        model.updateAllFilteredLists(keywords, startDate, endDate, state);
        // Save parameters of the search
        history.setPrevSearch(keywords, startDate, endDate, state);

        int deadlineSize = model.getFilteredDeadlineList().size();
        int eventSize = model.getFilteredEventList().size();
        int floatingSize = model.getFilteredFloatingTaskList().size();

        return new CommandResult(getMessageForEntryListShownSummary(deadlineSize + eventSize + floatingSize));
    }

    private void raise(BaseEvent event) {
        EventsCenter.getInstance().post(event);
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    @Override
    public void setData(Model model, CommandHistory history) {
        requireNonNull(model);
        requireNonNull(history);
        this.model = model;
        this.history = history;
    }

}
