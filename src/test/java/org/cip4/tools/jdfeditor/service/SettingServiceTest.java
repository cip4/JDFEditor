package org.cip4.tools.jdfeditor.service;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.cip4.tools.jdfeditor.model.enumeration.SettingKey;
import org.cip4.tools.jdfeditor.util.DirectoryUtil;
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

    private String CONF_FILE_NAME;

    private SettingService settingService;

    private File confFile;

    @Before
    public void setUp() throws Exception {

        // change logfile name
        CONF_FILE_NAME = "log.test";
        changeLogFileName(CONF_FILE_NAME);

        new File(FilenameUtils.concat(DirectoryUtil.getDirCIP4Tools(), CONF_FILE_NAME)).delete();

        // new service instance
        settingService = new SettingService();
        confFile = settingService.getConfFile();
    }

    @After
    public void tearDown() throws Exception {

        // clean up
        confFile.delete();
    }

    /**
     * Create a new setting file.
     * @throws Exception
     */
    @Test
    public void testCreateSettingFile() throws Exception {

        // arrange

        // act

        // assert
        Assert.assertEquals("Filename is wrong.", CONF_FILE_NAME, FilenameUtils.getName(settingService.getConfFile().getName()));
        Assert.assertTrue("LogFile does not exist.", confFile.exists());

        PropertiesConfiguration config = new PropertiesConfiguration(confFile);
        String language = (String) config.getProperty(SettingKey.GENERAL_LANGUAGE.getKey());

        Assert.assertEquals("Language attribute is wrong.", "en", language);
    }

    /**
     * Change existing attribute.
     * @throws Exception
     */
    @Test
    public void testChangeExistingAttribute() throws Exception {

        // arrange

        // act
        settingService.setSetting(SettingKey.GENERAL_LANGUAGE, "de");

        // assert
        File logFile = settingService.getConfFile();

        Assert.assertEquals("Filename is wrong.", CONF_FILE_NAME, FilenameUtils.getName(settingService.getConfFile().getName()));
        Assert.assertTrue("LogFile does not exist.", logFile.exists());

        PropertiesConfiguration config = new PropertiesConfiguration(logFile);
        String language = (String) config.getProperty(SettingKey.GENERAL_LANGUAGE.getKey());

        Assert.assertEquals("Language attribute is wrong.", "de", language);
    }

    /**
     * Change existing attribute after reloading service.
     * @throws Exception
     */
    @Test
    public void testChangeExistingAttributeReload() throws Exception {

        // arrange
        settingService.setSetting(SettingKey.GENERAL_LANGUAGE, "de");

        // act
        settingService = new SettingService();

        // assert
        Assert.assertEquals("Language attribute is wrong.", "de", settingService.getSetting(SettingKey.GENERAL_LANGUAGE, String.class));
    }

    /**
     * Simulate several service accesses.
     * @throws Exception
     */
    @Test
    public void testMultiThreading() throws Exception {

        // arrange

        // act
        settingService = new SettingService();
        settingService.setSetting(SettingKey.GENERAL_LANGUAGE, "tt");
        settingService = new SettingService();
        settingService.setSetting(SettingKey.LOGGING_ENABLED, true);
        settingService = new SettingService();
        settingService.setSetting(SettingKey.LOGGING_LEVEL, "yy");


        // assert
        File logFile = settingService.getConfFile();

        int lines = 0;
        BufferedReader br = new BufferedReader(new FileReader(logFile));
        while (br.readLine() != null) {
            lines++;
        }

        int cntKeys = 0;

        for(int i = 0; i < SettingKey.values().length; i ++) {
            if(SettingKey.values()[i].getDefaultValue() != null) {
                cntKeys ++;
            }
        }

        Assert.assertEquals("Number of lines is wrong.", cntKeys, lines);

        PropertiesConfiguration config = new PropertiesConfiguration(logFile);

        String language = (String) config.getProperty(SettingKey.GENERAL_LANGUAGE.getKey());
        Assert.assertEquals("Language attribute is wrong.", "tt", language);

        String logging_enabled = (String) config.getProperty(SettingKey.LOGGING_ENABLED.getKey());
        Assert.assertEquals("Language attribute is wrong.", "true", logging_enabled);

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
        PropertiesConfiguration config = new PropertiesConfiguration(settingService.getConfFile());
        config.clearProperty(SettingKey.GENERAL_LANGUAGE.getKey());
        config.save();

        Assert.assertFalse("Config file still contains language.", config.containsKey(SettingKey.GENERAL_LANGUAGE.getKey()));

        // act
        settingService = new SettingService();
        settingService.setSetting(SettingKey.GENERAL_LANGUAGE, "it");

        // assert
        File logFile = settingService.getConfFile();

        int lines = 0;
        BufferedReader br = new BufferedReader(new FileReader(logFile));
        while (br.readLine() != null) {
            lines++;
        }

        int cntKeys = 0;

        for(int i = 0; i < SettingKey.values().length; i ++) {
            if(SettingKey.values()[i].getDefaultValue() != null) {
                cntKeys ++;
            }
        }

        Assert.assertEquals("Number of lines is wrong.", cntKeys, lines);

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

        Field field = SettingService.class.getDeclaredField("confFileName");
        field.setAccessible(true);
        field.set(null, newName);
    }
}