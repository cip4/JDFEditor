package org.cip4.tools.jdfeditor.model.enumeration;

/**
 * Enum of all configuration keys.
 */
public enum SettingKey {

    GENERAL_LANGUAGE("general.language", "en"),
    GENERAL_READ_ONLY("general.readonly", "false"),
    GENERAL_NORMALIZE("general.normalize", "true"),
    GENERAL_AUTO_VALIDATE("general.auto.validate", "false"),
    GENERAL_DISPLAY_DEFAULT("general.display.default", "true"),
    GENERAL_REMOVE_DEFAULT("general.remove.default", "true"),
    GENERAL_REMOVE_WHITE("general.remove.white", "true"),
    GENERAL_INDENT("general.indent", "true"),
    GENERAL_LONGID("general.longid", "false"),
    GENERAL_USE_SCHEMA("general.use.schema", "false"),
    LOGGING_LEVEL("logging.level", "INFO"),
    LOGGING_ENABLED("logging.enabled", "true"),
    XJDF_CONVERT_SINGLENODE("xjdf.convert.singlenode", "true"),
    XJDF_CONVERT_STRIPPING("xjdf.convert.stripping","true"),
    XJDF_CONVERT_SPAN("xjdf.convert.span", "true"),
    XJDF_CONVERT_RUNLIST("xjdf.convert.runlist", "true"),
    XJDF_CONVERT_LAYOUTPREP("xjdf.convert.layoutprep", "true"),
    XJDF_CONVERT_TILDE("xjdf.convert.tilde", "true"),
    GOLDENTICKET_MISURL("goldenticket.misurl", null),
    GOLDENTICKET_BASELEVEL("goldenticket.baselevel", "1"),
    GOLDENTICKET_MISLEVEL("goldenticket.mislevel", "1"),
    GOLDENTICKET_JMFLEVEL("goldenticket.jmflevel", "1");


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
