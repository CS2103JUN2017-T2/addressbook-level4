package seedu.multitasky.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.multitasky.logic.CommandHistory;
import seedu.multitasky.model.Model;

/**
 * Lists all the commands entered by user from the start of app launch, shown from latest to earliest.
 */
public class HistoryCommand extends Command {

    public static final String COMMAND_WORD = "history";
    public static final String MESSAGE_SUCCESS = "Entered commands (from most recent to earliest):\n%1$s";
    public static final String MESSAGE_NO_HISTORY = "You have not yet entered any commands.";

    public static final String[] VALID_PREFIXES = {};

    @Override
    public CommandResult execute() {
        List<String> previousCommands = history.getHistory();

        if (previousCommands.isEmpty()) {
            return new CommandResult(MESSAGE_NO_HISTORY);
        }

        List<String> reversedHistory = new ArrayList<>(previousCommands);
        Collections.reverse(reversedHistory);
        return new CommandResult(String.format(MESSAGE_SUCCESS, String.join("\n", reversedHistory)));
    }

    @Override
    public void setData(Model model, CommandHistory history) {
        requireNonNull(history);
        this.history = history;
    }

}
