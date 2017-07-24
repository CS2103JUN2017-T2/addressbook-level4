# A0132788U-reused
###### \java\seedu\multitasky\logic\commands\CompleteByFindCommand.java
``` java
/**
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

    @Override
    public CommandResult execute() throws CommandException, DuplicateEntryException {
        List<ReadOnlyEntry> allList = getListAfterSearch();

        if (allList.size() == 1) { // proceed to complete
            entryToComplete = allList.get(0);
            try {
                model.changeEntryState(entryToComplete, Entry.State.ARCHIVED);
            } catch (EntryNotFoundException | OverlappingEventException | EntryOverdueException
                     | OverlappingAndOverdueEventException e) {
                throw new AssertionError("These exceptions are not valid for complete command");
            }
            revertListView();
            return new CommandResult(String.format(MESSAGE_SUCCESS, entryToComplete));
        } else {
            history.setPrevSearch(keywords, null, null, Entry.State.ACTIVE);
            if (allList.size() >= 2) { // multiple entries found
                throw new CommandException(MESSAGE_MULTIPLE_ENTRIES);
            } else {
                assert (allList.size() == 0); // no entries found
                throw new CommandException(MESSAGE_NO_ENTRIES);
            }
        }
    }

    /**
     * reverts inner lists in model to how they were before by using command history
     */
    private void revertListView() {
        model.updateAllFilteredLists(history.getPrevKeywords(), history.getPrevStartDate(),
                                     history.getPrevEndDate(), history.getPrevState(),
                                     history.getPrevSearches());
    }

    /**
     * updates inner lists with new keyword terms and returns a collated list with all found entries.
     */
    private List<ReadOnlyEntry> getListAfterSearch() {
        model.updateAllFilteredLists(keywords, null, null, Entry.State.ACTIVE, Model.STRICT_SEARCHES);

        List<ReadOnlyEntry> allList = new ArrayList<>();
        allList.addAll(model.getFilteredDeadlineList());
        allList.addAll(model.getFilteredEventList());
        allList.addAll(model.getFilteredFloatingTaskList());
        return allList;
    }

}
```
###### \java\seedu\multitasky\logic\commands\CompleteByIndexCommand.java
``` java
/**
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
        } catch (EntryNotFoundException | OverlappingEventException | OverlappingAndOverdueEventException
                 | EntryOverdueException e) {
            throw new AssertionError("These exceptions are not valid for complete command");
        }
        refreshListView();
        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToComplete));
    }

    /**
     * refresh inner lists by using previous command history
     */
    private void refreshListView() {
        model.updateAllFilteredLists(history.getPrevKeywords(), history.getPrevStartDate(),
                                     history.getPrevEndDate(), history.getPrevState(),
                                     history.getPrevSearches());
    }

}
```
###### \java\seedu\multitasky\logic\commands\CompleteCommand.java
``` java
/**
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
                                               + "Format: " + COMMAND_WORD + " <" + "KEYWORDS" + ">" + " |"
                                               + " <<" + String.join(" | ", CliSyntax.PREFIX_EVENT.toString(),
                                                       CliSyntax.PREFIX_DEADLINE.toString(),
                                                       CliSyntax.PREFIX_FLOATINGTASK.toString())
                                               + ">" + " INDEX" + ">" + "\n"
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
/**
 * Parses input arguments and creates a new CompleteCommand object
 */
public class CompleteCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the CompleteCommand and returns a
     * CompleteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */

    public CompleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                ParserUtil.toPrefixArray(CompleteCommand.VALID_PREFIXES));

        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CompleteCommand.MESSAGE_USAGE));
        }

        if (hasIndexFlag(argMultimap)) { // process to complete by indexes
            if (!ParserUtil.hasValidListTypeCombination(argMultimap)) {
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
