package seedu.multitasky.storage;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import seedu.multitasky.commons.exceptions.IllegalValueException;
import seedu.multitasky.model.tag.Tag;

//@@author A0132788U
/**
 * Confirms that the XmlAdaptedTag methods are working as required.
 */
public class XmlAdaptedTagTest {

    private static final String tagName = "project";
    private static XmlAdaptedTag tag;

    public XmlAdaptedTagTest() throws IllegalValueException {
        tag = new XmlAdaptedTag(new Tag(tagName));
    }

    /***************************
     * Illegal tag test *
     **************************/
    @Test
    public void testXmlAdaptedTag_nameEquality() throws IllegalValueException {
        XmlAdaptedTag newTag = new XmlAdaptedTag(new Tag("CS2103"));
        assertFalse(tag.equals(newTag));
    }

    /***************************
     * Illegal toModelType test *
     **************************/
    @Test
    public void testXmlAdaptedTag_notEqualToTag() throws IllegalValueException {
        assertFalse(tag.equals(tag.toModelType()));
    }

}
