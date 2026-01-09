/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2026 The International Cooperation for the Integration of
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test case for SettingsService.
 */
public class SettingServiceTest
{

	private final String CONF_FILE_NAME = "JDFEditor.conf";

	private SettingService settingService;

	private File confFile;

	@Before
	public void setUp() throws Exception
	{

		settingService = SettingService.getSettingService();
		confFile = settingService.getConfFile();
		confFile.delete();
		settingService = SettingService.getSettingService();
	}

	@After
	public void tearDown() throws Exception
	{
		confFile.delete();
	}

	@Test
	public void shouldReturnExpectedFileName() throws Exception
	{
		Assert.assertEquals("Filename is wrong", CONF_FILE_NAME, FilenameUtils.getName(settingService.getConfFile().getName()));
	}

	/**
	 * Change existing attribute.
	 *
	 * @throws Exception
	 */
	@Test
	public void testChangeExistingAttribute() throws Exception
	{
		settingService.setSetting(SettingKey.GENERAL_LANGUAGE, "de");

		final String language = settingService.getSetting(SettingKey.GENERAL_LANGUAGE, String.class);
		Assert.assertEquals("Language attribute is wrong", "de", language);
	}

	/**
	 * Change existing attribute after reloading service.
	 *
	 * @throws Exception
	 */
	@Test
	public void testChangeExistingAttributeReload() throws Exception
	{
		settingService.setSetting(SettingKey.GENERAL_LANGUAGE, "de");
		settingService = SettingService.getSettingService();

		final String language = settingService.getSetting(SettingKey.GENERAL_LANGUAGE, String.class);
		Assert.assertEquals("Language attribute is wrong", "de", language);
	}

	/**
	 * Simulate several service accesses.
	 *
	 * @throws Exception
	 */
	@Test
	public void testMultiThreading() throws Exception
	{

		// arrange

		// act
		settingService = SettingService.getSettingService();
		settingService.setSetting(SettingKey.GENERAL_LANGUAGE, "tt");
		settingService.setSetting(SettingKey.LOGGING_ENABLED, true);
		settingService.setSetting(SettingKey.LOGGING_LEVEL, "yy");

		// assert
		final File logFile = settingService.getConfFile();

		int lines = 0;
		final BufferedReader br = new BufferedReader(new FileReader(logFile));
		while (br.readLine() != null)
		{
			lines++;
		}

		int cntKeys = 0;

		for (int i = 0; i < SettingKey.values().length; i++)
		{
			if (SettingKey.values()[i].getDefaultValue() != null)
			{
				cntKeys++;
			}
		}

		Assert.assertEquals("Number of lines is wrong.", cntKeys, lines);

		final PropertiesConfiguration config = new PropertiesConfiguration(logFile);

		final String language = (String) config.getProperty(SettingKey.GENERAL_LANGUAGE.getKey());
		Assert.assertEquals("Language attribute is wrong.", "tt", language);

		final String logging_enabled = (String) config.getProperty(SettingKey.LOGGING_ENABLED.getKey());
		Assert.assertEquals("Language attribute is wrong.", "true", logging_enabled);

		final String logging_level = (String) config.getProperty(SettingKey.LOGGING_LEVEL.getKey());
		Assert.assertEquals("Language attribute is wrong.", "yy", logging_level);
	}

	/**
	 * Change a missing attributes value.
	 *
	 * @throws Exception
	 */
	@Test
	public void testChangeMissingAttribute() throws Exception
	{

		// arrange
		PropertiesConfiguration config = new PropertiesConfiguration(settingService.getConfFile());
		config.clearProperty(SettingKey.GENERAL_LANGUAGE.getKey());
		config.save();

		Assert.assertFalse("Config file still contains language.", config.containsKey(SettingKey.GENERAL_LANGUAGE.getKey()));

		// act
		settingService = SettingService.getSettingService();
		settingService.setSetting(SettingKey.GENERAL_LANGUAGE, "it");

		// assert
		final File logFile = settingService.getConfFile();

		int lines = 0;
		final BufferedReader br = new BufferedReader(new FileReader(logFile));
		while (br.readLine() != null)
		{
			lines++;
		}

		int cntKeys = 0;

		for (int i = 0; i < SettingKey.values().length; i++)
		{
			if (SettingKey.values()[i].getDefaultValue() != null)
			{
				cntKeys++;
			}
		}

		Assert.assertEquals("Number of lines is wrong.", cntKeys, lines);

		config = new PropertiesConfiguration(logFile);
		final String language = (String) config.getProperty(SettingKey.GENERAL_LANGUAGE.getKey());

		Assert.assertEquals("Language attribute is wrong.", "it", language);
	}

}