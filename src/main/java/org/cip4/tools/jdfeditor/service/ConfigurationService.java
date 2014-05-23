package org.cip4.tools.jdfeditor.service;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.cip4.tools.jdfeditor.util.LocationUtil;

import java.io.File;

/**
 * This services is responsible for all configuration settings stored in the external configuration file.
 */
public class ConfigurationService {

    private static final Logger LOGGER = Logger.getLogger(ConfigurationService.class);

    private static final XMLConfiguration config = initConfiguration();

    public static final String KEY_TEST = "com.flyeralarm.test[@attribute]";

    /**
     * Enum of all configuration keys.
     */
    public enum Key {

        LOGGING_LEVEL("logging.level", "INFO"),
        LOGGING_ENABLED("logging.enabled", "true");

        private final String key;
        private final String defaultValue;

        Key(String key, String defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return this.key;
        }

        public String getDefaultValue() {
            return this.defaultValue;
        }
    }

    /**
     * Default constructor.
     */
    public ConfigurationService() {


        // config.getString(KEY_TEST, "defaultValue");
        config.addProperty(KEY_TEST, config.getString(KEY_TEST, "defaultValue"));


    }


    /**
     * Initialize logging
     *
     * @return New XMLConfiguration object.
     */
    private static XMLConfiguration initConfiguration() {

        LOGGER.info("Initialize configuration service...");

        // config settings
        XMLConfiguration config = new XMLConfiguration();
        config.setAutoSave(true);
        config.setReloadingStrategy(new FileChangedReloadingStrategy());

        // path config file
        String path = FilenameUtils.concat(LocationUtil.getDirCIP4Tools(), "JDFEditor.conf");
        File configFile = new File(path);


        if (configFile.exists()) {

            // load config file
            try {
                config.load(configFile);

            } catch (ConfigurationException e) {
                LOGGER.error("Error during initializing configuration service.", e);
            }
        } else {

            // create config file
            activateKey(config, Key.LOGGING_ENABLED);

            try {
                config.save(configFile);

            } catch (ConfigurationException e) {
                LOGGER.error("Error during initializing configuration service.", e);
            }
        }

        // info log
        LOGGER.info("Initialize configuration service completed.");

        // return configuration
        return config;
    }

    /**
     * Activate a configuration key for being part of the initial config file (...so that it can be edited by the user).
     *
     * @param key Key of setting to be activated.
     */
    private static void activateKey(Configuration config, Key key) {

        // load default value
        config.addProperty(key.getKey(), key.getDefaultValue());
    }

    /**
     * Get a configuration setting by a key.
     *
     * @param key The configuration key.
     * @return The configuration setting value as String.
     */
    public String getSetting(Key key) {
        return config.getString(key.getKey(), key.getDefaultValue());
    }

    /**
     * Set a configuration setting by a key.
     *
     * @param key   The configuration key.
     * @param value The configuration setting value as String.
     */
    public void setSetting(Key key, String value) {
        config.addProperty(key.getKey(), value);
    }
}
