package org.cip4.tools.jdfeditor;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * JUnit test case for app.
 */
public class AppTest {

    @Test
    public void testGetBuildPropName() throws Exception {

        // arrange
        String expected = "CIP4 JDFEditor";

        Method method = App.class.getDeclaredMethod("getBuildProp", String.class);
        method.setAccessible(true);

        // act
        String result = (String) method.invoke(null, "name");

        // assert
        Assert.assertEquals("Name is wrong", expected, result);
    }

    @Test
    public void testGetBuildPropVersion() throws Exception {

        // arrange
        String expected = "2.4-SNAPSHOT";

        Method method = App.class.getDeclaredMethod("getBuildProp", String.class);
        method.setAccessible(true);

        // act
        String result = (String) method.invoke(null, "version");

        // assert
        Assert.assertEquals("Name is wrong", expected, result);
    }
}