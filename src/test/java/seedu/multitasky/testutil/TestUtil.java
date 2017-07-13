package seedu.multitasky.testutil;

import static org.junit.Assert.fail;
import static seedu.multitasky.model.util.TagSetBuilder.getTagSet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import guitests.guihandles.EntryCardHandle;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import junit.framework.AssertionFailedError;
import seedu.multitasky.commons.core.index.Index;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.commons.util.FileUtil;
import seedu.multitasky.commons.util.XmlUtil;
import seedu.multitasky.model.entry.Deadline;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Event;
import seedu.multitasky.model.entry.FloatingTask;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.entry.ReadOnlyEntry;
import seedu.multitasky.model.entry.util.Comparators;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    public static final String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    public static final FloatingTask[] SAMPLE_FLOATING_TASK_ARRAY_DATA = getSampleFloatingTaskArrayData();

    public static void assertThrows(Class<? extends Throwable> expected, Runnable executable) {
        try {
            executable.run();
        } catch (Throwable actualException) {
            if (actualException.getClass().isAssignableFrom(expected)) {
                return;
            }
            String message = String.format("Expected thrown: %s, actual: %s", expected.getName(),
                                           actualException.getClass().getName());
            throw new AssertionFailedError(message);
        }
        throw new AssertionFailedError(
                                       String.format("Expected %s to be thrown, but nothing was thrown.",
                                                     expected.getName()));
    }

    // @@author A0126623L
    /**
     * @return FloatingTask[] of 10 sample elements.
     */
    private static FloatingTask[] getSampleFloatingTaskArrayData() {
        try {
            return new FloatingTask[] {
                new FloatingTask(new Name("Take lunch to work"), getTagSet()),
                new FloatingTask(new Name("Take dog for walk"), getTagSet()),
                new FloatingTask(new Name("Fill up cat food bowl"), getTagSet()),
                new FloatingTask(new Name("Write novel"), getTagSet()),
                new FloatingTask(new Name("Buy groceries"), getTagSet()),
                new FloatingTask(new Name("Refactor code"), getTagSet()),
                new FloatingTask(new Name("Write two more tasks"), getTagSet()),
                new FloatingTask(new Name("Import test cases"), getTagSet()),
                new FloatingTask(new Name("Scold Travis"), getTagSet()),
                new FloatingTask(new Name("Get dinner"), getTagSet())
            };
        } catch (IllegalValueException e) {
            fail("TestUtil floating task array generation failed.");
            return null;
        }
    }
    // @@author

    // @@author A0126623L
    /**
     * @return List&lt;FloatingTask&gt; of 10 sample elements.
     */
    public static List<FloatingTask> getSampleFloatingTaskListData() {
        return Arrays.asList(SAMPLE_FLOATING_TASK_ARRAY_DATA);
    }

    /**
     * Appends the file name to the sandbox folder path.
     * Creates the sandbox folder if it doesn't exist.
     *
     * @param fileName
     * @return
     */
    public static String getFilePathInSandboxFolder(String fileName) {
        try {
            FileUtil.createDirs(new File(SANDBOX_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER + fileName;
    }

    public static <T> void createDataFileWithData(T data, String filePath) {
        try {
            File saveFileForTesting = new File(filePath);
            FileUtil.createIfMissing(saveFileForTesting);
            XmlUtil.saveDataToFile(saveFileForTesting, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets mid point of a node relative to the screen.
     *
     * @param node
     * @return
     */
    public static Point2D getScreenMidPoint(Node node) {
        double x = getScreenPos(node).getMinX() + node.getLayoutBounds().getWidth() / 2;
        double y = getScreenPos(node).getMinY() + node.getLayoutBounds().getHeight() / 2;
        return new Point2D(x, y);
    }

    public static Bounds getScreenPos(Node node) {
        return node.localToScreen(node.getBoundsInLocal());
    }

    /**
     * Removes a subset from the list of entries.
     *
     * @param entries The list of entries
     * @param entriesToRemove The subset of entries.
     * @return The modified entries after removal of the subset from entries.
     */
    public static Entry[] removeEntriesFromList(final Entry[] entries, Entry... entriesToRemove) {
        List<Entry> listOfEntries = asList(entries);
        listOfEntries.removeAll(asList(entriesToRemove));
        return listOfEntries.toArray(new Entry[listOfEntries.size()]);
    }

    /**
     * Returns a copy of the list with the entry at specified index removed.
     *
     * @param list original list to copy from
     */
    public static Entry[] removeEntryFromList(final Entry[] list, Index index) {
        return removeEntriesFromList(list, list[index.getZeroBased()]);
    }

    /**
     * Appends entries to the array of entries.
     *
     * @param entries A array of entries.
     * @param entriesToAdd The entries that are to be appended behind the original array.
     * @return The modified array of entries.
     */
    public static Entry[] addEntriesToList(final Entry[] entries, Entry... entriesToAdd) {
        List<Entry> listOfEntries = asList(entries);
        listOfEntries.addAll(asList(entriesToAdd));
        return listOfEntries.toArray(new Entry[listOfEntries.size()]);
    }

    public static Entry[] addEntriesToSortedList(final Entry[] entries, Entry... entriesToAdd) {
        List<Entry> listOfEntries = asList(entries);
        listOfEntries.addAll(asList(entriesToAdd));
        if (entries instanceof Event[]) {
            Collections.sort(listOfEntries, Comparators.EVENT_DEFAULT);
        } else if (entries instanceof Deadline[]) {
            Collections.sort(listOfEntries, Comparators.DEADLINE_DEFAULT);
        } else if (entries instanceof FloatingTask[]) {
            Collections.sort(listOfEntries, Comparators.FLOATING_TASK_DEFAULT);
        } else {
            throw new AssertionError("Unknown entry array type");
        }
        return listOfEntries.toArray(new Entry[listOfEntries.size()]);
    }

    public static <T> List<T> asList(T[] objs) {
        List<T> list = new ArrayList<>();
        for (T obj : objs) {
            list.add(obj);
        }
        return list;
    }

    public static boolean compareCardAndEntry(EntryCardHandle card, ReadOnlyEntry entry) {
        return card.isSameEntry(entry);
    }

}
