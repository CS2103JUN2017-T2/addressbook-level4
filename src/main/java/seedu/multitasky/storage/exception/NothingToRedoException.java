package seedu.multitasky.storage.exception;

// @@author A0132788U
/**
 * Exception thrown when there is nothing to redo.
 */
public class NothingToRedoException extends Exception {

    public NothingToRedoException(String message) {
        super(message);
    }
}
