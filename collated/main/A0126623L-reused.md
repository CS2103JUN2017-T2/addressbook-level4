# A0126623L-reused
###### \java\seedu\multitasky\logic\commands\RestoreByFindCommand.java
``` java
    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {

        List<ReadOnlyEntry> allList = collateArchivedAndDeletedEntries();

        if (allList.size() == 1) { // proceed to restore
            entryToRestore = allList.get(0);

            if (entryToRestore.isActive()) {
                throw new CommandException(RestoreCommand.MESSAGE_ENTRY_ALREADY_ACTIVE);
            }

            CommandResult commandResult = null;

            try {
                model.changeEntryState(entryToRestore, Entry.State.ACTIVE);
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS, entryToRestore));
            } catch (EntryNotFoundException e) {
                assert false : "The target entry cannot be missing";
            } catch (OverlappingEventException oee) {
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_ALERT,
                                                                entryToRestore.getName()));
            } catch (EntryOverdueException e) {
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERDUE_ALERT,
                                                                entryToRestore.getName()));
            } catch (OverlappingAndOverdueEventException e) {
                commandResult = new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_AND_OVERDUE_ALERT,
                                                                entryToRestore.getName()));
            }

            // refresh list view after updating.
            model.updateAllFilteredLists(history.getPrevKeywords(), history.getPrevStartDate(),
                                         history.getPrevEndDate(), history.getPrevState(),
                                         history.getPrevSearches());
            if (commandResult == null) {
                throw new AssertionError("commandResult in RestoreByFindCommand shouldn't be null here.");
            }
            return commandResult;

        } else {
            history.setPrevSearch(keywords, null, null, history.getPrevState());

            if (allList.size() >= 2) { // multiple entries found
                throw new CommandException(MESSAGE_MULTIPLE_ENTRIES);
            } else {
                assert (allList.size() == 0); // no entries found
                throw new CommandException(MESSAGE_NO_ENTRIES);
            }
        }
    }
```
###### \java\seedu\multitasky\logic\commands\RestoreByIndexCommand.java
``` java
/*
* Restores an entry identified using the type of entry followed by displayed index.
*/
public class RestoreByIndexCommand extends RestoreCommand {

    private Index targetIndex;
    private Prefix listIndicatorPrefix;

    public RestoreByIndexCommand(Index targetIndex, Prefix listIndicatorPrefix) {
        this.targetIndex = targetIndex;
        this.listIndicatorPrefix = listIndicatorPrefix;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {
        UnmodifiableObservableList<ReadOnlyEntry> listToRestoreFrom = ParserUtil
                .getListIndicatedByPrefix(model, listIndicatorPrefix);
        Objects.requireNonNull(listToRestoreFrom);

        if (targetIndex.getZeroBased() >= listToRestoreFrom.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        entryToRestore = listToRestoreFrom.get(targetIndex.getZeroBased());

        if (entryToRestore.isActive()) {
            throw new CommandException(RestoreCommand.MESSAGE_ENTRY_ALREADY_ACTIVE);
        }

        try {
            model.changeEntryState(entryToRestore, Entry.State.ACTIVE);;
        } catch (EntryNotFoundException enfe) {
            throw new AssertionError("The target entry cannot be missing");
        } catch (OverlappingEventException oee) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_ALERT,
                                                   entryToRestore.getName()));
        } catch (EntryOverdueException e) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERDUE_ALERT,
                                                   entryToRestore.getName()));
        } catch (OverlappingAndOverdueEventException e) {
            return new CommandResult(String.format(MESSAGE_SUCCESS_WITH_OVERLAP_AND_OVERDUE_ALERT,
                                                   entryToRestore.getName()));
        }

        // refresh list view after updating.
        model.updateAllFilteredLists(history.getPrevKeywords(), history.getPrevStartDate(),
                                     history.getPrevEndDate(), history.getPrevState(),
                                     history.getPrevSearches());

        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToRestore));
    }

}
```
###### \java\seedu\multitasky\logic\commands\RestoreCommand.java
``` java
/*
* Abstract class that represents a restore command. Holds command_word and confirmation messages a restore
* command will be using.
*/
public abstract class RestoreCommand extends Command {

    public static final String COMMAND_WORD = "restore";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " : Restores an archived/deleted entry identified"
            + " by keywords if it is the only entry found, or restores the entry identified by the index number of"
            + " the last archived/deleted entry listing.\n"
            + "Format: " + COMMAND_WORD + " [" + "[" + "KEYWORDS" + "]" + " |"
            + " [" + String.join(" | ", CliSyntax.PREFIX_EVENT.toString(), CliSyntax.PREFIX_DEADLINE.toString(),
            CliSyntax.PREFIX_FLOATINGTASK.toString()) + "]" + " INDEX" + "]" + "\n"
            + "All possible flags for Restore : 'event', 'deadline', 'float'";

    public static final String MESSAGE_SUCCESS = "Entry restored:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
                                                 + "Entry has been restored to active list";

    public static final String MESSAGE_SUCCESS_WITH_OVERLAP_ALERT = "Entry restored:" + "\n"
            + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
            + "Alert: Restored entry overlaps with existing event(s).";
    public static final String MESSAGE_SUCCESS_WITH_OVERDUE_ALERT = "Entry restored:" + "\n"
            + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
            + "Alert: Restored entry is overdue.";
    public static final String MESSAGE_SUCCESS_WITH_OVERLAP_AND_OVERDUE_ALERT = "Entry restored:" + "\n"
            + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
            + "Alert: Restored entry is overdue and overlaps with existing event(s).";

    public static final String MESSAGE_ENTRY_ALREADY_ACTIVE = "The provided entry is already active.";

    public static final String[] VALID_PREFIXES = {CliSyntax.PREFIX_EVENT.toString(),
                                                   CliSyntax.PREFIX_DEADLINE.toString(),
                                                   CliSyntax.PREFIX_FLOATINGTASK.toString()};

    protected ReadOnlyEntry entryToRestore;

    @Override
    public void setData(Model model, CommandHistory history) {
        //TODO: Consider refactoring these to the superclass's setData();
        Objects.requireNonNull(history);
        Objects.requireNonNull(model);
        this.model = model;
        this.history = history;
    }

}
```
###### \java\seedu\multitasky\logic\parser\ClearCommandParser.java
``` java
    private Prefix[] toPrefixArray(String... stringPrefixes) {
        Prefix[] prefixes = new Prefix[stringPrefixes.length];
        for (int i = 0; i < stringPrefixes.length; ++i) {
            prefixes[i] = new Prefix(stringPrefixes[i]);
        }
        return prefixes;
    }
```
###### \java\seedu\multitasky\logic\parser\RestoreCommandParser.java
``` java
/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class RestoreCommandParser {
    private ArgumentMultimap argMultimap;

    public ArgumentMultimap getArgMultimap() {
        return argMultimap;
    }

```
###### \java\seedu\multitasky\logic\parser\RestoreCommandParser.java
``` java
    /**
     * Parses the given {@code String} of arguments in the context of the RestoreCommand and returns a
     * RestoreCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public RestoreCommand parse(String args) throws ParseException {
        argMultimap = ArgumentTokenizer.tokenize(args,
                                                 ParserUtil.toPrefixArray(RestoreCommand.VALID_PREFIXES));

        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                   RestoreCommand.MESSAGE_USAGE));
        }

        if (hasIndexFlag(argMultimap)) { // process to restore by indexes
            if (hasInvalidFlagCombination(argMultimap)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                       RestoreCommand.MESSAGE_USAGE));
            }

            try {
                Prefix listIndicatorPrefix = ParserUtil.getMainPrefix(argMultimap, PREFIX_FLOATINGTASK,
                                                                      PREFIX_DEADLINE, PREFIX_EVENT);
                Index index = ParserUtil.parseIndex(argMultimap.getValue(listIndicatorPrefix).get());
                return new RestoreByIndexCommand(index, listIndicatorPrefix);
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else { // process to restore by find.
            String searchString = argMultimap.getPreamble().get()
                                             .replaceAll("\\" + CliSyntax.PREFIX_ESCAPE, "");
            final String[] keywords = searchString.split("\\s+");
            final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));

            return new RestoreByFindCommand(keywordSet);
        }
    }
```
###### \java\seedu\multitasky\logic\parser\RestoreCommandParser.java
``` java
    /**
     * A method that returns true if flags are given in an illogical manner for restoring commands.
     * illogical := any 2 of /float, /deadline, /event used together.
     */
    private boolean hasInvalidFlagCombination(ArgumentMultimap argMultimap) {
        if (argMultimap == null) {
            throw new AssertionError("argMultimap cannot be null.");
        }
        return ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_DEADLINE)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_DEADLINE, PREFIX_EVENT)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_EVENT);
    }
```
###### \java\seedu\multitasky\logic\parser\RestoreCommandParser.java
``` java
    /**
     * A method that returns true if flags in given ArgumentMultimap has at least one index-indicating
     * Prefix mapped to some arguments.
     * Index-indicating := /float or /deadline or /event
     */
    private boolean hasIndexFlag(ArgumentMultimap argMultimap) {
        Objects.requireNonNull(argMultimap);
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_DEADLINE,
                                             PREFIX_EVENT);
    }
```
