package org.cip4.tools.jdfeditor.service;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.cip4.tools.jdfeditor.util.LocationUtil;

import java.io.File;

/**
 * This services is responsible for all configuration settings stored in the external configuration file.
 */
public class SettingService {

    private static final Logger LOGGER = Logger.getLogger(SettingService.class);

    private static String logFileName = "JDFEditor.conf";

    private final PropertiesConfiguration config;

    /**
     * Default constructor.
     */
    public SettingService() {

        // init
        config = initConfiguration();
    }

    /**
     * Initialize logging
     *
     * @return New XMLConfiguration object.
     */
    private PropertiesConfiguration initConfiguration() {

        LOGGER.info("Initialize settings...");

        // path config file
        File configFile = getLogFile();

        // config settings
        PropertiesConfiguration config;

        try {
            config = new PropertiesConfiguration(configFile);
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
            config.setAutoSave(true);

        } catch (ConfigurationException e) {
            LOGGER.error("Error during initializing configuration service.", e);
            return null;
        }

        if (!configFile.exists()) {

            // create config file
            for(SettingKey key: SettingKey.values()) {
                config.addProperty(key.getKey(), key.getDefaultValue());
            }
        }

        // info log
        LOGGER.info("Initialize settings completed.");

        // return configuration
        return config;
    }

    /**
     * Get a configuration setting by a key.
     *
     * @param key The configuration key.
     * @return The configuration setting value as String.
     */
    public String getSetting(SettingKey key) {

        return config.getString(key.getKey(), key.getDefaultValue());
    }

    /**
     * Set a configuration setting by a key.
     *
     * @param key   The configuration key.
     * @param value The configuration setting value as String.
     */
    public void setSetting(SettingKey key, String value) {

        config.setProperty(key.getKey(), value);
    }

    /**
     * Returns the path of the logfile.
     * @return Path to Logfile.
     */
    public File getLogFile() {
        return new File(FilenameUtils.concat(LocationUtil.getDirCIP4Tools(), logFileName));
    }
}
