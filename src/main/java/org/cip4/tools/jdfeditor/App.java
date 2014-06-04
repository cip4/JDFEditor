package org.cip4.tools.jdfeditor;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.cip4.tools.jdfeditor.controller.MainController;
import org.cip4.tools.jdfeditor.util.DirectoryUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Initialize and start JDFEditor Application.
 */

@Configuration
@ComponentScan
public class App {

    public static final String APP_NAME = getBuildProp("name");

    public static final String APP_VERSION = getBuildProp("version");

    public static final String APP_RELEASE_DATE = getBuildProp("build.date");

    private static final String RES_BUILD_PROPS = "/org/cip4/tools/jdfeditor/build.properties";

    /**
     * Application main entrance point.
     * @param args
     */
    public static void main(final String[] args)
    {
        // apple menu compatibility
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // init logging
        String logFile = FilenameUtils.concat(DirectoryUtil.getDirCIP4Tools(), "logs");
        logFile = FilenameUtils.concat(logFile, "JDFEditor.log");

        System.setProperty("filename", logFile);
        LogManager.getLogger(App.class).info("--- Start CIP4 JDFEditor ------------------------------ ");

        // handle command line arguments
        File file = null;
        // mac may have 2nd argument
        for (int i = args.length - 1; i >= 0; i--)
        {
            if (!args[i].startsWith("-"))
            {
                file = new File(args[i]);
                if (file.canRead())
                {
                    break;
                }
                file = null;
            }
        }

        // show main view
        ApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);
        MainController mainController = ctx.getBean(MainController.class);
        mainController.displayForm(file);
    }

    /**
     * Read and returns a build property by key.
     * @param key The key of the build property.
     * @return The value of the build property by key.
     */
    private static String getBuildProp(String key) {

        Properties props = new Properties();

        try {
            props.load(App.class.getResourceAsStream(RES_BUILD_PROPS));

        } catch (IOException e) {
            LogManager.getLogger(App.class).error("Error loading build properties.", e);
        }

        String value = props.getProperty(key);

        return value;
    }
}


