package seedu.multitasky.testutil;

import java.util.Calendar;
import java.util.GregorianCalendar;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.logic.commands.EditCommand.EditEntryDescriptor;
import seedu.multitasky.model.entry.Entry;
import seedu.multitasky.model.entry.Name;
import seedu.multitasky.model.util.EntryBuilder;

//@@author A0140633R
/**
 * Utility class containing the constants required for tests related to EditCommand
 */
public class EditCommandTestUtil {
    public static final String VALID_NAME_CLEAN = "Clean the house";
    public static final String VALID_NAME_MEETING = "Meet the boss";
    public static final String VALID_NAME_BOWLING = "go and play bowling";
    public static final String VALID_NAME_EXAM = "final exams";
    public static final String VALID_NAME_FULLDAY = "work for 24/7";
    public static final String VALID_TAG_URGENT = "urgent";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_DATE_12_JULY_17 = "12 july 17";
    public static final String VALID_DATE_TIME_12_JULY_6PM = "12 july 6pm";
    public static final String VALID_TIME_9PM = "9pm";
    public static final String VALID_DATE_TIME_12_JULY_9PM = "12 july 9pm";
    public static final String VALID_DATE_11_JULY_17 = "11 july 17";
    public static final String VALID_DATE_20_DEC_17 = "12 dec 17";
    public static final Calendar VALID_CALENDAR_1 = new GregorianCalendar(2020, Calendar.JANUARY, 25, 18, 00);
    public static final Calendar VALID_CALENDAR_2 = new GregorianCalendar(2020, Calendar.JANUARY, 25, 19, 00);
    public static final Calendar VALID_CALENDAR_3 = new GregorianCalendar(2030, Calendar.JANUARY, 25, 04, 00);
    public static final Calendar VALID_CALENDAR_4 = new GregorianCalendar(2030, Calendar.JANUARY, 25, 12, 00);
    public static final Calendar VALID_CALENDAR_4_1 = new GregorianCalendar(2030, Calendar.JANUARY, 25, 00, 00);
    public static final Calendar VALID_CALENDAR_4_2 = new GregorianCalendar(2030, Calendar.JANUARY, 25, 23, 59);
    public static final Calendar INVALID_CALENDAR_START = new GregorianCalendar(2030, Calendar.JANUARY, 25, 23, 59);
    public static final Calendar INVALID_CALENDAR_END = new GregorianCalendar(2030, Calendar.JANUARY, 24, 00, 00);

    public static final EditEntryDescriptor DESC_CLEAN;
    public static final EditEntryDescriptor DESC_MEETING;
    public static final Entry EVENT_BOWLING;
    public static final Entry EVENT_FULLDAY;
    public static final Entry EVENT_FULLDAY_CORRECTED;
    public static final Entry DEADLINE_EXAM;


    static {
        try {
            DESC_CLEAN = new EditEntryDescriptorBuilder().withName(VALID_NAME_CLEAN)
                                                         .withTags(VALID_TAG_FRIEND).build();
            DESC_MEETING = new EditEntryDescriptorBuilder().withName(VALID_NAME_MEETING)
                                                           .withTags(VALID_TAG_URGENT, VALID_TAG_FRIEND)
                                                           .build();

            EVENT_BOWLING = EntryBuilder.build(new Name(VALID_NAME_BOWLING),
                                               VALID_CALENDAR_1,
                                               VALID_CALENDAR_2,
                                               "with", "colleagues");

            DEADLINE_EXAM = EntryBuilder.build(new Name(VALID_NAME_EXAM),
                                               VALID_CALENDAR_1,
                                               "hard", "study");

            EVENT_FULLDAY = EntryBuilder.build(new Name(VALID_NAME_FULLDAY),
                                               VALID_CALENDAR_4,
                                               VALID_CALENDAR_4,
                                               "tags");

            EVENT_FULLDAY_CORRECTED = EntryBuilder.build(new Name(VALID_NAME_FULLDAY),
                                               VALID_CALENDAR_4_1,
                                               VALID_CALENDAR_4_2,
                                               "tags");

        } catch (IllegalValueException ive) {
            throw new AssertionError("Method should not fail.");
        }
    }
}
