package seedu.multitasky.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    //@@author A0140633R
    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("/name");
    public static final Prefix PREFIX_TAG = new Prefix("/tag");
    public static final Prefix PREFIX_ARCHIVE = new Prefix("/archive");
    public static final Prefix PREFIX_BIN = new Prefix("/bin");
    public static final Prefix PREFIX_EVENT = new Prefix("/event");
    public static final Prefix PREFIX_DEADLINE = new Prefix("/deadline");
    public static final Prefix PREFIX_FLOATINGTASK = new Prefix("/float");

}