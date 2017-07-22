package seedu.multitasky.ui.util;

import java.util.Stack;

// @@author A0125586X
/**
 * Keeps track of the history of commands entered by the user into a text field.
 */
public class CommandHistory implements TextHistory {

    private Stack<String> commandPastStack;
    private Stack<String> commandFutureStack;

    private boolean isUserPreviouslyTypedCommandSaved;

    public CommandHistory() {
        commandPastStack = new Stack<>();
        commandFutureStack = new Stack<>();
        isUserPreviouslyTypedCommandSaved = false;
    }

    public void save(String text) {
        // First push all commands to the past
        while (!commandFutureStack.empty()) {
            commandPastStack.push(commandFutureStack.pop());
        }
        // Remove the previously saved user typed command if it wasn't executed
        if (isUserPreviouslyTypedCommandSaved) {
            isUserPreviouslyTypedCommandSaved = false;
            commandPastStack.pop();
        }
        // Save this command to the stack if it's the first, or if it's different from the last command
        if (text.length() > 0
            && (commandPastStack.empty() || !commandPastStack.peek().equals(text))) {
            commandPastStack.push(text);
        }
    }

    public String getPrevious(String current) {
        // A previous command doesn't exist
        if (commandPastStack.empty()) {
            return current;
        }
        // This is the first time in this run that the user requested the previous command,
        // Save previously typed command first if it is different from the previous entered command
        if (commandFutureStack.empty() && !commandPastStack.peek().equals(current)) {
            commandFutureStack.push(current);
            isUserPreviouslyTypedCommandSaved = true;
        }
        // Retrieve the previous command
        commandFutureStack.push(commandPastStack.pop());
        return commandFutureStack.peek();
    }

    public String getNext(String current) {
        // A newer command doesn't exist
        if (commandFutureStack.empty()) {
            return current;
        }
        commandPastStack.push(commandFutureStack.pop());
        // This is the newest command
        if (commandFutureStack.empty()) {
            commandFutureStack.push(commandPastStack.pop());
        }
        return commandFutureStack.peek();
    }

}
