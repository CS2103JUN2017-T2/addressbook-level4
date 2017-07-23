package seedu.multitasky.ui.util;

// @@author A0125586X
/**
 * Interface class for a general text history class
 */
public interface TextHistory {

    public void save(String text);

    public String getPrevious(String current);

    public String getNext(String current);

}
