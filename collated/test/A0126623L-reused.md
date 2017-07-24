# A0126623L-reused
###### \java\guitests\RestoreCommandArchiveTest.java
``` java
public class RestoreCommandArchiveTest extends EntryBookGuiTest {

```
###### \java\guitests\RestoreCommandArchiveTest.java
``` java
    /**********************
     * Restoring by Index *
     *********************/
    @Test
    public void restore_firstEventByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleArchivedEvents();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertRestoreEventByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_lastEventByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleArchivedEvents();
        Index targetIndex = Index.fromOneBased(currentList.length);
        assertRestoreEventByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_invalidEventIndex_errorMessage() {
        Entry[] currentList = SampleEntries.getSampleArchivedEvents();
        Index targetIndex = Index.fromOneBased(currentList.length + 1);
        commandBox.runCommand(CommandUtil.getRestoreEventByIndexCommand(targetIndex));
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void restore_firstDeadlineByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleArchivedDeadlines();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertRestoreDeadlineByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_lastDeadlineByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleArchivedDeadlines();
        Index targetIndex = Index.fromOneBased(currentList.length);
        assertRestoreDeadlineByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_invalidDeadlineIndex_errorMessage() {
        Entry[] currentList = SampleEntries.getSampleArchivedDeadlines();
        Index targetIndex = Index.fromOneBased(currentList.length + 1);
        commandBox.runCommand(CommandUtil.getRestoreDeadlineByIndexCommand(targetIndex));
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void restore_firstFloatingTaskByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleArchivedFloatingTasks();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        assertRestoreFloatingTaskByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_lastFloatingTaskByIndex_success() {
        Entry[] currentList = SampleEntries.getSampleArchivedFloatingTasks();
        Index targetIndex = Index.fromOneBased(currentList.length);
        assertRestoreFloatingTaskByIndexSuccess(targetIndex, currentList);
    }

    @Test
    public void restore_invalidFloatingTaskIndex_errorMessage() {
        Entry[] currentList = SampleEntries.getSampleArchivedFloatingTasks();
        Index targetIndex = Index.fromOneBased(currentList.length + 1);
        commandBox.runCommand(CommandUtil.getRestoreFloatingTaskByIndexCommand(targetIndex));
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /************************
     * Restoring by Keyword *
     ************************/
    @Test
    public void restore_eventKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleArchivedEvents();
        Entry entryToRestore = SampleEntries.ASSIGNMENT;
        commandBox.runCommand(RestoreCommand.COMMAND_WORD + " assignment");
        assertEventRestoredByKeyword(entryToRestore, currentList);
    }

    @Test
    public void restore_deadlineKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleArchivedDeadlines();
        Entry entryToRestore = SampleEntries.QUIZ;
        commandBox.runCommand(RestoreCommand.COMMAND_WORD + " quiz");
        assertDeadlineRestoredByKeyword(entryToRestore, currentList);
    }

    @Test
    public void restore_floatingTaskKeyword_success() {
        Entry[] currentList = SampleEntries.getSampleArchivedFloatingTasks();
        Entry entryToRestore = SampleEntries.EXERCISE;
        commandBox.runCommand(RestoreCommand.COMMAND_WORD + " jogging");
        assertFloatingTaskRestoredByKeyword(entryToRestore, currentList);
    }

    /**************************************
     * Different types of invalid wording *
     **************************************/
    @Test
    public void restore_unknownCommandName_errorMessage() {
        commandBox.runCommand(RestoreCommand.COMMAND_WORD.substring(0, RestoreCommand.COMMAND_WORD.length()
                                                                       - 1));
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand(RestoreCommand.COMMAND_WORD + "a");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void restore_invalidCommandFormat_errorMessage() {
        commandBox.runCommand(RestoreCommand.COMMAND_WORD);
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                          RestoreCommand.MESSAGE_USAGE));
    }

    /*******************************
     * Mixed-case and autocomplete *
     *******************************/
    /**
     * For all mixed-case tests only floating task entries are tested,
     * which should be suitable to test for all types since the type of task
     * doesn't affect the parsing of the command word.
     */
    @Test
    public void restore_firstCharUppercase_success() {
        char[] commandWord = RestoreCommand.COMMAND_WORD.toCharArray();
        commandWord[0] = Character.toUpperCase(commandWord[0]);
        assertRestoreWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void restore_lastCharUppercase_success() {
        char[] commandWord = RestoreCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length - 1] = Character.toUpperCase(commandWord[commandWord.length - 1]);
        assertRestoreWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void restore_middleCharUppercase_success() {
        char[] commandWord = RestoreCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length / 2] = Character.toUpperCase(commandWord[commandWord.length / 2]);
        assertRestoreWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void restore_allCharUppercase_success() {
        String commandWord = RestoreCommand.COMMAND_WORD.toUpperCase();
        assertRestoreWithCommandWord(commandWord);
    }

    @Test
    public void restore_tabAutocomplete_success() {
        for (int i = 3; i < RestoreCommand.COMMAND_WORD.length(); ++i) {
            assertRestoreTabAutocomplete(RestoreCommand.COMMAND_WORD.substring(0, i));
        }
    }

    /**
     * Confirms that the given input string will autocomplete to the correct restore command word.
     */
    private void assertRestoreTabAutocomplete(String input) {
        commandBox.enterCommand(input);
        commandBox.pressTabKey();
        assertCommandBox(RestoreCommand.COMMAND_WORD + " ");
    }

    private void assertRestoreWithCommandWord(String commandWord) {
        Entry[] currentList = SampleEntries.getSampleArchivedFloatingTasks();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        Entry entryToRestore = currentList[targetIndex.getZeroBased()];
        commandBox.runCommand(commandWord + " " + CliSyntax.PREFIX_FLOATINGTASK
                              + " " + targetIndex.getOneBased());
        assertFloatingTaskRestored(entryToRestore, currentList);
    }

    /**
     * Runs the restore command to restore the event at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of events (before deletion).
     */
    private void assertRestoreEventByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToRestore = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getRestoreEventByIndexCommand(index));
        assertEventRestored(entryToRestore, currentList);
    }

    /**
     * Runs the restore command to restore the deadline at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of deadlines (before deletion).
     */
    private void assertRestoreDeadlineByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToRestore = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getRestoreDeadlineByIndexCommand(index));
        assertDeadlineRestored(entryToRestore, currentList);
    }

    /**
     * Runs the restore command to restore the floating task at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of floating tasks (before deletion).
     */
    private void assertRestoreFloatingTaskByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToRestore = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getRestoreFloatingTaskByIndexCommand(index));
        assertFloatingTaskRestored(entryToRestore, currentList);
    }

    private void assertEventRestored(Entry entryRestored, final Entry[] currentList) {
        assertEventRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertEventRestoredByKeyword(Entry entryRestored, final Entry[] currentList) {
        assertEventRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreByFindCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertDeadlineRestored(Entry entryRestored, final Entry[] currentList) {
        assertDeadlineRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertDeadlineRestoredByKeyword(Entry entryRestored, final Entry[] currentList) {
        assertDeadlineRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreByFindCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertFloatingTaskRestored(Entry entryRestored, final Entry[] currentList) {
        assertFloatingTaskRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertFloatingTaskRestoredByKeyword(Entry entryRestored, final Entry[] currentList) {
        assertFloatingTaskRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreByFindCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertEventRemovedFromList(Entry entryRestored, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryRestored);
        assertTrue(eventListPanel.isListMatching(expectedList));
    }

    private void assertDeadlineRemovedFromList(Entry entryRestored, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryRestored);
        assertTrue(deadlineListPanel.isListMatching(expectedList));
    }

    private void assertFloatingTaskRemovedFromList(Entry entryRestored, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryRestored);
        assertTrue(floatingTaskListPanel.isListMatching(expectedList));
    }

}
```
###### \java\guitests\RestoreCommandBinTest.java
``` java
    /**************************************
     * Different types of invalid wording *
     **************************************/
    @Test
    public void restore_unknownCommandName_errorMessage() {
        commandBox.runCommand(RestoreCommand.COMMAND_WORD.substring(0, RestoreCommand.COMMAND_WORD.length()
                                                                       - 1));
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

        commandBox.runCommand(RestoreCommand.COMMAND_WORD + "a");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void restore_invalidCommandFormat_errorMessage() {
        commandBox.runCommand(RestoreCommand.COMMAND_WORD);
        assertResultMessage(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                          RestoreCommand.MESSAGE_USAGE));
    }

    /*******************************
     * Mixed-case and autocomplete *
     *******************************/
    /**
     * For all mixed-case tests only floating task entries are tested,
     * which should be suitable to test for all types since the type of task
     * doesn't affect the parsing of the command word.
     */
    @Test
    public void restore_firstCharUppercase_success() {
        char[] commandWord = RestoreCommand.COMMAND_WORD.toCharArray();
        commandWord[0] = Character.toUpperCase(commandWord[0]);
        assertRestoreWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void restore_lastCharUppercase_success() {
        char[] commandWord = RestoreCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length - 1] = Character.toUpperCase(commandWord[commandWord.length - 1]);
        assertRestoreWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void restore_middleCharUppercase_success() {
        char[] commandWord = RestoreCommand.COMMAND_WORD.toCharArray();
        commandWord[commandWord.length / 2] = Character.toUpperCase(commandWord[commandWord.length / 2]);
        assertRestoreWithCommandWord(String.copyValueOf(commandWord));
    }

    @Test
    public void restore_allCharUppercase_success() {
        String commandWord = RestoreCommand.COMMAND_WORD.toUpperCase();
        assertRestoreWithCommandWord(commandWord);
    }

    @Test
    public void restore_tabAutocomplete_success() {
        for (int i = 3; i < RestoreCommand.COMMAND_WORD.length(); ++i) {
            assertRestoreTabAutocomplete(RestoreCommand.COMMAND_WORD.substring(0, i));
        }
    }

    /**
     * Confirms that the given input string will autocomplete to the correct restore command word.
     */
    private void assertRestoreTabAutocomplete(String input) {
        commandBox.enterCommand(input);
        commandBox.pressTabKey();
        assertCommandBox(RestoreCommand.COMMAND_WORD + " ");
    }

    private void assertRestoreWithCommandWord(String commandWord) {
        Entry[] currentList = SampleEntries.getSampleDeletedFloatingTasks();
        Index targetIndex = SampleEntries.INDEX_FIRST_ENTRY;
        Entry entryToRestore = currentList[targetIndex.getZeroBased()];
        commandBox.runCommand(commandWord + " " + CliSyntax.PREFIX_FLOATINGTASK
                              + " " + targetIndex.getOneBased());
        assertFloatingTaskRestoredNormally(entryToRestore, currentList);
    }

    /**
     * Runs the restore command to restore the event at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of events (before deletion).
     */
    private void assertRestoreEventByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToRestore = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getRestoreEventByIndexCommand(index));
        assertEventRestoredNormally(entryToRestore, currentList);
    }

    /**
     * Runs the restore command to restore the deadline at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of deadlines (before deletion).
     */
    private void assertRestoreDeadlineByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToRestore = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getRestoreDeadlineByIndexCommand(index));
        assertDeadlineRestored(entryToRestore, currentList);
    }

    /**
     * Runs the restore command to restore the floating task at {@code index} and confirms the result is correct.
     * @param currentList A copy of the current list of floating tasks (before deletion).
     */
    private void assertRestoreFloatingTaskByIndexSuccess(Index index, final Entry[] currentList) {
        Entry entryToRestore = currentList[index.getZeroBased()];
        commandBox.runCommand(CommandUtil.getRestoreFloatingTaskByIndexCommand(index));
        assertFloatingTaskRestoredNormally(entryToRestore, currentList);
    }

    private void assertEventRestoredNormally(Entry entryRestored, final Entry[] currentList) {
        assertEventRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertDeadlineRestored(Entry entryRestored, final Entry[] currentList) {
        assertDeadlineRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertFloatingTaskRestoredNormally(Entry entryRestored, final Entry[] currentList) {
        assertFloatingTaskRemovedFromList(entryRestored, currentList);
        assertResultMessage(String.format(RestoreCommand.MESSAGE_SUCCESS, entryRestored));
    }

    private void assertEventRemovedFromList(Entry entryRestored, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryRestored);
        assertTrue(eventListPanel.isListMatching(expectedList));
    }

    private void assertDeadlineRemovedFromList(Entry entryRestored, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryRestored);
        assertTrue(deadlineListPanel.isListMatching(expectedList));
    }

    private void assertFloatingTaskRemovedFromList(Entry entryRestored, final Entry[] currentList) {
        Entry[] expectedList = TestUtil.removeEntriesFromList(currentList, entryRestored);
        assertTrue(floatingTaskListPanel.isListMatching(expectedList));
    }

}
```
###### \java\seedu\multitasky\logic\commands\RestoreCommandTest.java
``` java
/**
* Contains integration tests (interaction with the Model) and unit tests for {@code RestoreCommand}.
*/
public class RestoreCommandTest {

    private Model model = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(),
                                           new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyEntry entryToRestore = model.getFilteredFloatingTaskList()
                                            .get(INDEX_FIRST_ENTRY.getZeroBased());
        RestoreCommand restoreCommand = prepareCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(RestoreCommand.MESSAGE_SUCCESS, entryToRestore);

        ModelManager expectedModel = new ModelManager(SampleEntries.getSampleEntryBookWithActiveEntries(),
                                                      new UserPrefs());
        model.changeEntryState(entryToRestore, Entry.State.DELETED);
        expectedModel.updateAllFilteredListToShowAllActiveEntries();

        CommandTestUtil.assertCommandSuccess(restoreCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredFloatingTaskList().size() + 1);
        RestoreCommand restoreCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(restoreCommand, model,
                                             Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }


    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() throws Exception {
        showFirstEntryOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        // ensures that outOfBoundIndex is still in bounds of entry book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEntryBook().getFloatingTaskList().size());

        RestoreCommand restoreCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(restoreCommand, model,
                                             Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /**
     * Returns a {@code RestoreCommand} with the parameter {@code index}.
     */
    private RestoreCommand prepareCommand(Index index) {
        RestoreCommand restoreCommand = new RestoreByIndexCommand(index, new Prefix("float"));
        restoreCommand.setData(model, new CommandHistory());
        return restoreCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show only the first entry from the entry book.
     */
    private void showFirstEntryOnly(Model model) {
        ReadOnlyEntry entry = model.getEntryBook().getFloatingTaskList().get(0);
        final String[] splitName = entry.getName().fullName.split("\\s+");
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)), null, null,
                                             Entry.State.ACTIVE,
                                             Model.Search.AND, PowerMatch.UNUSED);

        assert model.getFilteredFloatingTaskList().size() == 1;
    }

}
```
