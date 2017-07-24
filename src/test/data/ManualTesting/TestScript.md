# Manual Scripted Testing

This is a guide which explains the steps to perform manual testing to users.

## Loading Sample Data
1. Download the `SampleData.xml` file from [GitHub repository](https://github.com/CS2103JUN2017-T2/main/blob/master/src/test/data/ManualTesting/SampleData.xml).
1. Start MultiTasky using the `.jar` file.
1. Load the sample data by entering the command `open <filepath to SampleData.xml>`.  
About 50 sample entries should be loaded on to the GUI panels.

## Help Command
### Opening the help window
> Command: `help`  
- Opens MultiTasky's UserGuide.
   
### Help window shortcut
> Command: `F1`  
- Opens MultiTasky's UserGuide.

## Add Command

### Add a floating task
> Command: `add run dishwasher tag housework`  
- Scroll down the Floating Task list to ensure that the entry is added successfully.

### Add a deadline 
> Command:`add Pay camp fee by tomorrow 6 pm tag school important`   
- This entry will be added to the Deadline List.  
- On the GUI, the next day's date is displayed.  
- The notations `one day from now` and time as `6 pm` are also displayed below the date.

### Add an event 
> Command: `add Football match on 2nd August 6 pm to 9 pm tag play`  
- Scroll down the Events List to ensure that it is successfully added.

### Add a duplicate entry
> Command: `add Wash dishes`
- MultiTasky should prevent us from adding duplicate entries. This also applies to deadlines and floating tasks.
- Tags are not taken into account to determine duplicates.

### Add an overlapping event
> Command: `add Driving test from 28 July 10am to 28 July 11am`
- The entry should be added successfully but with an alert that it overlaps with existing event(s).

### Add an overdue event/deadline
> Command: `add NDP 2016 on 9 August 2016 to 9 Aug 2016`  
- The entry should be added successfully but with an alert that the added event is overdue. This is also true for adding deadlines.

### Add an overlapping and overdue event
> Command: `add Register for SOC modules from 9 July to 10 July`
- The entry should be added successfully but with an alert that it is overdue and overlaps with existing event(s).

## Complete Command
### Completing an entry by index
> Command: `complete deadline 1`
- The first entry in the active deadlines list (`V0.5RC due`) is moved to the archive.

### Completing an entry by keyword search
> Command: `complete mpe modules`
- The entry `Register for MPE for modules` is moved to the archive.
- We do not need to specify the full name to select the entry to complete, as long as the keywords provided are sufficient to identify a single entry.

## List Command
### Listing the active entries
> Command: `list`
- The status bar at the top should read: `Currently displaying: active`.
- By default, active entries are displayed with `list`.

### Listing the archive (completed entries)
> Command: `list archive`
- The status bar at the top should read: `Currently displaying: archive`.

### Listing the bin (deleted entries)
> Command: `list bin`
- The status bar at the top should read: `Currently displaying: bin`.

### Listing all entries (active + archived + deleted)
> Command: `list all`
- The status bar at the top should read: `Currently displaying: all`.

### Listing entries in upcoming order
> Command: `list upcoming`
- All overdue events and deadlines are moved to the bottom of their respective lists.

### Listing entries in reverse order
> Command: `list reverse`
- The event and deadline that is furthest in the future is shown at the top of the list.

### Listing entries after a certain date
> Command: `list from July 26 2017`
- All events and deadlines before July 26 2017 are no longer shown.
- Floating tasks remain unchanged.

### Listing entries before a certain date
> Command: `list to July 26 2017`
- All events and deadlines after July 26 2017 are no longer shown.
- Floating tasks remain unchanged.

### Listing entries between two dates
> Command: `list from July 26 2017 to July 29 2017
- Only events and deadlines between the two dates are shown.
- Floating tasks remain unchanged.

### List command keyboard shortcut
> Command: press the `F5` key
- `list` is entered ino the command box.

## Edit Command
### Editing an entry by index
> Command: `edit float 5 name practice piano`
- The 5th floating task entry's name is changed to `practice piano` 

### Editing an entry by keyword search
> Command: `edit practice piano name practice guitar`
- The `practice piano` floating task entry's name is modified to `practice guitar`.

### Editing an entry's details
> Command: `edit bid cors from 30 july 10:30am to 12pm addtag dont_miss!`
- The `bid on cors` entry's start and end date are updated accordingly, and an additional `dont_miss!` tag is added
> Command: `edit bid cors tag`
- All tags are removed from the `bid on cors` entry.

### Editing across entry type
> Command: `edit cors from`
- the start date fields from `bid on cors` are removed, and the entry is automatically converted to a deadline entry. Scroll down on the deadline lists to verify.
> Command: `edit teach mom from tomorrow 9pm to 10pm`
- the entry `teach Mom to use Excel` is converted from a floating task to an event.

### Using saved edit details
> Command: `edit party from 27 dec 7pm to 9pm`
> Command: `edit event 2`
- If more than one entries are found through edit by keyword search, the edit details are saved and user can use index or hone the keyword search to identify the target entry to edit with the saved edit details.

### Edit shortcut
> Command: `F2`
- `edit` keyword appears on the command line.

## Find command
### Finding entries using basic keyword search
> Command: `find cs2103`
- All entries with `cs2103` in either the name or the tags are displayed.
- `cs2103` matches `CS2103`, as the search is case-insensitive.

### Finding entries over a date range
> Command: `find cs2103 from 21 July 2017 to 25 July 2017`
- Only entries that match `cs2103` and are between 21 July 2017 and 25 July 2017 are shown.
- In this case, only `Do lecture quiz` should be displayed.

### Finding entries using PowerSearch
> Command: `find sc2013`
- As regular search for `sc2013` does not produce any results, PowerSearch is used instead, which matches `cs2103` as a permutation of `sc2013`.
> Command: `find cs22223`
- PowerSearch is also able to match given extra or wrong characters in the search.

### Find command keyboard shortcut
> Command: `F3`
- `find` is entered into the command box.

## Delete Command
### Delete event by index
> Command: `delete event 1`
- Deletes `Register for MPE for modules` from the Events Active list and moves it to Bin.

### Delete deadline by keyword
> Command: `delete quiz`
- Deletes `Do lecture quiz` from the Deadlines Active list and moves it to Bin.

### Delete floating task by keyword then index
> Command: `delete dish`
- Lists two entries with the word dish, `Wash dishes` and `Run dishwasher`.
- User can specify which entry to delete using the index. 

> Command: `delete float 1`
- `Wash dishes` is deleted and moved from Floating Tasks Active list to Bin.

## Restore Command
### Restore an archived entry using keywords
> Command: `list archive`
> Command: `restore Register for FYP`
- List all archived entries and restore "Register for FYP" deadline. The entry should disappear from the archived after it is restored.
> Command: `list`
- This brings back the active list, which should have the restored "Register for FYP" deadline.

### Restore an archived entry using index
> Command: `list archive`
> Command: `restore float 2`
- This should restore the floating task "Buy cornflakes".
> Command: `list`
- This brings back the active list, which should have the restored "Buy cornflakes" floating task.

## History Command
### View command history
> Command: `history`
- All the entered commands are shown in the result display, with the most recently entered command shown first.

## Undo and Redo Commands
### Undo
> Command: `undo`
- Undo the previous data mutating action (undo restore `Buy cornflakes`).

### Undo shortcut
> Command: `Ctrl + Z`
- Undo again using keyboard shortcut (undo restore `Register for FYP`)

### Redo
> Command: `redo`
- Redo the previous undo (redo restore `Register for FYP`)

### Redo shortcut
> Command: `Ctrl + Y`
- Redo again using keyboard shortcut. (redo restore `Buy cornflakes`).

## Save Command
### Save entries to user-specified filepath
> Command: `save ./datafile.xml`
- Saves all the current information in the application to the specified filepath.
- In this case the data is now saved to the file `datafile.xml` in the same directory as `MultiTasky.jar`.

### Save command keyboard shortcut
> Command: `Ctrl` + `S`
- `save` is entered into the command box.

## Open Command
### Opens entries from to user-specified filepath
> Command: `open ./data/entrybook.xml`
- Loads all the information from the specified file into the application.
- The current save location remains unchanged as `./datafile.xml`.

### Open command keyboard shortcut
> Command: `Ctrl` + `O`
- `open` is entered into the command box.

## Clear command
### Clear active entries
> Command: `list`
> Command: `clear`
- Active entries are listed, and then cleared.

### Clear bin
> Command: `list bin`
> Command: `clear bin`
- The bin is cleared.
> Command: `list`
- Entries which are active should still be present.
> Command: `undo`
- Execute undo to bring back the cleared entries for subsequent tests.

### Clear archive
> Command: `list archive`
> Command: `clear archive`
- The archive is cleared.
> Command: `list`
- Entries which are active should still be present.
> Command: `undo`
- Execute undo to bring back the cleared entries for subsequent tests.

### Clear all entries
> Command: `clear all`
- This clears all command.
> Command: `list all`
- This should display an empty list.
> Command: `undo`
- Execute undo to bring back the cleared entries for subsequent tests.

## Additional Features
### Command word Autocomplete
> Command: `del` + `tab key`
- `del` is autocompleted to `delete`

### List command word Autocomplete possibilities
> Command: `c` + `tab key` twice quickly
- The result display should show `c:  clear        complete`, showing `clear` and `complete` as the two Autocomplete possibilities from `c`.
- The command entered is unchanged as `c`.

### Command word and keywords Autocomplete
> Command: `l u t` + `tab key`
- The command entered is completed to `list upcoming to`.

### Command word Autocorrect
> Command: `dete` + `tab key`
- The command entered is corrected to `delete`.

### Command box focus keyboard shortcuts
> Action: click somewhere else in the window apart from the command box to remove focus from it.  
> Command: `F6`
- Focus returns to the command box.

### Navigating command history keyboard shortcuts
> Command: `up arrow` and `down arrow` keys
- The `up arrow` key brings up the previously entered command into the command box, and the `down arrow` key brings up the next entered command into the command box.

## Exit command
> `exit`
- MultiTasky should be closed normally.

**End of Manual Test Script**

# Outdated
## Add Command

1. **Using keywords as names**  
   The words "event", "deadline", and "float" have special meanings for MultiTasky. To enter them as part of the name of entries, escape-word is necessary. As an example, to delete the event named "Homecoming-event briefing" in the sample data, try  
   `delete \event`  
   Scroll down the Events list to ensure that the event is successfully deleted.

1. **Finding an entry**  
   `find exam`  
   By default, only active entries are searched.
   
   To find a completed entry (in archive), do  
   `find turtle archive`
1. **Undo and redo changes**  
   `undo`  
   Clears the changes made by the previous command (i.e. `clear all`). The entries in the bin should reappear.  
   Enter the command  
   `list`  
   and notice that the active lists are still empty.
   Use the undo shortcut `Ctrl`+`Z` to make one additional undo, this will undo the changes made by `clear` and the active entries should appear.
   Redo can be done either by keying in the command `redo` or using the shortcut `Ctrl`+`Y`. The active entries should disappear again. Execute undo once more with `Ctrl`+`Z` to bring back the active entries for the upcoming tests.

3. **Smart Parsing**
   Key in the following command
   ```
   list
   add visit gardens by the bay by christmas
   ```
   Scroll down the Deadlines list to find the deadline named `visit gardens by the bay`.  
   MultiTasky is able to identify the correct "by" word and figure out the deadline.

1. **Default duration for fast entry**  
   `add Christmas party from christmas 7pm`  
   Should result in a default event duration of 1 hour.
