package seedu.multitasky.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    // @@author A0140633R
    /* Prefix definitions */

    // Note that preceding whitespace is handled by ArgumentTokenizer.
    /* Entry data fields */
    public static final Prefix PREFIX_NAME = new Prefix("name");
    public static final Prefix PREFIX_FROM = new Prefix("from");
    public static final Prefix PREFIX_BY = new Prefix("by");
    public static final Prefix PREFIX_AT = new Prefix("at");
    public static final Prefix PREFIX_TO = new Prefix("to");
    public static final Prefix PREFIX_TAG = new Prefix("tag");
    public static final Prefix PREFIX_ESCAPE = new Prefix("\\");

    /* Entry type */
    public static final Prefix PREFIX_EVENT = new Prefix("event");
    public static final Prefix PREFIX_DEADLINE = new Prefix("deadline");
    public static final Prefix PREFIX_FLOATINGTASK = new Prefix("float");

    /* EntryLists */
    public static final Prefix PREFIX_ARCHIVE = new Prefix("archive");
    public static final Prefix PREFIX_BIN = new Prefix("bin");

    /* List types */
    public static final Prefix PREFIX_UPCOMING = new Prefix("upcoming");
    public static final Prefix PREFIX_REVERSE = new Prefix("reverse");

}
