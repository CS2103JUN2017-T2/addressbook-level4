# A0132788U-reused
###### \java\seedu\multitasky\logic\commands\CompleteByFindCommand.java
``` java
/*
 * Finds entries from given keywords and completes the entry if it is the only one found and moves it to archive.
 */
public class CompleteByFindCommand extends CompleteCommand {
    public static final String MESSAGE_NO_ENTRIES = "No entries found! Please try again with different keywords";

    public static final String MESSAGE_MULTIPLE_ENTRIES = "More than one entry found! \n"
                                                          + "Use " + COMMAND_WORD + " ["
                                                          + String.join(" | ",
                                                                  CliSyntax.PREFIX_EVENT.toString(),
                                                                  CliSyntax.PREFIX_DEADLINE.toString(),
                                                                  CliSyntax.PREFIX_FLOATINGTASK.toString())
                                                          + "]"
                                                          + " INDEX to specify which entry to complete.";

    private Set<String> keywords;

    public CompleteByFindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {

        // Update all 3 lists with new search parameters until at least 1 result is found.
        model.updateAllFilteredLists(keywords, null, null, Entry.State.ACTIVE);

        // collate a combined list to measure how many entries are found.
        List<ReadOnlyEntry> allList = new ArrayList<>();
        allList.addAll(model.getFilteredDeadlineList());
        allList.addAll(model.getFilteredEventList());
        allList.addAll(model.getFilteredFloatingTaskList());

        if (allList.size() == 1) { // proceed to complete
            entryToComplete = allList.get(0);
            try {
                model.changeEntryState(entryToComplete, Entry.State.ARCHIVED);
            } catch (EntryNotFoundException e) {
                assert false : "The target entry cannot be missing";
            } catch (OverlappingEventException oee) {
                assert false : "This should not happen for complete command.";
            }
            // refresh list view after updating.
            model.updateAllFilteredLists(history.getPrevSearch(), history.getPrevStartDate(),
                                         history.getPrevEndDate(), history.getPrevState());

            return new CommandResult(String.format(MESSAGE_SUCCESS, entryToComplete));
        } else {
            history.setPrevSearch(keywords, null, null, Entry.State.ACTIVE);
            if (allList.size() >= 2) { // multiple entries found
                return new CommandResult(MESSAGE_MULTIPLE_ENTRIES);
            } else {
                assert (allList.size() == 0); // no entries found
                return new CommandResult(MESSAGE_NO_ENTRIES);
            }
        }
    }

}
```
###### \java\seedu\multitasky\logic\commands\CompleteByIndexCommand.java
``` java
/*
 * Completes an entry identified using the type of entry followed by displayed index.
 */
public class CompleteByIndexCommand extends CompleteCommand {

    private Index targetIndex;
    private Prefix listIndicatorPrefix;

    public CompleteByIndexCommand(Index targetIndex, Prefix listIndicatorPrefix) {
        this.targetIndex = targetIndex;
        this.listIndicatorPrefix = listIndicatorPrefix;
    }

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {
        UnmodifiableObservableList<ReadOnlyEntry> listToCompleteFrom = ParserUtil
                .getListIndicatedByPrefix(model, listIndicatorPrefix);
        assert listToCompleteFrom != null;

        if (targetIndex.getZeroBased() >= listToCompleteFrom.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        entryToComplete = listToCompleteFrom.get(targetIndex.getZeroBased());
        try {
            model.changeEntryState(entryToComplete, Entry.State.ARCHIVED);
        } catch (EntryNotFoundException enfe) {
            assert false : "The target entry cannot be missing";
        } catch (OverlappingEventException oee) {
            assert false : "This should not happen for complete command.";
        }
        // refresh list view after updating.
        model.updateAllFilteredLists(history.getPrevSearch(), history.getPrevStartDate(),
                                     history.getPrevEndDate(), history.getPrevState());

        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToComplete));
    }

}
```
###### \java\seedu\multitasky\logic\commands\CompleteCommand.java
``` java
/*
 * Abstract class that represents a complete command. It contains the command keyword, usage instructions,
 * prefixes and the success message.
 */
public abstract class CompleteCommand extends Command {
    public static final String COMMAND_WORD = "complete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
                                               + " : Completes the entry identified by keywords in the active list"
                                               + " if it is the only entry found, or completes the entry identified"
                                               + " by the index number of the last entry listing"
                                               + " and moves it to archive.\n"
                                               + "Format: " + COMMAND_WORD + " [" + "[" + "KEYWORDS" + "]" + " |"
                                               + " [" + String.join(" | ", CliSyntax.PREFIX_EVENT.toString(),
                                                       CliSyntax.PREFIX_DEADLINE.toString(),
                                                       CliSyntax.PREFIX_FLOATINGTASK.toString())
                                               + "]" + " INDEX" + "]" + "\n"
                                               + "All possible flags for Complete : 'event', 'deadline', 'float'";

    public static final String MESSAGE_SUCCESS = "Entry completed:" + "\n"
                                                 + Messages.MESSAGE_ENTRY_DESCRIPTION + "%1$s" + "\n"
                                                 + "Entry has been moved to archive";

    public static final String[] VALID_PREFIXES = { CliSyntax.PREFIX_EVENT.toString(),
                                                    CliSyntax.PREFIX_DEADLINE.toString(),
                                                    CliSyntax.PREFIX_FLOATINGTASK.toString() };

    protected ReadOnlyEntry entryToComplete;

    @Override
    public void setData(Model model, CommandHistory history) {
        requireNonNull(history);
        requireNonNull(model);
        this.model = model;
        this.history = history;
    }

}
```
###### \java\seedu\multitasky\logic\parser\CompleteCommandParser.java
``` java
    public CompleteCommand parse(String args) throws ParseException {
        argMultimap = ArgumentTokenizer.tokenize(args, ParserUtil.toPrefixArray(CompleteCommand.VALID_PREFIXES));

        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompleteCommand.MESSAGE_USAGE));
        }

        if (hasIndexFlag(argMultimap)) { // process to complete by indexes
            if (hasInvalidFlagCombination(argMultimap)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        CompleteCommand.MESSAGE_USAGE));
            }

            try {
                Prefix listIndicatorPrefix = ParserUtil.getMainPrefix(argMultimap, PREFIX_FLOATINGTASK,
                        PREFIX_DEADLINE, PREFIX_EVENT);
                Index index = ParserUtil.parseIndex(argMultimap.getValue(listIndicatorPrefix).get());
                return new CompleteByIndexCommand(index, listIndicatorPrefix);
            } catch (IllegalValueException ive) {
                throw new ParseException(ive.getMessage(), ive);
            }

        } else { // process to complete by find.
            String searchString = argMultimap.getPreamble().get()
                    .replaceAll("\\" + CliSyntax.PREFIX_ESCAPE, "");
            final String[] keywords = searchString.split("\\s+");
            final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));

            return new CompleteByFindCommand(keywordSet);
        }
    }

    /**
     * A method that returns true if flags are given in an illogical manner for complete commands.
     * illogical := any 2 of /float, /deadline, /event used together.
     */
    private boolean hasInvalidFlagCombination(ArgumentMultimap argMultimap) {
        assert argMultimap != null;
        return ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_DEADLINE)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_DEADLINE, PREFIX_EVENT)
               || ParserUtil.areAllPrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_EVENT);
    }

    /**
     * A method that returns true if flags in given ArgumentMultimap has at least one index-indicating
     * Prefix mapped to some arguments.
     * Index-indicating := /float or /deadline or /event
     */
    private boolean hasIndexFlag(ArgumentMultimap argMultimap) {
        assert argMultimap != null;
        return ParserUtil.arePrefixesPresent(argMultimap, PREFIX_FLOATINGTASK, PREFIX_DEADLINE,
                PREFIX_EVENT);
    }

}
```
