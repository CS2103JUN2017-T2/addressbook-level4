package seedu.multitasky.model.tag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.multitasky.commons.core.UnmodifiableObservableList;
import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.tag.UniqueTagList.DuplicateTagException;

public class UniqueTagListTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**********************************
     * DuplicateTagException Expected *
     *********************************/
    // @@author A0125586X
    @Test
    public void uniqueTagList_duplicateTagString_duplicateTagException() throws Exception {
        thrown.expect(DuplicateTagException.class);
        new UniqueTagList("tag", "tag");
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_duplicateTag_duplicateTagException() throws Exception {
        thrown.expect(DuplicateTagException.class);
        new UniqueTagList(new Tag("tag"), new Tag("tag"));
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_duplicateTagCollection_duplicateTagException() throws Exception {
        List<Tag> tags = Arrays.asList(new Tag[] {new Tag("tag1"), new Tag("tag1")});
        thrown.expect(DuplicateTagException.class);
        new UniqueTagList(tags);
    }

    /**********************************
     * IllegalValueException Expected *
     *********************************/
    // @@author A0125586X
    @Test
    public void uniqueTagList_illegalNameString_illegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        new UniqueTagList("$tag");
    }

    /***************
     * Constructor *
     **************/
    // @@author A0125586X
    @Test
    public void uniqueTagList_constructorUniqueStrings_success() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        assertContains(list, "tag1", "tag2");
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_constructorUniqueTags_success() throws Exception {
        UniqueTagList list = new UniqueTagList(new Tag("tag1"), new Tag("tag2"));
        assertContains(list, "tag1", "tag2");
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_constructorUniqueTagCollection_success() throws Exception {
        List<Tag> tags = Arrays.asList(new Tag[] {new Tag("tag1"), new Tag("tag2")});
        UniqueTagList list = new UniqueTagList(tags);
        assertContains(list, "tag1", "tag2");
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_constructorUniqueTagSet_success() throws Exception {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("tag1"));
        tags.add(new Tag("tag2"));
        UniqueTagList list = new UniqueTagList(tags);
        assertContains(list, "tag1", "tag2");
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_constructorUniqueTagList_success() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        UniqueTagList list2 = new UniqueTagList(list);
        assertContains(list, "tag1", "tag2");
        assertContains(list2, "tag1", "tag2");
    }

    /*****************
     * Other methods *
     ****************/
    // @@author A0125586X
    @Test
    public void uniqueTagList_toSet_matches() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        Set<Tag> tags = list.toSet();
        assertTrue(tags.contains(new Tag("tag1")));
        assertTrue(tags.contains(new Tag("tag2")));
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_setTag_set() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        UniqueTagList list2 = new UniqueTagList("tag3", "tag4");
        list.setTags(list2);
        assertContains(list2, "tag3", "tag4");
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_setTagDuplicateCollection_duplicateTagException() throws Exception {
        List<Tag> tags = Arrays.asList(new Tag[] {new Tag("tag3"), new Tag("tag3")});
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        thrown.expect(DuplicateTagException.class);
        list.setTags(tags);
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_setTagUniqueCollection_success() throws Exception {
        List<Tag> tags = Arrays.asList(new Tag[] {new Tag("tag3"), new Tag("tag4")});
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        list.setTags(tags);
        assertContains(list, "tag3", "tag4");
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_mergeFrom_success() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        UniqueTagList list2 = new UniqueTagList("tag3", "tag4");
        list.mergeFrom(list2);
        assertContains(list, "tag1", "tag2", "tag3", "tag4");
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_addDuplicate_duplicateTagException() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        thrown.expect(DuplicateTagException.class);
        list.add(new Tag("tag1"));
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_addUnique_success() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        list.add(new Tag("tag3"));
        assertContains(list, "tag1", "tag2", "tag3");
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_iterator_access() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1");
        for (Tag tag : list) {
            assertTrue(tag.equals(new Tag("tag1")));
        }
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_asObservableList_matches() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        UnmodifiableObservableList<Tag> observableList = list.asObservableList();
        assertTrue(observableList.contains(new Tag("tag1")));
        assertTrue(observableList.contains(new Tag("tag2")));
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_equals_false() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        UniqueTagList list2 = null;
        assertFalse(list.equals(list2));
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_equals_true() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        assertTrue(list.equals(list));
        UniqueTagList list2 = new UniqueTagList("tag1", "tag2");
        assertTrue(list.equals(list2));
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_equalsOrderInsensitive_true() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        assertTrue(list.equalsOrderInsensitive(list));
        UniqueTagList list2 = new UniqueTagList("tag2", "tag1");
        assertFalse(list.equals(list2));
        assertTrue(list.equalsOrderInsensitive(list2));
    }

    // @@author A0125586X
    @Test
    public void uniqueTagList_hashCode_correct() throws Exception {
        UniqueTagList list = new UniqueTagList("tag1", "tag2");
        ObservableList<Tag> internalList = FXCollections.observableArrayList();
        internalList.add(new Tag("tag1"));
        internalList.add(new Tag("tag2"));
        int expectedHashCode = internalList.hashCode();
        assertTrue(list.hashCode() == expectedHashCode);
    }

    /******************
     * Helper methods *
     *****************/
    // @@author A0125586X
    private void assertContains(UniqueTagList list, String... tags) throws Exception {
        for (String tag : tags) {
            assertContains(list, tag);
        }
    }

    // @@author A0125586X
    private void assertContains(UniqueTagList list, String tag) throws Exception {
        assertTrue(list.contains(new Tag(tag)));
    }

}
