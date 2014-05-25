package org.cip4.tools.jdfeditor.service;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;

/**
 * JUnit test case for SettingsService.
 */
public class SettingServiceTest {


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    /**
     * Create a new setting file.
     * @throws Exception
     */
    @Test
    public void testCreateSettingFile() throws Exception {

        // arrange
        String fileName = "log.test";
        changeLogFileName(fileName);


        // act
        SettingService service = new SettingService();

        // assert
        File logFile = service.getLogFile();

        Assert.assertEquals("Filename is wrong.", fileName, FilenameUtils.getName(service.getLogFile().getName()));
        Assert.assertTrue("LogFile does not exist.", logFile.exists());

        PropertiesConfiguration config = new PropertiesConfiguration(logFile);
        String language = (String) config.getProperty(SettingKey.GENERAL_LANGUAGE.getKey());

        Assert.assertEquals("Language attribute is wrong.", "en", language);

        logFile.delete();
    }

    /**
     * Change existing attribute.
     * @throws Exception
     */
    @Test
    public void testChangeExistingAttribute() throws Exception {

        // arrange
        String fileName = "log.test";
        changeLogFileName(fileName);

        // act
        SettingService service = new SettingService();
        service.setSetting(SettingKey.GENERAL_LANGUAGE, "de");

        // assert
        File logFile = service.getLogFile();

        Assert.assertEquals("Filename is wrong.", fileName, FilenameUtils.getName(service.getLogFile().getName()));
        Assert.assertTrue("LogFile does not exist.", logFile.exists());

        PropertiesConfiguration config = new PropertiesConfiguration(logFile);
        String language = (String) config.getProperty(SettingKey.GENERAL_LANGUAGE.getKey());

        Assert.assertEquals("Language attribute is wrong.", "de", language);

        logFile.delete();
    }

    /**
     * Change existing attribute after reloading service.
     * @throws Exception
     */
    @Test
    public void testChangeExistingAttributeReload() throws Exception {

        // arrange
        String fileName = "log.test";
        changeLogFileName(fileName);

        SettingService service = new SettingService();
        service.setSetting(SettingKey.GENERAL_LANGUAGE, "de");

        // act
        service = new SettingService();

        // assert
        Assert.assertEquals("Language attribute is wrong.", "de", service.getSetting(SettingKey.GENERAL_LANGUAGE));


        service.getLogFile().delete();
    }

    /**
     * Simulate several service accesses.
     * @throws Exception
     */
    @Test
    public void testMultiThreading() throws Exception {

        // arrange
        String fileName = "log.test";
        changeLogFileName(fileName);

        SettingService service;

        // act
        service = new SettingService();
        service.setSetting(SettingKey.GENERAL_LANGUAGE, "tt");
        service = new SettingService();
        service.setSetting(SettingKey.LOGGING_ENABLED, "dd");
        service = new SettingService();
        service.setSetting(SettingKey.LOGGING_LEVEL, "yy");


        // assert
        File logFile = service.getLogFile();

        int lines = 0;
        BufferedReader br = new BufferedReader(new FileReader(logFile));
        while (br.readLine() != null) {
            lines++;
        }

        Assert.assertEquals("Number of lines is wrong.", SettingKey.values().length, lines);

        PropertiesConfiguration config = new PropertiesConfiguration(logFile);

        String language = (String) config.getProperty(SettingKey.GENERAL_LANGUAGE.getKey());
        Assert.assertEquals("Language attribute is wrong.", "tt", language);

        String logging_enabled = (String) config.getProperty(SettingKey.LOGGING_ENABLED.getKey());
        Assert.assertEquals("Language attribute is wrong.", "dd", logging_enabled);

        String logging_level = (String) config.getProperty(SettingKey.LOGGING_LEVEL.getKey());
        Assert.assertEquals("Language attribute is wrong.", "yy", logging_level);

        logFile.delete();

    }

    /**
     * Change a missing attributes value.
     * @throws Exception
     */
    @Test
    public void testChangeMissingAttribute() throws Exception {

        // arrange
        String fileName = "log.test";
        changeLogFileName(fileName);

        SettingService service = new SettingService();

        PropertiesConfiguration config = new PropertiesConfiguration(service.getLogFile());
        config.clearProperty(SettingKey.GENERAL_LANGUAGE.getKey());
        config.save();

        Assert.assertFalse("Config file still contains language.", config.containsKey(SettingKey.GENERAL_LANGUAGE.getKey()));

        // act
        service = new SettingService();
        service.setSetting(SettingKey.GENERAL_LANGUAGE, "it");

        // assert
        File logFile = service.getLogFile();

        int lines = 0;
        BufferedReader br = new BufferedReader(new FileReader(logFile));
        while (br.readLine() != null) {
            lines++;
        }

        Assert.assertEquals("Number of lines is wrong.", SettingKey.values().length, lines);

        config = new PropertiesConfiguration(logFile);
        String language = (String) config.getProperty(SettingKey.GENERAL_LANGUAGE.getKey());

        Assert.assertEquals("Language attribute is wrong.", "it", language);

        logFile.delete();
    }

    /**
     * Change the name of the logfile
     *
     * @param newName New Filename of the logfile.
     * @throws Exception Is thrown in case an Exception has occured.
     */
    private void changeLogFileName(String newName) throws Exception {

        Field field = SettingService.class.getDeclaredField("logFileName");
        field.setAccessible(true);
        field.set(null, newName);
    }
}