package org.cip4.tools.jdfeditor.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by s.meissner on 23.11.2015.
 */
public class BuildPropsUtil {

    private static final Log LOGGER = LogFactory.getLog(BuildPropsUtil.class);

    private static final String RES_BUILD_PROPS = "/org/cip4/tools/jdfeditor/build.properties";

    private static Properties props;


    /**
     * If necessary creates and returns the Properties object.
     * @return The current Properties object
     */
    private static Properties getProps() {
        if(props == null) {
            props = new Properties();

            try {
                props.load(BuildPropsUtil.class.getResourceAsStream(RES_BUILD_PROPS));
            } catch (IOException e) {
                LOGGER.warn("Error during reading build.properties file.", e);
            }
        }

        return props;
    }

    /**
     * Returns the applications name as String.
     * @return The applications name as String.
     */
    public static String getAppName() {
        return getProps().getProperty("name");
    }

    /**
     * Returns the applications version as String.
     * @return The applications version as String.
     */
    public static String getAppVersion() {
        return getProps().getProperty("version");
    }

    /**
     * Returns the applications build date as String.
     * @return The applications build date as String.
     */
    public static String getBuildDate() {
        return getProps().getProperty("build.date");
    }
}
