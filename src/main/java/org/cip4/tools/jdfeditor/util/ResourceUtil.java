/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2024 The International Cooperation for the Integration of 
 * Processes in  Prepress, Press and Postpress (CIP4).  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        The International Cooperation for the Integration of 
 *        Processes in  Prepress, Press and Postpress (www.cip4.org)"
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "CIP4" and "The International Cooperation for the Integration of 
 *    Processes in  Prepress, Press and Postpress" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact info@cip4.org.
 *
 * 5. Products derived from this software may not be called "CIP4",
 *    nor may "CIP4" appear in their name, without prior written
 *    permission of the CIP4 organization
 *
 * Usage of this software in commercial products is subject to restrictions. For
 * details please consult info@cip4.org.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE INTERNATIONAL COOPERATION FOR
 * THE INTEGRATION OF PROCESSES IN PREPRESS, PRESS AND POSTPRESS OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the The International Cooperation for the Integration 
 * of Processes in Prepress, Press and Postpress and was
 * originally based on software 
 * copyright (c) 1999-2001, Heidelberger Druckmaschinen AG 
 * copyright (c) 1999-2001, Agfa-Gevaert N.V. 
 *  
 * For more information on The International Cooperation for the 
 * Integration of Processes in  Prepress, Press and Postpress , please see
 * <http://www.cip4.org/>.
 *  
 * 
 */
package org.cip4.tools.jdfeditor.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;
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

	private static ResourceBundle messageBundle = initMessageBundle();

	private static final String RES_MESSAGE_BUNDLE = "org.cip4.tools.jdfeditor.messages.JDFEditor";
	private static final String ICONS_PATH = "/org/cip4/tools/jdfeditor/icons/";
	private static ResourceUtil instance;

	static
	{
		instance = new ResourceUtil();
	}

	/**
	 * Private constructor. This class cannot be instantiated.
	 */
	private ResourceUtil()
	{

	}

	public static ResourceUtil getInstance()
	{
		return instance;
	}

	/**
	 * Return a localized message by key.
	 * 
	 * @param key Key of the message.
	 * @return Localized message as String.
	 */
	public static String getMessage(final String key)
	{
		String result = "undefined";
		try
		{
			result = messageBundle.getString(key);
		}
		catch (final MissingResourceException e)
		{
			result = "?" + key + "?";
			LOGGER.error("Error, no key found, key: " + key + ", return result: " + result, e);
		}
		return result;
	}

	/**
	 * Returns a resource as ImageIcon object.
	 * 
	 * @param imageName Resource String of Icon.
	 * @return The ImageIcon object.
	 */
	public static ImageIcon getImageIcon(final String imageName)
	{
		ImageIcon imageIcon = null;
		final InputStream is = ResourceUtil.class.getResourceAsStream(ICONS_PATH + imageName);

		try
		{
			if (is == null)
				throw new IOException("Image not existing: " + imageName);

			final byte[] bytes = IOUtils.toByteArray(is);
			imageIcon = new ImageIcon(bytes);
		}
		catch (final IOException e)
		{
			LOGGER.error("Error during loading image: " + imageName, e);
		}

		return imageIcon;
	}

	/**
	 * Initializes and returns the localized message bundle.
	 * 
	 * @return The Messages ResourceBundle object.
	 */
	private static ResourceBundle initMessageBundle()
	{

		// setup application language
		final String language = SettingService.getSettingService().getSetting(SettingKey.GENERAL_LANGUAGE, String.class);
		final Locale currentLocale = new Locale(language, language.toUpperCase());
		Locale.setDefault(currentLocale);

		// init and return
		final ResourceBundle messages = ResourceBundle.getBundle(RES_MESSAGE_BUNDLE, currentLocale);
		return messages;
	}
}
