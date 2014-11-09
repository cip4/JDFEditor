package org.cip4.tools.jdfeditor.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.service.SettingService;

/**
 * Util class for Resource Bundle management.
 */
public class ResourceUtil
{

	private static final Log LOGGER = LogFactory.getLog(ResourceUtil.class);

	private static final ResourceBundle messageBundle = initMessageBundle();

	private static final String RES_MESSAGE_BUNDLE = "org.cip4.tools.jdfeditor.messages.JDFEditor";

	private static final String ICONS_PATH = "/org/cip4/tools/jdfeditor/icons/";

	/**
	 * Private constructor. This class cannot be instantiated.
	 */
	private ResourceUtil()
	{
	}

	/**
	 * Return a localized message by key.
	 * @param key Key of the message.
	 * @return Localized message as String.
	 */
	public static String getMessage(String key)
	{

		// return localized message
		return messageBundle.getString(key);
	}

	/**
	 * Returns a resource as ImageIcon object.
	 * @param resIcon Resource String of Icon.
	 * @return The ImageIcon object.
	 */
	public static ImageIcon getImageIcon(String resIcon)
	{
		// load icon
		ImageIcon imageIcon = null;
		InputStream is = ResourceUtil.class.getResourceAsStream(ICONS_PATH + resIcon);

		try
		{
			byte[] bytes = IOUtils.toByteArray(is);
			imageIcon = new ImageIcon(bytes);

		}
		catch (IOException e)
		{
			LOGGER.error("Error during loading ImageIcon", e);
		}

		// return icon
		return imageIcon;
	}

	/**
	 * Initializes and returns the localized message bundle.
	 * @return The Messages ResourceBundle object.
	 */
	private static ResourceBundle initMessageBundle()
	{

		// setup application language
		String language = SettingService.getSettingService().getSetting(SettingKey.GENERAL_LANGUAGE, String.class);
		final Locale currentLocale = new Locale(language, language.toUpperCase());
		Locale.setDefault(currentLocale);

		// init and return
		ResourceBundle messages = ResourceBundle.getBundle(RES_MESSAGE_BUNDLE, currentLocale);
		return messages;
	}
}
