package seedu.multitasky.logic.commands;

import java.util.Set;

/**
 * Finds and lists all entries in entry book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all entries whose names/tags contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Format: " + COMMAND_WORD + " KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " science lecture";

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredFloatingTaskList(keywords);
        return new CommandResult(getMessageForEntryListShownSummary(model.getFilteredFloatingTaskList().size()));
    }

    public Set<String> getKeywords() {
        return keywords;
    }
}
