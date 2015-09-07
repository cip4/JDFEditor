/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2015 The International Cooperation for the Integration of 
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
package org.cip4.tools.jdfeditor.service;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cip4.jdflib.util.StringUtil;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.util.DirectoryUtil;

/**
 * This services is responsible for all configuration settings stored in the external configuration file.
 */
public class SettingService
{

	private static final Log LOG = LogFactory.getLog(SettingService.class);

	private static String confFileName = "JDFEditor.conf";

	private static File configFile;

	private static PropertiesConfiguration config;

	private static SettingService theSettingService;

	public static SettingService getSettingService()
	{
		if (theSettingService == null)
		{
			theSettingService = new SettingService();
		}
		return theSettingService;
	}

	/**
	 * Default constructor.
	 */
	private SettingService()
	{
		config = initConfiguration();
	}

	/**
	 * Initialize logging
	 *
	 * @return New PropertiesConfiguration object.
	 */
	private PropertiesConfiguration initConfiguration()
	{

		// path config file
		configFile = new File(FilenameUtils.concat(DirectoryUtil.getDirCIP4Tools(), confFileName));
		LOG.info("Initialize settings from " + configFile.getAbsolutePath());

		// config settings
		PropertiesConfiguration config;

		try
		{
			config = new PropertiesConfiguration(configFile);
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
			config.setAutoSave(true);

		}
		catch (ConfigurationException e)
		{
			LOG.error("Error during initializing configuration service.", e);
			return null;
		}

		if (!configFile.exists())
		{

			// create config file
			for (SettingKey key : SettingKey.values())
			{
				config.addProperty(key.getKey(), key.getDefaultValue());
			}
		}

		// info log
		LOG.info("Initialize settings completed.");

		// return configuration
		return config;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBool(SettingKey key)
	{
		String value = config.getString(key.getKey(), key.getDefaultValue());
		return StringUtil.parseBoolean(value, false);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(SettingKey key)
	{
		String value = config.getString(key.getKey(), key.getDefaultValue());
		return StringUtil.parseInt(value, 0);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getString(SettingKey key)
	{
		String value = config.getString(key.getKey(), key.getDefaultValue());
		return value;
	}

	/**
	 * Get a typed setting value by key.
	 * @param key The key of the setting value.
	 * @param c The type of the setting value.
	 * @return The typed setting value.
	 */
	public <T> T getSetting(SettingKey key, Class<T> c)
	{

		Class<?> clazz = key.getClazz();
		String value = config.getString(key.getKey(), key.getDefaultValue());

		if (Boolean.class.equals(clazz))
		{
			return c.cast(Boolean.parseBoolean(value));

		}
		else if (String.class.equals(clazz))
		{
			return c.cast(value);

		}
		else if (Integer.class.equals(clazz))
		{
			return c.cast(Integer.parseInt(value));

		}

		LOG.warn("Wrong DataType for SettingKey " + key.getKey());
		return null;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void set(SettingKey key, boolean value)
	{
		config.setProperty(key.getKey(), value ? "true" : "false");
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void set(SettingKey key, int value)
	{
		config.setProperty(key.getKey(), "" + value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void set(SettingKey key, String value)
	{
		config.setProperty(key.getKey(), value);
	}

	/**
	 * Set a setting value by a key.
	 * @param key   The configuration key.
	 * @param value The configuration setting value as String.
	 */
	public void setSetting(SettingKey key, Object value)
	{

		Class<?> clazz = key.getClazz();

		if (Boolean.class.equals(clazz))
		{
			config.setProperty(key.getKey(), Boolean.toString((Boolean) value));

		}
		else if (String.class.equals(clazz))
		{
			config.setProperty(key.getKey(), value);

		}
		else if (Integer.class.equals(clazz))
		{
			config.setProperty(key.getKey(), Integer.toString((Integer) value));

		}
	}

	/**
	 * Returns the path of the logfile.
	 * @return Path to Logfile.
	 */
	public File getConfFile()
	{
		return configFile;
	}

	/**
	 * Used only in JUnits.
	 */
	protected static void clearStateForTesting()
	{
		theSettingService = null;
		configFile = null;
		config = null;
	}
}
