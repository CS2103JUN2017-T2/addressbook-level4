# Manual Scripted Testing

This is a guide which explains the steps to perform manual testing to users.

## Loading Sample Data
1. Download the `SampleData.xml` file from [GitHub repository](https://github.com/CS2103JUN2017-T2/main/tree/manual-test-script/src/test/data/ManualTesting).
1. Start MultiTasky using the `.jar` file.
1. Load the sample data by entering the command `open <filepath to SampleData.xml>`.  
About 50 sample data should be loaded to the GUI panel.

## Test Cases
### Basic Features
1. **Add a floating task**  
   `add run dishwasher tag housework`  
   Scroll along the floating task GUI panel to ensure the entry is added successfully.
1. **Edit a floating task's name**  
   `edit dishwasher name buy dishwasher first`  
1. **Moving an entry to and from archive**  
   ```
   complete deadline 1
   list archive
   ```
   Scroll down in archive to find “label fridge”.
   
   ```
   restore label fridge
   list
   list upcoming
   ```
   Overdue deadline is shifted to the bottom of the deadline panel. Scroll down to check.
1. **Finding an entry**  
   `find exam`  
   By default, only active entries are searched.
   
   To find a completed entry (in archive), do  
   `find turtle archive`
1. **Clearing entries**  
   `list`
   `clear`  
   Active entries are listed, and then cleared.  
   
   `list bin`  
   Entries in the bin should appear (not cleared). Clear all entries, including those in the archive and bin, with the command  
   `clear all`
1. **Undo and redo changes**  
   `undo`  
   Clears the changes made by the previous command (i.e. `clear all`). The entries in the bin should reappear.  
   Enter the command  
   `list`  
   and notice that the active lists are still empty.
   Use the undo shortcut `Ctrl`+`Z` to make one additional undo, this will undo the changes made by `clear` and the active entries should appear.
   Redo can be done either by keying in the command `redo` or using the shortcut `Ctrl`+`Y`. The active entries should disappear again. Execute undo once more with `Ctrl`+`Z` to bring back the active entries for the upcoming tests.
   
### Time-Saving Features
1. **Auto-complete for command word and multiple keywords**  
   Key in the following line of incomplete words  
   `l u t`
   Press the `tab` key, and the command should auto-complete to `list upcoming to`.  
   Complete the command
   `list upcoming to 8pm`  
1. **Double-`tab` to show all possibilities of possible words for autocomplete if a single match cannot be found**  
   Key in the following in the command box  
   `c`  
   Press `tab` once. You should notice auto-complete doesn't work because there are two possible commands, i.e. `clear` and `complete`.  
   Now, try pressing `tab` twice (double-tap) and MultiTasky should display the possible commands:  
   `c: clear complete`
1. **Shortcuts**
   Try all the following keyboard shortcuts:

   Keyboard Shortcut | Associated Command
   ------------ | -------------
   `F1` | `help`
   `F2` | `edit`
   `F3` | `find`
   `F4` | `exit`
   `F5` | `list`
   `F6` | Brings focus back to command textbox
   `Ctrl`+`S` | `save`
   `Ctrl`+`O` | `open`
   `Up` | Previous entered command
   `Down` | Next entered command

