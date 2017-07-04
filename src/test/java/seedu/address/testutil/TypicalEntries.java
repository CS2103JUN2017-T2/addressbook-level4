package seedu.address.testutil;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.EntryBook;
import seedu.address.model.entry.Entry;
import seedu.address.model.entry.exceptions.DuplicateEntryException;

/**
 *
 */
public class TypicalEntries {

    public static final Index INDEX_FIRST_ENTRY = Index.fromOneBased(1);
    public static final Index INDEX_SECOND_ENTRY = Index.fromOneBased(2);
    public static final Index INDEX_THIRD_ENTRY = Index.fromOneBased(3);

    public final Entry alice, benson, carl, daniel, elle, fiona, george, hoon, ida;

    public TypicalEntries() {
        try {
            alice = new EntryBuilder().withName("Alice Pauline")
                    .withTags("friends").build();
            benson = new EntryBuilder().withName("Benson Meier").withTags("owesMoney", "friends").build();
            carl = new EntryBuilder().withName("Carl Kurz").build();
            daniel = new EntryBuilder().withName("Daniel Meier").build();
            elle = new EntryBuilder().withName("Elle Meyer").build();
            fiona = new EntryBuilder().withName("Fiona Kunz").build();
            george = new EntryBuilder().withName("George Best").build();

            // Manually added
            hoon = new EntryBuilder().withName("Hoon Meier").build();
            ida = new EntryBuilder().withName("Ida Mueller").build();
        } catch (IllegalValueException e) {
            throw new AssertionError("Sample data cannot be invalid", e);
        }
    }

    public static void loadEntryBookWithSampleData(EntryBook eb) {
        for (Entry entry : new TypicalEntries().getTypicalEntries()) {
            try {
                eb.addEntry(new Entry(entry));
            } catch (DuplicateEntryException e) {
                assert false : "not possible";
            }
        }
    }

    public Entry[] getTypicalEntries() {
        return new Entry[]{alice, benson, carl, daniel, elle, fiona, george};
    }

    public EntryBook getTypicalEntryBook() {
        EntryBook eb = new EntryBook();
        loadEntryBookWithSampleData(eb);
        return eb;
    }
}
