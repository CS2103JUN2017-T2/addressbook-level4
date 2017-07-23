package seedu.multitasky.storage.util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

//@@author A0132788U
/**
 * Unit test for StorageDateConverter exception
 */
public class StorageDateConverterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private StorageDateConverter storageDateConverter;

    @Before
    public void setUp() {
        storageDateConverter = new StorageDateConverter();
    }

    /***************************
     * Unit Test
     **************************/
    @Test
    public void convertStringToDate_error() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("Unable to set the time!");
        storageDateConverter.convertStringToDate("dummy");
    }

}
