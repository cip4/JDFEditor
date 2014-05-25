package org.cip4.tools.jdfeditor.service;

/**
 * Enum of all configuration keys.
 */
public enum SettingKey {

    GENERAL_LANGUAGE("general.language", "en"),
    LOGGING_LEVEL("logging.level", "INFO"),
    LOGGING_ENABLED("logging.enabled", "true");

    private final String key;
    private final String defaultValue;

    SettingKey(String key, String defaultValue) {
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
