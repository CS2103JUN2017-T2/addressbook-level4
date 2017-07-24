# A0126623L-reused
###### \java\seedu\multitasky\logic\commands\RestoreCommand.java
``` java
/**
 * Abstract class that represents a restore command. Holds command_word and confirmation messages a restore
 * command will be using.
 */
public abstract class RestoreCommand extends Command {

    public static final String COMMAND_WORD = "restore";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " : Restores an archived/deleted entry identified"
            + " by keywords if it is the only entry found, or restores the entry identified by the index number of"
            + " the last archived/deleted entry listing.\n"
            + "Format: " + COMMAND_WORD + " <" + "KEYWORDS" + ">" + " |"
            + " <<" + String.join(" | ", CliSyntax.PREFIX_EVENT.toString(), CliSyntax.PREFIX_DEADLINE.toString(),
            CliSyntax.PREFIX_FLOATINGTASK.toString()) + ">" + " INDEX" + ">" + "\n"
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
            if (!ParserUtil.hasValidListTypeCombination(argMultimap)) {
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
###### \java\seedu\multitasky\model\entry\EntryList.java
``` java
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
               || (other instanceof EntryList // instanceof handles nulls
                   && this.internalList.equals(((EntryList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public Iterator<Entry> iterator() {
        return internalList.iterator();
    }
}
```
###### \java\seedu\multitasky\model\entry\FloatingTask.java
``` java
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getState(), getTags());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(getName()).append(", Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
```
###### \java\seedu\multitasky\model\entry\Name.java
``` java
    /**
     * Returns true if a given string is a valid entry name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
               || (other instanceof Name // instanceof handles nulls
                   && this.fullName.equals(((Name) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
```
###### \java\seedu\multitasky\model\EntryBook.java
``` java
    /**
     * Ensures that every tag in these entries:
     * - exists in the master list {@link #tags}
     * - points to a Tag object in the master list
     *
     * @see #syncMasterTagListWith(Entry)
     */
    private void syncMasterTagListWith(EntryList entries) {
        entries.forEach(this::syncMasterTagListWith);
    }

    // ================= Util Methods =================

```
