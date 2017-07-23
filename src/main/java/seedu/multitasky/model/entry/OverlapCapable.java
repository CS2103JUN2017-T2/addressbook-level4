package seedu.multitasky.model.entry;

//@@author A0126623L
/**
* Interface for overlapping behavior.
*/
public interface OverlapCapable {

    public boolean overlapsWith(OverlapCapable other);

}
