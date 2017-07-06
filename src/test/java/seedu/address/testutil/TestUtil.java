package seedu.address.testutil;

import static seedu.address.model.util.SampleDataUtil.getTagSet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import guitests.guihandles.EntryCardHandle;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import junit.framework.AssertionFailedError;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.Name;
import seedu.address.model.entry.ReadOnlyEntry;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    public static final String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    public static final Entry[] SAMPLE_ENTRY_DATA = getSampleEntryData();

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
                String.format("Expected %s to be thrown, but nothing was thrown.", expected.getName()));
    }

    //@@author A0125586X
    private static Entry[] getSampleEntryData() {
        try {
            return new Entry[] {
                new Entry(new Name("Take lunch to work"), getTagSet()),
                new Entry(new Name("Take dog for walk"), getTagSet()),
                new Entry(new Name("Fill up cat food bowl"), getTagSet()),
                new Entry(new Name("Write novel"), getTagSet()),
                new Entry(new Name("Buy groceries"), getTagSet()),
                new Entry(new Name("Refactor code"), getTagSet()),
                new Entry(new Name("Write two more tasks"), getTagSet()),
                new Entry(new Name("Import test cases"), getTagSet()),
                new Entry(new Name("Scold Travis"), getTagSet())
            };
        } catch (IllegalValueException e) {
            assert false;
            // not possible to make array
            return null;
        }
    }
    //@@author

    public static List<Entry> generateSampleEntryData() {
        return Arrays.asList(SAMPLE_ENTRY_DATA);
    }

    /**
     * Appends the file name to the sandbox folder path.
     * Creates the sandbox folder if it doesn't exist.
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
     * @param list original list to copy from
     */
    public static Entry[] removeEntryFromList(final Entry[] list, Index index) {
        return removeEntriesFromList(list, list[index.getZeroBased()]);
    }

    /**
     * Appends entries to the array of entries.
     * @param entries A array of entries.
     * @param entriesToAdd The entries that are to be appended behind the original array.
     * @return The modified array of entries.
     */
    public static Entry[] addEntriessToList(final Entry[] entries, Entry... entriesToAdd) {
        List<Entry> listOfEntries = asList(entries);
        listOfEntries.addAll(asList(entriesToAdd));
        return listOfEntries.toArray(new Entry[listOfEntries.size()]);
    }

    private static <T> List<T> asList(T[] objs) {
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
