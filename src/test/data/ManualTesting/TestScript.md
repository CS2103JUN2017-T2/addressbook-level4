# Manual Scripted Testing

This is a guide which explains the steps to perform manual testing to users.

## Loading Sample Data
1. Download the `SampleData.xml` file from [GitHub repository](https://github.com/CS2103JUN2017-T2/main/blob/master/src/test/data/ManualTesting/SampleData.xml).
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

1. **Using keywords as names**  
   The words "event", "deadline", and "float" have special meaning to MultiTasky. To enter them as part of the name of entries, escape-word is necessary. As an example, to delete the event named "Homecoming event" in the sample data, try  
   `delete \event`  
   The event should be successfully deleted.

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
   `Ctrl`+`Z` | `undo`
   `Ctrl`+`Y` | `redo`
   `Up` | Previous entered command
   `Down` | Next entered command

### Smart-Features
1. **Auto-correct**  
   Key in the following wrongly spelled command  
   `lit revsi`  
   Press `tab` and MultiTasky should be able to auto-correct the entered command to `list reverse`.
2. **PowerSearch**  
   _wrong order_: The following command  
   `find sc2013` finds `CS2103`.  
   _missing characters_: The following command  
   `find cs213` finds `CS2103`.  
   _wrong or extra characters_: The following command  
   `find cs22223` finds `CS2103`.  
3. **Smart Parsing**
   Key in the following command
   ```
   list
   add visit gardens by the bay by christmas
   ```
   Scroll down the deadline panel to find the deadline named `visit gardens by the bay`.  
   MultiTasky is able to identify the correct "by" word and figure out the deadline.

1. **Detection and alerts**  
   _Duplicates_  
   Key in the following command to add a duplicate entry 
   `add Wash dishes`  
   MultiTasky should prevent us from adding duplicate entries.
   
   _Overlapping Events_  
   Try adding an overlapping event with existing entry  
   `add Driving test from 28 July 10am to 28 July 11am`  
   The entry should be added successfully but with an alert that it overlaps with existing event(s).
   
   _Overdue Events or Deadlines_  
   `add NDP 2016 on 9 August 2016 to 9 Aug 2016`  
   The entry should be added successfully but with an alert that the added event is overdue.  

1. **Default duration for fast entry**  
   `add Christmas party from christmas 7pm`  
   Should result in a default event duration of 1 hour.

1. **Memory for edit command**
   ```
   edit party from 27 dec 7pm to 9pm
   edit event 2
   ```
   If more than one entries are found, the edit details are saved and one should just need to use index or hone the keyword search to identify the target entry to edit with the saved edit details.

1. **Editing entries across entry types**  
   ```
   list
   edit cors from
   ```
   The event named `Bid on CORS` should be turned into a deadline. Scroll down the deadline panel to check.

**End of Manual Test Script**
