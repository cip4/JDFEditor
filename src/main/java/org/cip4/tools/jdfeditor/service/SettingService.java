package org.cip4.tools.jdfeditor.service;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.util.DirectoryUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * This services is responsible for all configuration settings stored in the external configuration file.
 */
@Service
@Scope(value = "singleton")
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
     * Get a typed setting value by key.
     * @param key The key of the setting value.
     * @param c The type of the setting value.
     * @return The typed setting value.
     */
    public <T> T getSetting(SettingKey key, Class<T> c) {

        Class clazz = key.getClazz();
        String value = config.getString(key.getKey(), key.getDefaultValue());

        if(Boolean.class.equals(clazz)) {
            return c.cast(Boolean.parseBoolean(value));

        } else if (String.class.equals(clazz)) {
            return c.cast(value);

        } else if (Integer.class.equals(clazz)) {
            return c.cast(Integer.parseInt(value));

        }

        LOGGER.warn("Wrong DataType for SettingKey " + key.getKey());
        return null;
    }

    /**
     * Set a setting value by a key.
     * @param key   The configuration key.
     * @param value The configuration setting value as String.
     */
    public void setSetting(SettingKey key, Object value) {

        Class clazz = key.getClazz();

        if(Boolean.class.equals(clazz)) {
            config.setProperty(key.getKey(), Boolean.toString((Boolean) value));

        } else if (String.class.equals(clazz)) {
            config.setProperty(key.getKey(), value);

        } else if (Integer.class.equals(clazz)) {
            config.setProperty(key.getKey(), Integer.toString((Integer) value));

        }
    }

    /**
     * Returns the path of the logfile.
     * @return Path to Logfile.
     */
    public File getConfFile() {
        return new File(FilenameUtils.concat(DirectoryUtil.getDirCIP4Tools(), confFileName));
    }
}
