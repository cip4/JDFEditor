package org.cip4.tools.jdfeditor.util;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by s.meissner on 23.11.2015.
 */
public class BuildPropsUtilTest {

    @Test
    public void testGetAppName() throws Exception {
        final String appName = BuildPropsUtil.getAppName();
        Assert.assertEquals("AppName is wrong.", "TEST_NAME", appName);
    }

    @Test
    public void testGetAppVersion() throws Exception {
        final String appVersion = BuildPropsUtil.getAppVersion();
        Assert.assertEquals("AppVersion is wrong.", "TEST_VERSION", appVersion);
    }

    @Test
    public void testGetBuildDate() throws Exception {
        final String buildDate = BuildPropsUtil.getBuildDate();
        Assert.assertEquals("BuildDate is wrong.", "TEST_DATE", buildDate);
    }
}