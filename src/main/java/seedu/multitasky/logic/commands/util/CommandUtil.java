package seedu.multitasky.logic.commands.util;

import java.util.HashMap;
import java.util.Map;

import seedu.multitasky.logic.commands.AddCommand;
import seedu.multitasky.logic.commands.ClearCommand;
import seedu.multitasky.logic.commands.CompleteCommand;
import seedu.multitasky.logic.commands.DeleteCommand;
import seedu.multitasky.logic.commands.EditCommand;
import seedu.multitasky.logic.commands.ExitCommand;
import seedu.multitasky.logic.commands.FindCommand;
import seedu.multitasky.logic.commands.HelpCommand;
import seedu.multitasky.logic.commands.HistoryCommand;
import seedu.multitasky.logic.commands.ListCommand;
import seedu.multitasky.logic.commands.OpenCommand;
import seedu.multitasky.logic.commands.RedoCommand;
import seedu.multitasky.logic.commands.RestoreCommand;
import seedu.multitasky.logic.commands.SaveCommand;
import seedu.multitasky.logic.commands.UndoCommand;

public class CommandUtil {
    public static final String[] COMMAND_WORDS = new String[] {
        AddCommand.COMMAND_WORD,
        ClearCommand.COMMAND_WORD,
        CompleteCommand.COMMAND_WORD,
        DeleteCommand.COMMAND_WORD,
        EditCommand.COMMAND_WORD,
        ExitCommand.COMMAND_WORD,
        FindCommand.COMMAND_WORD,
        HelpCommand.COMMAND_WORD,
        HistoryCommand.COMMAND_WORD,
        ListCommand.COMMAND_WORD,
        OpenCommand.COMMAND_WORD,
        RedoCommand.COMMAND_WORD,
        RestoreCommand.COMMAND_WORD,
        SaveCommand.COMMAND_WORD,
        UndoCommand.COMMAND_WORD
    };

    public static final Map<String, String[]> COMMAND_KEYWORDS;

    public static final String[] PREFIX_ONLY_COMMANDS = {
        ClearCommand.COMMAND_WORD,
        ExitCommand.COMMAND_WORD,
        HelpCommand.COMMAND_WORD,
        HistoryCommand.COMMAND_WORD,
        ListCommand.COMMAND_WORD,
        RedoCommand.COMMAND_WORD,
        UndoCommand.COMMAND_WORD
    };

    static {
        COMMAND_KEYWORDS = new HashMap<>();
        COMMAND_KEYWORDS.put(AddCommand.COMMAND_WORD, AddCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(ClearCommand.COMMAND_WORD, ClearCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(CompleteCommand.COMMAND_WORD, CompleteCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(DeleteCommand.COMMAND_WORD, DeleteCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(EditCommand.COMMAND_WORD, EditCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(ExitCommand.COMMAND_WORD, ExitCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(FindCommand.COMMAND_WORD, FindCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(HelpCommand.COMMAND_WORD, HelpCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(HistoryCommand.COMMAND_WORD, HistoryCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(ListCommand.COMMAND_WORD, ListCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(OpenCommand.COMMAND_WORD, OpenCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(RedoCommand.COMMAND_WORD, RedoCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(RestoreCommand.COMMAND_WORD, RestoreCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(SaveCommand.COMMAND_WORD, SaveCommand.VALID_PREFIXES);
        COMMAND_KEYWORDS.put(UndoCommand.COMMAND_WORD, UndoCommand.VALID_PREFIXES);
    }

}
