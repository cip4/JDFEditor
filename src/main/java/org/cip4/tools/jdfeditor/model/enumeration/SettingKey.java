package org.cip4.tools.jdfeditor.model.enumeration;

import org.cip4.jdflib.core.JDFConstants;

import javax.swing.*;

/**
 * Enum of all setting keys.
 */
public enum SettingKey {

    FIND_PATTERN("find.pattern", null),
    FIND_CASE_SENSITIVE("find.case.sensitive", true),
    FIND_WRAP("find.warp", true),

    GENERAL_LANGUAGE("general.language", "en"),
    GENERAL_LOOK("general.look", UIManager.getSystemLookAndFeelClassName()),
    GENERAL_READ_ONLY("general.readonly", false),
    GENERAL_NORMALIZE("general.normalize", true),
    GENERAL_AUTO_VALIDATE("general.auto.validate", false),
    GENERAL_DISPLAY_DEFAULT("general.display.default", true),
    GENERAL_REMOVE_DEFAULT("general.remove.default", true),
    GENERAL_REMOVE_WHITE("general.remove.white", true),
    GENERAL_INDENT("general.indent", true),
    GENERAL_LONG_ID("general.long.id", false),
    GENERAL_UPDATE_JOBID("general.update.jobid", false),
    GENERAL_USE_SCHEMA("general.use.schema", false),

    GOLDENTICKET_MISURL("goldenticket.misurl", null),
    GOLDENTICKET_BASELEVEL("goldenticket.baselevel", 1),
    GOLDENTICKET_MISLEVEL("goldenticket.mislevel", 1),
    GOLDENTICKET_JMFLEVEL("goldenticket.jmflevel", 1),

    HTTP_STORE_PATH("http.store.path", "/var/tmp/JDFEditor/ReceivedMessages/"),

    LOGGING_LEVEL("logging.level", "INFO"),
    LOGGING_ENABLED("logging.enabled", true),

    RECENT_DEV_CAP("recent.dev.cap", null),

    SEND_METHOD("send.method", "MIME"),
    SEND_PACKAGE("send.package", true),
    SEND_JOB_INCREMENT("send.job.increment", 1),
    SEND_URL_SEND("send.url.send", "http://"),
    SEND_URL_RETURN("send.url.return", "http://"),

    TREEVIEW_ATTRIBUTE("treeview.attribute", true),
    TREEVIEW_ATTRIBUTE_INHERITED("treeview.attribute.inherited", true),

    VALIDATION_EXPORT("validation.export", false),
    VALIDATION_HIGHTLIGHT_FN("validation.highlight.fn", true),
    VALIDATION_SCHEMA_URL("validation.schema.url", null),
    VALIDATION_VERSION("validation.version", "1.5"),
    VALIDATION_LEVEL("validation.level", JDFConstants.VALIDATIONLEVEL_RECURSIVECOMPLETE),
    VALIDATION_IGNORE_DEFAULT("validation.ignore.default", true),
    VALIDATION_CHECK_URL("validation.check.url", false),
    VALIDATION_FIX_ICS_VERSION("validation.fix.ics.version", false),
    VALIDATION_CONVERT_LPP("validation.convert.lpp", false),
    VALIDATION_GENERATE_FULL("validation.generate.full", true),
    VALIDATION_GENERIC_ATTR("validation.generic.attr", initGenericAttr()),

    XJDF_CONVERT_SINGLENODE("xjdf.convert.singlenode", true),
    XJDF_CONVERT_STRIPPING("xjdf.convert.stripping", true),
    XJDF_CONVERT_SPAN("xjdf.convert.span", true),
    XJDF_CONVERT_RUNLIST("xjdf.convert.runlist", true),
    XJDF_CONVERT_LAYOUTPREP("xjdf.convert.layoutprep", true),
    XJDF_CONVERT_TILDE("xjdf.convert.tilde", true),
    XJDF_TYPESAFE_JMF("xjdf.typesave_jmf", true),
    XJDF_FROM_RETAIN_PRODUCT("xjdf.from.retain.product", false),
    XJDF_FROM_HEURISTIC_LINK("xjdf.from.heuristic.link", false);


    private final String key;

    private final String defaultValue;

    private final Class clazz;

    /**
     * Custom constructor. Accepting a key an a default value for initializing.
     *
     * @param key          The Setting key as String.
     * @param defaultValue The default value as String.
     */
    SettingKey(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.clazz = String.class;
    }

    /**
     * Custom constructor. Accepting a key an a default value for initializing.
     *
     * @param key          The Setting key as String.
     * @param defaultValue The default value as Boolean.
     */
    SettingKey(String key, boolean defaultValue) {
        this.key = key;
        this.defaultValue = Boolean.toString(defaultValue);
        this.clazz = Boolean.class;
    }

    /**
     * Custom constructor. Accepting a key an a default value for initializing.
     *
     * @param key          The Setting key as String.
     * @param defaultValue The default value as Integer.
     */
    SettingKey(String key, int defaultValue) {
        this.key = key;
        this.defaultValue = Integer.toString(defaultValue);
        this.clazz = Integer.class;
    }

    /**
     * Creates and returns the list of generic attributes.
     *
     * @return The list of generic attributes.
     */
    private static String initGenericAttr() {

        String result = "";

        String[] generics = new String[]{
                "ID", "Type", "JobID", "JobPartID", "ProductID",
                "CustomerID", "SpawnIDs", "Class", "Status", "PartIDKeys",
                "xmlns", "xmlns:xsi", "xsi:Type", "SettingsPolicy",
                "BestEffortExceptions", "OperatorInterventionExceptions",
                "MustHonorExceptions", "DocIndex", "Locked", "DescriptiveName",
                "Brand"
        };


        // build string
        for(int i = 0; i < generics.length; i ++) {
            result += generics[i] + " ";
        }

        result = result.trim();

        // return result
        return result;
    }

    /**
     * Returns the setting key as String.
     *
     * @return The setting key as String.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Returns the default value as String.
     *
     * @return The default value as String.
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Returns the class type as Class object.
     *
     * @return The class type as Class object.
     */
    public Class getClazz() {
        return this.clazz;
    }
}
