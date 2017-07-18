# A0126623L-reused
###### \java\seedu\multitasky\logic\commands\RestoreCommandTest.java
``` java
/**
* Contains integration tests (interaction with the Model) and unit tests for {@code RestoreCommand}.
*/
public class RestoreCommandTest {

    private Model model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyEntry entryToRestore = model.getFilteredFloatingTaskList()
                                            .get(INDEX_FIRST_ENTRY.getZeroBased());
        RestoreCommand restoreCommand = prepareCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(RestoreCommand.MESSAGE_SUCCESS, entryToRestore);

        ModelManager expectedModel = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());
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

    // TODO fix bug from ModelManager updateFilteredList. look at NameQualifier.run(),
    // right now if emptySet is given as keywords, the for loop is not executed and returns true. (should be
    // false)
    // reimplement test after fixing.
    /*
     * @Test
     * public void execute_validIndexFilteredList_success() throws Exception {
     * showFirstEntryOnly(model);
     * ReadOnlyEntry entryToRestore =
     * model.getFilteredFloatingTaskList().get(INDEX_FIRST_ENTRY.getZeroBased());
     * RestoreCommand restoreCommand = prepareCommand(INDEX_FIRST_ENTRY);
     * String expectedMessage = String.format(RestoreCommand.MESSAGE_SUCCESS, entryToRestore);
     * Model expectedModel = new ModelManager(model.getEntryBook(), new UserPrefs());
     * expectedModel.restoreEntry(entryToRestore);
     * showNoEntry(expectedModel);
     * CommandTestUtil.assertCommandSuccess(restoreCommand, model, expectedMessage, expectedModel);
     * }
     */

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
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)), null, null, Entry.State.ACTIVE,
                                             Model.Search.AND, PowerMatch.Level.LEVEL_0);

        assert model.getFilteredFloatingTaskList().size() == 1;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoEntry(Model model) {
        model.updateFilteredFloatingTaskList(Collections.emptySet(), null, null, Entry.State.ACTIVE,
                                             Model.Search.AND, PowerMatch.Level.LEVEL_0);
        assert model.getFilteredFloatingTaskList().isEmpty();
    }

}
```
