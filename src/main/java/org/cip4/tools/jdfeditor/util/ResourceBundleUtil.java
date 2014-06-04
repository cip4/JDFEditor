package org.cip4.tools.jdfeditor.util;

import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Util class for Resource Bundle management.
 */
public class ResourceBundleUtil {

    private static final ResourceBundle messageBundle = initMessageBundle();

    private static final String RES_MESSAGE_BUNDLE = "org.cip4.tools.jdfeditor.messages.JDFEditor";


    /**
     * Return a localized message by key.
     * @param key Key of the message.
     * @return Localized message as String.
     */
    public static String getMessage(String key) {

        // return localized message
        return messageBundle.getString(key);
    }

    /**
     * Initializes and returns the localized message bundle.
     * @return The Messages ResourceBundle object.
     */
    private static ResourceBundle initMessageBundle() {

        // setup application language
        String language = new SettingService().getSetting(SettingKey.GENERAL_LANGUAGE, String.class);
        final Locale currentLocale = new Locale(language, language.toUpperCase());
        Locale.setDefault(currentLocale);

        // init and return
        ResourceBundle messages = ResourceBundle.getBundle(RES_MESSAGE_BUNDLE, currentLocale);
        return messages;
    }
}
