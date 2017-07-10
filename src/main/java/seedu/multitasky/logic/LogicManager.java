package seedu.multitasky.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.multitasky.commons.core.ComponentManager;
import seedu.multitasky.commons.core.LogsCenter;
import seedu.multitasky.logic.commands.Command;
import seedu.multitasky.logic.commands.CommandResult;
import seedu.multitasky.logic.commands.exceptions.CommandException;
import seedu.multitasky.logic.parser.Parser;
import seedu.multitasky.logic.parser.exceptions.ParseException;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.exceptions.DuplicateEntryException;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final Parser parser;

    public LogicManager(Model model) {
        this.model = model;
        this.history = new CommandHistory();
        this.parser = new Parser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException, DuplicateEntryException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = parser.parseCommand(commandText);
            command.setData(model, history);
            return command.execute();
        } finally {
            history.add(commandText);
        }
    }

    @Override
    public ObservableList<ReadOnlyEntry> getFilteredFloatingTaskList() {
        return model.getFilteredFloatingTaskList();
    }

    @Override
    public ObservableList<ReadOnlyEntry> getFilteredDeadlineList() {
        return model.getFilteredDeadlineList();
    }

    @Override
    public ObservableList<ReadOnlyEntry> getFilteredEventList() {
        return model.getFilteredEventList();
    }
}
