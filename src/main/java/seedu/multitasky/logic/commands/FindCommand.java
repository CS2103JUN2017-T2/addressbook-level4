package seedu.multitasky.logic.commands;

import java.util.Set;

/**
 * Finds and lists all entries in entry book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all entries whose names/tags contain any "
            + "of the specified keywords (non case-sensitive) and displays them as a list with index numbers.\n"
            + "Format: " + COMMAND_WORD + " KEYWORD [MORE_KEYWORDS]...\n";

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() {

        // update all 3 lists with new keywords.
        model.updateFilteredDeadlineList(keywords);
        model.updateFilteredEventList(keywords);
        model.updateFilteredFloatingTaskList(keywords);

        // get size of each lists for printing.
        int deadlineSize = model.getFilteredDeadlineList().size();
        int eventSize = model.getFilteredEventList().size();
        int floatingSize = model.getFilteredFloatingTaskList().size();
        return new CommandResult(getMessageForEntryListShownSummary(deadlineSize + eventSize + floatingSize));
    }

    public Set<String> getKeywords() {
        return keywords;
    }

}
