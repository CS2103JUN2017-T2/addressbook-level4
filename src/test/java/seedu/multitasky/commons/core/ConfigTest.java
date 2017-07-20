package seedu.multitasky.commons.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Objects;
import java.util.logging.Level;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConfigTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void config_defaultObjectToString_stringReturned() {
        String defaultConfigAsString = "App title : MultiTasky\n"
                + "Current log level : INFO\n"
                + "Preference file Location : preferences.json";

        assertEquals(defaultConfigAsString, new Config().toString());
    }

    @Test
    public void config_equalsMethod() {
        Config defaultConfig = new Config();
        assertNotNull(defaultConfig);
        assertTrue(defaultConfig.equals(defaultConfig));
        Config otherConfig = null;
        assertFalse(defaultConfig.equals(otherConfig));
        otherConfig = new Config();
        otherConfig.setAppTitle("MultiTasky 2.0");
        assertFalse(defaultConfig.equals(otherConfig));
        otherConfig.setAppTitle("MultiTasky");
        otherConfig.setLogLevel(Level.ALL);
        assertFalse(defaultConfig.equals(otherConfig));
        otherConfig.setLogLevel(Level.INFO);
        otherConfig.setUserPrefsFilePath("someotherfilepath.json");
        assertFalse(defaultConfig.equals(otherConfig));
    }

    @Test
    public void config_hashCode_matches() {
        int expectedHash = Objects.hash("MultiTasky", Level.INFO, "preferences.json");
        assertTrue(new Config().hashCode() == expectedHash);
    }
}
