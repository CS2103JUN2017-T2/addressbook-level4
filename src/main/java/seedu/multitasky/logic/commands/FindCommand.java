package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Set;

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
            + "Format: " + COMMAND_WORD + " KEYWORD [MORE_KEYWORDS]...\n";

    public static final String[] VALID_PREFIXES = {CliSyntax.PREFIX_ARCHIVE.toString(),
                                                   CliSyntax.PREFIX_BIN.toString()};

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() {

        // update all 3 lists with new keywords.
        model.updateFilteredDeadlineList(keywords, Entry.State.ACTIVE);
        model.updateFilteredEventList(keywords, Entry.State.ACTIVE);
        model.updateFilteredFloatingTaskList(keywords, Entry.State.ACTIVE);

        // get size of each lists for printing.
        int deadlineSize = model.getFilteredDeadlineList().size();
        int eventSize = model.getFilteredEventList().size();
        int floatingSize = model.getFilteredFloatingTaskList().size();

        // save keywords of the search
        history.setPrevSearch(keywords, Entry.State.ACTIVE);
        return new CommandResult(getMessageForEntryListShownSummary(deadlineSize + eventSize + floatingSize));
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
