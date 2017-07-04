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
import seedu.address.model.entry.Name;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.ReadOnlyEntry;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    public static final String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    public static final Entry[] SAMPLE_PERSON_DATA = getSampleEntryData();

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

    private static Entry[] getSampleEntryData() {
        try {
            //CHECKSTYLE.OFF: LineLength
            return new Entry[]{
                new Entry(new Name("Ali Muster"), getTagSet()),
                new Entry(new Name("Boris Mueller"), getTagSet()),
                new Entry(new Name("Carl Kurz"), getTagSet()),
                new Entry(new Name("Daniel Meier"), getTagSet()),
                new Entry(new Name("Elle Meyer"), getTagSet()),
                new Entry(new Name("Fiona Kunz"), getTagSet()),
                new Entry(new Name("George Best"), getTagSet()),
                new Entry(new Name("Hoon Meier"), getTagSet()),
                new Entry(new Name("Ida Mueller"), getTagSet())
            };
            //CHECKSTYLE.ON: LineLength
        } catch (IllegalValueException e) {
            assert false;
            // not possible
            return null;
        }
    }

    public static List<Entry> generateSampleEntryData() {
        return Arrays.asList(SAMPLE_PERSON_DATA);
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
     * Removes a subset from the list of persons.
     * @param persons The list of persons
     * @param personsToRemove The subset of persons.
     * @return The modified persons after removal of the subset from persons.
     */
    public static Entry[] removeEntriesFromList(final Entry[] persons, Entry... personsToRemove) {
        List<Entry> listOfEntries = asList(persons);
        listOfEntries.removeAll(asList(personsToRemove));
        return listOfEntries.toArray(new Entry[listOfEntries.size()]);
    }


    /**
     * Returns a copy of the list with the person at specified index removed.
     * @param list original list to copy from
     */
    public static Entry[] removeEntryFromList(final Entry[] list, Index index) {
        return removeEntriesFromList(list, list[index.getZeroBased()]);
    }

    /**
     * Appends persons to the array of persons.
     * @param persons A array of persons.
     * @param personsToAdd The persons that are to be appended behind the original array.
     * @return The modified array of persons.
     */
    public static Entry[] addEntriesToList(final Entry[] persons, Entry... personsToAdd) {
        List<Entry> listOfEntries = asList(persons);
        listOfEntries.addAll(asList(personsToAdd));
        return listOfEntries.toArray(new Entry[listOfEntries.size()]);
    }

    private static <T> List<T> asList(T[] objs) {
        List<T> list = new ArrayList<>();
        for (T obj : objs) {
            list.add(obj);
        }
        return list;
    }

    public static boolean compareCardAndEntry(EntryCardHandle card, ReadOnlyEntry person) {
        return card.isSameEntry(person);
    }

}
