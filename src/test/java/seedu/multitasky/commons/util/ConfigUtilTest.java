package seedu.multitasky.commons.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.multitasky.commons.core.Config;
import seedu.multitasky.commons.exceptions.DataConversionException;

public class ConfigUtilTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/ConfigUtilTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void read_null_throwsNullPointerException() throws DataConversionException {
        thrown.expect(NullPointerException.class);
        read(null);
    }

    @Test
    public void read_missingFile_emptyResult() throws DataConversionException {
        assertFalse(read("NonExistentFile.json").isPresent());
    }

    private Config getTypicalConfig() {
        Config config = new Config();
        config.setAppTitle("Typical App Title");
        config.setLogLevel(Level.INFO);
        config.setUserPrefsFilePath("C:\\preferences.json");
        return config;
    }

    private Optional<Config> read(String configFileInTestDataFolder) throws DataConversionException {
        String configFilePath = addToTestDataPathIfNotNull(configFileInTestDataFolder);
        return ConfigUtil.readConfig(configFilePath);
    }

    @Test
    public void save_nullConfig_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        save(null, "SomeFile.json");
    }

    @Test
    public void save_nullFile_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        save(new Config(), null);
    }

    @Test
    public void saveConfig_allInOrder_success() throws DataConversionException, IOException {
        Config original = getTypicalConfig();

        String configFilePath = testFolder.getRoot() + File.separator + "TempConfig.json";

        //Try writing when the file doesn't exist
        ConfigUtil.saveConfig(original, configFilePath);
        Config readBack = ConfigUtil.readConfig(configFilePath).get();
        assertEquals(original, readBack);

        //Try saving when the file exists
        original.setAppTitle("Updated Title");
        original.setLogLevel(Level.FINE);
        ConfigUtil.saveConfig(original, configFilePath);
        readBack = ConfigUtil.readConfig(configFilePath).get();
        assertEquals(original, readBack);
    }

    private void save(Config config, String configFileInTestDataFolder) throws IOException {
        String configFilePath = addToTestDataPathIfNotNull(configFileInTestDataFolder);
        ConfigUtil.saveConfig(config, configFilePath);
    }

    private String addToTestDataPathIfNotNull(String configFileInTestDataFolder) {
        return configFileInTestDataFolder != null
                                  ? TEST_DATA_FOLDER + configFileInTestDataFolder
                                  : null;
    }

}
