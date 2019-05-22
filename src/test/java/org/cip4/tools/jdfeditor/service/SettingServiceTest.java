package org.cip4.tools.jdfeditor.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;

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

	private final String CONF_FILE_NAME = "conf.test";

	private SettingService settingService;

	private File confFile;

	@Before
	public void setUp() throws Exception
	{
		changeFileName(CONF_FILE_NAME);

		// new service instance
		settingService.clearStateForTesting();
		settingService = SettingService.getSettingService();
		confFile = settingService.getConfFile();
	}

	@After
	public void tearDown() throws Exception
	{
		confFile.delete();
	}

	@Test
	public void shouldReturnExistingFile() throws Exception
	{
		Assert.assertTrue("File does not exist", confFile.exists());
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

		// reload
		settingService.clearStateForTesting();
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

	/**
	 * Change the name of the file
	 *
	 * @param fileName New Filename.
	 * @throws Exception Is thrown in case an Exception has occurred.
	 */
	private void changeFileName(final String fileName) throws Exception
	{
		final Field field = SettingService.class.getDeclaredField("confFileName");
		field.setAccessible(true);
		field.set(null, fileName);
	}
}