package seedu.multitasky.commons.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

// @@author A0125586X
/**
 * Uses int as E for most of the tests
 */
public class UnmodifiableObservableListTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private class MyInteger {
        private int value;
        public MyInteger(int value) {
            this.value = value;
        }
        public void setValue(int value) {
            this.value = value;
        }
        public boolean equals(MyInteger other) {
            return this == other
                || (other != null && this.value == other.value);
        }
    }

    private class BasicConsumer implements Consumer<MyInteger> {
        @Override
        public void accept(MyInteger integer) {
            integer.setValue(0);
        }
    }

    private class NonComparable {
        private int value;
        public NonComparable(int value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return new StringBuilder().append(value).toString();
        }
        public int intValue() {
            return value;
        }
    }

    private class AscendingComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer integer1, Integer integer2) {
            return integer1.intValue() - integer2.intValue();
        }
    }

    private class EvenPredicate implements Predicate<Integer> {
        @Override
        public boolean test(Integer integer) {
            return integer.intValue() % 2 == 0;
        }
    }

    private class BasicListChangeListener implements ListChangeListener<Integer> {
        @Override
        public void onChanged(Change<? extends Integer> c) {
        }
    }

    private class BasicInvalidationListener implements InvalidationListener {
        @Override
        public void invalidated(Observable arg0) {
        }
    }

    /*********************************
     * NullPointerException Expected *
     ********************************/
    @Test
    public void unmodifiableObservableList_constructorNullArgument_nullPointerException() {
        ObservableList<Integer> backingList = null;
        thrown.expect(NullPointerException.class);
        new UnmodifiableObservableList<Integer>(backingList);
    }

    /***************
     * Constructor *
     **************/
    @Test
    public void unmodifiableObservableList_constructor_success() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        assertContains(list, 1, 2);
    }

    /*****************
     * Other Methods *
     ****************/
    @Test
    public void unmodifiableObservableList_addRemoveListChangeListener_success() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        BasicListChangeListener listener = new BasicListChangeListener();
        list.addListener(listener);
        list.removeListener(listener);
    }

    @Test
    public void unmodifiableObservableList_addInvalidationListener_success() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        BasicInvalidationListener listener = new BasicInvalidationListener();
        list.addListener(listener);
        list.removeListener(listener);
    }

    @Test
    public void unmodifiableObservableList_addListChangeListener_success() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        list.addListener(new BasicInvalidationListener());
    }

    @Test
    public void unmodifiableObservableList_addAllElement_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList()).addAll(new Integer(1));
    }

    @Test
    public void unmodifiableObservableList_addAllCollection_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .addAll(Arrays.asList(new Integer[]{new Integer(1)}));
    }

    @Test
    public void unmodifiableObservableList_addAllIndexCollection_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .addAll(0, Arrays.asList(new Integer[]{new Integer(1)}));
    }

    @Test
    public void unmodifiableObservableList_setAllElement_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList()).setAll(new Integer(1));
    }

    @Test
    public void unmodifiableObservableList_setAllCollection_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .setAll(Arrays.asList(new Integer[]{new Integer(1)}));
    }

    @Test
    public void unmodifiableObservableList_removeAllElement_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .removeAll(new Integer(1));
    }

    @Test
    public void unmodifiableObservableList_removeAllCollection_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .removeAll(Arrays.asList(new Integer[]{new Integer(1)}));
    }

    @Test
    public void unmodifiableObservableList_retainAllElement_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .retainAll(new Integer(1));
    }

    @Test
    public void unmodifiableObservableList_retainAllCollection_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .retainAll(Arrays.asList(new Integer[]{new Integer(1)}));
    }

    @Test
    public void unmodifiableObservableList_removeFromTo_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .remove(0, 1);
    }

    @Test
    public void unmodifiableObservableList_removeObject_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .remove(new Integer(1));
    }

    @Test
    public void unmodifiableObservableList_removeIndex_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .remove(0);
    }

    @Test
    public void unmodifiableObservableList_add_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .add(new Integer(1));
    }

    @Test
    public void unmodifiableObservableList_addIndex_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .add(0, new Integer(1));
    }

    @Test
    public void unmodifiableObservableList_replaceAll_unsupportedOperationException() {
        class BasicUnaryOperator implements UnaryOperator<Integer> {
            public Integer apply(Integer integer) {
                return integer;
            }
        }
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .replaceAll(new BasicUnaryOperator());
    }

    @Test
    public void unmodifiableObservableList_sort_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .sort(new AscendingComparator());
    }

    @Test
    public void unmodifiableObservableList_clear_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList()).clear();
    }

    @Test
    public void unmodifiableObservableList_set_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .set(0, new Integer(1));
    }

    @Test
    public void unmodifiableObservableList_removeIf_unsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        new UnmodifiableObservableList<Integer>(FXCollections.observableArrayList())
                .removeIf(new EvenPredicate());
    }

    @Test
    public void unmodifiableObservableList_filtered_correct() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        backingList.add(new Integer(3));
        backingList.add(new Integer(4));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        assertContains(list, 1, 2, 3, 4);
        FilteredList<Integer> filteredList = list.filtered(new EvenPredicate());
        assertTrue(filteredList.contains(new Integer(2)));
        assertTrue(filteredList.contains(new Integer(4)));
    }

    @Test
    public void unmodifiableObservableList_sortedPredicate_correct() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(4));
        backingList.add(new Integer(3));
        backingList.add(new Integer(2));
        backingList.add(new Integer(1));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        assertContains(list, 1, 2, 3, 4);
        SortedList<Integer> sortedList = list.sorted(new AscendingComparator());
        assertTrue(sortedList.get(0).intValue() == 1);
        assertTrue(sortedList.get(1).intValue() == 2);
        assertTrue(sortedList.get(2).intValue() == 3);
        assertTrue(sortedList.get(3).intValue() == 4);
    }

    @Test
    public void unmodifiableObservableList_sortedComparable_correct() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(4));
        backingList.add(new Integer(3));
        backingList.add(new Integer(2));
        backingList.add(new Integer(1));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        assertContains(list, 1, 2, 3, 4);
        SortedList<Integer> sortedList = list.sorted();
        assertTrue(sortedList.get(0).intValue() == 1);
        assertTrue(sortedList.get(1).intValue() == 2);
        assertTrue(sortedList.get(2).intValue() == 3);
        assertTrue(sortedList.get(3).intValue() == 4);
    }

    @Test
    public void unmodifiableObservableList_sortedNonComparable_correct() {
        ObservableList<NonComparable> backingList = FXCollections.observableArrayList();
        backingList.add(new NonComparable(4));
        backingList.add(new NonComparable(3));
        backingList.add(new NonComparable(2));
        backingList.add(new NonComparable(1));
        UnmodifiableObservableList<NonComparable> list = new UnmodifiableObservableList<NonComparable>(backingList);
        SortedList<NonComparable> sortedList = list.sorted();
        assertTrue(sortedList.get(0).intValue() == 1);
        assertTrue(sortedList.get(1).intValue() == 2);
        assertTrue(sortedList.get(2).intValue() == 3);
        assertTrue(sortedList.get(3).intValue() == 4);
    }

    @Test
    public void unmodifiableObservableList_isEmpty_correct() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        assertTrue(list.isEmpty());
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        list = new UnmodifiableObservableList<Integer>(backingList);
        assertFalse(list.isEmpty());
    }

    @Test
    public void unmodifiableObservableList_iteratorRemove_unsupportedOperationException() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        Iterator<Integer> iter = list.iterator();
        thrown.expect(UnsupportedOperationException.class);
        iter.remove();
    }

    @Test
    public void unmodifiableObservableList_iteratorForEachRemaining_modified() {
        ObservableList<MyInteger> backingList = FXCollections.observableArrayList();
        backingList.add(new MyInteger(1));
        backingList.add(new MyInteger(2));
        backingList.add(new MyInteger(3));
        UnmodifiableObservableList<MyInteger> list = new UnmodifiableObservableList<MyInteger>(backingList);
        Iterator<MyInteger> iter = list.iterator();
        iter.forEachRemaining(new BasicConsumer());
        iter = list.iterator();
        assertTrue(iter.next().equals(new MyInteger(0)));
        assertTrue(iter.next().equals(new MyInteger(0)));
        assertTrue(iter.next().equals(new MyInteger(0)));
    }

    @Test
    public void unmodifiableObservableList_toArray_correct() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        Object[] array = list.toArray();
        assertTrue(((Integer) array[0]).intValue() == 1);
        assertTrue(((Integer) array[1]).intValue() == 2);
        Integer[] array2 = list.toArray(new Integer[2]);
        assertTrue(array2[0].intValue() == 1);
        assertTrue(array2[1].intValue() == 2);
    }

    @Test
    public void unmodifiableObservableList_containsAll_true() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        List<Integer> expectedList = Arrays.asList(new Integer[] {new Integer(1), new Integer(2)});
        List<Integer> unexpectedList = Arrays.asList(new Integer[] {new Integer(3)});
        assertTrue(list.containsAll(expectedList));
        assertFalse(list.containsAll(unexpectedList));
    }

    @Test
    public void unmodifiableObservableList_equals_correct() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        assertTrue(list.equals(list));
        UnmodifiableObservableList<Integer> list2 = new UnmodifiableObservableList<Integer>(backingList);
        assertTrue(list.equals(list2));
        ObservableList<Integer> backingList2 = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        backingList.add(new Integer(3));
        list2 = new UnmodifiableObservableList<Integer>(backingList2);
        assertFalse(list.equals(list2));
    }

    @Test
    public void unmodifiableObservableList_hashCode_correct() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        int expectedHashCode = backingList.hashCode();
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        assertTrue(list.hashCode() == expectedHashCode);
    }

    @Test
    public void unmodifiableObservableList_indexOf_correct() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        assertTrue(list.indexOf(new Integer(1)) == 0);
        assertTrue(list.indexOf(new Integer(2)) == 1);
    }

    @Test
    public void unmodifiableObservableList_lastIndexOf_correct() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        backingList.add(new Integer(2));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        assertTrue(list.lastIndexOf(new Integer(1)) == 0);
        assertTrue(list.lastIndexOf(new Integer(2)) == 2);
    }

    @Test
    public void unmodifiableObservableList_iterator_correct() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        backingList.add(new Integer(3));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        ListIterator<Integer> iter = list.listIterator();
        assertTrue(iter.hasNext());
        assertTrue(iter.nextIndex() == 0);
        assertTrue(iter.next().equals(new Integer(1)));
        assertTrue(iter.hasNext());
        assertTrue(iter.next().equals(new Integer(2)));
        assertTrue(iter.hasNext());
        assertTrue(iter.next().equals(new Integer(3)));
        assertFalse(iter.hasNext());
        assertTrue(iter.previousIndex() == 2);

        assertTrue(iter.hasPrevious());
        assertTrue(iter.previous().equals(new Integer(3)));
        assertTrue(iter.hasPrevious());
        assertTrue(iter.previous().equals(new Integer(2)));
        assertTrue(iter.hasPrevious());
        assertTrue(iter.previous().equals(new Integer(1)));
        assertFalse(iter.hasPrevious());
    }

    @Test
    public void unmodifiableObservableList_iteratorForEach_applied() {
        ObservableList<MyInteger> backingList = FXCollections.observableArrayList();
        backingList.add(new MyInteger(1));
        backingList.add(new MyInteger(2));
        backingList.add(new MyInteger(3));
        UnmodifiableObservableList<MyInteger> list = new UnmodifiableObservableList<MyInteger>(backingList);
        ListIterator<MyInteger> iter = list.listIterator();
        iter.forEachRemaining(new BasicConsumer());
        iter = list.listIterator();
        assertTrue(iter.next().equals(new MyInteger(0)));
        assertTrue(iter.next().equals(new MyInteger(0)));
        assertTrue(iter.next().equals(new MyInteger(0)));
    }

    @Test
    public void unmodifiableObservableList_iteratorUnsupportedOperation_unsupportedOperationException() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        backingList.add(new Integer(3));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        ListIterator<Integer> iter = list.listIterator();
        boolean thrown = false;
        try {
            iter.remove();
        } catch (UnsupportedOperationException e) {
            thrown = true;
        }
        if (!thrown) {
            fail("UnsupportedOperationException should have been thrown");
        }

        thrown = false;
        try {
            iter.set(new Integer(3));
        } catch (UnsupportedOperationException e) {
            thrown = true;
        }
        if (!thrown) {
            fail("UnsupportedOperationException should have been thrown");
        }

        thrown = false;
        try {
            iter.add(new Integer(3));
        } catch (UnsupportedOperationException e) {
            thrown = true;
        }
        if (!thrown) {
            fail("UnsupportedOperationException should have been thrown");
        }
    }

    @Test
    public void unmodifiableObservableList_subList_matches() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        backingList.add(new Integer(3));
        List<Integer> expectedSubList = Arrays.asList(new Integer[] {new Integer(1), new Integer(2)});
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        assertTrue(list.subList(0, 2).equals(expectedSubList));
    }

    @Test
    public void unmodifiableObservableList_forEach_applied() {
        ObservableList<MyInteger> backingList = FXCollections.observableArrayList();
        backingList.add(new MyInteger(1));
        backingList.add(new MyInteger(2));
        backingList.add(new MyInteger(3));
        UnmodifiableObservableList<MyInteger> list = new UnmodifiableObservableList<MyInteger>(backingList);
        list.forEach(new BasicConsumer());
        assertTrue(list.get(0).equals(new MyInteger(0)));
        assertTrue(list.get(1).equals(new MyInteger(0)));
        assertTrue(list.get(2).equals(new MyInteger(0)));
    }

    @Test
    public void unmodifiableObservableList_stream_correct() {
        ObservableList<Integer> backingList = FXCollections.observableArrayList();
        backingList.add(new Integer(1));
        backingList.add(new Integer(2));
        backingList.add(new Integer(3));
        UnmodifiableObservableList<Integer> list = new UnmodifiableObservableList<Integer>(backingList);
        StringBuilder builder = new StringBuilder();
        list.stream().forEach(s -> builder.append(s.intValue()));
        assertTrue(builder.toString().equals("123"));
    }

    /******************
     * Helper Methods *
     *****************/
    private void assertContains(UnmodifiableObservableList<Integer> list, int... integers) {
        for (int integer : integers) {
            assertContains(list, integer);
        }
    }

    private void assertContains(UnmodifiableObservableList<Integer> list, int integer) {
        assertTrue(list.contains(new Integer(integer)));
    }

}
