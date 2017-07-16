package seedu.multitasky.storage.exception;

// @@author A0132788U
/**
 * Exception thrown when there is nothing to undo.
 */
public class NothingToUndoException extends Exception {

    public NothingToUndoException(String message) {
        super(message);
    }
}
