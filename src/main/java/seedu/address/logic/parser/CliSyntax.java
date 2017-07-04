package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    //@@kevinlamKB A0140633R
    /* Prefix definitions */
    public static final Prefix PREFIX_START_DATE_ON = new Prefix("/on");
    public static final Prefix PREFIX_START_DATE_AT = new Prefix("/at");
    public static final Prefix PREFIX_START_DATE_FROM = new Prefix("/from");
    public static final Prefix PREFIX_START_DATE_BY = new Prefix("/by");
    public static final Prefix PREFIX_END_DATE = new Prefix("/to");
    public static final Prefix PREFIX_RECUR = new Prefix("/every");
    public static final Prefix PREFIX_RECUR_CUSTOMIZE = new Prefix("/until");
    public static final Prefix PREFIX_ENTRYBOOK_ARCHIVE = new Prefix("/archive");
    public static final Prefix PREFIX_ENTRYBOOK_BIN = new Prefix("/bin");
    public static final Prefix PREFIX_TAG = new Prefix("/tag");

}
