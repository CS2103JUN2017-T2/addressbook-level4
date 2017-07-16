# A0132788U-reused
###### \java\seedu\multitasky\logic\commands\CompleteCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code CompleteCommand}.
 */
public class CompleteCommandTest {

    private Model model = new ModelManager(SampleEntries.getSampleEntryBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyEntry entryToComplete = model.getFilteredFloatingTaskList()
                .get(INDEX_FIRST_ENTRY.getZeroBased());

        CompleteCommand completeCommand = prepareCommand(INDEX_FIRST_ENTRY);

        String expectedMessage = String.format(CompleteCommand.MESSAGE_SUCCESS, entryToComplete);

        ModelManager expectedModel = new ModelManager(model.getEntryBook(), new UserPrefs());
        model.changeEntryState(entryToComplete, Entry.State.ARCHIVED);
        expectedModel.updateAllFilteredListToShowAllActiveEntries();
        CommandTestUtil.assertCommandSuccess(completeCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() throws Exception {
        showFirstEntryOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_ENTRY;
        // ensures that outOfBoundIndex is still in bounds of entry book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEntryBook().getFloatingTaskList().size());

        CompleteCommand completeCommand = prepareCommand(outOfBoundIndex);

        CommandTestUtil.assertCommandFailure(completeCommand, model,
                Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    /**
     * Returns a {@code CompleteCommand} with the parameter {@code index}.
     */
    private CompleteCommand prepareCommand(Index index) {
        CompleteCommand completeCommand = new CompleteByIndexCommand(index, new Prefix("float"));
        completeCommand.setData(model, new CommandHistory());
        return completeCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show only the first entry from the entry book.
     */
    private void showFirstEntryOnly(Model model) {
        ReadOnlyEntry entry = model.getEntryBook().getFloatingTaskList().get(0);
        final String[] splitName = entry.getName().fullName.split("\\s+");
        model.updateFilteredFloatingTaskList(new HashSet<>(Arrays.asList(splitName)), null, null, Entry.State.ACTIVE,
                                             Model.Search.AND);

        assert model.getFilteredFloatingTaskList().size() == 1;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoEntry(Model model) {
        model.updateFilteredFloatingTaskList(Collections.emptySet(), null, null, Entry.State.ACTIVE, Model.Search.AND);
        assert model.getFilteredFloatingTaskList().isEmpty();
    }

}
```
