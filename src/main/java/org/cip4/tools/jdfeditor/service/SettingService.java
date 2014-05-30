package org.cip4.tools.jdfeditor.service;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.util.DirectoryUtil;

import java.io.File;

/**
 * This services is responsible for all configuration settings stored in the external configuration file.
 */
public class SettingService {

    private static final Logger LOGGER = LogManager.getLogger(SettingService.class.getName());

    private static String confFileName = "JDFEditor.conf";

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
        File configFile = getConfFile();

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
     * Get a setting as String.
     *
     * @param key The setting key.
     * @return The setting value as String.
     */
    public String getString(SettingKey key) {

        return config.getString(key.getKey(), key.getDefaultValue());
    }

    /**
     * Retruns a setting as boolean.
     * @param key The setting key.
     * @return The Setting value as boolean.
     */
    public boolean getBoolean(SettingKey key) {

        String value = getString(key);
        return Boolean.parseBoolean(value);
    }

    /**
     * Retruns a setting as int.
     * @param key The setting key.
     * @return The Setting value as int.
     */
    public int getInteger(SettingKey key) {

        String value = getString(key);
        return Integer.parseInt(value);
    }

    /**
     * Set a setting string by a key.
     *
     * @param key   The configuration key.
     * @param value The configuration setting value as String.
     */
    public void setString(SettingKey key, String value) {

        config.setProperty(key.getKey(), value);
    }

    /**
     * Set a setting boolean by a key.
     *
     * @param key   The configuration key.
     * @param value The configuration setting value as Boolean.
     */
    public void setBoolean(SettingKey key, boolean value) {

        config.setProperty(key.getKey(), Boolean.toString(value));
    }

    /**
     * Set a setting boolean by a key.
     *
     * @param key   The configuration key.
     * @param value The configuration setting value as int.
     */
    public void setInteger(SettingKey key, int value) {

        config.setProperty(key.getKey(), Integer.toString(value));
    }

    /**
     * Returns the path of the logfile.
     * @return Path to Logfile.
     */
    public File getConfFile() {
        return new File(FilenameUtils.concat(DirectoryUtil.getDirCIP4Tools(), confFileName));
    }
}
