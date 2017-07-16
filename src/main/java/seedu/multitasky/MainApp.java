package seedu.multitasky;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.multitasky.commons.core.Config;
import seedu.multitasky.commons.core.EventsCenter;
import seedu.multitasky.commons.core.LogsCenter;
import seedu.multitasky.commons.core.Version;
import seedu.multitasky.commons.events.model.EntryBookChangedEvent;
import seedu.multitasky.commons.events.ui.EntryAppRequestEvent;
import seedu.multitasky.commons.events.ui.ExitAppRequestEvent;
import seedu.multitasky.commons.exceptions.DataConversionException;
import seedu.multitasky.commons.util.ConfigUtil;
import seedu.multitasky.commons.util.StringUtil;
import seedu.multitasky.logic.Logic;
import seedu.multitasky.logic.LogicManager;
import seedu.multitasky.model.EntryBook;
import seedu.multitasky.model.Model;
import seedu.multitasky.model.ModelManager;
import seedu.multitasky.model.ReadOnlyEntryBook;
import seedu.multitasky.model.UserPrefs;
import seedu.multitasky.storage.EntryBookStorage;
import seedu.multitasky.storage.JsonUserPrefsStorage;
import seedu.multitasky.storage.Storage;
import seedu.multitasky.storage.StorageManager;
import seedu.multitasky.storage.UserPrefsStorage;
import seedu.multitasky.storage.XmlEntryBookStorage;
import seedu.multitasky.ui.Ui;
import seedu.multitasky.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 0, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected UserPrefs userPrefs;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing EntryBook ]===========================");
        super.init();

        config = initConfig(getApplicationParameter("config"));

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        userPrefs = initPrefs(userPrefsStorage);
        EntryBookStorage entryBookStorage = new XmlEntryBookStorage(userPrefs.getEntryBookFilePath());
        storage = new StorageManager(entryBookStorage, userPrefsStorage, userPrefs);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model);

        ui = new UiManager(logic, config, userPrefs);

        initEventsCenter();
    }

    private String getApplicationParameter(String parameterName) {
        Map<String, String> applicationParameters = getParameters().getNamed();
        return applicationParameters.get(parameterName);
    }

    private Model initModelManager(Storage storage, UserPrefs userPrefs) {
        Optional<ReadOnlyEntryBook> entryBookOptional;
        ReadOnlyEntryBook initialData;
        // @@author A0132788U
        /**
         * Deletes snapshot files from previous run, then loads the EntryBook/creates a new EntryBook
         * if it doesn't exist.
         */
        EntryAppRequestEvent event = new EntryAppRequestEvent();
        try {
            entryBookOptional = storage.readEntryBook();
            if (!entryBookOptional.isPresent()) {
                event.deleteAllSnapshotFiles();
                logger.info("Data file not found. Will be starting with an empty EntryBook");
                initialData = new EntryBook();
                storage.handleEntryBookChangedEvent(new EntryBookChangedEvent(initialData));
            } else {
                event.deleteAllSnapshotFiles();
                initialData = entryBookOptional.get();
                storage.handleEntryBookChangedEvent(new EntryBookChangedEvent(initialData));
            }
            // @@author
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty EntryBook");
            initialData = new EntryBook();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty EntryBook");
            initialData = new EntryBook();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    protected Config initConfig(String configFilePath) {
        Config initializedConfig;
        String configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                           + "Using default config properties");
            initializedConfig = new Config();
        }

        // Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        String prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                           + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty EntryBook");
            initializedPrefs = new UserPrefs();
        }

        // Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting EntryBook " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping EntryBook ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
